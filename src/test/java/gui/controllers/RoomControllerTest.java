package gui.controllers;

import javafx.scene.layout.GridPane;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for RoomController.
 */
public class RoomControllerTest {
    private RoomController controller = new RoomController();
    private String testConfigFile = "files/test/testConfig.json";

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
     * Test the configure method.
     */
    @Test
    void configureTest() {
//        final int numberOfChests = 3;
//        PlatformImpl.startup(() -> { });
//        CameraHandler camHandler = new CameraHandler();
//        controller.setCameraHandler(camHandler);
//        assertNull(controller.getProgress());
//        assertFalse(controller.isConfigured());
//
//        controller.configure(testConfigFile);
//        assertNotNull(controller.getProgress());
//        assertEquals(controller.getChestTimeStampList().size(), numberOfChests);
//        assertTrue(controller.isConfigured());
    }

    /**
     * Test if controller is correctly closed.
     */
    @Test
    void closeControllerTest() {
//        PlatformImpl.startup(() -> { });
//        CameraHandler camHandler = new CameraHandler();
//        controller.setCameraHandler(camHandler);
//        controller.configure(testConfigFile);
//        assertTrue(controller.isConfigured());
//        assertNotNull(controller.getProgress());
//
//        controller.closeController();
//        assertFalse(controller.isConfigured());
//        assertNull(controller.getProgress());
//        assertFalse(controller.isBehindSchedule());
//        assertFalse(controller.getSnoozeHint());
    }

    /**
     * Test whether the hintTimer starts correctly.
     */
    @Test
    void startHintTimerTest() {
        assertFalse(controller.getSnoozeHint());
        controller.startHintTimer();
        assertTrue(controller.getSnoozeHint());
    }

    /**
     * Test methods that concern the progess bar.
     */
    @Test
    void progressBarTest() {
        GridPane progressBar = new GridPane();
        controller.setProgressBar(progressBar);

        // need to fill progressbar with correct configuration

    }

}
