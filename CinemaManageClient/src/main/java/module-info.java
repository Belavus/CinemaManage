module com.cinemamanage.client.cinemamanageclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens com.cinemamanage.client to javafx.fxml, com.google.gson;
    exports com.cinemamanage.client;
    opens com.cinemamanage.client.models to com.google.gson;
    exports com.cinemamanage.client.models;
    exports com.cinemamanage.client.controllers;
    opens com.cinemamanage.client.controllers to com.google.gson, javafx.fxml;
    exports com.cinemamanage.client.connection;
    opens com.cinemamanage.client.connection to com.google.gson, javafx.fxml;
    exports com.cinemamanage.client.services;
    opens com.cinemamanage.client.services to com.google.gson, javafx.fxml;
}
