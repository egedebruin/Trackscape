package gui.panes;

import gui.Util;
import gui.controllers.RoomController;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;

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
        statusPane = new FlowPane();
        statusPane.setVisible(false);
        statusPane.setAlignment(Pos.TOP_CENTER);
        final int vgap = 30;
        statusPane.setVgap(vgap);

        roomController.setStatusPane(statusPane);

        return statusPane;
    }

    /**
     * Initialize the statusPane with its children.
     */
    public void initializeStatusPane() {
        statusPane.getChildren().addAll(
            createWarningSign(), createSetupPane(), createProgressPane());
    }

    /**
     * Create the setup pane for the initial setup parameters from configuration.
     * @return setupPane.
     */
    private Pane createSetupPane() {
        final int buttonWidth = 50;
        ImageView ppl = Util.createButtonLogo("peopleIcon", buttonWidth);
        ImageView box = Util.createButtonLogo("chestIcon", buttonWidth);
        ImageView cam = Util.createButtonLogo("camIcon", buttonWidth);

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
        FlowPane progressPane = new FlowPane();
        progressPane.setPrefWidth(prefWidth);
        progressPane.setAlignment(Pos.CENTER);

        Label status = new Label("Status\n");
        status.getStyleClass().add("bold");
        numOfChestsOpened = new Label("Amount of opened chests: 0\n");
        progressPane.getChildren().addAll(status,
            numOfChestsOpened);

        roomController.setNumOfChestsOpened(numOfChestsOpened);

        return progressPane;
    }

    /**
     * Create a warning sign for when players are behind schedule.
     * @return the warningPane
     */
    private Pane createWarningSign() {
        final int warningWidth = 150;
        ImageView warningView = Util.createButtonLogo("warning", warningWidth);
        Label warningLabel = new Label(
            "The team is getting behind schedule!\nThey could use a hint.");
        warningLabel.setTextAlignment(TextAlignment.CENTER);

        FlowPane warningPane = new FlowPane();
        warningPane.setAlignment(Pos.CENTER);
        warningPane.getChildren().addAll(warningView, warningLabel);
        warningPane.setVisible(false);

        return warningPane;
    }
}