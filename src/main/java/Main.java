package main.java;

import main.java.seatAllocationAlgorithm.src.BFSMaxDistanceSeatAlgorithm;
import main.java.seatAllocationAlgorithm.src.IAlgoSeatDistribution;
import main.java.services.CinemaService;
import main.java.models.Session;
import main.java.models.Seat;
import main.java.models.Booking;
import main.java.dao.SessionDao;
import main.java.dao.BookingDao;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        // Вывод текущей рабочей директории
        System.out.println("Current working directory: " + System.getProperty("user.dir"));

        // Инициализация DAO
        SessionDao sessionDao = new SessionDao();
        BookingDao bookingDao = new BookingDao();

        // Инициализация алгоритмического модуля
        IAlgoSeatDistribution algo = new BFSMaxDistanceSeatAlgorithm(); // Здесь нужно использовать конкретную реализацию алгоритма

        // Инициализация сервиса
        CinemaService cinemaService = new CinemaService(sessionDao, bookingDao, algo);

        // Пример использования сервиса
        Seat seat1 = new Seat(1, 1);
        Seat seat2 = new Seat(1, 2);
        Session session1 = new Session("3", "Movie", "18:00", Arrays.asList(seat1, seat2));
        Session session2 = new Session("4", "Best Movie", "19:00", Arrays.asList(seat2, seat1));
        cinemaService.addSession(session1);
        cinemaService.addSession(session2);

        Booking booking = new Booking("1", "1", seat1);
        cinemaService.addBooking(booking);

        System.out.println("Все сеансы: " + cinemaService.getAllSessions());
        System.out.println("Все бронирования: " + cinemaService.getAllBookings());
    }
}