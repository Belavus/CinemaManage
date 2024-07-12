module com.cinemamanage.client.cinemamanageclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens com.cinemamanage.client to javafx.fxml, com.google.gson;
    exports com.cinemamanage.client;
    opens com.cinemamanage.models to com.google.gson;
    exports com.cinemamanage.models;
}
