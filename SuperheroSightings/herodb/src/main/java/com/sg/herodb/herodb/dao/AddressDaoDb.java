package com.sg.herodb.herodb.dao;
import com.sg.herodb.herodb.entity.Address;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AddressDaoDb implements AddressDao {
    @Autowired
    JdbcTemplate jdbc;
    
    @Override
    public Address getAddressById(int id) {
        final String sql = "SELECT * FROM address WHERE id = ?";
        
        Address address;
        
        try {
            address = jdbc.queryForObject(sql, new AddressMapper(), id);
            return address;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Address> getAllAddresses() {
        final String sql = "SELECT * from address";
        return jdbc.query(sql, new AddressMapper());
    }

    @Override
    @Transactional
    public Address createAddress(Address address) {
        final String sql = "INSERT INTO address(`name`, `description`, " + 
                "streetaddress, territory, country, postalcode, latitude, longitude, city) " +
                "VALUES(?,?,?,?,?,?,?,?,?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbc.update((Connection conn) -> {

            PreparedStatement statement = conn.prepareStatement(
                sql, 
                Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, address.getName());
            statement.setString(2, address.getDescription());
            statement.setString(3, address.getStreetAddress());
            statement.setString(4, address.getTerritory());
            statement.setString(5, address.getCountry());
            statement.setString(6, address.getPostalCode());
            statement.setBigDecimal(7, address.getLatitude());
            statement.setBigDecimal(8, address.getLongitude());
            statement.setString(9, address.getCity());
            
            return statement;

        }, keyHolder);
        
        address.setId(keyHolder.getKey().intValue());
        
        return address;
    }

    @Override
    public void updateAddress(Address address) {
        final String sql = "UPDATE address SET `name` = ?, `description` = ?, " + 
                "streetaddress = ?, territory = ?, country = ?, postalcode = ?, latitude = ?, longitude = ?, city = ? " +
                "WHERE id = ?";
        
        jdbc.update(sql,
                address.getName(),
                address.getDescription(),
                address.getStreetAddress(),
                address.getTerritory(),
                address.getCountry(),
                address.getPostalCode(),
                address.getLatitude(),
                address.getLongitude(),
                address.getCity(),
                address.getId());
    }

    @Override
    @Transactional
    public void deleteAddress(int id) {
        if(id == 1) return;
        
        final String UPDATE_ORGANIZATION = "UPDATE organization SET addressid = ? WHERE addressid = ?";
        final String UPDATE_SIGHTING = "UPDATE sighting SET addressid = ? WHERE addressid = ?";
        final String DELETE_ADDRESS = "DELETE FROM address WHERE id = ?";
        

        jdbc.update(UPDATE_ORGANIZATION, 1, id);
        
        jdbc.update(UPDATE_SIGHTING, 1, id);
        
        jdbc.update(DELETE_ADDRESS, id);
    }

    @Override
    public List<Address> getAddressByHeroId(int id) {
        final String sql = 
                "SELECT a.* FROM address a " +
                "JOIN sighting s ON a.id = s.addressid " +
                "JOIN super_sighting ss ON s.id = ss.sightingid " +
                "JOIN `super` su ON ss.superid = su.id " +
                "WHERE su.id = ?";
        
        return jdbc.query(sql, new AddressMapper(), id);
    }
    
    
    protected static final class AddressMapper implements RowMapper<Address> {

        @Override
        public Address mapRow(ResultSet rs, int index) throws SQLException {
            Address a = new Address();
            a.setName(rs.getString("name"));
            a.setCountry(rs.getString("country"));
            a.setDescription(rs.getString("description"));
            a.setId(rs.getInt("id"));
            a.setLatitude(rs.getBigDecimal("latitude"));
            a.setLongitude(rs.getBigDecimal("longitude"));
            a.setPostalCode(rs.getString("postalcode"));
            a.setStreetAddress(rs.getString("streetaddress"));
            a.setTerritory(rs.getString("territory"));
            a.setCity(rs.getString("city"));
            
            return a;
        }
    }
}
