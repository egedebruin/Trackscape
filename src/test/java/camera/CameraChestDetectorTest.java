package camera;

import handlers.CameraHandler;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *
 */
class CameraChestDetectorTest {

    private final String shortVideoLinkWithBoxes = "files" + File.separator
        + "escaperoomwithopenbox.mov";
    private static final int SLEEPYTIME = 10;
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
        CameraHandler ch = new CameraHandler();
        ch.addCamera(shortVideoLinkWithBoxes);
        ch.processFrames();
        boolean found = false;
        while (ch.isChanged()) {
            ch.processFrames();
            if (ch.isChestFound()) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }
}
