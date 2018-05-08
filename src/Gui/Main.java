package Gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
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
import javafx.stage.Stage;
import javafx.stage.FileChooser;

import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("TrackScape");
        BorderPane mainPane = new BorderPane();      // Choose best-fitted pane for the application

        // All panes
        FlowPane titlePane = new FlowPane();
        titlePane.setAlignment(Pos.TOP_CENTER);
        BorderPane videoPane = new BorderPane();
        FlowPane bottomPane = new FlowPane();
        StackPane stack = new StackPane();

        // Structure of panes inside main pane
        mainPane.setTop(titlePane);
        //mainPane.setLeft(...);
        mainPane.setCenter(videoPane);
        //mainPane.setRight(...);
        mainPane.setBottom(bottomPane);

        //-------------------- Top of the application (title)-------------
        Text text = new Text("TrackScape");
        text.setFont(Font.font("Edwardian Script ITC", 80));
        text.setFill(Color.BLACK);
        text.setStroke(Color.LIGHTSLATEGREY);
        text.setStrokeWidth(2);

        titlePane.getChildren().addAll(text);

        //-------------------- Center of the application (mediaplayer)-------------

        // The Menu
        MenuBar video_menuBar = new MenuBar();
        Menu video_menuFile = new Menu("File");
        Menu data = new Menu("Overview of Statistics");
        MenuItem video_open = new MenuItem("Open File...");
        MenuItem joke = new MenuItem("Robins stukje");
        video_menuFile.getItems().addAll(video_open, joke);
        video_menuBar.getMenus().addAll(video_menuFile, data);
        video_menuBar.prefWidthProperty().bind(videoPane.widthProperty());

        StackPane menu = new StackPane();
        menu.getChildren().add(video_menuBar);
        videoPane.setTop(menu);

        // The Mediaplayer
        MediaView video_mv = new MediaView();
        stack.getChildren().addAll(new Rectangle(1000, 600, Color.BLACK), video_mv);
        video_open.setOnAction(t -> {
            FileChooser chooser = new FileChooser();
            File file = chooser.showOpenDialog(primaryStage);
            if (file != null) {
                Media video_source = new Media(file.toURI().toString());
                MediaPlayer video_mp = new MediaPlayer(video_source);
                video_mv.setMediaPlayer(video_mp);
                video_mp.play();
            }
        });
        videoPane.setBottom(stack);

        //-------------------- Bottom of the application (button)-------------
        Text text2 = new Text("© TrackScape");
        text2.setFont(Font.font("Edwardian Script ITC", 30));
        text2.setFill(Color.BLACK);
        text2.setStroke(Color.LIGHTSLATEGREY);
        text2.setStrokeWidth(1);
        bottomPane.getChildren().addAll(text2);


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
