package ru.otus.processor;

import ru.otus.Message;

public class ProcessReplace11and13 implements Processor {

    @Override
    public Message process(Message message) {
        String field11Value = message.getField11().toUpperCase();
        Message tempMessage = message.toBuilder().field11(message.getField13().toUpperCase()).build();
        tempMessage = tempMessage.toBuilder().field13(field11Value).build();
        return tempMessage;
    }
}
