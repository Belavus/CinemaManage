package com.cinemamanage.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainViewController {
    private HallsViewController hallsViewController;
    private SessionsViewController sessionsViewController;

    @FXML
    protected void onHallsButtonClick() throws IOException {
        Stage stage = (Stage) MainApp.getPrimaryStage().getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/com/cinemamanage/client/halls-view.fxml"));
        Parent root = loader.load();
        hallsViewController = loader.getController();
        stage.setScene(new Scene(root));
    }

    @FXML
    protected void onSessionsButtonClick() throws IOException {
        Stage stage = (Stage) MainApp.getPrimaryStage().getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cinemamanage/client/sessions-view.fxml"));
        Parent root = loader.load();
        sessionsViewController = loader.getController();
        stage.setScene(new Scene(root));
    }

    @FXML
    protected void onBookingsButtonClick() {
        // Implement navigation to Bookings view
    }

    public void onClose() {
        if (hallsViewController != null) {
            hallsViewController.onClose();
        }
    }
}