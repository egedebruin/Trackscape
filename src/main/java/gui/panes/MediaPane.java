package gui.panes;

import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.io.File;

/**
 * Class that constructs the MediaPane for the VideoPane.
 */
public class MediaPane {

    private ScrollPane mediaPane = new ScrollPane();

    /**
     * Create the pane that holds all imageViewers.
     * @return mediaPlayerPane
     */
    public ScrollPane createImageViewerPane() {

        mediaPane.setFitToHeight(true);
        mediaPane.setFitToWidth(true);
        mediaPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        mediaPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        showCameraIcon();
        mediaPane.getStyleClass().clear();
        mediaPane.getStyleClass().add("scroll");
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

        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(startImage);

        mediaPane.setContent(stackPane);

    }

    /**
     * Get the mediaPlayerPane.
     * @return mediaPlayerPane the pane on which the mediaPlayer is shown
     */
    public ScrollPane getMediaPlayerPane() {
        return mediaPane;
    }
}
