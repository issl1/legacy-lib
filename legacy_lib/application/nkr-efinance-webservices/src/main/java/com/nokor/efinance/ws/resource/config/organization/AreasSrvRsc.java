package com.nokor.efinance.ws.resource.config.organization;

import javax.ws.rs.Path;

import com.nokor.ersys.core.hr.model.eref.ETypeOrganization;
import com.nokor.ersys.core.hr.model.organization.OrganizationTypes;
import com.nokor.ersys.messaging.ws.resource.organization.BaseBranchSrvRsc;

/**
 * 
 * @author prasnar
 *
 */
@Path("/configs/collections/{orgPath:warehouses}")
public class AreasSrvRsc extends BaseBranchSrvRsc {
private static final ETypeOrganization TYPE_ORGANIZATION = OrganizationTypes.LOCATION;
	
	@Override
	public ETypeOrganization getTypeOrganization() {
		return TYPE_ORGANIZATION;
	}
}
