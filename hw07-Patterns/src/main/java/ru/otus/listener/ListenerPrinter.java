package ru.otus.listener;

import ru.otus.Message;

public class ListenerPrinter implements Listener {

    @Override
    public void onUpdated(Message oldMsg, Message newMsg) {
        var logString = String.format("\noldMsg:%s, \nnewMsg:%s", oldMsg, newMsg);
        System.out.println(logString);
    }
}
