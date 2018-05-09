package gui;

import java.io.File;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

// Import necessary Java class from src code
// to call method to send video source to

/**
 * Main.
 * Class that constructs and builds the GUI
 */
public class Main extends Application {
    /**
     * videoMv.
     * Deals with the mediaPlayer
     */
    private MediaView videoMv = new MediaView();

    /**
     * start.
     * Constructs the structure of the GUI
     * @param primaryStage starting stage
     * @throws Exception when there are errors
     */
    @Override
    public void start(final Stage primaryStage) throws Exception {
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
        final int width = 1250;
        final int height = 800;
        Scene scene = new Scene(root, width, height);
        scene.getStylesheets().add("stylesheet.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * createVideoPane.
     * Creates the center pane for the root, containing menu and mediaPlayer
     * @param primaryStage starting stage
     * @return videoPane
     */
    private Pane createVideoPane(final Stage primaryStage) {
        BorderPane videoPane = new BorderPane();

        ArrayList<Pane> menuMediaPane =
                createMenuMediaPane(videoPane, primaryStage);

        videoPane.setTop(menuMediaPane.get(0));
        videoPane.setCenter(menuMediaPane.get(1));

        return videoPane;
    }

    /**
     * createMenuMediaPane.
     * Creates the Menu pane and mediaPlayer pane
     * @param videoPane pane that includes menu and mediaplayer
     * @param primaryStage starting stage
     * @return ArrayList
     */
    private ArrayList<Pane> createMenuMediaPane(final Pane videoPane,
                                                final Stage primaryStage) {
        // ------------- Create Menu -------------
        MenuBar menu = new MenuBar();
        menu.prefWidthProperty().bind(videoPane.widthProperty());

        // Main menu items
        Menu menuFile = new Menu("File");
        Menu data = new Menu("Overview of Statistics");
        // Menu options
        MenuItem openVideo = new MenuItem("Open File...");
        MenuItem connectStream = new MenuItem("Connect Stream...");
        // Add menu options to main menu items
        menuFile.getItems().addAll(openVideo, connectStream);
        menu.getMenus().addAll(menuFile, data);

        StackPane menuPane = new StackPane();
        menuPane.getChildren().add(menu);

        // ------------- Create MediaPlayer Pane -------------
        StackPane mediaPlayerPane = new StackPane();
        final int width = 1000;
        final int height = 600;
        mediaPlayerPane.getChildren()
                .addAll(new Rectangle(width, height, Color.BLACK), videoMv);

        // When video_open option is clicked
        openVideo(openVideo, primaryStage);

        // Add menuPane and mediaPlayerPane to list of panes
        ArrayList<Pane> menuMediaList = new ArrayList<>();
        menuMediaList.add(menuPane);
        menuMediaList.add(mediaPlayerPane);

        return menuMediaList;
    }

    /**
     * openVideo.
     * Opens the video the user selected in the mediaPlayer
     * @param openVideo menu item to open a video
     * @param primaryStage starting stage
     */
    private void openVideo(final MenuItem openVideo,
                           final Stage primaryStage) {
        openVideo.setOnAction(t -> {
            FileChooser chooser = new FileChooser();
            File file = chooser.showOpenDialog(primaryStage);
            if (file != null) {
                Media videoSource = new Media(file.toURI().toString());
                MediaPlayer mediaPlayer = new MediaPlayer(videoSource);
                videoMv.setMediaPlayer(mediaPlayer);
            }
        });
    }

    /**
     * createTitlePane.
     * Creates the top pane of the root
     * @return Pane
     */
    private Pane createTitlePane() {
        final int size = 80;

        FlowPane titlePane = new FlowPane();
        titlePane.setAlignment(Pos.TOP_CENTER);

        Text text = new Text("TrackScape");
        text.setFont(Font.font("Edwardian Script ITC", size));
        text.setFill(Color.BLACK);
        text.setStroke(Color.LIGHTSLATEGREY);
        text.setStrokeWidth(2);

        titlePane.getChildren().addAll(text);

        return titlePane;
    }

    /**
     * createBottomPane.
     * Creates the bottom pane of the root
     * @return Pane
     */
    private Pane createBottomPane() {
        final int size = 20;

        Text text2 = new Text("© TrackScape");
        text2.setFont(Font.font("Edwardian Script ITC", size));
        text2.setFill(Color.BLACK);
        text2.setStroke(Color.LIGHTSLATEGREY);
        text2.setStrokeWidth(1);

        FlowPane bottomPane = new FlowPane();
        bottomPane.getChildren().addAll(text2, createMediaBar());

        return bottomPane;
    }

    /**
     * createMediaBar.
     * Creates a mediaBar for the mediaPlayer
     * @return HBox
     */
    private HBox createMediaBar() {
        final int top = 5;
        final int right = 10;
        final int bottom = 5;
        final int left = 10;
        final int minWidth = 50;
        final int prefWidth = 130;

        // Create the bar in which the video can be controlled
        HBox mediaBar = new HBox();
        mediaBar.setAlignment(Pos.CENTER);
        mediaBar.setPadding(new Insets(top, right, bottom, left));

        // Create the play button
        final Button playButton = new Button(">");
        playButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent event) {
                MediaPlayer mp = videoMv.getMediaPlayer();
                MediaPlayer.Status status = mp.getStatus();

                if (status == MediaPlayer.Status.UNKNOWN
                        || status == MediaPlayer.Status.HALTED) {
                    return;
                }
                if (status == MediaPlayer.Status.PAUSED
                        || status == MediaPlayer.Status.READY
                        || status == MediaPlayer.Status.STOPPED) {
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
        Slider timeSlider = new Slider();

        mediaBar.getChildren()
                .addAll(playButton, spacer, timeLabel, timeSlider, playTime);

        playTime.setPrefWidth(prefWidth);
        playTime.setMinWidth(minWidth);
        HBox.setHgrow(timeSlider, Priority.ALWAYS);
        timeSlider.setMinWidth(minWidth);
        timeSlider.setMaxWidth(Double.MAX_VALUE);

        return mediaBar;
    }

    /**
     * main.
     * Launches the application
     * @param args arguments
     */
    public static void main(final String[] args) {
        launch(args);
    }

}
