package main.java.services;

import main.java.models.Hall;
import main.java.models.Seat;
import main.java.models.Session;
import main.java.models.Booking;
import main.java.dao.HallDao;
import main.java.dao.SessionDao;
import main.java.dao.BookingDao;
import main.java.util.ConfigUtil;
import main.java.seatAllocationAlgorithm.src.IAlgoSeatDistribution;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.concurrent.locks.ReentrantReadWriteLock; //synchronization for critical sections

public class CinemaService {
    private final SessionDao sessionDao;
    private final BookingDao bookingDao;
    private final HallDao hallDao;
    private final IAlgoSeatDistribution algo;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public CinemaService(SessionDao sessionDao, BookingDao bookingDao, HallDao hallDao, IAlgoSeatDistribution algo) {
        this.sessionDao = sessionDao;
        this.bookingDao = bookingDao;
        this.hallDao = hallDao;
        this.algo = algo;
        initializeData();
    }

    public CinemaService(IAlgoSeatDistribution algo) {
        this(
                new SessionDao(ConfigUtil.getProperty("session.file.path")),
                new BookingDao(ConfigUtil.getProperty("booking.file.path")),
                new HallDao(ConfigUtil.getProperty("hall.file.path")),
                algo
        );
    }

    // Initialize data from files
    private void initializeData() {
        sessionDao.initializeData();
        bookingDao.initializeData();
        hallDao.initializeData();
    }

    // Manage sessions
    public void addSession(Session session) {
        lock.writeLock().lock();
        try {
            if (hallDao.get(String.valueOf(session.getHallNumber())) == null) {
                throw new IllegalArgumentException("Hall number " + session.getHallNumber() + " does not exist.");
            }
            sessionDao.save(session);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Session getSession(String sessionId) {
        lock.readLock().lock();
        try {
            return sessionDao.get(sessionId);
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<Session> getAllSessions() {
        lock.readLock().lock();
        try {
            return sessionDao.getAll().values().stream().collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    public void updateSession(Session session) {
        lock.writeLock().lock();
        try {
            if (sessionDao.get(session.getSessionId()) == null) {
                throw new IllegalArgumentException("Session with ID " + session.getSessionId() + " does not exist.");
            }
            sessionDao.update(session);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void deleteSession(String sessionId) {
        lock.writeLock().lock();
        try {
            // Remove all bookings associated with the session
            List<Booking> bookings = bookingDao.getAll().values().stream()
                    .filter(booking -> booking.getSessionId().equals(sessionId))
                    .collect(Collectors.toList());
            for (Booking booking : bookings) {
                bookingDao.delete(booking.getBookingId());
            }
            // Remove the session itself
            sessionDao.delete(sessionId);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void addSeatToSession(String sessionId, Seat seat) {
        lock.writeLock().lock();
        try {
            Session session = sessionDao.get(sessionId);
            if (session != null) {
                session.addSeat(seat);
                sessionDao.update(session);
            } else {
                throw new IllegalArgumentException("Session with ID " + sessionId + " does not exist.");
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void removeSeatFromSession(String sessionId, Seat seat) {
        lock.writeLock().lock();
        try {
            Session session = sessionDao.get(sessionId);
            if (session != null) {
                session.removeSeat(seat);
                sessionDao.update(session);
            } else {
                throw new IllegalArgumentException("Session with ID " + sessionId + " does not exist.");
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    // Manage bookings
    public void addBooking(Booking booking) {
        lock.writeLock().lock();
        try {
            if (sessionDao.get(booking.getSessionId()) == null) {
                throw new IllegalArgumentException("Session with ID " + booking.getSessionId() + " does not exist.");
            }
            bookingDao.save(booking);
            addSeatToSession(booking.getSessionId(), booking.getSeat());
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Booking getBooking(String bookingId) {
        lock.readLock().lock();
        try {
            return bookingDao.get(bookingId);
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<Booking> getAllBookings() {
        lock.readLock().lock();
        try {
            return bookingDao.getAll().values().stream().collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    public void updateBooking(Booking booking) {
        lock.writeLock().lock();
        try {
            if (bookingDao.get(booking.getBookingId()) == null) {
                throw new IllegalArgumentException("Booking with ID " + booking.getBookingId() + " does not exist.");
            }
            bookingDao.update(booking);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void deleteBooking(String bookingId) {
        lock.writeLock().lock();
        try {
            Booking booking = bookingDao.get(bookingId);
            if (booking != null) {
                removeSeatFromSession(booking.getSessionId(), booking.getSeat());
                bookingDao.delete(bookingId);
            } else {
                throw new IllegalArgumentException("Booking with ID " + bookingId + " does not exist.");
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    // Manage halls
    public void addHall(Hall hall) {
        lock.writeLock().lock();
        try {
            hallDao.save(hall);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Hall getHall(int hallNumber) {
        lock.readLock().lock();
        try {
            return hallDao.get(String.valueOf(hallNumber));
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<Hall> getAllHalls() {
        lock.readLock().lock();
        try {
            return hallDao.getAll().values().stream().collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    public void updateHall(Hall hall) {
        lock.writeLock().lock();
        try {
            if (hallDao.get(String.valueOf(hall.getHallNumber())) == null) {
                throw new IllegalArgumentException("Hall with number " + hall.getHallNumber() + " does not exist.");
            }
            hallDao.update(hall);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void deleteHall(int hallNumber) {
        lock.writeLock().lock();
        try {
            hallDao.delete(String.valueOf(hallNumber));
        } finally {
            lock.writeLock().unlock();
        }
    }

    // Seat allocation algorithm
    public int[] findBestSeats(int[][] seatLayout, int numberOfSeats, int preference) {
        lock.readLock().lock();
        try {
            return algo.findBestSeats(seatLayout, numberOfSeats, preference);
        } finally {
            lock.readLock().unlock();
        }
    }
}