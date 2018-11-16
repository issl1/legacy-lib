package com.nokor.ersys.core.hr.model.organization;

import com.nokor.ersys.core.hr.model.eref.ETypeOrganization;

/**
 * 
 * @author prasnar
 *
 */
public class OrganizationTypes {
	public final static ETypeOrganization MAIN = new ETypeOrganization("MAIN", 1);
	public final static ETypeOrganization INSURANCE= new ETypeOrganization("INSURANCE", 2);
	public final static ETypeOrganization AGENT = new ETypeOrganization("AGENT", 3);
	public final static ETypeOrganization LOCATION = new ETypeOrganization("LOCATION", 4);
	
}

