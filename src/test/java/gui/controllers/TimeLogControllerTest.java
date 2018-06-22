package gui.controllers;

import com.sun.javafx.application.PlatformImpl;
import handlers.CameraHandler;
import java.util.concurrent.TimeUnit;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.io.File;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;


import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for TimeLogController.
 */
public class TimeLogControllerTest {

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
     * Test the constructor for TimeLogController object.
     */
    @Test
    void constructorTest() {
        CameraHandler camHandler = new CameraHandler();
        Controller.setCameraHandler(camHandler);
        TimeLogController timeLogController = new TimeLogController();

        assertNotNull(timeLogController);
        assertNotNull(Controller.getCameraHandler().getInformationHandler());
    }

    /**
     * Test the changeTime method.
     */
    @Test
    void changeTimeTest() {
        PlatformImpl.startup(() -> { });
        CameraHandler camHandler = new CameraHandler();
        camHandler.setBeginTime(0);
        Controller.setCameraHandler(camHandler);
        TimeLogController controller = new TimeLogController();

        Label testLabel = new Label();
        controller.setTimerLabel(testLabel);
        controller.changeTime(TimeUnit.SECONDS.toNanos(2));

        assertEquals("00:00:02", testLabel.getText());
    }

    /**
     * Test that addInformation adds information to area.
     */
    @Test
    void addInformationTest() {
        PlatformImpl.startup(() -> {
        });
        CameraHandler camHandler = new CameraHandler();
        camHandler.setBeginTime(0);
        Controller.setCameraHandler(camHandler);
        TimeLogController controller = new TimeLogController();

        TextArea textArea = new TextArea();
        controller.setInformationBox(textArea);
        controller.addInformation("ajax");

        assertTrue(textArea.getText().contains("ajax"));
    }

    /**
     * Test confirm and not confirm chest adds and removes button.
     */
    @Test
    void confirmAndNotConfirmChestTest() {
        PlatformImpl.startup(() -> { });
        CameraHandler camHandler = new CameraHandler();
        camHandler.setBeginTime(0);
        Controller.setCameraHandler(camHandler);
        TimeLogController controller = new TimeLogController();

        TextArea textArea = new TextArea();
        controller.setInformationBox(textArea);
        controller.setApproveButton(new Button());
        controller.setNotApproveButton(new Button());
        controller.setQuestion(new Label());
        controller.setImageView(new ImageView());
        controller.setTimeStamp(new Label());
        controller.confirmedChest("1/2");

        assertTrue(textArea.getText().contains("chest"));
        controller.clearInformationArea();
        assertFalse(textArea.getText().contains("chest"));
        controller.unConfirm();
        assertFalse(textArea.getText().contains("chest"));
    }

    /**
     * Test checkInformation checks for new information.
     */
    @Test
    void checkInformationTest() {
        PlatformImpl.startup(() -> { });
        CameraHandler camHandler = new CameraHandler();
        camHandler.setBeginTime(0);
        Controller.setCameraHandler(camHandler);
        TimeLogController controller = new TimeLogController();

        camHandler.getInformationHandler().addInformation("info");
        TextArea textArea = new TextArea();
        controller.setInformationBox(textArea);

        controller.checkInformation();
        assertTrue(textArea.getText().contains("info"));
        controller.clearInformationArea();
        controller.checkInformation();
        assertTrue(textArea.getText().isEmpty());
    }

    /**
     * Test checkMatInformation creates buttons when new mat included.
     */
    @Test
    void checkMatTest() {
        PlatformImpl.startup(() -> { });
        CameraHandler camHandler = new CameraHandler();
        camHandler.setBeginTime(0);
        Controller.setCameraHandler(camHandler);
        TimeLogController controller = new TimeLogController();

        Mat mat = Imgcodecs.imread("files/test/subt3.png");

        camHandler.getInformationHandler().addMatrix(new Pair<>(mat, (long) 2));

        Button button = new Button();
        controller.setInformationBox(new TextArea());
        controller.setApproveButton(button);
        controller.setNotApproveButton(new Button());
        controller.setQuestion(new Label());
        controller.setImageView(new ImageView());
        controller.setTimeStamp(new Label());

        controller.checkMatInformation();

        assertTrue(button.isVisible());
    }
}
