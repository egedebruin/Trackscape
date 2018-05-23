package gui;

import java.io.File;
import java.util.ArrayList;
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
 * Class that constructs the monitorScene.
 */
public class MonitorScene {
    /**
     * Class parameters.
     */
    private ImageView imageView = new ImageView();
    private String theStreamString = "rtsp://192.168.0.117:554/"
        + "user=admin&password=&channel=1&stream=1"
        + ".sdp?real_stream--rtp-caching=100";
    private Controller controller;

    /**
     * Constructor.
     * @param crtl the controller
     */
    public MonitorScene(final Controller crtl) {
        this.controller = crtl;
    }

    /**
     * monitorScene.
     * Creates the scene where host can monitor the game.
     * @param width of the scene
     * @param height of the scene
     * @param primaryStage starting stage
     * @param stylesheet current stylesheet
     * @return monitorScene
     */
    public Scene createMonitorScene(final int width,
                                    final int height,
                                    final Stage primaryStage,
                                    final String stylesheet) {
        BorderPane root = new BorderPane();
        root.setTop(createTitlePane());
        root.setCenter(createVideoPane(primaryStage));
        root.setBottom(createBottomPane());

        Scene monitorScene = new Scene(root, width, height);
        monitorScene.getStylesheets().add(stylesheet);

        return monitorScene;
    }

    /**
     * createTitlePane.
     * Create the top pane of the root
     * @return Pane
     */
    private Pane createTitlePane() {
        final int size = 100;

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
        final int size = 15;

        Text text2 = new Text("© TrackScape");
        text2.setFont(Font.font("Verdana", size));
        text2.setFill(Color.BLACK);
        text2.setStroke(Color.LIGHTSLATEGREY);
        text2.setStrokeWidth(1);

        FlowPane bottomPane = new FlowPane();
        bottomPane.getChildren().addAll(text2);

        return bottomPane;
    }

    /**
     * createVideoPane.
     * Create the center pane for the root, containing menu,
     * mediaPlayer, and mediaBar for the monitorScene
     * @param primaryStage starting stage
     * @return videoPane
     */
    private Pane createVideoPane(final Stage primaryStage) {
        BorderPane videoPane = new BorderPane();

        ArrayList<Pane> menuMediaPane =
            createMenuMediaPane(videoPane, primaryStage);

        // gets the menubar and puts it at the top of the videoPane
        videoPane.setTop(menuMediaPane.get(0));
        // gets the imageView (location where videos are shown)
        // and puts it in the center of the videoPane
        videoPane.setCenter(menuMediaPane.get(1));
        videoPane.setBottom(createMediaBar());

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
        MenuItem theStream = new MenuItem("THE Stream");
        // Add menu options to main menu items
        menuFile.getItems().addAll(openVideo, connectStream, theStream);
        menu.getMenus().addAll(menuFile, data);

        StackPane menuPane = new StackPane();
        menuPane.getChildren().add(menu);

        // ------------- Create MediaPlayer Pane -------------
        StackPane mediaPlayerPane = new StackPane();

        mediaPlayerPane.getChildren().addAll(imageView);
        File streamEnd = new File(System.getProperty("user.dir")
            + "\\src\\main\\java\\gui\\images\\black.png");
        Image black = new Image(streamEnd.toURI().toString());
        imageView.setImage(black);

        // When menu options are clicked
        openVideo(openVideo, primaryStage);
        connectStream(connectStream, primaryStage);
        theStream(theStream);

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
                controller.createVideo(file);
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
            submit.setOnAction(t1 -> controller
                .createStream(streamStage, field));

            // Save the url of the RTSP stream by pressing on the enter key
            field.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.ENTER)  {
                    controller.createStream(streamStage, field);
                }
            });

            final int popUpWidth = 500;
            final int popUpHeight = 100;

            Scene popUp = new Scene(popUpVBox, popUpWidth, popUpHeight);
            streamStage.setScene(popUp);
            streamStage.show();
        });
    }

    /**
     * theStream.
     * Enables easy access to our  stream
     * @param theStream menuItem
     */
    private void theStream(final MenuItem theStream) {
        theStream.setOnAction((ActionEvent t)
            -> controller.createTheStream(theStreamString));
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

        // Create mediabar for video options
        HBox mediaBar = new HBox();
        mediaBar.setAlignment(Pos.CENTER);
        mediaBar.setPadding(new Insets(top, right, bottom, left));

        // Create the play/pauze button
        final Button playButton = new Button(">");
        playButton.setOnAction(event -> controller.grabTimeFrame(imageView));

        final Button closeStream = new Button("Close Stream");
        closeStream.setOnAction(event -> controller.closeStream(imageView));

        mediaBar.getChildren().addAll(playButton, closeStream);

        return mediaBar;
    }

}