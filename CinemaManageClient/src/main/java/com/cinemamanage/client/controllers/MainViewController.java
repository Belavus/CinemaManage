package com.cinemamanage.client.controllers;

import com.cinemamanage.client.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainViewController {

    @FXML
    public void initialize() {
    }

    @FXML
    protected void onHallsButtonClick() throws IOException {
        Stage stage = (Stage) MainApp.getPrimaryStage().getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cinemamanage/client/halls-view.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
    }

    @FXML
    protected void onSessionsButtonClick() throws IOException {
        Stage stage = (Stage) MainApp.getPrimaryStage().getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/com/cinemamanage/client/sessions-view.fxml"));
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
    }

    @FXML
    protected void onBookingsButtonClick() throws IOException {
        Stage stage = (Stage) MainApp.getPrimaryStage().getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/com/cinemamanage/client/bookings-view.fxml"));
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
    }

    public void onClose() {
    }
}