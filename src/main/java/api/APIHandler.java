package api;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import room.Room;

/**
 * Class for the APIHandler.
 */
public class APIHandler {

    /**
     * The default port.
     */
    public static final int DEFAULT_PORT = 8080;
    private Room room;
    private Server server;

    /**
     * Constructor of APIHandler.
     * @param newRoom the room for this handler.
     */
    public APIHandler(final Room newRoom) {
        room = newRoom;
        setServer(DEFAULT_PORT);
    }

    /**
     * Set the server with a port number.
     * @param port the (new) port number
     */
    public void setServer(final int port) {
        server = new Server(port);

        ContextHandler handler = new ContextHandler("/chest");
        handler.setHandler(new APIChestHandler(room));
        //For more handlers, create like above two lines and add handler to collection below.

        ContextHandler sectionHandler = new ContextHandler("/section");
        sectionHandler.setHandler(new APISectionHandler(room));

        HandlerCollection collection = new HandlerCollection();
        collection.addHandler(handler);
        collection.addHandler(sectionHandler);

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
        }
    }

    /**
     * Returns the server.
     * @return the server
     */
    public Server getServer() {
        return server;
    }
}
