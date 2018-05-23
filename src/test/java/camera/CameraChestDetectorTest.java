package camera;

import static org.junit.jupiter.api.Assertions.assertTrue;

import handlers.CameraHandler;

import java.io.File;

import org.junit.jupiter.api.Test;

/**
 *
 */
class CameraChestDetectorTest {

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

    /**
     * Test that checks whether includecontoursinframe gets called.
     */
    @Test
    void includeChestContoursInFrameCallTest() {
        CameraHandler ch = new CameraHandler();
        Camera camera = ch.addCamera(shortVideoLink);
        ch.getNewFrame(camera);

        //if cameraChest.isOpened it means that includeContoursInFrame gets called.
        assertTrue(ch.getCameraChestDetector().getIsOpened());
    }
}