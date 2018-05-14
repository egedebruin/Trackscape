import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import camera.Camera;
import java.io.File;

import org.junit.jupiter.api.Test;
import org.opencv.videoio.VideoCapture;

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

    @Test
    void addCameraTest() {
        CameraHandler c = new CameraHandler();

        assertEquals(0, c.getCameraList().size());
        c.addCamera(videoLink);
        assertEquals(1, c.getCameraList().size());
    }

    @Test
    void getNewFrameTest() {
        CameraHandler c = new CameraHandler();
        Camera cam = new Camera(new VideoCapture(videoLink), videoLink);
        assertNotNull(c.getNewFrame(cam));
    }
}