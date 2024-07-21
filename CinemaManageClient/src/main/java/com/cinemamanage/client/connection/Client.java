package com.cinemamanage.client.connection;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;

public class Client {
    private final String host;
    private final int port;
    private final Gson gson = new Gson();
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() throws IOException {
        System.out.println("Client connected");
        socket = new Socket(host, port);
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public Response sendRequest(Request request) throws IOException {
        if (out == null || in == null) {
            throw new IOException("Client is not connected.");
        }
        String requestJson = gson.toJson(request);
        out.println(requestJson);
        String responseJson = in.readLine();
        if (responseJson == null) {
            throw new IOException("Server closed connection unexpectedly.");
        }
        return gson.fromJson(responseJson, Response.class);
    }

    public void disconnect() throws IOException {
        System.out.println("Client disconnected.");
        if (in != null) in.close();
        if (out != null) out.close();
        if (socket != null) socket.close();
    }

    public Gson getGson() {
        return gson;
    }
}
