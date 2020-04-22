package com.sg.herodb.herodb.dao;

import com.sg.herodb.herodb.dao.PowerDaoDb.PowerMapper;
import com.sg.herodb.herodb.dao.SuperheroDaoDb.SuperheroMapper;
import com.sg.herodb.herodb.entity.Address;
import com.sg.herodb.herodb.entity.Organization;
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
public class OrganizationDaoDb implements OrganizationDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    public Organization getOrganizationById(int id) {
        final String sql = "SELECT * FROM organization WHERE id = ?";

        Organization organization;
        try {
            organization = jdbc.queryForObject(sql, new OrganizationMapper(), id);
            organization.setAddress(getAddressForOrganization(id));
            organization.setHeroes(getHeroesForOrganization(id));
            return organization;
        } catch (DataAccessException e) {
            return null;
        }
    }

    private Address getAddressForOrganization(int id) {
        final String sql = 
                "SELECT a.* FROM address a " +
                "JOIN organization o ON a.id = o.addressid " +
                "WHERE o.id = ?";
        Address address;
        address = jdbc.queryForObject(sql, new AddressDaoDb.AddressMapper(), id);

        return address;
    }
    
    private List<Superhero> getHeroesForOrganization(int id) {
        final String sql = 
                "SELECT s.* FROM `super` s " +
                "JOIN super_organization so ON so.superid = s.id " +
                "WHERE so.organizationid = ?";
        
        List<Superhero> heroes = jdbc.query(sql, new SuperheroMapper(), id);
        associatePowers(heroes);
        return heroes;
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
    public List<Organization> getAllOrganizations() {
        final String sql = "SELECT * FROM organization";
        
        List<Organization> organizations = jdbc.query(sql, new OrganizationMapper());
        associateAddressesAndHeroes(organizations);
        return organizations;
    }
    
    private void associateAddressesAndHeroes(List<Organization> organizations) {
        organizations.forEach(o -> {
            o.setAddress(getAddressForOrganization(o.getId()));
            o.setHeroes(getHeroesForOrganization(o.getId()));
        }); 
    }

    @Override
    @Transactional
    public Organization createOrganization(Organization organization) {
        final String INSERT_ORG = 
                "INSERT INTO organization(`name`, `description`, addressid) VALUES (?,?,?)";
        final String INSERT_SUPER_ORG = 
                "INSERT INTO super_organization(superid, organizationid) VALUES (?,?)";
        
        jdbc.update(INSERT_ORG,
                organization.getName(),
                organization.getDescription(),
                organization.getAddress().getId());
        
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        organization.setId(newId);
        
        organization.getHeroes().forEach((hero) -> 
            jdbc.update(INSERT_SUPER_ORG,
                    hero.getId(),
                    organization.getId()));
        
        return organization;
    }

    private void insertOrganizationSuper(Organization o) {
        final String sql = "INSERT INTO super_organization(organizationid, superid) VALUES (?,?)";
        
        o.getHeroes().forEach(h -> jdbc.update(sql, o.getId(), h.getId()));
    }
    
    @Override
    @Transactional
    public void updateOrganization(Organization organization) {
        final String sql = "UPDATE organization SET `name` = ?, `description` = ?, addressid = ? WHERE id = ?";
        
        jdbc.update(sql,
                organization.getName(),
                organization.getDescription(),
                organization.getAddress().getId(),
                organization.getId());
        

        final String DELETE_SUPER_ORGANIZATION = "DELETE FROM super_organization WHERE organizationid = ?";
        jdbc.update(DELETE_SUPER_ORGANIZATION, organization.getId());
        insertOrganizationSuper(organization);
    }

    @Override
    @Transactional
    public void deleteOrganization(int id) {
        final String DELETE_SUPER_ORGANIZATION = "DELETE FROM super_organization WHERE organizationid = ?";
        final String DELETE_ORGANIZATION = "DELETE FROM organization WHERE id = ?";
        
        jdbc.update(DELETE_SUPER_ORGANIZATION, id);
        jdbc.update(DELETE_ORGANIZATION, id);
    }

    @Override
    public List<Organization> getOrganizationsByHeroId(int id) {
        final String sql = 
                "SELECT * FROM organization o " + 
                "JOIN super_organization so ON o.id = so.organizationid " +
                "WHERE so.superid = ?";
        
        List<Organization> organizations = jdbc.query(sql, new OrganizationMapper(), id);
        associateAddressesAndHeroes(organizations);
        return organizations;    
    }

    private static final class OrganizationMapper implements RowMapper<Organization> {

        @Override
        public Organization mapRow(ResultSet rs, int index) throws SQLException {
            Organization o = new Organization();
            o.setDescription(rs.getString("description"));
            o.setName(rs.getString("name"));
            o.setId(rs.getInt("id"));

            return o;
        }
    }
}
