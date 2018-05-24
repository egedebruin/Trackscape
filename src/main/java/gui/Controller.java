package gui;

import camera.Camera;
import handlers.CameraHandler;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.opencv.core.Mat;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Controller class for controlling GUI elements.
 */
public class Controller {

    /**
     * Class parameters.
     */
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
     * @param cam the camera that is used
     * @return Image
     */
    private Image retrieveFrame(final Camera cam) {
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
     * Method to show a popup in which
     * you can specify a stream url to initialize a connection.
     * @param streamStage The popup window
     * @param field the specified url.
     */
    public void createStream(final Stage streamStage, final TextField field) {
        String streamUrl = field.getText();
        streamStage.close();
        cameraHandler.addCamera(streamUrl);
    }

    /**
     * Method to initialize a connection with our active camera(stream).
     * @param streamUrl THE url
     */
    public void createTheStream(final String streamUrl) {
        cameraHandler.addCamera(streamUrl);
    }

    /**
     * Method to initialize a connection with a video.
     * @param file the video file
     */
    public void createVideo(final File file) {
        String fileUrl = file.toString();
        cameraHandler.addCamera(fileUrl);
    }

    /**
     * grabTimeFrame.
     * Call updateImageViews method every period of time to retrieve a new frame
     * @param imageViews list of panels that show the frames
     */
    public void grabTimeFrame(final ArrayList<ImageView> imageViews) {
        if (!cameraActive) {
            ScheduledExecutorService timer;
            final int period = 1;
            Runnable frameGrabber = () -> updateImageViews(imageViews);
            timer = Executors.newSingleThreadScheduledExecutor();
            timer.scheduleAtFixedRate(
                frameGrabber, 0, period, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * updateImageViews.
     * Retrieve current frame and show in ImageView
     * @param imageViews list of panels that show the frames
     */
    private void updateImageViews(final ArrayList<ImageView> imageViews) {
        cameraActive = true;
        for (int i = 0; i < cameraHandler.listSize(); i++) {
            Image currentFrame = retrieveFrame(cameraHandler.getCamera(i));
            imageViews.get(i).setImage(currentFrame);
        }
    }

    /**
     * Method that closes a stream.
     * @param imageViews All the views where streams are displayed in
     */
    public void closeStream(final ArrayList<ImageView> imageViews) {
        if (cameraActive) {
            cameraActive = false;
            cameraHandler.clearList();

            File image = new File(System.getProperty("user.dir")
                + "\\src\\main\\java\\gui\\images\\nostream.png");
            Image noStreamAvailable = new Image(image.toURI().toString());

            final int viewPanels = imageViews.size();
            for (int i = 0; i < viewPanels; i++) {
                imageViews.get(i).setImage(noStreamAvailable);
            }
        }
    }

    /**
     * proceedToMonitorScene.
     * Move on to the next stage
     * @param ms the monitorScene
     * @param primaryStage starting stage
     * @param stylesheet current stylesheet
     */
    final void proceedToMonitorScene(final MonitorScene ms, final Stage primaryStage,
                                     final String stylesheet) {
        primaryStage.setScene(
            ms.createMonitorScene(
                primaryStage, stylesheet));
    }

}
