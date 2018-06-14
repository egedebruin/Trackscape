package gui.panes;

import gui.Util;
import gui.controllers.RoomController;
import gui.controllers.TimeLogController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Class that creates the TimeLoggerPane for the VideoPane.
 */
public class TimeLoggerPane {
    /**
     * Class parameters.
     */
    private TimeLogController timeLogController;
    private RoomController roomController;

    /**
     * Constructor for timeLoggerPane.
     * @param timeControl the timeController
     * @param roomControl the roomController
     */
    public TimeLoggerPane(final TimeLogController timeControl, final RoomController roomControl) {
        timeLogController = timeControl;
        roomController = roomControl;
    }

    /**
     * Creates the pane in which the timer will be shown.
     *
     * @return timerPane
     */
    public Pane createTimeLoggerPane() {
        final int largePadding = 15;
        final int smallPadding = 5;
        final int top = 5;
        final int bottom = 5;

        FlowPane timerPane = new FlowPane();
        timerPane.setAlignment(Pos.TOP_CENTER);
        timerPane.setPadding(new Insets(largePadding, 0, smallPadding, largePadding));

        Label description = new Label("Time playing: ");
        Label l = new Label("00:00:00");
        timeLogController.setTimerLabel(l);

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(top, 0, bottom, 0));
        vBox.getChildren().add(l);

        timerPane.getChildren().addAll(description, vBox, createLoggerPane(), createApproveArea());

        return timerPane;
    }

    /**
     * Creates the logger pane in which logs will be shown.
     *
     * @return loggerPane The logger pane
     */
    private Pane createLoggerPane() {
        final int width = 400;
        final int height = 300;

        FlowPane loggerPane = new FlowPane();
        loggerPane.setAlignment(Pos.CENTER);

        TextArea logText = new TextArea();
        logText.setEditable(false);
        timeLogController.setInformationBox(logText);

        logText.setPrefSize(width, height);

        // Automatically scrolls to bottom when items are added to the textArea
        logText.setScrollTop(Double.MIN_VALUE);

        loggerPane.getChildren().add(logText);

        return loggerPane;
    }


    /**
     * Method to create the approve are inside a new Pane, which consists of
     * the imageview, question and buttons.
     * @return the Pane where the area is made on.
     */
    public Pane createApproveArea() {
        final int padding = 20;
        final int viewHeight = 70;

        FlowPane buttonPane = new FlowPane();
        buttonPane.setAlignment(Pos.BOTTOM_CENTER);
        buttonPane.setPadding(new Insets(padding, padding, 0, 0));

        Label question = new Label("      Is this a newly opened chest?      ");
        question.setVisible(false);

        ImageView imageView = new ImageView();
        imageView.setVisible(false);

        Label timeStamp = new Label();
        timeStamp.setVisible(false);

        Button approveButton = createApproveButton(viewHeight);
        Button disapproveButton = createDisapproveButton(viewHeight);

        buttonPane.getChildren().addAll(imageView, timeStamp, question,
            approveButton, disapproveButton);

        timeLogController.setQuestion(question);
        timeLogController.setApproveButton(approveButton);
        timeLogController.setNotApproveButton(disapproveButton);
        timeLogController.setImageView(imageView);
        timeLogController.setTimeStamp(timeStamp);

        return buttonPane;
    }

    /**
     * Create the approve button.
     * @param viewHeight the height of the ImageView
     * @return ApproveButton
     */
    private Button createApproveButton(final int viewHeight) {
        Button approveButton = new Button();
        approveButton.setGraphic(Util.createImageViewLogo("buttons\\approve", viewHeight));
        approveButton.setVisible(false);
        return addFunctionalityApproveButton(approveButton, viewHeight);
    }

    /**
     * Add functionality to the approveButton when something happens to it.
     * @param approveButton the button
     * @param viewHeight the height of the image set on the button
     * @return the approveButton
     */
    private Button addFunctionalityApproveButton(final Button approveButton, final int viewHeight) {
        approveButton.setCursor(Cursor.HAND);
        approveButton.setOnAction(event -> {
            String chestsFound = "";
            if (roomController.isConfigured()) {
                long timestamp = timeLogController.getChestTimestamp();
                chestsFound = roomController.getProgress().confirmedChestString(timestamp);
            }
            timeLogController.confirmedChest(chestsFound);
        });
        approveButton.setOnMouseEntered(event -> {
            approveButton.setGraphic(Util.createImageViewLogo(
                "buttons\\approveActive", viewHeight));
        });
        approveButton.setOnMouseExited(event -> {
            approveButton.setGraphic(Util.createImageViewLogo("buttons\\approve", viewHeight));
        });
        return approveButton;
    }

    /**
     * Create the disapprove button.
     * @param viewHeight the height of the imageView
     * @return notApprove button
     */
    private Button createDisapproveButton(final int viewHeight) {
        Button disapproveButton = new Button();
        disapproveButton.setGraphic(Util.createImageViewLogo("buttons\\disapprove", viewHeight));
        disapproveButton.setCursor(Cursor.HAND);
        return addFunctionalityDisapproveButton(disapproveButton, viewHeight);
    }

    /**
     * Add functionality to the disapproveButton when something happens to it.
     * @param disapproveButton the button
     * @param viewHeight the height of the image set on the button
     * @return the disapproveButton
     */
    private Button addFunctionalityDisapproveButton(
        final Button disapproveButton, final int viewHeight) {
        disapproveButton.setOnAction(event -> timeLogController.unConfirm());
        disapproveButton.setOnMouseEntered(event -> {
            disapproveButton.setGraphic(Util.createImageViewLogo(
                "buttons\\disapproveActive", viewHeight));
        });
        disapproveButton.setOnMouseExited(event -> {
            disapproveButton.setGraphic(
                Util.createImageViewLogo("buttons\\disapprove", viewHeight));
        });

        return disapproveButton;
    }
}