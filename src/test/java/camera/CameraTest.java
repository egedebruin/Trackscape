package camera;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import handlers.CameraHandler;
import java.io.File;

import org.junit.jupiter.api.Test;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

/**
 * Test Camera class.
 */
class CameraTest {

    /**
     * Class parameters.
     */
    private Camera camera;
    private CameraHandler cameraHandler;
    private final String videoLink = "files" + File.separator + "webcast.mov";

    /**
     * Set up the testing environment.
     */
    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            System.load(System.getProperty("user.dir")
                + File.separator + "libs" + File.separator
                + "opencv_ffmpeg341_64.dll");
            System.load(System.getProperty("user.dir")
                + File.separator + "libs" + File.separator
                + "opencv_java341.dll");
        }
    }

    /**
     * tearDown.
     */
    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    /**
     * Test if new constructor is created.
     */
    @Test
    void constructorTest() {
        camera = new Camera(null, null);
        assertNotNull(camera);
    }

    /**
     * Verify that frames change in the video.
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
     * Verify that two different cameralinks do not result in the same camera.
     */
    @Test
    void equalsFalseDifCamTest() {
        Camera cam1 = new Camera(new VideoCapture(), "linkToCamOne");
        Camera cam2 = new Camera(new VideoCapture(), "linkToCamTwo");
        assertFalse(cam1.equals(cam2));
    }

    /**
     * Verify that cameraChest is not a camera object.
     */
    @Test
    void equalsFalseNoCamTest() {
        Camera cam1 = new Camera(new VideoCapture(), "linkToCamOne");
        CameraChest cameraChest = new CameraChest();
        assertFalse(cam1.equals(cameraChest));
    }

    /**
     * Test the equality of two cameras with the same url link.
     */
    @Test
    void equalsTrueDifObjTest() {
        Camera cam1 = new Camera(new VideoCapture(), "linkToCamOne");
        Camera cam2 = new Camera(new VideoCapture(), "linkToCamOne");
        assertTrue(cam1.equals(cam2));
    }

    /**
     * Test if camera object is equal to itself.
     */
    @Test
    void equalsTrueSameObjTest() {
        Camera cam1 = new Camera(new VideoCapture(), "linkToCamOne");
        assertTrue(cam1.equals(cam1));
    }

    /**
     * Verify that isChanged results in true
     * when a different frame is retrieved.
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
     * Verify that isChanged results in false when video has ended.
     */
    @Test
    void isChangedFalseTest() {
        cameraHandler = new CameraHandler();
        Camera cam = cameraHandler.addCamera(videoLink);
        //sets isChanged to true
        cameraHandler.getNewFrame(cam);
        cameraHandler.getNewFrame(cam);
        //loop trough the video until last frame is acquired twice
        while (cam.isChanged()) {
            cameraHandler.getNewFrame(cam);
        }
        assertFalse(cam.isChanged());
    }

}
