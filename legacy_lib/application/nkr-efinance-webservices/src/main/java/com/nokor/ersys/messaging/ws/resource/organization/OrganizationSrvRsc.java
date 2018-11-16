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
 * Company web service
 * @author uhout.cheng
 */
@Path("/configs/{orgPath:organizations}")
public class OrganizationSrvRsc extends BaseOrganizationSrvRsc {
	private static final ETypeOrganization TYPE_ORGANIZATION = OrganizationTypes.MAIN;
	
	/**
	 * List all companies
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response listAll() {		
		return super.listOrganizations(TYPE_ORGANIZATION); 
	}
	
	/**
	 * List root companies only
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/rootOnly")
	public Response listRootOnly() {		
		return super.listParentOrganizations(TYPE_ORGANIZATION); 
	}

	/**
	 * List company by id
	 * @param id
	 * @return
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(@PathParam("id") Long id) {
		return super.getRootOrganization(TYPE_ORGANIZATION, id);
	}
	
	/**
	 * Create Company
	 * @param companyDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response create(OrganizationDTO companyDTO) {
		return super.createRootOrganization(TYPE_ORGANIZATION, companyDTO);
	}
	
	/**
	 * Update company
	 * @param id
	 * @param companyDTO
	 * @return
	 */
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response update(@PathParam("id") Long id, OrganizationDTO companyDTO) {
		return super.updateRootOrganization(TYPE_ORGANIZATION, id, companyDTO);
	}
	
	/**
	 * Delete company
	 * @param id
	 * @return
	 */
	@DELETE
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response delete(@PathParam("id") Long id) {
		return super.deleteRootOrganization(TYPE_ORGANIZATION, id);
	}

}
