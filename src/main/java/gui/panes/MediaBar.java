package gui.panes;

import gui.Controller;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
    private Controller controller;
    private MenuPane menuPane;
    private MediaPane mediaPane;
    private final int buttonWidth = 70;

    /**
     * Constructor for MediaBar.
     * @param control the controller
     * @param menu the menu
     * @param media the mediaplayer
     */
    public MediaBar(final Controller control, final MenuPane menu, final MediaPane media) {
        this.controller = control;
        this.menuPane = menu;
        this.mediaPane = media;
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

        // Create the play button
        final Button playButton = new Button();
        playButton.setGraphic(createPlayImage());
        playButton.setOnAction(event -> {
            if (controller.getCameras() == 0) {
                mediaPane.showCameraIcon();
            } else if (!controller.isVideoPlaying()) {
                controller.setVideoPlaying(true);
                initializeImageViewers();
                controller.grabTimeFrame(imageViews);
            }
        });

        // Create the stop button
        final Button closeStream = new Button();
        closeStream.setGraphic(createStopImage());
        closeStream.setOnAction(event -> menuPane.endStream());

        mediaBar.getChildren().addAll(playButton, closeStream);

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
     * Create the imageView for the play logo.
     * @return playLogo
     */
    private ImageView createPlayImage() {
        File streamEnd = new File(System.getProperty("user.dir")
            + "\\src\\main\\java\\gui\\images\\play.png");
        Image play = new Image(streamEnd.toURI().toString());
        ImageView playLogo = new ImageView();
        playLogo.setFitWidth(buttonWidth);
        playLogo.setPreserveRatio(true);
        playLogo.setImage(play);
        return playLogo;
    }

    /**
     * Create the imageView for the play logo.
     * @return playLogo
     */
    private ImageView createStopImage() {
        File streamEnd = new File(System.getProperty("user.dir")
            + "\\src\\main\\java\\gui\\images\\stop.png");
        Image stop = new Image(streamEnd.toURI().toString());
        ImageView stopLogo = new ImageView();
        stopLogo.setFitWidth(buttonWidth);
        stopLogo.setPreserveRatio(true);
        stopLogo.setImage(stop);
        return stopLogo;
    }
}
