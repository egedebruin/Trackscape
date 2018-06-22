package gui.controllers;

import com.sun.javafx.application.PlatformImpl;
import handlers.CameraHandler;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for RoomController.
 */
public class RoomControllerTest {
    private String testConfigFile = "files/test/testConfig.json";

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
     * Test the configure method.
     */
    @Test
    void configureTest() {
        PlatformImpl.startup(() -> {
        });

        RoomController controller = new RoomController();
        final int numberOfChests = 3;
        CameraHandler camHandler = new CameraHandler();
        controller.setCameraHandler(camHandler);
        assertNull(controller.getProgress());
        assertFalse(controller.isConfigured());

        controller.configure(testConfigFile);
        assertNotNull(controller.getProgress());
        assertEquals(controller.getChestTimeStampList().size(), numberOfChests);
        assertTrue(controller.isConfigured());

        controller.getProgress().getApiHandler().stopServer();
    }

    /**
     * Test if controller is correctly closed.
     */
    @Test
    void closeControllerTest() {
        PlatformImpl.startup(() -> {
        });

        RoomController controller = new RoomController();
        CameraHandler camHandler = new CameraHandler();
        controller.setCameraHandler(camHandler);
        controller.configure(testConfigFile);
        assertTrue(controller.isConfigured());
        assertNotNull(controller.getProgress());

        controller.getProgress().getApiHandler().stopServer();

        controller.closeController();
        assertFalse(controller.isConfigured());
        assertNull(controller.getProgress());
        assertFalse(controller.isBehindSchedule());
        assertFalse(controller.getSnoozeHint());
    }

    /**
     * Test whether the hintTimer starts correctly or not.
     */
    @Test
    void startHintTimerTest() {
        RoomController controller = new RoomController();
        assertFalse(controller.getSnoozeHint());
        controller.startHintTimer();
        assertTrue(controller.getSnoozeHint());
    }

    /**
     * Test newItemDone method.
     */
    @Test
    void newItemDoneTest() {
        final int itemsDone = 46;
        PlatformImpl.startup(() -> {
        });

        RoomController controller = new RoomController();
        CameraHandler camHandler = new CameraHandler();
        controller.setCameraHandler(camHandler);
        controller.configure(testConfigFile);

        controller.newItemDone(itemsDone);

        assertEquals(2, controller.getProgress().getRoom().getChestsOpened());
        controller.getProgress().getApiHandler().stopServer();
    }

    /**
     * Test itemsRemoved method.
     */
    @Test
    void itemsRemovedTest() {
        final int itemsDone = 46;
        PlatformImpl.startup(() -> {
        });

        RoomController controller = new RoomController();
        CameraHandler camHandler = new CameraHandler();
        controller.setCameraHandler(camHandler);
        controller.configure(testConfigFile);

        controller.itemsRemoved(itemsDone);

        assertEquals(2, controller.getProgress().getRoom().getChestsOpened());
        controller.getProgress().getApiHandler().stopServer();
    }

    /**
     * Test updateActivity method.
     */
    @Test
    void updateActivityTest() {
        PlatformImpl.startup(() -> {
        });

        RoomController controller = new RoomController();
        CameraHandler camHandler = new CameraHandler();
        controller.setCameraHandler(camHandler);
        controller.configure(testConfigFile);

        Label label = new Label();
        controller.setActivityStatus(label);

        controller.updateActivity();
        assertTrue(label.getText().contains("zero"));
        controller.getProgress().getApiHandler().stopServer();
    }

    /**
     * Test updateChests method.
     */
    @Test
    void updateChestsTest() {
        PlatformImpl.startup(() -> {
        });

        RoomController controller = new RoomController();
        CameraHandler camHandler = new CameraHandler();
        controller.setCameraHandler(camHandler);
        controller.configure(testConfigFile);

        Label label = new Label();
        controller.setNumOfChestsOpened(label);

        controller.updateChests(1);
        assertTrue(label.getText().contains("1/3"));
        controller.getProgress().getApiHandler().stopServer();
    }

    /**
     * Test changeInformation method.
     */
    @Test
    void changeInformationTest() {
        PlatformImpl.startup(() -> {
        });

        RoomController controller = new RoomController();
        CameraHandler camHandler = new CameraHandler();
        controller.setCameraHandler(camHandler);
        controller.configure(testConfigFile);

        Label label = new Label();
        controller.setGameStatus(label);
        camHandler.setBeginTime(0);
        controller.setProgressBar(new GridPane());

        controller.changeInformation(1);
        assertTrue(label.getText().contains("started"));

        controller.getProgress().getRoom().setTargetDuration(0);
        controller.changeInformation(1);
        assertTrue(label.getText().contains("ended"));

        controller.getProgress().getApiHandler().stopServer();
    }

    /**
     * Test updateWarning method.
     */
    @Test
    void updateWarningPaneTest() {
        PlatformImpl.startup(() -> {
        });

        RoomController controller = new RoomController();
        CameraHandler camHandler = new CameraHandler();
        controller.setCameraHandler(camHandler);
        controller.configure(testConfigFile);

        Pane pane = new Pane();
        Pane pane2 = new Pane();
        pane.getChildren().add(new Pane());
        pane.getChildren().add(new Pane());
        pane.getChildren().add(pane2);
        controller.setStatusPane(pane);

        controller.updateWarningPane();
        assertFalse(pane2.isVisible());

        controller.getProgress().getApiHandler().stopServer();
    }

    /**
     * Test changeTime method.
     */
    @Test
    void changeTimeTest() {
        PlatformImpl.startup(() -> {
        });

        RoomController controller = new RoomController();
        CameraHandler camHandler = new CameraHandler();
        camHandler.setBeginTime(1);
        controller.setCameraHandler(camHandler);
        controller.configure(testConfigFile);

        GridPane pane = new GridPane();
        pane.getChildren().add(new Pane());
        controller.setProgressBar(pane);
        controller.getProgress().updateProgress();
        controller.fillProgress(2);
        controller.changeTime(1);

        List<Label> labels = controller.getChestTimeStampList();
        assertEquals("00:00", labels.get(0).getText());
        assertEquals("", labels.get(1).getText());
        assertEquals("", labels.get(2).getText());

        controller.getProgress().getApiHandler().stopServer();
    }

    /**
     * Test update method doesn't crash.
     */
    @Test
    void updateTest() {
        PlatformImpl.startup(() -> { });

        RoomController controller = new RoomController();
        CameraHandler camHandler = new CameraHandler();
        camHandler.setBeginTime(1);
        controller.setCameraHandler(camHandler);
        controller.configure(testConfigFile);

        controller.setProgressBar(new GridPane());
        controller.setGameStatus(new Label());
        controller.setNumOfChestsOpened(new Label());
        controller.setActivityStatus(new Label());

        Pane pane = new Pane();
        pane.getChildren().add(new Pane());
        pane.getChildren().add(new Pane());
        pane.getChildren().add(new Pane());

        controller.setStatusPane(pane);
        controller.update(1);

        controller.getProgress().getApiHandler().stopServer();
        assertFalse(pane.getChildren().get(2).isVisible());
    }

}
