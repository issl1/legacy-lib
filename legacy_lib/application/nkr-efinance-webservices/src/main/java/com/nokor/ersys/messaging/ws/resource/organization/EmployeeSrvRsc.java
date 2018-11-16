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
import com.nokor.ersys.messaging.share.organization.EmployeeDTO;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.WsReponseException;

/***
 * 
 * @author buntha.chea
 *
 */
@Path("/configs/{orgPath:organizations}")
public class EmployeeSrvRsc extends BaseOrganizationSrvRsc {
	
	private static final ETypeOrganization TYPE_ORGANIZATION = OrganizationTypes.MAIN;
	private static final String ORG_PATH_ORGANIZATIONS = "organizations";
	private static final String SUB_ORG_PATH_EMPLOYEE = "employees";
	
	/**
	 * 
	 * @param orgPath
	 * @param subOrgPath
	 * @return
	 */
	protected void checkValidPath(String orgPath, String subOrgPath) {
		boolean valid = (ORG_PATH_ORGANIZATIONS.equals(orgPath) && SUB_ORG_PATH_EMPLOYEE.equals(subOrgPath));
		if (!valid) {
			String errMsg = "Path not valid [../" + orgPath + "/" + subOrgPath + "]";
			LOG.error(errMsg);
			throw new WsReponseException(EResponseStatus.PARAMETER_NOT_VALID, errMsg);
		}
	}
	
	/**
	 * List all Employee in org
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/{orgId}/{subOrgPath:employees}")
	public Response list(@PathParam("orgPath") String orgPath,
						@PathParam("subOrgPath") String subOrgPath,
						@PathParam("orgId") Long orgId) {
		checkValidPath(orgPath, subOrgPath);
		return super.listEmployees(TYPE_ORGANIZATION, orgId);
	}
	
	/**
	 * get employee by id
	 * @param id
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/{orgId}/{subOrgPath:employees}/{id}")
	public Response get(@PathParam("orgPath") String orgPath,
						@PathParam("subOrgPath") String subOrgPath,
						@PathParam("orgId") Long orgId, 
						@PathParam("id") Long id) {
		checkValidPath(orgPath, subOrgPath);
		return super.getEmployee(TYPE_ORGANIZATION, orgId, id);
	}
	
	/**
	 * Create Employee
	 * @param employeeDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/{orgId}/{subOrgPath:employees}")
	public Response create(@PathParam("orgPath") String orgPath,
						@PathParam("subOrgPath") String subOrgPath,
						@PathParam("orgId") Long orgId, 
						EmployeeDTO employeeDTO) {
		checkValidPath(orgPath, subOrgPath);
		return super.createEmployee(TYPE_ORGANIZATION, orgId, employeeDTO);
	}
	
	
	/**
	 * Update Employee
	 * @param id
	 * @param employeeDTO
	 * @return
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/{orgId}/{subOrgPath:employees}/{id}")
	public Response update(@PathParam("orgPath") String orgPath,
						@PathParam("subOrgPath") String subOrgPath,
						@PathParam("orgId") Long orgId, 
						@PathParam("id") Long id, 
						EmployeeDTO employeeDTO) {
		checkValidPath(orgPath, subOrgPath);
		return super.updateEmployee(TYPE_ORGANIZATION, orgId, id, employeeDTO);
	}
	
	/**
	 * Delete Employee
	 * @param id
	 * @return
	 */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/{orgId}/{subOrgPath:employees}/{id}")
	public Response delete(@PathParam("orgPath") String orgPath,
						@PathParam("subOrgPath") String subOrgPath,
						@PathParam("orgId") Long orgId, 
						@PathParam("id") Long id) {
		checkValidPath(orgPath, subOrgPath);
		return super.deleteEmployee(TYPE_ORGANIZATION, orgId, id);
	}

	
	
}
