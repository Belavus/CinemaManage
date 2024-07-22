package com.cinemamanage.client.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class MainViewController {

    @FXML
    private StackPane mainPane;

    @FXML
    private VBox mainContent;

    @FXML
    public void initialize() {
        // Ensure mainPane is not null
        assert mainPane != null : "fx:id=\"mainPane\" was not injected: check your FXML file 'main-view.fxml'.";

        // Add background image
        ImageView backgroundImage = new ImageView(new Image(getClass().getResourceAsStream("/images/background.png")));
        backgroundImage.fitWidthProperty().bind(mainPane.widthProperty());
        backgroundImage.fitHeightProperty().bind(mainPane.heightProperty());
        backgroundImage.setPreserveRatio(false);

        // Add the image to the StackPane
        mainPane.getChildren().add(0, backgroundImage);
    }

    @FXML
    protected void onHallsButtonClick() throws IOException {
        Stage stage = (Stage) mainPane.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cinemamanage/client/halls-view.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
    }

    @FXML
    protected void onSessionsButtonClick() throws IOException {
        Stage stage = (Stage) mainPane.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/com/cinemamanage/client/sessions-view.fxml"));
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
    }

    @FXML
    protected void onBookingsButtonClick() throws IOException {
        Stage stage = (Stage) mainPane.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/com/cinemamanage/client/bookings-view.fxml"));
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
    }

    public void onClose() {
        // Your close logic here
    }
}


//package com.cinemamanage.client.controllers;
//
//import com.cinemamanage.client.MainApp;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.layout.VBox;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//
//public class MainViewController {
//
//    private HallsViewController hallsViewController;
//
//    @FXML
//    protected void onHallsButtonClick() throws IOException {
//        Stage stage = (Stage) MainApp.getPrimaryStage().getScene().getWindow();
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cinemamanage/client/halls-view.fxml"));
//        Parent root = loader.load();
//        hallsViewController = loader.getController();
//        stage.setScene(new Scene(root));
//        stage.centerOnScreen();
//    }
//
//    @FXML
//    protected void onSessionsButtonClick() throws IOException {
//        Stage stage = (Stage) MainApp.getPrimaryStage().getScene().getWindow();
//        Parent root = FXMLLoader.load(getClass().getResource("/com/cinemamanage/client/sessions-view.fxml"));
//        stage.setScene(new Scene(root));
//        stage.centerOnScreen();
//    }
//
//    @FXML
//    protected void onBookingsButtonClick() throws IOException {
//        Stage stage = (Stage) MainApp.getPrimaryStage().getScene().getWindow();
//        Parent root = FXMLLoader.load(getClass().getResource("/com/cinemamanage/client/bookings-view.fxml"));
//        stage.setScene(new Scene(root));
//        stage.centerOnScreen();
//    }
//
//    public void onClose() {
//        if (hallsViewController != null) {
//            hallsViewController.onClose();
//        }
//    }
//}
