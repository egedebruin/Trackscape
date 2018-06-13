package api;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


import handlers.JsonHandler;
import java.io.File;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.junit.jupiter.api.Test;
import room.Room;

/**
 * Tests for APIHandler class.
 */
public class APIHandlerTest {

    static {
        // These should be at the start of the application,
        // so if the main changes this should be included.
        // Load OpenCV library.
        System.load(System.getProperty("user.dir")
            + File.separator + "libs" + File.separator + "opencv_ffmpeg341_64.dll");
        System.load(System.getProperty("user.dir")
            + File.separator + "libs" + File.separator + "opencv_java341.dll");
    }

    /**
     * Test if constructor creates handler.
     */
    @Test
    void testConstructor() {
        Room room = new JsonHandler("files/test/testConfig.json").createSingleRoom();
        APIHandler apiHandler = new APIHandler(room);
        HandlerCollection collection = (HandlerCollection) apiHandler.getServer().getHandler();
        assertEquals(2, collection.getHandlers().length);
    }

    /**
     * Test if server is started.
     */
    @Test
    void testStartServer() {
        Room room = new JsonHandler("files/test/testConfig.json").createSingleRoom();
        APIHandler apiHandler = new APIHandler(room);
        apiHandler.startServer();
        assertTrue(apiHandler.getServer().isStarted());
        apiHandler.stopServer();
    }

    /**
     * Test if server is stopped.
     */
    @Test
    void testStopServer() {
        Room room = new JsonHandler("files/test/testConfig.json").createSingleRoom();
        APIHandler apiHandler = new APIHandler(room);
        apiHandler.startServer();
        assertTrue(apiHandler.getServer().isStarted());
        apiHandler.stopServer();
        assertFalse(apiHandler.getServer().isStarted());
    }

    /**
     * Test if server with wrong port cannot be started.
     */
    @Test
    void testStartFailed() {
        final int magicInt = 123456;
        Room room = new JsonHandler("files/test/testConfig.json").createSingleRoom();
        APIHandler apiHandler = new APIHandler(room);
        apiHandler.setServer(magicInt);
        apiHandler.startServer();
        assertFalse(apiHandler.getServer().isStarted());
    }
}
