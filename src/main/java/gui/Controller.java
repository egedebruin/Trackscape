package gui;

import camera.Camera;
import handlers.CameraHandler;
import handlers.InformationHandler;
import javafx.animation.AnimationTimer;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.opencv.core.Mat;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
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
    private InformationHandler informationHandler;
    private boolean cameraActive;
    private long beginTime = -1;
    private AnimationTimer animationTimer;
    private Label timerLabel;
    private TextArea informationArea;
    private Button approveButton;

    /**
     * Constructor method.
     */
    Controller() {
        informationHandler = new InformationHandler();
        cameraHandler = new CameraHandler(informationHandler);
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(final long now) {
                changeTime(now - beginTime);
                checkInformation();
            }
        };
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
            animationTimer.stop();
            beginTime = -1;
        } else {
            if (cameraHandler.isActive() && beginTime == -1) {
                beginTime = System.nanoTime();
                animationTimer.start();
            }
            BufferedImage bufferedFrame = matToBufferedImage(matrixFrame);
            frame = SwingFXUtils.toFXImage(bufferedFrame, null);
        }
        approveButton.setVisible(cameraHandler.isChestDetected());
        return frame;
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
     * Method to show a popup in which
     * you can specify a stream url to initialize a connection.
     *
     * @param streamStage the popup window
     * @param field the specified url.
     */
    public void createStream(final Stage streamStage, final TextField field) {
        String streamUrl = field.getText();
        streamStage.close();
        cameraHandler.addCamera(streamUrl);
    }

    /**
     * Method to initialize a connection with our active camera(stream).
     *
     * @param streamUrl THE url
     */
    public void createTheStream(final String streamUrl) {
        cameraHandler.addCamera(streamUrl);
    }

    /**
     * Method to initialize a connection with a video.
     *
     * @param file the video file
     */
    public void createVideo(final File file) {
        String fileUrl = file.toString();
        cameraHandler.addCamera(fileUrl);
    }

    /**
     * grabTimeFrame.
     * Call updateImageView method every period of time to retrieve a new frame
     *
     * @param imageView panel that shows the frame
     */
    void grabTimeFrame(final ImageView imageView) {
        if (!cameraActive) {
            ScheduledExecutorService timer;
            final int period = 1;
            Runnable frameGrabber = () -> updateView(imageView);
            timer = Executors.newSingleThreadScheduledExecutor();
            timer.scheduleAtFixedRate(
                frameGrabber, 0, period, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * updateView.
     * @param imageView the view for the video
     */
    void updateView(final ImageView imageView) {
        updateImageView(imageView);
    }

    /**
     * updateImageView.
     * Retrieve current frame and show in ImageView
     *
     * @param imageView panel that shows the frame
     */
    private void updateImageView(final ImageView imageView) {
        final int width = 600;
        for (int i = 0; i < cameraHandler.listSize(); i++) {
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
     *
     * @param imageView View where the stream is displayed in
     */
    public void closeStream(final ImageView imageView) {
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
            animationTimer.stop();
            beginTime = -1;
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

    /**
     * Changes the time of the timer.
     *
     * @param elapsedTime the elapsed time
     */
    public void changeTime(final long elapsedTime) {
        timerLabel.setText(getTimeString(elapsedTime));
    }

    /**
     * Add information to correct VBox.
     *
     * @param text The text to add.
     */
    public void addInformation(final String text) {
        long elapsedTime = System.nanoTime() - beginTime;
        String newText = getTimeString(elapsedTime) + ": " + text;
        informationArea.appendText(newText + "\n");
    }

    /**
     * Check if there is information to be shown.
     */
    public void checkInformation() {
        String log = informationHandler.getInformation();

        if (!log.equals("empty")) {
            addInformation(log);
        }
    }

    /**
     * Turns the button invisible after it is clicked.
     * @param button the button that gets turned invisible
     */
    public void confirmedChest(Button button) {
        addInformation("Found chest.");
        button.setVisible(false);
    }

    /**
     * Convert nano seconds to right time string.
     *
     * @param time Time in nano seconds.
     * @return Correct time string.
     */
    public String getTimeString(final long time) {
        final int sixtySeconds = 60;
        final int nineSeconds = 9;

        int seconds = (int) TimeUnit.NANOSECONDS.toSeconds(time) % sixtySeconds;
        int minutes = (int) TimeUnit.NANOSECONDS.toMinutes(time) % sixtySeconds;
        int hours = (int) TimeUnit.NANOSECONDS.toHours(time);

        String sec = Integer.toString(seconds);
        String min = Integer.toString(minutes);
        String hr = Integer.toString(hours);

        if (seconds <= nineSeconds) {
            sec = "0" + seconds;
        }
        if (minutes <= nineSeconds) {
            min = "0" + minutes;
        }
        if (hours <= nineSeconds) {
            hr = "0" + hours;
        }
        return hr + ":" + min + ":" + sec;
    }

    /**
     * Set the timerLabel with a specific label.
     *
     * @param newLabel the label to be set
     */
    public void setTimerLabel(final Label newLabel) {
        this.timerLabel = newLabel;
    }

    /**
     * Set the informationBox with a specific box.
     *
     * @param infoArea The box to be set.
     */
    public void setInformationBox(final TextArea infoArea) {
        this.informationArea = infoArea;
    }

    /**
     * Setter for approveButton.
     * @param button the button that gets assigned to this.approveButton
     */
    public void setApproveButton(Button button) {
        this.approveButton = button;
    }
}
