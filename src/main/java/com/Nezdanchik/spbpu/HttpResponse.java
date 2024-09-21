package com.Nezdanchik.spbpu;


import java.nio.charset.StandardCharsets;

public class HttpResponse {
    private int statusCode;
    private String body;

    public HttpResponse(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public byte[] toBytes() {
        String response = "HTTP/1.1 " + statusCode + " " + getStatusMessage(statusCode) + "\r\n" +
                "Content-Length: " + body.length() + "\r\n" +
                "Content-Type: text/plain\r\n" +
                "\r\n" +
                body;
        return response.getBytes(StandardCharsets.UTF_8);
    }

    private String getStatusMessage(int statusCode) {
        switch (statusCode) {
            case 200: return "OK";
            case 404: return "Not Found";
            default: return "Unknown";
        }
    }
}
