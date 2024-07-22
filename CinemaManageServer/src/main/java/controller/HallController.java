package main.java.controller;

import main.java.models.Hall;
import main.java.server.Response;

import java.util.Map;

public class HallController extends CinemaController {
    @Override
    public Response addObject(Map<String, Object> body) {
        try {
            String json = gson.toJson(body.get("hall"));
            Hall hall = gson.fromJson(json, Hall.class);
            cinemaService.addHall(hall);
            return new Response("success", "Hall added successfully");
        } catch (Exception e) {
            return new Response("error", e.getMessage());
        }
    }

    @Override
    public Response deleteObject(Map<String, Object> body) {
        try {
            int hallNumber = ((Number) body.get("hallNumber")).intValue();
            cinemaService.deleteHall(hallNumber);
            return new Response("success", "Hall deleted successfully");
        } catch (Exception e) {
            return new Response("error", e.getMessage());
        }
    }

    @Override
    public Response getAllObjects() {
        try {
            Map<String, Hall> halls = cinemaService.getAllHalls();
            return new Response("success", gson.toJson(halls));
        } catch (Exception e) {
            return new Response("error", e.getMessage());
        }
    }
}
