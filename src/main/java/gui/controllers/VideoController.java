package gui.controllers;

import camera.Camera;
import gui.Util;
import handlers.CameraHandler;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.opencv.core.Mat;

/**
 * Class for the VideoController, to control the video shown.
 */
public class VideoController extends Controller {

    private boolean closed;
    private List<ImageView> imageViews;

    /**
     * Constructor for the VideoController.
     */
    public VideoController() {
        closed = true;
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
            for (int j = 0; j < frames.size(); j++) {
                BufferedImage bufferedFrame = Util.matToBufferedImage(frames.get(j));
                processedFrames.add(SwingFXUtils.toFXImage(bufferedFrame, null));
            }
            closed = false;
        }
        return processedFrames;
    }

    /**
     * updateImageViews.
     * Retrieve current frames and show in ImageViews
     */
    public void update(final long now) {
        List<Image> currentFrames = requestFrames();
        for (int i = 0; i < currentFrames.size(); i++) {
            imageViews.get(i).setImage(currentFrames.get(i));
        }
    }

    public void closeController() {
        File streamEnd = new File(System.getProperty("user.dir")
            + "\\src\\main\\java\\gui\\images\\black.png");
        Image blackFrame = new Image(streamEnd.toURI().toString());
        for (int i = 0; i < imageViews.size(); i++) {
            imageViews.get(i).setImage(blackFrame);
        }
        getCameraHandler().closeHandler();
    }

    /**
     * Check if the stream is closed.
     * @return True if stream is closed, false otherwise.
     */
    public boolean isClosed() {
        return closed;
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
        Camera camera = getCameraHandler().addCamera(streamUrl);
//        AnimationTimer streamTimer = new AnimationTimer() {
//            @Override
//            public void handle(final long now) {
//                camera.loadFrame();
//            }
//        };
//        streamTimers.add(streamTimer);
//        streamTimer.start();
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

    public void setImageViews(final List<ImageView> newViews) {
        this.imageViews = newViews;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }
}
