package gui.panes;

import gui.Controller;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private double fittedWidth;

    /**
     * Constructor for ProgressBar.
     * @param control the controller
     */
    public ProgressBar(final Controller control) {
        this.controller = control;
    }

    /**
     * Create the progressBar.
     * @return progressBar
     */
    public GridPane createProgressBarPane() {
        progressBar = new GridPane();
        progressBar.setAlignment(Pos.CENTER);
        return progressBar;
    }

    /**
     * Construct and initialize progressbar.
     * @return progressBar
     */
    public GridPane constructProgressBar() {
        createItems();

        int spot = 0;
        for (int k = 0; k < progressStages.size(); k++) {
            progressBar.add(progressStages.get(k), spot, 0);
            spot = spot + 1;
            if (k != progressStages.size() - 1) {
                progressBar.add(createLineLabel(), spot, 0);
                spot = spot + 1;
            }
        }

        setItemsOnDone();

        return progressBar;
    }

    /**
     * Create the items of the progress bar.
     */
    private void createItems() {
        final int chests = 3;   // should retrieve number from handler
        final int steps = 3;    // should retrieve number from handler

        final int screenParts = 4;
        GraphicsDevice gd =
            GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int screenWidth = gd.getDisplayMode().getWidth();
        fittedWidth = (screenWidth) / (screenParts * (chests + steps + (chests + steps - 1)));

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

        final double size = fittedWidth;
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
            + "\\src\\main\\java\\gui\\images\\blackchest.png");
        Image chestImage = new Image(chest.toURI().toString());

        final double size = 1.4 * fittedWidth;
        ImageView chestIV = new ImageView(chestImage);
        chestIV.setFitWidth(size);
        chestIV.setFitHeight(size);

        Label chestLabel = new Label("");
        chestLabel.setGraphic(chestIV);

        final double circleSize = 1.3 * fittedWidth + 25;
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
        final int puzzlePieces = 8;
        Random random = new Random();

        File puzzle = new File(System.getProperty("user.dir")
            + "\\src\\main\\java\\gui\\images\\puzzlepieces\\puzzle"
            + random.nextInt(puzzlePieces) + ".png");
        Image puzzleImage = new Image(puzzle.toURI().toString());

        final double size = fittedWidth;
        ImageView puzzleIV = new ImageView(puzzleImage);
        puzzleIV.setFitWidth(size);
        puzzleIV.setFitHeight(size);

        Label puzzleLabel = new Label();
        puzzleLabel.setAlignment(Pos.CENTER);
        puzzleLabel.setGraphic(puzzleIV);

        final double circleSize = fittedWidth + 20;
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

            if (k == progressBar.getChildren().size() - 1) {
                // Done! Last box is unlocked.
                progressBar.getChildren().get(k).getStyleClass().add("progress-made");
            } else {
                progressBar.getChildren().get(k).getStyleClass().add("progress-made");
            }
            k++;
        }
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
    public void closeProgressBar() {
        progressBar.getChildren().clear();
    }

    /**
     * Retrieves the progressBar.
     * @return ProgressBar
     */
    public Pane getProgressBar() {
        return progressBar;
    }

}
