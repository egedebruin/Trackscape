package gui.panes;

import gui.Util;
import gui.controllers.MainController;
import gui.controllers.RoomController;
import gui.controllers.TimeLogController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
        final int padding = 20;
        FlowPane buttonPane = new FlowPane();
        buttonPane.setAlignment(Pos.BOTTOM_CENTER);
        buttonPane.setPadding(new Insets(padding, padding, 0, 0));

        Label question = new Label("      Is this a newly opened chest?      ");
        question.setVisible(false);

        ImageView imageView = new ImageView();
        imageView.setVisible(false);

        final int viewHeight = 70;
        Button approveButton = createApproveButton(viewHeight);
        Button disapproveButton = createDisapproveButton(viewHeight);

        buttonPane.getChildren().addAll(imageView, question, approveButton, disapproveButton);

        timeLogController.setQuestion(question);
        timeLogController.setApproveButton(approveButton);
        timeLogController.setNotApproveButton(disapproveButton);
        timeLogController.setImageView(imageView);

        return buttonPane;
    }

    /**
     * Create the approve button.
     * @param viewHeight the height of the ImageView
     * @return ApproveButton
     */
    private Button createApproveButton(final int viewHeight) {
        Button approveButton = new Button();
        approveButton.setGraphic(Util.createButtonLogo("approve", viewHeight));
        approveButton.setVisible(false);

        approveButton.setOnAction(event -> {
            timeLogController.confirmedChest();
            RoomController roomController = mainController.getRoomController();
            if (roomController.getProgress() != null) {
                roomController.getProgress().getRoom().setNextChestOpened();
                roomController.fillProgress(roomController.getProgress().getFillCount());
            }
        });
        approveButton.setOnMouseEntered(event -> {
            approveButton.setGraphic(Util.createButtonLogo(
                "approveActive", viewHeight));
        });
        approveButton.setOnMouseExited(event -> {
            approveButton.setGraphic(Util.createButtonLogo("approve", viewHeight));
        });

        return approveButton;
    }

    /**
     * Create the disapprove button.
     * @param viewHeight the height of the imageView
     * @return notApprove button
     */
    private Button createDisapproveButton(final int viewHeight) {
        Button notApprove = new Button();
        notApprove.setGraphic(Util.createButtonLogo("disapprove", viewHeight));
        notApprove.setVisible(false);

        notApprove.setOnAction(event -> timeLogController.unConfirm());
        notApprove.setOnMouseEntered(event -> {
            notApprove.setGraphic(Util.createButtonLogo(
                "disapproveActive", viewHeight));
        });
        notApprove.setOnMouseExited(event -> {
            notApprove.setGraphic(Util.createButtonLogo("disapprove", viewHeight));
        });

        return notApprove;
    }

}
