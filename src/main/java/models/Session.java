package main.java.models;

import java.util.List;

public class Session {
    private String sessionId;
    private String movieName;
    private String time;
    private List<Seat> seats;

    // Constructors, getters and setters
    public Session(String sessionId, String movieName, String time, List<Seat> seats) {
        this.sessionId = sessionId;
        this.movieName = movieName;
        this.time = time;
        this.seats = seats;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }
}

