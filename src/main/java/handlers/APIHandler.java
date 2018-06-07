package handlers;

import gui.controllers.MainController;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;

/**
 * Class for the APIHandler.
 */
public class APIHandler {

    private MainController controller;
    private final int defaultPort = 8080;
    private Server server;

    /**
     * Constructor of APIHandler.
     * @param mainController the mainController for this handler.
     */
    public APIHandler(final MainController mainController) {
        controller = mainController;
        setServer(defaultPort);
    }

    /**
     * Set the server with a port number.
     * @param port the (new) port number
     */
    public void setServer(final int port) {
        server = new Server(port);

        ContextHandler handler = new ContextHandler("/chest");
        handler.setHandler(new APIChestHandler(controller));
        //For more handlers, create like above two lines and add handler to collection below.

        HandlerCollection collection = new HandlerCollection();
        collection.addHandler(handler);

        server.setHandler(collection);
    }

    /**
     * Start the server.
     */
    public void startServer() {
        try {
            server.start();
        } catch (Exception e) {
            System.out.println("Server could not be started");
            e.printStackTrace();
        }
    }

    /**
     * Stop the server.
     */
    public void stopServer() {
        try {
            server.stop();
        } catch (Exception e) {
            System.out.println("Server could not be stopped");
            e.printStackTrace();
        }
    }

}
