package servlets;

import db_services.DbService;
import db_services.NoUserInDbException;
import entitys.User;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import templayter.PageGenerator;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;



public class SendToAdvcashServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(SendToAdvcashServlet.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String,String[]> params = req.getParameterMap();
        if (params!=null&params.size()==2) {
            Long userId = Long.parseLong(params.get("userId")[0]);
            String typeOfParchase = params.get("typeOfParchase")[0];
            String ac_amount = "0";
            String ac_comments ="";
            User user=null;
            try {
                user=DbService.getInstance().getUserFromDb(userId);
            } catch (NoUserInDbException e) {
                log.error("no in db userid="+userId);
                log.trace(e);
            }
            if (user!=null){
                switch (typeOfParchase){
                    case "oneMonth":
                        ac_amount = "6.00";
                        ac_comments = " bot подписка один месяц";
                        break;
                    case "twoMonth":
                        ac_amount ="7";
                        ac_comments = "bot подписка два месяца";
                        break;
                    case "threeMonth" :
                        ac_amount = "8";
                        ac_comments = "bot подписка три месяца";
                        break;
                    case "oneTimeConsultation":
                        ac_amount = "6";
                        ac_comments = "bot персональная консультация";
                        break;
                    case "unlimit":
                        ac_amount = "10";
                        ac_comments = "bot безлимитная подписка";
                        break;
                    default:
                        log.error("Не известный тип платежа "+typeOfParchase);
                }
                LocalDateTime dateTime = LocalDateTime.now();
                String stringDateTime = "" + dateTime.getYear() + dateTime.getMonthValue() + dateTime.getDayOfMonth() + dateTime.getHour() + dateTime.getMinute();
                String ac_order_id = ""+userId+"_"+typeOfParchase+"-"+stringDateTime;
                String ac_sign="";
                /*String sign = "mega_pokemon@mail.ru:testBot:"+ac_amount+":ac_currency:MegaP0kem0n:"+ac_order_id;
                String key = "MegaP0kem0n";
                String ac_sign="";
                try {
                    Mac sha256 = Mac.getInstance("HmacSHA256");
                    SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(),"HmacSHA256");
                    sha256.init(secretKey);
                    ac_sign = Base64.encodeBase64String(sha256.doFinal(sign.getBytes()));

                } catch (NoSuchAlgorithmException e) {
                    log.error(" не смог зашифровать");
                    log.trace(e);
                } catch (InvalidKeyException e) {
                    log.error(" не смог зашифровать");
                    log.trace(e);
                }
                */
                Map<String, String> dataMap = new HashMap<>();
                dataMap.put("ac_order_id",ac_order_id);
                dataMap.put("ac_amount",ac_amount);
                dataMap.put("ac_comments", ac_comments);
                dataMap.put("ac_sign",ac_sign);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentType("text/html;charset=UTF-8");
                resp.getWriter().println(PageGenerator.instance().getStaticPage("Advcash_Form.html", dataMap));
                log.info("userId="+userId+" отправил запрос на плату "+typeOfParchase);
            }
            else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("такого пользователя нет в базе");
            }
        }else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }
}
