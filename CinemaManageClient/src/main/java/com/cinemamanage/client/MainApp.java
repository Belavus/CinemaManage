package com.cinemamanage.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        MainApp.primaryStage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cinemamanage/client/main-view.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Cinema Management");
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            MainViewController controller = loader.getController();
            controller.onClose();
        });
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
