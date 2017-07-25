package main;

import db_services.DbService;
import entitys.User;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import servlets.*;

import java.math.BigDecimal;

public class Main {
    private static final Logger log = Logger.getLogger(Main.class);
    public static void main(String[] args) throws Exception {
        User user1 = new User(0001l);
        user1.setFirstName("user01");
       // user1.setLocalWallet(new BigDecimal("0.01"));
        User user2 = new User(0002l);
        user2.setFirstName("user02");
        //user2.setLocalWallet(new BigDecimal("0.01"));
        User user3 = new User(0003l);
        user3.setFirstName("user03");
        //user3.setLocalWallet(new BigDecimal("0.01"));
        User user4 = new User(0004l);
        user4.setFirstName("user04");

        user1.setLevel(0);
        user2.setLevel(1);
        user3.setLevel(2);
        user4.setLevel(3);

        user1.setLeftKey(1);
        user2.setLeftKey(2);
        user3.setLeftKey(3);
        user4.setLeftKey(4);

        user4.setRightKey(5);
        user3.setRightKey(6);
        user2.setRightKey(7);
        user1.setRightKey(8);

        DbService.getInstance().addUserInDb(user1);
        DbService.getInstance().addUserInDb(user2);
        DbService.getInstance().addUserInDb(user3);
        DbService.getInstance().addUserInDb(user4);

        Server server = new Server(80);
        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.addServlet(RootServlet.class,"/");
        contextHandler.addServlet(SendToAdvcashServlet.class,"/redirectToAdvcash");
        contextHandler.addServlet(SuccessfulPayServlet.class,"/successful");
        contextHandler.addServlet(UnsuccessfulPayServlet.class,"/unsuccessful");
        contextHandler.addServlet(StatusPayServlet.class,"/status");
        server.setHandler(contextHandler);
        server.start();
        log.info("*******Server started*********");
        server.join();
    }
}
