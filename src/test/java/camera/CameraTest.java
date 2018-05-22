package camera;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

/**
 * Test for the Camera class.
 */
class CameraTest {

    /**
     * Class parameters.
     */
    private Camera camera;

    /**
     * Set up the environment to be able to test.
     */
    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            System.load(System.getProperty("user.dir")
                + File.separator + "libs"
                + File.separator + "opencv_ffmpeg341_64.dll");
            System.load(System.getProperty("user.dir")
                + File.separator + "libs"
                + File.separator + "opencv_java341.dll");
        }
    }

    /**
     * tearDown.
     */
    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    /**
     * Test if constructor works.
     */
    @Test
    void constructorTest() {
        camera = new Camera(null, null);
        assertNotNull(camera);
    }

    /**
     * Test if last frame is retrieved.
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

        int frameSize = frame.width() * frame.height();
        int sumOfFrameParts = 0;
        for (int i = 0; i < fp.size(); i++) {
            sumOfFrameParts = sumOfFrameParts + (fp.get(i).height() * fp.get(i).width());
        }

        assertEquals(fp.get(0).size(), fp.get(1).size());   //equal divisions
    }

}
