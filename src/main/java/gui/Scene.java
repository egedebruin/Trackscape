package gui;

import javafx.geometry.Pos;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Abstract class for Scenes with repeated methods.
 */
public abstract class Scene {
    /**
     * Class parameters.
     */
    private Controller controller;

    /**
     * Constructor for Scene.
     * @param crtl the controller
     */
    public Scene(final Controller crtl) {
        this.controller = crtl;
    }

    /**
     * createTopPane.
     * Create the top pane of the root
     * @return topPane
     */
    protected Pane createTopPane() {
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
     * createBottomPane.
     * Create the bottom pane of the root
     * @return Pane
     */
    protected Pane createBottomPane() {
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

    /**
     * Get the controller.
     * @return controller
     */
    public Controller getController() {
        return controller;
    }

}
