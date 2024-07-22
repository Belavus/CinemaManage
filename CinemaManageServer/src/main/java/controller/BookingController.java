package main.java.controller;

import main.java.models.Booking;
import main.java.server.Response;

import java.util.Map;

public class BookingController extends CinemaController{
    @Override
    public Response addObject(Map<String, Object> body) {
        try {
            String json = gson.toJson(body.get("booking"));
            Booking booking = gson.fromJson(json, Booking.class);
            cinemaService.addBooking(booking);
            return new Response("success", "Booking added successfully");
        } catch (Exception e) {
            return new Response("error", e.getMessage());
        }
    }

    @Override
    public Response deleteObject(Map<String, Object> body) {
        try {
            String bookingId = (String) body.get("bookingId");
            cinemaService.deleteBooking(bookingId);
            return new Response("success", "Booking deleted successfully");
        } catch (Exception e) {
            return new Response("error", e.getMessage());
        }
    }

    @Override
    public Response getAllObjects() {
        try {
            Map<String, Booking> bookings = cinemaService.getAllBookings();
            return new Response("success", gson.toJson(bookings));
        } catch (Exception e) {
            return new Response("error", e.getMessage());
        }
    }
}
