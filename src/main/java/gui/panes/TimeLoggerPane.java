package gui.panes;

import gui.Util;
import gui.controllers.MainController;
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
                createApproveArea());

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

        final int width = 400;
        final int height = 300;

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
        FlowPane buttonPane = new FlowPane();
        buttonPane.setAlignment(Pos.BOTTOM_CENTER);
        buttonPane.setPadding(new Insets(padding, padding, 0, 0));

        Label question = new Label("      Is this a newly opened chest?      ");
        question.setVisible(false);

        ImageView imageView = new ImageView();
        imageView.setVisible(false);

        Label timeStamp = new Label();
        timeStamp.setVisible(false);

        final int viewHeight = 70;
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

        approveButton.setOnAction(event -> {
            String chestsFound = "";
            if (mainController.getConfigured()) {
                chestsFound = mainController.getRoomController().confirmedChest();
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
        Button notApprove = new Button();
        notApprove.setGraphic(Util.createImageViewLogo("buttons\\disapprove", viewHeight));
        notApprove.setVisible(false);

        notApprove.setOnAction(event -> timeLogController.unConfirm());
        notApprove.setOnMouseEntered(event -> {
            notApprove.setGraphic(Util.createImageViewLogo(
                "buttons\\disapproveActive", viewHeight));
        });
        notApprove.setOnMouseExited(event -> {
            notApprove.setGraphic(Util.createImageViewLogo("buttons\\disapprove", viewHeight));
        });

        return notApprove;
    }

}
