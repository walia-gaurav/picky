package org.cmu.picky.services;

import org.cmu.picky.db.MySQLConnectionFactory;
import org.cmu.picky.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserService {

    private Logger logger = LoggerFactory.getLogger(UserService.class);

    private TokenService tokenService = new TokenService();

    public UserService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

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
                user.setToken(tokenService.generateRandomTokenValue());

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

    public boolean signUp(String username, String password) {
        final String insertQuery = "INSERT INTO User(username, token, password) VALUES (?, NULL, ?);";

        try (Connection connection = MySQLConnectionFactory.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            logger.error("Problem executing statement", ex);
        }
        return false;
    }

    public boolean usernameInUse(String username) {
        final String query = "SELECT id FROM User WHERE username = ?";

        try (Connection connection = MySQLConnectionFactory.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            logger.error("Problem executing statement", ex);
        }
        return false;
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

    public boolean updatePassword(User user, String newPassword) {
        final String updateQuery = "UPDATE User SET password = ?  WHERE id = ?";

        try (Connection connection = MySQLConnectionFactory.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);

            preparedStatement.setInt(1, user.getId());
            preparedStatement.setString(2, newPassword);
            preparedStatement.executeUpdate();

            return true;
        } catch (SQLException ex) {
            logger.error("Problem executing statement", ex);
        }
        return false;
    }

}
