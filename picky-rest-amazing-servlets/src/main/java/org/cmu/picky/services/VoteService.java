package org.cmu.picky.services;

import org.cmu.picky.db.MySQLConnectionFactory;
import org.cmu.picky.model.Picky;
import org.cmu.picky.model.User;
import org.cmu.picky.model.Vote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VoteService {

    private static final  Logger logger = LoggerFactory.getLogger(VoteService.class);

    public VoteService() {}

    public boolean userVoted(int userId, int pickyId) {
        final String query = "SELECT id FROM UserVote WHERE userId = ? AND pickyId = ?";

        try (Connection connection = MySQLConnectionFactory.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, pickyId);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            logger.error("Problem executing statement", ex);
        }
        return false;

    }

    public boolean vote(int userId, int pickyId, Vote vote) {
        final String insertQuery = "INSERT INTO UserVote(userId, pickyId) VALUES(?, ?)";
        final String updateQuery = (vote == Vote.RIGHT ?
                "UPDATE Picky SET rightPhotoId = rightPhotoId + 1 " :
                "UPDATE Picky SET leftPhotoId = leftPhotoId + 1 ") + "WHERE id = ?";

        try (Connection connection = MySQLConnectionFactory.getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, pickyId);
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setInt(1, pickyId);
            preparedStatement.executeUpdate();
            connection.commit();
            return true;
        } catch (SQLException ex) {
            logger.error("Problem executing statement", ex);
        }
        return false;
    }

}
