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


    public StatusPane() {
    }

    public Pane createStatusPane() {
        FlowPane statusPane = new FlowPane();
        statusPane.setPrefWidth(350);
        statusPane.setAlignment(Pos.CENTER);

        Label status = new Label("Status" + "\n");
        status.getStyleClass().add("bold");
        Label numOfChests = new Label("Amount of chests in the room: " + jsonHandler.getAmountChests(0) + "\n");
        Label numOfPersons = new Label("Amount of players: " + jsonHandler.getAmountPeople(0) + "\n");
        Label numOfChestsOpened = new Label("Amount of opened chests: ?" + "\n");
        Label numOfPersonsDetected = new Label("Amount of persons detected: ?");
        statusPane.getChildren().addAll(status, numOfChests, numOfPersons, numOfChestsOpened, numOfPersonsDetected);

        return statusPane;
    }
}
