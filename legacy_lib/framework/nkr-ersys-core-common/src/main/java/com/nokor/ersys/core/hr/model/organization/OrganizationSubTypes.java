package com.nokor.ersys.core.hr.model.organization;

import com.nokor.ersys.core.hr.model.eref.ESubTypeOrganization;

/**
 * 
 * @author prasnar
 *
 */
public class OrganizationSubTypes {
	public final static ESubTypeOrganization REGISTRATION_AGENT = new ESubTypeOrganization("REGI_AGENT", 1, OrganizationTypes.AGENT);
	public final static ESubTypeOrganization COLLECTION_AGENT= new ESubTypeOrganization("COLL_AGENT", 2, OrganizationTypes.AGENT);
	public final static ESubTypeOrganization PRINTING_AGENT = new ESubTypeOrganization("PRIN_AGENT", 3, OrganizationTypes.AGENT);
	
	public final static ESubTypeOrganization COLLECTION_LOCATION = new ESubTypeOrganization("COLL_LOCATION", 4, OrganizationTypes.LOCATION);
	public final static ESubTypeOrganization AUCTION_LOCATION = new ESubTypeOrganization("AUCT_LOCATION", 5, OrganizationTypes.LOCATION);
	public final static ESubTypeOrganization DEALER_AGENT = new ESubTypeOrganization("DEALER_AGENT", 6, OrganizationTypes.AGENT);
	public final static ESubTypeOrganization OUTSOURCE_AGENT = new ESubTypeOrganization("OUTSOURCE_AGENT", 7, OrganizationTypes.AGENT);
	
}

