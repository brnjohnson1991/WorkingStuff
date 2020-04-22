package com.sg.herodb.herodb.dao;

import com.sg.herodb.herodb.entity.Address;
import com.sg.herodb.herodb.entity.Organization;
import com.sg.herodb.herodb.entity.Power;
import com.sg.herodb.herodb.entity.Sighting;
import com.sg.herodb.herodb.entity.Superhero;
import java.math.BigDecimal;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author BRNJO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PowerDaoDbTest {
    @Autowired
    AddressDao addressDao;
    
    @Autowired
    OrganizationDao organizationDao;
    
    @Autowired
    PowerDao powerDao;
    
    @Autowired
    SightingDao sightingDao;
    
    @Autowired
    SuperheroDao superheroDao;
    
    public PowerDaoDbTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        List<Address> addresses = addressDao.getAllAddresses();
        addresses.forEach(a -> addressDao.deleteAddress(a.getId()));

        List<Organization> orgs = organizationDao.getAllOrganizations();
        orgs.forEach(o -> organizationDao.deleteOrganization(o.getId()));

        List<Power> powers = powerDao.getAllPowers();
        powers.forEach(p -> powerDao.deletePower(p.getId()));

        List<Sighting> sightings = sightingDao.getAllSightings();
        sightings.forEach(s -> sightingDao.deleteSighting(s.getId()));

        List<Superhero> heroes = superheroDao.getAllSuperheroes();
        heroes.forEach(h -> superheroDao.deleteSuperhero(h.getId()));

        if (addressDao.getAddressById(1) == null) {
            Address address = new Address();
            address.setCountry("Country Unknown");
            address.setDescription("No Description");
            address.setLatitude(new BigDecimal("1.000000"));
            address.setLongitude(new BigDecimal("1.000000"));
            address.setName("Name Unknown");
            address.setPostalCode("ZipUnknown");
            address.setStreetAddress("Street Address Unknown");
            address.setTerritory("Territory Unknown");
            address.setCity("City Unknown");
            addressDao.createAddress(address);
        }

        if (powerDao.getPowerById(1) == null) {
            Power power = new Power();
            power.setDescription("No description");
            power.setName("Power unknown");
            powerDao.createPower(power);
        }
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testAddAndGetPower() {
        Power power = new Power();
        power.setDescription("Test power description");
        power.setName("Test power");
        power = powerDao.createPower(power);
        
        Power fromDao = powerDao.getPowerById(power.getId());
        
        assertEquals(power, fromDao);
    }
    
    @Test
    public void testGetAllPowers() {
        Power power = new Power();
        power.setDescription("Test power description");
        power.setName("Test power");
        power = powerDao.createPower(power);

        Power power2 = new Power();
        power2.setDescription("Test power description 2");
        power2.setName("Test power 2");
        power2 = powerDao.createPower(power2);
        
        List<Power> powers = powerDao.getAllPowers();
        assertEquals(3, powers.size());
        assertTrue(powers.contains(power));
        assertTrue(powers.contains(power2));
    }
    
    @Test
    public void testUpdatePower() {
        Power power = new Power();
        power.setDescription("Test power description");
        power.setName("Test power");
        power = powerDao.createPower(power);
        
        Power fromDao = powerDao.getPowerById(power.getId());
        
        assertEquals(power, fromDao);
        
        power.setName("Test updated name");
        powerDao.updatePower(power);
        
        assertNotEquals(power, fromDao);
        
        fromDao = powerDao.getPowerById(power.getId());
        
        assertEquals(power, fromDao);
    }
    
    @Test
    public void testDeletePower() {
        Power power = new Power();
        power.setDescription("Test power description");
        power.setName("Test power");
        power = powerDao.createPower(power);

        Superhero hero = new Superhero();
        hero.setDescription("Test hero description 1");
        hero.setIsVillain(true);
        hero.setName("Test hero 1");
        hero.setPower(power);
        hero = superheroDao.createSuperhero(hero);
        
        Power fromDao = powerDao.getPowerById(power.getId());
        assertEquals(power, fromDao);
        
        powerDao.deletePower(power.getId());
        fromDao = powerDao.getPowerById(power.getId());
        assertNull(fromDao);
        
        hero = superheroDao.getSupeheroById(hero.getId());
        assertEquals("Power unknown", hero.getPower().getName());
    }
}
