package ru.otus.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.otus.dto.UserDto;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.messagesystem.message.MessageHelper;
import ru.otus.model.User;
import ru.otus.service.DBServiceUser;
import ru.otus.service.DbServiceException;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class AddUserRequestHandler implements RequestHandler<UserDto> {
    private final DBServiceUser dbServiceUser;

    @Override
    public Optional<Message> handle(Message msg) {
        UserDto user = MessageHelper.getPayload(msg);
        try {
            User model = user.fromDto();
            dbServiceUser.saveUser(model);
            return Optional.of(MessageBuilder.buildReplyMessage(msg, model.toDto()));
        } catch (DbServiceException e) {
            log.error("Can't handle {} request from {}", msg.getType(), msg.getFrom(), e);
            return Optional.empty();
        }
    }
}
