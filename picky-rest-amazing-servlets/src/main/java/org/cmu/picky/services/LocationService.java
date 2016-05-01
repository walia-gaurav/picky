package org.cmu.picky.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.cmu.picky.db.MySQLConnectionFactory;
import org.cmu.picky.model.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Operations with Location model.
 */
public class LocationService {

    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);

    /**
     * Save the given Location in the database. Returns true if everything went well else false.
     */
    public boolean save(Location location) {
        final String selectQuery = "SELECT id, latitude, longitude FROM Location WHERE latitude = ? and longitude = ?";
        final String insertQuery = "INSERT INTO Location(latitude, longitude) VALUES(?, ?)";


        try (Connection connection = MySQLConnectionFactory.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);

            preparedStatement.setDouble(1, location.getLatitude());
            preparedStatement.setDouble(2, location.getLongitude());
            ResultSet rs = preparedStatement.executeQuery();

            if (!rs.next()) {
                preparedStatement = connection.prepareStatement(insertQuery);
                preparedStatement.setDouble(1, location.getLatitude());
                preparedStatement.setDouble(2, location.getLongitude());
                preparedStatement.executeUpdate();

                preparedStatement = connection.prepareStatement(selectQuery);
                preparedStatement.setDouble(1, location.getLatitude());
                preparedStatement.setDouble(2, location.getLongitude());
                rs = preparedStatement.executeQuery();
                rs.next();
            }
            location.setId(rs.getInt("id"));
        } catch (SQLException ex) {
            logger.error("Problem executing statement", ex);
            return false;
        }
        return true;
    }

}
