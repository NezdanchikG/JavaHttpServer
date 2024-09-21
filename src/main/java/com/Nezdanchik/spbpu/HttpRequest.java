package com.Nezdanchik.spbpu;


import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private String method;
    private String path;
    private Map<String, String> headers;
    private String body;

    public HttpRequest(String method, String path, Map<String, String> headers, String body) {
        this.method = method;
        this.path = path;
        this.headers = headers;
        this.body = body;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public static HttpRequest parse(String rawRequest) {
        String[] requestParts = rawRequest.split("\r\n\r\n", 2);
        String headerPart = requestParts[0];
        String bodyPart = requestParts.length > 1 ? requestParts[1] : "";

        String[] headerLines = headerPart.split("\r\n");
        String[] requestLine = headerLines[0].split(" ");
        String method = requestLine[0];
        String path = requestLine[1];

        Map<String, String> headers = new HashMap<>();
        for (int i = 1; i < headerLines.length; i++) {
            String[] header = headerLines[i].split(": ", 2);
            headers.put(header[0], header[1]);
        }

        return new HttpRequest(method, path, headers, bodyPart);
    }
}
