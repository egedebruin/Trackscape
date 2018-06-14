package gui.controllers;

import gui.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import room.Chest;
import room.Progress;

/**
 * Controller for the gui with a room.
 */
public class RoomController extends Controller {

    private Progress progress;
    private Pane progressBar;
    private Pane statusPane;
    private Label gameStatus;
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
     * Configures everything needed.
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
     * @param chests the amount of chests in the game
     * @param totalDuration the total duration of the game
     * @param sectionList the list with the amount of sections per chest
     * @param durationList the list with the duration for each chest
     */
    public void manualConfig(final int players, final int chests,
                             final int totalDuration, final ArrayList<Integer> sectionList,
                             final ArrayList<Integer> durationList) {
        progress = new Progress(players, chests, totalDuration, sectionList, durationList);
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
        if (progress != null) {
            progress.updateProgress();
            fillProgress(progress.getFillCount());
            changeTime(now);
            changeInformation(now);
            // Update the progressPane
            updateChests(progress.getRoom().getChestsOpened());
            updateActivity();
            updateWarningPane();
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
    private void newItemDone(final int index) {
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
    private void itemsRemoved(final int index) {
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
    public void fillProgress(final int stage) {
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
    private void updateActivity() {
        String activeString = "" + getCameraHandler().getActive();
        activityStatus.setText(" Current activity: " + activeString.toLowerCase());
    }

    /**
     * Updates the amount of chests present in the room.
     * @param chests the amount of chests
     */
    public void updateChests(final int chests) {
        numOfChestsOpened.setText(" Chests opened: " + chests + "/"
            + getProgress().getRoom().getChestList().size());
        if (progress.allChestsOpened()) {
            numOfChestsOpened.setText(" All chests have been opened!");
            numOfChestsOpened.setTextFill(Color.FORESTGREEN);
        } else {
            numOfChestsOpened.setTextFill(Color.BLACK);
        }
    }

    /**
     * Changes the time of the timer.
     *
     * @param elapsedTime the elapsed time
     */
    public void changeTime(final long elapsedTime) {
        List<Chest> chestList = progress.getRoom().getChestList();
        if (chestList.size() > 0 && getCameraHandler().getBeginTime() != -1) {
            for (int i = 0; i < chestList.size(); i++) {
                Chest currentChest = chestList.get(i);
                long time = elapsedTime - getCameraHandler().getBeginTime();

                if (currentChest.getChestState() == Chest.Status.OPENED) {
                    time = currentChest.getTimeFound() - getCameraHandler().getBeginTime();
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
    public void changeInformation(final long elapsedTime) {
        if (getCameraHandler().getBeginTime() != -1) {
            long time = elapsedTime - getCameraHandler().getBeginTime();
            if (time > TimeUnit.SECONDS.toNanos(progress.getRoom().getTargetDuration())) {
                gameStatus.setText(" Time is up! Game has ended.");
                gameStatus.setTextFill(Color.RED);
            } else {
                setProgressBarActive();
                gameStatus.setText(" Game has started");
                gameStatus.setTextFill(Color.FORESTGREEN);
            }
        }
    }

    /**
     * Update the warning pane, show if needed and hide if not needed.
     */
    public void updateWarningPane() {
        // Update the warningPane
        // When people are behind on schedule
        if (behindSchedule && !snoozeHint && !progress.allChestsOpened()) {
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
     */
    private void setProgressBarActive() {
        for (Node child : progressBar.getChildren()) {
            child.setDisable(false);
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
     * If the room is configured.
     * @return true if configured, false otherwise
     */
    public boolean isConfigured() {
        return configured;
    }
}
