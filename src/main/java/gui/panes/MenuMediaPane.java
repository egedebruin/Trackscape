package gui.panes;

import gui.controllers.MainController;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.util.ArrayList;

/**
 * Class that creates the MenuMediaPane in which the MenuPane and MediaPane are created for the
 * VideoPane.
 */
public class MenuMediaPane {
    /**
     * Class parameters.
     */
    private MainController mainController;
    private MenuPane menuPane;
    private MediaPane mediaPane;
    private FlowPane mediaPlayerPane;

    /**
     * Constructor for MenuMediaPane.
     * @param control the mainController
     * @param inputMediaPlayerPane the mediaPlayerPane
     */
    public MenuMediaPane(final MainController control, final FlowPane inputMediaPlayerPane) {
        this.mainController = control;
        this.mediaPlayerPane = inputMediaPlayerPane;
        mediaPane = new MediaPane(mediaPlayerPane);
        menuPane = new MenuPane(mainController, mediaPane);
    }

    /**
     * createMenuAndMediaPane.
     * Create the Menu pane and mediaPlayer pane
     * @param videoPane pane that includes menu and mediaPlayer
     * @param primaryStage starting stage
     * @return ArrayList
     */
    public ArrayList<Pane> createMenuAndMediaPane(final Pane videoPane,
                                                   final Stage primaryStage) {
        // Add menuPane and mediaPlayerPane to a list
        ArrayList<Pane> menuMediaList = new ArrayList<>();
        menuMediaList.add(menuPane.createMenuPane(videoPane, primaryStage));
        menuMediaList.add(mediaPane.createImageViewerPane());

        return menuMediaList;
    }

    /**
     * Get the menuPane.
     * @return menuPane the pane on which the menu is shown
     */
    public MenuPane getMenuPane() {
        return menuPane;
    }

    /**
     * Get the mediaPane.
     * @return mediaPane the pane on which the media is shown
     */
    public MediaPane getMediaPane() {
        return mediaPane;
    }
}
