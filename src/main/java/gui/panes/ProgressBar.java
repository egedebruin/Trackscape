package gui.panes;

import gui.Controller;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that creates a progress bar.
 */
public class ProgressBar {
    /**
     * Class parameters.
     */
    private Controller controller;
    private GridPane progressBar;
    private List<Label> progressStages;

    /**
     * Constructor for ProgressBar.
     * @param control the controller
     */
    public ProgressBar(final Controller control) {
        this.controller = control;
    }

    /**
     * Creates the progressBarPane.
     * @return Pane with progressbar
     */
    public Pane createProgressBarPane() {
        progressBar = createProgressBar();
        setItemsOnDone();
        return progressBar;
    }

    /**
     * Create HBox with progressbar.
     * @return progressBar
     */
    private GridPane createProgressBar() {
        createItems();

        progressBar = new GridPane();
        progressBar.setAlignment(Pos.CENTER);

        int spot = 0;
        for (int k = 0; k < progressStages.size(); k++) {
            progressBar.add(progressStages.get(k), spot, 0);
            spot = spot + 1;
            if (k != progressStages.size() - 1) {
                progressBar.add(createLineLabel(), spot, 0);
                spot = spot + 1;
            }
        }

        return progressBar;
    }

    /**
     * Create the items of the progress bar.
     */
    private void createItems() {
        final int chests = 3;   // should retrieve number from handler
        final int steps = 1;    // should retrieve number from handler

        progressStages = new ArrayList<>();

        // Add chests and their puzzle steps to the list
        for (int i = 0; i < chests; i++) {
            for (int j = 0; j < steps; j++) {
                progressStages.add(createPuzzleLabel());
            }
            progressStages.add(createChestLabel());
        }

        // Add initial stylesheet
        for (int m = 0; m < progressStages.size(); m++) {
            progressStages.get(m).getStyleClass().add("progress-reset");
        }
    }

    /**
     * Create the label with a line.
     * @return lineLabel
     */
    private Label createLineLabel() {
        File line = new File(System.getProperty("user.dir")
            + "\\src\\main\\java\\gui\\images\\line.png");
        Image lineImage = new Image(line.toURI().toString());

        final int size = 50;
        ImageView iv = new ImageView(lineImage);
        iv.setFitWidth(size);
        iv.setFitHeight(size);

        Label lineLabel = new Label();
        lineLabel.setGraphic(iv);

        lineLabel.setId("line");
        return lineLabel;
    }

    /**
     * Create the label for a chest.
     * @return ChestLabel
     */
    private Label createChestLabel() {
        File chest = new File(System.getProperty("user.dir")
            + "\\src\\main\\java\\gui\\images\\chest.png");
        Image chestImage = new Image(chest.toURI().toString());

        final int size = 60;
        ImageView chestIV = new ImageView(chestImage);
        chestIV.setFitWidth(size);
        chestIV.setFitHeight(size);

        Label chestLabel = new Label("");
        chestLabel.setGraphic(chestIV);

        final int circleSize = 90;
        chestLabel.setMinHeight(circleSize);
        chestLabel.setMinWidth(circleSize);
        chestLabel.setAlignment(Pos.CENTER);

        chestLabel.setId("chest");
        return chestLabel;
    }

    /**
     * Create the ImageView for puzzle Image.
     * @return PuzzleImageView
     */
    private Label createPuzzleLabel() {
        File puzzle = new File(System.getProperty("user.dir")
            + "\\src\\main\\java\\gui\\images\\puzzle.png");
        Image puzzleImage = new Image(puzzle.toURI().toString());

        final int size = 50;
        ImageView puzzleIV = new ImageView(puzzleImage);
        puzzleIV.setFitWidth(size);
        puzzleIV.setFitHeight(size);

        Label puzzleLabel = new Label();
        puzzleLabel.setAlignment(Pos.CENTER);
        puzzleLabel.setGraphic(puzzleIV);

        final int circleSize = 70;
        puzzleLabel.setMinHeight(circleSize);
        puzzleLabel.setMinWidth(circleSize);

        puzzleLabel.setId("puzzle");
        return puzzleLabel;
    }

    /**
     * Fills the progressbar up to the current stage.
     * @param stage the current progress stage of the game
     */
    private void fillProgress(final int stage) {
        for (int k = 0; k <= stage; k++) {
            progressBar.getChildren().get(k);
            progressBar.getChildren().get(k).getStyleClass().clear();
            progressBar.getChildren().get(k).getStyleClass().add("progress-made");
            k++;
        }
    }

    /**
     * Resets the progressbar to the current stage.
     * @param stage the current progress stage of the game
     */
    private void resetProgress(final int stage) {
        if (stage == 0
            && progressBar.getChildren().get(2).getStyleClass().toString().contains("progress-reset")) {
            progressBar.getChildren().get(stage).getStyleClass().clear();
            progressBar.getChildren().get(stage).getStyleClass().add("progress-reset");
        }
        for (int k = stage + 2; k < progressBar.getChildren().size(); k++) {
            progressBar.getChildren().get(k).getStyleClass().clear();
            progressBar.getChildren().get(k).getStyleClass().add("progress-reset");
            k++;
        }
    }

    /**
     * Let the host set items on done when the team has finished them.
     */
    private void setItemsOnDone() {
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
     * Reset the progressBar when stream is closed.
     */
    private void closeProgressBar() {
        progressBar.getChildren().clear();
    }

}
