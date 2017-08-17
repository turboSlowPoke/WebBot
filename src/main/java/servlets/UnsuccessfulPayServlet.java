package servlets;

import org.apache.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class UnsuccessfulPayServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(UnsuccessfulPayServlet.class);
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        File file = new File("../resources/main/webcontent/failpage.html");
        Document doc = Jsoup.parse(file,"UTF-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().write(doc.html());
        StringBuilder stringBuilder = new StringBuilder();
        Map<String,String[]> map = req.getParameterMap();
        for (Map.Entry<String,String[]> entry : map.entrySet()){
            stringBuilder.append(entry.getKey()).append(":").append(entry.getValue()[0]).append("\n");
        }
        log.info("Отказ платежа:  "+stringBuilder.toString());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.error("вызван метод doGet");
        doPost(req,resp);
    }
}
