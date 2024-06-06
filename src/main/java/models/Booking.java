package main.java.models;

public class Booking {
    private String bookingId;
    private String sessionId;
    private Seat seat;

    // Конструкторы, геттеры и сеттеры
    public Booking(String bookingId, String sessionId, Seat seat) {
        this.bookingId = bookingId;
        this.sessionId = sessionId;
        this.seat = seat;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }
}

