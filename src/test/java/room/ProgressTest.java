package room;

import com.sun.javafx.application.PlatformImpl;
import org.junit.jupiter.api.Test;

import java.io.File;


import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        chest.setApprovedChestFoundByHost();
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
        chest2.setApprovedChestFoundByHost();
        assertEquals(progress.calculateProgress(), SECONDCHESTNOOFSECTIONS);

        chest.updateStatus(precedingChest);
        chest.subSectionCompleted();
        chest.setApprovedChestFoundByHost();
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
        final int evenIndex = 8;
        final int expected1 = 5;
        assertEquals(expected1, progress.getSubSectionCountFromBarIndex(evenIndex));
        final int oddIndex = 7;
        final int expected2 = 4;
        assertEquals(expected2, progress.getSubSectionCountFromBarIndex(oddIndex));
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
        final int expected = -2;
        assertEquals(expected, progress.getFillCount());
        updateProgress();
        progress.getRoom().getChestList().get(0).setApprovedChestFoundByHost();
        updateProgress();
        assertEquals((FIRSTCHESTNOOFSECTIONS - 1) * 2, progress.getFillCount());
    }

    /**
     * Test getter setter for subsectionCount.
     */
    @Test
    void setSubSectionCount() {
        Progress progress = new Progress(testConfigFile, 0);
        assertEquals(0, progress.getSubSectionCount());
        final int newsubsections = 3;
        progress.setSubSectionCount(newsubsections);
        assertEquals(newsubsections, progress.getSubSectionCount());
    }

    /**
     * Test stopServer method.
     */
    @Test
    void testStopServer() {
        PlatformImpl.startup(() -> { });
        PlatformImpl.runLater(() -> {
            Progress progress = new Progress(testConfigFile, 0);
            assertTrue(progress.getApiHandler().getServer().isStarted());
            progress.stopServer();
            assertFalse(progress.getApiHandler().getServer().isStarted());
        });
    }

    /**
     * Test newProgress method.
     */
    @Test
    void testNewProgress() {
        Progress progress = new Progress(testConfigFile, 0);
        assertEquals(0, progress.newProgress(2));
    }

    /**
     * Test allChestsOpened method.
     */
    @Test
    void testAllChestsOpened() {
        Progress progress = new Progress(testConfigFile, 0);
        assertFalse(progress.allChestsOpened());
        progress.getRoom().updateRoom();
        progress.getRoom().setNextChestOpened(0);
        progress.getRoom().updateRoom();
        progress.getRoom().setNextChestOpened(0);
        progress.getRoom().updateRoom();
        progress.getRoom().setNextChestOpened(0);
        assertTrue(progress.allChestsOpened());
    }

    /**
     * Test confirmedChestString method.
     */
    @Test
    void testConfirmedChestString() {
        Progress progress = new Progress(testConfigFile, 0);
        progress.getRoom().updateRoom();
        assertEquals("1/3", progress.confirmedChestString(0));
    }


}
