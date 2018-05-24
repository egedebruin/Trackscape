package camera;

import handlers.CameraHandler;
import org.junit.jupiter.api.Test;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
     * Test if divideFrame divides all pixels equally.
     */
    @Test
    void divideFrameTest() {
        String link = "files" + File.separator + "webcast.mov";
        VideoCapture videoCapture = new VideoCapture(link);
        camera = new Camera(videoCapture, link);
        Mat frame = camera.getLastFrame();

        assertNotNull(frame);

        camera.divideFrame(camera.getLastFrame());
        List<Mat> fp = camera.getFrameParts();

        assertNotNull(fp);
        assertNotEquals(new ArrayList<Mat>(), camera.getFrameParts());
        assertEquals(fp.get(0).size(), fp.get(1).size());   //equal divisions
    }

    /**
     * Verify that activity is added.
     */
    @Test
    void addActivityTest() {
        String link = "files" + File.separator + "webcast.mov";
        VideoCapture videoCapture = new VideoCapture(link);
        camera = new Camera(videoCapture, link);

        final int frames = 10;
        for (int i = 0; i < frames; i++) {
            camera.loadFrame();
        }
        Mat frame1 = camera.loadFrame();
        Mat frame2 = camera.loadFrame();
        int parts = camera.getFrames();

        assertNotNull(camera.getActivity());
        assertEquals(camera.getActivity().get(parts), new ArrayList());

        camera.setFrameCounter(0);
        camera.addActivity(frame1, parts, camera.getKnns().get(parts));
        assertEquals(camera.getActivity().get(parts), new ArrayList());

        camera.setFrameCounter(Integer.MAX_VALUE);
        camera.addActivity(frame2, parts, camera.getKnns().get(parts));

        assertNotEquals(camera.getActivity().get(parts), new ArrayList());
    }

    /**
     * Tests if equals returns false when 2 different cameras are compared.
     */
    @Test
    void equalsFalseDifferentCameraTest() {
        Camera cam1 = new Camera(new VideoCapture(), "linkToCamOne");
        Camera cam2 = new Camera(new VideoCapture(), "linkToCamTwo",
            DEFAULTNOOFCHEST);
        assertFalse(cam1.equals(cam2));
    }

    /**
     * Tests if equals returns false when Camera is compared to non Camera Object.
     */
    @Test
    void equalsFalseNoCameraTest() {
        Camera cam1 = new Camera(new VideoCapture(), "linkToCamOne");
        CameraChestDetector cameraChest = new CameraChestDetector();
        assertFalse(cam1.equals(cameraChest));
    }

    /**
     * Tests if equals returns true when 2 different cameras with the same link are compared.
     */
    @Test
    void equalsTrueDifObjTest() {
        Camera cam1 = new Camera(new VideoCapture(), "linkToCamOne");
        Camera cam2 = new Camera(new VideoCapture(), "linkToCamOne");
        assertTrue(cam1.equals(cam2));
    }

    /**
     * Tests if equals returns true if a Camera is compared to itself.
     */
    @Test
    void equalsTrueSameObjTest() {
        Camera cam1 = new Camera(new VideoCapture(), "linkToCamOne");
        assertTrue(cam1.equals(cam1));
    }

    /**
     * Tests if isChanged is changed to true when a new frame is loaded.
     */
    @Test
    void isChangedTrueTest() {
        cameraHandler = new CameraHandler();
        Camera cam = cameraHandler.addCamera(videoLink);
        cameraHandler.getNewFrame(cam);
        cameraHandler.getNewFrame(cam);
        assertTrue(cam.isChanged());
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
     * Method that loops till the end of a the videolink video.
     */
    private void loopToEndOfVideo() {
        cameraHandler = new CameraHandler();
        Camera cam = cameraHandler.addCamera(videoLink);
        //sets isChanged to true
        cameraHandler.getNewFrame(cam);
        cameraHandler.getNewFrame(cam);
        //loop trough the video until last frame is acquired twice
        while (cam.isChanged()) {
            cameraHandler.getNewFrame(cam);
        }
    }

}
