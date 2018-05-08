package Gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("TrackScape");

        GridPane gridPane = new GridPane();
        gridPane.setMinSize(500, 500);
        gridPane.setPadding(new Insets(40, 10, 10, 10));
        gridPane.setVgap(20);
        gridPane.setHgap(20);
        gridPane.setAlignment(Pos.TOP_CENTER);

        // Title of application
        Text text = new Text("TrackScape");
        text.setFont(Font.font("Edwardian Script ITC", 80));
        text.setFill(Color.BLACK);
        text.setStroke(Color.BLACK);
        text.setStrokeWidth(3);
        //text.setTextAlignment(TextAlignment.CENTER);

        // Upload video button
        Button upload = new Button("Upload Video");
        upload.setTextAlignment(TextAlignment.CENTER);
        upload.setAlignment(Pos.BASELINE_CENTER);
        upload.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");

        gridPane.add(text, 0, 0);
        gridPane.add(upload, 0, 25);

        //StackPane root = new StackPane(text, upload);
        Scene scene = new Scene(gridPane,1250, 800);
        scene.getStylesheets().add("stylesheet.css");
        scene.setFill(Color.OLIVE);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
