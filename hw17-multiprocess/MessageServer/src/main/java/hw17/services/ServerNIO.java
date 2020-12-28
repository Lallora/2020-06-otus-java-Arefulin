package hw17.services;

import hw17.messageservice.MessageService;
import hw17.messageservice.handler.MessageHandler;
import hw17.server.ClientHandler;
import hw17.server.SocketChannelHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ServerNIO {
    private static final Logger logger = LoggerFactory.getLogger(ServerNIO.class);
    private final MessageService messageService;
    private final Map<SocketChannel, ClientHandler> clientsMap = new ConcurrentHashMap<>();

    public ServerNIO(MessageService messageService) {
        this.messageService = messageService;
        final var handler = new MessageHandler(clientsMap);
        this.messageService.addHandler(handler);
    }

    @PostConstruct
    public void startServer() throws IOException {
        final int PORT = 8090;
        start(PORT);
    }

    public void start(int port) throws IOException {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.configureBlocking(false);

            ServerSocket serverSocket = serverSocketChannel.socket();
            serverSocket.bind(new InetSocketAddress(port));

            try (Selector selector = Selector.open()) {
                serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

                while (!Thread.currentThread().isInterrupted()) {
                    logger.info("waiting for client");
                    if (selector.select() > 0) { // This method performs a blocking
                        performIO(selector);
                    }
                }
            }
        }
    }

    private void performIO(Selector selector) throws IOException {
        final Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

        while (keys.hasNext()) {
            final SelectionKey key = keys.next();
            if (key.isAcceptable()) {
                acceptConnection(key, selector);
            } else if (key.isReadable()) {
                final var socketChannel = (SocketChannel) key.channel();
                readWriteClient(socketChannel);
            }
            keys.remove();
        }
    }

    private void acceptConnection(SelectionKey key, Selector selector) throws IOException {
        logger.info("accept client connection");
        final var serverSocketChannel = (ServerSocketChannel) key.channel();
        final var socketChannel = serverSocketChannel.accept(); // The socket channel for the new connection
        logger.debug("New socket channel {}", socketChannel.toString());

        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);

        clientsMap.put(socketChannel, new ClientHandler(messageService));
    }

    private void readWriteClient(SocketChannel socketChannel) throws IOException {
        logger.debug("read from client");
        try {
            final String requestFromClient = SocketChannelHelper.recive(socketChannel);
            final String sendToMsResult = sendToMessageSystemClient(socketChannel, requestFromClient);
            SocketChannelHelper.send(socketChannel, sendToMsResult);
        } catch (Exception ex) {
            logger.error("error sending response", ex);

            final var clientHander = clientsMap.get(socketChannel);
            messageService.removeClient(clientHander.getName());
            clientsMap.remove(socketChannel);

            socketChannel.close();
        }
    }

    private String sendToMessageSystemClient(SocketChannel socketChannel, String message) {
        final var clientHander = clientsMap.get(socketChannel);
        return clientHander.recive(message);
    }
}
