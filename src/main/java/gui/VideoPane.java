package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

/**
 * Class that constructs the videoPane for the MonitorScene.
 */
public class VideoPane {
    /**
     * Class parameters.
     */
    private ArrayList<ImageView> imageViews = new ArrayList<>();
    private FlowPane mediaPlayerPane = new FlowPane();
    private Controller controller;
    private MenuMediaPane menuMediaPane;

    /**
     * Constructor for VideoPane.
     * @param control the controller
     */
    public VideoPane(final Controller control) {
        this.controller = control;
        menuMediaPane = new MenuMediaPane(controller, mediaPlayerPane);
    }

    /**
     * createVideoPane.
     * Create the center pane for the root, containing menu,
     * mediaPlayer, and mediaBar for the monitorScene
     * @param primaryStage starting stage
     * @return videoPane
     */
    public Pane createVideoPane(final Stage primaryStage) {
        BorderPane videoPane = new BorderPane();

        ArrayList<Pane> mmp =
            menuMediaPane.createMenuAndMediaPane(videoPane, primaryStage);

        // get the menubar and put it at the top of the videoPane
        videoPane.setTop(mmp.get(0));
        // get the imageView (location where videos are shown)
        // and put it in the center of the videoPane
        videoPane.setCenter(mmp.get(1));

        // create the functionality panes for the videoPane
        videoPane.setBottom(createMediaBar());
        videoPane.setLeft(createTimeLoggerPane());
        videoPane.setRight(new FlowPane()); // escape room tatus will be displayed here

        return videoPane;
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
        final int spacing = 10;

        // Create mediabar for video options
        HBox mediaBar = new HBox();
        mediaBar.setAlignment(Pos.CENTER);
        mediaBar.setPadding(new Insets(top, right, bottom, left));
        mediaBar.setSpacing(spacing);

        // Create the play/pauze button
        final Button playButton = new Button("Start Cameras");
        playButton.setOnAction(event -> {
            if (controller.getCameras() == 0) {
                menuMediaPane.getMediaPane().showCameraIcon();
            } else if (!controller.isVideoPlaying()) {
                controller.setVideoPlaying(true);
                initializeImageViewers();
                controller.grabTimeFrame(imageViews);
            }
        });

        final Button closeStream = new Button("Close Stream");
        closeStream.setOnAction(event -> menuMediaPane.getMenuPane().endStream());

        mediaBar.getChildren().addAll(playButton, closeStream);

        return mediaBar;
    }

    /**
     * Creates the pane in which the timer will be shown.
     *
     * @return timerPane
     */
    private Pane createTimeLoggerPane() {
        FlowPane timerPane = new FlowPane();
        timerPane.setAlignment(Pos.TOP_CENTER);

        Label description = new Label("Time playing:");
        Label l = new Label("00:00:00");
        this.controller.setTimerLabel(l);

        final int top = 5;
        final int bottom = 5;

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(top, 0, bottom, 0));
        vBox.getChildren().add(l);

        timerPane.getChildren().addAll(description, vBox, createLoggerPane(),
            createApproveButton());

        return timerPane;
    }

    /**
     * Creates the logger pane in which logs will be shown.
     *
     * @return loggerPane The logger pane
     */
    private Pane createLoggerPane() {
        FlowPane loggerPane = new FlowPane();
        loggerPane.setAlignment(Pos.CENTER);

        TextArea logText = new TextArea();
        logText.setEditable(false);
        controller.setInformationBox(logText);

        final int width = 350;
        final int height = 300;

        logText.setPrefSize(width, height);

        // Automatically scrolls to bottom when items are added to the textArea
        logText.setScrollTop(Double.MIN_VALUE);

        loggerPane.getChildren().add(logText);

        return loggerPane;
    }


    /**
     * Method to create an approveButton inside a new Pane.
     * @return the Pane where the button is made on.
     */
    public Pane createApproveButton() {
        final int topPadding = 10;
        FlowPane buttonPane = new FlowPane();
        buttonPane.setAlignment(Pos.BOTTOM_CENTER);
        buttonPane.setPadding(new Insets(topPadding, 0, 0, 0));

        Button approveButton = new Button();
        approveButton.setText("Confirm chest opened");
        approveButton.setVisible(false);
        approveButton.setOnAction(event -> controller.confirmedChest());

        Button notApprove = new Button();
        notApprove.setText("Not Confirm");
        notApprove.setVisible(false);
        notApprove.setOnAction(event -> controller.unConfirm());

        buttonPane.getChildren().addAll(approveButton, notApprove);

        controller.setApproveButton(approveButton);
        controller.setNotApproveButton(notApprove);

        return buttonPane;
    }

    /**
     * Initializes the imageViews with a black image.
     */
    private void initializeImageViewers() {
        mediaPlayerPane.getChildren().clear();

        imageViews.clear();
        for (int k = 0; k < controller.getCameras(); k++) {
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
        mediaPlayerPane.getChildren().addAll(imageViews);
    }
}