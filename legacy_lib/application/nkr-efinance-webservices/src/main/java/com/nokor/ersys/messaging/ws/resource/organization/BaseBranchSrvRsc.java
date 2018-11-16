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
import com.nokor.ersys.messaging.share.organization.BranchDTO;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * 
 * @author prasnar
 *
 */
public abstract class BaseBranchSrvRsc extends BaseOrganizationSrvRsc {

	public abstract ETypeOrganization getTypeOrganization();

	private static final String ORG_PATH_ORGANIZATIONS = "organizations";
	private static final String ORG_PATH_WAREHOUSES = "warehouses";
	private static final String SUB_ORG_PATH_BRANCHES = "branches";
	private static final String SUB_ORG_PATH_AREAS = "areas";

	/**
	 * 
	 * @param orgPath
	 * @param subOrgPath
	 * @return
	 */
	protected void checkValidPath(String orgPath, String subOrgPath) {
		boolean valid = (ORG_PATH_ORGANIZATIONS.equals(orgPath) && SUB_ORG_PATH_BRANCHES.equals(subOrgPath)) 
					|| (ORG_PATH_WAREHOUSES.equals(orgPath) && SUB_ORG_PATH_AREAS.equals(subOrgPath));
		if (!valid) {
			String errMsg = "Path not valid [../" + orgPath + "/" + subOrgPath + "]";
			LOG.error(errMsg);
			throw new WsReponseException(EResponseStatus.PARAMETER_NOT_VALID, errMsg);
		}
	}
	
	/**
	 * List all companies
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/{orgId}/{subOrgPath:branches|areas}")
	public Response list(@PathParam("orgPath") String orgPath,
						@PathParam("subOrgPath") String subOrgPath,
						@PathParam("orgId") Long orgId) {
		checkValidPath(orgPath, subOrgPath);
		return super.listBranches(getTypeOrganization(), orgId);
	}
	
	/**
	 * List company by id
	 * @param id
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/{orgId}/{subOrgPath:branches|areas}/{id}")
	public Response get(@PathParam("orgPath") String orgPath,
						@PathParam("subOrgPath") String subOrgPath,
						@PathParam("orgId") Long orgId, 
						@PathParam("id") Long id) {
		checkValidPath(orgPath, subOrgPath);
		return super.getBranch(getTypeOrganization(), orgId, id);
	}
	
	/**
	 * Create Company
	 * @param companyDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/{orgId}/{subOrgPath:branches|areas}")
	public Response create(@PathParam("orgPath") String orgPath,
						@PathParam("subOrgPath") String subOrgPath,
						@PathParam("orgId") Long orgId, 
						BranchDTO branchDTO) {
		checkValidPath(orgPath, subOrgPath);
		return super.createBranch(getTypeOrganization(), orgId, branchDTO);
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
	@Path("/{orgId}/{subOrgPath:branches|areas}/{id}")
	public Response update(@PathParam("orgPath") String orgPath,
						@PathParam("subOrgPath") String subOrgPath,
						@PathParam("orgId") Long orgId, 
						@PathParam("id") Long id, 
						BranchDTO branchDTO) {
		checkValidPath(orgPath, subOrgPath);
		return super.updateBranch(getTypeOrganization(), orgId, id, branchDTO);
	}
	
	/**
	 * Delete company
	 * @param id
	 * @return
	 */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/{orgId}/{subOrgPath:branches|areas}/{id}")
	public Response delete(@PathParam("orgPath") String orgPath,
						@PathParam("subOrgPath") String subOrgPath,
						@PathParam("orgId") Long orgId, 
						@PathParam("id") Long id) {
		checkValidPath(orgPath, subOrgPath);
		return super.deleteBranch(getTypeOrganization(), orgId, id);
	}

}
