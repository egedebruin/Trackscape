package gui.panes;

import gui.Controller;
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
    private Controller controller;
    private MediaPane mediaPane;
    private MenuPane menuPane;
    private MediaBar mediaBar;
    private ProgressBar progressBar;
    private TimeLoggerPane timeLoggerPane;

    /**
     * Constructor for VideoPane.
     * @param control the controller
     */
    public VideoPane(final Controller control) {
        this.controller = control;
        mediaPane = new MediaPane();
        menuPane = new MenuPane(controller, mediaPane);
        mediaBar = new MediaBar(controller, menuPane, mediaPane);
        progressBar = new ProgressBar(controller);
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

        // put menubar at the top of the pane
        videoPane.setTop(menuPane.createMenuPane(videoPane, primaryStage));
        // put the imageviews in the center of the pane
        videoPane.setCenter(mediaPane.createImageViewerPane());
        // put the timelogger in the left of the pane
        videoPane.setLeft(timeLoggerPane.createTimeLoggerPane());
        // put the escape room status in the right of the pane
        videoPane.setRight(new FlowPane()); // escape room status will be displayed here
        // put the mediabar and progressbar in the bottom of the pane
        videoPane.setBottom(mediaBar.createMediaBar());

        return videoPane;
    }
}