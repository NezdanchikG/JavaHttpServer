package com.Nezdanchik.spbpu;


import java.util.HashMap;
import java.util.Map;

public class PathListener {
    private final Map<String, Map<HttpMethod, RequestHandler>> routes = new HashMap<>();

    public void addListener(String path, HttpMethod method, RequestHandler handler) {
        routes.computeIfAbsent(path, k -> new HashMap<>()).put(method, handler);
    }

    public RequestHandler getHandler(String path, HttpMethod method) {
        Map<HttpMethod, RequestHandler> methodHandlers = routes.get(path);
        if (methodHandlers != null) {
            return methodHandlers.get(method);
        }
        return null;
    }
}
