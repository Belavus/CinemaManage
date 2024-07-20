package com.cinemamanage.client;

import com.cinemamanage.models.Booking;
import com.cinemamanage.models.Session;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class BookingsViewController {

    @FXML
    private ComboBox<String> movieNameComboBox;

    @FXML
    private ComboBox<String> timeComboBox;

    @FXML
    private ComboBox<String> hallNumberComboBox;

    @FXML
    private TableView<Session> sessionsTableView;

    @FXML
    private TableColumn<Session, String> sessionIdColumn;

    @FXML
    private TableColumn<Session, String> movieNameColumn;

    @FXML
    private TableColumn<Session, String> timeColumn;

    @FXML
    private TableColumn<Session, Integer> hallNumberColumn;

    @FXML
    private TableView<Booking> bookingsTableView;

    @FXML
    private TableColumn<Booking, String> bookingIdColumn;

    @FXML
    private TableColumn<Booking, String> phoneNumberColumn;

    @FXML
    private TableColumn<Booking, String> seatColumn;

    @FXML
    private TextField phoneFilterField;

    private CinemaService cinemaService;
    private ObservableList<Session> allSessions;
    private ObservableList<Booking> allBookings;
    private Session selectedSession;

    @FXML
    public void initialize() {
        try {
            cinemaService = new CinemaService("localhost", 34567);
            initializeTableColumns();
            fetchAllSessions();
            fetchAllBookings();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeTableColumns() {
        sessionIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSessionId()));
        movieNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMovieName()));
        timeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTime()));
        hallNumberColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getHallNumber()).asObject());

        bookingIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBookingId()));
        phoneNumberColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhoneNumber()));
        seatColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSeat().toString()));

        sessionsTableView.setRowFactory(tv -> {
            TableRow<Session> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                    Session rowData = row.getItem();
                    selectedSession = rowData;
                    displayBookingsForSession(rowData);
                }
            });
            return row;
        });
    }

    private void fetchAllSessions() throws IOException {
        cinemaService.fetchAllSessions();
        allSessions = FXCollections.observableArrayList(cinemaService.getAllSessions().values());
        sessionsTableView.setItems(allSessions);

        movieNameComboBox.setItems(FXCollections.observableArrayList(
                allSessions.stream().map(Session::getMovieName).distinct().collect(Collectors.toList())
        ));
        timeComboBox.setItems(FXCollections.observableArrayList(
                allSessions.stream().map(Session::getTime).distinct().collect(Collectors.toList())
        ));
        hallNumberComboBox.setItems(FXCollections.observableArrayList(
                allSessions.stream().map(session -> String.valueOf(session.getHallNumber())).distinct().collect(Collectors.toList())
        ));
    }

    private void fetchAllBookings() throws IOException {
        cinemaService.fetchAllBookings();
        allBookings = FXCollections.observableArrayList(cinemaService.getAllBookings().values());
    }

    private void displayBookingsForSession(Session session) {
        List<Booking> sessionBookings = allBookings.stream()
                .filter(booking -> booking.getSessionId().equals(session.getSessionId()))
                .collect(Collectors.toList());
        bookingsTableView.setItems(FXCollections.observableArrayList(sessionBookings));
    }

    @FXML
    protected void onFilterSessions() {
        String movieName = movieNameComboBox.getValue();
        String time = timeComboBox.getValue();
        String hallNumber = hallNumberComboBox.getValue();

        List<Session> filteredSessions = allSessions.stream().filter(session -> {
            boolean matches = true;
            if (movieName != null && !movieName.isEmpty()) {
                matches = session.getMovieName().equals(movieName);
            }
            if (time != null && !time.isEmpty()) {
                matches = matches && session.getTime().equals(time);
            }
            if (hallNumber != null && !hallNumber.isEmpty()) {
                matches = matches && String.valueOf(session.getHallNumber()).equals(hallNumber);
            }
            return matches;
        }).collect(Collectors.toList());

        sessionsTableView.setItems(FXCollections.observableArrayList(filteredSessions));
    }

    @FXML
    protected void onFilterBookings() {
        String phoneFilter = phoneFilterField.getText().toLowerCase();

        List<Booking> filteredBookings = allBookings.stream().filter(booking ->
                booking.getPhoneNumber().toLowerCase().contains(phoneFilter)
        ).collect(Collectors.toList());

        bookingsTableView.setItems(FXCollections.observableArrayList(filteredBookings));
    }

    @FXML
    protected void onAddBookingButtonClick() {
        if (selectedSession == null) {
            showAlert("Error", "Please select a session before adding a booking.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cinemamanage/client/add-booking-view.fxml"));
            Parent root = loader.load();

            AddBookingViewController controller = loader.getController();
            controller.setCinemaService(cinemaService);
            controller.setSession(selectedSession);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();

            fetchAllBookings(); // Refresh bookings after adding new one
            fetchAllSessions();
            initializeTableColumns();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    protected void onDeleteBookingButtonClick() {
        Booking selectedBooking = bookingsTableView.getSelectionModel().getSelectedItem();
        if (selectedBooking != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Booking");
            alert.setContentText("Are you sure you want to delete this booking?");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        cinemaService.deleteBooking(selectedBooking.getBookingId());
                        allBookings.remove(selectedBooking);
                        bookingsTableView.setItems(allBookings);
                    } catch (IOException e) {
                        showAlert("Error", "Failed to delete the booking.");
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    protected void onBackButtonClick() throws IOException {
        Stage stage = (Stage) sessionsTableView.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/com/cinemamanage/client/main-view.fxml"));
        stage.setScene(new Scene(root));
    }

    public void onClose() {
        try {
            cinemaService.onClose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}