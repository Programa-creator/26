package org.example;

import java.net.Socket;
import java.time.LocalDateTime;

public class ClientConnection {

    private String name;
    private LocalDateTime connectionTime;
    private Socket socket;

    public ClientConnection(Socket socket) {
        this.name = "client-" + (int) (Math.random() * 1000); // генеруємо випадкове ім'я
        this.connectionTime = LocalDateTime.now();
        this.socket = socket;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getConnectionTime() {
        return connectionTime;
    }

    public Socket getSocket() {
        return socket;
    }
}
