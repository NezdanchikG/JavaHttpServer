package com.Nezdanchik.spbpu;



import java.util.HashMap;
import java.util.Map;

public class HttpHeaders {
    private final Map<String, String> headers = new HashMap<>();

    public void addHeader(String name, String value) {
        headers.put(name, value);
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public Map<String, String> getAllHeaders() {
        return headers;
    }
}
