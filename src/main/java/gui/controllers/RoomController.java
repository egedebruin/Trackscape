package gui.controllers;

import gui.Util;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import room.Chest;
import room.Progress;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Controller for the gui with a room.
 */
public class RoomController extends Controller {

    private Progress progress;
    private Pane progressBar;
    private Pane statusPane;
    private Label gameStatus;
    private Label timeLeft;
    private Label numOfChestsOpened;
    private Label activityStatus;
    private boolean snoozeHint = false;
    private boolean behindSchedule = false;
    private List<Label> chestTimeStampList;
    private boolean configured;

    /**
     * Creates a new progress depending on the config.
     * @param configFile the configFile for this room.
     */
    public void configure(final String configFile) {
        progress = new Progress(configFile);
        configure();
    }

    /**
     * Configure everything needed to start observing the game fin the application.
     */
    private void configure() {
        for (String link : progress.getRoom().getLinkList()) {
            getCameraHandler().addCamera(link, progress.getRoom().getChestList().size());
        }
        progress.getRoom().setInformationHandler(getCameraHandler().getInformationHandler());

        List<Chest> chestList = progress.getRoom().getChestList();
        chestTimeStampList = new ArrayList<>();

        if (chestList != null) {
            for (int i = 0; i < chestList.size(); i++) {
                chestTimeStampList.add(new Label());
            }
        }
        configured = true;
    }

    /**
     * Creates a new Room from manual configuration.
     * @param players the amount of players in the game
     * @param totalDuration the total duration of the game in seconds
     * @param sectionList the list with the amount of sections per chest
     * @param durationList the list with the duration for each chest
     * @param warning the list with the warning time for each chest
     */
    public void manualConfig(final int players, final int totalDuration,
                             final List<Integer> sectionList, final List<Integer> durationList,
                             final List<Integer> warning) {
        progress = new Progress(players, totalDuration, sectionList, durationList, warning);
        configure();
    }

    /**
     * Close the controller when the stream is closed.
     */
    @Override
    public void closeController() {
        snoozeHint = false;
        behindSchedule = false;
        if (progressBar != null) {
            progressBar.getChildren().clear();
            progress.stopServer();
        }
        if (statusPane != null) {
            statusPane.getChildren().clear();
        }
        configured = false;
        progress = null;
    }

    @Override
    public void update(final long now) {
        if (progress != null && getCameraHandler().getBeginTime() != -1) {
            progress.updateProgress();
            fillProgress(progress.getFillCount());
            changeTime(now);
            changeInformation(now);
            // Update the progressPane
            updateChests(progress.getRoom().getChestsOpened());
            updateActivity();
            updateWarningPane(now);
        }
    }

    /**
     * Let the host set items on done when the team has finished them.
     */
    public void setItemsOnDone() {
        progressBar.getChildren().forEach(item -> item.setOnMouseClicked(event -> {
            if (!Objects.equals(item.getId(), "line")) {
                int index = progressBar.getChildren().indexOf(item);
                if (item.getStyleClass().toString().contains("progress-reset")) {
                    newItemDone(index);
                } else {
                    int newIndex = resetProgress(index);
                    itemsRemoved(newIndex);
                }
            }
        }));
    }

    /**
     * Logic for when a new item is clicked in the progressbar.
     * @param index the index of the new item
     */
    void newItemDone(final int index) {
        int oldChests = progress.getRoom().getChestsOpened();
        int newChests = progress.newProgress(index);
        for (int i = oldChests + 1; i < newChests + 1; i++) {
            String chestsFound = i + "/" + progress.getRoom().getChestList().size();
            getCameraHandler().getInformationHandler().addInformation("Found chest " + chestsFound);
        }
    }

    /**
     * Logic for when items are removed.
     * @param index the index of the new item
     */
    void itemsRemoved(final int index) {
        int oldChests = progress.getRoom().getChestsOpened();
        int newChests = progress.newProgress(index);
        for (int i = oldChests; i > newChests; i--) {
            getCameraHandler().getInformationHandler().addInformation("Removed chest " + i);
        }
    }

    /**
     * Resets the progressbar to the current stage.
     * @param stage the current progress stage of the game
     * @return newIndex
     */
    private int resetProgress(final int stage) {
        if (stage == progressBar.getChildren().size() - 1
            || progressBar.getChildren().get(stage + 2)
            .getStyleClass().toString().contains("progress-reset")) {
            // At the end state of the progress bar or
            // when the next item is not already done, reset this item
            return stage - 2;
        } else {
            // When the next item is already done, reset until this item
            return stage;
        }
    }

    /**
     * Fills the progressbar up to the current stage.
     * @param stage the current progress stage of the game
     */
    void fillProgress(final int stage) {
        for (int k = 0; k < progressBar.getChildren().size(); k++) {
            progressBar.getChildren().get(k).getStyleClass().clear();
            if (k <= stage) {
                progressBar.getChildren().get(k).getStyleClass().add("progress-made");
            } else {
                progressBar.getChildren().get(k).getStyleClass().add("progress-reset");
            }
            k++;
        }
    }

    /**
     * Update the activity Label.
     */
    void updateActivity() {
        String activeString = "" + getCameraHandler().getActive();
        activityStatus.setText(" Current activity: " + activeString.toLowerCase());
    }

    /**
     * Updates the amount of chests present in the room.
     * @param chests the amount of chests
     */
    void updateChests(final int chests) {
        numOfChestsOpened.setText(" Chests opened: " + chests + "/"
            + getProgress().getRoom().getChestList().size());
        if (progress.allChestsOpened()) {
            numOfChestsOpened.setText(" All chests have been opened!");
            numOfChestsOpened.setTextFill(Color.FORESTGREEN);
            getCameraHandler().setAllChestsDetected(true);
        } else {
            numOfChestsOpened.setTextFill(Color.BLACK);
            getCameraHandler().setAllChestsDetected(false);
        }
    }

    /**
     * Changes the time of the timer.
     *
     * @param elapsedTime the elapsed time
     */
    void changeTime(final long elapsedTime) {
        List<Chest> chestList = progress.getRoom().getChestList();
        if (chestList.size() > 0 && getCameraHandler().getBeginTime() != -1) {
            for (int i = 0; i < chestList.size(); i++) {
                Chest currentChest = chestList.get(i);
                long time = elapsedTime - currentChest.getBeginTime();

                if (currentChest.getChestState() == Chest.Status.OPENED) {
                    time = currentChest.getTimeFound() - currentChest.getBeginTime();
                    chestTimeStampList.get(i).setText(Util.getTimeString(time, false));
                } else if (currentChest.getChestState() == Chest.Status.TO_BE_OPENED) {
                    chestTimeStampList.get(i).setText(Util.getTimeString(time, false));
                } else if (currentChest.getChestState()
                    == Chest.Status.WAITING_FOR_SECTION_TO_START) {
                    chestTimeStampList.get(i).setText("");
                }
                updateTimeChestsPanel(time, i);
            }
        }
    }

    /**
     * Change information about if room is ended.
     * @param elapsedTime The time of the room.
     */
    void changeInformation(final long elapsedTime) {
        if (getCameraHandler().getBeginTime() != -1) {
            long time = elapsedTime - getCameraHandler().getBeginTime();
            if (time > TimeUnit.SECONDS.toNanos(progress.getRoom().getTargetDuration())) {
                gameStatus.setText(" Time is up! Game has ended.");
                gameStatus.setTextFill(Color.RED);
            } else {
                setProgressBarActive();
                long timeToEscape =
                    TimeUnit.SECONDS.toNanos(
                            getProgress().getRoom().getTargetDuration()
                             - TimeUnit.NANOSECONDS.toSeconds(time));
                String text = " Time left: " + Util.getTimeString(timeToEscape, true);
                timeLeft.setText(text);
                gameStatus.setTextFill(Color.FORESTGREEN);
                gameStatus.setText(" Game has started");
            }
        }
    }

    /**
     * Update the warning pane, show if needed and hide if not needed.
     * @param time the current time
     */
    public void updateWarningPane(final long time) {
        // When people are behind on schedule
        if (checkBehindSchedule(time) && !snoozeHint && !progress.allChestsOpened()) {
            // Get the warningPane of the statusPane and set it on visible
            statusPane.getChildren().get(2).setVisible(true);
        } else {
            statusPane.getChildren().get(2).setVisible(false);
        }
    }

    /**
     * Update the colors of the time text and check if the team is behind schedule.
     * @param time the current time - time chest was discovered
     * @param pos the current position in the chestlist
     */
    private void updateTimeChestsPanel(final long time, final int pos) {
        List<Chest> chestList = progress.getRoom().getChestList();
        Chest chest = chestList.get(pos);
        if (chest.getChestState() == Chest.Status.TO_BE_OPENED
            && !(TimeUnit.NANOSECONDS.toSeconds(time) <= chest.getTargetDurationInSec())) {
            if (chestTimeStampList.get(pos).getTextFill().equals(Color.GREEN)) {
                snoozeHint = false;
            }
            chestTimeStampList.get(pos).setTextFill(Color.RED);
        } else if ((chest.getChestState() == Chest.Status.TO_BE_OPENED
            && (TimeUnit.NANOSECONDS.toSeconds(time) <= chest.getTargetDurationInSec()))
            || (chest.getChestState() == Chest.Status.OPENED && TimeUnit.NANOSECONDS.toSeconds(time)
            < chest.getTargetDurationInSec())) {
            chestTimeStampList.get(pos).setTextFill(Color.GREEN);
        }
    }

    /**
     * Check if a warning pane needs to be shown.
     * @param time the current time
     * @return true if warning pane needs to be shown, false otherwise
     */
    private boolean checkBehindSchedule(final long time) {
        if (getCameraHandler().getBeginTime() != -1) {
            List<Chest> chestList = progress.getRoom().getChestList();
            for (Chest chest : chestList) {
                long seconds = TimeUnit.NANOSECONDS.toSeconds(time - chest.getBeginTime());
                if (chest.getChestState() == Chest.Status.TO_BE_OPENED
                    && (seconds > chest.getWarningTimeInSec()
                    || seconds > chest.getTargetDurationInSec())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Make the warningPane invisible until time is up and players are still behind.
     */
    public void startHintTimer() {
        final long timeUntilWarning = TimeUnit.SECONDS.toMillis(30);

        snoozeHint = true;
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                snoozeHint = false;
            }
        };
        Timer hintTimer = new Timer();
        // Wait 30 seconds before showing another warning
        hintTimer.schedule(task, timeUntilWarning);
    }

    /**
     * Set the progressBar on inactive.
     */
    private void setProgressBarActive() {
        for (Node child : progressBar.getChildren()) {
            child.setDisable(false);
        }
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

    /**
     * If the room is configured.
     * @return true if configured, false otherwise
     */
    public boolean isConfigured() {
        return configured;
    }

    /**
     * Return whether players are behind schedule or not.
     * @return behindSchedule.
     */
    boolean isBehindSchedule() {
        return behindSchedule;
    }

    /**
     * Return whether hint is snoozed or not.
     * @return snoozeHint
     */
    boolean getSnoozeHint() {
        return snoozeHint;
    }

    /**
     * Resets the progress to null.
     */
    public void resetProgressObject() {
        progress = null;
        this.configured = false;
    }

    /**
     * Set the label that displays the time left to escape.
     * @param timeLeftLabel the label
     */
    public void setTimeLeft(final Label timeLeftLabel) {
        this.timeLeft = timeLeftLabel;
    }
}
