package gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * Class that constructs the inputScene.
 */
public class InputScene extends BaseScene {
    /**
     * Class parameters.
     */
    private MonitorScene monitorScene;

    /**
     * Constructor.
     * @param ctrl the controller
     */
    public InputScene(final Controller ctrl) {
        super(ctrl);
        this.monitorScene = new MonitorScene(getController());
    }

    /**
     * createInputScene.
     * Creates the scene where host can initialize the game.
     * @param width of the scene
     * @param height of the scene
     * @param primaryStage starting stage
     * @param stylesheet current stylesheet
     * @return inputScene
     */
    public Scene createInputScene(final int width, final int height,
                            final Stage primaryStage, final String stylesheet) {
        BorderPane root = new BorderPane();
        root.setTop(createTopPane());
        root.setCenter(createInputPane(
            primaryStage, stylesheet));
        root.setBottom(createBottomPane());

        Scene inputScene = new Scene(root, width, height);
        inputScene.getStylesheets().add(stylesheet);

        return inputScene;
    }

    /**
     * createInputPane.
     * Construct the pane where host can give input about the game
     * for the inputScene
     * @param primaryStage starting stage
     * @param stylesheet current stylesheet
     * @return inputPane
     */
    private Pane createInputPane(final Stage primaryStage,
                                 final String stylesheet) {
        final int gapSize = 15;
        GridPane formPane = new GridPane();
        formPane.setAlignment(Pos.BASELINE_CENTER);
        formPane.setHgap(gapSize);
        formPane.setVgap(gapSize);

        Text description = new Text("Welcome to TrackScape! "
            + "Please set up the Escape Room parameters.");

        final int colspan = 3;
        final int rowspan = 3;
        final int rowIndex = 7;
        formPane.add(description, 0, 0, colspan, rowspan);
        description.setTextAlignment(TextAlignment.CENTER);

        Button submit = new Button("Proceed");
        submit.setOnAction(event -> getController().proceedToMonitorScene(
            monitorScene, primaryStage, stylesheet));
        formPane.add(submit, 1, rowIndex, colspan, rowspan);

        return addFieldsOfFormToFormPane(formPane);
    }

    /**
     * Method that adds form fields to the formpane.
     * @param formPane the pane where field should be added to.
     * @return the formpane with added fields
     */
    private GridPane addFieldsOfFormToFormPane(final GridPane formPane) {
        final int rowIndexRoom = 3;
        final int rowIndexPeople = 4;
        final int rowIndexChest = 5;

        // The fields of the form
        Label room = new Label("Name of Escape Room:");
        formPane.add(room, 0, rowIndexRoom);
        TextField roomTextField = new TextField();
        formPane.add(roomTextField, 1, rowIndexRoom);
        Label peopleNum = new Label("Number of Players:");
        formPane.add(peopleNum, 0, rowIndexPeople);
        TextField peopleTextField = new TextField();
        formPane.add(peopleTextField, 1, rowIndexPeople);
        Label chestNum = new Label("Number of Chests:");
        formPane.add(chestNum, 0, rowIndexChest);
        TextField chestTextField = new TextField();
        formPane.add(chestTextField, 1, rowIndexChest);

        return formPane;
    }

}
