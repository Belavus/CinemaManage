package com.cinemamanage.client;

import com.cinemamanage.models.Hall;
import com.cinemamanage.models.Seat;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SessionsViewController {

    @FXML
    private TextField movieNameField;

    @FXML
    private TextField timeField;

    @FXML
    private TextField hallNumberField;

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

    @FXML
    public void initialize() {
        try {
            cinemaService = new CinemaService("localhost", 34567);
            initializeTableColumns();
            fetchAllSessions();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeTableColumns() {
        sessionIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSessionId()));
        movieNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMovieName()));
        timeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTime()));
        hallNumberColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getHallNumber()).asObject());
    }

    private void fetchAllSessions() throws IOException {
        cinemaService.fetchAllSessions();
        sessionsTableView.setItems(FXCollections.observableArrayList(cinemaService.getAllSessions().values()));
    }

    @FXML
    protected void onFilterButtonClick() {
        String movieName = movieNameField.getText();
        String time = timeField.getText();
        String hallNumberText = hallNumberField.getText();
        Integer hallNumber = hallNumberText.isEmpty() ? null : Integer.parseInt(hallNumberText);

        List<Session> filteredSessions = cinemaService.getAllSessions().values().stream()
                .filter(session -> (movieName.isEmpty() || session.getMovieName().equalsIgnoreCase(movieName)) &&
                        (time.isEmpty() || session.getTime().equalsIgnoreCase(time)) &&
                        (hallNumber == null || session.getHallNumber() == hallNumber))
                .collect(Collectors.toList());

        sessionsTableView.setItems(FXCollections.observableArrayList(filteredSessions));
    }

    @FXML
    protected void onSessionSelected(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            Session selectedSession = sessionsTableView.getSelectionModel().getSelectedItem();
            if (selectedSession != null) {
                displaySessionLayout(selectedSession);
            }
        }
    }

    private void displaySessionLayout(Session session) {
        hallLayoutGrid.getChildren().clear();
        Hall hall = cinemaService.getAllHalls().get(String.valueOf(session.getHallNumber()));
        int[][] layout = hall.getLayout();

        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout[i].length; j++) {
                Pane cell = createCell(layout[i][j], i, j);
                hallLayoutGrid.add(cell, j, i);
            }
        }

        for (Seat seat : session.getSeats()) {
            int row = seat.getRow();
            int col = seat.getColumn();
            Pane cell = (Pane) hallLayoutGrid.getChildren().get(row * layout[0].length + col);
            Rectangle rectangle = (Rectangle) cell.getChildren().get(0);
            rectangle.setFill(Color.RED); // Mark occupied seats
        }
    }

    private Pane createCell(int value, int row, int column) {
        Pane cell = new Pane();
        cell.setPrefSize(30, 30);
        Rectangle rectangle = new Rectangle(30, 30);
        rectangle.setFill(getColorForValue(value));
        cell.getChildren().add(rectangle);
        return cell;
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
    protected void onBackButtonClick() throws IOException {
        Stage stage = (Stage) hallLayoutGrid.getScene().getWindow();
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


//package com.cinemamanage.client;
//
//import com.cinemamanage.models.Hall;
//import com.cinemamanage.models.Seat;
//import com.cinemamanage.models.Session;
//import javafx.collections.FXCollections;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.input.MouseButton;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.layout.GridPane;
//import javafx.scene.layout.Pane;
//import javafx.scene.paint.Color;
//import javafx.scene.shape.Rectangle;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//public class SessionsViewController {
//
//    @FXML
//    private TextField movieNameField;
//
//    @FXML
//    private TextField timeField;
//
//    @FXML
//    private TextField hallNumberField;
//
//    @FXML
//    private ListView<Session> sessionsListView;
//
//    @FXML
//    private GridPane hallLayoutGrid;
//
//    private CinemaService cinemaService;
//
//    @FXML
//    public void initialize() {
//        try {
//            cinemaService = new CinemaService("localhost", 34567);
//            fetchAllSessions();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void fetchAllSessions() throws IOException {
//        cinemaService.fetchAllSessions();
//        sessionsListView.setItems(FXCollections.observableArrayList(cinemaService.getAllSessions().values()));
//    }
//
//    @FXML
//    protected void onFilterButtonClick() {
//        String movieName = movieNameField.getText();
//        String time = timeField.getText();
//        String hallNumberText = hallNumberField.getText();
//        Integer hallNumber = hallNumberText.isEmpty() ? null : Integer.parseInt(hallNumberText);
//
//        List<Session> filteredSessions = cinemaService.getAllSessions().values().stream()
//                .filter(session -> (movieName.isEmpty() || session.getMovieName().equalsIgnoreCase(movieName)) &&
//                        (time.isEmpty() || session.getTime().equalsIgnoreCase(time)) &&
//                        (hallNumber == null || session.getHallNumber() == hallNumber))
//                .collect(Collectors.toList());
//
//        sessionsListView.setItems(FXCollections.observableArrayList(filteredSessions));
//    }
//
//    @FXML
//    protected void onSessionSelected(MouseEvent event) {
//        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
//            Session selectedSession = sessionsListView.getSelectionModel().getSelectedItem();
//            if (selectedSession != null) {
//                displaySessionLayout(selectedSession);
//            }
//        }
//    }
//
//    private void displaySessionLayout(Session session) {
//        hallLayoutGrid.getChildren().clear();
//        Hall hall = cinemaService.getAllHalls().get(String.valueOf(session.getHallNumber()));
//        int[][] layout = hall.getLayout();
//
//        for (int i = 0; i < layout.length; i++) {
//            for (int j = 0; j < layout[i].length; j++) {
//                Pane cell = createCell(layout[i][j], i, j);
//                hallLayoutGrid.add(cell, j, i);
//            }
//        }
//
//        for (Seat seat : session.getSeats()) {
//            int row = seat.getRow();
//            int col = seat.getColumn();
//            Pane cell = (Pane) hallLayoutGrid.getChildren().get(row * layout[0].length + col);
//            Rectangle rectangle = (Rectangle) cell.getChildren().get(0);
//            rectangle.setFill(Color.RED); // Mark occupied seats
//        }
//    }
//
//    private Pane createCell(int value, int row, int column) {
//        Pane cell = new Pane();
//        cell.setPrefSize(30, 30);
//        Rectangle rectangle = new Rectangle(30, 30);
//        rectangle.setFill(getColorForValue(value));
//        cell.getChildren().add(rectangle);
//        return cell;
//    }
//
//    private Color getColorForValue(int value) {
//        switch (value) {
//            case Hall.EMPTY: return Color.WHITE;
//            case Hall.OCCUPIED: return Color.RED;
//            case Hall.EMPTY_SPACE: return Color.BLACK;
//            case Hall.VIP: return Color.BLUE;
//            case Hall.ACCESSIBLE: return Color.YELLOW;
//            default: return Color.GRAY;
//        }
//    }
//
//    @FXML
//    protected void onBackButtonClick() throws IOException {
//        Stage stage = (Stage) hallLayoutGrid.getScene().getWindow();
//        Parent root = FXMLLoader.load(getClass().getResource("/com/cinemamanage/client/main-view.fxml"));
//        stage.setScene(new Scene(root));
//    }
//
//    public void onClose() {
//        try {
//            cinemaService.onClose();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
