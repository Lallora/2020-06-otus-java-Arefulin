package hw17.messageservice;

import java.util.Optional;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hw17.core.model.User;
import hw17.core.service.DBServiceUser;
import hw17.dto.UserDto;
import hw17.messageclient.MessageClient;
import hw17.server.message.InterprocessMessage;

public class MessageSystemClient {
    private final static Logger logger = LoggerFactory.getLogger(MessageSystemClient.class);
    private final MessageClient messageClient;
    private final DBServiceUser dbServiceUser;

    public MessageSystemClient(MessageClient messageClient, DBServiceUser dbServiceUser) {
        this.messageClient = messageClient;
        this.messageClient.setResponseHandler(this::responseHandler);
        this.dbServiceUser = dbServiceUser;
    }

    public void connect() {
        messageClient.connect();
    }

    private void responseHandler(InterprocessMessage response) {
        logger.debug("received message: {}", response);

        final String userJson = response.getData();
        final Optional<User> mayBeUser = jsonToUser(userJson);
        if (mayBeUser.isEmpty()) {
            logger.error("User {} not saved", userJson);
            return;
        }
        final var user = mayBeUser.get();

        saveNewUser(user);
        final var userListJson = getAllUsersJson();
        logger.debug("send to {} message {}", response.getFrom(), userListJson);
        send(response.getFrom(), userListJson);
    }

    private Optional<User> jsonToUser(String message) {
        final Gson gson = new Gson();
        try {
            final User user = gson.fromJson(message, User.class);
            return Optional.of(user);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private void saveNewUser(User user) {
        if (user.getName().isEmpty() || user.getPassword().isEmpty()) {
            logger.error("User {} does not contain and required fields", user.toString());
            return;
        }
        dbServiceUser.saveUser(user);
    }

    private String getAllUsersJson() {
        final var allUsersDto = dbServiceUser.getAllUsers().stream().map(UserDto::new).collect(Collectors.toList());
        final Gson gson = new Gson();
        return gson.toJson(allUsersDto);
    }

    public void send(String toClient, String message) {
        messageClient.send(toClient, message);
    }
}
