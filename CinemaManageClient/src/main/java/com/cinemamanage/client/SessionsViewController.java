package com.cinemamanage.client;

import com.cinemamanage.models.Hall;
import com.cinemamanage.models.Seat;
import com.cinemamanage.models.Session;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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
    private TextField hallNumberField;

    @FXML
    private TextField newMovieNameField;

    @FXML
    private TextField newTimeField;

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
    private TableColumn<Session, Integer> hallNumberColumn;

    @FXML
    private GridPane hallLayoutGrid;

    private CinemaService cinemaService;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private ObservableList<Session> allSessions;

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
        hallNumberColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getHallNumber()).asObject());

        sessionsTableView.setRowFactory(tv -> {
            TableRow<Session> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    Session rowData = row.getItem();
                    displaySession(rowData);
                }
            });
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
        String time = newTimeField.getText();
        String hallNumberStr = newHallComboBox.getSelectionModel().getSelectedItem();
        if (movieName.isEmpty() || time.isEmpty() || hallNumberStr == null) {
            showAlert("Error", "All fields must be filled.");
            return;
        }
        try {
            LocalDateTime.parse(time, dateTimeFormatter);
        } catch (Exception e) {
            showAlert("Error", "Time format must be yyyy-MM-dd HH:mm.");
            return;
        }

        int hallNumber = Integer.parseInt(hallNumberStr);
        String sessionId = generateSessionId();
        Session newSession = new Session(sessionId, movieName, time, hallNumber);

        try {
            cinemaService.addSession(newSession);
            fetchAllSessions();
        } catch (IOException e) {
            showAlert("Error", "Failed to add session: " + e.getMessage());
        }
    }

    private String generateSessionId() {
        int maxId = cinemaService.getAllSessions().keySet().stream()
                .map(id -> id.replaceAll("\\D", ""))
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(0);
        return String.format("S%03d", maxId + 1);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    protected void onBackButtonClick() throws IOException {
        Stage stage = (Stage) sessionsTableView.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("main-view.fxml"));
        stage.setScene(new Scene(root));
    }
}
