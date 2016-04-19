package org.cmu.picky.servlets;

import com.google.gson.Gson;
import org.cmu.picky.model.User;
import org.cmu.picky.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginServlet extends HttpServlet {

    private static UserService userService;

    public static void init(UserService _userService) {
        userService = _userService;
    }

    public static final int UNAUTHORIZED_STATUS = 401;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        response.setContentType("application/json");
        response.setHeader("Content-Type", "text/plain; charset=UTF-8");
        if (username != null && !username.equals("") && password != null && !password.equals("")) {
            User user = userService.login(username, password);

            if (user != null) {
                Gson gson = new Gson();

                request.getSession().setAttribute("user", user);
                response.getOutputStream().print(gson.toJson(user));
                response.getOutputStream().flush();
            } else {
                response.setStatus(UNAUTHORIZED_STATUS);
            }
        } else {
            response.setStatus(UNAUTHORIZED_STATUS);
        }

    }

}