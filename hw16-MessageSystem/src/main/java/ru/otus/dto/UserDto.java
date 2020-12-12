package ru.otus.dto;

import lombok.Value;
import ru.otus.messagesystem.client.ResultDataType;
import ru.otus.model.Address;
import ru.otus.model.Phone;
import ru.otus.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Value
public class UserDto extends ResultDataType {
    private final long id;
    private final String name;
    private final int age;
    private final String address;
    private final List<String> phones;

    public User fromDto() {
        return new User(name, age, new Address(address), phones.stream().map(Phone::new).collect(Collectors.toList()));
    }

    public static UserDto id(long id) {
        return new UserDto(id, null, 0, null, null);
    }
}
