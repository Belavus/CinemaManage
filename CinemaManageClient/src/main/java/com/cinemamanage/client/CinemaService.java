package com.cinemamanage.client;

import com.cinemamanage.models.Booking;
import com.cinemamanage.models.Hall;
import com.cinemamanage.models.Session;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
    private final Gson gson;

    public CinemaService(String host, int port) throws IOException {
        this.client = new Client(host, port);
        this.gson = new GsonBuilder().create();
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

    public void addSession(Session session) throws IOException {
        client.connect();
        Request request = new Request();
        Map<String, String> headers = new HashMap<>();
        headers.put("action", "session/add");
        request.setHeaders(headers);
        Map<String, Object> body = new HashMap<>();
        body.put("session", session);
        request.setBody(body);

        Response response = client.sendRequest(request);
        if ("success".equals(response.getStatus())) {
            sessions.put(session.getSessionId(), session);
        } else {
            throw new IOException("Error adding session: " + response.getMessage());
        }
        client.disconnect();
    }

    public void deleteSession(String sessionId) throws IOException {
        client.connect();
        Request request = new Request();
        Map<String, String> headers = new HashMap<>();
        headers.put("action", "session/delete");
        request.setHeaders(headers);
        Map<String, Object> body = new HashMap<>();
        body.put("sessionId", sessionId);
        request.setBody(body);

        Response response = client.sendRequest(request);
        if ("success".equals(response.getStatus())) {
            sessions.remove(sessionId);
        } else {
            throw new IOException("Error deleting session: " + response.getMessage());
        }
        client.disconnect();
    }

    public void addBooking(Booking booking) throws IOException {
        client.connect();
        Request request = new Request();
        Map<String, String> headers = new HashMap<>();
        headers.put("action", "booking/add");
        request.setHeaders(headers);
        Map<String, Object> body = new HashMap<>();
        body.put("booking", booking);
        request.setBody(body);

        Response response = client.sendRequest(request);
        if ("success".equals(response.getStatus())) {
            bookings.put(booking.getBookingId(), booking);
        } else {
            throw new IOException("Error adding booking: " + response.getMessage());
        }
        client.disconnect();
    }

    public void deleteBooking(String bookingId) throws IOException {
        client.connect();
        Request request = new Request();
        Map<String, String> headers = new HashMap<>();
        headers.put("action", "booking/delete");
        request.setHeaders(headers);
        Map<String, Object> body = new HashMap<>();
        body.put("bookingId", bookingId);
        request.setBody(body);

        Response response = client.sendRequest(request);
        if ("success".equals(response.getStatus())) {
            bookings.remove(bookingId);
        } else {
            throw new IOException("Error deleting booking: " + response.getMessage());
        }
        client.disconnect();
    }

    public void onClose() throws IOException {
        if (client != null) {
            client.disconnect();
        }
    }
}


//package com.cinemamanage.client;
//
//import com.cinemamanage.models.Booking;
//import com.cinemamanage.models.Hall;
//import com.cinemamanage.models.Session;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.reflect.TypeToken;
//
//import java.io.IOException;
//import java.lang.reflect.Type;
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//
//public class CinemaService {
//    private Client client;
//    private Map<String, Booking> bookings;
//    private Map<String, Session> sessions;
//    private Map<String, Hall> halls;
//    private final Gson gson;
//
//    public CinemaService(String host, int port) throws IOException {
//        this.client = new Client(host, port);
//        this.gson = new GsonBuilder().create();
//        this.bookings = new HashMap<>();
//        this.sessions = new HashMap<>();
//        this.halls = new HashMap<>();
//        fetchAllData();
//    }
//
//    public void fetchAllData() throws IOException {
//        fetchAllHalls();
//        fetchAllSessions();
//        fetchAllBookings();
//    }
//
//    public void fetchAllHalls() throws IOException {
//        client.connect();
//        Request request = new Request();
//        Map<String, String> headers = new HashMap<>();
//        headers.put("action", "hall/getAll");
//        request.setHeaders(headers);
//
//        Response response = client.sendRequest(request);
//
//        if ("success".equals(response.getStatus())) {
//            Type hallMapType = new TypeToken<Map<String, Hall>>() {}.getType();
//            halls = gson.fromJson(response.getMessage(), hallMapType);
//        } else {
//            throw new IOException("Error fetching halls: " + response.getMessage());
//        }
//        client.disconnect();
//    }
//
//    public void fetchAllSessions() throws IOException {
//        client.connect();
//        Request request = new Request();
//        Map<String, String> headers = new HashMap<>();
//        headers.put("action", "session/getAll");
//        request.setHeaders(headers);
//
//        Response response = client.sendRequest(request);
//
//        if ("success".equals(response.getStatus())) {
//            Type sessionMapType = new TypeToken<Map<String, Session>>() {}.getType();
//            sessions = gson.fromJson(response.getMessage(), sessionMapType);
//        } else {
//            throw new IOException("Error fetching sessions: " + response.getMessage());
//        }
//        client.disconnect();
//    }
//
//    public void fetchAllBookings() throws IOException {
//        client.connect();
//        Request request = new Request();
//        Map<String, String> headers = new HashMap<>();
//        headers.put("action", "booking/getAll");
//        request.setHeaders(headers);
//
//        Response response = client.sendRequest(request);
//        if ("success".equals(response.getStatus())) {
//            Type bookingMapType = new TypeToken<Map<String, Booking>>() {}.getType();
//            bookings = gson.fromJson(response.getMessage(), bookingMapType);
//        } else {
//            throw new IOException("Error fetching bookings: " + response.getMessage());
//        }
//        client.disconnect();
//    }
//
//    public Map<String, Hall> getAllHalls() {
//        return halls;
//    }
//
//    public Map<String, Session> getAllSessions() {
//        return sessions;
//    }
//
//    public Map<String, Booking> getAllBookings() {
//        return bookings;
//    }
//
//    public void addHall(Hall hall) throws IOException {
//        client.connect();
//        Request request = new Request();
//        Map<String, String> headers = new HashMap<>();
//        headers.put("action", "hall/add");
//        request.setHeaders(headers);
//        Map<String, Object> body = new HashMap<>();
//        body.put("hall", hall);
//        request.setBody(body);
//
//        Response response = client.sendRequest(request);
//        if ("success".equals(response.getStatus())) {
//            halls.put(String.valueOf(hall.getHallNumber()), hall);
//        } else {
//            throw new IOException("Error adding hall: " + response.getMessage());
//        }
//        client.disconnect();
//    }
//
//    public void deleteHall(int hallNumber) throws IOException {
//        client.connect();
//        Request request = new Request();
//        Map<String, String> headers = new HashMap<>();
//        headers.put("action", "hall/delete");
//        request.setHeaders(headers);
//        Map<String, Object> body = new HashMap<>();
//        body.put("hallNumber", hallNumber);
//        request.setBody(body);
//
//        Response response = client.sendRequest(request);
//        if ("success".equals(response.getStatus())) {
//            halls.remove(String.valueOf(hallNumber));
//        } else {
//            throw new IOException("Error deleting hall: " + response.getMessage());
//        }
//        client.disconnect();
//    }
//
//    public void addSession(Session session) throws IOException {
//        client.connect();
//        Request request = new Request();
//        Map<String, String> headers = new HashMap<>();
//        headers.put("action", "session/add");
//        request.setHeaders(headers);
//        Map<String, Object> body = new HashMap<>();
//        body.put("session", session);
//        request.setBody(body);
//
//        Response response = client.sendRequest(request);
//        if ("success".equals(response.getStatus())) {
//            sessions.put(session.getSessionId(), session);
//        } else {
//            throw new IOException("Error adding session: " + response.getMessage());
//        }
//        client.disconnect();
//    }
//
//    public void deleteSession(String sessionId) throws IOException {
//        client.connect();
//        Request request = new Request();
//        Map<String, String> headers = new HashMap<>();
//        headers.put("action", "session/delete");
//        request.setHeaders(headers);
//        Map<String, Object> body = new HashMap<>();
//        body.put("sessionId", sessionId);
//        request.setBody(body);
//
//        Response response = client.sendRequest(request);
//        if ("success".equals(response.getStatus())) {
//            sessions.remove(sessionId);
//        } else {
//            throw new IOException("Error deleting session: " + response.getMessage());
//        }
//        client.disconnect();
//    }
//
//    public void onClose() throws IOException {
//        if (client != null) {
//            client.disconnect();
//        }
//    }
//}
