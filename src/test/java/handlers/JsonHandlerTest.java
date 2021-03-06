package handlers;

import org.junit.jupiter.api.Test;
import room.Chest;
import room.Room;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    private final String jsonFile = "files/test/testConfig.json";
    private final String jsonFile2 = "files/test/testConfig2.json";
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
        assertEquals(handler.getAmountPeople(2), 0);
    }

    /**
     * Test that constructor doesn't create jsonElement with wrong filename.
     */
    @Test
    void incorrectFileNameTest() {
        handler = new JsonHandler("ajax");
        assertNull(handler.getJsonElement());
    }

    /**
     * Tests if a single room is correctly created when createsingleroom is called.
     */
    @Test
    void createSingleRoomTest() {
        handler = new JsonHandler(jsonFile);
        Room room = handler.createSingleRoom();
        assertNotNull(room);
        assertEquals(jsonPeople, room.getNumberOfPeople());
        assertEquals(jsonChests, room.getChestList().size());
    }

    /**
     * Tests if a generic room is returned whenever jsonfile is wrong.
     */
    @Test
    void createSingleRoomEmptyTest() {
        String emptyJsonFile = "files/test/empty.json";
        handler = new JsonHandler(emptyJsonFile);
        Room room = handler.createSingleRoom();
        assertNotNull(room);
        assertEquals(0, room.getNumberOfPeople());
        assertNull(room.getChestList());
    }

    /**
     * Tests if the correct amount of chests are returned when getAmountChests(roomid) is called.
     */
    @Test
    void getAmountChestsTest() {
        handler = new JsonHandler(jsonFile);
        assertEquals(jsonChests, handler.getAmountChests(0));
    }

    /**
     * Tests if the correct port is given when getPortNumber(roomid) is called.
     */
    @Test
    void getPortNumberTest() {
        final int port = 7070;
        handler = new JsonHandler(jsonFile);
        assertEquals(port, handler.getPortNumber(0));
    }

    /**
     * Test configFile without target duration.
     */
    @Test
    void noTargetDurationTest() {
        handler = new JsonHandler(jsonFile2);
        assertEquals(0, handler.getTargetDuration(0));
    }

    /**
     * Test configFile without amount people.
     */
    @Test
    void noAmountPeopleTest() {
        handler = new JsonHandler(jsonFile2);
        assertEquals(0, handler.getAmountPeople(0));
    }

    /**
     * Test configFile without camera links.
     */
    @Test
    void noCameraLinksTest() {
        handler = new JsonHandler(jsonFile2);
        List<String> stringList = handler.getCameraLinks(0);
        assertEquals(0, stringList.size());
    }

    /**
     * Test configFile without chests.
     */
    @Test
    void noChestsTest() {
        handler = new JsonHandler(jsonFile2);
        List<Chest> stringList = handler.createChests(0);
        assertEquals(0, stringList.size());
    }

    /**
     * Test configFile without port returns default port.
     */
    @Test
    void noPortTest() {
        handler = new JsonHandler(jsonFile2);
        final int defaultPort = 8080;
        assertEquals(defaultPort, handler.getPortNumber(0));
    }
}
