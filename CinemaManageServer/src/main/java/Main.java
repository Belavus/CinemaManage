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
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Output current working directory
        System.out.println("Current working directory: " + System.getProperty("user.dir"));

        // Use paths from config.properties file
        IAlgoSeatDistribution algo = new BFSMaxDistanceSeatAlgorithm(); // Используем конкретную реализацию алгоритма
        CinemaService cinemaService = new CinemaService(algo);

        // Add and retrieve a hall
        Hall hall = new Hall(3, 4, 3);
        hall.markAsVIP(1, 1);
        hall.markAsAccessible(2, 2);
        hall.markAsEmptySpace(3, 3);
        cinemaService.addHall(hall);

        System.out.println("Все залы: " + cinemaService.getAllHalls());
    }
}