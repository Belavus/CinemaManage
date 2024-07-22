package main.java.controller;

import com.google.gson.Gson;
import main.java.BFSMaxDistanceSeatAlgorithm;
import main.java.IAlgoSeatDistribution;
import main.java.models.Seat;
import main.java.server.Response;
import main.java.services.CinemaService;

import java.util.List;
import java.util.Map;

public abstract class CinemaController {
    protected final CinemaService cinemaService;
    protected final Gson gson = new Gson();

    public CinemaController() {
        // Initialize CinemaService with default configuration
        this.cinemaService = new CinemaService(new BFSMaxDistanceSeatAlgorithm());
    }

    public abstract Response addObject(Map<String, Object> body);
    public abstract Response deleteObject(Map<String, Object> body);
    public abstract Response getAllObjects();


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
