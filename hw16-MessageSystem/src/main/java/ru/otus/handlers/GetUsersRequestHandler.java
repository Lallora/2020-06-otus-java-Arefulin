package ru.otus.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.otus.dto.UserListDto;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.model.User;
import ru.otus.service.DBServiceUser;

import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class GetUsersRequestHandler implements RequestHandler<UserListDto> {
    private final DBServiceUser dbServiceUser;

    @Override
    public Optional<Message> handle(Message msg) {
        log.debug("Got message {}", msg.getType());
        var dto = new UserListDto(dbServiceUser.getAll().stream().map(User::toDto).collect(Collectors.toList()));
        return Optional.of(MessageBuilder.buildReplyMessage(msg, dto));
    }

}
