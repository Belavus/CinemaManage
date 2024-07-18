package main.java.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import main.java.models.Booking;
import main.java.models.Seat;
import main.java.models.Session;
import main.java.server.Request;
import main.java.server.Response;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.*;

import main.java.models.Hall;

public class Client {
    private final String host;
    private final int port;
    private final Gson gson = new Gson();

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Response sendRequest(Request request) {
        try (Socket socket = new Socket(host, port);
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
             Scanner reader = new Scanner(new InputStreamReader(socket.getInputStream()))) {

            String requestJson = gson.toJson(request);
            writer.println(requestJson);

            String responseJson = reader.nextLine();
            return gson.fromJson(responseJson, Response.class);

        } catch (Exception e) {
            e.printStackTrace();
            return new Response("error", e.getMessage());
        }
    }

    public static void main(String[] args) {
        Client client = new Client("localhost", 34567);

        // ===========Create a request to add a hall===========
        Map<String, String> headers = new HashMap<>();
        headers.put("action", "hall/add");


        Map<String, Object> body = new HashMap<>();
        Hall hall = new Hall(4,10,6);
        body.put("hall", hall);

        Request request = new Request();
        request.setHeaders(headers);
        request.setBody(body);

        Response response = client.sendRequest(request);
        System.out.println("Add hall Response: " + response.getStatus() + " - " + response.getMessage());

        // =========== Additional requests to get all HALLS and display the result ===========
        Gson gson = new Gson();
        Type hallMapType = new TypeToken<Map<String, Hall>>() {}.getType();

        headers.put("action", "hall/getAll");
        request.setHeaders(headers);
        request.setBody(new HashMap<>()); // Empty body

        response = client.sendRequest(request);
        Map<String,Hall> hallMapFromJson = gson.fromJson(response.getMessage(), hallMapType);
        System.out.println(hallMapFromJson.get("1"));
        System.out.println("All Halls: " + hallMapFromJson);

        // =========== Additional requests to add and get all SESSIONS and display the result ===========
        Seat seat1 = new Seat(1, 1);
        Seat seat2 = new Seat(1, 2);
        Session session = new Session("1", "Terminator", "2024-07-18 20:00", Arrays.asList(seat1, seat2), 1);

        headers.put("action", "session/add");
        body.clear();
        body.put("session", session);
        request.setHeaders(headers);
        request.setBody(body);

        response = client.sendRequest(request);
        System.out.println("Add session Response: " + response.getStatus() + " - " + response.getMessage());


        Type sessionMapType = new TypeToken<Map<String, Session>>() {}.getType();
        headers.put("action", "session/getAll");
        request.setHeaders(headers);
        request.setBody(new HashMap<>()); // Empty body

        response = client.sendRequest(request);
        Map<String,Session> sessionMapFromJson = gson.fromJson(response.getMessage(), sessionMapType);
        System.out.println(sessionMapFromJson.get("1"));
        System.out.println("All Sessions: " + sessionMapFromJson);

        // =========== Additional requests to add and get all BOOKINGS and display the result ===========
        Seat seat = new Seat(1, 1);
        Booking booking = new Booking("2", "1", seat, "1234567890");

        headers.put("action", "booking/add");
        body.clear();
        body.put("booking", booking);
        request.setHeaders(headers);
        request.setBody(body);

        response = client.sendRequest(request);
        System.out.println("Add booking Response: " + response.getStatus() + " - " + response.getMessage());


        Type bookingMapType = new TypeToken<Map<String, Booking>>() {}.getType();
        headers.put("action", "booking/getAll");
        request.setHeaders(headers);
        request.setBody(new HashMap<>()); // Empty body

        response = client.sendRequest(request);
        Map<String,Booking> bookingMapFromJson = gson.fromJson(response.getMessage(), bookingMapType);
        System.out.println(bookingMapFromJson.get("1"));
        System.out.println("All Bookings: " + bookingMapFromJson);
    }
}