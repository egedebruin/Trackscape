package api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


import handlers.JsonHandler;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import org.junit.jupiter.api.Test;
import room.Room;

/**
 * Tests for the APIChestHandler class.
 */
public class APIChestHandlerTest {

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
     * Test if the constructor creates a handler.
     */
    @Test
    void testConstructor() {
        Room room = new JsonHandler("files/test/testConfig.json").createSingleRoom();
        APIChestHandler handler = new APIChestHandler(room);
        assertNotNull(handler);
    }

    /**
     * Test if correct http request opens chest.
     * @throws IOException exception
     */
    @Test
    void testHandleCorrect() throws IOException {
        Room room = new JsonHandler("files/test/testConfig.json").createSingleRoom();
        room.updateRoom();
        APIHandler handler = new APIHandler(room);
        handler.startServer();

        HttpURLConnection http =
            (HttpURLConnection) new URL("http://localhost:8080/chest?opened=true").openConnection();
        http.connect();
        http.getResponseCode();

        assertEquals(1, room.getChestsOpened());
        handler.stopServer();
    }

    /**
     * Test if incorrect http request doesn't open chest.
     * @throws IOException exception
     */
    @Test
    void testHandleIncorrect() throws IOException {
        Room room = new JsonHandler("files/test/testConfig.json").createSingleRoom();
        room.updateRoom();
        APIHandler handler = new APIHandler(room);
        handler.startServer();

        HttpURLConnection http =
            (HttpURLConnection) new URL("http://localhost:8080/chest?opene=true").openConnection();
        http.connect();
        http.getResponseCode();
        assertEquals(0, room.getChestsOpened());

        HttpURLConnection http2 =
            (HttpURLConnection) new URL("http://localhost:8080/chest?opened=fals").openConnection();
        http2.connect();
        http2.getResponseCode();
        assertEquals(0, room.getChestsOpened());
        handler.stopServer();
    }
}
