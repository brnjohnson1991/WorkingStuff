package com.sg.herodb.herodb.dao;

import com.sg.herodb.herodb.entity.Superhero;
import java.util.List;

/**
 *
 * @author BRNJO
 */
public interface SuperheroDao {
    Superhero getSupeheroById(int id);
    List<Superhero> getAllSuperheroes();
    Superhero createSuperhero(Superhero superhero);
    void updateSuperhero(Superhero superhero);
    void deleteSuperhero(int id);
    
    List<Superhero> getSuperheroesByAddressId(int addressId);
    List<Superhero> getHeroesNotInOrganization(int id);
    List<Superhero> getHeroesNotInSighting(int id);

}
