package hw17.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class SocketChannelHelper {
    public static void send(SocketChannel socketChannel, String message) throws IOException{
        final ByteBuffer buffer = ByteBuffer.allocate(1000);
        final byte[] array = message.getBytes();
        for (byte b : array) {
            buffer.put(b);
            if (buffer.position() == buffer.limit()) {
                buffer.flip();
                socketChannel.write(buffer);
                buffer.flip();
            }
        }
        if (buffer.hasRemaining()) {
            buffer.flip();
            socketChannel.write(buffer);
        }
    }

    public static String recive(SocketChannel socketChannel) throws IOException {
        final ByteBuffer buffer = ByteBuffer.allocate(1000);
        final StringBuilder inputBuffer = new StringBuilder(100);

        while (socketChannel.read(buffer) > 0) {
            buffer.flip();
            final String input = StandardCharsets.UTF_8.decode(buffer).toString();
            buffer.flip();
            inputBuffer.append(input);
        }
        final String receiveMessage = inputBuffer.toString().replace("\n", "").replace("\r", "");
        return receiveMessage;
    }

}

