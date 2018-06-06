package room;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Class for testing progress.
 */
class ProgressTest {

    private String testConfigFile = "files/test/testConfig.json";
    private Chest precedingChest = new OpenedChest();
    private static final int FIRSTCHESTNOOFSECTIONS = 21;
    private static final int SECONDCHESTNOOFSECTIONS = 3;
    private static final int COMBINEDNOOFSECTIONS = 24;
    private static final int TOTALNOOFSECTIONS = 25;


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
     * Tests calculate progress for only one chest.
     */
    @Test
    void calculateProgress() {
        Progress progress = new Progress(testConfigFile);
        assertEquals(progress.calculateProgress(), 0);
        Chest chest  = progress.getRoom().getChestList().get(0);

        chest.updateStatus(precedingChest);
        assertEquals(progress.calculateProgress(), 0);
        chest.subSectionCompleted();
        assertEquals(progress.calculateProgress(), 1);
        chest.setApprovedChestFoundByHost(true);
        chest.updateStatus(precedingChest);
        assertEquals(progress.calculateProgress(), FIRSTCHESTNOOFSECTIONS);
    }

    /**
     * Tests calculate progress for two chests.
     */
    @Test
    void calculateProgressMultipleChests() {
        Progress progress = new Progress(testConfigFile);
        Chest chest  = progress.getRoom().getChestList().get(0);
        Chest chest2  = progress.getRoom().getChestList().get(1);
        //chest is preceeded by nothing.
        chest.updateStatus(new OpenedChest());
        //chest2 is preceeded by chest.
        chest2.updateStatus(chest);
        chest2.subSectionCompleted();
        assertEquals(progress.calculateProgress(), 0);
        chest2.setApprovedChestFoundByHost(true);
        assertEquals(progress.calculateProgress(), SECONDCHESTNOOFSECTIONS);

        chest.updateStatus(precedingChest);
        chest.subSectionCompleted();
        chest.setApprovedChestFoundByHost(true);
        chest.updateStatus(precedingChest);
        assertEquals(progress.calculateProgress(), COMBINEDNOOFSECTIONS);
    }

    /**
     * Tests is calculate progress doesnt count to many subsections.
     */
    @Test
    void calculateProgressSingleSectionChest() {
        Progress progress = new Progress(testConfigFile);
        assertEquals(progress.calculateProgress(), 0);
        Chest chest  = progress.getRoom().getChestList().get(2);
        chest.updateStatus(new OpenedChest());
        assertEquals(progress.calculateProgress(), 0);
        chest.subSectionCompleted();
        assertEquals(progress.calculateProgress(), 1);
        chest.subSectionCompleted();
        assertEquals(progress.calculateProgress(), 1);
    }


    /**
     * Tests if getter for room returns a room when correctly initialized.
     */
    @Test
    void getRoom() {
        Progress progress = new Progress(testConfigFile, 0);
        assertNotNull(progress.getRoom());
    }

    /**
     * Test if getTotalSections the correct number of sections returned when correct file is loaded.
     */
    @Test
    void getTotalSections() {
        Progress progress = new Progress(testConfigFile, 0);
        assertEquals(TOTALNOOFSECTIONS, progress.getTotalSections());
    }

    /**
     * Test if getTotalSections the correct number of sections returned when correct file is loaded.
     */
    @Test
    void getSubsectionCountFromBarIndex() {
        Progress progress = new Progress(testConfigFile, 0);
        int evenIndex = 8;
        assertEquals(5, progress.getSubSectionCountFromBarIndex(evenIndex));
        int oddIndex = 7;
        assertEquals(4, progress.getSubSectionCountFromBarIndex(oddIndex));
    }

    /**
     * Tests if updateProgress updates the first chest if that chest needs to be updated.
     */
    @Test
    void updateProgress() {
        Progress progress = new Progress(testConfigFile, 0);
        Chest chest = progress.getRoom().getChestList().get(0);
        assertEquals(chest.getChestState(), Chest.Status.WAITING_FOR_SECTION_TO_START);
        progress.updateProgress();
        assertEquals(chest.getChestState(), Chest.Status.TO_BE_OPENED);
    }

    /**
     * Test if getTotalSections the correct number of sections returned when correct file is loaded.
     */
    @Test
    void getFillCount() {
        Progress progress = new Progress(testConfigFile, 0);

        assertEquals(-2, progress.getFillCount());
        updateProgress();
        progress.getRoom().getChestList().get(0).setApprovedChestFoundByHost(true);
        updateProgress();
        assertEquals((FIRSTCHESTNOOFSECTIONS - 1) * 2, progress.getFillCount());
    }

    @Test
    void setSubSectionCount() {
        Progress progress = new Progress(testConfigFile, 0);
        assertEquals(0, progress.getSubSectionCount());
        progress.setSubSectionCount(3);
        assertEquals(3, progress.getSubSectionCount());
    }


}
