package org.cmu.picky.services;

import org.cmu.picky.db.MySQLConnectionFactory;
import org.cmu.picky.model.Location;
import org.cmu.picky.model.Photo;
import org.cmu.picky.model.Picky;
import org.cmu.picky.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PickyService {

    private final static Logger logger = LoggerFactory.getLogger(PickyService.class);

    public PickyService() {}

    public List<Picky> getUserTimeline(User user) {
        final String query = "SELECT P.id, P.title, LP.id AS leftPhotoId, LP.url AS leftPhotoUrl, RP.id AS rightPhotoId, " +
                             "RP.url AS rightPhotoUrl, L.id as locationId, L.latitude, L.longitude, P.leftVotes, P.rightVotes\n" +
                             "FROM Picky P\n" +
                             "INNER JOIN Photo LP ON P.leftPhotoId = LP.id\n" +
                             "INNER JOIN Photo RP ON P.leftPhotoId = LP.id\n" +
                             "INNER JOIN Location L ON P.locationId = L.id\n" +
                             "WHERE P.userId = ?";

        try (Connection connection = MySQLConnectionFactory.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, user.getId());

            ResultSet rs = preparedStatement.executeQuery();
            List<Picky> pickies = new ArrayList<Picky>();

            while (rs.next()) {
                Picky picky = new Picky();
                Photo leftPhoto = new Photo();
                Photo rightPhoto = new Photo();
                Location location = new Location();

                leftPhoto.setId(rs.getInt("leftPhotoId"));
                leftPhoto.setUrl(rs.getString("leftPhotoUrl"));

                rightPhoto.setId(rs.getInt("rightPhotoId"));
                rightPhoto.setUrl(rs.getString("rightPhotoUrl"));

                location.setId(rs.getInt("locationId"));
                location.setLatitude(rs.getDouble("locationLatitude"));
                location.setLongitude(rs.getDouble("locationLongitude"));

                picky.setId(rs.getInt("id"));
                picky.setTitle(rs.getString("title"));
                picky.setUser(user);
                picky.setLeftPhoto(leftPhoto);
                picky.setRightPhoto(rightPhoto);
                picky.setLocation(location);
                picky.setLeftVotes(rs.getInt("leftVotes"));
                picky.setRightVotes(rs.getInt("rightVotes"));
                pickies.add(picky);
            }
            return pickies;
        } catch (SQLException ex) {
            logger.error("Problem executing statement", ex);
        }
        return null;
    }

}
