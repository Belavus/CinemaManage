package main.java.controller;

import com.google.gson.Gson;
import main.java.BFSMaxDistanceSeatAlgorithm;
import main.java.IAlgoSeatDistribution;
import main.java.SimpleMaxDistanceSeatAlgorithm;
import main.java.models.Seat;
import main.java.models.Session;
import main.java.models.Booking;
import main.java.models.Hall;
import main.java.services.CinemaService;
import main.java.server.Response;

import java.util.List;
import java.util.Map;

public class CinemaController {
    private final CinemaService cinemaService;
    private final Gson gson = new Gson();

    public CinemaController() {
        // Initialize CinemaService with default values or configuration
        this.cinemaService = new CinemaService(new BFSMaxDistanceSeatAlgorithm());
    }

    public Response addSession(Map<String, Object> body) {
        try {
            String json = gson.toJson(body.get("session"));
            Session session = gson.fromJson(json, Session.class);
            cinemaService.addSession(session);
            return new Response("success", "Session added successfully");
        } catch (Exception e) {
            return new Response("error", e.getMessage());
        }
    }

    public Response deleteSession(Map<String, Object> body) {
        try {
            String sessionId = (String) body.get("sessionId");
            cinemaService.deleteSession(sessionId);
            return new Response("success", "Session deleted successfully");
        } catch (Exception e) {
            return new Response("error", e.getMessage());
        }
    }

    public Response addBooking(Map<String, Object> body) {
        try {
            String json = gson.toJson(body.get("booking"));
            Booking booking = gson.fromJson(json, Booking.class);
            cinemaService.addBooking(booking);
            return new Response("success", "Booking added successfully");
        } catch (Exception e) {
            return new Response("error", e.getMessage());
        }
    }

    public Response deleteBooking(Map<String, Object> body) {
        try {
            String bookingId = (String) body.get("bookingId");
            cinemaService.deleteBooking(bookingId);
            return new Response("success", "Booking deleted successfully");
        } catch (Exception e) {
            return new Response("error", e.getMessage());
        }
    }

    public Response addHall(Map<String, Object> body) {
        try {
            String json = gson.toJson(body.get("hall"));
            Hall hall = gson.fromJson(json, Hall.class);
            cinemaService.addHall(hall);
            return new Response("success", "Hall added successfully");
        } catch (Exception e) {
            return new Response("error", e.getMessage());
        }
    }

    public Response deleteHall(Map<String, Object> body) {
        try {
            int hallNumber = ((Number) body.get("hallNumber")).intValue();
            cinemaService.deleteHall(hallNumber);
            return new Response("success", "Hall deleted successfully");
        } catch (Exception e) {
            return new Response("error", e.getMessage());
        }
    }

    public Response getAllHalls() {
        try {
            Map<String, Hall> halls = cinemaService.getAllHalls();
            return new Response("success", gson.toJson(halls));
        } catch (Exception e) {
            return new Response("error", e.getMessage());
        }
    }

    public Response getAllSessions() {
        try {
            Map<String, Session> sessions = cinemaService.getAllSessions();
            return new Response("success", gson.toJson(sessions));
        } catch (Exception e) {
            return new Response("error", e.getMessage());
        }
    }

    public Response getAllBookings() {
        try {
            Map<String, Booking> bookings = cinemaService.getAllBookings();
            return new Response("success", gson.toJson(bookings));
        } catch (Exception e) {
            return new Response("error", e.getMessage());
        }
    }

    public Response generateSeats(Map<String, Object> body) {
        IAlgoSeatDistribution algorithm;
        String sessionId = (String) body.get("sessionId");
        int numberOfPeople = ((Double) body.get("numberOfPeople")).intValue();
        int distance = ((Double) body.get("distance")).intValue();
        String algorithmName = (String) body.get("algorithm");

        List<Seat> generatedSeats;
        if(!cinemaService.setAlgorithm(algorithmName)){
            return new Response("error", "Unknown algorithm: " + algorithmName);
        }

        generatedSeats = cinemaService.findBestSeats(sessionId,numberOfPeople,distance);
        return new Response("success", gson.toJson(generatedSeats));
    }

}
