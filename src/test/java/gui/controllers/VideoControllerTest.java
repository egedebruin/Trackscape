package gui.controllers;

import com.sun.javafx.application.PlatformImpl;
import handlers.CameraHandler;
import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test the VideoController class.
 */
public class VideoControllerTest {
    private String videoLink = "files" + File.separator + "postit.mov";
    private File file = new File(videoLink);

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
     * Test the constructor for VideoController object.
     */
    @Test
    void constructorTest() {
        VideoController vc = new VideoController();
        assertNotNull(vc);
        assertTrue(vc.isClosed());
    }

    /**
     * Verify that frames are being returned.
     */
    @Test
    void updateTest() {
        VideoController videoController = new VideoController();
        videoController.setCameraHandler(new CameraHandler());
        assertTrue(videoController.isClosed());

        videoController.createVideo(file);
        assertEquals(videoController.getCameras(), 1);

        ArrayList<ImageView> ivs = new ArrayList();
        ivs.add(new ImageView());
        videoController.setImageViews(ivs);
        assertTrue(videoController.isClosed());

        videoController.update(0);
        assertFalse(videoController.isClosed());
        assertNotNull(videoController.getImageViews().get(0));
        Image im = videoController.getImageViews().get(0).getImage();

        videoController.update(0);
        assertNotEquals(im, videoController.getImageViews().get(0).getImage());
    }

    /**
     * Test that controller is correctly closed.
     */
    @Test
    void closeControllerTest() {
        PlatformImpl.startup(() -> { });
        VideoController vc = new VideoController();
        CameraHandler camHandler = new CameraHandler();
        vc.setCameraHandler(camHandler);

        vc.closeController();
        assertTrue(vc.isClosed());

        Image currentImage;
        for (ImageView imageView : vc.getImageViews()) {
            currentImage = imageView.getImage();
            assertNotNull(imageView.getImage());
            assertEquals(currentImage, imageView.getImage());
        }
    }

    /**
     * Test if adding stream is correctly handled.
     */
    @Test
    void createStream() {
        PlatformImpl.startup(() -> { });
        VideoController vc = new VideoController();
        CameraHandler camHandler = new CameraHandler();
        vc.setCameraHandler(camHandler);
        TextField tf = new TextField();
        tf.setText(videoLink);

        Platform.runLater(() -> {
        assertEquals(vc.getCameras(), 0);
        vc.createStream(new Stage(), tf);
        assertEquals(vc.getCameras(), 1);
        });
    }

    /**
     * Test if adding videos is correctly handled.
     */
    @Test
    void createVideoTest() {
        VideoController vc = new VideoController();
        CameraHandler camHandler = new CameraHandler();
        vc.setCameraHandler(camHandler);

        assertEquals(vc.getCameras(), 0);
        vc.createVideo(file);
        assertEquals(vc.getCameras(), 1);
    }

    /**
     * Test the setClosed method.
     */
    @Test
    void setClosedTest() {
        VideoController vc = new VideoController();
        assertTrue(vc.isClosed());
        vc.setClosed(false);
        assertFalse(vc.isClosed());
    }
}