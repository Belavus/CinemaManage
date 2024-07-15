package com.cinemamanage.client;

import com.cinemamanage.models.Booking;
import com.cinemamanage.models.Hall;
import com.cinemamanage.models.Session;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class CinemaService {
    private Client client;
    private Map<String, Booking> bookings;
    private Map<String, Session> sessions;
    private Map<String, Hall> halls;
    private final Gson gson = new Gson();

    public CinemaService(String host, int port) throws IOException {
        this.client = new Client(host, port);
//        this.client.connect();
        this.bookings = new HashMap<>();
        this.sessions = new HashMap<>();
        this.halls = new HashMap<>();
        fetchAllData();
    }

    public void fetchAllData() throws IOException {
        fetchAllHalls();
        fetchAllSessions();
        fetchAllBookings();
    }

    public void fetchAllHalls() throws IOException {
        client.connect();
        Request request = new Request();
        Map<String, String> headers = new HashMap<>();
        headers.put("action", "hall/getAll");
        request.setHeaders(headers);

        Response response = client.sendRequest(request);

        if ("success".equals(response.getStatus())) {
            Type hallMapType = new TypeToken<Map<String, Hall>>() {}.getType();
            halls = gson.fromJson(response.getMessage(), hallMapType);
        } else {
            throw new IOException("Error fetching halls: " + response.getMessage());
        }
        client.disconnect();
    }

    public void fetchAllSessions() throws IOException {
        client.connect();
        Request request = new Request();
        Map<String, String> headers = new HashMap<>();
        headers.put("action", "session/getAll");
        request.setHeaders(headers);

        Response response = client.sendRequest(request);

        if ("success".equals(response.getStatus())) {
            Type sessionMapType = new TypeToken<Map<String, Session>>() {}.getType();
            sessions = gson.fromJson(response.getMessage(), sessionMapType);
        } else {
            throw new IOException("Error fetching sessions: " + response.getMessage());
        }
        client.disconnect();
    }

    public void fetchAllBookings() throws IOException {
        client.connect();
        Request request = new Request();
        Map<String, String> headers = new HashMap<>();
        headers.put("action", "booking/getAll");
        request.setHeaders(headers);

        Response response = client.sendRequest(request);
        if ("success".equals(response.getStatus())) {
            Type bookingMapType = new TypeToken<Map<String, Booking>>() {}.getType();
            bookings = gson.fromJson(response.getMessage(), bookingMapType);
        } else {
            throw new IOException("Error fetching bookings: " + response.getMessage());
        }
        client.disconnect();
    }

    public Map<String, Hall> getAllHalls() {
        return halls;
    }

    public Map<String, Session> getAllSessions() {
        return sessions;
    }

    public Map<String, Booking> getAllBookings() {
        return bookings;
    }

    public void addHall(Hall hall) throws IOException {
        client.connect();
        Request request = new Request();
        Map<String, String> headers = new HashMap<>();
        headers.put("action", "hall/add");
        request.setHeaders(headers);
        Map<String, Object> body = new HashMap<>();
        body.put("hall", hall);
        request.setBody(body);

        Response response = client.sendRequest(request);
        if ("success".equals(response.getStatus())) {
            halls.put(String.valueOf(hall.getHallNumber()), hall);
        } else {
            throw new IOException("Error adding hall: " + response.getMessage());
        }
        client.disconnect();
    }

    public void deleteHall(int hallNumber) throws IOException {
        client.connect();
        Request request = new Request();
        Map<String, String> headers = new HashMap<>();
        headers.put("action", "hall/delete");
        request.setHeaders(headers);
        Map<String, Object> body = new HashMap<>();
        body.put("hallNumber", hallNumber);
        request.setBody(body);

        Response response = client.sendRequest(request);
        if ("success".equals(response.getStatus())) {
            halls.remove(String.valueOf(hallNumber));
        } else {
            throw new IOException("Error deleting hall: " + response.getMessage());
        }
        client.disconnect();
    }

    public void onClose() throws IOException {
        if (client != null) {
            client.disconnect();
        }
    }
}
