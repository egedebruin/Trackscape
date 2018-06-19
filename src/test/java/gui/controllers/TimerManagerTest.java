package gui.controllers;

import com.sun.javafx.application.PlatformImpl;
import handlers.CameraHandler;
import javafx.scene.control.TextArea;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for TimerManager.
 */
public class TimerManagerTest {
    private TimerManager timerManager;
    private RoomController roomController;
    private TimeLogController timeLogController;
    private VideoController videoController;

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
     * Test whether timer gets started and stopped correctly.
     */
    @Test
    void startAndStopTimerTest() {
        PlatformImpl.startup(() -> {
        });

        PlatformImpl.runLater(() -> {
            videoController = new VideoController();
            videoController.setCameraHandler(new CameraHandler());
            timeLogController = new TimeLogController();
            timeLogController.setInformationBox(new TextArea());
            roomController = new RoomController();

            timerManager = new TimerManager(roomController, timeLogController, videoController);

            timerManager.startTimer();
            assertTrue(videoController.isClosed());
            assertNotNull(roomController.getProgress());
            assertNotEquals(timeLogController.getTimerLabel(), "00:00:00");

            timerManager.stopTimer();
            assertFalse(videoController.isClosed());
            assertNull(roomController.getProgress());
            assertEquals(timeLogController.getTimerLabel(), "00:00:00");
        });
    }
}