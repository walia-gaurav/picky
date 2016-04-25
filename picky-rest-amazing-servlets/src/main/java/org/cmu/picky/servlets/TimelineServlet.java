package org.cmu.picky.servlets;

import com.google.gson.Gson;
import org.cmu.picky.model.Picky;
import org.cmu.picky.model.User;
import org.cmu.picky.services.AuthService;
import org.cmu.picky.services.PickyService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class TimelineServlet extends HttpServlet {

    private static PickyService pickyService;

    public static void init(PickyService _pickyService) {
        pickyService = _pickyService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Picky> timeline = pickyService.getTimeline();
        Gson gson = new Gson();

        response.getOutputStream().print(gson.toJson(timeline));
        response.getOutputStream().flush();

    }

}