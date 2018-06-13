package gui.controllers;

import java.util.concurrent.TimeUnit;
import javafx.animation.AnimationTimer;

/**
 * TimerController class for controlling GUI elements.
 */
public class MainController {

    /**
     * Class parameters.
     */
    private AnimationTimer animationTimer;
    private RoomController roomController;
    private TimeLogController timeLogController;
    private VideoController videoController;

    /**
     * Constructor method.
     */
    public MainController(final RoomController roomControl, final TimeLogController timeLogControl,
                          final VideoController videoControl) {
        roomController = roomControl;
        timeLogController = timeLogControl;
        videoController = videoControl;
    }

    /**
     * startTimer.
     * Call updateImageViews method every period of time to retrieve a new frame
     */
    public void startTimer() {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(final long now) {
                videoController.update(now);
                if (videoController.isClosed()) {
                    stopTimer();
                    return;
                }
                roomController.update(now);
                timeLogController.update(now);
            }
        };
        timeLogController.clearInformationArea();
        animationTimer.start();
    }

    /**
     * Method that closes a stream.
     */
    public void stopTimer() {
        if (animationTimer != null) {
            animationTimer.stop();
        }
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (roomController.isConfigured()) {
            roomController.closeController();
        }
        timeLogController.closeController();
        videoController.closeController();
    }
}
