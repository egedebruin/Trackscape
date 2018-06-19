package gui.controllers;

import handlers.CameraHandler;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class for TimeLogController.
 */
public class TimeLogControllerTest {
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
     * Test the constructor for TimeLogController object.
     */
    @Test
    void constructorTest() {
        VideoController videoController = new VideoController();
        CameraHandler camHandler = new CameraHandler();
        videoController.setCameraHandler(camHandler);
        TimeLogController timeLogController = new TimeLogController();

        assertNotNull(timeLogController);
        assertNotNull(timeLogController.getCameraHandler().getInformationHandler());
    }
}