package hw17.server;

import hw17.messageservice.MessageService;
import hw17.server.message.ResponseMessage;
import hw17.server.message.ResponseType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientHandlerTest {
    @Test
    public void constructorTest() {
        assertDoesNotThrow(() -> new ClientHandler(null));
    }

    @Test
    public void reciveWithEmptyMessageTest() {
        assertEquals(new ResponseMessage(ResponseType.ERROR).toJson(), new ClientHandler(null).recive(""));
    }

    @Test
    public void reciveWithNotValidJsonTest() {
        assertEquals(new ResponseMessage(ResponseType.ERROR).toJson(), new ClientHandler(null).recive("{'from':}"));
    }

    @Test
    public void reciveRegistationClientTest() {
        final MessageService mockedMessageService = Mockito.mock(MessageService.class);

        final var clientHandler = new ClientHandler(mockedMessageService);
        assertThat(clientHandler.getName()).isEmpty();
        final String response = clientHandler.recive("{'from':'front','to':'db', 'data':'message content'}");
        assertThat(clientHandler.getName()).isEqualTo("front");

        Mockito.verify(mockedMessageService, Mockito.times(1)).addClient("front");
        assertEquals(response, new ResponseMessage(ResponseType.REGISTERED).toJson());
    }

    @Test
    public void sendMessageToMessageSystemTest(){
        final MessageService mockedMessageService = Mockito.mock(MessageService.class);

        final ClientHandler socketHandlerSpy = Mockito.spy(new ClientHandler(mockedMessageService));
        Mockito.doReturn("front").when(socketHandlerSpy).getName();

        final String response = socketHandlerSpy.recive("{'from':'front','to':'db', 'data':'message content'}");

        Mockito.verify(mockedMessageService, Mockito.times(1)).sendMessage(Mockito.any(), Mockito.any());
        assertEquals(response, new ResponseMessage(ResponseType.RECEIVED).toJson());
    }
}
