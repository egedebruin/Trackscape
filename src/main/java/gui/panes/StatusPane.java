package gui.panes;

import gui.Util;
import gui.controllers.RoomController;
import java.util.List;
import java.util.concurrent.TimeUnit;
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

/**
 * Class that constructs the StatusPane for the VideoPane.
 */
public class StatusPane {
    /**
     * Class parameters.
     */
    private RoomController roomController;
    private FlowPane statusPane;
    private Label gameStatus;
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
            + roomController.getProgress().getRoom().getLinkList().size());
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

        final int buttonWidth = 20;
        gameStatus = new Label(" Game will start soon");
        gameStatus.setGraphic(Util.createImageViewLogo(
            "//icons//star", buttonWidth));
        numOfChestsOpened = new Label(" Chests opened: 0 / "
            + roomController.getProgress().getRoom().getChestList().size());
        numOfChestsOpened.setGraphic(Util.createImageViewLogo(
            "//icons//star", buttonWidth));
        activity = new Label(" Current activity: low");
        activity.setGraphic(Util.createImageViewLogo(
            "//icons//star", buttonWidth));

        progressPane.getChildren().addAll(status, gameStatus,
                numOfChestsOpened, activity, createChestTimePane());

        roomController.setGameStatus(gameStatus);
        roomController.setNumOfChestsOpened(numOfChestsOpened);
        roomController.setActivityStatus(activity);

        return progressPane;
    }

    /**
     * Create the pane where chests and current time are shown.
     * @return chestTimePane
     */
    private Pane createChestTimePane() {
        GridPane chestTimePane = new GridPane();
        final int topInset = 20;
        chestTimePane.getStyleClass().add("grid-lines");
        chestTimePane.setPadding(new Insets(topInset, 0, 0, 0));
        chestTimePane.setAlignment(Pos.CENTER);

        Label chest = new Label("  Chest  ");
        Label elapsedTime = new Label("  Time  ");
        Label targetTime = new Label("  Target  ");

        chestTimePane.add(chest, 0, 0);
        chestTimePane.add(elapsedTime, 1, 0);
        chestTimePane.add(targetTime, 2, 0);

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
        FlowPane warningPane = new FlowPane();

        final int warningWidth = 150;
        ImageView warningView = Util.createImageViewLogo("icons\\warning", warningWidth);

        Label warningLabel = new Label(
            " The team is getting behind schedule! \nThey could use a hint.");
        warningLabel.setTextAlignment(TextAlignment.CENTER);

        final int buttonWidth = 125;
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