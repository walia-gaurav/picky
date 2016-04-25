package org.cmu.picky.servlets;

import com.google.gson.Gson;
import org.cmu.picky.model.Photo;
import org.cmu.picky.services.PhotoService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UploadServlet extends HttpServlet {

    public static final int BAD_STATUS = 400;

    private static PhotoService photoService;

    public static void init(PhotoService _photoService) {
        photoService = _photoService;

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String imageBase64 = request.getParameter("image");

        Photo photo = photoService.savePhoto(imageBase64);
        if (photo != null) {
            Gson gson = new Gson();

            response.getOutputStream().print(gson.toJson(photo));
            response.getOutputStream().flush();
        } else {
            response.setStatus(BAD_STATUS);
        }
    }

}