package com.sg.herodb.herodb.dao;

import com.sg.herodb.herodb.dao.PowerDaoDb.PowerMapper;
import com.sg.herodb.herodb.entity.Power;
import com.sg.herodb.herodb.entity.Superhero;
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
public class SuperheroDaoDb implements SuperheroDao {
    @Autowired
    JdbcTemplate jdbc;

    @Override
    @Transactional
    public Superhero getSupeheroById(int id) {
        final String sql = "SELECT * from `super` WHERE id = ?";
        
        Superhero hero;
        try {
            hero = jdbc.queryForObject(sql, new SuperheroMapper(), id);
        } catch (DataAccessException e) {
            return null;
        }
        
        getPower(hero);
        return hero;
    }
    
    protected void getPower(Superhero hero) {
        final String sql = 
                "SELECT p.* FROM power p " +
                "JOIN `super` s ON s.powerid = p.id " +
                "WHERE s.id = ?";
        
        Power power = jdbc.queryForObject(sql, new PowerMapper(), hero.getId());
        hero.setPower(power);
    }
    
    protected void associatePowers(List<Superhero> heroes) {
        heroes.forEach(h -> getPower(h));
    }

    @Override
    @Transactional
    public List<Superhero> getAllSuperheroes() {
        final String sql = "SELECT * from `super`";
        
        List<Superhero> heroes = jdbc.query(sql, new SuperheroMapper());
        associatePowers(heroes);
        
        return heroes;
    }

    @Override
    @Transactional
    public Superhero createSuperhero(Superhero hero) {
        final String sql = "INSERT INTO `super`(`name`, `description`, powerid, isvillain) VALUES(?,?,?,?)";
        
        jdbc.update(sql,
                hero.getName(),
                hero.getDescription(),
                hero.getPower().getId(),
                hero.isVillain());
        
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        hero.setId(newId);
        
        return hero;
    }

    @Override
    public void updateSuperhero(Superhero hero) {
        final String sql = 
                "UPDATE `super` " +
                "SET `name` = ?, `description` = ?, isvillain = ?, powerid = ? " + 
                "WHERE id = ?";
        
        jdbc.update(sql,
                hero.getName(),
                hero.getDescription(),
                hero.isVillain(),
                hero.getPower().getId(),
                hero.getId());
    }

    @Override
    @Transactional
    public void deleteSuperhero(int id) {
        final String DELETE_SUPER_SIGHTING = "DELETE FROM super_sighting WHERE superid = ?";
        final String DELETE_SUPER_ORGANIZATION = "DELETE FROM super_organization WHERE superid = ?";
        final String DELETE_HERO = "DELETE FROM `super` WHERE id = ?";
        
        jdbc.update(DELETE_SUPER_SIGHTING,id);
        jdbc.update(DELETE_SUPER_ORGANIZATION, id);
        jdbc.update(DELETE_HERO, id);
    }

    @Override
    public List<Superhero> getSuperheroesByAddressId(int id) {
        final String sql = 
                "SELECT s.* FROM `super` s " +
                "JOIN super_sighting ss ON s.id = ss.superid " +
                "JOIN sighting si ON ss.sightingid = si.id " +
                "WHERE si.addressid = ?";
        
        List<Superhero> heroes = jdbc.query(sql, new SuperheroMapper(), id);
        associatePowers(heroes);
        
        return heroes;
    }
    
        
    @Override
    public List<Superhero> getHeroesNotInOrganization(int id) {
        
        // Find the heroes that don't have a bridge table entry for the org
        final String sql = 
                "SELECT * FROM `super` WHERE id NOT IN (" +
                "SELECT s.id FROM `super` s " +
                "JOIN super_organization so ON so.superid = s.id " + 
                "WHERE so.organizationid = ?)";
        
        List<Superhero> heroes = jdbc.query(sql, new SuperheroMapper(), id);
        associatePowers(heroes);
        return heroes;
    }

    @Override
    public List<Superhero> getHeroesNotInSighting(int id) {
        final String sql = 
                "SELECT * FROM `super` WHERE id NOT IN (" +
                "SELECT s.id FROM `super` s " +
                "JOIN super_sighting ss ON s.id = ss.superid " +
                "WHERE ss.sightingid = ?)";
        
        List<Superhero> heroes = jdbc.query(sql, new SuperheroMapper(), id);
        associatePowers(heroes);
        return heroes;
    }

    protected static final class SuperheroMapper implements RowMapper<Superhero> {

        @Override
        public Superhero mapRow(ResultSet rs, int index) throws SQLException {
            Superhero s = new Superhero();
            s.setDescription(rs.getString("description"));
            s.setId(rs.getInt("id"));
            s.setIsVillain(rs.getBoolean("isvillain"));
            s.setName(rs.getString("name"));

            return s;
        }
    }
}
