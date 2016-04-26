package org.cmu.picky.servlets;

import org.cmu.picky.model.User;
import org.cmu.picky.services.AuthService;
import org.cmu.picky.services.UserService;
import org.cmu.picky.util.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UpdatePasswordServlet extends HttpServlet {

    private static AuthService authService;
    private static UserService userService;

    public static void init(AuthService _authService, UserService _userService) {
        authService = _authService;
        userService = _userService;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = authService.getUser(request);
        String password = request.getParameter("password");

        if (!userService.updatePassword(user, password)) {
            response.setStatus(ServletUtils.BAD_STATUS);
        }
    }

}