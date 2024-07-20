package main.java.server;

import com.google.gson.Gson;
import main.java.controller.CinemaController;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class HandleRequest implements Runnable {
    private final Socket clientSocket;

    public HandleRequest(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (Scanner reader = new Scanner(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true)) {

            String requestJson = reader.nextLine();
            Gson gson = new Gson();
            Request request = gson.fromJson(requestJson, Request.class);

            Response response = handleRequest(request);

            String responseJson = gson.toJson(response);
            writer.println(responseJson);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Response handleRequest(Request request) {
        String action = request.getHeaders().get("action");
        CinemaController controller = new CinemaController();

        switch (action) {
            case "session/add":
                return controller.addSession(request.getBody());
            case "session/delete":
                return controller.deleteSession(request.getBody());
            case "session/getAll":
                return controller.getAllSessions();
            case "booking/add":
                return controller.addBooking(request.getBody());
            case "booking/delete":
                return controller.deleteBooking(request.getBody());
            case "booking/getAll":
                return controller.getAllBookings();
            case "hall/add":
                return controller.addHall(request.getBody());
            case "hall/delete":
                return controller.deleteHall(request.getBody());
            case "hall/getAll":
                return controller.getAllHalls();
            case "seat/generate":
                return controller.generateSeats(request.getBody());
            default:
                return new Response("error", "Unknown action: " + action);
        }
    }
}

