package com.sg.mastermind.dao;

import com.sg.mastermind.entity.Game;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.EmptyResultDataAccessException;
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
public class MastermindGameDaoDBImpl implements MastermindGameDao {
    
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Override
    public Game createGame(Game game) {
        final String sql = "INSERT INTO game(solution) VALUES(?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update((Connection conn) -> {

            PreparedStatement statement = conn.prepareStatement(
                sql, 
                Statement.RETURN_GENERATED_KEYS);

            StringBuilder sb = new StringBuilder();
            game.getSolution().stream()
                    .forEachOrdered(c -> sb.append(c));
            
            statement.setString(1, sb.toString());
            return statement;

        }, keyHolder);
        
        game.setId(keyHolder.getKey().intValue());
        
        return game;
    }

    @Override
    public Game getGameById(int id) throws GameNotFoundException {
        final String sql = "SELECT id, complete, solution FROM game "
                + "WHERE id = ?";
        Game game;
        
        try {
            game =  jdbcTemplate.queryForObject(sql, new GameMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new GameNotFoundException("Game not found!");
        }
        
        return game;
    }

    @Override
    @Transactional
    public List<Game> getAllGames() {
        final String sql = "SELECT id, complete, solution FROM game ";
        return jdbcTemplate.query(sql, new GameMapper());
    }

    @Override
    @Transactional
    public boolean endGame(Game game) throws GameNotFoundException {
        final String sql = "UPDATE game SET complete = TRUE WHERE id = ? ";
        boolean success;
        
        jdbcTemplate.update("SET SQL_SAFE_UPDATES = 0");
        success = jdbcTemplate.update(sql, game.getId()) > 0;
        jdbcTemplate.update("SET SQL_SAFE_UPDATES = 1");
        
        return success;
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

    private static final class GameMapper implements RowMapper<Game> {

        @Override
        public Game mapRow(ResultSet rs, int index) throws SQLException {
            Game g = new Game();
            g.setId(rs.getInt("id"));
            g.setComplete(rs.getBoolean("complete"));
            String s = rs.getString("solution");
            List<Character> a = s.chars()
                    .mapToObj(i -> (char) i)
                    .collect(Collectors.toList());
            g.setSolution(a);
            return g;
        }
    }
}
