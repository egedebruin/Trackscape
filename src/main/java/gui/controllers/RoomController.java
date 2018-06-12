package gui.controllers;

import gui.Util;
import handlers.CameraHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import room.Chest;
import room.Progress;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Controller for the gui with a room.
 */
public class RoomController {

    private Progress progress;
    private Pane progressBar;
    private Pane statusPane;
    private CameraHandler cameraHandler;
    private int progressCompleted;
    private Label gameStatus;
    private Label numOfChestsOpened;
    private Label activityStatus;
    private boolean snoozeHint = false;
    private boolean behindSchedule = false;
    private List<Chest> chestList;
    private List<Label> chestTimeStampList;

    /**
     * Constructor.
     */
    public RoomController() {
        progressCompleted = 0;
    }

    /**
     * Creates a new camerahandler depending on the config.
     * @param configFile The configfile for this room.
     */
    public void configure(final String configFile) {
        progress = new Progress(configFile);
        cameraHandler = progress.getRoom().getCameraHandler();
        chestList = progress.getRoom().getChestList();
        chestTimeStampList = new ArrayList<>();

        if (chestList != null) {
            for (int i = 0; i < chestList.size(); i++) {
                chestTimeStampList.add(new Label());
            }
        }
    }

    /**
     * Close the controller when the stream is closed.
     */
    public void closeController() {
        snoozeHint = false;
        behindSchedule = false;
        progressCompleted = 0;
        if (progressBar != null) {
            progressBar.getChildren().clear();
        }
        if (statusPane != null) {
            statusPane.getChildren().clear();
        }
    }

    /**
     * Let the host set items on done when the team has finished them.
     */
    public void setItemsOnDone() {
        progressBar.getChildren().forEach(item -> {
            item.setOnMouseClicked(event -> {
                if (item.getId() != "line") {
                    int index = progressBar.getChildren().indexOf(item);
                    if (item.getStyleClass().toString().contains("progress-reset")) {
                        newItemDone(index);
                    } else {
                        resetProgress(index);
                        itemsRemoved();
                    }
                }
            });
        });
    }

    /**
     * Logic for when a new item is clicked in the progressbar.
     * @param index the index of the new item
     */
    private void newItemDone(final int index) {
        int chestsOpened = progress.getRoom().getChestsOpened();
        fillProgress(index);
        int completedSections = progress.getSubSectionCountFromBarIndex(index);
        progress.setSubSectionCount(completedSections);
        progress.getRoom().setChestSectionsCompletedTill(completedSections);
        progress.updateProgress();
        int amountNewChests = progress.getRoom().getChestsOpened();
        for (int i = chestsOpened + 1; i < amountNewChests + 1; i++) {
            String chestsFound = i + "/"
                + progress.getRoom().getChestList().size();
            cameraHandler.getInformationHandler().addInformation("Found chest " + chestsFound);
        }
    }

    /**
     * Logic for when items are removed.
     */
    private void itemsRemoved() {
        int chestsOpened = progress.getRoom().getChestsOpened();
        int completedSections =
            progress.getSubSectionCountFromBarIndex(progressCompleted);
        progress.getRoom().unsetChestSectionsCompletedTill(completedSections);
        int newChests = progress.getRoom().getChestsOpened();
        for (int i = chestsOpened; i > newChests; i--) {
            cameraHandler.getInformationHandler().addInformation("Removed chest " + i);
        }
    }

    /**
     * Fills the progressbar up to the current stage.
     * @param stage the current progress stage of the game
     */
    public void fillProgress(final int stage) {
        for (int k = 0; k <= stage; k++) {
            progressBar.getChildren().get(k).getStyleClass().clear();
            progressBar.getChildren().get(k).getStyleClass().add("progress-made");
            k++;
        }
        progressCompleted = stage;
    }

    /**
     * Resets the progressbar to the current stage.
     * @param stage the current progress stage of the game
     */
    private void resetProgress(final int stage) {
        if (stage == progressBar.getChildren().size() - 1
            || progressBar.getChildren().get(stage + 2)
            .getStyleClass().toString().contains("progress-reset")) {
            // At the end state of the progress bar or
            // when the next item is not already done, reset this item
            clearStyleSheet(stage);
            progressCompleted = stage - 2;
        } else {
            // When the next item is already done, reset until this item
            for (int k = stage + 2; k < progressBar.getChildren().size(); k++) {
                clearStyleSheet(k);
                k++;
            }
            progressCompleted = stage;
        }
    }

    /**
     * Clear the stylesheet of current stage item.
     * @param stage the current stage item
     */
    private void clearStyleSheet(final int stage) {
        progressBar.getChildren().get(stage).getStyleClass().clear();
        progressBar.getChildren().get(stage).getStyleClass().add("progress-reset");
    }

    /**
     * Update the roomController.
     * @param now the actual time in nanoseconds
     */
    public void update(final long now) {
        if (progress != null) {
            progress.updateProgress();
            changeTime(now);
            if (statusPane.getChildren().size() > 0) {
                // Update the progressPane
                updateChests(progress.getRoom().getChestsOpened());
                updateActivity();
                // Update the warningPane
                // When people are behind on schedule
                if (behindSchedule && !snoozeHint && !allChestsOpened()) {
                    // Get the warningPane of the statusPane and set it on visible
                    statusPane.getChildren().get(2).setVisible(true);
                } else {
                    statusPane.getChildren().get(2).setVisible(false);
                }
            }
        }
    }

    /**
     * Update the activity Label.
     */
    private void updateActivity() {
        String activeString = "" + this.getCameraHandler().getActive();
        activityStatus.setText(" Current activity: " + activeString.toLowerCase());
    }

    /**
     * Updates the amount of chests present in the room.
     * @param chests the amount of chests
     */
    public void updateChests(final int chests) {
        numOfChestsOpened.setText(" Chests opened: " + chests + "/"
            + getProgress().getRoom().getChestList().size());
        if (allChestsOpened()) {
            numOfChestsOpened.setText(" All chests have been opened!");
            numOfChestsOpened.setTextFill(Color.FORESTGREEN);
        } else {
            numOfChestsOpened.setTextFill(Color.BLACK);
        }
    }

    /** Method for when a chest if confirmed by the host.
     * @param timestamp the timestamp when the chest was opened
     * @return string format of how many chests are opened
     */
    public String confirmedChestString(final long timestamp) {
        if (getProgress() != null) {
            getProgress().getRoom().setNextChestOpened(timestamp);
            fillProgress(getProgress().getFillCount());
        }
        return progress.getRoom().getChestsOpened() + "/"
            + progress.getRoom().getChestList().size();
    }

    /**
     * Changes the time of the timer.
     *
     * @param elapsedTime the elapsed time
     */
    public void changeTime(final long elapsedTime) {
        if (chestList.size() > 0 && cameraHandler.getBeginTime() != -1) {
            for (int i = 0; i < chestList.size(); i++) {
                Chest currentChest = chestList.get(i);
                long time = elapsedTime - cameraHandler.getBeginTime();

                if (currentChest.getChestState() == Chest.Status.OPENED) {
                    time = currentChest.getTimeFound() - cameraHandler.getBeginTime();
                    chestTimeStampList.get(i).setText(Util.getTimeString(time, false));
                } else if (currentChest.getChestState() == Chest.Status.TO_BE_OPENED) {
                    chestTimeStampList.get(i).setText(Util.getTimeString(time, false));
                } else if (currentChest.getChestState()
                    == Chest.Status.WAITING_FOR_SECTION_TO_START) {
                    chestTimeStampList.get(i).setText("");
                }
                updateTimeChestsPanel(time, i);

                if (time > TimeUnit.SECONDS.toNanos(progress.getRoom().getTargetDuration())) {
                    gameStatus.setText(" Time is up! Game has ended.");
                    gameStatus.setTextFill(Color.RED);
                } else {
                    setProgressBarActive(true);
                    gameStatus.setText(" Game has started");
                    gameStatus.setTextFill(Color.FORESTGREEN);
                }
            }
        }
    }

    /**
     * Update the colors of the time text and check if the team is behind schedule.
     * @param time the current time - time chest was discovered
     * @param pos the current position in the chestlist
     */
    private void updateTimeChestsPanel(final long time, final int pos) {
        if (chestList.get(pos).getChestState() == Chest.Status.TO_BE_OPENED
            && !(TimeUnit.NANOSECONDS.toSeconds(time)
            <= chestList.get(pos).getTargetDurationInSec())) {
            behindSchedule = true;
            chestTimeStampList.get(pos).setTextFill(Color.RED);
        } else if (chestList.get(pos).getChestState() == Chest.Status.TO_BE_OPENED) {
            behindSchedule = false;
            chestTimeStampList.get(pos).setTextFill(Color.GREEN);
        }
    }

    /**
     * Check if all chests are opened.
     * @return true if all chests are opened, false otherwise
     */
    public boolean allChestsOpened() {
        if (progress == null) {
            return false;
        }
        if (progress.getRoom().getChestList().size() == progress.getRoom().getChestsOpened()) {
            return true;
        }
        return false;
    }

    /**
     * Snooze the warningPane when hint is given by host.
     * @param snooze whether the warningPane should be snoozed or not.
     */
    public void snoozeHint(final boolean snooze) {
        snoozeHint = snooze;
    }

    /**
     * Get the camera handler.
     * @return The camera handler.
     */
    public CameraHandler getCameraHandler() {
        return cameraHandler;
    }

    /**
     * Get the progress object.
     * @return The progress object.
     */
    public Progress getProgress() {
        return progress; }

    /**
     * Set the progressBar.
     * @param newProgressBar the new progressBar
     */
    public void setProgressBar(final GridPane newProgressBar) {
        this.progressBar = newProgressBar;
    }

    /**
     * Set the progressBar on inactive.
     * @param active whether the progessbar should be active or not
     */
    public void setProgressBarActive(final boolean active) {
        for (Node child : progressBar.getChildren()) {
            child.setDisable(!active);
        }
    }

    /**
     * Set the statusPane.
     * @param pane the statusPane
     */
    public void setStatusPane(final Pane pane) {
        this.statusPane = pane; }

    /**
     * Set the numOfChestsOpened label.
     * @param label the label showing the amount of chests opened
     */
    public void setNumOfChestsOpened(final Label label) {
        this.numOfChestsOpened = label; }

    /**
     * Get the chestTimeStampList.
     * @return the list
     */
    public List<Label> getChestTimeStampList() {
        return chestTimeStampList;
    }

    /**
     * Set the activityStatus.
     * @param activity the label that shows activity status
     */
    public void setActivityStatus(final Label activity) {
        this.activityStatus = activity;
    }

    /**
     * Set the gameStatus.
     * @param gameStat the label that shows the game status
     */
    public void setGameStatus(final Label gameStat) {
        this.gameStatus = gameStat;
    }
}
