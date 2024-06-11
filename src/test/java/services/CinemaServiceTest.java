package test.java.services;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import main.java.services.CinemaService;
import main.java.models.Session;
import main.java.models.Seat;
import main.java.models.Booking;
import main.java.models.Hall;
import main.java.dao.SessionDao;
import main.java.dao.BookingDao;
import main.java.dao.HallDao;
import main.java.seatAllocationAlgorithm.src.IAlgoSeatDistribution;
import main.java.seatAllocationAlgorithm.src.BFSMaxDistanceSeatAlgorithm;
import java.util.Arrays;
import java.util.List;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class CinemaServiceTest {
    private CinemaService cinemaService;
    private File sessionFile;
    private File bookingFile;
    private File hallFile;

    @BeforeEach
    public void setUp() throws Exception {
        sessionFile = createInitializedFile("sessions.ser");
        bookingFile = createInitializedFile("bookings.ser");
        hallFile = createInitializedFile("halls.ser");

        SessionDao sessionDao = new SessionDao(sessionFile.getAbsolutePath());
        BookingDao bookingDao = new BookingDao(bookingFile.getAbsolutePath());
        HallDao hallDao = new HallDao(hallFile.getAbsolutePath());
        IAlgoSeatDistribution algo = new BFSMaxDistanceSeatAlgorithm(); // Используем ваш алгоритм

        cinemaService = new CinemaService(sessionDao, bookingDao, hallDao, algo);
    }

    @AfterEach
    public void tearDown() {
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

    private File createInitializedFile(String filename) throws IOException {
        File file = File.createTempFile(filename, ".ser");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(new HashMap<>()); // Записываем пустую карту
        }
        return file;
    }

    @Test
    public void testAddAndGetSession() {
        Seat seat1 = new Seat(1, 1);
        Seat seat2 = new Seat(1, 2);
        Session session = new Session("1", "Movie", "18:00", Arrays.asList(seat1, seat2), 1);
        cinemaService.addSession(session);

        Session retrievedSession = cinemaService.getSession("1");
        assertNotNull(retrievedSession);
        assertEquals("Movie", retrievedSession.getMovieName());
        assertEquals("18:00", retrievedSession.getTime());
    }

    @Test
    public void testAddAndGetBooking() {
        Seat seat = new Seat(1, 1);
        Booking booking = new Booking("1", "1", seat, "1234567890");
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
    public void testFindBestSeats() {
        int[][] layout = {
                {0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 1, 1, 1, 0}
        };
        int numberOfSeats = 2;
        int preference = 1; // Предпочтение (например, максимальное расстояние между зрителями)

        int[] bestSeats = cinemaService.findBestSeats(layout, numberOfSeats, preference);
        assertNotNull(bestSeats);
        assertEquals(4, bestSeats.length); // Должно быть 4 элемента (2 пары координат)
    }

    @Test
    public void testGetAllSessions() {
        Seat seat1 = new Seat(1, 1);
        Seat seat2 = new Seat(1, 2);
        Session session = new Session("1", "Movie", "18:00", Arrays.asList(seat1, seat2), 1);
        cinemaService.addSession(session);

        List<Session> sessions = cinemaService.getAllSessions();
        assertNotNull(sessions);
        assertEquals(1, sessions.size());
    }

    @Test
    public void testGetAllBookings() {
        Seat seat = new Seat(1, 1);
        Booking booking = new Booking("1", "1", seat, "1234567890");
        cinemaService.addBooking(booking);

        List<Booking> bookings = cinemaService.getAllBookings();
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
    }

    @Test
    public void testGetAllHalls() {
        Hall hall = new Hall(1, 5, 5);
        cinemaService.addHall(hall);

        List<Hall> halls = cinemaService.getAllHalls();
        assertNotNull(halls);
        assertEquals(1, halls.size());
    }
}