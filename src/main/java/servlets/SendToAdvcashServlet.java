package servlets;

import db_services.DbService;
import templayter.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class SendToAdvcashServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String,String[]> params = req.getParameterMap();
        if (params!=null&params.size()==2) {
            Long userId = Long.parseLong(params.get("userId")[0]);
            String typeOfParchase = params.get("typeOfParchase")[0];
            String ac_amount = "0";
            String ac_comments ="";
            if (DbService.getInstance().checkUser(userId)){
                switch (typeOfParchase){
                    case "oneMonth":
                        ac_amount = "6.00";
                        ac_comments = " bot подписка один месяц";
                        break;
                    case "twoMonth":
                        ac_amount ="10.00";
                        ac_comments = "bot подписка два месяца";
                        break;
                    case "threeMonth" :
                        ac_amount = "15.00";
                        ac_comments = "bot подписка три месяца";
                        break;
                    default:
                        System.out.println("Проблема в SendToAdvcashServlet, оператор switch");
                }
                LocalDateTime dateTime = LocalDateTime.now();
                String stringDateTime = "" + dateTime.getYear() + dateTime.getMonthValue() + dateTime.getDayOfMonth() + dateTime.getHour() + dateTime.getMinute();
                Map<String, String> dataMap = new HashMap<>();
                dataMap.put("stringUserId", userId.toString());
                dataMap.put("typeOfParchase", typeOfParchase);
                dataMap.put("dateTime", stringDateTime);
                dataMap.put("ac_amount",ac_amount);
                dataMap.put("ac_comments", ac_comments);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentType("text/html;charset=UTF-8");
                resp.getWriter().println(PageGenerator.instance().getStaticPage("Advcash_Form.html", dataMap));
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
