package servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginServlets extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null) {
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            if (username.equals("user01") && password.equals("123456")) {
                HttpSession newSession = req.getSession();
                newSession.setAttribute("username", "user01");
                resp.sendRedirect("/");
            }
        }
    }
}
