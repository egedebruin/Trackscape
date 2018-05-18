package gui;

import java.io.File;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Main.
 * Class that constructs and builds the GUI
 */
public class Main extends Application {

    /**
     * Class parameters.
     */
    private File css = new File(System.getProperty("user.dir")
        + "\\src\\main\\java\\gui\\stylesheet.css");
    private String stylesheet = "file:///"
        + css.getAbsolutePath().replace("\\", "/");
    private MonitorScene monitorScene = new MonitorScene();
    private InputScene inputScene = new InputScene();

    /**
     * main.
     * Launch the application
     * @param args arguments
     */
    public static void main(final String[] args) {
        System.load(System.getProperty("user.dir")
            + File.separator + "libs"
            + File.separator + "opencv_ffmpeg341_64.dll");
        System.load(System.getProperty("user.dir")
            + File.separator + "libs"
            + File.separator + "opencv_java341.dll");

        launch(args);
    }

    /**
     * start.
     * Construct the structure of the GUI
     * @param primaryStage starting stage
     */
    @Override
    public void start(final Stage primaryStage) {
        // Create the inputScene and monitorScene
        final int width = 1250;
        final int height = 800;

        // Set the scene and show primaryStage
        primaryStage.setTitle("TrackScape");
        primaryStage.setScene(monitorScene
            .createMonitorScene(width, height, primaryStage, stylesheet));
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> System.exit(0));
    }

}
