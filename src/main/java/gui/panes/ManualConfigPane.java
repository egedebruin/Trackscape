package gui.panes;

import gui.controllers.MainController;
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
    private MainController controller;
    private Stage manualStage;

    /**
     * Constructor for ManualConfigPane.
     * @param control the mainController
     */
    public ManualConfigPane(final MainController control) {
        this.controller = control;
    }

    /**
     * Creates the manualConfig stage and pane.
     * @param manual the menuItem
     * @param primaryStage the primaryStage
     */
    public void createManualConfig(final MenuItem manual, final Stage primaryStage) {
        manual.setOnAction(t -> {
            if (!controller.isVideoPlaying()) {
                manualStage = new Stage();
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
                proceedButtonAction(playerField, chestField, maxWidth, fillInPane, proceed);
                fillInPane.add(proceed, 0, 2);

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
     * Handles the action of the proceed and the submit buttons.
     * @param playerField the field in which the amount of players need to be filled in
     * @param chestField the field in which the amount of chests need to be filled in
     * @param maxWidth the maximum width of the textFields
     * @param fillInPane the pane in which labels and textFields are added
     * @param proceed the proceed button
     */
    public void proceedButtonAction(TextField playerField, TextField chestField, int maxWidth, GridPane fillInPane, Button proceed) {
        Label error = new Label("Please fill in a number!");
        Label submitError = new Label("Please fill in a number!");
        fillInPane.add(error, 0, 3);
        error.setVisible(false);
        proceed.setOnAction(t1 -> {
            if (!chestField.getText().isEmpty() && !playerField.getText().isEmpty()) {
                int filledInChests;
                try {
                    filledInChests = Integer.parseInt(chestField.getText());
                    error.setVisible(false);
                    ArrayList<TextField> sectionList = new ArrayList<>();
                    ArrayList<TextField> durationList = new ArrayList<>();
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
                        int line = (i - 1) * 3;
                        fillInPane.add(settings, 0, line + 3);
                        fillInPane.add(sections, 0, line + 4);
                        fillInPane.add(sectionField, 1, line + 4);
                        fillInPane.add(targetDuration, 0, line + 5);
                        fillInPane.add(durationField, 1, line + 5);
                    }

                    Button submit = new Button("Submit");
                    fillInPane.add(submit, 0, filledInChests * 3 + 3);

                    submitError.setVisible(false);
                    fillInPane.add(submitError, 0, filledInChests * 3 + 4);
                    submit.setOnAction(t2 -> {
                        try {
                            int players = Integer.parseInt(playerField.getText().trim());
                            ArrayList<Integer> sectionIntList = new ArrayList<>();
                            ArrayList<Integer> durationIntList = new ArrayList<>();
                            for (int i = 0; i < sectionList.size(); i++) {
                                sectionIntList.add(Integer.parseInt(sectionList.get(i).getText().trim()));
                                durationIntList.add(Integer.parseInt(durationList.get(i).getText().trim()));
                            }
                            controller.getRoomController().manualConfig(players, filledInChests, sectionIntList, durationIntList);
                            manualStage.close();
                        } catch (NumberFormatException e) {
                            submitError.setVisible(true);
                        }
                    });
                    proceed.setVisible(false);
                    chestField.setEditable(false);
                } catch (NumberFormatException e) {
                    error.setVisible(true);
                }
            } else {
                error.setVisible(true);
            }
        });
    }


}
