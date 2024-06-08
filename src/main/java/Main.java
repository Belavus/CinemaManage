package main.java;

import main.java.dao.HallDao;
import main.java.models.Hall;
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
        HallDao hallDao = new HallDao();

        // Инициализация алгоритмического модуля
        IAlgoSeatDistribution algo = new BFSMaxDistanceSeatAlgorithm(); // Здесь нужно использовать конкретную реализацию алгоритма

        // Инициализация сервиса
        CinemaService cinemaService = new CinemaService(sessionDao, bookingDao, hallDao, algo);

        // Пример использования сервиса
        Seat seat1 = new Seat(1, 1);
        Seat seat2 = new Seat(1, 2);
        Session session = new Session("1", "Movie", "18:00", Arrays.asList(seat1, seat2), 1);
        cinemaService.addSession(session);

        Booking booking = new Booking("1", "1", seat1, "1234567890");
        cinemaService.addBooking(booking);

        Hall hall = new Hall(2, 5, 5);
        hall.markAsVIP(1, 3);
        hall.markAsAccessible(2, 2);
        hall.markAsEmptySpace(3, 3);
        cinemaService.addHall(hall);

        System.out.println("Все сеансы: " + cinemaService.getAllSessions());
        System.out.println("Все бронирования: " + cinemaService.getAllBookings());
        System.out.println("Все залы: " + cinemaService.getAllHalls());
    }
}