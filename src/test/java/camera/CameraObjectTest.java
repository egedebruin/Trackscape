package camera;

import static camera.CameraObject.bgrToHsv;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

/**
 * Test CameraObject class.
 */
class CameraObjectTest {

    /**
     * Class parameters.
     */
    private final String shortVideoLink
        = "files" + File.separator + "postit.mov";

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
     * Test the conversion of mat to hsv colour space.
     * @throws IOException exception
     */
    @Test
    void bgrToHsvTest() throws IOException {
        VideoCapture videoCapture = new VideoCapture(shortVideoLink);
        Camera camera = new Camera(videoCapture, shortVideoLink);

        Mat mat = camera.getLastFrame();

        assertNotNull(mat);

        // turns mat in into hsv colour space
        Mat mat2 = bgrToHsv(mat);
        assertNotEquals(mat, mat2);
    }
}
