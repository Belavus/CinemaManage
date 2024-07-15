package com.cinemamanage.client;

import com.cinemamanage.models.Hall;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HallsViewController implements Initializable {
    @FXML
    private Label welcomeText;

    @FXML
    private Button getAllHallsButton;

    @FXML
    private ListView<String> hallsListView;

    private CinemaService cinemaService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            this.cinemaService = new CinemaService("localhost", 34567);
            welcomeText.setText("Connected to server.");
        } catch (IOException e) {
            welcomeText.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    protected void onGetAllHallsButtonClick() {
        try {
            cinemaService.fetchAllHalls();
            hallsListView.getItems().clear();
            for (Hall hall : cinemaService.getAllHalls().values()) {
                hallsListView.getItems().add(hall.toString());
            }
        } catch (IOException e) {
            welcomeText.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void onClose() {
        try {
            if (cinemaService != null) {
                cinemaService.onClose();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
