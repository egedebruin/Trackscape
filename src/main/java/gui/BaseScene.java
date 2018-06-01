package gui;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.File;

/**
 * Abstract class for Scenes with repeated methods.
 */
public abstract class BaseScene {
    /**
     * Class parameters.
     */
    private Controller controller;

    /**
     * Constructor for BaseScene.
     * @param crtl the controller
     */
    public BaseScene(final Controller crtl) {
        this.controller = crtl;
    }

    /**
     * createTopPane.
     * Create the top pane of the root
     * @return topPane
     */
    protected Pane createTopPane() {
        final int width = 140;
        File streamEnd = new File(System.getProperty("user.dir")
            + "\\src\\main\\java\\gui\\images\\trackscape.png");
        Image trackscape = new Image(streamEnd.toURI().toString());
        ImageView logo = new ImageView();
        logo.setFitWidth(width);
        logo.setPreserveRatio(true);
        logo.setImage(trackscape);
        FlowPane logoPane = new FlowPane();
        logoPane.getChildren().add(logo);
        logoPane.setAlignment(Pos.CENTER);

        logoPane.getStyleClass().add("top");

        return logoPane;
    }

    /**
     * createBottomPane.
     * Create the bottom pane of the root
     * @return Pane
     */
    protected Pane createBottomPane() {
        final int size = 15;

        Text text = new Text("TrackScape");
        text.setFont(Font.font("Verdana", size));
        text.setFill(Color.BLACK);
        text.setStroke(Color.LIGHTSLATEGREY);
        text.setStrokeWidth(1);

        FlowPane bottomPane = new FlowPane();
        bottomPane.getChildren().addAll(text);

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
