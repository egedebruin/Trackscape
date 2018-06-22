package gui.panes;

import gui.controllers.TimerManager;
import gui.controllers.RoomController;
import gui.controllers.TimeLogController;
import gui.controllers.VideoController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;

/**
 * Class that creates the MenuPane for the VideoPane.
 */
public class MenuPane {
    private MediaPane mediaPane;
    private ManualConfigPane manualConfigPane;
    private Label cameraStatus;
    private TimeLogController timeLogController;
    private VideoController videoController;
    private TimerManager timerManager;
    private RoomController roomController;
    private static SimpleObjectProperty<File> lastKnownDirectoryProperty
            = new SimpleObjectProperty<>();

    /**
     * Constructor for menuPane.
     * @param roomControl the roomController
     * @param timerManagerControl the timerManager
     * @param timeLogControl the timeLogController
     * @param videoControl the videoController
     * @param pane the mediaPane
     */
    public MenuPane(final RoomController roomControl, final TimerManager timerManagerControl,
                    final TimeLogController timeLogControl, final VideoController videoControl,
                    final MediaPane pane) {
        this.mediaPane = pane;
        this.manualConfigPane = new ManualConfigPane(videoControl, roomControl);
        roomController = roomControl;
        timerManager = timerManagerControl;
        timeLogController = timeLogControl;
        videoController = videoControl;
    }

    /**
     * Construct the menu for the application.
     * @param videoPane the pane that will hold the menu
     * @param primaryStage starting stage
     * @return menuPane
     */
    public Pane createMenuPane(final Pane videoPane, final Stage primaryStage) {
        // Menu options for settings
        Menu settings = createSettings();

        // Menu options for automatic configuration
        Menu config = createConfig(primaryStage);

        // Menu options for adding extra media
        Menu extraMedia = createExtraMedia(primaryStage);

        // Add al submenus to main menu bar
        MenuBar menu = new MenuBar();
        menu.prefWidthProperty().bind(videoPane.widthProperty());
        menu.getMenus().addAll(settings, config, extraMedia);
        StackPane menuPane = new StackPane();
        menuPane.getChildren().add(menu);

        return menuPane;
    }

    /**
     * Creates the settings menu option for the menu pane.
     * @return the settings Menu
     */
    private Menu createSettings() {
        Menu settings = new Menu("Settings");

        MenuItem clearImageViewers = new MenuItem("Reset Application");
        resetCameras(clearImageViewers);

        MenuItem closeApp = new MenuItem("Close Application");
        closeApp(closeApp);

        settings.getItems().addAll(clearImageViewers, closeApp);
        return settings;
    }

    /**
     * Creates the config menu option for the menu pane.
     * @param primaryStage the primary stage
     * @return the config menu
     */
    private Menu createConfig(final Stage primaryStage) {
        Menu config = new Menu("Configure the Escape Room");

        MenuItem configFile = new MenuItem("Load Configuration File...");
        openConfig(configFile, primaryStage);

        MenuItem manual = new MenuItem("Manual Configuration");
        manualConfigPane.createManualConfig(manual);

        MenuItem standardFile = new MenuItem("Use Standard Configuration");
        standardConfig(standardFile);

        config.getItems().addAll(configFile, manual, standardFile);
        return config;
    }

    /**
     * Creates the add extra media menu option in the menu pane.
     * @param primaryStage the primary stage
     * @return the add extra media menu
     */
    private Menu createExtraMedia(final Stage primaryStage) {
        Menu extraMedia = new Menu("Add Video Source");

        MenuItem openVideo = new MenuItem("Add Video File...");
        openVideo(openVideo, primaryStage);

        MenuItem connectStream = new MenuItem("Add Stream...");
        connectStream(connectStream, primaryStage);

        extraMedia.getItems().addAll(openVideo, connectStream);
        return extraMedia;
    }

    /**
     * Remove all current cameras.
     * @param clearImageViewers the current imageViewers
     */
    private void resetCameras(final MenuItem clearImageViewers) {
        clearImageViewers.setOnAction(event -> {
            timeLogController.clearInformationArea();
            endStream();
        });
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
            if (videoController.isClosed() && !roomController.isConfigured()) {
                FileChooser chooser = new FileChooser();
                chooser.setTitle("Select Configuration File (JSon format)");
                chooser.initialDirectoryProperty().bindBidirectional(lastKnownDirectoryProperty);
                File file = chooser.showOpenDialog(primaryStage);
                if (file != null) {
                    roomController.configure(file.toString());
                    lastKnownDirectoryProperty.setValue(file.getParentFile());
                }
                setCameraStatus();

            }
        });
    }

    /**
     * Configure the standard file.
     * @param standardFile a provided standard file
     */
    private void standardConfig(final MenuItem standardFile) {
        standardFile.setOnAction(event -> {
            if (videoController.isClosed() && !roomController.isConfigured()) {
                roomController.configure("files/standard.json");
                setCameraStatus();
            }
        });
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
            if (videoController.isClosed()) {
                FileChooser chooser = new FileChooser();
                chooser.setTitle("Select Video File");
                chooser.initialDirectoryProperty().bindBidirectional(lastKnownDirectoryProperty);
                File file = chooser.showOpenDialog(primaryStage);
                if (file != null) {
                    videoController.createVideo(file);
                    setCameraStatus();
                    lastKnownDirectoryProperty.setValue(file.getParentFile());
                }
            }
        });
    }

    /**
     * connectStream.
     * Get url of the stream from user
     * @param connectStream menuOption
     * @param primaryStage starting stage
     */
    private void connectStream(final MenuItem connectStream, final Stage primaryStage) {
        final int popUpWidth = 500;
        final int popUpHeight = 100;

        connectStream.setOnAction(t -> {
            if (videoController.isClosed()) {
                // Set up pop up window
                final Stage streamStage = new Stage();
                streamStage.initModality(Modality.APPLICATION_MODAL);
                streamStage.initOwner(primaryStage);

                Scene popUp = new Scene(createPopUpWindow(streamStage), popUpWidth, popUpHeight);
                streamStage.setScene(popUp);
                streamStage.show();
            }
        });
    }

    /**
     * Create the pop-up window that asks for stream input.
     * @param streamStage the new stage for the pop-up window
     * @return VBox with the pop-up
     */
    private VBox createPopUpWindow(final Stage streamStage) {
        // Set up layout of the pop up window
        final Label fieldLabel = new Label("Enter url of the RTSP stream:");
        final TextField field = new TextField();
        Button submit = new Button("Submit");

        // Save the url of the RTSP stream by pressing on the enter key
        field.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                videoController.createStream(streamStage, field);
                setCameraStatus();
            }
        });

        // Save the url of the RTSP stream by clicking on submit
        submit.setOnAction(t1 -> {
            videoController.createStream(streamStage, field);
            setCameraStatus();
        });

        // Set up box in pop up window
        VBox popUpVBox = constructPopUpVBox();
        popUpVBox.getChildren().addAll(fieldLabel, field, submit);

        return popUpVBox;
    }

    /**
     * Construct the VBox for the pop-up window.
     * @return popUpVBox
     */
    private VBox constructPopUpVBox() {
        final int spacing = 6;
        final int insetPositions = 10;
        VBox popUpVBox = new VBox();
        popUpVBox.setPadding(new Insets(insetPositions,
            insetPositions, insetPositions, insetPositions));
        popUpVBox.setSpacing(spacing);
        popUpVBox.setAlignment(Pos.CENTER);
        return popUpVBox;
    }

    /**
     * Display in a label how many cameras are active.
     */
    private void setCameraStatus() {
        String text;
        if (videoController.getCameras() == 1) {
            text = "1 camera is currently ready to be activated.";
        } else {
            text = videoController.getCameras() + " cameras are currently ready to be activated.";
        }
        cameraStatus = new Label(text);
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(cameraStatus);
        mediaPane.getMediaPlayerPane().setContent(stackPane);
    }

    /**
     * Close the stream(s) and reset the application.
     */
    public void endStream() {
        timerManager.stopTimer();
        mediaPane.showCameraIcon();
    }
}