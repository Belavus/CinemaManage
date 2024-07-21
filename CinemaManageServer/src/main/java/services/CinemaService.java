package main.java.services;

import main.java.BFSMaxDistanceSeatAlgorithm;
import main.java.IAlgoSeatDistribution;
import main.java.SimpleMaxDistanceSeatAlgorithm;
import main.java.models.Hall;
import main.java.models.Seat;
import main.java.models.Session;
import main.java.models.Booking;
import main.java.dao.HallDao;
import main.java.dao.SessionDao;
import main.java.dao.BookingDao;
import main.java.util.ConfigUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock; //synchronization for critical sections

public class CinemaService {
    private IAlgoSeatDistribution algorithm;
    private final SessionDao sessionDao;
    private final BookingDao bookingDao;
    private final HallDao hallDao;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public CinemaService(SessionDao sessionDao, BookingDao bookingDao, HallDao hallDao) {
        this.sessionDao = sessionDao;
        this.bookingDao = bookingDao;
        this.hallDao = hallDao;
        initializeData();
    }

    public CinemaService(IAlgoSeatDistribution algo) {
        this(
                new SessionDao(ConfigUtil.getProperty("session.file.path")),
                new BookingDao(ConfigUtil.getProperty("booking.file.path")),
                new HallDao(ConfigUtil.getProperty("hall.file.path"))
        );
    }

    // Initialize data from files
    private void initializeData() {
        sessionDao.initializeData();
        bookingDao.initializeData();
        hallDao.initializeData();
    }

    // Manage sessions
    public void addSession(Session session) throws IOException {
        lock.writeLock().lock();
        try {
            if (hallDao.get(String.valueOf(session.getHallNumber())) == null) {
                throw new IllegalArgumentException("Hall number " + session.getHallNumber() + " does not exist.");
            }

            // Проверка на перекрытие сеансов
            LocalDateTime startTime = LocalDateTime.parse(session.getTime(), dateTimeFormatter);
            if (isSessionOverlapping(session.getHallNumber(), startTime, session.getDuration())) {
                throw new IllegalArgumentException("The session time overlaps with an existing session in the same hall.");
            }

            sessionDao.save(session);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean isSessionOverlapping(int hallNumber, LocalDateTime startTime, int duration) {
        lock.readLock().lock();
        try {
            LocalDateTime endTime = startTime.plusMinutes(duration);
            for (Session session : sessionDao.getAll().values()) {
                if (session.getHallNumber() == hallNumber) {
                    LocalDateTime existingStartTime = LocalDateTime.parse(session.getTime(), dateTimeFormatter);
                    LocalDateTime existingEndTime = existingStartTime.plusMinutes(session.getDuration());
                    if (startTime.isBefore(existingEndTime) && endTime.isAfter(existingStartTime)) {
                        return true; // Сеансы перекрываются
                    }
                }
            }
            return false;
        } finally {
            lock.readLock().unlock();
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

    public Map<String, Session> getAllSessions() {
        lock.readLock().lock();
        try {
            return sessionDao.getAll();
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
            for (Booking booking : bookingDao.getAll().values()) {
                if (booking.getSessionId().equals(sessionId)) {
                    bookingDao.delete(booking.getBookingId());
                }
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
            Session session = sessionDao.get(booking.getSessionId());
            if (session == null) {
                throw new IllegalArgumentException("Session with ID " + booking.getSessionId() + " does not exist.");
            }

            Hall hall = hallDao.get(String.valueOf(session.getHallNumber()));
            if (hall == null) {
                throw new IllegalArgumentException("Hall with number " + session.getHallNumber() + " does not exist.");
            }

            int row = booking.getSeat().getRow();
            int col = booking.getSeat().getColumn();

            // Check if the seat is marked as EMPTY_SPACE
            if (hall.getLayout()[row][col] == Hall.EMPTY_SPACE) {
                throw new IllegalArgumentException("Seat at row " + row + " and column " + col + " is marked as EMPTY_SPACE and cannot be booked.");
            }

            // Check if the seat is already booked
            for (Seat seat : session.getSeats()) {
                if (seat.getRow() == row && seat.getColumn() == col) {
                    throw new IllegalArgumentException("Seat at row " + row + " and column " + col + " is already booked.");
                }
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

    public Map<String, Booking> getAllBookings() {
        lock.readLock().lock();
        try {
            return bookingDao.getAll();
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

    public Map<String, Hall> getAllHalls() {
        lock.readLock().lock();
        try {
            return hallDao.getAll();
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
            //delete sessions connected with the hall
            for (Session session : sessionDao.getAll().values()) {
                if (session.getHallNumber() == hallNumber) {
                    deleteSession(session.getSessionId());
                }
            }
            //delete hall
            hallDao.delete(String.valueOf(hallNumber));
        } finally {
            lock.writeLock().unlock();
        }
    }

    // Seat allocation algorithm
    public boolean setAlgorithm(String algorithmName) {
        switch (algorithmName) {
            case "Simple":
                this.algorithm = new SimpleMaxDistanceSeatAlgorithm();
                break;
            case "BFS":
                this.algorithm = new BFSMaxDistanceSeatAlgorithm();
                break;
            default:
                return false;
        }
        return true;
    }
    public List<Seat> findBestSeats(String sessionId, int numberOfSeats, int distance) {
        lock.readLock().lock();
        try {
            Session session = sessionDao.get(sessionId);
            int [][] seatLayout = getHall(session.getHallNumber()).getLayout();//return copy of layout
            for(Seat seat : session.getSeats()) {
                seatLayout[seat.getRow()][seat.getColumn()] = Hall.OCCUPIED;
            }
            int[] bestSeatsIndices = this.algorithm.findBestSeats(seatLayout, numberOfSeats, distance);

            List<Seat> bestSeats = new ArrayList<>();
            for (int i = 0; i < bestSeatsIndices.length; i += 2) {
                int row = bestSeatsIndices[i];
                int column = bestSeatsIndices[i + 1];
                bestSeats.add(new Seat(row, column));
            }

            return bestSeats;
        } finally {
            lock.readLock().unlock();
        }
    }
}