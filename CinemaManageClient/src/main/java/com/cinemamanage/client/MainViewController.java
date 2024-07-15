package com.cinemamanage.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainViewController {
    private HallsViewController hallsViewController;

    @FXML
    protected void onHallsButtonClick() throws IOException {
        Stage stage = (Stage) MainApp.getPrimaryStage().getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/com/cinemamanage/client/halls-view.fxml"));
        Parent root = loader.load();
        hallsViewController = loader.getController();
        stage.setScene(new Scene(root));
    }

    @FXML
    protected void onSessionsButtonClick() {
        // Implement navigation to Sessions view
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








//package com.cinemamanage.client;
//
//import com.cinemamanage.models.Hall;
//import com.cinemamanage.models.Session;
//import javafx.fxml.FXML;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.ListView;
//
//import java.io.IOException;
//
//public class MainViewController {
//    @FXML
//    private Label welcomeText;
//
//    @FXML
//    private Button getAllHallsButton;
//
//    @FXML
//    private ListView<String> hallsListView;
//
//    @FXML
//    private Button getAllSessionsButton;
//
//    @FXML
//    private ListView<String> sessionsListView;
//
//    private final CinemaService cinemaService;
//
//    public MainViewController() {
//        Client client = new Client("localhost", 34567);
//        this.cinemaService = new CinemaService(client);
//    }
//
//    @FXML
//    protected void onGetAllHallsButtonClick() {
//        try {
//            cinemaService.fetchAllHalls();
//            hallsListView.getItems().clear();
//            for (Hall hall : cinemaService.getAllHalls().values()) {
//                hallsListView.getItems().add(hall.toString());
//            }
//        } catch (IOException e) {
//            welcomeText.setText("Error: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    @FXML
//    protected void onGetAllSessionsButtonClick() {
//        try {
//            cinemaService.fetchAllSessions();
//            sessionsListView.getItems().clear();
//            for (Session session : cinemaService.getAllSessions().values()) {
//                sessionsListView.getItems().add(session.toString());
//            }
//        } catch (IOException e) {
//            welcomeText.setText("Error: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    public void onClose() {
//        // No need to disconnect manually, as each request opens and closes its own connection
//    }
//
//    public void onSendButtonClick() {
//        // Implement this method if needed
//    }
//}