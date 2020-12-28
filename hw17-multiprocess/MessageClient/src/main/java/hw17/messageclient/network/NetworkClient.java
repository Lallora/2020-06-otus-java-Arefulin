package hw17.messageclient.network;

public interface NetworkClient {
    public void connect();

    public void send(String data);

    public void setResponseHandler(ResponseCallback<String> handler);

    public boolean isConnected();
}
