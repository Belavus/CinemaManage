package com.cinemamanage.client;

import com.cinemamanage.models.Hall;
import javafx.collections.FXCollections;
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
import java.util.Optional;

public class HallsViewController {

    @FXML
    private ComboBox<String> hallsComboBox;

    @FXML
    private GridPane hallLayoutGrid;

    @FXML
    private TextField rowsField;

    @FXML
    private TextField columnsField;

    @FXML
    private TextField hallNumberField;

    private CinemaService cinemaService;

    private Hall currentEditingHall;

    @FXML
    public void initialize() {
        try {
            cinemaService = new CinemaService("localhost", 34567);
            fetchAllHalls();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fetchAllHalls() throws IOException {
        cinemaService.fetchAllHalls();
        hallsComboBox.setItems(FXCollections.observableArrayList(cinemaService.getAllHalls().keySet()));
    }

    @FXML
    protected void onGetAllHallsButtonClick() {
        try {
            fetchAllHalls();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onNewHallButtonClick() {
        hallLayoutGrid.getChildren().clear();
        rowsField.clear();
        columnsField.clear();
        hallNumberField.clear();
    }

    @FXML
    protected void onCreateNewHallButtonClick() {
        int rows = Integer.parseInt(rowsField.getText());
        int columns = Integer.parseInt(columnsField.getText());
        int hallNumber = Integer.parseInt(hallNumberField.getText());

        int[][] layout = new int[rows][columns];
        currentEditingHall = new Hall(hallNumber, layout);
        displayHallLayout(currentEditingHall);
    }

    @FXML
    protected void onSaveNewHallButtonClick() {
        try {
            cinemaService.addHall(currentEditingHall);
            fetchAllHalls();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onDeleteHallButtonClick() {
        String selectedHallNumber = hallsComboBox.getSelectionModel().getSelectedItem();
        if (selectedHallNumber != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Hall");
            alert.setHeaderText("Are you sure you want to delete hall " + selectedHallNumber + "?");
            alert.setContentText("This action cannot be undone.");

            ButtonType buttonTypeYes = new ButtonType("Yes");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == buttonTypeYes) {
                try {
                    cinemaService.deleteHall(Integer.parseInt(selectedHallNumber));
                    fetchAllHalls();
                    hallLayoutGrid.getChildren().clear();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    protected void onHallSelected() {
        String selectedHallNumber = hallsComboBox.getSelectionModel().getSelectedItem();
        if (selectedHallNumber != null) {
            Hall selectedHall = cinemaService.getAllHalls().get(selectedHallNumber);
            displayHallLayout(selectedHall);
        }
    }

    @FXML
    protected void onBackButtonClick() throws IOException {
        Stage stage = (Stage) hallLayoutGrid.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/com/cinemamanage/client/main-view.fxml"));
        stage.setScene(new Scene(root));
    }

    private void displayHallLayout(Hall hall) {
        hallLayoutGrid.getChildren().clear();
        int[][] layout = hall.getLayout();

        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout[i].length; j++) {
                Pane cell = createCell(layout[i][j], i, j);
                hallLayoutGrid.add(cell, j, i);
            }
        }
    }

    private Pane createCell(int value, int row, int column) {
        Pane cell = new Pane();
        cell.setPrefSize(30, 30);
        Rectangle rectangle = new Rectangle(30, 30);
        rectangle.setFill(getColorForValue(value));
        cell.getChildren().add(rectangle);
        cell.setOnMouseClicked(event -> onCellClicked(event, row, column));
        return cell;
    }

    private void onCellClicked(MouseEvent event, int row, int column) {
        if (event.getButton() == MouseButton.SECONDARY && this.currentEditingHall != null) {
            showContextMenu(event, row, column);
        } /*else {*/
//            int currentValue = currentEditingHall.getLayout()[row][column];
//            int newValue = (currentValue + 1) % 5; // Assuming 5 different values
//            currentEditingHall.getLayout()[row][column] = newValue;
//            displayHallLayout(currentEditingHall); // Refresh the layout
//        }
    }

    private void showContextMenu(MouseEvent event, int row, int column) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem emptyItem = new MenuItem("Empty");
        emptyItem.setOnAction(e -> updateCell(row, column, Hall.EMPTY));

        MenuItem occupiedItem = new MenuItem("Occupied");
        occupiedItem.setOnAction(e -> updateCell(row, column, Hall.OCCUPIED));

        MenuItem emptySpaceItem = new MenuItem("Empty Space");
        emptySpaceItem.setOnAction(e -> updateCell(row, column, Hall.EMPTY_SPACE));

        MenuItem vipItem = new MenuItem("VIP");
        vipItem.setOnAction(e -> updateCell(row, column, Hall.VIP));

        MenuItem accessibleItem = new MenuItem("Accessible");
        accessibleItem.setOnAction(e -> updateCell(row, column, Hall.ACCESSIBLE));

        contextMenu.getItems().addAll(emptyItem, occupiedItem, emptySpaceItem, vipItem, accessibleItem);
        contextMenu.show(hallLayoutGrid, event.getScreenX(), event.getScreenY());
    }


    private void updateCell(int row, int column, int value) {
        currentEditingHall.getLayout()[row][column] = value;
        displayHallLayout(currentEditingHall);
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

    public void onClose() {
        try {
            cinemaService.onClose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}