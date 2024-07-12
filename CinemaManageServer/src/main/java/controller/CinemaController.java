package main.java.controller;

import com.google.gson.Gson;
import main.java.models.Session;
import main.java.models.Booking;
import main.java.models.Hall;
import main.java.seatAllocationAlgorithm.src.BFSMaxDistanceSeatAlgorithm;
import main.java.services.CinemaService;
import main.java.server.Response;
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
            String json = gson.toJson(body);
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
            String json = gson.toJson(body);
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
}
