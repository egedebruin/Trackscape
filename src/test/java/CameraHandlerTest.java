import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import camera.Camera;
import handlers.CameraHandler;
import java.io.File;
import org.junit.jupiter.api.Test;
import org.opencv.videoio.VideoCapture;

/**
 * Test CameraHandler class.
 */
class CameraHandlerTest {

    /**
     * Class parameters.
     */
    private final String videoLink = "files" + File.separator + "webcast.mov";

    static {
        // These should be at the start of the application,
        // so if the main changes this should be included.
        // Load OpenCV library.
        System.load(System.getProperty("user.dir")
            + File.separator + "libs" + File.separator
            + "opencv_ffmpeg341_64.dll");
        System.load(System.getProperty("user.dir")
            + File.separator + "libs" + File.separator
            + "opencv_java341.dll");
    }

    /**
     * Test if camera is added to the list.
     */
    @Test
    void addCameraTest() {
        CameraHandler c = new CameraHandler();

        assertEquals(0, c.listSize());
        c.addCamera(videoLink);
        assertEquals(1, c.listSize());
    }

    /**
     * Test if new frame is retrieved.
     */
    @Test
    void getNewFrameTest() {
        CameraHandler c = new CameraHandler();
        Camera cam = new Camera(new VideoCapture(videoLink), videoLink);
        assertNotNull(c.getNewFrame(cam));
    }

    /**
     * Test list operations for cameraHandler.
     */
    @Test
    void clearListTest() {
        CameraHandler ch = new CameraHandler();
        ch.addCamera(videoLink);

        assertTrue(ch.listSize() > 0);
        ch.clearList();
        assertTrue(ch.listSize() == 0);
    }

    /**
     * Verify that correct camera is added to the list.
     */
    @Test
    void getCameraTest() {
        CameraHandler ch = new CameraHandler();
        Camera camera = ch.addCamera(videoLink);
        assertEquals(camera, ch.getCamera(0));
    }

    /**
     * Verify that wrong link does not result into a camera object.
     */
    @Test
    void addNullCameraTest() {
        CameraHandler ch = new CameraHandler();
        Camera camera = ch.addCamera("WrongVideoLink");
        assertNull(camera);
    }
}
