package hw17.messageclient;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import hw17.messageclient.MessageClient.ClientState;
import hw17.messageclient.network.NetworkClient;

public class MessageClientTest {
    private final static String CLIENT_NAME = "front";

    @Test
    public void connectToUnaviableServerTest() {
        final NetworkClient networkClientMock = Mockito.mock(NetworkClient.class);
        Mockito.doThrow(new RuntimeException()).when(networkClientMock).connect();

        final var messageClient = new MessageClient(CLIENT_NAME, networkClientMock);
        assertThatThrownBy(messageClient::connect).isInstanceOf(MessageClientException.class);
    }

    @Test
    public void connectingTimeOut(){
        final NetworkClient networkClientMock = Mockito.mock(NetworkClient.class);
        Mockito.doNothing().when(networkClientMock).connect();
        Mockito.doReturn(false).when(networkClientMock).isConnected();

        final var messageClient = new MessageClient(CLIENT_NAME, networkClientMock);
        MessageClient.dissableSleep();

        assertThatThrownBy(messageClient::connect).isInstanceOf(MessageClientException.class);
    }

    @Test
    public void registredTest(){
        final NetworkClient networkClientMock = Mockito.mock(NetworkClient.class);
        Mockito.doNothing().when(networkClientMock).connect();
        Mockito.doReturn(true).when(networkClientMock).isConnected();

        final var messageClient = new MessageClient(CLIENT_NAME, networkClientMock);
        messageClient.connect();

        Mockito.verify(networkClientMock).send("{\"from\":\"front\",\"to\":\"\",\"data\":\"\"}");
    }

    @Test
    public void sendFromRegistredStateTest(){
        final NetworkClient networkClientMock = Mockito.mock(NetworkClient.class);
        Mockito.doNothing().when(networkClientMock).connect();

        final var messageClient = Mockito.spy( new MessageClient(CLIENT_NAME, networkClientMock));
        Mockito.doReturn(ClientState.REGISTRED).when(messageClient).getCurrentState();
        messageClient.send("db", "test");

        Mockito.verify(networkClientMock).send("{\"from\":\"front\",\"to\":\"db\",\"data\":\"test\"}");
    }

    @Test
    public void sendFromNotRegistredStateTest(){
        final NetworkClient networkClientMock = Mockito.mock(NetworkClient.class);
        Mockito.doNothing().when(networkClientMock).connect();

        final var messageClient = Mockito.spy( new MessageClient(CLIENT_NAME, networkClientMock));
        Mockito.doReturn(ClientState.CONNECTED).when(messageClient).getCurrentState();
        assertThatThrownBy(() -> messageClient.send("db", "test")).isInstanceOf(MessageClientException.class);

        Mockito.verify(networkClientMock, never()).send(Mockito.anyString());
    }
}
