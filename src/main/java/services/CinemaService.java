package main.java.services;

import main.java.dao.HallDao;
import main.java.models.Hall;
import main.java.models.Session;
import main.java.models.Booking;
import main.java.dao.SessionDao;
import main.java.dao.BookingDao;
import main.java.seatAllocationAlgorithm.src.IAlgoSeatDistribution;

import java.util.List;

import java.util.stream.Collectors;

public class CinemaService {
    private SessionDao sessionDao;
    private BookingDao bookingDao;
    private HallDao hallDao;
    private IAlgoSeatDistribution algo;

    public CinemaService(SessionDao sessionDao, BookingDao bookingDao, HallDao hallDao, IAlgoSeatDistribution algo) {
        this.sessionDao = sessionDao;
        this.bookingDao = bookingDao;
        this.hallDao = hallDao;
        this.algo = algo;
        initializeData();
    }

    private void initializeData() {
        sessionDao.initializeData();
        bookingDao.initializeData();
        hallDao.initializeData();
    }

    // Управление сеансами
    public void addSession(Session session) {
        sessionDao.save(session);
    }

    public Session getSession(String sessionId) {
        return sessionDao.get(sessionId);
    }

    public List<Session> getAllSessions() {
        return sessionDao.getAll().values().stream().collect(Collectors.toList());
    }

    public void updateSession(Session session) {
        sessionDao.update(session);
    }

    public void deleteSession(String sessionId) {
        sessionDao.delete(sessionId);
    }

    // Управление бронированиями
    public void addBooking(Booking booking) {
        bookingDao.save(booking);
    }

    public Booking getBooking(String bookingId) {
        return bookingDao.get(bookingId);
    }

    public List<Booking> getAllBookings() {
        return bookingDao.getAll().values().stream().collect(Collectors.toList());
    }

    public void updateBooking(Booking booking) {
        bookingDao.update(booking);
    }

    public void deleteBooking(String bookingId) {
        bookingDao.delete(bookingId);
    }

    // Управление залами
    public void addHall(Hall hall) {
        hallDao.save(hall);
    }

    public Hall getHall(int hallNumber) {
        return hallDao.get(String.valueOf(hallNumber));
    }

    public List<Hall> getAllHalls() {
        return hallDao.getAll().values().stream().collect(Collectors.toList());
    }

    public void updateHall(Hall hall) {
        hallDao.update(hall);
    }

    public void deleteHall(int hallNumber) {
        hallDao.delete(String.valueOf(hallNumber));
    }

    // Алгоритм распределения мест
    public int[] findBestSeats(int[][] seatLayout, int numberOfSeats, int preference) {
        return algo.findBestSeats(seatLayout, numberOfSeats, preference);
    }
}

