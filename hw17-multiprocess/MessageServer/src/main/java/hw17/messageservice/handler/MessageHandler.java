package hw17.messageservice.handler;

import hw17.messageservice.MessageServiceException;
import hw17.messageservice.MsMessage;
import hw17.server.ClientHandler;
import hw17.server.SocketChannelHelper;
import hw17.server.message.InterprocessMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.client.ResultDataType;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageHelper;

import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class MessageHandler implements RequestHandler<ResultDataType> {
    private final Logger logger = LoggerFactory.getLogger(MessageHandler.class);
    private final Map<SocketChannel, ClientHandler> clientMap;

    public MessageHandler(Map<SocketChannel, ClientHandler> clientMap) {
        this.clientMap = clientMap;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        final var reciveMessage = convertFormMsMessage(msg);

        final String toClientName = reciveMessage.getTo();
        final var clientChannelList = getClientSocketChannels(toClientName);

        final String reciveMessageJson = reciveMessage.toJson();
        logger.debug("send to message {}", reciveMessageJson);

        for (final var clientChannel : clientChannelList) {
            try {
                SocketChannelHelper.send(clientChannel, reciveMessageJson);
            } catch (Exception e) {
                throw new MessageServiceException(
                        "Failed send message: " + reciveMessageJson + " to client " + clientChannel.toString());
            }
        }
        return Optional.empty();
    }

    private List<SocketChannel> getClientSocketChannels(String clientName) {
        return clientMap.entrySet().stream().filter(e -> e.getValue().getName().startsWith(clientName))
                .map(Map.Entry::getKey).collect(Collectors.toList());
    }

    private InterprocessMessage convertFormMsMessage(Message message) {
        final MsMessage msMessage = MessageHelper.getPayload(message);
        final var mayBeMessage = InterprocessMessage.fromJson(msMessage.getData());
        if (mayBeMessage.isEmpty()) {
            throw new MessageServiceException("Converter to InterprocessMessage failed from  " + message);
        }
        return mayBeMessage.get();
    }
}
