package camera;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import handlers.CameraHandler;
import java.io.File;

import org.junit.jupiter.api.Test;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

class CameraTest {

    private Camera camera;
    private CameraHandler cameraHandler;
    private final String videoLink = "files" + File.separator + "webcast.mov";

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            System.load(System.getProperty("user.dir")
                + File.separator + "libs" + File.separator + "opencv_ffmpeg341_64.dll");
            System.load(System.getProperty("user.dir")
                + File.separator + "libs" + File.separator + "opencv_java341.dll");
        }
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @Test
    void constructorTest() {
        camera = new Camera(null, null);
        assertNotNull(camera);
    }

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

    @Test
    void equalsFalseDifCamTest() {
        Camera cam1 = new Camera(new VideoCapture(),"linkToCamOne");
        Camera cam2 = new Camera(new VideoCapture(),"linkToCamTwo");
        assertFalse(cam1.equals(cam2));
    }

    @Test
    void equalsFalseNoCamTest() {
        Camera cam1 = new Camera(new VideoCapture(),"linkToCamOne");
        CameraChest cameraChest = new CameraChest();
        assertFalse(cam1.equals(cameraChest));
    }

    @Test
    void equalsTrueDifObjTest() {
        Camera cam1 = new Camera(new VideoCapture(),"linkToCamOne");
        Camera cam2 = new Camera(new VideoCapture(),"linkToCamOne");
        assertTrue(cam1.equals(cam2));
    }

    @Test
    void equalsTrueSameObjTest() {
        Camera cam1 = new Camera(new VideoCapture(),"linkToCamOne");
        assertTrue(cam1.equals(cam1));
    }

    @Test
    void isChangedTrueTest() {
        cameraHandler = new CameraHandler();
        Camera cam = cameraHandler.addCamera(videoLink);
        cameraHandler.getNewFrame(cam);
        cameraHandler.getNewFrame(cam);
        assertTrue(cam.isChanged());
    }

    @Test
    void isChangedFalseTest() {
        cameraHandler = new CameraHandler();
        Camera cam = cameraHandler.addCamera(videoLink);
        //sets isChanged to true
        cameraHandler.getNewFrame(cam);
        cameraHandler.getNewFrame(cam);
        //loop trough the video until last frame is acquired twice
        while (cam.isChanged()) {
            cameraHandler.getNewFrame(cam);
        }
        assertFalse(cam.isChanged());

    }

}