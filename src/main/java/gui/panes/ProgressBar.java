package gui.panes;

import gui.controllers.RoomController;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import room.Chest;

import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;
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
    private RoomController controller;
    private GridPane progressBar;
    private List<Label> progressStages;
    private double fittedWidth;

    /**
     * Constructor for ProgressBar.
     * @param control the controller
     */
    public ProgressBar(final RoomController control) {
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
     */
    public void constructProgressBar() {
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
        controller.setProgressBar(progressBar);
        controller.setItemsOnDone();
        }

    /**
     * Create the items of the progress bar.
     */
    private void createItems() {
        List<Chest> chests = controller.getProgress().getRoom().getChestList();

        GraphicsDevice gd =
            GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int screenWidth = gd.getDisplayMode().getWidth();

        final int sectionsForMaxScreen = 10;
        if (controller.getProgress().getTotalSections() < sectionsForMaxScreen) {
            final int screenParts = 6;
            fittedWidth = (screenWidth)
                / (screenParts * (controller.getProgress().getTotalSections()));
        } else {
            final int screenParts = 4;
            fittedWidth = (screenWidth)
                / (screenParts * (controller.getProgress().getTotalSections()));
        }

        progressStages = new ArrayList<>();
        // Add chests and their puzzle steps to the list
        for (Chest chest : chests) {
            for (int i = 1; i < chest.getNumberOfSubSections(); i++) {
                progressStages.add(createPuzzleLabel());
            }
            progressStages.add(createChestLabel());
        }

        // Add initial stylesheet
        for (int m = 0; m < progressStages.size(); m++) {
            progressStages.get(m).getStyleClass().add("progress-reset");
            progressStages.get(m).setDisable(true);
        }
    }

    /**
     * Create the label with a line.
     * @return lineLabel
     */
    private Label createLineLabel() {
        File line = new File(System.getProperty("user.dir")
            + "\\src\\main\\java\\gui\\images\\progressbar\\line.png");
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
            + "\\src\\main\\java\\gui\\images\\progressbar\\blackchest.png");
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
            + "\\src\\main\\java\\gui\\images\\progressbar\\puzzle"
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
     * Retrieves the progressBar.
     * @return ProgressBar
     */
    public Pane getProgressBar() {
        return progressBar;
    }

}
