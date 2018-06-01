package gui.panes;

import gui.controllers.MainController;
import java.io.File;
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

/**
 * Class that creates the MenuPane for the VideoPane.
 */
public class MenuPane {
    /**
     * Class parameters.
     */
    private MediaPane mediaPane;
    private Label cameraStatus;
    private MainController mainController;
    private static SimpleObjectProperty<File> lastKnownDirectoryProperty
            = new SimpleObjectProperty<>();

    /**
     * Constructor for MenuPane.
     * @param control the mainController
     * @param pane the mediaPane
     */
    public MenuPane(final MainController control, final MediaPane pane) {
        this.mainController = control;
        this.mediaPane = pane;
    }

    /**
     * Construct the menu for the application.
     * @param videoPane the pane that will hold the menu
     * @param primaryStage starting stage
     * @return menuPane
     */
    public Pane createMenuPane(final Pane videoPane, final Stage primaryStage) {
        // Menu options for settings
        Menu settings = new Menu("Settings");
        MenuItem clearImageViewers = new MenuItem("Reset Application");
        MenuItem closeApp = new MenuItem("Close Application");
        settings.getItems().addAll(clearImageViewers, closeApp);

        // Menu options for automatic configuration
        Menu config = new Menu("Configure the Escape Room");
        MenuItem configFile = new MenuItem("Load Configuration File...");
        MenuItem standardFile = new MenuItem("Use Standard Configuration");
        config.getItems().addAll(configFile, standardFile);

        // Menu options for manual configuration
        Menu configManual = new Menu("Manual Configuration");
        MenuItem openVideo = new MenuItem("Add Video File...");
        MenuItem connectStream = new MenuItem("Add Stream...");
        configManual.getItems().addAll(openVideo, connectStream);

        // Add al submenus to main menu bar
        MenuBar menu = new MenuBar();
        menu.prefWidthProperty().bind(videoPane.widthProperty());
        menu.getMenus().addAll(settings, config, configManual);
        StackPane menuPane = new StackPane();
        menuPane.getChildren().add(menu);

        // When menu options are clicked
        resetCameras(clearImageViewers);
        closeApp(closeApp);
        openConfig(configFile, primaryStage);
        standardConfig(standardFile);
        openVideo(openVideo, primaryStage);
        connectStream(connectStream, primaryStage);

        return menuPane;
    }

    //---------------------------------------- BEGIN OF ALL MENU OPTIONS
    /**
     * Remove all current cameras.
     * @param clearImageViewers the current imageViewers
     */
    private void resetCameras(final MenuItem clearImageViewers) {
        clearImageViewers.setOnAction(event -> {
            mainController.getTimeLogController().clearInformationArea();
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
            if (!mainController.isVideoPlaying() && !mainController.getConfigured()) {
                FileChooser chooser = new FileChooser();
                chooser.setTitle("Select Configuration File (JSon format)");
                chooser.initialDirectoryProperty().bindBidirectional(lastKnownDirectoryProperty);
                File file = chooser.showOpenDialog(primaryStage);
                if (file != null) {
                    mainController.configure(file.toString());
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
            if (!mainController.isVideoPlaying() && !mainController.getConfigured()) {
                mainController.configure("files/standard.json");
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
            if (!mainController.isVideoPlaying()) {
                FileChooser chooser = new FileChooser();
                chooser.setTitle("Select Video File");
                chooser.initialDirectoryProperty().bindBidirectional(lastKnownDirectoryProperty);
                File file = chooser.showOpenDialog(primaryStage);
                if (file != null) {
                    mainController.createVideo(file);
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
    private void connectStream(
            final MenuItem connectStream, final Stage primaryStage) {
        connectStream.setOnAction(t -> {
            if (!mainController.isVideoPlaying()) {
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
                    mainController.createStream(streamStage, field);
                    setCameraStatus();
                });

                // Save the url of the RTSP stream by pressing on the enter key
                field.setOnKeyPressed(keyEvent -> {
                    if (keyEvent.getCode() == KeyCode.ENTER) {
                        mainController.createStream(streamStage, field);
                        setCameraStatus();
                    }
                });

                final int popUpWidth = 500;
                final int popUpHeight = 100;

                Scene popUp = new Scene(popUpVBox, popUpWidth, popUpHeight);
                streamStage.setScene(popUp);
                streamStage.show();
            }
        });
    }
    //---------------------------------------- END OF ALL MENU OPTIONS

    /**
     * Display in a label how many cameras are active.
     */
    private void setCameraStatus() {
        String text;
        if (mainController.getCameras() == 1) {
            text = "1 camera is currently ready to be activated.";
        } else {
            text = mainController.getCameras() + " cameras are currently ready to be activated.";
        }
        cameraStatus = new Label(text);
        mediaPane.getMediaPlayerPane().getChildren().clear();
        mediaPane.getMediaPlayerPane().getChildren().add(cameraStatus);
    }

    /**
     * Close the stream(s) and reset the application.
     */
    public void endStream() {
        mainController.closeStream();
        mediaPane.getMediaPlayerPane().getChildren().clear();
        mediaPane.showCameraIcon();
    }
}
