package ru.otus.servlet;

import com.google.gson.Gson;
import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceUser;
import ru.otus.services.TemplateProcessor;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UsersServlet extends HttpServlet {

    private static final int MAX_INACTIVE_INTERVAL = 30;


    private final TemplateProcessor templateProcessor;
    private final DBServiceUser dbServiceUser;
    private final Gson gson;


    public UsersServlet(TemplateProcessor templateProcessor, DBServiceUser dbServiceUser, Gson gson) {
        this.templateProcessor = templateProcessor;
        this.dbServiceUser = dbServiceUser;
        this.gson = gson;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Map<String, Object> data = new HashMap<>();
        List<User> users = this.dbServiceUser.getAllUsers();

        data.put("users", users);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding("UTF-8");
        if (request.isUserInRole("admin")){
            response.getWriter().println(templateProcessor.getPage("users.html", data));
        } else {
            response.getWriter().println(templateProcessor.getPage("guest.html", data));
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);

        BufferedReader reader = request.getReader();
        String body = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        User user = gson.fromJson(body, User.class);
        this.dbServiceUser.saveUser(user);
        List<User> users = this.dbServiceUser.getAllUsers();
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.print(gson.toJson(users));
    }
}
