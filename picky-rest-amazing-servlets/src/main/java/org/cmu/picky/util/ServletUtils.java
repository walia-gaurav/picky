package org.cmu.picky.util;


import com.google.gson.Gson;
import org.cmu.picky.model.Error;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ServletUtils {

    public static final int BAD_STATUS = 400;
    public static final int UNAUTHORIZED_STATUS = 401;

    public static void addError(HttpServletResponse response, Gson gson, String message) throws IOException {
        Error error = new Error();

        error.setMessage(message);
        response.getOutputStream().print(gson.toJson(error));
        response.setStatus(BAD_STATUS);
    }

    public static void addJSONSettings(HttpServletResponse response) {
        response.setContentType("application/json");
        response.setHeader("Content-Type", "text/plain; charset=UTF-8");
    }

}
