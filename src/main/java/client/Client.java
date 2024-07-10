package main.java.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import main.java.server.Request;
import main.java.server.Response;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
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
        Hall hall = new Hall(1,5,6);
        body.put("hall", hall);

        Request request = new Request();
        request.setHeaders(headers);
        request.setBody(body);

        Response response = client.sendRequest(request);
        System.out.println("Response: " + response.getStatus() + " - " + response.getMessage());

        // =========== Additional requests to get all halls and display the result ===========
        Gson gson = new Gson();
        Type hallMapType = new TypeToken<Map<String, Hall>>() {}.getType();

        headers.put("action", "hall/getAll");
        request.setHeaders(headers);
        request.setBody(new HashMap<>()); // Empty body

        response = client.sendRequest(request);
        Map<String,Hall> hallMapFromJson = gson.fromJson(response.getMessage(), hallMapType);
        System.out.println(hallMapFromJson.get("1"));
        System.out.println("All Halls: " + hallMapFromJson);
    }
}