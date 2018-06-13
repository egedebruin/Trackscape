package camera;

import org.junit.jupiter.api.Test;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *
 */
class CameraChestDetectorTest {

    private final String shortVideoLinkWithBoxes = "files" + File.separator
        + "openchest.png";
    private static final int MAGICTIMEOUT = 15;

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
     * Test that checks whether includecontoursinframe gets called.
     * @throws InterruptedException when interrupted
     */
    @Test
    void includeChestContoursInFrameCallTest() throws InterruptedException {
        CameraChestDetector cameraChestDetector = new CameraChestDetector();



        VideoCapture vc = new VideoCapture(shortVideoLinkWithBoxes);
        vc.open(shortVideoLinkWithBoxes);

        Camera cam = new Camera(vc, shortVideoLinkWithBoxes, 1);

        Mat frame = cam.getLastFrame();

        Mat hsv = Mat.ones(frame.size(), frame.type());

        List<Mat> mats = cameraChestDetector.includeChestContoursInFrame(frame, hsv);

        assertTrue(mats.size() > 0);

    }
}
