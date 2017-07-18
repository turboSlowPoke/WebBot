package main;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import servlets.RedirectToAdvcash;

public class Main {
    public static void main(String[] args) throws Exception {
        Server server = new Server(80);

        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/");
        contextHandler.setWelcomeFiles(new String[]{"index.html"});
        contextHandler.addServlet(RedirectToAdvcash.class,"/redirectToAdvcash");
        server.setHandler(contextHandler);

        server.start();
        server.join();
    }
}
