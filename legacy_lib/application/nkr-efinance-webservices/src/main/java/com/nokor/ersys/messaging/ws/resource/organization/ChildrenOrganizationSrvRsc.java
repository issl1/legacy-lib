package com.nokor.ersys.messaging.ws.resource.organization;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.nokor.ersys.core.hr.model.eref.ETypeOrganization;
import com.nokor.ersys.core.hr.model.organization.OrganizationTypes;
import com.nokor.ersys.messaging.share.organization.OrganizationDTO;

/**
 * 
 * @author prasnar
 *
 */
@Path("/configs/{orgPath:organizations}")
public class ChildrenOrganizationSrvRsc extends BaseOrganizationSrvRsc {
	private static final ETypeOrganization TYPE_ORGANIZATION = OrganizationTypes.MAIN;

	/**
	 * List all children companies
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/{orgId}/children")
	public Response list(@PathParam("orgId") Long parentOrgId) {		
		return super.listChildrenOrganizations(TYPE_ORGANIZATION, parentOrgId); 
	}


	/**
	 * List company by id
	 * @param orgId
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/{orgId}/children/{id}")
	public Response get(@PathParam("orgId") Long parentOrgId, @PathParam("id") Long orgId) {
		return super.getChildOrganization(TYPE_ORGANIZATION, null, parentOrgId, orgId);
	}
	
	/**
	 * Create Company
	 * @param companyDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/{orgId}/children")
	public Response create(@PathParam("orgId") Long parentOrgId, OrganizationDTO companyDTO) {
		return super.createChildOrganization(TYPE_ORGANIZATION, parentOrgId,companyDTO);
	}
	
	/**
	 * Update company
	 * @param orgId
	 * @param companyDTO
	 * @return
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/{orgId}/children/{id}")
	public Response update(@PathParam("orgId") Long parentOrgId, @PathParam("id") Long orgId, OrganizationDTO companyDTO) {
		return super.updateChildOrganization(TYPE_ORGANIZATION, orgId, parentOrgId, companyDTO);
	}
	
	/**
	 * Delete company
	 * @param orgId
	 * @return
	 */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/{orgId}/children/{id}")
	public Response delete(@PathParam("orgId") Long parentOrgId, @PathParam("id") Long orgId) {
		return super.deleteChildOrganization(TYPE_ORGANIZATION, orgId, parentOrgId);
	}

}
