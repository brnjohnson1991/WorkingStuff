package com.sg.herodb.herodb.dao;

import com.sg.herodb.herodb.entity.Organization;
import java.util.List;

/**
 *
 * @author BRNJO
 */
public interface OrganizationDao {
    Organization getOrganizationById(int id);
    List<Organization> getAllOrganizations();
    Organization createOrganization(Organization organization);
    void updateOrganization(Organization organization);
    void deleteOrganization(int id);
    
    List<Organization> getOrganizationsByHeroId(int id);
}
