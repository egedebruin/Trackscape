package gui.panes;

import gui.Controller;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
    private MenuMediaPane menuMediaPane;
    private MediaBar mediaBar;
    private GridPane progressBar = new GridPane();
    private List<Label> progressStages = new ArrayList<>();

    /**
     * Constructor for ProgressBar.
     * @param control the controller
     * @param mmp the MenuMediaPane
     */
    public ProgressBar(final Controller control, final MenuMediaPane mmp) {
        this.controller = control;
        this.menuMediaPane = mmp;
        this.mediaBar = new MediaBar(controller, menuMediaPane);
    }

    /**
     * Creates the bottom pane for the videoPane with mediaBar and progressBar.
     * @return VBox with buttons and progressbar
     */
    public VBox createMediaAndProgressBar() {
        final int spacing = 30;
        progressBar = createProgressBar();
        HBox mediaButtons = mediaBar.createMediaBar();
        VBox buttonsAndProgress =  new VBox();
        buttonsAndProgress.setSpacing(spacing);
        buttonsAndProgress.getChildren().addAll(progressBar, mediaButtons);

        gridPaneEvent();

        return buttonsAndProgress;
    }

    /**
     * Create HBox with progressbar.
     * @return progressBar
     */
    private GridPane createProgressBar() {
        final int chests = 3;
        final int steps = 1;

        // Add chests and their puzzle steps to the list
        for (int i = 1; i <= chests; i++) {
            for (int j = 1; j <= steps; j++) {
                progressStages.add(createPuzzleLabel());
            }
            progressStages.add(createChestLabel());
        }

        // Add stylesheet
        for (int m = 0; m < progressStages.size(); m++) {
            progressStages.get(m).getStyleClass().add("progress-bar");
        }

        progressBar.setAlignment(Pos.CENTER);

        int spot = 0;
        for (int k = 0; k < progressStages.size(); k++) {
            progressStages.get(k).setId("item");
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

        return puzzleLabel;
    }

    /**
     * Fills the progressbar up to the current stage.
     * @param stage the current progress stage where the game is at
     */
    private void fillProgress(final int stage) {
        for (int k = 0; k < stage; k++) {
            progressStages.get(k).getStyleClass().clear();
            progressStages.get(k).getStyleClass().add("progress-bar-done");
        }
    }

    /**
     * Let the host set items on done when the team has finished them.
     */
    private void setItemsOnDone() {
        progressBar.getChildren().forEach(item -> {
            item.setOnMouseClicked(event -> {
                if (item.getId() == "item") {
                    if (item.getStyleClass().toString() == "progress-bar") {
                        item.getStyleClass().clear();
                        item.getStyleClass().add("progress-bar-done");
                    } else {
                        item.getStyleClass().clear();
                        item.getStyleClass().add("progress-bar");
                    }
                }
            });
        });
    }

}
