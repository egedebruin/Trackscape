package handlers;

import gui.controllers.MainController;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;

public class APIHandler {

    MainController controller;
    Server server;

    public APIHandler (MainController mainController) {
        controller = mainController;
        server = new Server(8080);

        ContextHandler handler = new ContextHandler("/chest");
        handler.setHandler(new APIChestHandler(mainController));
        //For more handlers, create like above two lines and add handler to collection below.

        HandlerCollection collection = new HandlerCollection();
        collection.addHandler(handler);

        server.setHandler(collection);
    }

    public void startServer() {
        try {
            server.start();
        } catch (Exception e) {
            System.out.println("Server could not be started");
            e.printStackTrace();
        }
    }

    public void stopServer() {
        try {
            server.stop();
        } catch (Exception e) {
            System.out.println("Server could not be stopped");
            e.printStackTrace();
        }
    }

}
