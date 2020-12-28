package hw17.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping({ "/users" })
    public String userListView(Model model) {
        return "users.html";
    }
}
