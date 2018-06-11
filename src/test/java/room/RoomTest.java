package room;

import handlers.CameraHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    /**
     * REset cameralist and handeler after all teststr/.
     */
    @AfterEach
    void reset() {
        cameraLinks = new ArrayList<>();
        chestList = new ArrayList<>();
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
        room = new Room(0, 2, cameraLinks, chestList, 1, 1);
        assertEquals(0, room.getId());
        assertEquals(2, room.getNumberOfPeople());
        assertEquals(chestList, room.getChestList());
        assertEquals(1, room.getTargetDuration());
        assertEquals(1, room.getPort());
    }

    /**
     * Tests for all setters in class.
     */
    @Test
    void testSetters() {
        cameraLinks.add("ajax");
        chestList.add(new Chest(1, TARGETTIME));
        room = new Room(0, 2, cameraLinks, chestList, 1, 1);

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
        room = new Room(0, 2, cameraLinks, chestList, 1, 1);

        assertNotNull(room.getCameraHandler());

        CameraHandler newHandler = new CameraHandler();
        room.setCameraHandler(newHandler);

        assertEquals(newHandler, room.getCameraHandler());
    }

    /**
     * Test if setNextChestOpened opens next chest.
     */
    @Test
    void setNextChestOpened() {
        chestList.add(new Chest(1, TARGETTIME));
        room = new Room(0, 2, cameraLinks, chestList, 1, 1);
        assertEquals(chestList.get(0).getChestState(), Chest.Status.WAITING_FOR_SECTION_TO_START);
        room.updateRoom();
        room.setNextChestOpened();
        room.updateRoom();
        assertEquals(chestList.get(0).getChestState(), Chest.Status.OPENED);
    }

    @Test
    void setNextSectionCompleted() {
        chestList.add(new Chest(1, TARGETTIME));
        room = new Room(0, 2, cameraLinks, chestList, 1, 1);
        assertEquals(chestList.get(0).getChestState(), Chest.Status.WAITING_FOR_SECTION_TO_START);
        room.updateRoom();
        room.setNextSectionOpened();
        room.updateRoom();
        assertEquals(chestList.get(0).getChestState(), Chest.Status.OPENED);
    }

    /**
     * Test if unsetChestTill sets only the chests till subsections and not further.
     */
    @Test
    void unsetChestsTill() {
        room = new Room(0, 2, cameraLinks, chestList, 1, 1);
        Chest chest = new Chest(1, TARGETTIME);
        Chest chest2 = new Chest(1, TARGETTIME);
        Chest chest3 = new Chest(2, TARGETTIME);
        chestList.add(chest);
        chestList.add(chest2);
        chestList.add(chest3);
        final int subsections = 3;
        room.unsetChestSectionsCompletedTill(subsections);
        assertEquals(chest.getChestState(), Chest.Status.OPENED);
        assertEquals(chest2.getChestState(), Chest.Status.OPENED);
        assertEquals(chest3.getChestState(), Chest.Status.WAITING_FOR_SECTION_TO_START);
        assertEquals(chest3.countSubsectionsCompleted(), 1);

    }

    /**
     * Test if getChestsOpened gets correct amount of chests opened.
     */
    @Test
    void getChestsOpenedTest() {
        room = new Room(0, 2, cameraLinks, chestList, 1, 1);
        Chest chest = new Chest(1, TARGETTIME);
        Chest chest2 = new Chest(2, TARGETTIME);
        chestList.add(chest);
        chestList.add(chest2);
        assertEquals(0, room.getChestsOpened());

        chest.setApprovedChestFoundByHost(true);
        assertEquals(1, room.getChestsOpened());
    }

}
