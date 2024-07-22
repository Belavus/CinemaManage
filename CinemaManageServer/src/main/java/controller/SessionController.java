package main.java.controller;

import main.java.models.Session;
import main.java.server.Response;

import java.util.Map;

public class SessionController extends CinemaController{
    @Override
    public Response addObject(Map<String, Object> body) {
        try {
            String json = gson.toJson(body.get("session"));
            Session session = gson.fromJson(json, Session.class);
            cinemaService.addSession(session);
            return new Response("success", "Session added successfully");
        } catch (Exception e) {
            return new Response("error", e.getMessage());
        }
    }

    @Override
    public Response deleteObject(Map<String, Object> body) {
        try {
            String sessionId = (String) body.get("sessionId");
            cinemaService.deleteSession(sessionId);
            return new Response("success", "Session deleted successfully");
        } catch (Exception e) {
            return new Response("error", e.getMessage());
        }
    }

    @Override
    public Response getAllObjects() {
        try {
            Map<String, Session> sessions = cinemaService.getAllSessions();
            return new Response("success", gson.toJson(sessions));
        } catch (Exception e) {
            return new Response("error", e.getMessage());
        }
    }
}
