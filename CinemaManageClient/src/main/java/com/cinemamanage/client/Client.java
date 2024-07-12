package com.cinemamanage.client;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;

public class Client {
    private final String host;
    private final int port;
    private final Gson gson = new Gson();

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Response sendRequest(Request request) throws IOException {
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String requestJson = gson.toJson(request);
            out.println(requestJson);
            String responseJson = in.readLine();
            return gson.fromJson(responseJson, Response.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Error during communication with server.", e);
        }
    }

    public Gson getGson() {
        return gson;
    }
}
