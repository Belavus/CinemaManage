package main.java.server;

import com.google.gson.Gson;
import main.java.controller.CinemaController;
import main.java.controller.CinemaControllerFactory;

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
        String controllerName;
        String specificAction;
        String[] splitAction = action.split("/");
        controllerName = splitAction[0];
        specificAction = splitAction[1];

        CinemaController controller = CinemaControllerFactory.getCinemaController(controllerName);
        switch (specificAction) {
            case "add":
                return controller.addObject(request.getBody());
            case "delete":
                return controller.deleteObject(request.getBody());
            case "getAll":
                return controller.getAllObjects();
            case "generate":
                return controller.generateSeats(request.getBody());
            default:
                return new Response("error", "Unknown action: " + action);
        }
    }
}

