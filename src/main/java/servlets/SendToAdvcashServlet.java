package servlets;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class SendToAdvcashServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String,String[]> params = req.getParameterMap();
        String stringUserId = params.get("userId")[0];
        String typeOfParchase = params.get("typeOfParchase")[0];



        File file = new File("../resources/main/templates/Advcash_Form.html");
        Document doc = Jsoup.parse(file,"UTF-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().write(doc.html());
    }
}
