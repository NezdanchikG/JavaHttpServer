package com.Nezdanchik.spbpu;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpServerTest {

    private HttpServer server;

    @AfterEach
    public void tearDown() throws IOException {
        if (server != null) {
            server.stop();
        }
    }

    @Test
    public void testGetRequest() throws IOException {
        server = new HttpServer("localhost", 8081);
        server.addHandler("GET", "/test", request -> new HttpResponse(200, "GET OK"));

        new Thread(() -> {
            try {
                server.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        try (SocketChannel client = SocketChannel.open()) {
            client.connect(new InetSocketAddress("localhost", 8081));
            String request = "GET /test HTTP/1.1\r\n\r\n";
            client.write(ByteBuffer.wrap(request.getBytes()));

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            client.read(buffer);
            buffer.flip();
            String response = new String(buffer.array()).trim();

            assertEquals("HTTP/1.1 200 OK\r\nContent-Length: 6\r\nContent-Type: text/plain\r\n\r\nGET OK", response);
        }
    }

    @Test
    public void testPostRequest() throws IOException {
        server = new HttpServer("localhost", 8081);
        server.addHandler("POST", "/submit", request -> new HttpResponse(200, "POST OK"));

        new Thread(() -> {
            try {
                server.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        try (SocketChannel client = SocketChannel.open()) {
            client.connect(new InetSocketAddress("localhost", 8081));
            String request = "POST /submit HTTP/1.1\r\nContent-Length: 9\r\n\r\nTest Data";
            client.write(ByteBuffer.wrap(request.getBytes()));

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            client.read(buffer);
            buffer.flip();
            String response = new String(buffer.array()).trim();

            assertEquals("HTTP/1.1 200 OK\r\nContent-Length: 7\r\nContent-Type: text/plain\r\n\r\nPOST OK", response);
        }
    }

    @Test
    public void testNotFound() throws IOException {
        server = new HttpServer("localhost", 8081);

        new Thread(() -> {
            try {
                server.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        try (SocketChannel client = SocketChannel.open()) {
            client.connect(new InetSocketAddress("localhost", 8081));
            String request = "GET /unknown HTTP/1.1\r\n\r\n";
            client.write(ByteBuffer.wrap(request.getBytes()));

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            client.read(buffer);
            buffer.flip();
            String response = new String(buffer.array()).trim();

            assertEquals("HTTP/1.1 404 Not Found\r\nContent-Length: 9\r\nContent-Type: text/plain\r\n\r\nNot Found", response);
        }
    }

    @Test
    public void testPutRequest() throws IOException {
        server = new HttpServer("localhost", 8081);
        server.addHandler("PUT", "/update", request -> new HttpResponse(200, "PUT OK"));

        new Thread(() -> {
            try {
                server.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        try (SocketChannel client = SocketChannel.open()) {
            client.connect(new InetSocketAddress("localhost", 8081));
            String request = "PUT /update HTTP/1.1\r\nContent-Length: 9\r\n\r\nUpdateMe";
            client.write(ByteBuffer.wrap(request.getBytes()));

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            client.read(buffer);
            buffer.flip();
            String response = new String(buffer.array()).trim();

            assertEquals("HTTP/1.1 200 OK\r\nContent-Length: 6\r\nContent-Type: text/plain\r\n\r\nPUT OK", response);
        }
    }

    @Test
    public void testPatchRequest() throws IOException {
        server = new HttpServer("localhost", 8081);
        server.addHandler("PATCH", "/modify", request -> new HttpResponse(200, "PATCH OK"));

        new Thread(() -> {
            try {
                server.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        try (SocketChannel client = SocketChannel.open()) {
            client.connect(new InetSocketAddress("localhost", 8081));
            String request = "PATCH /modify HTTP/1.1\r\nContent-Length: 7\r\n\r\nPatchMe";
            client.write(ByteBuffer.wrap(request.getBytes()));

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            client.read(buffer);
            buffer.flip();
            String response = new String(buffer.array()).trim();

            assertEquals("HTTP/1.1 200 OK\r\nContent-Length: 8\r\nContent-Type: text/plain\r\n\r\nPATCH OK", response);
        }
    }

    @Test
    public void testDeleteRequest() throws IOException {
        server = new HttpServer("localhost", 8081);
        server.addHandler("DELETE", "/remove", request -> new HttpResponse(200, "DELETE OK"));

        new Thread(() -> {
            try {
                server.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        try (SocketChannel client = SocketChannel.open()) {
            client.connect(new InetSocketAddress("localhost", 8081));
            String request = "DELETE /remove HTTP/1.1\r\n\r\n";
            client.write(ByteBuffer.wrap(request.getBytes()));

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            client.read(buffer);
            buffer.flip();
            String response = new String(buffer.array()).trim();

            assertEquals("HTTP/1.1 200 OK\r\nContent-Length: 9\r\nContent-Type: text/plain\r\n\r\nDELETE OK", response);
        }
    }
}
