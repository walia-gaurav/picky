package org.cmu.picky.servlets;

import com.google.gson.Gson;
import org.cmu.picky.model.User;
import org.cmu.picky.services.UserService;
import org.cmu.picky.util.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles login.
 */
public class LoginServlet extends HttpServlet {

    private static UserService userService;

    public static void init(UserService _userService) {
        userService = _userService;
    }

    /**
     * Read username and password form parameters and check if they are correct.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        ServletUtils.addJSONSettings(response);
        if (username != null && !username.equals("") && password != null && !password.equals("")) {
            User user = userService.login(username, password);

            if (user != null) {
                Gson gson = new Gson();

                response.getOutputStream().print(gson.toJson(user));
                response.getOutputStream().flush();
            } else {
                response.setStatus(ServletUtils.UNAUTHORIZED_STATUS);
            }
        } else {
            response.setStatus(ServletUtils.UNAUTHORIZED_STATUS);
        }

    }

}