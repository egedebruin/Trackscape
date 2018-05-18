import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import camera.Camera;
import handlers.CameraHandler;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

class CameraHandlerTest {

    private final String videoLink = "files" + File.separator + "webcast.mov";
    private final String shortVideoLink = "files" + File.separator + "postit.mov";

    static {
        // These should be at the start of the application,
        // so if the main changes this should be included.
        // Load OpenCV library.
        System.load(System.getProperty("user.dir")
            + File.separator + "libs" + File.separator + "opencv_ffmpeg341_64.dll");
        System.load(System.getProperty("user.dir")
            + File.separator + "libs" + File.separator + "opencv_java341.dll");
    }

    @Test
    void addCameraTest() {
        CameraHandler c = new CameraHandler();

        assertEquals(0, c.listSize());
        c.addCamera(videoLink);
        assertEquals(1, c.listSize());
    }

    @Test
    void getNewFrameTest() {
        CameraHandler c = new CameraHandler();
        Camera cam = new Camera(new VideoCapture(videoLink), videoLink);
        assertNotNull(c.getNewFrame(cam));
    }

    @Test
    void bgrToHsvTest() throws IOException {
        VideoCapture videoCapture = new VideoCapture(shortVideoLink);
        Camera camera = new Camera(videoCapture, shortVideoLink);

        Mat mat = camera.getLastFrame();

        assertNotNull(mat);

        //turns mat in into hsv colour space
        Mat mat2 = new CameraHandler().bgrToHsv(mat);
        assertNotEquals(mat,mat2);
    }

    @Test
    void includeChestContoursInFrameCallTest() {
        CameraHandler ch = new CameraHandler();
        Camera camera = ch.addCamera(shortVideoLink);
        ch.getNewFrame(camera);

        //if cameraChest.isOpened it means that includeContoursInFrame gets called.
        assertTrue(ch.cameraChest.isOpened);
    }

    @Test
    void clearListTest() {
        CameraHandler ch = new CameraHandler();
        ch.addCamera(shortVideoLink);

        assertTrue(ch.listSize() > 0);
        ch.clearList();
        assertTrue(ch.listSize() == 0);
    }

    @Test
    void getCameraTest() {
        CameraHandler ch = new CameraHandler();
        Camera camera = ch.addCamera(shortVideoLink);
        assertEquals(camera,ch.getCamera(0));
    }

    @Test
    void addNullCameraTest() {
        CameraHandler ch = new CameraHandler();
        Camera camera = ch.addCamera("WrongVideoLink");
        assertNull(camera);
    }
}