package hw17.messageclient.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class ClientNIO implements NetworkClient{
    private static final Logger logger = LoggerFactory.getLogger(ClientNIO.class);
    private final int port;
    private final String host;
    private final Thread thread;
    private SocketChannel channel;
    private boolean isConnected;
    private ResponseCallback<String> callback;

    public ClientNIO(String host, int port) {
        this.port = port;
        this.host = host;
        thread = new Thread(this::handler);
    }

    @Override
    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public void connect() {
        thread.start();
    }

    @Override
    public void setResponseHandler(ResponseCallback<String> response){
        this.callback = response;
    }

    @Override
    public void send(String request) {
        ByteBuffer buffer = ByteBuffer.allocate(1000);
        final byte[] array = request.getBytes();
        try {
            for (byte b : array) {
                buffer.put(b);
                if (buffer.position() == buffer.limit()) {
                    buffer.flip();
                    channel.write(buffer);
                    buffer.flip();
                }
            }
            if (buffer.hasRemaining()) {
                buffer.flip();
                channel.write(buffer);
            }
        } catch (IOException e) {
            logger.error("Error send request to server ");
        }
    }

    private void handler() {
        try {
            try (SocketChannel socketChannel = SocketChannel.open()) {
                channel = socketChannel;
                socketChannel.configureBlocking(false);

                final var inetSocketAddress = new InetSocketAddress(host, port);
                socketChannel.connect(inetSocketAddress);
                while (!socketChannel.finishConnect()) {
                    logger.info("connect to {}", inetSocketAddress.toString());
                }

                isConnected = true;

                handleResponse(socketChannel);
            }
        } catch (Exception ex) {
            logger.error("error", ex);
        }
    }

    private void handleResponse(SocketChannel socketChannel) throws IOException {
        final Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_READ);
        while (!Thread.currentThread().isInterrupted()) {
            logger.info("waiting for response");
            if (selector.select() > 0) { //This method performs a blocking
                final Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    final SelectionKey key = keys.next();
                    if (key.isReadable()) {
                        processServerResponse((SocketChannel) key.channel());
                        keys.remove();
                    }
                }
            }
        }
    }

    private void processServerResponse(SocketChannel socketChannel) throws IOException {
        final ByteBuffer buffer = ByteBuffer.allocate(1000);
        final StringBuilder response = new StringBuilder();
        while (socketChannel.read(buffer) > 0) {
            buffer.flip();
            final String responsePart = StandardCharsets.UTF_8.decode(buffer).toString();
            response.append(responsePart);
            buffer.flip();
        }
        final var responseStr = response.toString().replace("\n", "").replace("\r", "");
        logger.info("response: {}", responseStr);
        callback.accept(responseStr);
    }

}
