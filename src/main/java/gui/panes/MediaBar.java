package gui.panes;

import gui.controllers.Controller;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
    private Controller controller;
    private MenuMediaPane menuMediaPane;

    /**
     * Constructor for MediaBar.
     * @param control the controller
     * @param pane the menuMediaPane
     */
    public MediaBar(final Controller control, final MenuMediaPane pane) {
        this.controller = control;
        this.menuMediaPane = pane;
    }

    /**
     * createMediaBar.
     * Create a mediaBar for the mediaPlayer
     * @return HBox
     */
    public HBox createMediaBar() {
        final int top = 5;
        final int right = 10;
        final int bottom = 5;
        final int left = 10;
        final int spacing = 10;

        // Create mediabar for video options
        HBox mediaBar = new HBox();
        mediaBar.setAlignment(Pos.CENTER);
        mediaBar.setPadding(new Insets(top, right, bottom, left));
        mediaBar.setSpacing(spacing);

        // Create the play/pauze button
        final Button playButton = new Button("Start Cameras");
        playButton.setOnAction(event -> {
            if (controller.getCameras() == 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Yo momma is fat");
                alert.setContentText("There are no cameras to be shown!");
                alert.showAndWait();
            } else if (!controller.isVideoPlaying()) {
                controller.setVideoPlaying(true);
                initializeImageViewers();
                controller.grabTimeFrame(imageViews);
            }
        });

        final Button closeStream = new Button("Close Stream");
        closeStream.setOnAction(event -> menuMediaPane.getMenuPane().endStream());

        mediaBar.getChildren().addAll(playButton, closeStream);

        return mediaBar;
    }

    /**
     * Initializes the imageViews with a black image.
     */
    private void initializeImageViewers() {
        menuMediaPane.getMediaPane().getMediaPlayerPane().getChildren().clear();

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
        menuMediaPane.getMediaPane().getMediaPlayerPane().getChildren().addAll(imageViews);
    }
}
