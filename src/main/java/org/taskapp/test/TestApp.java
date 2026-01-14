package org.taskapp.test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TestApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        System.out.println("JavaFX Application Starting...");

        Label label = new Label("JavaFX is working!");
        Button button = new Button("Click Me");
        button.setOnAction(e -> label.setText("Button clicked!"));

        VBox root = new VBox(10, label, button);
        Scene scene = new Scene(root, 300, 200);

        primaryStage.setTitle("Test JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();

        System.out.println("JavaFX Application Started!");
    }

    public static void main(String[] args) {
        System.out.println("Launching JavaFX...");
        launch(args);
    }
}