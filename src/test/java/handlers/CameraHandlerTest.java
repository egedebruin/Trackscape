package handlers;

import camera.Camera;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
     * Tests if non-null frames are returned.
     * Checks this when processFrames is called with a correctly initialized camera
     */
    @Test
    void processFramesTest() {
        CameraHandler c = new CameraHandler();
        c.addCamera(videoLink);
        assertNotNull(c.processFrames());
    }

    /**
     * Tests if the cameraList gets cleared whenever clearList() is called.
     */
    @Test
    void clearListsTest() {
        CameraHandler ch = new CameraHandler();
        ch.addCamera(videoLink);

        assertTrue(ch.listSize() > 0);
        ch.clearLists();
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
     * Test isChanged method.
     */
    @Test
    void testIsChanged() {
        CameraHandler ch  = new CameraHandler();
        ch.addCamera(videoLink);
        assertFalse(ch.isChanged());
        ch.processFrames();
        assertTrue(ch.isChanged());
    }

    /**
     * Test closeHandler method.
     */
    @Test
    void testCloseHandler() {
        CameraHandler ch  = new CameraHandler();
        ch.addCamera(videoLink);
        ch.closeHandler();
        assertEquals(-1, ch.getBeginTime());
        assertEquals(0, ch.listSize());
    }

    /**
     * Test setInformation method.
     */
    @Test
    void testSetInformationHandler() {
        InformationHandler handler = new InformationHandler();
        InformationHandler handler2 = new InformationHandler();
        CameraHandler ch  = new CameraHandler(handler);
        assertEquals(handler, ch.getInformationHandler());

        ch.setInformationHandler(handler2);
        assertEquals(handler2, ch.getInformationHandler());
        assertNotEquals(handler, ch.getInformationHandler());
    }
}