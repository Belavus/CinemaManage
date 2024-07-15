package com.cinemamanage.client;

import com.cinemamanage.models.Hall;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.Map;

public class HallsViewController {

    @FXML
    private ComboBox<String> hallsComboBox;

    @FXML
    private Label hallDetailsLabel;

    private CinemaService cinemaService;

    @FXML
    public void initialize() {
        try {
            cinemaService = new CinemaService("localhost", 34567);
            fetchAllHalls();
        } catch (IOException e) {
            hallDetailsLabel.setText("Error: Unable to connect to the server.");
            e.printStackTrace();
        }
    }

    @FXML
    protected void onGetAllHallsButtonClick() {
        try {
            fetchAllHalls();
        } catch (IOException e) {
            hallDetailsLabel.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void fetchAllHalls() throws IOException {
        cinemaService.fetchAllHalls();
        hallsComboBox.setItems(FXCollections.observableArrayList(cinemaService.getAllHalls().keySet()));
    }

    @FXML
    protected void onHallSelected() {
        String selectedHallNumber = hallsComboBox.getSelectionModel().getSelectedItem();
        if (selectedHallNumber != null) {
            Hall selectedHall = cinemaService.getAllHalls().get(selectedHallNumber);
            hallDetailsLabel.setText(selectedHall.toString());
        }
    }

    public void onClose() {
        try {
            cinemaService.onClose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
