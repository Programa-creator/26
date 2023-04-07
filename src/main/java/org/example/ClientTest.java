package org.example;

import java.io.IOException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ClientTest {

    private static final String HOST = "localhost";
    private static final int PORT = 5000;
    private static Client client;

    @BeforeAll
    static void setUp() throws IOException {
        client = new Client(HOST, PORT);
        client.connect();
    }

    @AfterAll
    static void tearDown() throws IOException {
        client.disconnect();
    }

    @Test
    void testDisconnect() throws IOException {
        client.disconnect();
        assertTrue(client.socket.isClosed());
    }
}