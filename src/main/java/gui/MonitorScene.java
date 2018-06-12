package gui;

import gui.controllers.MainController;
import gui.panes.VideoPane;
import javafx.animation.FadeTransition;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Class that constructs the monitorScene.
 */
public class MonitorScene extends BaseScene {
    /**
     * Class parameters.
     */
    private VideoPane videoPane = new VideoPane(getMainController());

    /**
     * Constructor.
     * @param mainController the mainController
     */
    public MonitorScene(final MainController mainController) {
        super(mainController);
    }

    /**
     * monitorScene.
     * Creates the scene where host can monitor the game.
     * @param primaryStage starting stage
     * @param stylesheet current stylesheet
     * @return monitorScene
     */
    public Scene createMonitorScene(final Stage primaryStage, final String stylesheet) {
        BorderPane root = new BorderPane();
        root.setTop(createTopPane());
        root.setCenter(videoPane.createVideoPane(primaryStage));
        root.setBottom(createBottomPane());

        Scene monitorScene = new Scene(root, primaryStage.getWidth(), primaryStage.getHeight());
        monitorScene.getStylesheets().add(stylesheet);

        final int fadeTime = 1000;
        FadeTransition ft = new FadeTransition(Duration.millis(fadeTime), root);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();

        return monitorScene;
    }
}
