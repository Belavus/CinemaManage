package com.cinemamanage.client;

import com.cinemamanage.models.Hall;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class MainViewController {
    @FXML
    private Label welcomeText;

    @FXML
    private Button getAllHallsButton;

    @FXML
    private ListView<String> hallsListView;

    private final Client client;

    public MainViewController() {
        this.client = new Client("localhost", 34567);
    }

    @FXML
    protected void onGetAllHallsButtonClick() {
        Request request = new Request();
        Map<String, String> headers = new HashMap<>();
        headers.put("action", "hall/getAll");
        request.setHeaders(headers);

        try {
            Response response = client.sendRequest(request);
            if ("success".equals(response.getStatus())) {
                Type hallMapType = new TypeToken<Map<String, Hall>>() {}.getType();
                Map<String, Hall> hallMap = client.getGson().fromJson(response.getMessage(), hallMapType);

                hallsListView.getItems().clear();
                for (Hall hall : hallMap.values()) {
                    hallsListView.getItems().add(hall.toString());
                }
            } else {
                welcomeText.setText("Error: " + response.getMessage());
            }
        } catch (IOException e) {
            welcomeText.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void onClose() {
        // No need to disconnect manually, as each request opens and closes its own connection
    }

    public void onSendButtonClick() {
        // Implement this method if needed
    }
}
