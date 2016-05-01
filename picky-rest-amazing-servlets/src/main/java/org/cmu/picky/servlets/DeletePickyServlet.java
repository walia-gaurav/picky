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

/**
 * Handles deleting a Picky.
 */
public class DeletePickyServlet extends HttpServlet {

    private static PickyService pickyService;
    private static AuthService authService;

    public static void init(AuthService _authService, PickyService _pickyService) {
        authService = _authService;
        pickyService = _pickyService;
    }

    /**
     * Expects the form parameter id and will delete the Picky with the given id after some validations.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        User user = authService.getUser(request);
        Picky picky = pickyService.get(id);
        Gson gson  = new Gson();

        ServletUtils.addJSONSettings(response);
        if (picky == null) {
            ServletUtils.addError(response, gson, "Picky does not exists");
            response.setStatus(ServletUtils.NOT_FOUND);
            return;
        } if (picky.getUser().getId() != user.getId()) {
            ServletUtils.addError(response, gson, "Invalid user");
            response.setStatus(ServletUtils.BAD_STATUS);
            return;
        }
        boolean result = pickyService.delete(picky.getId());

        if (result) {
            response.getOutputStream().print(gson.toJson(picky));
            response.getOutputStream().flush();
        } else {
            ServletUtils.addError(response, gson, "Could delete picky");
            response.setStatus(ServletUtils.BAD_STATUS);
        }
    }

}