package gui.controllers;

import camera.Camera;
import gui.MonitorScene;
import handlers.CameraHandler;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javafx.animation.AnimationTimer;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * MainController class for controlling GUI elements.
 */
public class MainController {

    /**
     * Class parameters.
     */
    private CameraHandler cameraHandler;
    private AnimationTimer animationTimer;
    private VideoController videoController;
    private TimeLogController timeLogController;
    private RoomController roomController;
    private boolean configured = false;
    private boolean videoPlaying = false;
    private List<AnimationTimer> streamTimers = new ArrayList<>();

    /**
     * Constructor method.
     */
    public MainController() {
        cameraHandler = new CameraHandler();
        videoController = new VideoController(cameraHandler);
        timeLogController = new TimeLogController(cameraHandler);
        roomController = new RoomController();
    }

    /**
     * Method to show a popup in which
     * you can specify a stream url to initialize a connection.
     *
     * @param streamStage the popup window
     * @param field the specified url.
     */
    public void createStream(final Stage streamStage, final TextField field) {
        String streamUrl = field.getText();
        streamStage.close();
        Camera camera = cameraHandler.addCamera(streamUrl);
        AnimationTimer streamTimer = new AnimationTimer() {
            @Override
            public void handle(final long now) {
                camera.loadFrame();
            }
        };
        streamTimers.add(streamTimer);
        streamTimer.start();
    }

    /**
     * Method to initialize a connection with a video.
     *
     * @param file the video file
     */
    public void createVideo(final File file) {
        String fileUrl = file.toString();
        cameraHandler.addCamera(fileUrl);
    }

    /**
     * grabTimeFrame.
     * Call updateImageViews method every period of time to retrieve a new frame
     * @param imageViews list of panels that show the frames
     */
    public void grabTimeFrame(final List<ImageView> imageViews) {
        for (AnimationTimer streamTimer : streamTimers) {
            streamTimer.stop();
        }
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(final long now) {
                videoController.updateImageViews(imageViews);
                if (videoController.isClosed()) {
                    closeStream();
                }
                roomController.update(now);
                timeLogController.processFrame(now);
            }
        };
        timeLogController.clearInformationArea();
        animationTimer.start();
    }

    /**
     * Method that closes a stream.
     */
    public void closeStream() {
        if (animationTimer != null) {
            animationTimer.stop();
        }
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        cameraHandler.closeHandler();
        timeLogController.closeController();
        if (roomController != null && configured) {
            roomController.closeController();
        }
        configured = false;
        videoPlaying = false;
    }

    /**
     * proceedToMonitorScene.
     * Move on to the next stage
     * @param ms the monitorScene
     * @param primaryStage starting stage
     * @param stylesheet current stylesheet
     */
    public final void proceedToMonitorScene(final MonitorScene ms, final Stage primaryStage,
                                            final String stylesheet) {
        primaryStage.setScene(
            ms.createMonitorScene(
                primaryStage, stylesheet));
    }

    /**
     * Load the configuration file.
     * @param jsonHandler the current jsonHandler
     */
    public void configure(final String jsonHandler) {
        roomController.configure(jsonHandler);

        // Set the correct cameraHandlers.
        for (int i = 0; i < cameraHandler.listSize(); i++) {
            roomController.getCameraHandler().addCamera(cameraHandler.getCamera(i).getLink());
        }
        cameraHandler = roomController.getCameraHandler();
        timeLogController.setCameraHandler(cameraHandler);
        videoController.setCameraHandler(cameraHandler);
        configured = true;
    }

    /**
     * Get the number of active cameras.
     * @return number of cameras
     */
    public int getCameras() {
        return cameraHandler.listSize();
    }

    /**
     * Get the status of the configuration.
     * @return configured
     */
    public boolean getConfigured() {
        return configured;
    }

    /**
     * Get the status of the videoPlaying.
     * @return videoPlaying
     */
    public boolean isVideoPlaying() {
        return videoPlaying;
    }

    /**
     * Set the status of the videoPlaying.
     * @param isVideoPlaying boolean value for whether videos are playing
     */
    public void setVideoPlaying(final boolean isVideoPlaying) {
        this.videoPlaying = isVideoPlaying;
    }

    /**
     * Get the TimeLogController.
     * @return The TimeLogController
     */
    public TimeLogController getTimeLogController() {
        return timeLogController;
    }

    /**
     * Get the RoomController.
     * @return The RoomController
     */
    public RoomController getRoomController() {
        return roomController;
    }
}
