package com.nokor.efinance.ws.resource.config.partner;

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
import com.nokor.ersys.messaging.ws.resource.organization.BaseOrganizationSrvRsc;

/**
 * 
 * @author prasnar
 *
 */
@Path("/configs/agents/companies")
public class AgentCompanySrvRsc extends BaseOrganizationSrvRsc {
	private static final ETypeOrganization TYPE_ORGANIZATION = OrganizationTypes.AGENT;

	/**
	 * List all companies
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list() {
		return super.listOrganizations(TYPE_ORGANIZATION); 
	}
	
	/**
	 * List all companies
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/{subTypeOrganizationId}")
	public Response list(@PathParam("subTypeOrganizationId") Long subTypeOrganizationId) {
		return super.listOrganizations(TYPE_ORGANIZATION, checkSubTypeOrganization(subTypeOrganizationId)); 
	}
	
	/**
	 * List company by id
	 * @param id
	 * @return
	 */
	@GET
	@Path("/{subTypeOrganizationId}/{id}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(@PathParam("subTypeOrganizationId") Long subTypeOrganizationId, @PathParam("id") Long id) {
		return super.getOrganization(TYPE_ORGANIZATION, checkSubTypeOrganization(subTypeOrganizationId), id, null, null);
	}
	
	/**
	 * Create Company
	 * @param companyDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/{subTypeOrganizationId}")
	public Response create(@PathParam("subTypeOrganizationId") Long subTypeOrganizationId, OrganizationDTO companyDTO) {
		return super.createOrganization(TYPE_ORGANIZATION, companyDTO, checkSubTypeOrganization(subTypeOrganizationId));
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
	@Path("/{subTypeOrganizationId}/{id}")
	public Response update(@PathParam("subTypeOrganizationId") Long subTypeOrganizationId, @PathParam("id") Long id, OrganizationDTO companyDTO) {
		return super.updateOrganization(TYPE_ORGANIZATION, id, companyDTO, checkSubTypeOrganization(subTypeOrganizationId));
	}
	
	/**
	 * Delete company
	 * @param id
	 * @return
	 */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/{subTypeOrganizationId}/{id}")
	public Response delete(@PathParam("subTypeOrganizationId") Long subTypeOrganizationId, @PathParam("id") Long id) {
		return super.deleteOrganization(TYPE_ORGANIZATION, checkSubTypeOrganization(subTypeOrganizationId), id, null, null);
	}

}
