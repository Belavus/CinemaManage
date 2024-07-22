package main.java.controller;

import main.java.server.Response;

public class CinemaControllerFactory {
    public static CinemaController getCinemaController(String controllerName) {
        switch (controllerName) {
            case "session":
                return new SessionController();
            case "booking":
                return new BookingController();
            default:
                return new HallController();
        }
    }
}