package camera;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


import java.io.File;
import org.junit.jupiter.api.Test;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;


/**
 * Class for testing cameraObjectDetector.
 */
class CameraObjectDetectorTest {

    private final String shortVideoLink = "files" + File.separator + "postit.mov";
    private static final int DEFAULTNOOFCHEST = 10;

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
     * Tests if when bgrtohsv is called a non null hsv frame is returned.
     * This hsv Mat should be different than the bgr Mat
     */
    @Test
    void bgrToHsvTest() {
        CameraObjectDetector cameraObjectDetector = new CameraChestDetector();
        VideoCapture videoCapture = new VideoCapture(shortVideoLink);
        Camera cam = new Camera(videoCapture, shortVideoLink, DEFAULTNOOFCHEST);

        Mat mat = cam.getLastFrame();

        assertNotNull(mat);

        //turns mat in into hsv colour space
        Mat mat2 = cameraObjectDetector.bgrToHsv(mat);
        assertNotEquals(mat, mat2);
    }

    /**
     * Tests if the frame is subtracted.
     * When another frame is subtracted, the result should not be the same when the same frame is
     * subtracted.
     */
    @Test
    void subtractFrameTest() {
        CameraObjectDetector cameraObjectDetector = new CameraChestDetector();
        VideoCapture videoCapture = new VideoCapture(shortVideoLink);
        Camera camera = new Camera(videoCapture, shortVideoLink);

        Mat mat = camera.getLastFrame();

        assertNotEquals(cameraObjectDetector.subtractFrame(mat), mat);
    }
}
