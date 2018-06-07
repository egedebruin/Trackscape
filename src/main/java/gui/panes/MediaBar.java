package gui.panes;

import gui.Util;
import gui.controllers.MainController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.File;
import java.util.ArrayList;

/**
 * Class that creates the MediaBar for the VideoPane.
 */
public class MediaBar {
    /**
     * Class parameters.
     */
    private ArrayList<ImageView> imageViews = new ArrayList<>();
    private MainController controller;
    private MenuPane menuPane;
    private MediaPane mediaPane;
    private StatusPane statusPane;
    private ProgressBar progressBar;

    /**
     * Constructor for MediaBar.
     * @param control the controller
     * @param menu the menu
     * @param media the mediaplayer
     * @param status the status of the game
     * @param progress the progress bar
     */
    public MediaBar(final MainController control, final MenuPane menu,
                    final MediaPane media, final StatusPane status,
                    final ProgressBar progress) {
        this.controller = control;
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

        // Create mediabar for video options
        HBox mediaBar = new HBox();
        mediaBar.setAlignment(Pos.CENTER);
        mediaBar.setPadding(new Insets(top, right, bottom, left));
        mediaBar.setSpacing(spacing);

        final int buttonWidth = 70;
        // Create the play button
        final Button playButton = new Button();
        playButton.getStyleClass().add("media-buttons");
        playButton.setGraphic(Util.createButtonLogo("play", buttonWidth));
        playButton.setCursor(Cursor.HAND);
        playButton.setOnAction(event -> {
            if (controller.getCameras() == 0) {
                controller.closeStream();
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("TrackScape");
                alert.setContentText("There are no cameras to be shown!");
                alert.showAndWait();
            } else if (!controller.isVideoPlaying()) {
                controller.setVideoPlaying(true);
                initializeImageViewers();
                if (controller.getConfigured()) {
                    initializeProgressBar();
                    initializeStatus();
                }
                controller.grabTimeFrame(imageViews);
            }
        });
        playButton.setOnMouseEntered(event
            -> playButton.setGraphic(Util.createButtonLogo(
                "playActive", buttonWidth)));
        playButton.setOnMouseExited(event
            -> playButton.setGraphic(Util.createButtonLogo("play", buttonWidth)));

        // Create the stop button
        final Button stopButton = new Button();
        stopButton.getStyleClass().add("media-buttons");
        stopButton.setGraphic(Util.createButtonLogo("stop", buttonWidth));
        stopButton.setCursor(Cursor.HAND);
        stopButton.setOnAction(event -> {
            menuPane.endStream();
        });
        stopButton.setOnMouseEntered(event
            -> stopButton.setGraphic(Util.createButtonLogo("stopActive", buttonWidth)));
        stopButton.setOnMouseExited(event
            -> stopButton.setGraphic(Util.createButtonLogo("stop", buttonWidth)));

        mediaBar.getChildren().addAll(playButton, stopButton);

        return mediaBar;
    }

    /**
     * Initializes the imageViews with a black image.
     */
    private void initializeImageViewers() {
        mediaPane.getMediaPlayerPane().getChildren().clear();

        imageViews.clear();
        for (int k = 0; k < controller.getCameras(); k++) {
            imageViews.add(new ImageView());
        }

        File streamEnd = new File(System.getProperty("user.dir")
                + "\\src\\main\\java\\gui\\images\\black.png");
        Image black = new Image(streamEnd.toURI().toString());

        final int height = 300;
        for (int i = 0; i < imageViews.size(); i++) {
            imageViews.get(i).setImage(black);
            imageViews.get(i).setFitHeight(height);
            imageViews.get(i).setPreserveRatio(true);
            imageViews.get(i).setSmooth(true);
            imageViews.get(i).setCache(false);
        }
        mediaPane.getMediaPlayerPane().getChildren().addAll(imageViews);
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
