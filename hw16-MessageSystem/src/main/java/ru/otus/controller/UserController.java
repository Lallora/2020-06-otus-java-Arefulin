package ru.otus.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.otus.model.User;
import ru.otus.service.DBServiceUser;

import java.util.Collection;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final DBServiceUser userService;

    @GetMapping("/users")
    public String users() {
        return "users";
    }

    @ModelAttribute("allUsers")
    public Collection<User> populateUsers() {
        return userService.getAll();
    }

}
