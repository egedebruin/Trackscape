package gui.controllers;

import com.sun.javafx.application.PlatformImpl;
import handlers.CameraHandler;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for RoomController.
 */
public class RoomControllerTest {
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
        RoomController controller = new RoomController();
        final int numberOfChests = 3;
        PlatformImpl.startup(() -> { });
        CameraHandler camHandler = new CameraHandler();
        controller.setCameraHandler(camHandler);
        assertNull(controller.getProgress());
        assertFalse(controller.isConfigured());

        controller.configure(testConfigFile);
        assertNotNull(controller.getProgress());
        assertEquals(controller.getChestTimeStampList().size(), numberOfChests);
        assertTrue(controller.isConfigured());
    }

    /**
     * Test if controller is correctly closed.
     */
    @Test
    void closeControllerTest() {
        RoomController controller = new RoomController();
        PlatformImpl.startup(() -> { });
        CameraHandler camHandler = new CameraHandler();
            controller.setCameraHandler(camHandler);
            controller.configure(testConfigFile);
            assertTrue(controller.isConfigured());
            assertNotNull(controller.getProgress());

            controller.closeController();
            assertFalse(controller.isConfigured());
            assertNull(controller.getProgress());
            assertFalse(controller.isBehindSchedule());
            assertFalse(controller.getSnoozeHint());
    }

    /**
     * Test whether the hintTimer starts correctly.
     */
    @Test
    void startHintTimerTest() {
        RoomController controller = new RoomController();
        assertFalse(controller.getSnoozeHint());
        controller.startHintTimer();
        assertTrue(controller.getSnoozeHint());
    }

    /**
     * Test methods that concern the progress bar.
     */
    @Test
    void progressBarTest() {
//        CameraHandler camHandler = new CameraHandler();
//        controller.setCameraHandler(camHandler);
//        ProgressBar pg = new ProgressBar(controller);
//
//        GridPane progressBar = pg.createProgressBarPane();
//        controller.setProgressBar(progressBar);
//
//        pg.constructProgressBar();
//        pg.createProgressBarPane();
    }

}
