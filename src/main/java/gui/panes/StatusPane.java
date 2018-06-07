package gui.panes;

import gui.Util;
import gui.controllers.RoomController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import room.Chest;

import java.util.List;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Class that constructs the StatusPane for the VideoPane.
 */
public class StatusPane {
    /**
     * Class parameters.
     */
    private RoomController roomController;
    private FlowPane statusPane;
    private Label numOfChestsOpened;

    /**
     * Constructor for StatusPane.
     * @param control the mainController
     */
    public StatusPane(final RoomController control) {
        this.roomController = control;
    }

    /**
     * Creates the statusPane that holds information about the current game.
     * @return statusPane the statusPane
     */
    public Pane createStatusPane() {
        final int bigPadding = 50;
        final int smallPadding = 20;
        final int vgap = 40;

        statusPane = new FlowPane();
        statusPane.setVisible(false);
        statusPane.setAlignment(Pos.TOP_CENTER);
        statusPane.setPadding(new Insets(bigPadding, smallPadding, smallPadding, 0));
        statusPane.setVgap(vgap);

        roomController.setStatusPane(statusPane);

        return statusPane;
    }

    /**
     * Initialize the statusPane with its children.
     */
    public void initializeStatusPane() {
        statusPane.getChildren().addAll(createSetupPane(),
            createProgressPane(), createWarningSign());
        statusPane.setVisible(true);
    }

    /**
     * Create the setup pane for the initial setup parameters from configuration.
     * @return setupPane.
     */
    private Pane createSetupPane() {
        final int buttonWidth = 50;
        ImageView ppl = Util.createImageViewLogo("icons\\peopleIcon", buttonWidth);
        ImageView box = Util.createImageViewLogo("icons\\chestIcon", buttonWidth);
        ImageView cam = Util.createImageViewLogo("icons\\camIcon", buttonWidth);

        Label cameras = new Label(""
            + roomController.getProgress().getRoom().getCameraHandler().listSize());
        cameras.setGraphic(cam);
        Label persons = new Label(""
            + roomController.getProgress().getRoom().getNumberOfPeople());
        persons.setGraphic(ppl);
        Label chests = new Label(""
            + roomController.getProgress().getRoom().getChestList().size());
        chests.setGraphic(box);

        GridPane setupPane = new GridPane();
        final int hgap = 30;
        setupPane.setHgap(hgap);

        setupPane.add(cameras, 0, 0);
        setupPane.add(persons, 1, 0);
        setupPane.add(chests, 2, 0);

        return setupPane;
    }

    /**
     * Create the progressPane where chests and corresponding time are shown.
     * @return progressPane
     */
    private Pane createProgressPane() {
        final int prefWidth = 350;
        VBox progressPane = new VBox();
        progressPane.setPrefWidth(prefWidth);
        progressPane.setAlignment(Pos.CENTER_LEFT);

        Label status = new Label("Status\n");
        status.getStyleClass().add("bold");

        numOfChestsOpened = new Label("Amount of opened chests: 0");
        progressPane.getChildren().addAll(status,
                numOfChestsOpened);

        // Get the amount of chests that are present in the room and create
        // the same amount of labels showing the time spend for activities
        // belonging to these chests. (By a for loop?)

        List<Chest> chestList = roomController.getProgress().getRoom().getChestList();

        for (int i = 0; i < chestList.size(); i++) {
            Label chest = new Label();
            chest.setText("Chest " + (i+1) + "/" + chestList.size() + " " + chestList.get(i).getTargetDurationInSec() + " ");
            progressPane.getChildren().add(chest);
        }

        roomController.setNumOfChestsOpened(numOfChestsOpened);

        return progressPane;
    }

    /**
     * Create a warning sign for when players are behind schedule.
     * @return the warningPane
     */
    private Pane createWarningSign() {
        FlowPane warningPane = new FlowPane();

        final int warningWidth = 150;
        ImageView warningView = Util.createImageViewLogo("icons\\warning", warningWidth);

        Label warningLabel = new Label(
            "The team is getting behind schedule!\nThey could use a hint.");
        warningLabel.setTextAlignment(TextAlignment.CENTER);

        final int buttonWidth = 125;
        Button okButton = new Button();
        okButton.setGraphic(Util.createImageViewLogo("buttons\\okButton", buttonWidth));
        okButton.setCursor(Cursor.HAND);
        okButton.setOnAction(event -> {
            startHintTimer();
        });

        warningPane.setAlignment(Pos.CENTER);
        warningPane.getChildren().addAll(warningView, warningLabel, okButton);
        warningPane.setVisible(false);

        return warningPane;
    }

    /**
     * Make the warningPane invisible until time is up and players are still behind.
     */
    private void startHintTimer() {
        roomController.snoozeHint(true);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                roomController.snoozeHint(false);
            }
        };
        Timer hintTimer = new Timer();
        final int timeUntilWarning = 2000;
        hintTimer.schedule(task, timeUntilWarning);
    }

    /**
     * Retrieve the statusPane.
     * @return statusPane
     */
    public FlowPane getStatusPane() {
        return statusPane;
    }
}