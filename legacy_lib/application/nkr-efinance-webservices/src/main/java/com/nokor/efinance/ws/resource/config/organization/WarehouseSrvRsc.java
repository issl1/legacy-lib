package com.nokor.efinance.ws.resource.config.organization;

import javax.ws.rs.Path;

import com.nokor.ersys.core.hr.model.eref.ESubTypeOrganization;
import com.nokor.ersys.core.hr.model.organization.OrganizationSubTypes;
import com.nokor.ersys.messaging.ws.resource.organization.BaseLocationSrvRsc;

/**
 * 
 * @author prasnar
 *
 */
@Path("/configs/collections/{orgPath:warehouses}")
public class WarehouseSrvRsc extends BaseLocationSrvRsc {
	private static final ESubTypeOrganization SUB_TYPE_ORGANIZATION = OrganizationSubTypes.COLLECTION_LOCATION;
	
	@Override
	public ESubTypeOrganization getSubTypeOrganization() {
		return SUB_TYPE_ORGANIZATION;
	}

}
