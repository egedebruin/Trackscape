package room;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class ProgressTest {

    private String testConfigFile = "files/testConfig.json";
    private Chest preceedingChest = new OpenedChest();

    static {
        // These should be at the start of the application,
        // so if the main changes this should be included.
        // Load OpenCV library.
        System.load(System.getProperty("user.dir")
            + File.separator + "libs" + File.separator + "opencv_ffmpeg341_64.dll");
        System.load(System.getProperty("user.dir")
            + File.separator + "libs" + File.separator + "opencv_java341.dll");
    }

    @Test
    void calculateProgress() {
        Progress progress = new Progress(testConfigFile);
        assertEquals(progress.calculateProgress(), 0);
        Chest chest  = progress.getRoom().getChestList().get(0);

        chest.updateStatus(preceedingChest);
        assertEquals(progress.calculateProgress(), 0);
        chest.subSectionCompleted();
        assertEquals(progress.calculateProgress(), 1);
        chest.setApprovedChestFoundByHost(true);
        chest.updateStatus(preceedingChest);
        assertEquals(progress.calculateProgress(), 21);
    }

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
        assertEquals(progress.calculateProgress(), 3);

        chest.updateStatus(preceedingChest);
        chest.subSectionCompleted();
        chest.setApprovedChestFoundByHost(true);
        chest.updateStatus(preceedingChest);
        assertEquals(progress.calculateProgress(), 24);
    }

    @Test
    void calculateProgresssingleSectionChest() {
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



    @Test
    void getRoom() {
        Progress progress = new Progress(testConfigFile, 0);
        assertNotNull(progress.getRoom());
    }
}
