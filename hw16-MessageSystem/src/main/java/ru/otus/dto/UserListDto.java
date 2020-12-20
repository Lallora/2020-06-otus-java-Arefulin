package ru.otus.dto;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import ru.otus.messagesystem.client.ResultDataType;

import java.util.List;

@RequiredArgsConstructor
@Value
public class UserListDto extends ResultDataType {
    private final List<UserDto> users;
}
