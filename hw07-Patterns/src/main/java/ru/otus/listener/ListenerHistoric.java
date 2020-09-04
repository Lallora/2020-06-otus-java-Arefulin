package ru.otus.listener;

import ru.otus.History;
import ru.otus.Message;

import java.util.Date;

public class ListenerHistoric implements Listener {

    @Override
    public void onUpdated(Message oldMsg, Message newMsg) {
        String logString = String.format(new Date() + " Historical newMsg: %s", newMsg);
        History.writeToHistoryFile(logString);
    }
}