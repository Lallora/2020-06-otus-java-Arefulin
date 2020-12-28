package hw17.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

@Controller
public class LoginController {

    @GetMapping("/")
    public String loginPageView() {
        return "login.html";
    }

    @PostMapping("/login")
    public RedirectView handleLogin(@RequestParam Map<String, String> requestParams) {
        final var login = requestParams.get("login");
        final var password = requestParams.get("password");
        final boolean isAdmin = true;
        if (isAdmin) {
            return new RedirectView("/users", true);
        } else {
            return new RedirectView("/", true);
        }
    }
}
