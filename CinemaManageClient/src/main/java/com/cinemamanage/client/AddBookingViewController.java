package com.cinemamanage.client;

import com.cinemamanage.models.Booking;
import com.cinemamanage.models.Hall;
import com.cinemamanage.models.Seat;
import com.cinemamanage.models.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddBookingViewController {

    @FXML
    private GridPane hallLayoutGrid;

    @FXML
    private TextField phoneNumberField;

    private CinemaService cinemaService;
    private Session session;
    private List<Seat> selectedSeats = new ArrayList<>();

    public void setCinemaService(CinemaService cinemaService) {
        this.cinemaService = cinemaService;
    }

    public void setSession(Session session) {
        this.session = session;
        displayHallLayout();
    }

    private void displayHallLayout() {
        hallLayoutGrid.getChildren().clear();
        Hall hall = cinemaService.getAllHalls().get(String.valueOf(session.getHallNumber()));
        if (hall != null) {
            int[][] layout = hall.getLayout();
            for (int i = 0; i < layout.length; i++) {
                for (int j = 0; j < layout[i].length; j++) {
                    Pane cell = createCell(layout[i][j], i, j);
                    hallLayoutGrid.add(cell, j, i);
                }
            }
        }
    }

    private Pane createCell(int value, int row, int column) {
        Pane cell = new Pane();
        cell.setPrefSize(30, 30);
        Rectangle rectangle = new Rectangle(30, 30);
        rectangle.setFill(getColorForValue(value));
        cell.getChildren().add(rectangle);
        cell.setOnMouseClicked(event -> onCellClicked(row, column));
        return cell;
    }

    private void onCellClicked(int row, int column) {
        Seat seat = new Seat(row, column);
        if (selectedSeats.contains(seat)) {
            selectedSeats.remove(seat);
        } else {
            selectedSeats.add(seat);
        }
        displayHallLayout(); // Обновить отображение зала
    }

    private Color getColorForValue(int value) {
        switch (value) {
            case Hall.EMPTY: return Color.WHITE;
            case Hall.OCCUPIED: return Color.RED;
            case Hall.EMPTY_SPACE: return Color.BLACK;
            case Hall.VIP: return Color.BLUE;
            case Hall.ACCESSIBLE: return Color.YELLOW;
            default: return Color.GRAY;
        }
    }

    @FXML
    protected void onBookButtonClick() {
        String phoneNumber = phoneNumberField.getText();
        if (phoneNumber.isEmpty() || selectedSeats.isEmpty()) {
            showAlert("Error", "Phone number and at least one seat must be selected.");
            return;
        }

        try {
            for (Seat seat : selectedSeats) {
                String bookingId = generateNewBookingId();
                Booking newBooking = new Booking(bookingId, session.getSessionId(), seat, phoneNumber);
                cinemaService.addBooking(newBooking);
            }
            Stage stage = (Stage) hallLayoutGrid.getScene().getWindow();
            stage.close(); // Закрыть окно после бронирования
        } catch (IOException e) {
            showAlert("Error", "Failed to add the booking.");
            e.printStackTrace();
        }
    }

    private String generateNewBookingId() {
        // Сгенерировать новый bookingId
        int maxId = cinemaService.getAllBookings().values().stream().mapToInt(booking -> Integer.parseInt(booking.getBookingId())).max().orElse(0);
        return String.valueOf(maxId + 1);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
