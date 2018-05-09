package Gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;

// Import necessary Java class from src code to call method to send video source to

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("TrackScape");

        // Choose best-fitted pane for the application
        BorderPane root = new BorderPane();

        // Structure of panes inside the root pane
        root.setTop(createTitlePane());
        root.setCenter(createVideoPane(primaryStage));
        root.setBottom(createBottomPane());
        //root.setLeft(...);
        //root.setRight(...);

        // Show the scene
        Scene scene = new Scene(root,1250, 800);
        scene.getStylesheets().add("stylesheet.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Pane createVideoPane(Stage primaryStage) {
        BorderPane videoPane = new BorderPane();

        ArrayList<Pane> menuMediaPane = createMenuMediaPane(videoPane, primaryStage);

        videoPane.setTop(menuMediaPane.get(0));
        videoPane.setCenter(menuMediaPane.get(1));

        return videoPane;
    }

    private ArrayList<Pane> createMenuMediaPane(Pane videoPane, Stage primaryStage) {
        ArrayList<Pane> menuMediaList = new ArrayList<>();

        // ------------- Create Menu -------------
        MenuBar video_menuBar = new MenuBar();
        video_menuBar.prefWidthProperty().bind(videoPane.widthProperty());

        // Main menu items
        Menu video_menuFile = new Menu("File");
        Menu data = new Menu("Overview of Statistics");
        // Menu options
        MenuItem video_open = new MenuItem("Open File...");
        MenuItem joke = new MenuItem("Robins stukje");
        // Add menu options to main menu items
        video_menuFile.getItems().addAll(video_open, joke);
        video_menuBar.getMenus().addAll(video_menuFile, data);

        StackPane menuPane = new StackPane();
        menuPane.getChildren().add(video_menuBar);


        // ------------- Create MediaPlayer -------------
        MediaView video_mv = new MediaView();

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

        StackPane mediaPlayerPane = new StackPane();
        mediaPlayerPane.getChildren().addAll(new Rectangle(1000, 600, Color.BLACK), video_mv);

        // Add menuPane and mediaPlayerPane to the list of panes
        menuMediaList.add(menuPane);
        menuMediaList.add(mediaPlayerPane);

        return menuMediaList;
    }

    private Pane createTitlePane() {
        FlowPane titlePane = new FlowPane();
        titlePane.setAlignment(Pos.TOP_CENTER);

        Text text = new Text("TrackScape");
        text.setFont(Font.font("Edwardian Script ITC", 80));
        text.setFill(Color.BLACK);
        text.setStroke(Color.LIGHTSLATEGREY);
        text.setStrokeWidth(2);

        titlePane.getChildren().addAll(text);

        return titlePane;
    }

    private Pane createBottomPane() {
        FlowPane bottomPane = new FlowPane();

        Text text2 = new Text("© TrackScape");
        text2.setFont(Font.font("Edwardian Script ITC", 30));
        text2.setFill(Color.BLACK);
        text2.setStroke(Color.LIGHTSLATEGREY);
        text2.setStrokeWidth(1);

        bottomPane.getChildren().addAll(text2);

        return bottomPane;
    }

    public static void main(String[] args) {
        launch(args);
    }

}