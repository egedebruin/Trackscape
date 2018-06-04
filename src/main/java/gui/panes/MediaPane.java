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
    private FlowPane mediaPane = new FlowPane();

    /**
     * Constructor for MediaPane.
     */
    public MediaPane() {
    }

    /**
     * Create the pane that holds all imageViewers.
     * @return mediaPlayerPane
     */
    public Pane createImageViewerPane() {
        final int inset = 5;
        mediaPane.setPadding(new Insets(0, inset, inset, inset));
        showCameraIcon();
        mediaPane.setAlignment(Pos.CENTER);
        return mediaPane;
    }

    /**
     * Display icon of camera when no cameras are active yet.
     */
    public void showCameraIcon() {
        final int width = 100;
        File streamEnd = new File(System.getProperty("user.dir")
                + "\\src\\main\\java\\gui\\images\\icons\\cameraIcon.png");
        Image cameraIcon = new Image(streamEnd.toURI().toString());
        ImageView startImage = new ImageView();
        startImage.setFitWidth(width);
        startImage.setPreserveRatio(true);
        startImage.setImage(cameraIcon);
        mediaPane.getChildren().clear();
        mediaPane.getChildren().add(startImage);
    }

    /**
     * Get the mediaPlayerPane.
     * @return mediaPlayerPane the pane on which the mediaPlayer is shown
     */
    public Pane getMediaPlayerPane() {
        return mediaPane;
    }
}
