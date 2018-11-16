package com.nokor.ersys.messaging.ws.resource.organization;

import javax.ws.rs.Path;

import com.nokor.ersys.core.hr.model.eref.ETypeOrganization;
import com.nokor.ersys.core.hr.model.organization.OrganizationTypes;

/**
 * 
 * @author prasnar
 *
 */
@Path("/configs/{orgPath:organizations}")
public class BranchSrvRsc extends BaseBranchSrvRsc {
	private static final ETypeOrganization TYPE_ORGANIZATION = OrganizationTypes.MAIN;
	
	@Override
	public ETypeOrganization getTypeOrganization() {
		return TYPE_ORGANIZATION;
	}

}
