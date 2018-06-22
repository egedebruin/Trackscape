package gui.controllers;

import com.sun.javafx.application.PlatformImpl;
import handlers.CameraHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static junit.framework.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
     * Construct the TimerManager.
     */
    @BeforeEach
    void initializeTimerManager() {
        PlatformImpl.startup(() -> { });

        videoController = new VideoController();
        videoController.setCameraHandler(new CameraHandler());

        timeLogController = new TimeLogController();
        timeLogController.setInformationBox(new TextArea());
        timeLogController.setTimerLabel(new Label());
        timeLogController.setTimeStamp(new Label());
        timeLogController.setQuestion(new Label());
        timeLogController.setApproveButton(new Button());
        timeLogController.setNotApproveButton(new Button());
        timeLogController.setImageView(new ImageView());

        roomController = new RoomController();
        roomController.configure("files/test/testConfig.json");

        timerManager = new TimerManager(roomController, timeLogController, videoController);
    }

    /**
     * Test whether timer gets started and stopped correctly.
     */
    @Test
    void startAndStopTimerTest() {
        timerManager.startTimer();
        assertTrue(videoController.isClosed());
        assertNotNull(roomController.getProgress());
        assertNotEquals(timeLogController.getTimerLabel().getText(), "00:00:00");

        roomController.getProgress().getApiHandler().stopServer();
        timerManager.stopTimer();
        assertTrue(videoController.isClosed());
        assertNull(roomController.getProgress());
        assertEquals(timeLogController.getTimerLabel().getText(), "00:00:00");
    }
}