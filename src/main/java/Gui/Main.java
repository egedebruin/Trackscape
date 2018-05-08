package Gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("TrackScape");
        BorderPane mainPane = new BorderPane();      // Choose best-fitted pane for the application

        // All panes
        FlowPane flowPane = new FlowPane();
        flowPane.setAlignment(Pos.TOP_CENTER);
        StackPane videoPane = new StackPane();
        FlowPane flowPane2 = new FlowPane();

        // Structure of panes inside main pane
        mainPane.setTop(flowPane);
        //mainPane.setLeft(...);
        mainPane.setCenter(videoPane);
        //mainPane.setRight(...);
        mainPane.setBottom(flowPane2);

        //-------------------- Top of the application (title)-------------
        Text text = new Text("TrackScape");
        text.setFont(Font.font("Edwardian Script ITC", 80));
        text.setFill(Color.BLACK);
        text.setStroke(Color.LIGHTSLATEGREY);
        text.setStrokeWidth(2);

        flowPane.getChildren().addAll(text);

        //-------------------- Center of the application (mediaplayer)-------------
        // Working on this atm!

        //-------------------- Bottom of the application (button)-------------
        Text text2 = new Text("© TrackScape");
        text2.setFont(Font.font("Edwardian Script ITC", 30));
        text2.setFill(Color.BLACK);
        text2.setStroke(Color.LIGHTSLATEGREY);
        text2.setStrokeWidth(1);
        flowPane2.getChildren().addAll(text2);

        // Show the scene
        Scene scene = new Scene(mainPane,1250, 800);
        scene.getStylesheets().add("stylesheet.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
