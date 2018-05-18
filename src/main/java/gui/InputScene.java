package gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Class that constructs the inputScene.
 */
public class InputScene {

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
        root.setCenter(createInputPane());
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
     * @return inputPane
     */
    private Pane createInputPane() {
        BorderPane inputPane = new BorderPane();


        return inputPane;
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
