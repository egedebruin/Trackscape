package gui.panes;

import gui.controllers.MainController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
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
    /**
     * Class parameters.
     */
    private MediaPane mediaPane;
    private Label cameraStatus;
    private MainController controller;
    private static SimpleObjectProperty<File> lastKnownDirectoryProperty
            = new SimpleObjectProperty<>();

    /**
     * Constructor for MenuPane.
     * @param control the controller
     * @param pane the mediaPane
     */
    public MenuPane(final MainController control, final MediaPane pane) {
        this.controller = control;
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

        // Menu options for (automatic) escape room configuration
        Menu config = new Menu("Configure the Escape Room");
        MenuItem configFile = new MenuItem("Load Configuration File...");
        MenuItem standardFile = new MenuItem("Use Standard Configuration");
        MenuItem manual = new MenuItem("Manual Configuration");
        config.getItems().addAll(configFile, standardFile, manual);

        // Menu options for adding extra media
        Menu extraMedia = new Menu("Add extra media");
        MenuItem openVideo = new MenuItem("Add Video File...");
        MenuItem connectStream = new MenuItem("Add Stream...");
        extraMedia.getItems().addAll(openVideo, connectStream);

        // Add al submenus to main menu bar
        MenuBar menu = new MenuBar();
        menu.prefWidthProperty().bind(videoPane.widthProperty());
        menu.getMenus().addAll(settings, config, extraMedia);
        StackPane menuPane = new StackPane();
        menuPane.getChildren().add(menu);

        // When menu options are clicked
        resetCameras(clearImageViewers);
        closeApp(closeApp);
        openConfig(configFile, primaryStage);
        standardConfig(standardFile);
        manualConfig(manual, primaryStage);
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
            controller.getTimeLogController().clearInformationArea();
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
            if (!controller.isVideoPlaying() && !controller.getConfigured()) {
                FileChooser chooser = new FileChooser();
                chooser.setTitle("Select Configuration File (JSon format)");
                chooser.initialDirectoryProperty().bindBidirectional(lastKnownDirectoryProperty);
                File file = chooser.showOpenDialog(primaryStage);
                if (file != null) {
                    controller.configure(file.toString());
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
            if (!controller.isVideoPlaying() && !controller.getConfigured()) {
                controller.configure("files/standard.json");
                setCameraStatus();
            }
        });
    }

    private void manualConfig(final MenuItem manual, final Stage primaryStage) {
        manual.setOnAction(t -> {
            if (!controller.isVideoPlaying()) {
                final Stage manualStage = new Stage();
                manualStage.setTitle("Manual Escape Room Configuration");
                manualStage.initModality(Modality.APPLICATION_MODAL);
                manualStage.initOwner(primaryStage);

                GridPane fillInPane = new GridPane();
                fillInPane.setAlignment(Pos.CENTER);

                final Label players = new Label("Amount of players: ");
                final TextField playerField = new TextField();
                fillInPane.add(players, 0, 0);
                fillInPane.add(playerField, 1, 0);

                final Label chests = new Label("Amount of chests: ");
                final TextField chestField = new TextField();
                fillInPane.add(chests, 0, 1);
                fillInPane.add(chestField, 1, 1);

                final int maxWidth = 60;
                playerField.setMaxWidth(maxWidth);
                chestField.setMaxWidth(maxWidth);

                Button proceed = new Button("Proceed");
                proceed.setOnAction(t1 -> {
                    int filledInChests = Integer.parseInt(chestField.getText());
                    int j = 1;
                    for (int i = 0; i < filledInChests*3; i = i + 3) {
                        Label settings = new Label("Settings for chest " + j);
                        settings.setStyle("-fx-font-weight: bold");

                        Label sections = new Label("Amount of sections: ");
                        Label targetDuration = new Label("The target duration in sec: ");
                        TextField sectionField = new TextField();
                        TextField durationField = new TextField();

                        sectionField.setMaxWidth(maxWidth);
                        durationField.setMaxWidth(maxWidth);

                        fillInPane.add(settings, 0, i + 3);
                        fillInPane.add(sections, 0, i + 4);
                        fillInPane.add(sectionField, 1, i + 4);
                        fillInPane.add(targetDuration, 0, i + 5);
                        fillInPane.add(durationField, 1, i + 5);
                        j++;
                    }
                    proceed.setVisible(false);
                });
                fillInPane.add(proceed, 0, 2);

                Button submit = new Button("Submit");
                submit.setOnAction(t1 -> {
                    manualStage.close();
                });
//                fillInPane.add(submit, 6, 7);

                ScrollPane scrollPane = new ScrollPane();
                scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
                scrollPane.setContent(fillInPane);

                Scene manualConfigScene = new Scene(scrollPane, 330, 350);
                manualStage.setScene(manualConfigScene);
                manualStage.show();
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
            if (!controller.isVideoPlaying()) {
                FileChooser chooser = new FileChooser();
                chooser.setTitle("Select Video File");
                chooser.initialDirectoryProperty().bindBidirectional(lastKnownDirectoryProperty);
                File file = chooser.showOpenDialog(primaryStage);
                if (file != null) {
                    controller.createVideo(file);
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
            if (!controller.isVideoPlaying()) {
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
                    controller.createStream(streamStage, field);
                    setCameraStatus();
                });

                // Save the url of the RTSP stream by pressing on the enter key
                field.setOnKeyPressed(keyEvent -> {
                    if (keyEvent.getCode() == KeyCode.ENTER) {
                        controller.createStream(streamStage, field);
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
        if (controller.getCameras() == 1) {
            text = "1 camera is currently ready to be activated.";
        } else {
            text = controller.getCameras() + " cameras are currently ready to be activated.";
        }
        cameraStatus = new Label(text);
        mediaPane.getMediaPlayerPane().getChildren().clear();
        mediaPane.getMediaPlayerPane().getChildren().add(cameraStatus);
    }

    /**
     * Close the stream(s) and reset the application.
     */
    public void endStream() {
        controller.closeStream();
        mediaPane.getMediaPlayerPane().getChildren().clear();
        mediaPane.showCameraIcon();
    }

}
