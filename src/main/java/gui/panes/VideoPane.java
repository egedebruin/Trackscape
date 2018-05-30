package gui.panes;

import gui.Controller;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Class that constructs the videoPane for the MonitorScene.
 */
public class VideoPane {
    /**
     * Class parameters.
     */
    private FlowPane mediaPlayerPane = new FlowPane();
    private Controller controller;
    private MenuMediaPane menuMediaPane;
    private MediaBar mediaBar;
    private ProgressBar progressBar;
    private TimeLoggerPane timeLoggerPane;

    /**
     * Constructor for VideoPane.
     * @param control the controller
     */
    public VideoPane(final Controller control) {
        this.controller = control;
        menuMediaPane = new MenuMediaPane(controller, mediaPlayerPane);
        mediaBar = new MediaBar(controller, menuMediaPane);
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

        ArrayList<Pane> mmp =
            menuMediaPane.createMenuAndMediaPane(videoPane, primaryStage);

        // put menubar at the top of the pane
        videoPane.setTop(mmp.get(0));
        // put the imageviews in the center of the pane
        videoPane.setCenter(mmp.get(1));
        // put the timelogger in the left of the pane
        videoPane.setLeft(timeLoggerPane.createTimeLoggerPane());
        // put the escape room status in the right of the pane
        videoPane.setRight(new FlowPane()); // escape room status will be displayed here
        // put the mediabar and progressbar in the bottom of the pane
        videoPane.setBottom(mediaBar.createMediaBar());

        return videoPane;
    }
}