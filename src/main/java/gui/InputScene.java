package gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * Class that constructs the inputScene.
 */
public class InputScene {
    /**
     * Class parameters.
     */
    private Controller controller;
    private MonitorScene monitorScene;

    /**
     * Constructor.
     * @param ctrl the controller
     */
    public InputScene(final Controller ctrl) {
        this.controller = ctrl;
        this.monitorScene = new MonitorScene(this.controller);
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
        root.setCenter(createInputPane(width, height,
            primaryStage, stylesheet));
        root.setBottom(createBottomPane());

        Scene inputScene = new Scene(root, width, height);
        inputScene.getStylesheets().add(stylesheet);

        return inputScene;
    }

    /**
     * createTopPane.
     * Create the top pane of the root
     * @return topPane
     */
    private Pane createTopPane() {
        final int size = 100;
        Text text = new Text("TrackScape");
        text.setFont(Font.font("Edwardian Script ITC", size));
        text.setFill(Color.BLACK);
        text.setStroke(Color.LIGHTSLATEGREY);
        text.setStrokeWidth(2);

        FlowPane topPane = new FlowPane();
        topPane.getChildren().addAll(text);
        topPane.setAlignment(Pos.CENTER);

        return topPane;
    }

    /**
     * createInputPane.
     * Construct the pane where host can give input about the game
     * for the inputScene
     * @param width of the scene
     * @param height of the scene
     * @param primaryStage starting stage
     * @param stylesheet current stylesheet
     * @return inputPane
     */
    private Pane createInputPane(final int width, final int height,
                                 final Stage primaryStage,
                                 final String stylesheet) {
        GridPane formPane = new GridPane();
        formPane.setAlignment(Pos.BASELINE_CENTER);
        formPane.setHgap(15);
        formPane.setVgap(15);

        Text description = new Text("Welcome to TrackScape! "
            + "Please set up the Escape Room parameters.");
        formPane.add(description, 0, 0, 3, 3);
        description.setTextAlignment(TextAlignment.CENTER);

        Button submit = new Button("Proceed");
        submit.setOnAction(event -> proceed(
            width, height, primaryStage, stylesheet));
        formPane.add(submit, 1, 7, 3, 3);

        // The fields of the form
        Label room = new Label("Name of Escape Room:");
        formPane.add(room, 0, 3);
        TextField roomTextField = new TextField();
        formPane.add(roomTextField, 1, 3);
        Label peopleNum = new Label("Number of Players:");
        formPane.add(peopleNum, 0, 4);
        TextField peopleTextField = new TextField();
        formPane.add(peopleTextField, 1, 4);
        Label chestNum = new Label("Number of Chests:");
        formPane.add(chestNum, 0, 5);
        TextField chestTextField = new TextField();
        formPane.add(chestTextField, 1, 5);

        return formPane;
    }

    /**
     * proceed.
     * Move on to the next stage
     * @param width of the scene
     * @param height of the scene
     * @param primaryStage starting stage
     * @param stylesheet current stylesheet
     */
    final void proceed(final int width, final int height,
                 final Stage primaryStage, final String stylesheet) {
        primaryStage.setScene(
            monitorScene.createMonitorScene(
                width, height, primaryStage, stylesheet));
    }

    /**
     * createBottomPane.
     * Create the bottom pane of the root
     * @return Pane
     */
    private Pane createBottomPane() {
        final int size = 15;

        Text text2 = new Text("© TrackScape");
        text2.setFont(Font.font("Verdana", size));
        text2.setFill(Color.BLACK);
        text2.setStroke(Color.LIGHTSLATEGREY);
        text2.setStrokeWidth(1);

        FlowPane bottomPane = new FlowPane();
        bottomPane.getChildren().addAll(text2);

        return bottomPane;
    }

}
