package org.cmu.picky.services;

import org.cmu.picky.db.MySQLConnectionFactory;
import org.cmu.picky.model.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LocationService {

    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);

    public boolean save(Location location) {
        final String selectQuery = "SELECT id, latitude, logintude FROM Location WHERE latitude = ? and logintude = ?";
        final String insertQuery = "INSERT INTO Location(latitude, longitude) VALUES(?, ?)";


        try (Connection connection = MySQLConnectionFactory.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);

            preparedStatement.setDouble(1, location.getLatitude());
            preparedStatement.setDouble(2, location.getLatitude());
            ResultSet rs = preparedStatement.executeQuery();

            if (!rs.next()) {
                preparedStatement = connection.prepareStatement(insertQuery);
                preparedStatement.setDouble(1, location.getLatitude());
                preparedStatement.setDouble(2, location.getLatitude());
                preparedStatement = connection.prepareStatement(selectQuery);
                preparedStatement.setDouble(1, location.getLatitude());
                preparedStatement.setDouble(2, location.getLatitude());
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
