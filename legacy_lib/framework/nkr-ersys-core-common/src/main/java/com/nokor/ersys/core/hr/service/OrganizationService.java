package com.nokor.ersys.core.hr.service;

import java.util.List;

import org.seuksa.frmk.service.MainEntityService;

import com.nokor.ersys.core.hr.model.eref.ESubTypeOrganization;
import com.nokor.ersys.core.hr.model.eref.ETypeOrganization;
import com.nokor.ersys.core.hr.model.organization.Organization;

/**
 * 
 * @author prasnar
 *
 */
public interface OrganizationService extends MainEntityService {

	List<Organization> listChildrenOrganizations(ETypeOrganization typeOrganization, Long parentOrgId);

	List<Organization> listRootOrganizations(ETypeOrganization typeOrganization);

	List<Organization> listOrganizations(ETypeOrganization typeOrganization, ESubTypeOrganization subTypeOrganization, Boolean rootOnly, Long parentOrgId);

	Organization getRootOrganization(ETypeOrganization typeOrganization, Long orgId);

	Organization getOrganization(ETypeOrganization typeOrganization, Boolean rootOnly, Long parentOrgId, Long orgId);

	Organization getOrganization(ETypeOrganization typeOrganization, ESubTypeOrganization subTypeOrganization, Boolean rootOnly, Long parentOrgId, Long orgId);

}
