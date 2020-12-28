package hw17.controller;

import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import hw17.messageclient.MessageClient;
import hw17.model.User;
import hw17.server.message.InterprocessMessage;


@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    private final SimpMessagingTemplate template;
    private final MessageClient messageClient;

    public MessageController( MessageClient messageClient, SimpMessagingTemplate template) {
        this.template = template;
        this.messageClient = messageClient;
        this.messageClient.connect();
        this.messageClient.setResponseHandler(this::sendUserList);
    }

    @MessageMapping("/newUser")
    public void onNewUser(User user) {
        logger.info("got new user :{}", user);
        final Gson gson = new Gson();
        messageClient.send("db", gson.toJson(user));
    }

    private void sendUserList(InterprocessMessage response){
        logger.debug("recived message: {}", response.toString());
        this.template.convertAndSend("/topic/users", response.getData());
    }
}
