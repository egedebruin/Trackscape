package gui.panes;

import gui.controllers.MainController;
import gui.controllers.RoomController;
import handlers.JsonHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

import java.io.File;

/**
 * Class that constructs the StatusPane for the VideoPane.
 */
public class StatusPane {
    /**
     * Class parameters.
     */
    private RoomController roomController;
    private JsonHandler jsonHandler = new JsonHandler("files/example.json");
    private Label numOfChestsOpened;

    /**
     * Constructor for StatusPane.
     * @param control the mainController
     */
    public StatusPane(final MainController control) {
        this.roomController = control.getRoomController();
    }

    /**
     * Creates the StatusPane.
     * @return statusPane the statusPane
     */
    public Pane createStatusPane() {
        FlowPane statusPane = new FlowPane();
        statusPane.setVisible(false);

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
        statusPane.getChildren().addAll(status, numOfChests, numOfPersons,
                numOfChestsOpened, createWarningSign());

        roomController.setStatusPane(statusPane);
        roomController.setNumOfChestsOpened(numOfChestsOpened);

        return statusPane;
    }

    /**
     * Creates a warning sign in the statusPane when players are delaying.
     * @return the warningPane
     */
    private Pane createWarningSign() {
        FlowPane warningPane = new FlowPane();
        warningPane.setVisible(false);

        File warningFile = new File(System.getProperty("user.dir")
                + "\\src\\main\\java\\gui\\images\\warning.png");
        Image warningImage = new Image(warningFile.toURI().toString());
        ImageView warningView = new ImageView();

        warningView.setImage(warningImage);

        warningPane.getChildren().add(warningView);
        return warningPane;
    }

}
