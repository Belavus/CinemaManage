package main.java.models;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class Session implements Serializable {
    private static final long serialVersionUID = 1L;

    private String sessionId;
    private String movieName;
    private String time;
    private List<Seat> seats;
    private int hallNumber; // Добавление номера зала

    // Конструкторы, геттеры и сеттеры
    public Session(String sessionId, String movieName, String time, List<Seat> seats, int hallNumber) {
        this.sessionId = sessionId;
        this.movieName = movieName;
        this.time = time;
        this.seats = seats;
        this.hallNumber = hallNumber;
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

    public int getHallNumber() {
        return hallNumber;
    }

    public void setHallNumber(int hallNumber) {
        this.hallNumber = hallNumber;
    }

    @Override
    public String toString() {
        return "Session{" +
                "sessionId='" + sessionId + '\'' +
                ", movieName='" + movieName + '\'' +
                ", time='" + time + '\'' +
                ", seats=" + seats +
                ", hallNumber=" + hallNumber +
                '}';
    }
}