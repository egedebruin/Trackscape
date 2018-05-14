package gui;

import handlers.CameraHandler;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Main.
 * Class that constructs and builds the GUI
 */
public class Main extends Application {

    /**
     * Class variables.
     */
    private boolean validVid = false;
    private final int timeframe = 300;
    private int counter = 0;
    private ImageView imageView = new ImageView();
    private Timeline timeline = new Timeline(
        new KeyFrame(Duration.millis(timeframe), e -> showFrame())
    );
    private CameraHandler cameraHandler = new CameraHandler();

    /**
     * start.
     * Construct the structure of the GUI
     * @param primaryStage starting stage
     */
    @Override
    public void start(final Stage primaryStage) throws IOException {
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
        File css = new File(System.getProperty("user.dir")
            + "\\src\\main\\java\\gui\\stylesheet.css");
        scene.getStylesheets().add("file:///"
            + css.getAbsolutePath().replace("\\", "/"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * createVideoPane.
     * Create the center pane for the root, containing menu and mediaPlayer
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
     * Create the Menu pane and mediaPlayer pane
     * @param videoPane pane that includes menu and mediaPlayer
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
        final int width = 300;
        final int height = 300;

        mediaPlayerPane.getChildren()
                .addAll(new Rectangle(width, height, Color.BLACK), imageView);

        // When menu options are clicked
        openVideo(openVideo, primaryStage);
        connectStream(connectStream, primaryStage);

        // Add menuPane and mediaPlayerPane to list of panes
        ArrayList<Pane> menuMediaList = new ArrayList<>();
        menuMediaList.add(menuPane);
        menuMediaList.add(mediaPlayerPane);

        return menuMediaList;
    }

    /**
     * openVideo.
     * Get video file url from user
     * @param openVideo menu item to open a video
     * @param primaryStage starting stage
     */
    private void openVideo(final MenuItem openVideo,
                           final Stage primaryStage) {
        openVideo.setOnAction(t -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Select Video File");
            File file = chooser.showOpenDialog(primaryStage);
            if (file != null) {
                String fileUrl = file.toURI().toString();
                validVid = true;
                cameraHandler.addCamera(fileUrl);
            }
        });
    }

    /**
     * connectStream.
     * Get url of the stream from user
     * @param connectStream menuOption
     * @param primaryStage starting stage
     */
    private void connectStream(
        final MenuItem connectStream, final Stage primaryStage) {
        connectStream.setOnAction(t -> {
            // Set up pop up window
            final Stage streamStage = new Stage();
            streamStage.initModality(Modality.APPLICATION_MODAL);
            streamStage.initOwner(primaryStage);

            // Set up layout of the pop up window
            final Label fieldLabel = new Label("Enter url of the RTSP stream:");
            final TextField field = new TextField();
            Button submit = new Button("Submit");

            final int spacing = 6;
            final int insetPositions = 10;

            // Set up box in pop up window
            VBox popUpVBox = new VBox();
            popUpVBox.setPadding(new Insets(insetPositions,
                insetPositions, insetPositions, insetPositions));
            popUpVBox.getChildren().addAll(fieldLabel, field, submit);
            popUpVBox.setSpacing(spacing);
            popUpVBox.setAlignment(Pos.CENTER);

            // Save the url of the RTSP stream by clicking on submit
            submit.setOnAction(t1 -> {
                String streamUrl = field.getText();
                streamStage.close();

                cameraHandler.getNewFrame(cameraHandler.getCameraList().get(0));
                validVid = true;
            });

            // Save the url of the RTSP stream by pressing on the enter key
            field.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.ENTER)  {
                    String streamUrl = field.getText();
                    streamStage.close();

                    cameraHandler
                        .getNewFrame(cameraHandler.getCameraList().get(0));
                    validVid = true;
                }
            });

            final int popUpWidth = 500;
            final int popUpHeight = 100;

            Scene popUp = new Scene(popUpVBox, popUpWidth, popUpHeight);
            streamStage.setScene(popUp);
            streamStage.show();

            askFrame();
        });
    }

    /**
     * askFrame.
     * Ask for new frame every time unit
     */
    private void askFrame() {
        final int cycles = 100;
        timeline.setCycleCount(cycles);
        timeline.play();
    }

    /**
     * showFrame.
     * Retrieve current frame and show in ImageView
     */
    private void showFrame() {
        System.out.println("Framenumber: " + counter);
        counter = counter + 1;

        final int width = 750;
        Image currentFrame = retrieveFrame();
        imageView.setImage(currentFrame);
        imageView.setFitWidth(width);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);
    }

    /**
     * retrieveFrame.
     * Retrieve last frame from video reader in handlers.CameraHandler
     * @return Image
     */
    private Image retrieveFrame() {
        final int width = 750;
        final int height = 500;
        BufferedImage bufferedFrame =
            cameraHandler.getNewFrame(cameraHandler.getCameraList().get(0));
        Image frame = SwingFXUtils.toFXImage(bufferedFrame, null);
        return frame;
    }

    /**
     * createTitlePane.
     * Create the top pane of the root
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
     * Create the bottom pane of the root
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
     * Create a mediaBar for the mediaPlayer
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

        // Create the play/pauze button
        final Button playButton = new Button(">");
        playButton.setOnAction(event -> {
            if (validVid) {
                if (timeline.getStatus().toString() != "RUNNING") {
                    askFrame();
                } else {
                    timeline.pause();
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

}
