package gui;

import javafx.application.Application;
import javafx.stage.Stage;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.File;

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
    private Controller controller = new Controller();
    private MonitorScene monitorScene = new MonitorScene(controller);

    /**
     * main.
     * Launch the application
     *
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
     *
     * @param primaryStage starting stage
     */
    @Override
    public void start(final Stage primaryStage) {
        GraphicsDevice gd = GraphicsEnvironment
            .getLocalGraphicsEnvironment().getDefaultScreenDevice();
        final int width = gd.getDisplayMode().getWidth();
        final int height = gd.getDisplayMode().getHeight();
        primaryStage.setWidth(width);
        primaryStage.setHeight(height);

        // Set the scene and show primaryStage
        primaryStage.setTitle("TrackScape");
        primaryStage.setScene(monitorScene.createMonitorScene(primaryStage, stylesheet));
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> System.exit(0));
    }
}
