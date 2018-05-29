package gui.panes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

import java.io.File;

/**
 * Class that constructs the MediaPane for the VideoPane.
 */
public class MediaPane {
    /**
     * Class parameters.
     */
    private FlowPane mediaPlayerPane;

    /**
     * Constructor for MenuPane.
     * @param mediaPane the pane on which the mediaPlayer is shown
     */
    public MediaPane(final FlowPane mediaPane) {
        this.mediaPlayerPane = mediaPane;
    }

    /**
     * Create the pane that holds all imageViewers.
     * @return mediaPlayerPane
     */
    public Pane createImageViewerPane() {
        final int gap = 3;
        final int inset = 5;
        mediaPlayerPane.setPadding(new Insets(0, inset, inset, inset));
        mediaPlayerPane.setVgap(gap);
        mediaPlayerPane.setHgap(gap);
        mediaPlayerPane.setAlignment(Pos.CENTER);

        showCameraIcon();

        return mediaPlayerPane;
    }

    /**
     * Display icon of camera when no cameras are active yet.
     */
    public void showCameraIcon() {
        final int width = 100;
        File streamEnd = new File(System.getProperty("user.dir")
                + "\\src\\main\\java\\gui\\images\\cameraIcon.png");
        Image cameraIcon = new Image(streamEnd.toURI().toString());
        ImageView startImage = new ImageView();
        startImage.setFitWidth(width);
        startImage.setPreserveRatio(true);
        startImage.setImage(cameraIcon);
        mediaPlayerPane.getChildren().clear();
        mediaPlayerPane.getChildren().add(startImage);
    }

    /**
     * Get the mediaPlayerPane.
     * @return mediaPlayerPane the pane on which the mediaPlayer is shown
     */
    public FlowPane getMediaPlayerPane() {
        return mediaPlayerPane;
    }
}
