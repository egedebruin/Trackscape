package gui.panes;

import gui.controllers.MainController;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Class that constructs the videoPane for the MonitorScene.
 */
public class VideoPane {
    /**
     * Class parameters.
     */
    private MainController controller;
    private MediaPane mediaPane;
    private MenuPane menuPane;
    private MediaBar mediaBar;
    private ProgressBar progressBar;
    private TimeLoggerPane timeLoggerPane;

    /**
     * Constructor for VideoPane.
     * @param control the mainController
     */
    public VideoPane(final MainController control) {
        this.controller = control;
        mediaPane = new MediaPane();
        progressBar = new ProgressBar(controller.getRoomController());
        menuPane = new MenuPane(controller, mediaPane);
        mediaBar = new MediaBar(controller, menuPane, mediaPane, progressBar);
        timeLoggerPane = new TimeLoggerPane(controller);
    }

    /**
     * createVideoPane.
     * Create the center pane for the root, containing menu,
     * mediaPlayer, and mediaBar for the monitorScene
     * @param primaryStage starting stage
     * @return videoPane
     */
    public Pane createVideoPane(final Stage primaryStage) {
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
        videoPane.setRight(new FlowPane()); // escape room status will be displayed here
        // put the mediabar and progressbar in the bottom of the videopane
        videoPane.setBottom(progressBar.createProgressBarPane());

        return videoPane;
    }
}
