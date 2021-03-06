package room;

import handlers.InformationHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
     * Reset cameralist and handeler after all teststr/.
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
    private static final long WARNINGTIME = 60;
    private static final long WARNINGTIME2 = 50;
    private static final long TARGETTIME2 = 100;
    private InformationHandler informationHandler = new InformationHandler();

    /**
     * Tests for all getters in class.
     */
    @Test
    void testGetters() {
        cameraLinks.add("ajax");
        chestList.add(new Chest(1, TARGETTIME, WARNINGTIME));
        room = new Room(0, 2, cameraLinks, chestList, 1, 1);
        assertEquals(0, room.getId());
        assertEquals(2, room.getNumberOfPeople());
        assertEquals(chestList, room.getChestList());
        assertEquals(1, room.getTargetDuration());
        assertEquals(1, room.getPort());
        assertNull(room.getInformationHandler());
        assertEquals(cameraLinks, room.getLinkList());
    }

    /**
     * Tests for all setters in class.
     */
    @Test
    void testSetters() {
        cameraLinks.add("ajax");
        chestList.add(new Chest(1, TARGETTIME, WARNINGTIME));
        room = new Room(0, 2, cameraLinks, chestList, 1, 1);

        List<Chest> chestList2 = new ArrayList<>();
        chestList2.add(new Chest(0, TARGETTIME2, WARNINGTIME2));
        room.setChestList(chestList2);
        assertEquals(chestList2, room.getChestList());

        room.setId(1);
        assertEquals(1, room.getId());

        room.setNumberOfPeople(1);
        assertEquals(1, room.getNumberOfPeople());

        room.setTargetDuration(2);
        assertEquals(2, room.getTargetDuration());

        room.setInformationHandler(informationHandler);
        assertEquals(informationHandler, room.getInformationHandler());
    }

    /**
     * Test if setNextChestOpened opens next chest.
     */
    @Test
    void testSetNextChestOpened() {
        chestList.add(new Chest(1, TARGETTIME, WARNINGTIME));
        room = new Room(0, 2, cameraLinks, chestList, 1, 1);
        assertEquals(chestList.get(0).getChestState(), Chest.Status.WAITING_FOR_SECTION_TO_START);
        room.updateRoom();
        room.setNextChestOpened(System.nanoTime());
        room.updateRoom();
        assertEquals(chestList.get(0).getChestState(), Chest.Status.OPENED);
    }

    /**
     * Test if setNextSectionOpened opens next section.
     */
    @Test
    void testSetNextSectionOpened() {
        chestList.add(new Chest(1, TARGETTIME, WARNINGTIME));
        room = new Room(0, 2, cameraLinks, chestList, 1, 1);
        assertEquals(chestList.get(0).getChestState(), Chest.Status.WAITING_FOR_SECTION_TO_START);
        room.updateRoom();
        room.setNextSectionOpened();
        room.updateRoom();
        assertEquals(chestList.get(0).getChestState(), Chest.Status.OPENED);
    }

    /**
     * Test if getChestsOpened gets correct amount of chests opened.
     */
    @Test
    void getChestsOpenedTest() {
        room = new Room(0, 2, cameraLinks, chestList, 1, 1);
        Chest chest = new Chest(1, TARGETTIME, WARNINGTIME);
        Chest chest2 = new Chest(2, TARGETTIME, WARNINGTIME);
        chestList.add(chest);
        chestList.add(chest2);
        assertEquals(0, room.getChestsOpened());

        chest.setApprovedChestFoundByHost();
        assertEquals(1, room.getChestsOpened());
    }

    /**
     * Test if totalSubsections returns correct number.
     */
    @Test
    void totalSubsectionsTest() {
        room = new Room(0, 2, cameraLinks, chestList, 1, 1);
        Chest chest = new Chest(1, TARGETTIME, WARNINGTIME);
        Chest chest2 = new Chest(1, TARGETTIME, WARNINGTIME);
        chestList.add(chest);
        chestList.add(chest2);
        assertEquals(2, room.getTotalSubsections());
    }

    /**
     * Test setChestCompletedTill method.
     */
    @Test
    void testSetChestCompletedTill() {
        room = new Room(0, 2, cameraLinks, chestList, 1, 1);
        Chest chest = new Chest(1, TARGETTIME, WARNINGTIME);
        Chest chest2 = new Chest(2, TARGETTIME, WARNINGTIME);
        chestList.add(chest);
        chestList.add(chest2);
        room.setChestSectionsCompletedTill(2);
        room.updateRoom();
        assertEquals(1, room.getChestsOpened());
    }

}
