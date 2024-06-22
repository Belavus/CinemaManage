package main.java.models;

import java.io.Serial;
import java.io.Serializable;

public class Booking implements Serializable {
    private static final long serialVersionUID = 1L;

    private String bookingId;
    private String sessionId;
    private Seat seat;
    private String phoneNumber;

    // Constructors, getters, setters
    public Booking(String bookingId, String sessionId, Seat seat, String phoneNumber) {
        this.bookingId = bookingId;
        this.sessionId = sessionId;
        this.seat = seat;
        this.phoneNumber = phoneNumber;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId='" + bookingId + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", seat=" + seat +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}

