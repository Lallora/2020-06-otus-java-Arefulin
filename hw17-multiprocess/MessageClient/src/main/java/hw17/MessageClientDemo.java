package hw17;

import hw17.messageclient.MessageClient;
import hw17.messageclient.network.ClientNIO;
import hw17.messageclient.network.NetworkClient;
import hw17.server.message.InterprocessMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class MessageClientDemo {
    private final static Logger logger = LoggerFactory.getLogger(MessageClientDemo.class);

    public static void main(String[] args) {
        final String HOST = "localhost";
        final int PORT = 8080;
        final NetworkClient client = new ClientNIO(HOST, PORT);
        final var messageClient = new MessageClient("front", client);

        messageClient.connect();

        // needed send response {"response":"REGISTRED"}
        logger.info("Waiting response .... ");

        sleep();

        messageClient.send("db", "test");

        messageClient.setResponseHandler(MessageClientDemo::clientResponseHandler);
    }

    private static void clientResponseHandler(InterprocessMessage response) {
        logger.info("response form client {}", response);
    }

    private static void sleep() {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(5));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
