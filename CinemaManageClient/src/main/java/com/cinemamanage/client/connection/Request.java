package com.cinemamanage.client.connection;

import java.util.Map;

public class Request {
    private Map<String, String> headers;
    private Map<String, Object> body;

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, Object> getBody() {
        return body;
    }

    public void setBody(Map<String, Object> body) {
        this.body = body;
    }
}
