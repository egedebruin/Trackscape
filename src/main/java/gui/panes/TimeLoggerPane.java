package gui.panes;

import gui.controllers.MainController;
import gui.controllers.RoomController;
import gui.controllers.TimeLogController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.File;

/**
 * Class that creates the TimeLoggerPane for the VideoPane.
 */
public class TimeLoggerPane {
    /**
     * Class parameters.
     */
    private TimeLogController timeLogController;
    private MainController mainController;

    /**
     * Constructor for TimeLoggerPane.
     * @param control the mainController
     */
    public TimeLoggerPane(final MainController control) {
        this.mainController = control;
        this.timeLogController = control.getTimeLogController();
    }

    /**
     * Creates the pane in which the timer will be shown.
     *
     * @return timerPane
     */
    public Pane createTimeLoggerPane() {
        FlowPane timerPane = new FlowPane();
        timerPane.setAlignment(Pos.TOP_CENTER);

        Label description = new Label("Time playing:");
        Label l = new Label("00:00:00");
        timeLogController.setTimerLabel(l);

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
        timeLogController.setInformationBox(logText);

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
        final int padding = 5;
        GridPane buttonPane = new GridPane();
        buttonPane.setAlignment(Pos.BOTTOM_CENTER);
        buttonPane.setPadding(new Insets(padding, padding, 0, 0));

        Label question = new Label("Is this a chest?" + "\n");
        question.setVisible(false);

        File tick = new File(System.getProperty("user.dir")
                + "\\src\\main\\java\\gui\\images\\checkButton.png");
        File cross = new File(System.getProperty("user.dir")
                + "\\src\\main\\java\\gui\\images\\cancelButton.png");

        final int viewHeight = 50;

        Image greenTick = new Image(tick.toURI().toString());
        ImageView tickView = new ImageView();
        tickView.setFitHeight(viewHeight);
        tickView.setPreserveRatio(true);
        tickView.setImage(greenTick);

        Image redCross = new Image(cross.toURI().toString());
        ImageView crossView = new ImageView();
        crossView.setFitHeight(viewHeight);
        crossView.setPreserveRatio(true);
        crossView.setImage(redCross);

        Button approveButton = new Button();
        approveButton.setGraphic(tickView);
        approveButton.setVisible(false);
        approveButton.setOnAction(event -> {
            timeLogController.confirmedChest();
            RoomController roomController = mainController.getRoomController();
            if (roomController.getProgress() != null) {
                roomController.getProgress().getRoom().setNextChestOpened();
                roomController.fillProgress(roomController.getProgress().getFillCount());
            }
        });

        Button notApprove = new Button();
        notApprove.setGraphic(crossView);
        notApprove.setVisible(false);
        notApprove.setOnAction(event -> timeLogController.unConfirm());

        ImageView imageView = new ImageView();
        imageView.setVisible(false);

        final int rowIndex = 3;
        buttonPane.add(imageView, 0, 1);
        buttonPane.add(question, 0, 2);
        buttonPane.add(approveButton, 0, rowIndex);
        buttonPane.add(notApprove, 1, rowIndex);

        timeLogController.setQuestion(question);
        timeLogController.setApproveButton(approveButton);
        timeLogController.setNotApproveButton(notApprove);
        timeLogController.setImageView(imageView);

        return buttonPane;
    }
}
