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

import com.nokor.ersys.core.hr.model.eref.ESubTypeOrganization;
import com.nokor.ersys.messaging.share.organization.OrganizationDTO;

/**
 * 
 * @author prasnar
 *
 */
public abstract class BaseLocationSrvRsc extends BaseOrganizationSrvRsc {

	protected abstract ESubTypeOrganization getSubTypeOrganization();
	
	/**
	 * List all companies
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list() {
		return super.listOrganizations(getSubTypeOrganization().getTypeOrganization(), getSubTypeOrganization()); 
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
		return super.getOrganization(getSubTypeOrganization().getTypeOrganization(), getSubTypeOrganization(), id, null, null);
	}
	
	/**
	 * Create Company
	 * @param companyDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("")
	public Response create(OrganizationDTO companyDTO) {
		return super.createOrganization(getSubTypeOrganization().getTypeOrganization(), companyDTO, getSubTypeOrganization());
	}
	
	/**
	 * Update company
	 * @param id
	 * @param companyDTO
	 * @return
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/{id}")
	public Response update(@PathParam("id") Long id, OrganizationDTO companyDTO) {
		return super.updateOrganization(getSubTypeOrganization().getTypeOrganization(), id, companyDTO, getSubTypeOrganization());
	}
	
	/**
	 * Delete company
	 * @param id
	 * @return
	 */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/{id}")
	public Response delete(@PathParam("id") Long id) {
		return super.deleteOrganization(getSubTypeOrganization().getTypeOrganization(), getSubTypeOrganization(), id, null, null);
	}

}
