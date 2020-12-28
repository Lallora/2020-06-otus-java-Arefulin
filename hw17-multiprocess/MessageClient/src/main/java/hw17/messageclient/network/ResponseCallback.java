package hw17.messageclient.network;

import java.util.function.Consumer;

public interface ResponseCallback<T> extends Consumer<T> {
}
