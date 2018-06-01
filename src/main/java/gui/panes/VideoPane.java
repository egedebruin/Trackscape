package gui.panes;

import gui.controllers.MainController;
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
    private MainController mainController;
    private MenuMediaPane menuMediaPane;
    private MediaBar mediaBar;
    private TimeLoggerPane timeLoggerPane;

    /**
     * Constructor for VideoPane.
     * @param control the mainController
     */
    public VideoPane(final MainController control) {
        this.mainController = control;
        menuMediaPane = new MenuMediaPane(mainController, mediaPlayerPane);
        mediaBar = new MediaBar(mainController, menuMediaPane);
        timeLoggerPane = new TimeLoggerPane(mainController.getTimeLogController());
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

        // get the menubar and put it at the top of the videoPane
        videoPane.setTop(mmp.get(0));
        // get the imageViews (location where videos are shown)
        // and put it in the center of the videoPane
        videoPane.setCenter(mmp.get(1));

        // create the functionality panes for the videoPane
        videoPane.setBottom(mediaBar.createMediaBar());
        videoPane.setLeft(timeLoggerPane.createTimeLoggerPane());
        videoPane.setRight(new FlowPane()); // escape room status will be displayed here

        return videoPane;
    }
}