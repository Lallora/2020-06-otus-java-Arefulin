package hw17.server;

import hw17.messageservice.MessageService;
import hw17.server.message.InterprocessMessage;
import hw17.server.message.ResponseMessage;
import hw17.server.message.ResponseType;
import ru.otus.messagesystem.client.MsClient;

public class ClientHandler {
    private String clientName = "";
    private final MessageService messageService;
    private MsClient msClient;

    public ClientHandler(MessageService messageService) {
        this.messageService = messageService;
    }

    public String recive(String data) {
        final var mayBeMessage = InterprocessMessage.fromJson(data);
        if (mayBeMessage.isEmpty()) {
            return new ResponseMessage(ResponseType.ERROR).toJson();
        }
        final InterprocessMessage message = mayBeMessage.get();

        if (getName().isEmpty()) {
            clientName = message.getFrom();
            addMessageSystemClient(clientName);
            return new ResponseMessage(ResponseType.REGISTERED).toJson();
        }

        messageService.sendMessage(msClient, message);

        return new ResponseMessage(ResponseType.RECEIVED).toJson();
    }

    public String getName() {
        return clientName;
    }

    private void addMessageSystemClient(String clientName) {
        msClient = messageService.addClient(clientName);
    }
}
