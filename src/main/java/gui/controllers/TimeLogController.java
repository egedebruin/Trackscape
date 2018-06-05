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
import javafx.util.Pair;
import org.opencv.core.Mat;

import java.awt.Graphics2D;
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
    private long chestTimestamp = -1;

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
        addInformation(text, System.nanoTime());
    }

    /**
     * Add information with different time to correct VBox.
     * @param text The text to add.
     * @param time The timestamp.
     */
    public void addInformation(final String text, final long time) {
        long elapsedTime = time - cameraHandler.getBeginTime();
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
        addInformation("Found chest", chestTimestamp);
        clearButtons();
    }

    /**
     * Turns button invisible without notification of found chest.
     */
    public void unConfirm() {
        clearButtons();
    }

    /**
     * Set the buttons and picture to not visible.
     */
    public void clearButtons() {
        question.setVisible(false);
        approveButton.setVisible(false);
        notApproveButton.setVisible(false);
        imageView.setVisible(false);
        chestTimestamp = -1;
    }

    /**
     * Close this controller when the stream is closed.
     */
    public void closeController() {
        timerLabel.setText("00:00:00");
        clearButtons();
        imageView.setImage(null);
        informationHandler.clearMatQueue();
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
        changeTime(now);
        checkMatInformation();
        checkInformation();
    }

    /**
     * Check the information from the Mat queue for pictures of chests.
     */
    public void checkMatInformation() {
        if (chestTimestamp == -1) {
            Pair<Mat, Long> mat = informationHandler.getMatrix();
            if (mat != null) {
                question.setVisible(true);
                approveButton.setVisible(true);
                notApproveButton.setVisible(true);
                imageView.setVisible(true);
                BufferedImage bufferedFrame = Util.matToBufferedImage(mat.getKey());

                final int newWidth = 300;
                final int newHeight = 200;
                BufferedImage resizedSubFrame = resizeBImage(bufferedFrame, newWidth, newHeight);
                Image image = SwingFXUtils.toFXImage(resizedSubFrame, null);

                imageView.setImage(image);
                chestTimestamp = mat.getValue();
            }
        }
    }

    /**
     * Resize the BufferedImage to fit in the screen.
     * @param bi the BufferedImage
     * @param newWidth the new width
     * @param newHeight the new height
     * @return scaledImage
     */
    public BufferedImage resizeBImage(final BufferedImage bi,
                                      final int newWidth, final int newHeight) {
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

    /**
     * Set the ImageView to a different ImageView.
     * @param iv the imageView to be set
     */
    public void setImageView(final ImageView iv) {
        this.imageView = iv;
    }
}
