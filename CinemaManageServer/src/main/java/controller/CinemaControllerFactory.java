package main.java.controller;

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