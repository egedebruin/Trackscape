package gui.controllers;

import gui.Util;
import handlers.CameraHandler;
import handlers.InformationHandler;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.concurrent.TimeUnit;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.Mat;

/**
 * Class for the TimeLogController, to control the time and log.
 */
public class TimeLogController {

    private Label timerLabel;
    private TextArea informationArea;
    private Button approveButton;
    private Button notApproveButton;
    //private long beginTime = -1;
    private InformationHandler informationHandler = new InformationHandler();
    private CameraHandler cameraHandler;
    private ImageView imageView;

    /**
     * Constructor for the TimeLogController, sets a new informationHandler.
     * @param handler The cameraHandler.
     */
    public TimeLogController(CameraHandler handler) {
        cameraHandler = handler;
        cameraHandler.setInformationHandler(informationHandler);
    }

    /**
     * Changes the time of the timer.
     *
     * @param elapsedTime the elapsed time
     */
    public void changeTime(final long elapsedTime) {
        if (cameraHandler.getBeginTime() != -1) {
            long time = elapsedTime - cameraHandler.getBeginTime();
            timerLabel.setText(Util.getTimeString(time));
        }
    }

    /**
     * Add information to correct VBox.
     *
     * @param text The text to add.
     */
    public void addInformation(final String text) {
        long elapsedTime = System.nanoTime() - cameraHandler.getBeginTime();
        String newText = Util.getTimeString(elapsedTime) + ": " + text;
        informationArea.appendText(newText + "\n");
    }

    /**
     * Method that removes all text from the information area.
     */
    public void clearInformationArea() {
        informationArea.clear();
    }
    /**
     * Check if there is information to be shown.
     */
    public void checkInformation() {
        if (cameraHandler.getBeginTime() != -1) {
            String log = informationHandler.getInformation();

            if (!log.equals("empty")) {
                addInformation(log);
            }
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
     * Close this controller when the stream is closed.
     */
    public void closeController() {
        timerLabel.setText("00:00:00");
        approveButton.setVisible(false);
        notApproveButton.setVisible(false);
    }

    /**
     * Setter for notApproveButton.
     *
     * @param button the button that gets assigned to this.notApproveButton
     */
    public void setNotApproveButton(final Button button) {
        this.notApproveButton = button;
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
     * Process the frames depending on the changes in cameraHandler.
     */
    public void processFrame() {
        Mat mat = informationHandler.getMatrix();
        if (mat != null) {
            approveButton.setVisible(true);
            notApproveButton.setVisible(true);
            BufferedImage bufferedFrame = Util.matToBufferedImage(mat);
            Image image = SwingFXUtils.toFXImage(bufferedFrame, null);
            imageView.setImage(image);
        }
    }

    /**
     * Set the cameraHandler with the right informationHandler.
     * @param handler The new cameraHandler.
     */
    public void setCameraHandler(CameraHandler handler) {
        handler.setInformationHandler(informationHandler);
        this.cameraHandler = handler;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
}
