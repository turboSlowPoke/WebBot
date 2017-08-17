package main;

import db_services.DbService;
import entitys.User;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.*;

import java.math.BigDecimal;

public class Main {
    private static final Logger log = Logger.getLogger(Main.class);
    public static void main(String[] args) throws Exception {
        Server server = new Server(80);
        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/");
        ServletHolder staticHolder = new ServletHolder(new DefaultServlet());
        staticHolder.setInitParameter("resourceBase", "../resources/main/webcontent/");
        contextHandler.addServlet(staticHolder, "/*");
        contextHandler.addServlet(RootServlet.class,"/");
        contextHandler.addServlet(SendToAdvcashServlet.class,"/redirectToAdvcash");
        contextHandler.addServlet(SuccessfulPayServlet.class,"/successful");
        contextHandler.addServlet(UnsuccessfulPayServlet.class,"/unsuccessful");
        contextHandler.addServlet(StatusPayServlet.class,"/432757270:AAH-WlbhHAjVRah4VyqqOEKmR-1dR2zeMhs");
        contextHandler.addServlet(StatusPayServlet.class,"/status");
        server.setHandler(contextHandler);
        server.start();
        log.info("*******Server started*********");
        server.join();
    }
}
