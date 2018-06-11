package camera;

import static org.junit.jupiter.api.Assertions.assertTrue;


import handlers.CameraHandler;
import java.io.File;
import org.junit.jupiter.api.Test;

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
     * @throws InterruptedException when interrupted
     */
    @Test
    void includeChestContoursInFrameCallTest() throws InterruptedException {
        CameraHandler ch = new CameraHandler();
        ch.addCamera(shortVideoLinkWithBoxes);
        ch.processFrames();
        while (ch.isChanged()) {
            ch.processFrames();
        }
        assertTrue(ch.isChestFound());
    }
}
