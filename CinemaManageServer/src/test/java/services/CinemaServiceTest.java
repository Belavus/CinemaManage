package test.java.services;

import main.java.services.CinemaService;
import main.java.models.Session;
import main.java.models.Seat;
import main.java.models.Booking;
import main.java.models.Hall;
import main.java.dao.SessionDao;
import main.java.dao.BookingDao;
import main.java.dao.HallDao;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CinemaServiceTest {
    private static CinemaService cinemaService;
    private static File sessionFile;
    private static File bookingFile;
    private static File hallFile;

    @BeforeAll
    public static void setUp() throws Exception {
        sessionFile = createInitializedFile("sessions.ser");
        bookingFile = createInitializedFile("bookings.ser");
        hallFile = createInitializedFile("halls.ser");

        SessionDao sessionDao = new SessionDao(sessionFile.getAbsolutePath());
        BookingDao bookingDao = new BookingDao(bookingFile.getAbsolutePath());
        HallDao hallDao = new HallDao(hallFile.getAbsolutePath());

        cinemaService = new CinemaService(sessionDao, bookingDao, hallDao);
        cinemaService.setAlgorithm("BFS");
    }

    @AfterAll
    public static void tearDown() {
        if (sessionFile.exists()) {
            sessionFile.delete();
        }
        if (bookingFile.exists()) {
            bookingFile.delete();
        }
        if (hallFile.exists()) {
            hallFile.delete();
        }
    }

    private static File createInitializedFile(String filename) throws IOException {
        File file = File.createTempFile(filename, ".ser");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(new HashMap<>());
        }
        return file;
    }

    @BeforeEach
    public void initializeData() {
    }

    @Test
    public void testAddAndGetSession() throws IOException {
        Seat seat1 = new Seat(1, 1);
        Seat seat2 = new Seat(1, 2);
        Session session = new Session("1", "Movie", "2024-07-18 00:00", 60, Arrays.asList(seat1, seat2), 1);
        cinemaService.addSession(session);

        Session retrievedSession = cinemaService.getSession("1");
        assertNotNull(retrievedSession);
        assertEquals("Movie", retrievedSession.getMovieName());
        assertEquals("2024-07-18 00:00", retrievedSession.getTime());
    }

    @Test
    public void testAddAndGetBooking() throws IOException {
        Hall hall = new Hall(1, 5, 5);
        cinemaService.addHall(hall);

        Seat seat = new Seat(1, 1);
        Session session = new Session("1", "Movie", "2024-07-18 20:00", 120, Arrays.asList(seat), 1);
        cinemaService.addSession(session);

        Booking booking = new Booking("1", "1", new Seat(2,2), "1234567890");
        cinemaService.addBooking(booking);

        Booking retrievedBooking = cinemaService.getBooking("1");
        assertNotNull(retrievedBooking);
        assertEquals("1", retrievedBooking.getSessionId());
        assertEquals("1234567890", retrievedBooking.getPhoneNumber());
    }

    @Test
    public void testAddAndGetHall() {
        Hall hall = new Hall(1, 5, 5);
        hall.markAsVIP(1, 1);
        hall.markAsAccessible(2, 2);
        hall.markAsEmptySpace(3, 3);
        cinemaService.addHall(hall);

        Hall retrievedHall = cinemaService.getHall(1);
        assertNotNull(retrievedHall);
        assertEquals(5, retrievedHall.getLayout().length);
        assertEquals(Hall.VIP, retrievedHall.getLayout()[1][1]);
        assertEquals(Hall.ACCESSIBLE, retrievedHall.getLayout()[2][2]);
        assertEquals(Hall.EMPTY_SPACE, retrievedHall.getLayout()[3][3]);
    }

    @Test
    public void testFindBestSeats() throws IOException {
        int[][] layout = {
                {0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 1, 1, 1, 0}
        };
        Hall hall = new Hall(1, layout);
        cinemaService.addHall(hall);

        Seat seat1 = new Seat(0, 2);
        Seat seat2 = new Seat(2, 1);
        Seat seat3 = new Seat(2, 2);
        Seat seat4 = new Seat(2, 3);
        Session session = new Session("1", "Movie", "2024-07-18 20:00", 120, Arrays.asList(seat1, seat2, seat3, seat4), 1);
        cinemaService.addSession(session);

        List<Seat> bestSeats = cinemaService.findBestSeats("1", 2, 1);
        assertNotNull(bestSeats);
        assertEquals(2, bestSeats.size());
    }

    @Test
    public void testGetAllSessions() throws IOException {
        Seat seat1 = new Seat(1, 1);
        Seat seat2 = new Seat(1, 2);
        Session session = new Session("1", "Movie", "2024-07-18 20:00", 120, Arrays.asList(seat1, seat2), 1);
        cinemaService.addSession(session);

        Map<String, Session> sessions = cinemaService.getAllSessions();
        assertNotNull(sessions);
        assertEquals(1, sessions.size());
        assertTrue(sessions.containsKey("1"));
    }

    @Test
    public void testGetAllBookings() throws IOException {
        Hall hall = new Hall(1, 5, 5);
        cinemaService.addHall(hall);

        Seat seat = new Seat(1, 1);
        Session session = new Session("1", "Movie", "2024-07-18 20:00", 120, Arrays.asList(seat), 1);
        cinemaService.addSession(session);

        Booking booking = new Booking("1", "1", new Seat(2,2), "1234567890");
        cinemaService.addBooking(booking);

        Map<String, Booking> bookings = cinemaService.getAllBookings();
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertTrue(bookings.containsKey("1"));
    }

    @Test
    public void testGetAllHalls() {
        Hall hall = new Hall(1, 5, 5);
        cinemaService.addHall(hall);

        Map<String, Hall> halls = cinemaService.getAllHalls();
        assertNotNull(halls);
        assertEquals(1, halls.size());
        assertTrue(halls.containsKey("1"));
    }
}
