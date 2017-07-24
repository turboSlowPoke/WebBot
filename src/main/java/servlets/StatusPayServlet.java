package servlets;

import configs.TypeOfPurchase;
import db_services.DbService;
import entitys.AdvcashTransaction;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class StatusPayServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("doGet start ...");
        Map<String,String[]> params = req.getParameterMap();
        System.out.println(params.size());
        String ac_src_wallet = params.get("ac_src_wallet")[0];
        String ac_dest_wallet = params.get("ac_dest_wallet")[0];
        BigDecimal ac_amount = new BigDecimal(params.get("ac_amount")[0]);
        BigDecimal ac_merchant_amount = new BigDecimal(params.get("ac_merchant_amount")[0]);
        String ac_merchant_currency = params.get("ac_merchant_currency")[0];
        BigDecimal ac_fee = new BigDecimal(params.get("ac_fee")[0]);
        BigDecimal ac_buyer_amount_without_commission = new BigDecimal(params.get("ac_buyer_amount_without_commission")[0]);
        BigDecimal ac_buyer_amount_with_commission = new BigDecimal(params.get("ac_buyer_amount_with_commission")[0]);
        String ac_buyer_currency = params.get("ac_buyer_currency")[0];
        String ac_transfer = params.get("ac_transfer")[0];
        String ac_sci_name = params.get("ac_sci_name")[0];
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ac_start_date = LocalDateTime.parse(params.get("ac_start_date")[0],formatter);
        String ac_order_id =params.get("ac_order_id")[0];
        String ac_ps = params.get("ac_ps")[0];
        String ac_transaction_status =params.get("ac_transaction_status")[0];
        String ac_buyer_email = params.get("ac_buyer_email")[0];
        Boolean ac_buyer_verified = Boolean.parseBoolean(params.get("ac_buyer_verified")[0]);
        String ac_comments = params.get("ac_comments")[0];
        String ac_hash = params.get("ac_hash")[0];

        AdvcashTransaction transaction = new AdvcashTransaction(
                ac_src_wallet,
                ac_dest_wallet,
                ac_amount,
                ac_merchant_amount,
                ac_merchant_currency,
                ac_fee,
                ac_buyer_amount_without_commission,
                ac_buyer_amount_with_commission,
                ac_buyer_currency,
                ac_transfer,
                ac_sci_name,
                ac_start_date,
                ac_order_id,
                ac_ps,
                ac_transaction_status,
                ac_buyer_email,
                ac_buyer_verified,
                ac_comments,
                ac_hash
                );
        System.out.println("транзакция создана");
        Long userId = Long.parseLong(ac_order_id.substring(0,ac_order_id.indexOf("_")));
        System.out.println("пользователь " +userId);
        String typeOfParchase = ac_order_id.substring(ac_order_id.indexOf("_")+1,ac_order_id.indexOf("-"));
        System.out.println("операция " + typeOfParchase);
        if (typeOfParchase.equals(TypeOfPurchase.ONE_MONTH)
                ||typeOfParchase.equals(TypeOfPurchase.TWO_MONTH)
                ||typeOfParchase.equals(TypeOfPurchase.THREE_MONTH)) {

            if (DbService.getInstance().checkUser(userId)) {
                DbService.getInstance().transactionHandler(userId, transaction, typeOfParchase);
                resp.setStatus(HttpServletResponse.SC_OK);
                System.out.println("транзакия обработана");
            } else {
                System.out.println("!! в базе нет пользователя " + userId);
            }
        } else {
            System.out.println("неизвстная покупка " +typeOfParchase);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }
}
