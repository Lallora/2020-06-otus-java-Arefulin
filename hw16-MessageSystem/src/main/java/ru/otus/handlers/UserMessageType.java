package ru.otus.handlers;

import lombok.RequiredArgsConstructor;
import ru.otus.messagesystem.message.MessageType;

@RequiredArgsConstructor
public enum UserMessageType implements MessageType {
    GET_ALL,
    ADD,
    GET;

    @Override
    public String getName() {
        return "users_" + name().toLowerCase();
    }
}
