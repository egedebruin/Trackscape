package gui;

import gui.panes.VideoPane;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Class that constructs the monitorScene.
 */
public class MonitorScene extends BaseScene {
    /**
     * Class parameters.
     */
    private VideoPane videoPane = new VideoPane(getController());

    /**
     * Constructor.
     * @param controller the controller
     */
    public MonitorScene(final Controller controller) {
        super(controller);
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

        return monitorScene;
    }
}
