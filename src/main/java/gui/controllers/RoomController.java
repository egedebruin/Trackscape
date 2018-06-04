package gui.controllers;

import handlers.CameraHandler;
import javafx.scene.layout.GridPane;
import room.Progress;

/**
 * Controller for the gui with a room.
 */
public class RoomController {

    private Progress progress;
    private GridPane progressBar;
    private CameraHandler cameraHandler;
    private int progressCompleted;

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
    }

    /**
     * Close the controller when the stream is closed.
     */
    public void closeController() {
        progressBar.getChildren().clear();
    }

    /**
     * Let the host set items on done when the team has finished them.
     */
    public void setItemsOnDone() {
        progressBar.getChildren().forEach(item -> {
            item.setOnMouseClicked(event -> {
                if (item.getId() != "line") {
                    if (item.getStyleClass().toString().contains("progress-reset")) {
                        fillProgress(progressBar.getChildren().indexOf(item));
                    } else {
                        resetProgress(progressBar.getChildren().indexOf(item));
                    }
                }
            });
        });
    }

    /**
     * Fills the progressbar up to the current stage.
     * @param stage the current progress stage of the game
     */
    public void fillProgress(final int stage) {
        for (int k = 0; k <= stage; k++) {
            //progressBar.getChildren().get(k);
            progressBar.getChildren().get(k).getStyleClass().clear();

            if (k == progressBar.getChildren().size() - 1) {
                // Done! Last box is unlocked.
                progressBar.getChildren().get(k).getStyleClass().add("progress-made");
            } else {
                progressBar.getChildren().get(k).getStyleClass().add("progress-made");
            }
            k++;
        }
        progressCompleted = stage;
    }

    /**
     * Resets the progressbar to the current stage.
     * @param stage the current progress stage of the game
     */
    private void resetProgress(final int stage) {
        if (stage == progressBar.getChildren().size() - 1) {
            clearStyleSheet(stage);
        }
        if (stage < progressBar.getChildren().size() - 1) {
            if (progressBar.getChildren().get(stage + 2)
                .getStyleClass().toString().contains("progress-reset")) {
                clearStyleSheet(stage);
            } else {
                for (int k = stage + 2; k < progressBar.getChildren().size(); k++) {
                    clearStyleSheet(k);
                    k++;
                }
            }
        }
        this.progressCompleted = stage;
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
        return progress;
    }

    /**
     * Set the progressBar.
     * @param newProgressBar the new progressBar
     */
    public void setProgressBar(final GridPane newProgressBar) {
        this.progressBar = newProgressBar;
    }

    public int getProgressCompleted() {
        return progressCompleted;
    }
    
}
