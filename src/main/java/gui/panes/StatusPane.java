package gui.panes;

import handlers.JsonHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

/**
 * Class that constructs the StatusPane for the VideoPane.
 */
public class StatusPane {
    /**
     * Class parameters.
     */
    private JsonHandler jsonHandler = new JsonHandler("files/example.json");
    private Label numOfChestsOpened;
    private Label numOfPersonsDetected;

    /**
     * Constructor for StatusPane.
     */
    public StatusPane() {
    }

    /**
     * Creates the StatusPane.
     * @return statusPane the statusPane
     */
    public Pane createStatusPane() {
        FlowPane statusPane = new FlowPane();

        final int prefWidth = 350;
        statusPane.setPrefWidth(prefWidth);
        statusPane.setAlignment(Pos.CENTER);

        Label status = new Label("Status" + "\n");
        status.getStyleClass().add("bold");
        Label numOfChests = new Label("Amount of chests in the room: "
                + jsonHandler.getAmountChests(0) + "\n");
        Label numOfPersons = new Label("Amount of players: "
                + jsonHandler.getAmountPeople(0) + "\n");
        numOfChestsOpened = new Label("Amount of opened chests: ?" + "\n");
        numOfPersonsDetected = new Label("Amount of persons detected: ?");
        statusPane.getChildren().addAll(status, numOfChests, numOfPersons,
                numOfChestsOpened, numOfPersonsDetected);

        return statusPane;
    }

    /**
     * Updates the amount of chests present in the room.
     * @param chests the amount of chests
     */
    public void updateChests(final String chests) {
        numOfChestsOpened.setText("Amount of opened chests: " + chests + "\n");
    }

    /**
     * Updates the amount of persons present in the room.
     * @param persons the amount of persons
     */
    public void updatePersons(final String persons) {
        numOfPersonsDetected.setText("Amount of persons detected: " + persons + "\n");
    }
}
