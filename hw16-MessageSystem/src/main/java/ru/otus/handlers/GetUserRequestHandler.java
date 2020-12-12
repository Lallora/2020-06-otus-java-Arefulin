package ru.otus.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.dto.UserDto;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.messagesystem.message.MessageHelper;
import ru.otus.service.DBServiceUser;

import java.util.Optional;

@RequiredArgsConstructor
public class GetUserRequestHandler implements RequestHandler<UserDto> {
    private final DBServiceUser dbServiceUser;

    @Override
    public Optional<Message> handle(Message msg) {
        var id = ((UserDto) MessageHelper.getPayload(msg)).getId();
        return (id == -1 ? dbServiceUser.getRandom() : dbServiceUser.getUser(id)).map(
                user -> MessageBuilder.buildReplyMessage(msg, user.toDto()));
    }
}
