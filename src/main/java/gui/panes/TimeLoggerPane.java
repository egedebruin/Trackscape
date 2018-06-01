package gui.panes;

import gui.controllers.TimeLogController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;

/**
 * Class that creates the TimeLoggerPane for the VideoPane.
 */
public class TimeLoggerPane {
    /**
     * Class parameters.
     */
    private TimeLogController timeLogController;

    /**
     * Constructor for TimeLoggerPane.
     * @param control the mainController
     */
    public TimeLoggerPane(final TimeLogController control) {
        this.timeLogController = control;
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
        final int topPadding = 10;
        FlowPane buttonPane = new FlowPane();
        buttonPane.setAlignment(Pos.BOTTOM_CENTER);
        buttonPane.setPadding(new Insets(topPadding, 0, 0, 0));

        Button approveButton = new Button();
        approveButton.setText("Confirm chest opened");
        approveButton.setVisible(false);
        approveButton.setOnAction(event -> timeLogController.confirmedChest());

        Button notApprove = new Button();
        notApprove.setText("Not Confirm");
        notApprove.setVisible(false);
        notApprove.setOnAction(event -> timeLogController.unConfirm());

        ImageView imageView = new ImageView();
        imageView.setVisible(false);

        buttonPane.getChildren().addAll(approveButton, notApprove, imageView);
        
        timeLogController.setApproveButton(approveButton);
        timeLogController.setNotApproveButton(notApprove);
        timeLogController.setImageView(imageView);

        return buttonPane;
    }
}
