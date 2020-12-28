package hw17.messageservice;

import ru.otus.messagesystem.client.ResultDataType;

public class MsMessage extends ResultDataType {
    private final String data;

    public MsMessage(String data) {
        this.data = data;
    }

    public String getData(){
        return data;
    }
}
