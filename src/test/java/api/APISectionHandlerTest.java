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
 * Tests for APISectionHandler class.
 */
public class APISectionHandlerTest {

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
        APISectionHandler handler = new APISectionHandler(room);
        assertNotNull(handler);
    }

    /**
     * Test if correct http request finishes section.
     * @throws IOException exception
     */
    @Test
    void testHandleCorrect() throws IOException {
        Room room = new JsonHandler("files/test/testConfig.json").createSingleRoom();
        room.updateRoom();
        APIHandler handler = new APIHandler(room);
        handler.startServer();

        HttpURLConnection http = (HttpURLConnection)
            new URL("http://localhost:8080/section?completed=true").openConnection();
        http.connect();
        http.getResponseCode();

        assertEquals(1, room.getChestList().get(0).countSubsectionsCompleted());
        handler.stopServer();
    }

    /**
     * Test if incorrect http request doesn't complete section.
     * @throws IOException exception
     */
    @Test
    void testHandleIncorrect() throws IOException {
        Room room = new JsonHandler("files/test/testConfig.json").createSingleRoom();
        room.updateRoom();
        APIHandler handler = new APIHandler(room);
        handler.startServer();

        HttpURLConnection http = (HttpURLConnection)
            new URL("http://localhost:8080/section?complete=true").openConnection();
        http.connect();
        http.getResponseCode();
        assertEquals(0, room.getChestList().get(0).countSubsectionsCompleted());

        HttpURLConnection http2 = (HttpURLConnection)
            new URL("http://localhost:8080/section?completed=false").openConnection();
        http2.connect();
        http2.getResponseCode();
        assertEquals(0, room.getChestList().get(0).countSubsectionsCompleted());

        for (int i = 0; i < room.getChestList().size(); i++) {
            room.setNextChestOpened(0);
            room.updateRoom();
        }

        HttpURLConnection http3 = (HttpURLConnection)
            new URL("http://localhost:8080/section?completed=true").openConnection();
        http3.connect();
        http3.getResponseCode();
        assertEquals(room.getChestList().size(), room.getChestsOpened());
        handler.stopServer();
    }
}
