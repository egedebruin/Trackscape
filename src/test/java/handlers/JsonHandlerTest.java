package handlers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


import java.io.File;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Test the Json handler class.
 */
public class JsonHandlerTest {

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
     * Class variables.
     */
    private final String jsonFile = "files/example.json";
    private final int jsonPeople = 5;
    private final int jsonChests = 3;
    private JsonHandler handler;

    /**
     * Test the constructor, check if the file is read correctly.
     */
    @Test
    void constructorTest() {
        handler = new JsonHandler(jsonFile);
        assertNotNull(handler.getJsonElement());
    }

    /**
     * Test the getCameraLinks method.
     */
    @Test
    void getCameraLinksTest() {
        handler = new JsonHandler(jsonFile);
        List<String> list = handler.getCameraLinks(0);
        assertEquals(2, list.size());
    }

    /**
     * Test the getAmountPeople method.
     */
    @Test
    void getAmountPeopleTest() {
        handler = new JsonHandler(jsonFile);
        int people = handler.getAmountPeople(0);
        assertEquals(jsonPeople, people);
    }

    /**
     * Test the createRooms method.
     */
    @Test
    void createRoomsTest() {
        handler = new JsonHandler(jsonFile);
        int rooms = handler.createRooms().size();
        assertEquals(1, rooms);
    }

    /**
     * Test the createChests method.
     */
    @Test
    void createChestsTest() {
        handler = new JsonHandler(jsonFile);
        int chests = handler.createChests(0).size();
        assertEquals(jsonChests, chests);
    }

    /**
     * Test that NullPointer will be thrown if roomId is not correct.
     */
    @Test
    void incorrectRoomIdTest() {
        handler = new JsonHandler(jsonFile);
        assertThrows(NullPointerException.class,
            () -> handler.getAmountPeople(2));
    }

    /**
     * Test that constructor doesn't create jsonElement with wrong filename.
     */
    @Test
    void incorrectFileNameTest() {
        handler = new JsonHandler("ajax");
        assertNull(handler.getJsonElement());
    }
}
