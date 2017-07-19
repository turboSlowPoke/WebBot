package main;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import servlets.SendToAdvcashServlet;
import servlets.SuccessfulPayServlet;
import servlets.UnsuccessfulPayServlet;

public class Main {
    public static void main(String[] args) throws Exception {
        Server server = new Server(80);

        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/");
        contextHandler.setWelcomeFiles(new String[]{"index.html"});
        contextHandler.addServlet(SendToAdvcashServlet.class,"/redirectToAdvcash");
        contextHandler.addServlet(SuccessfulPayServlet.class,"/successful");
        contextHandler.addServlet(UnsuccessfulPayServlet.class,"/unsuccessful");

        server.setHandler(contextHandler);

        server.start();
        server.join();
    }
}
