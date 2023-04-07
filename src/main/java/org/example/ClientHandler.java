package org.example;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private String connectionName;

    public ClientHandler(Socket clientSocket, String connectionName) {
        this.clientSocket = clientSocket;
        this.connectionName = connectionName;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println(connectionName + ": " + inputLine);

                if (inputLine.startsWith("-file")) {
                    String filePath = inputLine.substring(6).trim();
                    System.out.println("File path: " + filePath);

                    receiveFile(filePath);
                } else {
                    String message = connectionName + ": " + inputLine;
                    Server.sendToAll(message);
                }
            }

            Server.activeConnections.remove(connectionName);

            String message = "[SERVER] " + connectionName + " відключився від сервера.";
            Server.sendToAll(message);

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveFile(String filePath) throws IOException {
        InputStream inputStream = clientSocket.getInputStream();
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);

        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, bytesRead);
        }

        fileOutputStream.close();
        System.out.println("File received and saved: " + filePath);
    }
}
