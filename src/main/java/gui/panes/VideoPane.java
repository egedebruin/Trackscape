package gui.panes;

import gui.controllers.TimerManager;
import gui.controllers.RoomController;
import gui.controllers.TimeLogController;
import gui.controllers.VideoController;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Class that constructs the videoPane for the MonitorScene.
 */
public class VideoPane {
    /**
     * Class parameters.
     */
    private MediaPane mediaPane;
    private MenuPane menuPane;
    private MediaBar mediaBar;
    private ProgressBar progressBar;
    private StatusPane statusPane;
    private TimeLoggerPane timeLoggerPane;

    /**
     * Constructor for VideoPane.
     */
    public VideoPane() {
        RoomController roomController = new RoomController();
        TimeLogController timeLogController = new TimeLogController();
        VideoController videoController = new VideoController();
        TimerManager timerManager = new TimerManager(roomController, timeLogController,
            videoController);

        mediaPane = new MediaPane();
        progressBar = new ProgressBar(roomController);
        statusPane = new StatusPane(roomController);
        menuPane = new MenuPane(roomController, timerManager, timeLogController,
            videoController, mediaPane);
        mediaBar = new MediaBar(videoController, timerManager, menuPane, mediaPane, statusPane,
            progressBar);
        timeLoggerPane = new TimeLoggerPane(timeLogController, roomController);
    }

    /**
     * createVideoPane.
     * Create the center pane for the root, containing menu,
     * mediaPlayer, and mediaBar for the monitorScene
     * @param primaryStage starting stage
     * @return videoPane
     */
    public BorderPane createVideoPane(final Stage primaryStage) {
        BorderPane videoPane = new BorderPane();

        // Create the pane that will be in the center of the videopane
        BorderPane mediaPlayer = new BorderPane();
        mediaPlayer.setCenter(mediaPane.createImageViewerPane());
        mediaPlayer.setBottom(mediaBar.createMediaBar());

        // put menubar at the top of the videopane
        videoPane.setTop(menuPane.createMenuPane(videoPane, primaryStage));
        // put the imageviews & mediabar in the center of the videopane
        videoPane.setCenter(mediaPlayer);
        // put the timelogger in the left of the videopane
        videoPane.setLeft(timeLoggerPane.createTimeLoggerPane());
        // put the escape room status in the right of the videopane
        videoPane.setRight(statusPane.createStatusPane());
        // put the mediabar and progressbar in the bottom of the videopane
        videoPane.setBottom(progressBar.createProgressBarPane());

        return videoPane;
    }
}
