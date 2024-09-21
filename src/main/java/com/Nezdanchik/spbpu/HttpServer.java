package com.Nezdanchik.spbpu;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HttpServer {

    private final Selector selector;
    private final ServerSocketChannel serverSocketChannel;
    private final Map<String, RequestHandler> handlers = new HashMap<>();
    private volatile boolean running = true;

    public HttpServer(String host, int port) throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(host, port));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void start() throws IOException {
        System.out.println("Server started...");
        while (running) {
            selector.select();
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
            while (keys.hasNext()) {
                SelectionKey key = keys.next();
                keys.remove();

                if (!key.isValid()) continue;

                if (key.isAcceptable()) {
                    acceptConnection(key);
                } else if (key.isReadable()) {
                    handleRequest(key);
                }
            }
        }
        selector.close();
        serverSocketChannel.close();
    }

    public void stop() throws IOException {
        running = false;
        selector.wakeup();
    }

    private void acceptConnection(SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
    }

    private void handleRequest(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int bytesRead = socketChannel.read(buffer);

        if (bytesRead == -1) {
            socketChannel.close();
            return;
        }

        buffer.flip();
        byte[] data = new byte[buffer.limit()];
        buffer.get(data);

        String request = new String(data);
        HttpRequest httpRequest = HttpRequest.parse(request);

        RequestHandler handler = handlers.get(httpRequest.getMethod() + " " + httpRequest.getPath());
        HttpResponse response;

        if (handler != null) {
            response = handler.handle(httpRequest);
        } else {
            response = new HttpResponse(404, "Not Found");
        }

        socketChannel.write(ByteBuffer.wrap(response.toBytes()));
        socketChannel.close();
    }

    public void addHandler(String method, String path, RequestHandler handler) {
        handlers.put(method + " " + path, handler);
    }
}
