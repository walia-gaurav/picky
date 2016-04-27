package org.cmu.picky.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cmu.picky.model.Picky;
import org.cmu.picky.model.User;
import org.cmu.picky.services.AuthService;
import org.cmu.picky.services.PickyService;
import org.cmu.picky.util.ServletUtils;

import com.google.gson.Gson;

public class UploadServlet extends HttpServlet {

    public static final int BAD_STATUS = 400;

    private static PickyService pickyService;
    private static AuthService authService;

    public static void init(AuthService _authService, PickyService _pickyService) {
        authService = _authService;
        pickyService = _pickyService;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Gson gson = new Gson();
        User user = authService.getUser(request);
        Picky picky = gson.fromJson(request.getParameter("picky"), Picky.class);

        picky.setUser(user);
        boolean result = pickyService.save(picky);

        ServletUtils.addJSONSettings(response);
        if (result) {
            response.getOutputStream().print(gson.toJson(picky));
            response.getOutputStream().flush();
        } else {
            response.setStatus(BAD_STATUS);
        }
    }

}