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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PickyService {

    private static final Logger logger = LoggerFactory.getLogger(PickyService.class);

    public PickyService() {}

    public List<Picky> getMyPickies(User user) {
        final String query = "SELECT P.id, P.title, LP.id AS leftPhotoId, LP.url AS leftPhotoUrl, RP.id AS rightPhotoId, " +
                             "RP.url AS rightPhotoUrl, L.id AS locationId, L.latitude, L.longitude, P.leftVotes, P.rightVotes, \n" +
                             "P.expirationTime\n" +
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
                Picky picky = fillPicky(rs);
                picky.setUser(user);
                pickies.add(picky);
            }
            return pickies;
        } catch (SQLException ex) {
            logger.error("Problem executing statement", ex);
        }
        return null;
    }

    private Picky fillPicky(ResultSet rs) throws SQLException {
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
        picky.setLeftPhoto(leftPhoto);
        picky.setRightPhoto(rightPhoto);
        picky.setLocation(location);
        picky.setLeftVotes(rs.getInt("leftVotes"));
        picky.setRightVotes(rs.getInt("rightVotes"));

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        calendar.setTimeInMillis(rs.getDate("expirationTime").getTime());
        picky.setExpirationDate(simpleDateFormat.format(calendar.getTime()));

        return picky;
    }

    public List<Picky> getTimeline() {
        final String query = "SELECT P.id, P.title, U.id AS userId, U.username, " +
                "LP.id AS leftPhotoId, LP.url AS leftPhotoUrl, RP.id AS rightPhotoId, " +
                "RP.url AS rightPhotoUrl, L.id AS locationId, L.latitude, L.longitude, P.leftVotes, P.rightVotes, \n" +
                "P.expirationTime\n" +
                "FROM Picky P\n" +
                "INNER JOIN Photo LP ON P.leftPhotoId = LP.id\n" +
                "INNER JOIN Photo RP ON P.leftPhotoId = LP.id\n" +
                "INNER JOIN Location L ON P.locationId = L.id\n" +
                "INNER JOIN User U ON P.userId = U.id\n" +
                "WHERE expirationTime >= ?";

        try (Connection connection = MySQLConnectionFactory.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            Calendar calendar = Calendar.getInstance();

            preparedStatement.setDate(1, new java.sql.Date(calendar.getTimeInMillis()));
            ResultSet rs = preparedStatement.executeQuery();
            List<Picky> pickies = new ArrayList<Picky>();

            while (rs.next()) {
                Picky picky = fillPicky(rs);

                addUser(rs);
                pickies.add(picky);
            }
            return pickies;
        } catch (SQLException ex) {
            logger.error("Problem executing statement", ex);
        }
        return null;
    }

    private void addUser(ResultSet rs) throws SQLException {
        User user = new User();

        user.setId(rs.getInt("userId"));
        user.setUsername(rs.getString("username"));
    }



}
