package org.cmu.picky.servlets;

import com.google.gson.Gson;
import org.cmu.picky.model.Picky;
import org.cmu.picky.model.User;
import org.cmu.picky.model.Vote;
import org.cmu.picky.services.AuthService;
import org.cmu.picky.services.PickyService;
import org.cmu.picky.services.VoteService;
import org.cmu.picky.util.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class VotePickyServlet extends HttpServlet {

    private static PickyService pickyService;
    private static AuthService authService;
    private static VoteService voteService;

    public static void init(AuthService _authService, PickyService _pickyService, VoteService _voteService) {
        authService = _authService;
        pickyService = _pickyService;
        voteService = _voteService;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int pickyId = Integer.parseInt(request.getParameter("id"));
        Vote vote = Vote.valueOf(request.getParameter("vote"));
        User user = authService.getUser(request);
        Picky picky = pickyService.get(pickyId);
        Gson gson  = new Gson();

        ServletUtils.addJSONSettings(response);
        if (picky == null) {
            ServletUtils.addError(response, gson, "Picky does not exists");
            response.setStatus(ServletUtils.NOT_FOUND);
            return;
        } if (voteService.userVoted(user.getId(), pickyId)) {
            ServletUtils.addError(response, gson, "Already voted");
            response.setStatus(ServletUtils.BAD_STATUS);
            return;
        }
        boolean result = voteService.vote(user.getId(), picky.getId(),vote);

        if (!result) {
            ServletUtils.addError(response, gson, "Could vote for picky");
            response.setStatus(ServletUtils.BAD_STATUS);
        }
    }

}