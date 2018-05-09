package Gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;

// Import necessary Java class from src code to call method to send video source to

/**
 * Main
 * Class that constructs and builds the GUI
 */
public class Main extends Application {
    private MediaView video_mv = new MediaView();

    /**
     * start
     * Constructs the structure of the GUI
     * @param primaryStage
     * @throws Exception
     */
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

    /**
     * createVideoPane
     * Creates the center pane for the root, containing menu and mediaPlayer
     * @param primaryStage
     * @return videoPane
     */
    private Pane createVideoPane(Stage primaryStage) {
        BorderPane videoPane = new BorderPane();

        ArrayList<Pane> menuMediaPane = createMenuMediaPane(videoPane, primaryStage);

        videoPane.setTop(menuMediaPane.get(0));
        videoPane.setCenter(menuMediaPane.get(1));

        return videoPane;
    }

    /**
     * createMenuMediaPane
     * Creates the Menu pane and mediaPlayer pane
     * @param videoPane
     * @param primaryStage
     * @return ArrayList<Pane>
     */
    private ArrayList<Pane> createMenuMediaPane(Pane videoPane, Stage primaryStage) {
        ArrayList<Pane> menuMediaList = new ArrayList<>();

        // ------------- Create Menu -------------
        MenuBar video_menuBar = new MenuBar();
        video_menuBar.prefWidthProperty().bind(videoPane.widthProperty());

        // Main menu items
        Menu video_menuFile = new Menu("File");
        Menu data = new Menu("Overview of Statistics");
        // Menu options
        MenuItem open_video = new MenuItem("Open File...");
        MenuItem connect_stream = new MenuItem("Connect Stream...");
        // Add menu options to main menu items
        video_menuFile.getItems().addAll(open_video, connect_stream);
        video_menuBar.getMenus().addAll(video_menuFile, data);

        StackPane menuPane = new StackPane();
        menuPane.getChildren().add(video_menuBar);

        // ------------- Create MediaPlayer Pane -------------
        StackPane mediaPlayerPane = new StackPane();
        mediaPlayerPane.getChildren().addAll(new Rectangle(1000, 600, Color.BLACK), video_mv);

        // When video_open option is clicked
        openVideo(open_video, primaryStage);

        // Add menuPane and mediaPlayerPane to the list of panes
        menuMediaList.add(menuPane);
        menuMediaList.add(mediaPlayerPane);

        return menuMediaList;
    }

    /**
     * openVideo
     * Opens the video the user selected in the mediaPlayer
     * @param open_video
     * @param primaryStage
     */
    private void openVideo(MenuItem open_video, Stage primaryStage) {
        open_video.setOnAction(t -> {
            FileChooser chooser = new FileChooser();
            File file = chooser.showOpenDialog(primaryStage);
            if (file != null) {
                Media video_source = new Media(file.toURI().toString());
                MediaPlayer video_mp = new MediaPlayer(video_source);
                video_mv.setMediaPlayer(video_mp);
            }
        });
    }

    /**
     * createTitlePane
     * Creates the top pane of the root
     * @return Pane
     */
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

    /**
     * createBottomPane
     * Creates the bottom pane of the root
     * @return Pane
     */
    private Pane createBottomPane() {
        FlowPane bottomPane = new FlowPane();

        Text text2 = new Text("© TrackScape");
        text2.setFont(Font.font("Edwardian Script ITC", 30));
        text2.setFill(Color.BLACK);
        text2.setStroke(Color.LIGHTSLATEGREY);
        text2.setStrokeWidth(1);

        bottomPane.getChildren().addAll(text2, createMediaBar());

        return bottomPane;
    }

    /**
     * createMediaBar()
     * Creates a mediaBar for the mediaPlayer
     * @return HBox
     */
    private HBox createMediaBar() {
        // Create the bar in which the video can be controlled (play, pause, scroll etc.)
        HBox mediaBar = new HBox();
        mediaBar.setAlignment(Pos.CENTER);
        mediaBar.setPadding(new Insets(5, 10, 5, 10));

        // Create the play button
        final Button playButton = new Button(">");
        playButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MediaPlayer mp = video_mv.getMediaPlayer();
                MediaPlayer.Status status = mp.getStatus();

                if(status == MediaPlayer.Status.UNKNOWN || status == MediaPlayer.Status.HALTED){
                    return;
                }
                if(status == MediaPlayer.Status.PAUSED || status == MediaPlayer.Status.READY || status == MediaPlayer.Status.STOPPED){
                    mp.play();
                } else {
                    mp.pause();
                }
            }
        });

        // Add labels and slider to the mediabar
        Label spacer = new Label("   ");
        Label timeLabel = new Label("Time: ");
        Label playTime = new Label();
        playTime.setPrefWidth(130);
        playTime.setMinWidth(50);
        Slider timeSlider = new Slider();
        HBox.setHgrow(timeSlider, Priority.ALWAYS);
        timeSlider.setMinWidth(50);
        timeSlider.setMaxWidth(Double.MAX_VALUE);

        mediaBar.getChildren().addAll(playButton, spacer, timeLabel, timeSlider, playTime);

        return mediaBar;
    }

    /**
     * main
     * Launches the application
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

}