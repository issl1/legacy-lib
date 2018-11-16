package com.nokor.efinance.ws.resource.app.applicant;

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

import com.nokor.efinance.share.applicant.CompanyCriteriaDTO;
import com.nokor.efinance.share.applicant.CompanyDTO;
import com.nokor.efinance.share.applicant.CompanyEmployeeDTO;
import com.nokor.ersys.core.hr.model.eref.ETypeOrganization;
import com.nokor.ersys.core.hr.model.organization.OrganizationTypes;
import com.nokor.ersys.messaging.share.address.AddressDTO;

/**
 * 
 * @author buntha.chea
 *
 */
@Path("/applicants/companies")
public class CompanySrvRsc extends BaseCompanySrvRsc {
	
	   private static final ETypeOrganization TYPE_ORGANIZATION = OrganizationTypes.MAIN;
	
	/**
	 * get List company
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response listCompanies() {		
		return super.listCompanies(TYPE_ORGANIZATION);
	}
	
	/**
	 * get campaign by id
	 * @param id
	 * @return
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response getCompany(@PathParam("id") Long id) {
		return super.getCompany(TYPE_ORGANIZATION, id);
	}
	/**
	 * Create Company
	 * @param companyDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response create(CompanyDTO companyDTO) {
		return super.createCompnay(TYPE_ORGANIZATION, companyDTO);
	}
	/**
	 * UPDATE company
	 * @param id
	 * @param companyDTO
	 * @return
	 */
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response update(@PathParam("id") Long id, CompanyDTO companyDTO) {
		return super.updateCompany(TYPE_ORGANIZATION, id, companyDTO);
	}	
	
	/**
	 * Delete Company
	 * @param id
	 * @return
	 */
	@DELETE
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response delete(@PathParam("id") Long id) {
		return super.deleteCompany(TYPE_ORGANIZATION, id);
	}
	/**
	 * Get list addresses in company
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/{comId}/addresses")
	public Response listAddresses(@PathParam("comId") Long comId) {		
		return super.listAddresses(TYPE_ORGANIZATION, comId);
	}
	/**
	 * Get Address by addid in company by comid
	 * @param id
	 * @return
	 */
	@GET
	@Path("/{comId}/addresses/{addId}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response getAddress(@PathParam("comId") Long comId, @PathParam("addId") Long addId) {
		return super.getAddress(TYPE_ORGANIZATION, comId, addId);
	}
	
	/**
	 * create address in company
	 * @param addressDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/{comId}/addresses")
	public Response createAddress(@PathParam("comId") Long comId, AddressDTO addressDTO) {
		return super.createAddress(TYPE_ORGANIZATION, addressDTO, comId);
	}
	/**
	 * UPDATE address in company
	 * @param id
	 * @param addressDTO
	 * @return
	 */
	@PUT
	@Path("/{comId}/addresses/{addId}")
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response updateAddress(@PathParam("comId") Long comId, @PathParam("addId") Long addId, AddressDTO addressDTO) {
		return super.updateAddress(TYPE_ORGANIZATION, comId, addressDTO, addId);
	}	
	
	/**
	 * DELETE address in company
	 * @param comId
	 * @param addId
	 * @return
	 */
	@DELETE
	@Path("/{comId}/addresses/{addId}")
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response deleteAddress(@PathParam("comId") Long comId, @PathParam("addId") Long addId) {
		return super.deleteAddress(comId, addId);
	}	
	
	/**
	 * Get list employees in company
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/{comId}/employees")
	public Response listEmployees(@PathParam("comId") Long comId) {		
		return super.listEmployees(TYPE_ORGANIZATION, comId);
	}
		
	/**
	 * Get Employee by empid in company by comid
	 * @param id
	 * @return
	 */
	@GET
	@Path("/{comId}/employees/{empId}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response getEmployee(@PathParam("comId") Long comId, @PathParam("empId") Long addId) {
		return super.getEmployee(TYPE_ORGANIZATION, comId, addId);
	}
	
	/**
	 * CREATE employee in company
	 * @param employeeDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/{comId}/employees")
	public Response createEmployee(@PathParam("comId") Long comId, CompanyEmployeeDTO employeeDTO) {
		return super.createEmployee(TYPE_ORGANIZATION, employeeDTO, comId);
	}
	
	/**
	 * UPDATE employee in company
	 * @param comId
	 * @param empId
	 * @param employeeDTO
	 * @return
	 */
	@PUT
	@Path("/{comId}/employees/{empId}")
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response updateEmployee(@PathParam("comId") Long comId, @PathParam("empId") Long empId, CompanyEmployeeDTO employeeDTO) {
		return super.updateEmployee(TYPE_ORGANIZATION, comId, employeeDTO, empId);
	}	
	
	/**
	 * DELETE employee in company
	 * @param comId
	 * @param empId
	 * @return
	 */
	@DELETE
	@Path("/{comId}/employees/{empId}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response deleteEmployee(@PathParam("comId") Long comId, @PathParam("empId") Long empId) {
		return super.deleteEmployee(comId, empId);
	}	
	
	/**
	 * Search company by code, name, licenseNo...
	 * @param companyCriteriaDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/search")
	public Response searchCompany(CompanyCriteriaDTO companyCriteriaDTO) {
		return super.searchCompany(companyCriteriaDTO);
	}
}
