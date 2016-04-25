package org.cmu.picky.services;


import org.cmu.picky.db.MySQLConnectionFactory;
import org.cmu.picky.model.Photo;
import org.cmu.picky.util.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PhotoService {

    private static final String FILE_PATH = "/var/webapp/upload/";
    private static final String UPLOAD_PATH = "/upload/";
    private static final String IMAGE_TYPE = "png";

    private Logger logger = LoggerFactory.getLogger(PhotoService.class);
    private TokenService tokenService;

    public PhotoService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public Photo savePhoto(String imageBase64) {
        BufferedImage bufferedImage = ImageUtils.decodeToImage(imageBase64);
        Photo photo = null;

        if (bufferedImage != null) {
            String filename = tokenService.getImageMD5(bufferedImage, IMAGE_TYPE);
            File outputFile = new File(FILE_PATH + filename);

            try {
                ImageIO.write(bufferedImage, IMAGE_TYPE, outputFile);
                final String insertQuery = "INSERT INTO Photo(url) VALUES(?)";
                final String selectQuery = "SELECT id, url FROM Photo WHERE url = ?";

                try (Connection connection = MySQLConnectionFactory.getConnection()) {
                    PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
                    preparedStatement.setString(1, filename);
                    ResultSet rs = preparedStatement.executeQuery();

                    if (!rs.next()) {
                        preparedStatement = connection.prepareStatement(insertQuery);
                        preparedStatement.setString(1, filename);
                        preparedStatement.executeUpdate();
                        preparedStatement = connection.prepareStatement(selectQuery);
                        preparedStatement.setString(1, filename);
                        rs = preparedStatement.executeQuery();
                        rs.next();
                    }
                    photo = new Photo();

                    photo.setId(rs.getInt("id"));
                    photo.setUrl(UPLOAD_PATH + rs.getString("url"));
                } catch (SQLException ex) {
                    logger.error("Problem executing statement", ex);
                }
            } catch (IOException e) {}
        }
        return photo;
    }
}
