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

import javax.xml.soap.Text;

/**
 * Class that creates the ManualConfigPane in a new Scene.
 */
public class ManualConfigPane {
    /**
     * Class parameters.
     */
    private MainController controller;

    /**
     * Constructor for ManualConfigPane.
     * @param control the mainController
     */
    public ManualConfigPane(final MainController control) {
        this.controller = control;
    }

    public void createManualConfig(final MenuItem manual, final Stage primaryStage) {
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
                    proceedButtonAction(chestField, maxWidth, fillInPane);
                    proceed.setVisible(false);
                    chestField.setEditable(false);
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

    public void proceedButtonAction(TextField chestField, int maxWidth, GridPane fillInPane) {
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
    }


}
