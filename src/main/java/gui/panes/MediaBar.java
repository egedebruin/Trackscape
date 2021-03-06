package gui.panes;

import gui.Util;
import gui.controllers.TimerManager;
import gui.controllers.VideoController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

import java.io.File;
import java.util.ArrayList;

/**
 * Class that creates the MediaBar for the VideoPane.
 */
public class MediaBar {
    private ArrayList<ImageView> imageViews = new ArrayList<>();
    private VideoController videoController;
    private TimerManager timerManager;
    private MenuPane menuPane;
    private MediaPane mediaPane;
    private StatusPane statusPane;
    private ProgressBar progressBar;
    private Button playButton;
    private static final int IMAGEVIEW_HEIGHT = 300;


    /**
     * Constructor for MediaBar.
     * @param videoControl the videoController
     * @param timerManagerControl the timerManager
     * @param menu the menu
     * @param media the mediaplayer
     * @param status the statuspane
     * @param progress the progress bar
     */
    public MediaBar(final VideoController videoControl,
                    final TimerManager timerManagerControl, final MenuPane menu,
                    final MediaPane media, final StatusPane status, final ProgressBar progress) {
        this.videoController = videoControl;
        this.timerManager = timerManagerControl;
        this.menuPane = menu;
        this.mediaPane = media;
        this.statusPane = status;
        this.progressBar = progress;
    }

    /**
     * createMediaBar.
     * Create a mediaBar for the mediaPlayer
     * @return HBox
     */
    public HBox createMediaBar() {
        final int top = 5;
        final int right = 10;
        final int bottom = 25;
        final int left = 10;
        final int spacing = 0;

        // Create mediaBar for video options
        HBox mediaBar = new HBox();
        mediaBar.setAlignment(Pos.CENTER);
        mediaBar.setPadding(new Insets(top, right, bottom, left));
        mediaBar.setSpacing(spacing);

        mediaBar.getChildren().addAll(createPlayButton());

        return mediaBar;
    }

    /**
     * Create the playButton for the mediaBar.
     * @return playButton
     */
    private Button createPlayButton() {
        final int buttonWidth = 70;

        playButton = new Button();
        playButton.getStyleClass().add("media-buttons");
        playButton.setGraphic(Util.createImageViewLogo("buttons\\play", buttonWidth));
        playButton.setCursor(Cursor.HAND);
        playButton.setOnAction(event -> initializeConfigurations());
        playButton.setOnMouseEntered(event
            -> playButton.setGraphic(Util.createImageViewLogo(
            "buttons\\playActive", buttonWidth)));
        playButton.setOnMouseExited(event
            -> playButton.setGraphic(Util.createImageViewLogo("buttons\\play", buttonWidth)));
        videoController.setPlayButton(playButton);
        return playButton;
    }

    /**
     * Initialize things that are needed to start the video.
     */
    private void initializeConfigurations() {
        if (videoController.getCameras() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("TrackScape");
            alert.setContentText("There are no cameras to be shown!");
            alert.showAndWait();
        } else if (videoController.isClosed()) {
            videoController.setClosed(false);
            initializeImageViewers();
            initializeProgressBar();
            initializeStatus();
            timerManager.startTimer();
            videoController.setImageViews(imageViews);
            playButton.setVisible(false);
        }
    }

    /**
     * Initializes the imageViews with a black image.
     */
    private void initializeImageViewers() {
        imageViews.clear();
        for (int k = 0; k < videoController.getCameras(); k++) {
            imageViews.add(new ImageView());
        }

        File streamEnd = new File(System.getProperty("user.dir")
            + "\\src\\main\\java\\gui\\images\\black.png");
        Image black = new Image(streamEnd.toURI().toString());

        for (ImageView imageView : imageViews) {
            imageView.setImage(black);
            imageView.setFitHeight(IMAGEVIEW_HEIGHT);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageView.setCache(false);
        }

        FlowPane flowPane = new FlowPane();
        flowPane.getChildren().addAll(imageViews);
        flowPane.setAlignment(Pos.CENTER);
        mediaPane.getMediaPlayerPane().setContent(flowPane);
    }

    /**
     * Initialize the progressBar with current configuration.
     */
    private void initializeProgressBar() {
        progressBar.getProgressBar().getChildren().clear();
        progressBar.constructProgressBar();
    }

    /**
     * Initialize the statusPane with current configuration.
     */
    private void initializeStatus() {
        statusPane.getStatusPane().getChildren().clear();
        statusPane.initializeStatusPane();
    }
}
