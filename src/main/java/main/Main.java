package main;

import db_services.DbService;
import entitys.User;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import servlets.*;

public class Main {
    public static void main(String[] args) throws Exception {
        User user = new User(1234567890l);
        user.setFirstName("user01");
        DbService.getInstance().addUserInDb(user);
        Server server = new Server(80);
        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.addServlet(RootServlet.class,"/");
        contextHandler.addServlet(SendToAdvcashServlet.class,"/redirectToAdvcash");
        contextHandler.addServlet(SuccessfulPayServlet.class,"/successful");
        contextHandler.addServlet(UnsuccessfulPayServlet.class,"/unsuccessful");
        contextHandler.addServlet(StatusPayServlet.class,"/status");
        server.setHandler(contextHandler);
        server.start();
        server.join();
    }
}
