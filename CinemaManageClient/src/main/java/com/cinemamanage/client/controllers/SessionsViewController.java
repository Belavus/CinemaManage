package com.cinemamanage.client.controllers;

import com.cinemamanage.client.services.CinemaService;
import com.cinemamanage.client.models.Hall;
import com.cinemamanage.client.models.Seat;
import com.cinemamanage.client.models.Session;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class SessionsViewController {

    @FXML
    private TextField movieNameField;

    @FXML
    private TextField timeField;

    @FXML
    private TextField newDateField;

    @FXML
    private TextField newTimeField;


    @FXML
    private TextField hallNumberField;

    @FXML
    private TextField newMovieNameField;

    @FXML
    private TextField newDurationField;

    @FXML
    private ComboBox<String> newHallComboBox;

    @FXML
    private Button addNewSessionButton;

    @FXML
    private TableView<Session> sessionsTableView;

    @FXML
    private TableColumn<Session, String> sessionIdColumn;

    @FXML
    private TableColumn<Session, String> movieNameColumn;

    @FXML
    private TableColumn<Session, String> timeColumn;

    @FXML
    private TableColumn<Session, Integer> durationColumn;

    @FXML
    private TableColumn<Session, Integer> hallNumberColumn;

    @FXML
    private GridPane hallLayoutGrid;

    private CinemaService cinemaService;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private ObservableList<Session> allSessions;

    private Session selectedSession;

    @FXML
    public void initialize() {
        try {
            cinemaService = new CinemaService("localhost", 34567);
            initializeTableColumns();
            fetchAllSessions();
            fetchAllHalls();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeTableColumns() {
        sessionIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSessionId()));
        movieNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMovieName()));
        timeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTime()));
        durationColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getDuration()).asObject());
        hallNumberColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getHallNumber()).asObject());

        sessionsTableView.setRowFactory(tv -> {
            TableRow<Session> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                    Session rowData = row.getItem();
                    displaySession(rowData);
                }
            });
            row.setOnContextMenuRequested(event -> showContextMenu(event, row));
            return row;
        });
    }

    private void fetchAllSessions() throws IOException {
        cinemaService.fetchAllSessions();
        allSessions = FXCollections.observableArrayList(cinemaService.getAllSessions().values());
        sessionsTableView.setItems(allSessions);
    }

    private void fetchAllHalls() throws IOException {
        cinemaService.fetchAllHalls();
        newHallComboBox.setItems(FXCollections.observableArrayList(cinemaService.getAllHalls().keySet()));
    }

    private void displaySession(Session session) {
        hallLayoutGrid.getChildren().clear();
        Hall hall = cinemaService.getAllHalls().get(String.valueOf(session.getHallNumber()));
        if (hall != null) {
            int[][] layout = hall.getLayout();
            for (int i = 0; i < layout.length; i++) {
                for (int j = 0; j < layout[i].length; j++) {
                    Pane cell = createCell(layout[i][j], i, j, session.getSeats());
                    hallLayoutGrid.add(cell, j, i);
                }
            }
        }
    }

    private Pane createCell(int value, int row, int column, List<Seat> bookedSeats) {
        Pane cell = new Pane();
        cell.setPrefSize(30, 30);
        Rectangle rectangle = new Rectangle(30, 30);
        if (isSeatBooked(bookedSeats, row, column)) {
            rectangle.setFill(Color.RED);
        } else {
            rectangle.setFill(getColorForValue(value));
        }
        cell.getChildren().add(rectangle);
        return cell;
    }

    private boolean isSeatBooked(List<Seat> bookedSeats, int row, int column) {
        return bookedSeats.stream().anyMatch(seat -> seat.getRow() == row && seat.getColumn() == column);
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
    protected void onFilterButtonClick() {
        String movieName = movieNameField.getText().toLowerCase();
        String time = timeField.getText();
        String hallNumber = hallNumberField.getText();

        List<Session> filteredSessions = allSessions.stream().filter(session -> {
            boolean matches = true;
            if (!movieName.isEmpty()) {
                matches = session.getMovieName().toLowerCase().contains(movieName);
            }
            if (!time.isEmpty()) {
                matches = matches && session.getTime().contains(time);
            }
            if (!hallNumber.isEmpty()) {
                matches = matches && Integer.toString(session.getHallNumber()).equals(hallNumber);
            }
            return matches;
        }).collect(Collectors.toList());

        sessionsTableView.setItems(FXCollections.observableArrayList(filteredSessions));
    }

    @FXML
    protected void onAddNewSessionButtonClick() {
        String movieName = newMovieNameField.getText();
        String date = newDateField.getText();
        String time = newTimeField.getText();
        String durationStr = newDurationField.getText();
        String hallNumberStr = newHallComboBox.getSelectionModel().getSelectedItem();

        if (movieName.isEmpty() || date.isEmpty() || time.isEmpty() || durationStr.isEmpty() || hallNumberStr == null) {
            showAlert("Error", "All fields must be filled.");
            return;
        }

        LocalDateTime startTime;
        try {
            startTime = LocalDateTime.parse(date + " " + time, dateTimeFormatter);
        } catch (Exception e) {
            showAlert("Error", "Invalid date or time format.");
            return;
        }

        int duration = Integer.parseInt(durationStr);
        int hallNumber = Integer.parseInt(hallNumberStr);

        if (isSessionOverlapping(hallNumber, startTime, duration)) {
            showAlert("Error", "The session time overlaps with an existing session in the same hall.");
            return;
        }

        String sessionId = (selectedSession == null) ? generateNewSessionId() : selectedSession.getSessionId();
        Session newSession = new Session(sessionId, movieName, startTime.format(dateTimeFormatter), duration, hallNumber);
        try {
            if (selectedSession == null) {
                cinemaService.addSession(newSession);
                allSessions.add(newSession);
            } else {
                cinemaService.addSession(newSession);
                int index = allSessions.indexOf(selectedSession);
                allSessions.set(index, newSession);
                selectedSession = null;
                addNewSessionButton.setText("Add New Session");
            }
            sessionsTableView.setItems(allSessions);
            clearSessionFields();
        } catch (IOException e) {
            showAlert("Error", "Failed to add the new session.");
            e.printStackTrace();
        }
    }

    private boolean isSessionOverlapping(int hallNumber, LocalDateTime startTime, int duration) {
        LocalDateTime endTime = startTime.plusMinutes(duration);
        return allSessions.stream().anyMatch(session -> {
            if (session.getHallNumber() == hallNumber) {
                LocalDateTime sessionStart = LocalDateTime.parse(session.getTime(), dateTimeFormatter);
                LocalDateTime sessionEnd = sessionStart.plusMinutes(session.getDuration());
                return startTime.isBefore(sessionEnd) && endTime.isAfter(sessionStart);
            }
            return false;
        });
    }

    private String generateNewSessionId() {
        int maxId = allSessions.stream().mapToInt(session -> Integer.parseInt(session.getSessionId())).max().orElse(0);
        return String.valueOf(maxId + 1);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showContextMenu(ContextMenuEvent event, TableRow<Session> row) {
        if (!row.isEmpty()) {
            ContextMenu contextMenu = new ContextMenu();

//            MenuItem editItem = new MenuItem("Edit");
//            editItem.setOnAction(e -> onEditSession(row.getItem()));

            MenuItem deleteItem = new MenuItem("Delete");
            deleteItem.setOnAction(e -> onDeleteSession(row.getItem()));

            contextMenu.getItems().addAll(/*editItem,*/ deleteItem);
            contextMenu.show(row, event.getScreenX(), event.getScreenY());
        }
    }

    private void onEditSession(Session session) {
        selectedSession = session;
        newMovieNameField.setText(session.getMovieName());
        newTimeField.setText(session.getTime());
        newDurationField.setText(String.valueOf(session.getDuration()));
        newHallComboBox.getSelectionModel().select(String.valueOf(session.getHallNumber()));
        addNewSessionButton.setText("Save Changes");
    }

    private void onDeleteSession(Session session) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Session");
        alert.setContentText("Are you sure you want to delete this session?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    cinemaService.deleteSession(session.getSessionId());
                    allSessions.remove(session);
                    sessionsTableView.setItems(allSessions);
                } catch (IOException e) {
                    showAlert("Error", "Failed to delete the session.");
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    protected void onBackButtonClick() throws IOException {
        Stage stage = (Stage) sessionsTableView.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/com/cinemamanage/client/main-view.fxml"));
        stage.setScene(new Scene(root));
    }

    private void clearSessionFields() {
        newMovieNameField.clear();
        newDateField.clear();
        newTimeField.clear();
        newDurationField.clear();
        newHallComboBox.getSelectionModel().clearSelection();
    }


    public void onClose() {
        try {
            cinemaService.onClose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
