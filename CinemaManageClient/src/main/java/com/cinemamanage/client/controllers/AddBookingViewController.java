package com.cinemamanage.client.controllers;

import com.cinemamanage.client.services.CinemaService;
import com.cinemamanage.client.models.Booking;
import com.cinemamanage.client.models.Hall;
import com.cinemamanage.client.models.Seat;
import com.cinemamanage.client.models.Session;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddBookingViewController {

    @FXML
    private GridPane hallLayoutGrid;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private TextField numberOfPeopleField;

    @FXML
    private TextField distanceField;

    @FXML
    private ComboBox<String> algorithmComboBox;

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

        // Highlight already booked seats
        if (isSeatBooked(row, column)) {
            rectangle.setFill(Color.RED);
        }

        // Highlight already chosen seats
        if (isSeatChosen(row, column)) {
            rectangle.setFill(Color.GREEN);
        }

        cell.getChildren().add(rectangle);
        cell.setOnMouseClicked(event -> onCellClicked(row, column, rectangle, value));
        return cell;
    }

    private boolean isSeatBooked(int row, int column) {
        return session.getSeats().stream().anyMatch(seat -> seat.getRow() == row && seat.getColumn() == column);
    }

    private boolean isSeatChosen(int row, int column) {
        return selectedSeats.stream().anyMatch(seat -> seat.getRow() == row && seat.getColumn() == column);
    }

    private void onCellClicked(int row, int column, Rectangle rectangle, int value) {
        if (value == Hall.EMPTY_SPACE) {
            showAlert("Error", "Cannot book a seat marked as empty space.");
            return;
        }

        Seat seat = new Seat(row, column);
        if (selectedSeats.contains(seat)) {
            selectedSeats.remove(seat);
            rectangle.setFill(getColorForValue(Hall.EMPTY)); // Return to original color
        } else {
            if (isSeatBooked(row, column)) {
                showAlert("Error", "Seat is already booked.");
            } else {
                selectedSeats.add(seat);
                rectangle.setFill(Color.GREEN); // Highlight selected seat
            }
        }
    }

    private Color getColorForValue(int value) {
        switch (value) {
            case Hall.EMPTY:
                return Color.WHITE;
            case Hall.OCCUPIED:
                return Color.RED;
            case Hall.EMPTY_SPACE:
                return Color.BLACK;
            case Hall.VIP:
                return Color.BLUE;
            case Hall.ACCESSIBLE:
                return Color.YELLOW;
            default:
                return Color.GRAY;
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
            stage.close(); // Close window after booking
        } catch (IOException e) {
            showAlert("Error", "Failed to add the booking.");
            e.printStackTrace();
        }
    }

    private String generateNewBookingId() {
        int maxId = cinemaService.getAllBookings().values().stream().mapToInt(booking -> Integer.parseInt(booking.getBookingId())).max().orElse(0);
        return String.valueOf(maxId + 1);
    }

    @FXML
    protected void onGenerateSeatsButtonClick() {
        String algorithm = algorithmComboBox.getValue();
        String numberOfPeopleStr = numberOfPeopleField.getText();
        String distanceStr = distanceField.getText();

        if (algorithm == null || numberOfPeopleStr.isEmpty() || distanceStr.isEmpty()) {
            showAlert("Error", "All fields must be filled.");
            return;
        }

        try {
            int numberOfPeople = Integer.parseInt(numberOfPeopleStr);
            int distance = Integer.parseInt(distanceStr);

            List<Seat> generatedSeats = cinemaService.generateSeats(session.getSessionId(), numberOfPeople, distance, algorithm);

            selectedSeats.clear();
            selectedSeats.addAll(generatedSeats);
            displayHallLayout(); // Refresh the layout to show the selected seats
            if (selectedSeats.size() < numberOfPeople) {
                showAlert("Warning", "The number of requested seats exceeds the hall's capacity! " + (numberOfPeople - selectedSeats.size()) + " seats could not be selected. Please try a different number or add seats manually!");
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Number of people and distance must be integers.");
        } catch (IOException e) {
            showAlert("Error", "Failed to generate seats.");
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}