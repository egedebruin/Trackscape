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
import java.util.List;

/**
 * Class that creates the ManualConfigPane in a new Scene.
 */
class ManualConfigPane {
    private VideoController videoController;
    private RoomController roomController;
    private Stage manualStage;
    private GridPane fillInPane;
    private Font font;
    private final int maxWidth = 60;
    private static final int MENU_ITEMS_PER_CHEST = 5;

    /**
     * Constructor for ManualConfigPane.
     * @param vControl the videoController
     * @param rControl the roomController
     */
    ManualConfigPane(final VideoController vControl, final RoomController rControl) {
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
    void createManualConfig(final MenuItem manual) {
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

        createChestOrPlayerLine(playerField, false);
        createChestOrPlayerLine(chestField, true);
        createTotalDurationLine(totalDurationField);

        Button proceed = new Button("Proceed");
        proceed.setFont(font);

        proceedSetUp(playerField, chestField, totalDurationField, proceed);

        final int rowIndex = 3;
        fillInPane.add(proceed, 0, rowIndex);
    }

    /**
     * Adds the fields to the pane to fill in the amount of chests/players.
     * @param objectField the field in which the amount of chests/players need to be filled in
     * @param isChest boolean value that indicates whether textfield is from chests or players.
     */
    private void createChestOrPlayerLine(final TextField objectField, final boolean isChest) {
        objectField.setMaxWidth(maxWidth);
        if (isChest) {
            final Label objects = createLabel("Amount of chests: ", false);
            fillInPane.add(objects, 0, 1);
            fillInPane.add(objectField, 1, 1);
        } else {
            final Label players = createLabel("Amount of players: ", false);
            fillInPane.add(players, 0, 0);
            fillInPane.add(objectField, 1, 0);
        }
    }

    /**
     * Adds the field to the pane to fill in the total duration of the escape room.
     * @param totalDurationField the field in which the total duration need to be filled in
     */
    private void createTotalDurationLine(final TextField totalDurationField) {
        final Label totalDuration = createLabel("Total duration of the escape room: ", false);
        final Label seconds = createLabel(" sec", false);

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

                proceed.setText("Adjust Settings");
                proceedAction(chestField, playerField, totalDurationField, error);
            } else {
                error.setText("Please fill in all fields.");
                error.setVisible(true);
            }
        });
    }

    /**
     * Checks if a integer in a list is negative.
     * @param numbers a list of integers
     * @param error the label that shows an error if an int is negative
     * @return true iff at least one int is negative.
     */
    private boolean checkForNegativeNumbersSubmit(final List<Integer> numbers, final Label error) {
        for (int i : numbers) {
            if (i < 0) {
                error.setText("Please fill in positive numbers only.");
                error.setVisible(true);
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if all numbers that are filled in are positive.
     * @param chests amount of chests as was filled in
     * @param players amount of players as was filled in
     * @param totalDuration total duration as was filled in
     * @param error the label that shows the error if necessary
     * @return true iff one or more numbers are < 0
     */
    private boolean checkForNegativeNumbersProceed(final int chests, final int players,
                                                   final int totalDuration, final Label error) {
        if (chests < 0 || players < 0 || totalDuration < 0) {
            error.setText("Please fill in positive numbers only.");
            error.setVisible(true);
            return true;
        }
        return false;
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
     * Handles the action after clicking on the proceed button.
     * @param chestField the field in which the amount of chests is filled in
     * @param playerField the field in which the amount of players is filled in
     * @param totalDurationField the field in which the total duration is filled in
     * @param error the fixed error message label
     */
    private void proceedAction(final TextField chestField, final TextField playerField,
                              final TextField totalDurationField, final Label error) {
        try {
            int filledInChests = Integer.parseInt(chestField.getText());
            int players = Integer.parseInt(playerField.getText().trim());
            int totalDuration = Integer.parseInt(totalDurationField.getText().trim());

            if (!checkForNegativeNumbersProceed(filledInChests, players, totalDuration, error)) {

                error.setVisible(false);

                ArrayList<TextField> sectionList = new ArrayList<>();
                ArrayList<TextField> durationList = new ArrayList<>();

                createProceedItems(filledInChests, sectionList, durationList);

                Button submit = createSubmitButton(filledInChests);
                Label submitError = createSubmitError(filledInChests);

                submit.setOnAction(t2 -> submitAction(sectionList, durationList, players,
                        filledInChests, totalDuration, submitError));
            }
        } catch (NumberFormatException e) {
            error.setText("Please fill in numbers only.");
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
        for (int i = 1; i <= filledInChests; i++) {
            Label settings = createLabel("Settings for chest " + i, true);
            Label sections = createLabel("Amount of sections: ", false);
            Label targetDuration = createLabel("The target duration: ", false);
            Label seconds = createLabel(" sec", false);

            TextField sectionField = createTextField();
            TextField durationField = createTextField();

            sectionList.add(sectionField);
            durationList.add(durationField);

            int line = (i - 1) * MENU_ITEMS_PER_CHEST;
            fillInPane.add(settings, 0, line + MENU_ITEMS_PER_CHEST);
            fillInPane.add(sections, 0, line + MENU_ITEMS_PER_CHEST + 1);
            fillInPane.add(sectionField, 1, line + MENU_ITEMS_PER_CHEST + 1);
            fillInPane.add(targetDuration, 0, line + MENU_ITEMS_PER_CHEST + 2);
            fillInPane.add(durationField, 1, line + MENU_ITEMS_PER_CHEST + 2);
            fillInPane.add(seconds, 2, line + MENU_ITEMS_PER_CHEST + 2);
        }
    }

    /**
     * Create a label with the given text.
     * @param labelString the text for the label
     * @param bold if the text needs to be bold
     * @return the label
     */
    private Label createLabel(final String labelString, final boolean bold) {
        Label label = new Label(labelString);
        label.setFont(font);

        if (bold) {
            label.setStyle("-fx-font-size: 16pt;"
                + "-fx-font-weight: bold");
        }

        return label;
    }

    /**
     * Creates the text field.
     * @return the text field
     */
    private TextField createTextField() {
        TextField sectionField = new TextField();
        sectionField.setMaxWidth(maxWidth);
        return sectionField;
    }

    /**
     * Creates the submit button.
     * @param filledInChests the amount of chests filled in
     * @return the submit button
     */
    private Button createSubmitButton(final int filledInChests) {
        Button submit = new Button("Submit");
        submit.setFont(font);

        fillInPane.add(submit, 0, filledInChests * MENU_ITEMS_PER_CHEST + MENU_ITEMS_PER_CHEST);

        return submit;
    }

    /**
     * Creates a fixed error message.
     * @return the error label
     */
    private Label createFixedErrorMessage() {
        Label error = createErrorLabel();

        final int rowIndex = 4;
        fillInPane.add(error, 0, rowIndex);

        return error;
    }

    /**
     * Creates the submit error.
     * @param filledInChests the amount of chests filled in
     * @return the submit error label
     */
    private Label createSubmitError(final int filledInChests) {
        Label submitError = createErrorLabel();

        fillInPane.add(submitError, 0,
                filledInChests * MENU_ITEMS_PER_CHEST + MENU_ITEMS_PER_CHEST + 1);

        return submitError;
    }

    /**
     * Create an error label.
     * @return the error label
     */
    private Label createErrorLabel() {
        Label errorLabel = new Label("Please fill in all fields.");
        errorLabel.setVisible(false);
        return errorLabel;
    }

    /**
     * Handles the action after clicking on the submit button.
     * @param sectionList the list with textFields of sections
     * @param durationList the list with textFields of duration
     * @param players the amount of players
     * @param chests the amount of filled in chests
     * @param totalDuration the total duration of the escape room in minutes
     * @param submitError the submit error
     */
    private void submitAction(final ArrayList<TextField> sectionList,
                              final ArrayList<TextField> durationList,
                              final int players, final int chests, final int totalDuration,
                              final Label submitError) {
        try {
            ArrayList<Integer> sectionIntList = new ArrayList<>();
            ArrayList<Integer> durationIntList = new ArrayList<>();
            for (int i = 0; i < sectionList.size(); i++) {

                // Checks if all fields are filled in.
                if (sectionList.get(i).getText().isEmpty()
                        || durationList.get(i).getText().isEmpty()) {
                    submitError.setText("Please fill in all fields.");
                    submitError.setVisible(true);
                    return;
                }

                sectionIntList.add(Integer.parseInt(sectionList.get(i).getText().trim()));
                durationIntList.add(Integer.parseInt(durationList.get(i).getText().trim()));
            }

            // Check if only positive numbers are filled in
            if (!(checkForNegativeNumbersSubmit(sectionIntList, submitError)
                    || checkForNegativeNumbersSubmit(durationIntList, submitError))) {

                roomController.manualConfig(players, chests,
                        totalDuration, sectionIntList, durationIntList);
                manualStage.close();
                manualStage.setScene(null);
            }
        } catch (NumberFormatException e) {
            submitError.setText("Please fill in a number in each field.");
            submitError.setVisible(true);
        }
    }
}

