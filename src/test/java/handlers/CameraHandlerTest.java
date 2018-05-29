package handlers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


import camera.Camera;
import java.io.File;
import org.junit.jupiter.api.Test;

/**
 * Class for testing CameraHandler.
 */
class CameraHandlerTest {

    private final String videoLink = "files" + File.separator + "webcast.mov";

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
     * Test the constructor with information handler.
     */
    @Test
    void constructorTest() {
        InformationHandler handler = new InformationHandler();
        CameraHandler c = new CameraHandler(handler);
        assertEquals(handler, c.getInformationHandler());
    }
    /**
     * Tests if a camera is added when addCamera is used.
     */
    @Test
    void addCameraTest() {
        final int chestNumber = 3;
        CameraHandler c = new CameraHandler();

        assertEquals(0, c.listSize());
        c.addCamera(videoLink);
        assertEquals(1, c.listSize());
        c.addCamera(videoLink, chestNumber);
        assertEquals(2, c.listSize());
    }

    /**
     * Tests if a non null frame is returned.
     * Checks this when getNewFrame is called with a correctly initialized camera
     */
    @Test
    void getNewFrameTest() {
        CameraHandler c = new CameraHandler();
        assertNotNull(c.processFrames());
    }

    /**
     * Tests if the cameraList gets cleared whenever clearList() is called.
     */
    @Test
    void clearListTest() {
        CameraHandler ch = new CameraHandler();
        ch.addCamera(videoLink);

        assertTrue(ch.listSize() > 0);
        ch.clearList();
        assertEquals(0, ch.listSize());
    }

    /**
     * Tests if a camera is correctly returned after getCamera(int index) is called.
     */
    @Test
    void getCameraTest() {
        CameraHandler ch = new CameraHandler();
        Camera camera = ch.addCamera(videoLink);
        assertEquals(camera, ch.getCamera(0));
    }

    /**
     * Tests if whenever a camera with a wrong link is being added this camera isn't actually added.
     */
    @Test
    void addNullCameraTest() {
        CameraHandler ch = new CameraHandler();
        Camera camera = ch.addCamera("WrongVideoLink");
        assertNull(camera);
        assertEquals(0, ch.listSize());
    }

    /**
     * Test that the camera handler isn't active from the start.
     */
    @Test
    void testIsActive() {
        CameraHandler ch = new CameraHandler();
        assertFalse(ch.isActive());
    }
}