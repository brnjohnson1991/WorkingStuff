package com.sg.herodb.herodb.dao;

import com.sg.herodb.herodb.entity.Power;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author BRNJO
 */
@Repository
public class PowerDaoDb implements PowerDao {
    @Autowired
    JdbcTemplate jdbc;

    @Override
    @Transactional
    public Power getPowerById(int id) {
        final String sql = "SELECT * from `power` WHERE id = ?";

        Power power;
        try {
            power = jdbc.queryForObject(sql, new PowerMapper(), id);
        } catch (DataAccessException e) {
            return null;
        }

        return power;
    }

    @Override
    public List<Power> getAllPowers() {
        final String sql = "SELECT * from `power`";
        
        List<Power> powers = jdbc.query(sql, new PowerMapper());
        return powers;
    }

    @Override
    public Power createPower(Power power) {
        final String sql = "INSERT INTO `power`(`name`,`description`) VALUES (?,?)";
        
        jdbc.update(sql, power.getName(), power.getDescription());
        
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        power.setId(newId);
        
        return power;
    }

    @Override
    public void updatePower(Power power) {
        final String sql = "UPDATE `power` SET `name` = ?, `description` = ? WHERE id = ?";
        
        jdbc.update(sql, power.getName(), power.getDescription(), power.getId());
    }

    @Override
    @Transactional
    public void deletePower(int id) {
        if(id == 1) return;
        
        final String UPDATE_HEROES = "UPDATE `super` SET powerid = 1 WHERE powerid = ?";
        final String DELETE_POWER = "DELETE FROM `power` WHERE id = ?";
        

        jdbc.update(UPDATE_HEROES, id);
        jdbc.update(DELETE_POWER, id);
    }

    protected static final class PowerMapper implements RowMapper<Power> {

        @Override
        public Power mapRow(ResultSet rs, int index) throws SQLException {
            Power p = new Power();
            p.setDescription(rs.getString("description"));
            p.setId(rs.getInt("id"));
            p.setName(rs.getString("name"));

            return p;
        }
    }
}
