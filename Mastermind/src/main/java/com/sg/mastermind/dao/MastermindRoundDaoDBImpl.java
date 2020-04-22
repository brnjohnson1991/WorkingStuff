package com.sg.mastermind.dao;

import com.sg.mastermind.entity.Round;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author BRNJO
 */
@Repository
@Profile("db")
public class MastermindRoundDaoDBImpl implements MastermindRoundDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public Round createRound(Round round) {
        final String sql = "INSERT INTO round(gameId, guess, exactMatches, partialMatches) VALUES(?,?,?,?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update((Connection conn) -> {

            PreparedStatement statement = conn.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS);

            StringBuilder sb = new StringBuilder();
            round.getGuess().stream()
                    .forEachOrdered(c -> sb.append(c));

            statement.setInt(1, round.getGameId());
            statement.setString(2, sb.toString());
            statement.setInt(3, round.getExactMatches());
            statement.setInt(4, round.getPartialMatches());
            return statement;

        }, keyHolder);


        final String sql2
                = "SELECT id, exactMatches, partialMatches, timestamp, guess, gameId "
                + "FROM round "
                + "WHERE id = ?";

        Round newRound = jdbcTemplate.queryForObject(sql2, new RoundMapper(), keyHolder.getKey().intValue());

        return newRound;
    }

    @Override
    public List<Round> getRoundsByGameId(int id) throws GameEmptyException {
        final String sql
                = "SELECT id, exactMatches, partialMatches, timestamp, guess, gameId "
                + "FROM round "
                + "WHERE gameId = ?";

        List<Round> roundList = jdbcTemplate.query(sql, new RoundMapper(), id);

        if (roundList.isEmpty()) {
            throw new GameEmptyException("No rounds associated with this game.");
        }

        return roundList;
    }

    @Override
    @Transactional
    public void clearStorage() {
        final String sql = "DELETE FROM round";
        final String sql2 = "DELETE FROM game";

        jdbcTemplate.update("SET SQL_SAFE_UPDATES = 0");
        jdbcTemplate.update(sql);
        jdbcTemplate.update(sql2);
        jdbcTemplate.update("SET SQL_SAFE_UPDATES = 1");
    }

    private static final class RoundMapper implements RowMapper<Round> {

        @Override
        public Round mapRow(ResultSet rs, int index) throws SQLException {
            Round r = new Round();
            r.setId(rs.getInt("id"));
            r.setExactMatches(rs.getInt("exactMatches"));
            r.setPartialMatches(rs.getInt("partialMatches"));
            r.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
            r.setGameId(rs.getInt("gameId"));

            String s = rs.getString("guess");
            List<Character> a = s.chars()
                    .mapToObj(i -> (char) i)
                    .collect(Collectors.toList());
            r.setGuess(a);

            return r;
        }
    }
}
