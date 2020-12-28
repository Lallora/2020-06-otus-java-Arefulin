package hw17.messageclient;

import hw17.messageclient.network.NetworkClient;
import hw17.messageclient.network.ResponseCallback;
import hw17.server.message.InterprocessMessage;
import hw17.server.message.ResponseMessage;
import hw17.server.message.ResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class MessageClient {

    public enum ClientState {
        DISCONECTED, CONNECTED, REGISTRED
    }

    private final static Logger logger = LoggerFactory.getLogger(MessageClient.class);
    private final String name;
    private final NetworkClient networkClient;
    private static boolean dissableSleep = false; // for unit testing
    private ClientState currentState = ClientState.DISCONECTED;
    private ResponseCallback<InterprocessMessage> responseCallback;
    private String messageCollector = "";

    public MessageClient(String name, NetworkClient networkClient) {
        this.name = name;
        this.networkClient = networkClient;
        this.networkClient.setResponseHandler(this::responseHandler);
    }

    public void setResponseHandler(ResponseCallback<InterprocessMessage> responseCallback) {
        this.responseCallback = responseCallback;
    }

    public void connect() {
        tryConnected();
        tryRegistred();
    }

    public void send(String toClient, String message) {
        if (getCurrentState() != ClientState.REGISTRED) {
            throw new MessageClientException("Unable send message from state " + currentState);
        }
        final InterprocessMessage requestMessage = new InterprocessMessage(name, toClient, message);
        networkClient.send(requestMessage.toJson());
    }

    public ClientState getCurrentState() {
        return currentState;
    }

    private void responseHandler(String response) {
        logger.debug("current client state: {}", currentState.name());
        logger.debug("recive response: {}", response);
        switch (getCurrentState()) {
            case CONNECTED:
                onConnected(response);
                break;
            case REGISTRED:
                onRegistred(response);
                break;
            default:
                throw new MessageClientException("Unable handled response in state " + currentState);
        }
    }

    private void onConnected(String response) {
        final var registredStr = new ResponseMessage(ResponseType.REGISTERED).toJson();
        if (response.equals(registredStr)) {
            currentState = ClientState.REGISTRED;
            logger.debug("current state changed to {}", currentState);
        }
    }

    private void onRegistred(String response) {
        if(responseCallback == null){
            return;
        }
        messageCollector += response;
        logger.debug("Message collector {}", messageCollector);
        final var mayBeResponse = InterprocessMessage.fromJson(messageCollector);
        if (mayBeResponse.isEmpty()) {
            return;
        }
        messageCollector = "";
        final var responseMsg = mayBeResponse.get();
        if(!responseMsg.isValid())
        {
            return;
        }
        if (!this.name.equals(responseMsg.getTo())) {
            logger.error("recived response for another client: {}", responseMsg.getTo());
            return;
        }
        responseCallback.accept(responseMsg);
    }

    private void tryConnected() {
        try {
            networkClient.connect();
        } catch (Exception e) {
            throw new MessageClientException("Could't connect to Message server");
        }
        for (int i = 10; i >= 0; i--) { // waiting 10 seconds
            if (networkClient.isConnected()) {
                break;
            }
            if (i == 0) {
                throw new MessageClientException("Connected to message server time out");
            }
            sleep();
        }
        currentState = ClientState.CONNECTED;
        logger.debug("current state changed to {}", currentState);
    }

    private static void sleep() {
        if (dissableSleep) { // for unit testing
            return;
        }
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(1));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void tryRegistred() {
        final InterprocessMessage requestMessage = new InterprocessMessage(name, "", "");
        networkClient.send(requestMessage.toJson());
    }

    public static void dissableSleep() {
        dissableSleep = true;
    }
}
