package main.java;

import main.java.models.Hall;
import main.java.seatAllocationAlgorithm.src.BFSMaxDistanceSeatAlgorithm;
import main.java.seatAllocationAlgorithm.src.IAlgoSeatDistribution;
import main.java.services.CinemaService;
import main.java.models.Session;
import main.java.models.Seat;
import main.java.models.Booking;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        // output current working directory
        System.out.println("Current working directory: " + System.getProperty("user.dir"));

        // Use paths from config.properties file
        IAlgoSeatDistribution algo = new BFSMaxDistanceSeatAlgorithm();
        CinemaService cinemaService = new CinemaService(algo);

//        // Uncomment to add initial data
//        Seat seat1 = new Seat(1, 1);
//        Seat seat2 = new Seat(1, 2);

//        Session session = new Session("1", "Movie", "18:00", new ArrayList<>(Arrays.asList(seat1, seat2)), 1);
//        cinemaService.addSession(session);

//        Booking booking = new Booking("1", "1", seat1, "1234567890");
//        cinemaService.addBooking(booking);
//
//        Hall hall = new Hall(1, 5, 5);
//        hall.markAsVIP(1, 1);
//        hall.markAsAccessible(2, 2);
//        hall.markAsEmptySpace(3, 3);
//        cinemaService.addHall(hall);

        // Removing seat
        Session session = cinemaService.getSession("1");
        session.removeSeat(new Seat(3, 3));
        cinemaService.updateSession(session);
        System.out.println("Session updated: " + session);

//        // Add seat
//        Session session = cinemaService.getSession("1");
//        session.addSeat(new Seat(3, 3));
//        cinemaService.updateSession(session);
//        System.out.println("Session updated: " + session);


        System.out.println("Все сеансы: " + cinemaService.getAllSessions());
        System.out.println("Все бронирования: " + cinemaService.getAllBookings());
        System.out.println("Все залы: " + cinemaService.getAllHalls());
    }
}