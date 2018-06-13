package room;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the Chest class.
 */
class ChestTest {

    private static final long TARGETTIME = 120;
    private Chest precedingChest = new OpenedChest();

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
     * Test for the updateStatus method, every transition is tested.
     */
    @Test
    void updateStatusTest() {
        Chest chest = new Chest(1, TARGETTIME);
        assertEquals(chest.getChestState(), Chest.Status.WAITING_FOR_SECTION_TO_START);
        chest.updateStatus(precedingChest);
        assertEquals(chest.getChestState(), Chest.Status.TO_BE_OPENED);
        chest.updateStatus(precedingChest);
        assertEquals(chest.getChestState(), Chest.Status.TO_BE_OPENED);
        chest.setApprovedChestFoundByHost();
        chest.updateStatus(precedingChest);
        assertEquals(chest.getChestState(), Chest.Status.OPENED);
        chest.updateStatus(precedingChest);
        assertEquals(chest.getChestState(), Chest.Status.OPENED);
    }

    /**
     * Test for the getChestState method.
     */
    @Test
    void getChestStateTest() {
        Chest chest = new Chest(1, TARGETTIME);
        assertEquals(chest.getChestState(), Chest.Status.WAITING_FOR_SECTION_TO_START);
    }

    /**
     * Test for the countSubsections method.
     */
    @Test
    void countSubsectionsTest() {
        Chest chest = new Chest(1, TARGETTIME);
        assertEquals(chest.countSubsectionsCompleted(), 0);
        chest.subSectionCompleted();
        assertEquals(chest.countSubsectionsCompleted(), 1);
        chest.subSectionCompleted();
        assertEquals(chest.countSubsectionsCompleted(), 1);
    }

    /**
     * Test for the getNumberOfSubsections method.
     */
    @Test
    void getNumberOfSubSectionsTest() {
        Chest chest = new Chest(1, TARGETTIME);
        assertEquals(chest.getNumberOfSubSections(), 1);
        Chest chest2 = new Chest(-1, TARGETTIME);
        assertEquals(chest2.getNumberOfSubSections(), 1);
    }

    /**
     * Test for the countSubsectionsCompleted method.
     */
    @Test
    void subSectionCompletedTest() {
        Chest chest = new Chest(1, TARGETTIME);
        assertEquals(chest.countSubsectionsCompleted(), 0);
        chest.subSectionCompleted();
        assertEquals(chest.countSubsectionsCompleted(), 1);
        chest.subSectionCompleted();
        assertEquals(chest.countSubsectionsCompleted(), 1);
    }

    /**
     * Tests is countsubsections works when only cheststate is opened.
     */
    @Test
    void countSubsectionsCompletedOpenedChestTest() {
        Chest chest = new OpenedChest();
        assertEquals(chest.countSubsectionsCompleted(), 1);
    }

    /**
     * Tests if status gets updated to opened when it was to be opened.
     */
    @Test
    void chestStateUpdateFromToBeOpenedToOpened() {
        Chest chest = new Chest(1, TARGETTIME);
        chest.updateStatus(precedingChest);
        chest.subSectionCompleted();
        chest.updateStatus(precedingChest);

        assertEquals(chest.getChestState(), Chest.Status.OPENED);
    }

    /**
     * Test getTargetDurationInSec method.
     */
    @Test
    void getTargetDurationInSecTest() {
        Chest chest = new Chest(1, TARGETTIME);
        assertEquals(chest.getTargetDurationInSec(), TARGETTIME);
    }

    /**
     * Test getTimeFount method.
     */
    @Test
    void getTimeFoundTest() {
        Chest chest = new Chest(1, TARGETTIME);
        assertEquals(-1, chest.getTimeFound());
        chest.setTimeFound(1);
        assertEquals(1, chest.getTimeFound());
    }
}
