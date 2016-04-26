package org.cmu.picky.servlets;

import com.google.gson.Gson;
import org.cmu.picky.model.Picky;
import org.cmu.picky.model.User;
import org.cmu.picky.services.AuthService;
import org.cmu.picky.services.PickyService;
import org.cmu.picky.util.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;

public class DeletePickyServlet extends HttpServlet {

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
        int id = Integer.parseInt(request.getParameter("id"));
        User user = authService.getUser(request);
        Gson gson  = new Gson();
        Picky picky = pickyService.get(id);

        ServletUtils.addJSONSettings(response);
        if (picky.getUser().getId() != user.getId()) {
            ServletUtils.addError(response, gson, "Invalid user");
            response.setStatus(BAD_STATUS);
        }
        boolean result = pickyService.delete(picky);

        if (result) {
            response.getOutputStream().print(gson.toJson(picky));
            response.getOutputStream().flush();
        } else {
            ServletUtils.addError(response, gson, "Could delete picky");
            response.setStatus(BAD_STATUS);
        }
    }

}