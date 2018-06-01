package gui.controllers;

import gui.Util;
import handlers.CameraHandler;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.Mat;

/**
 * Class for the VideoController, to control the video shown.
 */
public class VideoController {

    private CameraHandler cameraHandler;
    private boolean closed;

    /**
     * Constructor for the VideoController.
     * @param handler The CameraHandler.
     */
    public VideoController(final CameraHandler handler) {
        cameraHandler = handler;
    }

    /**
     * Request frames from all the cameras.
     * @return list of frames in Mat format
     */
    private List<Image> requestFrames() {
        List<Mat> frames = cameraHandler.processFrames();
        List<Image> processedFrames = new ArrayList<>();

        File streamEnd = new File(System.getProperty("user.dir")
            + "\\src\\main\\java\\gui\\images\\black.png");
        Image blackFrame = new Image(streamEnd.toURI().toString());
        if (!cameraHandler.isChanged()) {
            for (int k = 0; k < cameraHandler.listSize(); k++) {
                processedFrames.add(blackFrame);
            }
            closeStream();
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
     * @param imageViews list of panels that show the frames
     */
    public void updateImageViews(final List<ImageView> imageViews) {
        List<Image> currentFrames = requestFrames();
        for (int i = 0; i < cameraHandler.listSize(); i++) {
            imageViews.get(i).setImage(currentFrames.get(i));
        }
    }

    /**
     * Close the stream.
     */
    public void closeStream() {
        closed = true;
    }

    /**
     * Check if the stream is closed.
     * @return True if stream is closed, false otherwise.
     */
    public boolean isClosed() {
        return closed;
    }

    /**
     * Set the cameraHandler.
     * @param handler The new CameraHandler.
     */
    public void setCameraHandler(final CameraHandler handler) {
        this.cameraHandler = handler;
    }
}
