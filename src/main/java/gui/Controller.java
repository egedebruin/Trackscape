package gui;

import camera.Camera;
import handlers.CameraHandler;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.opencv.core.Mat;

/**
 * Controller class for controlling GUI elements.
 */
class Controller {

    private CameraHandler cameraHandler;
    private boolean cameraActive;

    /**
     * Constructor method.
     */
    Controller() {
        cameraHandler = new CameraHandler();
    }

    /**
     * retrieveFrame.
     * Retrieve last frame from video reader in handlers.CameraHandler
     * @return Image
     */
    private Image retrieveFrame(Camera cam) {
        Image frame;
        Mat matrixFrame = cameraHandler.getNewFrame(cam);
        if (!cam.isChanged()) {
            File streamEnd = new File(System.getProperty("user.dir")
                + "\\src\\main\\java\\gui\\images\\black.png");
            frame = new Image(streamEnd.toURI().toString());
            cameraHandler.clearList();
            cameraActive = false;
        } else {
            BufferedImage bufferedFrame = matToBufferedImage(matrixFrame);
            frame = SwingFXUtils.toFXImage(bufferedFrame, null);
        }
        return frame;
    }

    /**
     * Converts a Mat to a BufferedImage.
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
     * Method to show a popup in which you can specify a stream url to initialize a connection.
     * @param streamStage The popup window
     * @param field the specified url.
     */
    void createStream(final Stage streamStage, TextField field) {
        String streamUrl = field.getText();
        streamStage.close();
        cameraHandler.addCamera(streamUrl);
    }

    /**
     * Method to initialize a connection with our active camera(stream).
     * @param streamUrl THE url
     */
    void createTheStream(String streamUrl) {
        cameraHandler.addCamera(streamUrl);
    }

    /**
     * Method to initialize a connection with a video.
     * @param file the video file
     */
    void createVideo(File file) {
        String fileUrl = file.toString();
        cameraHandler.addCamera(fileUrl);
    }

    /**
     * grabTimeFrame.
     * Call updateImageView method every period of time to retrieve a new frame
     */
    void grabTimeFrame(ImageView imageView) {
        if (!cameraActive) {
            ScheduledExecutorService timer;
            final int period = 1;
            Runnable frameGrabber = () -> updateImageView(imageView);
            timer = Executors.newSingleThreadScheduledExecutor();
            timer.scheduleAtFixedRate(
                frameGrabber, 0, period, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * updateImageView.
     * Retrieve current frame and show in ImageView
     */
    private void updateImageView(ImageView imageView) {
        final int width = 600;
        for (int i = 0; i<cameraHandler.listSize();i++) {
            cameraActive = true;
            Image currentFrame = retrieveFrame(cameraHandler.getCamera(i));
            imageView.setImage(currentFrame);
            imageView.setFitWidth(width);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageView.setCache(true);
        }
    }

    /**
     * Method that closes a stream.
     * @param imageView View where the stream is displayed in
     */
    void closeStream(ImageView imageView) {
        final int width = 500;
        if (cameraActive) {
            cameraHandler.clearList();
            cameraActive = false;
            File image = new File(System.getProperty("user.dir")
                + "\\src\\main\\java\\gui\\images\\nostream.png");
            Image noStreamAvailable = new Image(image.toURI().toString());
            imageView.setImage(noStreamAvailable);
            imageView.setFitWidth(width);
            imageView.setPreserveRatio(true);
        }
    }
}
