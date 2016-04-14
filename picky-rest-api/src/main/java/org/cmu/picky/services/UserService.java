package org.cmu.picky.services;

import org.cmu.picky.model.User;
import org.cmu.picky.db.MySQLConnectionFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService {

    private Logger logger = LoggerFactory.getLogger(UserService.class);

    public User getUserByToken(String token) {
        final String query = "SELECT id, username FROM User WHERE token = ?";

        try (Connection connection = MySQLConnectionFactory.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, token);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                User user = new User();

                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                return user;
            }
        } catch (SQLException ex) {
            logger.error("Problem executing statement", ex);
        }
        return null;
    }

    public User login(String username, String password) {
        final String selectQuery = "SELECT id FROM User WHERE username = ? and password = ?";
        final String updateQuery = "UPDATE User SET token = ?  WHERE username = ?";

        try (Connection connection = MySQLConnectionFactory.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                User user = new User();

                user.setId(rs.getInt("id"));
                user.setUsername(username);
                user.setToken(generateRandomTokenValue());

                preparedStatement = connection.prepareStatement(updateQuery);
                preparedStatement.setString(1, user.getToken());
                preparedStatement.setString(2, user.getUsername());
                preparedStatement.executeUpdate();

                return user;
            }
        } catch (SQLException ex) {
            logger.error("Problem executing statement", ex);
        }
        return null;
    }

    public boolean logout(String username) {
        final String updateQuery = "UPDATE User SET token = ?  WHERE username = ?";

        try (Connection connection = MySQLConnectionFactory.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);

            preparedStatement.setString(1, null);
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();

            return true;
        } catch (SQLException ex) {
            logger.error("Problem executing statement", ex);
        }
        return false;
    }

    private String generateRandomTokenValue() {
        String randomUUID = UUID.randomUUID().toString();
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
        messageDigest.update(randomUUID.getBytes());
        byte[] digest = messageDigest.digest();
        StringBuilder stringBuilder = new StringBuilder();

        for (byte b : digest) {
            stringBuilder.append(String.format("%02x", b & 0xff));
        }
        return stringBuilder.toString();
    }

}
