package gui;

import handlers.JsonHandler;
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
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

/**
 * Class that constructs the monitorScene.
 */
public class MonitorScene extends BaseScene {
    /**
     * Class parameters.
     */
    private ArrayList<ImageView> imageViews = new ArrayList<>();
    private String theStreamString = "rtsp://192.168.0.117:554/"
        + "user=admin&password=&channel=1&stream=1"
        + ".sdp?real_stream--rtp-caching=100";
    private FlowPane mediaPlayerPane = new FlowPane();
    private int cameras = 0;
    private int people = 0;
    private int chests = 0;

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
        root.setCenter(createVideoPane(primaryStage));
        root.setBottom(createBottomPane());

        Scene monitorScene = new Scene(root, primaryStage.getWidth(), primaryStage.getHeight());
        monitorScene.getStylesheets().add(stylesheet);

        return monitorScene;
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
            createMenuAndMediaPane(videoPane, primaryStage);

        // get the menubar and put it at the top of the videoPane
        videoPane.setTop(menuMediaPane.get(0));
        // get the imageView (location where videos are shown)
        // and put it in the center of the videoPane
        videoPane.setCenter(menuMediaPane.get(1));

        // create the mediabar and put it at the bottom of the videoPane
        videoPane.setBottom(createMediaBar());

        return videoPane;
    }

    /**
     * createMenuAndMediaPane.
     * Create the Menu pane and mediaPlayer pane
     * @param videoPane pane that includes menu and mediaPlayer
     * @param primaryStage starting stage
     * @return ArrayList
     */
    private ArrayList<Pane> createMenuAndMediaPane(final Pane videoPane,
                                                   final Stage primaryStage) {
        // Add menuPane and mediaPlayerPane to a list
        ArrayList<Pane> menuMediaList = new ArrayList<>();
        menuMediaList.add(createMenuPane(videoPane, primaryStage));
        menuMediaList.add(createImageViewerPane());

        return menuMediaList;
    }

    /**
     * Create the pane that holds all imageViewers.
     * @return mediaPlayerPane
     */
    private Pane createImageViewerPane() {
        final int gap = 3;
        final int inset = 5;
        mediaPlayerPane.setPadding(new Insets(0, inset, inset, inset));
        mediaPlayerPane.setVgap(gap);
        mediaPlayerPane.setHgap(gap);
        mediaPlayerPane.setAlignment(Pos.CENTER);
        initializeImageViewers();
        mediaPlayerPane.getChildren().addAll(imageViews);

        return mediaPlayerPane;
    }

    /**
     * Construct the menu for the application.
     * @param videoPane the pane that will hold the menu
     * @param primaryStage starting stage
     * @return menuPane
     */
    private Pane createMenuPane(final Pane videoPane, final Stage primaryStage) {
        // Menu options for settings
        Menu settings = new Menu("Settings");
        MenuItem clearImageViewers = new MenuItem("Reset application");
        MenuItem closeApp = new MenuItem("Close application");
        settings.getItems().addAll(clearImageViewers, closeApp);

        // Menu options for automatic configuration
        Menu config = new Menu("Configure the Escape Room");
        MenuItem configFile = new MenuItem("Load configuration file...");
        Menu configManual = new Menu("Manual configuration...");
        MenuItem standardFile = new MenuItem("Use standard configuration");
        config.getItems().addAll(configFile, configManual, standardFile);

        // Menu options for manual configuration
        Menu cameraSettings = new Menu("Add camera");
        MenuItem openVideo = new MenuItem("Open File...");
        MenuItem connectStream = new MenuItem("Connect Stream...");
        MenuItem theStream = new MenuItem("THE Stream");
        cameraSettings.getItems().addAll(openVideo, connectStream, theStream);
        configManual.getItems().addAll(cameraSettings);

        // Add al submenus to main menu bar
        MenuBar menu = new MenuBar();
        menu.prefWidthProperty().bind(videoPane.widthProperty());
        menu.getMenus().addAll(settings, config);
        StackPane menuPane = new StackPane();
        menuPane.getChildren().add(menu);

        // When menu options are clicked
        resetCameras(clearImageViewers);
        closeApp(closeApp);
        openConfig(configFile, primaryStage);
        standardConfig(standardFile);
        openVideo(openVideo, primaryStage);
        connectStream(connectStream, primaryStage);
        theStream(theStream);

        return menuPane;
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
        playButton.setOnAction(event -> getController().grabTimeFrame(imageViews));

        final Button closeStream = new Button("Close Stream");
        closeStream.setOnAction(event -> getController().closeStream(imageViews));

        mediaBar.getChildren().addAll(playButton, closeStream);

        return mediaBar;
    }

    /**
     * Initializes the imageViews with a black image.
     */
    private void initializeImageViewers() {
        mediaPlayerPane.getChildren().clear();

        imageViews.clear();
        for (int k = 0; k < cameras; k++) {
            imageViews.add(new ImageView());
        }

        File streamEnd = new File(System.getProperty("user.dir")
            + "\\src\\main\\java\\gui\\images\\black.png");
        Image black = new Image(streamEnd.toURI().toString());

        final int height = 300;
        for (int i = 0; i < imageViews.size(); i++) {
            imageViews.get(i).setImage(black);
            imageViews.get(i).setFitHeight(height);
            imageViews.get(i).setPreserveRatio(true);
            imageViews.get(i).setSmooth(true);
            imageViews.get(i).setCache(false);
        }
    }

    /**
     * Remove all current cameras.
     * @param clearImageViewers the current imageViewers
     */
    private void resetCameras(final MenuItem clearImageViewers) {
        clearImageViewers.setOnAction(event -> getController().clearCameras());
        for (ImageView iv : imageViews) {
            iv.setImage(null);
        }
    }

    /**
     * Shut down application.
     * @param closeApp menu item
     */
    private void closeApp(final MenuItem closeApp) {
        closeApp.setOnAction(event -> System.exit(0));
    }

    /**
     * Let user select a configuration file.
     * @param configFile the file
     * @param primaryStage the starting stage
     */
    private void openConfig(final MenuItem configFile, final Stage primaryStage) {
        configFile.setOnAction(event -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Select Configuration File (JSon format)");
            File file = chooser.showOpenDialog(primaryStage);
            JsonHandler jsonHandler = new JsonHandler(file.toString());
            configure(jsonHandler);
        });
    }

    /**
     * Configure the standard file.
     * @param standardFile a provided standard file
     */
    private void standardConfig(final MenuItem standardFile) {
        standardFile.setOnAction(event -> {
            JsonHandler jsonHandler = new JsonHandler("files/standard.json");
            configure(jsonHandler);
        });
    }

    /**
     * Load the configuration file.
     * @param jsonHandler the current jsonHandler
     */
    private void configure(final JsonHandler jsonHandler) {
        chests = jsonHandler.getAmountChests(0);
        people = jsonHandler.getAmountPeople(0);
        cameras = jsonHandler.getCameraLinks(0).size();

        for (int k = 0; k < jsonHandler.getCameraLinks(0).size(); k++) {
            getController().createCamera(jsonHandler.getCameraLinks(0).get(k));
        }

        // Display the corresponding imageViewers
        mediaPlayerPane.getChildren().removeAll();
        initializeImageViewers();
        mediaPlayerPane.getChildren().addAll(imageViews);
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
                getController().createVideo(file);
                cameras++;
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
            submit.setOnAction(t1 -> getController()
                .createStream(streamStage, field));

            // Save the url of the RTSP stream by pressing on the enter key
            field.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.ENTER)  {
                    getController().createStream(streamStage, field);
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
            -> getController().createTheStream(theStreamString));
    }

}
