package gui.controllers;

import gui.Util;
import handlers.CameraHandler;
import handlers.InformationHandler;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.Mat;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Class for the TimeLogController, to control the time and log.
 */
public class TimeLogController {

    private Label timerLabel;
    private TextArea informationArea;
    private Label question;
    private Button approveButton;
    private Button notApproveButton;
    private InformationHandler informationHandler = new InformationHandler();
    private CameraHandler cameraHandler;
    private ImageView imageView;

    /**
     * Constructor for the TimeLogController, sets a new informationHandler.
     * @param handler The cameraHandler.
     */
    public TimeLogController(final CameraHandler handler) {
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
        addInformation("Found chest");
        question.setVisible(false);
        approveButton.setVisible(false);
        notApproveButton.setVisible(false);
        imageView.setVisible(false);
    }

    /**
     * Turns button invisible without notification of found chest.
     */
    public void unConfirm() {
        question.setVisible(false);
        approveButton.setVisible(false);
        notApproveButton.setVisible(false);
        imageView.setVisible(false);
    }

    /**
     * Close this controller when the stream is closed.
     */
    public void closeController() {
        timerLabel.setText("00:00:00");
        question.setVisible(false);
        approveButton.setVisible(false);
        notApproveButton.setVisible(false);
        imageView.setVisible(false);
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
     * Setter for the question asked when a chest is shown.
     * @param label the label that needs to be set
     */
    public void setQuestion(final Label label) {
        this.question = label;
    }

    /**
     * Process the frames depending on the changes in cameraHandler.
     * @param now The current time.
     */
    public void processFrame(final long now) {
        if (!imageView.isVisible()) {
            Mat mat = informationHandler.getMatrix();
            if (mat != null) {
                question.setVisible(true);
                approveButton.setVisible(true);
                notApproveButton.setVisible(true);
                imageView.setVisible(true);
                BufferedImage bufferedFrame = Util.matToBufferedImage(mat);

                int newWidth = 300;
                int newHeight = 200;
                BufferedImage resizedSubFrame = resizeBImage(bufferedFrame, newWidth, newHeight);
                Image image = SwingFXUtils.toFXImage(resizedSubFrame, null);

                imageView.setImage(image);
            }
        }
        changeTime(now);
        checkInformation();
    }

    public BufferedImage resizeBImage(BufferedImage bi, int newWidth, int newHeight) {
        BufferedImage scaledImage = null;
        if (bi != null) {
            scaledImage = new BufferedImage(newWidth, newHeight, bi.getType());
            Graphics2D graphics2D = scaledImage.createGraphics();
            graphics2D.drawImage(bi, 0, 0, newWidth, newHeight, null);
            graphics2D.dispose();
        }
        return scaledImage;
    }
    /**
     * Set the cameraHandler with the right informationHandler.
     * @param handler The new cameraHandler.
     */
    public void setCameraHandler(final CameraHandler handler) {
        handler.setInformationHandler(informationHandler);
        this.cameraHandler = handler;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
}
