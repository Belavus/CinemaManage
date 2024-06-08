package main.java.services;

import main.java.models.Session;
import main.java.models.Booking;
import main.java.dao.SessionDao;
import main.java.dao.BookingDao;
import main.java.seatAllocationAlgorithm.src.IAlgoSeatDistribution;

import java.util.ArrayList;
import java.util.List;

import java.util.stream.Collectors;

public class CinemaService {
    private SessionDao sessionDao;
    private BookingDao bookingDao;
    private IAlgoSeatDistribution algo;

    public CinemaService(SessionDao sessionDao, BookingDao bookingDao, IAlgoSeatDistribution algo) {
        this.sessionDao = sessionDao;
        this.bookingDao = bookingDao;
        this.algo = algo;
        initializeData();
    }

    private void initializeData() {
        sessionDao.initializeData();
        bookingDao.initializeData();
    }

    // Session Managing
    public void addSession(Session session) {
        sessionDao.save(session);
    }

    public Session getSession(String sessionId) {
        return sessionDao.get(sessionId);
    }

    public List<Session> getAllSessions() {
        return new ArrayList<>(sessionDao.getAll().values());
    }

    public void updateSession(Session session) {
        sessionDao.update(session);
    }

    public void deleteSession(String sessionId) {
        sessionDao.delete(sessionId);
    }

    // Booking Managing
    public void addBooking(Booking booking) {
        bookingDao.save(booking);
    }

    public Booking getBooking(String bookingId) {
        return bookingDao.get(bookingId);
    }

    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookingDao.getAll().values());
    }

    public void updateBooking(Booking booking) {
        bookingDao.update(booking);
    }

    public void deleteBooking(String bookingId) {
        bookingDao.delete(bookingId);
    }

    // Алгоритм распределения мест
    public int[] findBestSeats(int[][] seatLayout, int numberOfSeats, int preference) {
        return algo.findBestSeats(seatLayout, numberOfSeats, preference);
    }
}

