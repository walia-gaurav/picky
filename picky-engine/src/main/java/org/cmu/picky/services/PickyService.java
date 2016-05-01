package org.cmu.picky.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.cmu.picky.db.MySQLConnectionFactory;
import org.cmu.picky.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Operations with Picky model.
 */
public class PickyService {

    private static final Logger logger = LoggerFactory.getLogger(PickyService.class);

    private PhotoService photoService;
    private LocationService locationService;

    public PickyService(PhotoService photoService, LocationService locationService) {
        this.photoService = photoService;
        this.locationService = locationService;
    }

    /**
     * Returns all pickies that belongs to the given user.
     */
    public List<Picky> getMyPickies(User user) {
        final String query = "SELECT P.id, P.title, LP.id AS leftPhotoId, LP.url AS leftPhotoUrl, RP.id AS rightPhotoId, " +
                             "RP.url AS rightPhotoUrl, L.id AS locationId, L.latitude, L.longitude, P.leftVotes, P.rightVotes, \n" +
                             "P.expirationTime\n" +
                             "FROM Picky P\n" +
                             "INNER JOIN Photo LP ON P.leftPhotoId = LP.id\n" +
                             "INNER JOIN Photo RP ON P.rightPhotoId = RP.id\n" +
                             "INNER JOIN Location L ON P.locationId = L.id\n" +
                             "WHERE P.userId = ?";

        try (Connection connection = MySQLConnectionFactory.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, user.getId());

            ResultSet rs = preparedStatement.executeQuery();
            List<Picky> pickies = new ArrayList<>();

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

    /**
     * Fetch data from ResultSet and creates a Picky with it.
     */
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
        location.setLatitude(rs.getDouble("latitude"));
        location.setLongitude(rs.getDouble("longitude"));

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

    /**
     * Returns next user Picky so he can watch it and vote.
     */
    public Picky nextPick(User user) {
        final String query = "SELECT P.id, P.title, U.id AS userId, U.username, " +
                "LP.id AS leftPhotoId, LP.url AS leftPhotoUrl, RP.id AS rightPhotoId, " +
                "RP.url AS rightPhotoUrl, L.id AS locationId, L.latitude, L.longitude, P.leftVotes, P.rightVotes, \n" +
                "P.expirationTime\n" +
                "FROM Picky P\n" +
                "INNER JOIN Photo LP ON P.leftPhotoId = LP.id\n" +
                "INNER JOIN Photo RP ON P.rightPhotoId = RP.id\n" +
                "INNER JOIN Location L ON P.locationId = L.id\n" +
                "INNER JOIN User U ON P.userId = U.id\n" +
                "LEFT JOIN UserVote UV ON UV.pickyId = P.id AND UV.userId = P.userId\n" +
                "WHERE P.expirationTime >= ? AND UV.id IS NULL AND U.id = ?\n" +
                "LIMIT 1";

        try (Connection connection = MySQLConnectionFactory.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            Calendar calendar = Calendar.getInstance();

            preparedStatement.setDate(1, new java.sql.Date(calendar.getTimeInMillis()));
            preparedStatement.setInt(2, user.getId());
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                Picky picky = fillPicky(rs);

                addUser(rs, picky);
                return picky;
            }
        } catch (SQLException ex) {
            logger.error("Problem executing statement", ex);
        }
        return null;
    }

    /**
     * Fetch user data from the ResultSet and adds it to the Picky.
     */
    private void addUser(ResultSet rs, Picky picky) throws SQLException {
        User user = new User();

        user.setId(rs.getInt("userId"));
        user.setUsername(rs.getString("username"));
        picky.setUser(user);
    }

    /**
     * Save picky in the database. Returns true if everything went well else false.
     */
    public boolean save(Picky picky) {
        Photo leftPhoto = photoService.savePhoto(picky.getLeftPhoto().getBase64Image());

        if (leftPhoto == null) return false;
        picky.setLeftPhoto(leftPhoto);
        Photo rightPhoto = photoService.savePhoto(picky.getRightPhoto().getBase64Image());

        if (rightPhoto == null) return false;
        picky.setRightPhoto(rightPhoto);
        if (!locationService.save(picky.getLocation())) return false;

        final String insertQuery = "INSERT INTO Picky(title, userId, leftPhotoId, rightPhotoId, locationId, " +
                                   "leftVotes, rightVotes, expirationTime) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        final String selectQuery = "SELECT id FROM Picky WHERE userId = ? AND expirationTime = ?";

        try (Connection connection = MySQLConnectionFactory.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            Calendar calendar = Calendar.getInstance();

            calendar.add(Calendar.DATE, 2);
            preparedStatement.setString(1, picky.getTitle());
            preparedStatement.setInt(2, picky.getUser().getId());
            preparedStatement.setInt(3, picky.getLeftPhoto().getId());
            preparedStatement.setInt(4, picky.getRightPhoto().getId());
            preparedStatement.setInt(5, picky.getLocation().getId());
            preparedStatement.setInt(6, 0);
            preparedStatement.setInt(7, 0);
            preparedStatement.setDate(8, new java.sql.Date(calendar.getTimeInMillis()));
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setInt(1, picky.getUser().getId());
            preparedStatement.setDate(2, new java.sql.Date(calendar.getTimeInMillis()));
            ResultSet rs = preparedStatement.executeQuery();

            rs.next();
            picky.setId(rs.getInt("id"));
        } catch (SQLException ex) {
            logger.error("Problem executing statement", ex);
            return false;
        }
        return true;
    }

    /**
     * Get Picky with the given id.
     */
    public Picky get(int id) {
        final String query = "SELECT P.id, P.title, U.id AS userId, U.username, " +
                "LP.id AS leftPhotoId, LP.url AS leftPhotoUrl, RP.id AS rightPhotoId, " +
                "RP.url AS rightPhotoUrl, L.id AS locationId, L.latitude, L.longitude, P.leftVotes, P.rightVotes, \n" +
                "P.expirationTime\n" +
                "FROM Picky P\n" +
                "INNER JOIN Photo LP ON P.leftPhotoId = LP.id\n" +
                "INNER JOIN Photo RP ON P.rightPhotoId = RP.id\n" +
                "INNER JOIN Location L ON P.locationId = L.id\n" +
                "INNER JOIN User U ON P.userId = U.id\n" +
                "WHERE P.id = ?";

        try (Connection connection = MySQLConnectionFactory.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                Picky picky = fillPicky(rs);

                addUser(rs, picky);
                return picky;
            }
        } catch (SQLException ex) {
            logger.error("Problem executing statement", ex);
        }
        return null;
    }

    /**
     * Delete Picky with the given id. Returns true if everything went well else false.
     */
    public boolean delete(int id) {
        final String deleteQuery = "DELETE FROM Picky WHERE id = ?";

        try (Connection connection = MySQLConnectionFactory.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            logger.error("Problem executing statement", ex);
        }
        return false;
    }

}
