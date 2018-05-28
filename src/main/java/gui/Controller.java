package gui;

import camera.Camera;
import handlers.CameraHandler;
import handlers.InformationHandler;
import handlers.JsonHandler;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
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

/**
 * Controller class for controlling GUI elements.
 */
public class Controller {

    /**
     * Class parameters.
     */
    private CameraHandler cameraHandler;
    private JsonHandler jsonHandler;
    private InformationHandler informationHandler;
    private long beginTime = -1;
    private AnimationTimer animationTimer;
    private Label timerLabel;
    private TextArea informationArea;
    private Button approveButton;
    private boolean configurated = false;
    private boolean videoPlaying = false;
    private Button notApproveButton;

    /**
     * Constructor method.
     */
    Controller() {
        informationHandler = new InformationHandler();
        cameraHandler = new CameraHandler(informationHandler);
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
            closeStream();
            return frame;
        } else {
            if (cameraHandler.isActive() && beginTime == -1) {
                beginTime = System.nanoTime();
            }
            BufferedImage bufferedFrame = matToBufferedImage(matrixFrame);
            frame = SwingFXUtils.toFXImage(bufferedFrame, null);
        }
        if (cameraHandler.isChestDetected()) {
            approveButton.setVisible(true);
            notApproveButton.setVisible(true);
        }
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
     * Method to initialize a connection with a video or stream.
     * @param url to file or stream
     * @param numChests amount of chests
     */
    public void createCamera(final String url, final int numChests) {
        cameraHandler.addCamera(url, numChests);
    }

    /**
     * grabTimeFrame.
     * Call updateImageViews method every period of time to retrieve a new frame
     * @param imageViews list of panels that show the frames
     */
    public void grabTimeFrame(final ArrayList<ImageView> imageViews) {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(final long now) {
                updateImageViews(imageViews);
                if (beginTime != -1) {
                    changeTime(now - beginTime);
                    checkInformation();
                }
            }
        };
        informationArea.setText("");
        animationTimer.start();
    }

    /**
     * updateImageViews.
     * Retrieve current frames and show in ImageViews
     * @param imageViews list of panels that show the frames
     */
    private void updateImageViews(final ArrayList<ImageView> imageViews) {
        for (int i = 0; i < cameraHandler.listSize(); i++) {
            Image currentFrame = retrieveFrame(cameraHandler.getCamera(i));
            imageViews.get(i).setImage(currentFrame);
        }
    }

    /**
     * Method that closes a stream.
     */
    public void closeStream() {
            cameraHandler.clearList();
            cameraHandler.setActive(false);
            animationTimer.stop();
            beginTime = -1;
            timerLabel.setText("00:00:00");
            configurated = false;
            videoPlaying = false;
            approveButton.setVisible(false);
            notApproveButton.setVisible(false);
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
     * Load the configuration file.
     * @param handler the current jsonHandler
     */
    public void configure(final JsonHandler handler) {
        jsonHandler = handler;

        int cameras = jsonHandler.getCameraLinks(0).size();

        for (int k = 0; k < cameras; k++) {
            createCamera(jsonHandler.getCameraLinks(0).get(k), jsonHandler.getAmountChests(0));
        }
        configurated = true;
    }

    /**
     * Get the number of active cameras.
     * @return number of cameras
     */
    public int getCameras() {
        return cameraHandler.listSize();
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
     */
    public void confirmedChest() {
        addInformation("Found chest.");
        approveButton.setVisible(false);
        notApproveButton.setVisible(false);
    }

    /**
     * Turns button invisible without notification of found chest.
     */
    public void unConfirm() {
        approveButton.setVisible(false);
        notApproveButton.setVisible(false);
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
    public void setApproveButton(final Button button) {
        this.approveButton = button;
    }

    /**
     * Get the status of the configuration.
     * @return configurated
     */
    public boolean getConfigurated() {
        return configurated;
    }

    /**
     * Get the status of the videoPlaying.
     * @return videoPlaying
     */
    public boolean isVideoPlaying() {
        return videoPlaying;
    }

    /**
     * Set the status of the videoPlaying.
     * @param isVideoPlaying boolean value for whether videos are playing
     */
    public void setVideoPlaying(final boolean isVideoPlaying) {
        this.videoPlaying = isVideoPlaying;
    }

    /**
     * Setter for notApproveButton.
     *
     * @param button the button that gets assigned to this.notApproveButton
     */
    public void setNotApproveButton(final Button button) {
        this.notApproveButton = button;
    }
}
