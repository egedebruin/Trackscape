package camera;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;


import handlers.CameraHandler;
import java.io.File;
import java.time.Duration;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

/**
 * Class for testing Camera.
 */
class CameraTest {

    private static final int DEFAULTNOOFCHEST = 10;
    private static final int VIDEOLENGTH = 25;
    private Camera camera;
    private CameraHandler cameraHandler;
    private final String videoLink = "files" + File.separator + "postit.mov";

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
     * Tests if a camera is correctly constructed.
     */
    @Test
    void constructorTest() {
        camera = new Camera(null, null);
        assertNotNull(camera);
    }

    /**
     * Tests if a camera is correctly constructed when extra parameters are given.
     */
    @Test
    void constructorPlusTest() {
        camera = new Camera(null, null, DEFAULTNOOFCHEST);
        assertNotNull(camera);
        assertEquals(camera.getNumOfChestsInRoom(), DEFAULTNOOFCHEST);
    }

    /**
     * Tests if the latest frame is returned as a non null Mat whenever getLastFrame is called.
     */
    @Test
    void getLastFrame() {
        String link = "files" + File.separator + "webcast.mov";
        VideoCapture videoCapture = new VideoCapture(link);
        camera = new Camera(videoCapture, link);

        Mat mat = camera.getLastFrame();
        assertNotNull(mat);

        Mat mat2 = camera.getLastFrame();
        assertNotEquals(mat, mat2);
    }

    /**
     * Tests if equals returns false when 2 different cameras are compared.
     */
    @Test
    void equalsFalseDifferentCameraTest() {
        Camera cam1 = new Camera(new VideoCapture(), "linkToCamOne");
        Camera cam2 = new Camera(new VideoCapture(), "linkToCamTwo",
            DEFAULTNOOFCHEST);
        assertNotEquals(cam1, cam2);
    }

    /**
     * Tests if equals returns false when Camera is compared to non Camera Object.
     */
    @Test
    void equalsFalseNoCameraTest() {
        Camera cam1 = new Camera(new VideoCapture(), "linkToCamOne");
        CameraChestDetector cameraChest = new CameraChestDetector();
        assertNotEquals(cam1, cameraChest);
    }

    /**
     * Tests if equals returns true when 2 different cameras with the same link are compared.
     */
    @Test
    void equalsTrueDifObjTest() {
        Camera cam1 = new Camera(new VideoCapture(), "linkToCamOne");
        Camera cam2 = new Camera(new VideoCapture(), "linkToCamOne");
        assertEquals(cam1, cam2);
    }

    /**
     * Tests if equals returns true if a Camera is compared to itself.
     */
    @Test
    void equalsTrueSameObjTest() {
        Camera cam1 = new Camera(new VideoCapture(), "linkToCamOne");
        assertEquals(cam1, cam1);
    }

    /**
     * Tests if isChanged is changed to true when a new frame is loaded.
     */
    @Test
    void isChangedTrueTest() {
        cameraHandler = new CameraHandler();
        cameraHandler.processFrames();
        cameraHandler.processFrames();
        assertTrue(cameraHandler.isChanged());
    }

    /**
     * Tests if isChanged is changed to false.
     * This should happen when getNewFrame gets called and all frames have already been handled.
     */
    @Test
    void videoGetsIsChangeFalseWhenNoMoreFramesAreNew() {
        //the video that is being loaded in is 4 seconds long in duration
        assertTimeout(Duration.ofSeconds(VIDEOLENGTH), this::loopToEndOfVideo);
    }

    /**
     * Test for getLink method.
     */
    @Test
    void getLinkTest() {
        cameraHandler = new CameraHandler();
        Camera cam = cameraHandler.addCamera(videoLink);
        assertEquals(videoLink, cam.getLink());
    }

    /**
     * Method that loops till the end of a the videolink video.
     */
    private void loopToEndOfVideo() {
        cameraHandler = new CameraHandler();
        Camera cam = cameraHandler.addCamera(videoLink);
        //sets isChanged to true
        cameraHandler.processFrames();
        cameraHandler.processFrames();
        //loop trough the video until last frame is acquired twice
        while (cam.isChanged()) {
            cameraHandler.processFrames();
        }
    }

}
