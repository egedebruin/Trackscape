package room;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


import handlers.CameraHandler;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Tests for the class Room.
 */
public class RoomTest {

    static {
        // These should be at the start of the application,
        // so if the main changes this should be included.
        // Load OpenCV library.
        System.load(System.getProperty("user.dir")
            + File.separator + "libs" + File.separator + "opencv_ffmpeg341_64.dll");
        System.load(System.getProperty("user.dir")
            + File.separator + "libs" + File.separator + "opencv_java341.dll");
    }

    private Room room;
    private List<String> cameraLinks = new ArrayList<>();
    private List<Chest> chestList = new ArrayList<>();
    private static final long TARGETTIME = 120;
    private static final long TARGETTIME2 = 100;

    /**
     * Tests for all getters in class.
     */
    @Test
    void testGetters() {
        cameraLinks.add("ajax");
        chestList.add(new Chest(1, TARGETTIME));
        room = new Room(0, 2, cameraLinks, chestList, 1);
        assertEquals(0, room.getId());
        assertEquals(2, room.getNumberOfPeople());
        assertEquals(chestList, room.getChestList());
        assertEquals(1, room.getTargetDuration());
    }

    /**
     * Tests for all setters in class.
     */
    @Test
    void testSetters() {
        cameraLinks.add("ajax");
        chestList.add(new Chest(1, TARGETTIME));
        room = new Room(0, 2, cameraLinks, chestList, 1);

        List<Chest> chestList2 = new ArrayList<>();
        chestList2.add(new Chest(0, TARGETTIME2));
        room.setChestList(chestList2);
        assertEquals(chestList2, room.getChestList());

        room.setId(1);
        assertEquals(1, room.getId());

        room.setNumberOfPeople(1);
        assertEquals(1, room.getNumberOfPeople());

        room.setTargetDuration(2);
        assertEquals(2, room.getTargetDuration());
    }

    /**
     * Tests for cameraHandler in class.
     */
    @Test
    void testCameraHandler() {
        room = new Room(0, 2, cameraLinks, chestList, 1);

        assertNotNull(room.getCameraHandler());

        CameraHandler newHandler = new CameraHandler();
        room.setCameraHandler(newHandler);

        assertEquals(newHandler, room.getCameraHandler());
    }
}
