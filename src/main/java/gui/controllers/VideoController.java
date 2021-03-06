package gui.controllers;

import gui.Util;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.opencv.core.Mat;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for the VideoController, to control the video shown.
 */
public class VideoController extends Controller {

    private boolean closed;
    private List<ImageView> imageViews = new ArrayList<>();
    private Button playButton;

    /**
     * Constructor for the VideoController.
     */
    public VideoController() {
        closed = true;
        playButton = new Button();
    }

    /**
     * Request frames from all the cameras.
     * @return list of frames in Mat format
     */
    private List<Image> requestFrames() {
        List<Mat> frames = getCameraHandler().processFrames();
        List<Image> processedFrames = new ArrayList<>();

        if (!getCameraHandler().isChanged()) {
            closed = true;
        } else {
            for (Mat frame : frames) {
                BufferedImage bufferedFrame = Util.matToBufferedImage(frame);
                processedFrames.add(SwingFXUtils.toFXImage(bufferedFrame, null));
            }
            closed = false;
        }
        return processedFrames;
    }

    @Override
    public void update(final long now) {
        List<Image> currentFrames = requestFrames();
        for (int i = 0; i < currentFrames.size(); i++) {
            imageViews.get(i).setImage(currentFrames.get(i));
        }
    }

    @Override
    public void closeController() {
        File streamEnd = new File(System.getProperty("user.dir")
            + "\\src\\main\\java\\gui\\images\\black.png");
        Image blackFrame = new Image(streamEnd.toURI().toString());
        for (ImageView imageView : imageViews) {
            imageView.setImage(blackFrame);
        }
        getCameraHandler().closeHandler();
        closed = true;
        playButton.setVisible(true);
    }

    /**
     * Method to show a popup in which
     * you can specify a stream url to initialize a connection.
     *
     * @param streamStage the popup window
     * @param field the specified url.
     */
    public void createStream(final Stage streamStage, final TextField field) {
        String streamUrl = field.getText();
        streamStage.close();
        getCameraHandler().addCamera(streamUrl);
    }

    /**
     * Method to initialize a connection with a video.
     *
     * @param file the video file
     */
    public void createVideo(final File file) {
        String fileUrl = file.toString();
        getCameraHandler().addCamera(fileUrl);
    }

    /**
     * Get the current imageViews.
     * @return imageViews
     */
    public List<ImageView> getImageViews() {
        return imageViews;
    }

    /**
     * Set the imageViews.
     * @param newViews the new imageViews
     */
    public void setImageViews(final List<ImageView> newViews) {
        this.imageViews = newViews;
    }

    /**
     * Set if the videoController is closed.
     * @param newClosed new closed variable
     */
    public void setClosed(final boolean newClosed) {
        this.closed = newClosed;
    }

    /**
     * Set the playButton so the mediaBar playButton can be controlled from here.
     * @param play the button that starts the game observation
     */
    public void setPlayButton(final Button play) {
        this.playButton = play;
    }


    /**
     * Check if the stream is closed.
     * @return True if stream is closed, false otherwise.
     */
    public boolean isClosed() {
        return closed;
    }

}

