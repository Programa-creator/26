package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import org.apache.commons.lang3.StringUtils;

public class Client {

    private String host;
    private int port;
    Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() throws IOException {
        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println("Connected to server on " + host + ":" + port);

        new Thread(this::readMessages).start();
    }

    public void disconnect() throws IOException {
        out.close();
        in.close();
        socket.close();
        System.out.println("Disconnected from server");
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void sendCommand(String command) {
        out.println(command);
    }

    private void readMessages() {
        try {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        String host = "localhost";
        int port = 1234;

        Client client = new Client(host, port);
        client.connect();

        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        String inputLine;
        while ((inputLine = consoleReader.readLine()) != null) {
            if (StringUtils.isBlank(inputLine)) {
                continue;
            } else if (inputLine.startsWith("-")) {
                client.sendCommand(inputLine);
            } else {
                client.sendMessage(inputLine);
            }
        }

        client.disconnect();
        consoleReader.close();
    }
}
