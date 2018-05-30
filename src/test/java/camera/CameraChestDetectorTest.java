package camera;

import handlers.CameraHandler;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *
 */
class CameraChestDetectorTest {

    private final String shortVideoLinkWithBoxes = "files" + File.separator
        + "escaperoomwithopenbox.mov";

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
     */
    @Test
    void includeChestContoursInFrameCallTest() {
        CameraHandler ch = new CameraHandler();
        ch.addCamera(shortVideoLinkWithBoxes);
        boolean isDetected = false;
        ch.processFrames();
        while (ch.isChanged()) {
            ch.processFrames();
            if (ch.isChestDetected()) {
                isDetected = true;
                break;
            }
        }

        assertTrue(isDetected);
    }
}
