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
import java.util.concurrent.TimeUnit;

/**
 * Class that constructs the StatusPane for the VideoPane.
 */
public class StatusPane {
    private RoomController roomController;
    private FlowPane statusPane;
    private Label gameStatus;
    private Label timeLeft;
    private Label numOfChestsOpened;
    private Label activity;

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
        final int bigPadding = 25;
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
        if (roomController.isConfigured()) {
            statusPane.getChildren().addAll(createSetupPane(),
                createProgressPane(), createWarningSign());
            statusPane.setVisible(true);
        }
    }

    /**
     * Create the setup pane for the initial setup parameters from configuration.
     * @return setupPane.
     */
    private Pane createSetupPane() {
        final int buttonWidth = 50;
        final int hgap = 30;

        Label cameras = new Label(""
            + roomController.getCameras());
        cameras.setGraphic(Util.createImageViewLogo("icons\\camIcon", buttonWidth));
        Label persons = new Label(""
            + roomController.getProgress().getRoom().getNumberOfPeople());
        persons.setGraphic(Util.createImageViewLogo("icons\\peopleIcon", buttonWidth));
        Label chests = new Label(""
            + roomController.getProgress().getRoom().getChestList().size());
        chests.setGraphic(Util.createImageViewLogo("icons\\chestIcon", buttonWidth));

        GridPane setupPane = new GridPane();
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

        Label status = new Label("Status\n");
        status.getStyleClass().add("bold");

        initializeProgressLabels();

        VBox progressPane = new VBox();
        progressPane.setPrefWidth(prefWidth);
        progressPane.setAlignment(Pos.CENTER_LEFT);
        progressPane.getChildren().addAll(status, gameStatus, timeLeft,
            numOfChestsOpened, activity, createChestTimePane());

        return progressPane;
    }

    /**
     * Initialize and set the dynamic labels for the progressPane in roomController.
     */
    private void initializeProgressLabels() {
        final int buttonWidth = 20;

        gameStatus = new Label(" Game will start soon");
        gameStatus.setGraphic(Util.createImageViewLogo("//icons//star", buttonWidth));
        timeLeft = new Label(" Time left: " + Util.getTimeString(TimeUnit.SECONDS.toNanos(
            roomController.getProgress().getRoom().getTargetDuration()), true));
        timeLeft.setGraphic(Util.createImageViewLogo("//icons//star", buttonWidth));
        numOfChestsOpened = new Label(" Chests opened: 0 / "
            + roomController.getProgress().getRoom().getChestList().size());
        numOfChestsOpened.setGraphic(Util.createImageViewLogo("//icons//star", buttonWidth));
        activity = new Label(" Current activity: low");
        activity.setGraphic(Util.createImageViewLogo("//icons//star", buttonWidth));

        roomController.setGameStatus(gameStatus);
        roomController.setTimeLeft(timeLeft);
        roomController.setNumOfChestsOpened(numOfChestsOpened);
        roomController.setActivityStatus(activity);
    }

    /**
     * Create the pane where chests and current/target time are shown.
     * @return chestTimePane
     */
    private Pane createChestTimePane() {
        final int topInset = 20;

        GridPane chestTimePane = new GridPane();
        chestTimePane.getStyleClass().add("grid-lines");
        chestTimePane.setAlignment(Pos.CENTER);
        chestTimePane.setPadding(new Insets(topInset, 0, 0, 0));

        chestTimePane.add(new Label("  Chest  "), 0, 0);
        chestTimePane.add(new Label("  Time  "), 1, 0);
        chestTimePane.add(new Label("  Target  "), 2, 0);

        return addLabelsPerChest(chestTimePane);
    }

    /**
     * Add the chestNumber, currentTime, and targetTime labels for each chest to the gridPane.
     * @param chestTimePane the pane where current and target times of chests are shown.
     * @return chestTimePane
     */
    private Pane addLabelsPerChest(final GridPane chestTimePane) {
        List<Chest> chestList = roomController.getProgress().getRoom().getChestList();

        for (int i = 0; i < chestList.size(); i++) {
            Label chestLabel = new Label((i + 1) + "");
            chestLabel.setTextAlignment(TextAlignment.CENTER);
            Label timeLabel = roomController.getChestTimeStampList().get(i);
            Label targetLabel = new Label(Util.getTimeString(TimeUnit.SECONDS.toNanos(
                chestList.get(i).getTargetDurationInSec()), false));
            chestTimePane.add(chestLabel, 0, (i + 1));
            chestTimePane.add(timeLabel, 1, (i + 1));
            chestTimePane.add(targetLabel, 2, (i + 1));
        }
        return chestTimePane;
    }

    /**
     * Create a warning sign for when players are behind schedule.
     * @return the warningPane
     */
    private Pane createWarningSign() {
        final int warningWidth = 150;
        final int buttonWidth = 125;

        FlowPane warningPane = new FlowPane();

        ImageView warningView = Util.createImageViewLogo("icons\\warning", warningWidth);

        Label warningLabel = new Label(
            " The team is getting behind schedule! \nThey could use a hint.");
        warningLabel.setTextAlignment(TextAlignment.CENTER);

        Button okButton = new Button();
        okButton.setGraphic(Util.createImageViewLogo("buttons\\okButton", buttonWidth));
        okButton.setCursor(Cursor.HAND);
        okButton.setOnAction(event -> {
            roomController.startHintTimer();
        });

        warningPane.setAlignment(Pos.CENTER);
        warningPane.getChildren().addAll(warningView, warningLabel, okButton);
        warningPane.setVisible(false);
        warningPane.getStyleClass().add("warning");

        return warningPane;
    }

    /**
     * Retrieve the statusPane.
     * @return statusPane
     */
    public FlowPane getStatusPane() {
        return statusPane;
    }
}