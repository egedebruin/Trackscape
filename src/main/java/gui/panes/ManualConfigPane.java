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
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Class that creates the ManualConfigPane in a new Scene.
 */
public class ManualConfigPane {

    private VideoController videoController;
    private RoomController roomController;
    private Stage manualStage;
    private GridPane fillInPane;
    private Font font;
    private final int maxWidth = 60;

    /**
     * Constructor for ManualConfigPane.
     * @param vControl the videoController
     * @param rControl the roomController
     */
    public ManualConfigPane(final VideoController vControl, final RoomController rControl) {
        this.videoController = vControl;
        this.roomController = rControl;
        this.manualStage = new Stage();
        this.fillInPane = new GridPane();

        final int fontSize = 20;
        this.font = new Font(fontSize);
    }

    /**
     * Creates the manualConfig stage and panes.
     * @param manual the menuItem
     */
    public void createManualConfig(final MenuItem manual) {
        manual.setOnAction(t -> {
            if (videoController.isClosed()) {
                resetFillInPane();
                manualStage.setTitle("Manual Escape Room Configuration");

                fillInPane.setAlignment(Pos.CENTER);

                createPaneItems();
                ScrollPane scrollPane = createScrollPane();

                final int width = 500;
                final int height = 500;
                Scene manualConfigScene = new Scene(scrollPane, width, height);

                manualStage.setScene(manualConfigScene);
                manualStage.show();
            }
        });
    }

    /**
     * Creates the items for manual config pane.
     */
    private void createPaneItems() {
        final TextField playerField = new TextField();
        final TextField chestField = new TextField();
        final TextField totalDurationField = new TextField();

        createPlayersLine(playerField);
        createChestsLine(chestField);
        createTotalDurationLine(totalDurationField);

        Button proceed = new Button("Proceed");
        proceed.setFont(font);

        proceedSetUp(playerField, chestField, totalDurationField, proceed);

        final int rowIndex = 3;
        fillInPane.add(proceed, 0, rowIndex);
    }

    /**
     * Adds the line to the pane to fill in the amount of players.
     * @param playerField the field in which the amount of players need to be filled in
     */
    private void createPlayersLine(final TextField playerField) {
        final Label players = new Label("Amount of players: ");
        players.setFont(font);

        playerField.setMaxWidth(maxWidth);

        fillInPane.add(players, 0, 0);
        fillInPane.add(playerField, 1, 0);
    }

    /**
     * Adds the line to the pane to fill in the amount of chests.
     * @param chestField the field in which the amount of chests need to be filled in
     */
    private void createChestsLine(final TextField chestField) {
        final Label chests = new Label("Amount of chests: ");
        chests.setFont(font);

        chestField.setMaxWidth(maxWidth);

        fillInPane.add(chests, 0, 1);
        fillInPane.add(chestField, 1, 1);
    }

    /**
     * Adds the line to the pane to fill in the total duration of the escape room.
     * @param totalDurationField the field in which the total duration need to be filled in
     */
    private void createTotalDurationLine(final TextField totalDurationField) {
        final Label totalDuration = new Label("Total duration of the escape room: ");
        final Label seconds = createSecondsLabel();
        totalDuration.setFont(font);

        totalDurationField.setMaxWidth(maxWidth);

        fillInPane.add(totalDuration, 0, 2);
        fillInPane.add(totalDurationField, 1, 2);
        fillInPane.add(seconds, 2, 2);
    }

    /**
     * Creates the scrollPane in the new window.
     * @return the scrollPane
     */
    private ScrollPane createScrollPane() {

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setContent(fillInPane);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        return scrollPane;
    }

    /**
     * Handles the set up of the proceed button.
     * @param playerField the field in which the amount of players need to be filled in
     * @param chestField the field in which the amount of chests need to be filled in
     * @param totalDurationField the field in which the total duration of the escape room
     *                           needs to be filled in
     * @param proceed the proceed button
     */
    private void proceedSetUp(final TextField playerField, final TextField chestField,
                                     final TextField totalDurationField, final Button proceed) {
        Label error = createFixedErrorMessage();
        proceed.setOnAction(t1 -> {
            resetChestFillIn();
            if (!chestField.getText().isEmpty() && !playerField.getText().isEmpty()
                    && !totalDurationField.getText().isEmpty()) {
                proceedAction(chestField, playerField, totalDurationField, error, proceed);
            } else {
                error.setVisible(true);
            }
        });
    }

    /**
     * Reset the chest section of the fillInPane only.
     */
    private void resetChestFillIn() {
        final int chestSectionStartIndex = 9;
        fillInPane.getChildren().remove(chestSectionStartIndex, fillInPane.getChildren().size());

        roomController.resetProgressObject();
    }

    /**
     * Reset the fillInPane to clear previous content completely.
     */
    private void resetFillInPane() {
        fillInPane.getChildren().clear();
    }

    /**
     * Creates a fixed error message.
     * @return the error label
     */
    private Label createFixedErrorMessage() {
        Label error = new Label("Please fill in a number!");
        error.setVisible(false);

        final int rowIndex = 4;
        fillInPane.add(error, 0, rowIndex);

        return error;
    }

    /**
     * Handles the action after clicking on the proceed button.
     * @param chestField the field in which the amount of chests is filled in
     * @param playerField the field in which the amount of players is filled in
     * @param totalDurationField the field in which the total duration is filled in
     * @param error the fixed error message label
     * @param proceed the proceed button
     */
    private void proceedAction(final TextField chestField, final TextField playerField,
                              final TextField totalDurationField, final Label error,
                              final Button proceed) {
        try {
            int filledInChests = Integer.parseInt(chestField.getText());
            int players = Integer.parseInt(playerField.getText().trim());
            int totalDuration = Integer.parseInt(totalDurationField.getText().trim());

            error.setVisible(false);

            ArrayList<TextField> sectionList = new ArrayList<>();
            ArrayList<TextField> durationList = new ArrayList<>();

            createProceedItems(filledInChests, sectionList, durationList);

            Button submit = createSubmitButton(filledInChests);
            Label submitError = createSubmitError(filledInChests);

            submit.setOnAction(t2 -> {
                submitAction(sectionList, durationList, players, filledInChests, totalDuration,
                        submitError);
            });

        } catch (NumberFormatException e) {
            error.setVisible(true);
        }
    }

    /**
     * Add the items to the window belonging to the action of the proceed button.
     * @param filledInChests the amount of chests filled in
     * @param sectionList the list with textFields of the amount of sections of a chest
     * @param durationList the list with textFields of the duration of a chest
     */
    private void createProceedItems(final int filledInChests,
                                    final ArrayList<TextField> sectionList,
                                    final ArrayList<TextField> durationList) {
        final int skipFour = 5;
        for (int i = 1; i <= filledInChests; i++) {
            Label settings = createSettingsLabel(i);
            Label sections = createSectionsLabel();
            Label targetDuration = createTargetDurationLabel();
            Label seconds = createSecondsLabel();

            TextField sectionField = createSectionField();
            TextField durationField = createDurationField();

            sectionList.add(sectionField);
            durationList.add(durationField);

            int line = (i - 1) * skipFour;
            fillInPane.add(settings, 0, line + skipFour);
            fillInPane.add(sections, 0, line + skipFour + 1);
            fillInPane.add(sectionField, 1, line + skipFour + 1);
            fillInPane.add(targetDuration, 0, line + skipFour + 2);
            fillInPane.add(durationField, 1, line + skipFour + 2);
            fillInPane.add(seconds, 2, line + skipFour + 2);
        }
    }

    /**
     * Creates the settings label for each chest.
     * @param i the ith chest
     * @return the settings label
     */
    private Label createSettingsLabel(final int i) {
        Label settings = new Label("Settings for chest " + i);
        settings.setStyle("-fx-font-weight: bold");
        settings.setFont(font);

        return settings;
    }

    /**
     * Creates the sections label.
     * @return the sections label
     */
    private Label createSectionsLabel() {
        Label sections = new Label("Amount of sections: ");
        sections.setFont(font);

        return sections;
    }

    /**
     * Creates the target duration label.
     * @return the target duration label
     */
    private Label createTargetDurationLabel() {
        Label targetDuration = new Label("The target duration: ");
        targetDuration.setFont(font);

        return targetDuration;
    }

    /**
     * Creates the seconds label.
     * @return the seconds label
     */
    private Label createSecondsLabel() {
        Label seconds = new Label(" sec");
        seconds.setFont(font);

        return seconds;
    }

    /**
     * Creates the section field.
     * @return the section field
     */
    private TextField createSectionField() {
        TextField sectionField = new TextField();
        sectionField.setMaxWidth(maxWidth);

        return sectionField;
    }

    /**
     * Creates the duration field.
     * @return the duration field
     */
    private TextField createDurationField() {
        TextField durationField = new TextField();
        durationField.setMaxWidth(maxWidth);

        return durationField;
    }

    /**
     * Creates the submit button.
     * @param filledInChests the amount of chests filled in
     * @return the submit button
     */
    private Button createSubmitButton(final int filledInChests) {
        Button submit = new Button("Submit");
        submit.setFont(font);

        final int skipThree = 5;
        final int skipFive = 5;

        fillInPane.add(submit, 0, filledInChests * skipThree + skipFive);

        return submit;
    }

    /**
     * Creates the submit error.
     * @param filledInChests the amount of chests filled in
     * @return the submit error label
     */
    private Label createSubmitError(final int filledInChests) {
        Label submitError = new Label("Please fill in a number!");
        submitError.setVisible(false);

        final int skipThree = 5;
        final int skipSix = 6;

        fillInPane.add(submitError, 0, filledInChests * skipThree + skipSix);

        return submitError;
    }

    /**
     * Handles the action after clicking on the submit button.
     * @param sectionList the list with textFields of sections
     * @param durationList the list with textFields of duration
     * @param players the amount of players
     * @param filledInChests the amount of filled in chests
     * @param totalDuration the total duration of the escape room
     * @param submitError the submit error
     */
    private void submitAction(final ArrayList<TextField> sectionList,
                              final ArrayList<TextField> durationList,
                              final int players, final int filledInChests, final int totalDuration,
                              final Label submitError) {
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
            manualStage.setScene(null);
        } catch (NumberFormatException e) {
            submitError.setVisible(true);
        }
    }

}
