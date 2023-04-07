package org.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Server {

    private int port;
    private ServerSocket serverSocket;
    public static Map<String, Socket> activeConnections;

    public Server(int port) {
        this.port = port;
        activeConnections = new HashMap<>();
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());

                String connectionName = generateConnectionName();
                System.out.println("Active connection name: " + connectionName);

                activeConnections.put(connectionName, clientSocket);

                String message = "[SERVER] " + connectionName + " успішно підключився.";
                sendToAll(message);

                ClientHandler clientHandler = new ClientHandler(clientSocket, connectionName);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateConnectionName() {
        String uuid = UUID.randomUUID().toString();
        return "client-" + uuid.replaceAll("-", "");
    }

    public static void sendToAll(String message) {
        activeConnections.forEach((name, socket) -> {
            try {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}