package gui.controllers;

import gui.Util;
import handlers.InformationHandler;
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Pair;
import org.opencv.core.Mat;

/**
 * Class for the TimeLogController, to control the time and log.
 */
public class TimeLogController extends Controller {

    private Label timerLabel;
    private TextArea informationArea;
    private Label question;
    private Button approveButton;
    private Button notApproveButton;
    private InformationHandler informationHandler = new InformationHandler();
    private ImageView imageView;
    private long chestTimestamp = -1;
    private Label timeStamp;

    /**
     * Constructor for the TimeLogController, sets a new informationHandler.
     */
    public TimeLogController() {
        informationHandler = getCameraHandler().getInformationHandler();
    }

    @Override
    public void closeController() {
        timerLabel.setText("00:00:00");
        clearButtons();
        imageView.setImage(null);
        informationHandler.clearMatQueue();
    }

    @Override
    public void update(final long now) {
        changeTime(now);
        checkMatInformation();
        checkInformation();
    }

    /**
     * Changes the time of the timer.
     *
     * @param elapsedTime the elapsed time
     */
    public void changeTime(final long elapsedTime) {
        if (getCameraHandler().getBeginTime() != -1) {
            long time = elapsedTime - getCameraHandler().getBeginTime();
            timerLabel.setText(Util.getTimeString(time, true));
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
        long elapsedTime = time - getCameraHandler().getBeginTime();
        String newText = Util.getTimeString(elapsedTime, true) + ": " + text;
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
        if (getCameraHandler().getBeginTime() != -1) {
            String log = informationHandler.getInformation();

            if (!log.equals("empty")) {
                addInformation(log);
            }
        }
    }

    /**
     * Turns the button invisible after it is clicked.
     * @param chestsFound the string format of the amount of chests found
     */
    public void confirmedChest(final String chestsFound) {
        addInformation("Found chest " + chestsFound, chestTimestamp);
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
        timeStamp.setVisible(false);
        chestTimestamp = -1;
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
     * Setter for the timeStamp at which a chest is detected.
     * @param label the label that needs to be set
     */
    public void setTimeStamp(final Label label) {
        this.timeStamp = label;
    }

    /**
     * Check the information from the Mat queue for pictures of chests.
     */
    public void checkMatInformation() {
        if (getCameraHandler().areAllChestsDetected()) {
            clearButtons();
        } else if (chestTimestamp == -1) {
                Pair<Mat, Long> mat = informationHandler.getMatrix();
                if (mat != null) {
                    question.setVisible(true);
                    approveButton.setVisible(true);
                    notApproveButton.setVisible(true);
                    imageView.setVisible(true);
                    timeStamp.setVisible(true);
                    timeStamp.setText("Time detected: " + (Util.getTimeString(mat.getValue()
                            - getCameraHandler().getBeginTime(), true)));

                    Image image = newChestFrame(mat);
                    imageView.setImage(image);
                    chestTimestamp = mat.getValue();
                }
        }
    }

    /**
     * Create new image for the found chest.
     * @param mat the mat of the image
     * @return the image
     */
    private Image newChestFrame(final Pair<Mat, Long> mat) {
        BufferedImage bufferedFrame = Util.matToBufferedImage(mat.getKey());

        final int newWidth = 300;
        final int newHeight = 200;
        BufferedImage resizedSubFrame =
            Util.resizeBufferedImage(bufferedFrame, newWidth, newHeight);

        return SwingFXUtils.toFXImage(resizedSubFrame, null);
    }

    /**
     * Set the ImageView to a different ImageView.
     * @param iv the imageView to be set
     */
    public void setImageView(final ImageView iv) {
        this.imageView = iv;
    }

    /**
     * get the timestamp when the chest was found in the video.
     * @return timestamp the chest was found on
     */
    public long getChestTimestamp() {
        return chestTimestamp;
    }
}
