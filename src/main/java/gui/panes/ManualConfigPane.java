package gui.panes;

import gui.controllers.RoomController;
import gui.controllers.VideoController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Class that creates the ManualConfigPane in a new Scene.
 */
public class ManualConfigPane {
    /**
     * Class parameters.
     */
    private VideoController videoController;
    private RoomController roomController;
    private Stage manualStage;

    /**
     * Constructor for ManualConfigPane.
     * @param vControl the videoController
     * @param rControl the roomController
     */
    public ManualConfigPane(final VideoController vControl, final RoomController rControl) {
        this.videoController = vControl;
        this.roomController = rControl;
    }

    /**
     * Creates the manualConfig stage and pane.
     * @param manual the menuItem
     * @param primaryStage the primaryStage
     */
    public void createManualConfig(final MenuItem manual, final Stage primaryStage) {
        manual.setOnAction(t -> {
            if (videoController.isClosed()) {
                manualStage = new Stage();
                manualStage.setTitle("Manual Escape Room Configuration");
                manualStage.initModality(Modality.APPLICATION_MODAL);
                manualStage.initOwner(primaryStage);
                manualStage.setResizable(false);

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

                final Label totalDuration = new Label("Total duration of the escape room in sec: ");
                final TextField totalDurationField = new TextField();
                fillInPane.add(totalDuration, 0, 2);
                fillInPane.add(totalDurationField, 1, 2);

                final int maxWidth = 60;
                playerField.setMaxWidth(maxWidth);
                chestField.setMaxWidth(maxWidth);
                totalDurationField.setMaxWidth(maxWidth);

                Button proceed = new Button("Proceed");
                proceedButtonAction(playerField, chestField, totalDurationField, maxWidth,
                        fillInPane, proceed);
                final int rowIndex = 3;
                fillInPane.add(proceed, 0, rowIndex);

                ScrollPane scrollPane = new ScrollPane();
                scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
                scrollPane.setContent(fillInPane);
                scrollPane.setFitToHeight(true);
                scrollPane.setFitToWidth(true);

                final int width = 360;
                final int height = 400;
                Scene manualConfigScene = new Scene(scrollPane, width, height);
                manualStage.setScene(manualConfigScene);
                manualStage.show();
            }
        });
    }

    /**
     * Handles the action of the proceed and the submit buttons.
     * @param playerField the field in which the amount of players need to be filled in
     * @param chestField the field in which the amount of chests need to be filled in
     * @param totalDurationField the field in which the total duration of the escape room
     *                           needs to be filled in
     * @param maxWidth the maximum width of the textFields
     * @param fillInPane the pane in which labels and textFields are added
     * @param proceed the proceed button
     */
    private void proceedButtonAction(final TextField playerField, final TextField chestField,
                                     final TextField totalDurationField, final int maxWidth,
                                     final GridPane fillInPane, final Button proceed) {
        Label error = new Label("Please fill in a number!");
        Label submitError = new Label("Please fill in a number!");
        final int rowIndex = 4;
        fillInPane.add(error, 0, rowIndex);
        error.setVisible(false);
        proceed.setOnAction(t1 -> {
            if (!chestField.getText().isEmpty() && !playerField.getText().isEmpty()
                    && !totalDurationField.getText().isEmpty()) {
                try {
                    int filledInChests = Integer.parseInt(chestField.getText());
                    int players = Integer.parseInt(playerField.getText().trim());
                    int totalDuration = Integer.parseInt(totalDurationField.getText().trim());
                    error.setVisible(false);
                    ArrayList<TextField> sectionList = new ArrayList<>();
                    ArrayList<TextField> durationList = new ArrayList<>();
                    final int skipThree = 3;
                    final int skipFour = 4;
                    final int skipFive = 5;
                    for (int i = 1; i <= filledInChests; i++) {
                        Label settings = new Label("Settings for chest " + i);
                        settings.setStyle("-fx-font-weight: bold");

                        Label sections = new Label("Amount of sections: ");
                        Label targetDuration = new Label("The target duration in sec: ");
                        TextField sectionField = new TextField();
                        TextField durationField = new TextField();

                        sectionList.add(sectionField);
                        durationList.add(durationField);

                        sectionField.setMaxWidth(maxWidth);
                        durationField.setMaxWidth(maxWidth);

                        int line = (i - 1) * skipThree;
                        fillInPane.add(settings, 0, line + skipThree);
                        fillInPane.add(sections, 0, line + skipFour);
                        fillInPane.add(sectionField, 1, line + skipFour);
                        fillInPane.add(targetDuration, 0, line + skipFive);
                        fillInPane.add(durationField, 1, line + skipFive);
                    }

                    Button submit = new Button("Submit");
                    fillInPane.add(submit, 0, filledInChests * skipThree + skipThree);

                    submitError.setVisible(false);
                    fillInPane.add(submitError, 0, filledInChests * skipThree + skipFour);
                    submit.setOnAction(t2 -> {
                        try {
                            ArrayList<Integer> sectionIntList = new ArrayList<>();
                            ArrayList<Integer> durationIntList = new ArrayList<>();
                            for (int i = 0; i < sectionList.size(); i++) {
                                sectionIntList.add(Integer.parseInt(
                                        sectionList.get(i).getText().trim()));
                                durationIntList.add(Integer.parseInt(
                                        durationList.get(i).getText().trim()));
                            }
                            roomController.manualConfig(players, filledInChests,
                                    totalDuration, sectionIntList, durationIntList);
                            manualStage.close();
                        } catch (NumberFormatException e) {
                            submitError.setVisible(true);
                        }
                    });
                    proceed.setVisible(false);
                    chestField.setEditable(false);
                    playerField.setEditable(false);
                    totalDurationField.setEditable(false);
                } catch (NumberFormatException e) {
                    error.setVisible(true);
                }
            } else {
                error.setVisible(true);
            }
        });
    }


}
