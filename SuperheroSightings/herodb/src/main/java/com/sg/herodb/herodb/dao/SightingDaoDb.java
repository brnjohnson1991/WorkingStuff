package com.sg.herodb.herodb.dao;

import com.sg.herodb.herodb.entity.Address;
import com.sg.herodb.herodb.entity.Power;
import com.sg.herodb.herodb.entity.Sighting;
import com.sg.herodb.herodb.entity.Superhero;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author BRNJO
 */
@Repository
public class SightingDaoDb implements SightingDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    public Sighting getSightingById(int id) {
        final String sql = "SELECT * FROM sighting WHERE id = ?";

        Sighting sighting;
        try {
            sighting = jdbc.queryForObject(sql, new SightingMapper(), id);
            sighting.setHeroes(getHeroesForSighting(id));
            sighting.setAddress(getAddressForSighting(id));
            return sighting;
        } catch (DataAccessException e) {
            return null;
        }
    }

    private Address getAddressForSighting(int id) {
        final String sql
                = "SELECT a.* FROM address a "
                + "JOIN sighting s ON a.id = s.addressid "
                + "WHERE s.id = ?";

        Address address;
        try {
            address = jdbc.queryForObject(sql, new AddressDaoDb.AddressMapper(), id);
            return address;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private List<Superhero> getHeroesForSighting(int id) {
        final String sql
                = "SELECT s.* FROM `super` s "
                + "JOIN super_sighting ss ON ss.superid = s.id "
                + "JOIN sighting si ON ss.sightingid = si.id "
                + "WHERE si.id = ?";

        List<Superhero> heroes = jdbc.query(sql, new SuperheroDaoDb.SuperheroMapper(), id);
        associatePowers(heroes);
        return heroes;
    }

    protected void getPower(Superhero hero) {
        final String sql
                = "SELECT p.* FROM power p "
                + "JOIN `super` s ON s.powerid = p.id "
                + "WHERE s.id = ?";

        Power power = jdbc.queryForObject(sql, new PowerDaoDb.PowerMapper(), hero.getId());
        hero.setPower(power);
    }

    protected void associatePowers(List<Superhero> heroes) {
        heroes.forEach(h -> getPower(h));
    }

    @Override
    public List<Sighting> getAllSightings() {
        final String sql = "SELECT * FROM sighting";

        List<Sighting> sightings = jdbc.query(sql, new SightingMapper());
        associateHeroesAndAddresses(sightings);

        return sightings;
    }

    private void associateHeroesAndAddresses(List<Sighting> sightings) {
        sightings.forEach(s -> {
            s.setHeroes(getHeroesForSighting(s.getId()));
            s.setAddress(getAddressForSighting(s.getId()));
        });
    }

    @Override
    @Transactional
    public Sighting createSighting(Sighting sighting) {
        final String INSERT_SIGHTING = "INSERT INTO sighting(date, addressid, description) VALUES(?,?,?)";
        final String INSERT_SUPER_SIGHTING
                = "INSERT INTO super_sighting(superid, sightingid) VALUES(?,?)";

        jdbc.update(INSERT_SIGHTING,
                sighting.getDate(),
                sighting.getAddress().getId(),
                sighting.getDescription());

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        sighting.setId(newId);

        // Add heroes associated with sighting to bridge table
        sighting.getHeroes().forEach((hero)
                -> jdbc.update(INSERT_SUPER_SIGHTING,
                        hero.getId(),
                        sighting.getId()));

        return sighting;
    }

    @Override
    @Transactional
    public void updateSighting(Sighting sighting) {
        final String UPDATE_SIGHTING = "UPDATE sighting SET date = ?, addressid = ?, description = ? WHERE id = ?";
        final String CLEAR_SUPER_SIGHTING = "DELETE FROM super_sighting WHERE sightingid = ?";
        final String INSERT_SUPER_SIGHTING
                = "INSERT INTO super_sighting(superid, sightingid) VALUES(?,?)";

        jdbc.update(UPDATE_SIGHTING,
                Date.valueOf(sighting.getDate()),
                sighting.getAddress().getId(),
                sighting.getDescription(),
                sighting.getId());

        jdbc.update(CLEAR_SUPER_SIGHTING, sighting.getId());

        // Add heroes associated with sighting to bridge table
        sighting.getHeroes().forEach((hero)
                -> jdbc.update(INSERT_SUPER_SIGHTING,
                        hero.getId(),
                        sighting.getId()));
    }

    @Override
    @Transactional
    public void deleteSighting(int id) {
        final String CLEAR_SUPER_SIGHTING = "DELETE FROM super_sighting WHERE sightingid = ?";
        final String DELETE_SIGHTING = "DELETE FROM sighting WHERE id = ?";
        
        jdbc.update(CLEAR_SUPER_SIGHTING, id);
        jdbc.update(DELETE_SIGHTING, id);
    }

    @Override
    public List<Sighting> getSightingsByDate(LocalDate date) {
        final String sql = "SELECT * FROM sighting WHERE date = ?";

        List<Sighting> sightings = jdbc.query(sql, new SightingMapper(), date);
        associateHeroesAndAddresses(sightings);

        return sightings;
    }
    
    @Override
    public List<Sighting> getSightingsByHeroId(int id) {
        String sql = "SELECT s.* FROM sighting s " +
                "JOIN super_sighting ss ON s.id = ss.sightingid " +
                "WHERE ss.superid = ?";
        List<Sighting> sightings = jdbc.query(sql, new SightingMapper(), id);
        associateHeroesAndAddresses(sightings);

        return sightings;
     }

    private static final class SightingMapper implements RowMapper<Sighting> {

        @Override
        public Sighting mapRow(ResultSet rs, int index) throws SQLException {
            Sighting s = new Sighting();

            s.setId(rs.getInt("id"));
            s.setDate(rs.getDate("date").toLocalDate());
            s.setDescription(rs.getString("description"));
            return s;
        }
    }
}
