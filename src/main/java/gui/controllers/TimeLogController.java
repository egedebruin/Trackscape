package gui.controllers;

import handlers.CameraHandler;
import handlers.InformationHandler;
import java.util.concurrent.TimeUnit;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class TimeLogController {

    private Label timerLabel;
    private TextArea informationArea;
    private Button approveButton;
    private Button notApproveButton;
    private long beginTime = -1;
    private InformationHandler informationHandler = new InformationHandler();
    private CameraHandler cameraHandler;

    public TimeLogController (CameraHandler handler) {
        cameraHandler = handler;
        cameraHandler.setInformationHandler(informationHandler);
    }

    /**
     * Changes the time of the timer.
     *
     * @param elapsedTime the elapsed time
     */
    public void changeTime(final long elapsedTime) {
        if (beginTime != -1) {
            long time = elapsedTime - beginTime;
            timerLabel.setText(getTimeString(time));
        }
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
     * Method that removes all text from the information area.
     */
    public void clearInformationArea() {
        informationArea.clear();
    }
    /**
     * Check if there is information to be shown.
     */
    public void checkInformation() {
        if (beginTime != -1) {
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

    public void closeController() {
        beginTime = -1;
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

    public void processFrame() {
        if (cameraHandler.isActive() && beginTime == -1) {
            beginTime = System.nanoTime();
        }
        if (cameraHandler.isChestDetected()) {
            approveButton.setVisible(true);
            notApproveButton.setVisible(true);
        }
    }

    public void setCameraHandler(CameraHandler cameraHandler) {
        cameraHandler.setInformationHandler(informationHandler);
        this.cameraHandler = cameraHandler;
    }
}
