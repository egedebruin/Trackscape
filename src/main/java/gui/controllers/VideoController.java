package gui.controllers;

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

public class VideoController {

    private CameraHandler cameraHandler;
    private boolean closed;

    public VideoController(CameraHandler handler) {
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
                BufferedImage bufferedFrame = matToBufferedImage(frames.get(j));
                processedFrames.add(SwingFXUtils.toFXImage(bufferedFrame, null));
            }
            closed = false;
        }
        return processedFrames;
    }

    /**
     * Converts a Mat to a BufferedImage.
     *
     * @param videoMatImage The frame in Mat.
     * @return The BufferedImage.
     */
    private BufferedImage matToBufferedImage(final Mat videoMatImage) {
        int type;
        if (videoMatImage.channels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY;
        } else {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }

        int bufferSize = videoMatImage.channels()
            * videoMatImage.cols() * videoMatImage.rows();
        byte[] buffer = new byte[bufferSize];
        videoMatImage.get(0, 0, buffer);
        BufferedImage image = new BufferedImage(
            videoMatImage.cols(), videoMatImage.rows(), type);
        final byte[] targetPixels =
            ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(buffer, 0, targetPixels, 0, buffer.length);
        return image;
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

    public void closeStream() {
        closed = true;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setCameraHandler(CameraHandler cameraHandler) {
        this.cameraHandler = cameraHandler;
    }
}
