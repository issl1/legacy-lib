package com.nokor.efinance.ws.resource.app.applicant;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.core.Response;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.CrudAction;
import org.seuksa.frmk.tools.exception.EntityAlreadyExistsException;
import org.seuksa.frmk.tools.exception.EntityCreationException;
import org.seuksa.frmk.tools.exception.EntityNotValidParameterException;
import org.springframework.dao.DataIntegrityViolationException;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.applicant.model.Company;
import com.nokor.efinance.core.applicant.model.CompanyAddress;
import com.nokor.efinance.core.applicant.model.CompanyEmployee;
import com.nokor.efinance.core.applicant.model.CompanyRestriction;
import com.nokor.efinance.share.applicant.CompanyCriteriaDTO;
import com.nokor.efinance.share.applicant.CompanyDTO;
import com.nokor.efinance.share.applicant.CompanyEmployeeDTO;
import com.nokor.efinance.ws.FinResourceSrvRsc;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.eref.ETypeOrganization;
import com.nokor.ersys.messaging.share.address.AddressDTO;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;
/**
 * 
 * @author buntha.chea
 *
 */
public class BaseCompanySrvRsc extends FinResourceSrvRsc{

	/**
	 * 
	 * @param typeOrganization
	 * @return
	 */
	protected Response listCompanies(ETypeOrganization typeOrganization) {		
		try {
			List<Company> companies = getListCompanies(typeOrganization);
			List<CompanyDTO> orgDTOs = toCompanyDTOs(companies, typeOrganization);
			
			return ResponseHelper.ok(orgDTOs);
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_LIST_KO, errMsg);
		} 
	}
	
	/**
	 * 
	 * @param typeOrganization
	 * @param id
	 * @return
	 */
	protected Response getCompany(ETypeOrganization typeOrganization, Long id) {
		try {
			Company company = checkCompany(typeOrganization, id);
			
			CompanyDTO companyDTO = toCompanyDTO(company);
			
			return ResponseHelper.ok(companyDTO);
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_UNIQUE_KO, errMsg);
		}
	}
	
	/**
	 * 
	 * @param typeOrganization
	 * @param companyDTO
	 * @param subTypeOrganization
	 * @return
	 */
	protected Response createCompnay(ETypeOrganization typeOrganization, CompanyDTO companyDTO) {
		try {
			
			Company company = toCompany(typeOrganization, companyDTO, null);
			company.setTypeOrganization(typeOrganization);
			
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
			
			ENTITY_SRV.create(company);
			if (company.getCompanyAddresses() != null && !company.getCompanyAddresses().isEmpty()) {
				this.saveOrUpdateAddresses(company);
			}
			
			companyDTO.setId(company.getId());
			
			return ResponseHelper.ok(companyDTO);
		} catch (IllegalStateException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.KO, errMsg);
		} catch (EntityCreationException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.CREATION_KO, errMsg);
		} catch (EntityAlreadyExistsException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.ALREADY_EXISTS, errMsg);
		} catch (EntityNotValidParameterException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.PARAMETER_NOT_VALID, errMsg);
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.CREATION_KO, errMsg);
		}
	}
	
	/**
	 * 
	 * @param typeOrganization
	 * @param comId
	 * @param companyDTO
	 * @return
	 */
	protected Response updateCompany(ETypeOrganization typeOrganization, Long comId, CompanyDTO companyDTO) {
		try {
			LOG.debug("Company [" + (comId != null ? comId : NULL) + "]");
			
			companyDTO.setId(comId);
			Company company = toCompany(typeOrganization, companyDTO, comId);
			
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.message(errMsg));
			} 
			
			if (company.getWkfStatus() == null) {
				company.setWkfStatus(EWkfStatus.NEW);
			}
			
			ENTITY_SRV.update(company);
			
			return ResponseHelper.updateSucess();
		} catch (IllegalStateException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.KO, errMsg);
		} catch (EntityCreationException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.UPDATE_KO, errMsg);
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (EntityNotValidParameterException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.PARAMETER_NOT_VALID, errMsg);
		} catch (DataIntegrityViolationException e) {
			String errMsg = "Error data integrity violation";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.UPDATE_KO, errMsg);	
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.UPDATE_KO, errMsg);
		}
	}
	
	/**
	 * 
	 * @param typeOrganization
	 * @param comId
	 * @return
	 */
	protected Response deleteCompany(ETypeOrganization typeOrganization, Long comId) {
		try {
			LOG.debug("Company [" + (comId != null ? comId : NULL) + "]");
			
			Company company = checkCompany(typeOrganization, comId);
			
			this.deleteAddresses(company, null);
			
			ENTITY_SRV.delete(company);
			
			return ResponseHelper.deleteSucess();
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (EntityNotValidParameterException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.PARAMETER_NOT_VALID, errMsg);
		} catch (DataIntegrityViolationException e) {
			String errMsg = I18N.messageObjectCanNotDelete("" + comId);
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.DELETE_KO, errMsg);
		} catch(Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.DELETE_KO, errMsg);
		}
	}
	
	/**
	 * 
	 * @param comId
	 * @return
	 */
	protected Response listAddresses(ETypeOrganization typeOrganization, Long comId) {		
		try {
	
			Company company = checkCompany(typeOrganization, comId);
			List<AddressDTO> addressDTOs = toAddressDTOs(company.getCompanyAddresses());
			
			return ResponseHelper.ok(addressDTOs);
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_LIST_KO, errMsg);
		} 
	}
	/**
	 * Get Address by  addId in Company By comId
	 * @param typeOrganization
	 * @param comId
	 * @param addId
	 * @return
	 */
	protected Response getAddress(ETypeOrganization typeOrganization, Long comId, Long addId) {
		try {
			Company company = checkCompany(typeOrganization, comId);
			Address address = checkAddress(company, addId);
			AddressDTO addressDTO = toAddressDTO(address);
			
			return ResponseHelper.ok(addressDTO);
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_UNIQUE_KO, errMsg);
		}
	}
	
	/**
	 * create address in company
	 * @param typeOrganization
	 * @param addressDTO
	 * @param comId
	 * @return
	 */
	protected Response createAddress(ETypeOrganization typeOrganization, AddressDTO addressDTO, Long comId) {
		try {
			
			Company company = checkCompany(typeOrganization, comId);
			Address address = toAddress(false, addressDTO);
			
			CompanyAddress companyAddress = new CompanyAddress();
			companyAddress.setCompany(company);
			companyAddress.setAddress(address);
			
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
			
			ENTITY_SRV.create(address);
			ENTITY_SRV.create(companyAddress);
			
			addressDTO.setId(address.getId());
			
			return ResponseHelper.ok(addressDTO);
		} catch (IllegalStateException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.KO, errMsg);
		} catch (EntityCreationException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.CREATION_KO, errMsg);
		} catch (EntityAlreadyExistsException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.ALREADY_EXISTS, errMsg);
		} catch (EntityNotValidParameterException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.PARAMETER_NOT_VALID, errMsg);
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.CREATION_KO, errMsg);
		}
	}
	
	/**
	 * update address in company
	 * @param typeOrganization
	 * @param comId
	 * @param addressDTO
	 * @param addId
	 * @return
	 */
	protected Response updateAddress(ETypeOrganization typeOrganization, Long comId, AddressDTO addressDTO, Long addId) {
		try {
			LOG.debug("Company [" + (comId != null ? comId : NULL) + "]");
			
			addressDTO.setId(addId);
			Address address = toAddress(false, addressDTO);
			
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.message(errMsg));
			} 
			
			ENTITY_SRV.update(address);
			
			return ResponseHelper.updateSucess();
		} catch (IllegalStateException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.KO, errMsg);
		} catch (EntityCreationException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.UPDATE_KO, errMsg);
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (EntityNotValidParameterException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.PARAMETER_NOT_VALID, errMsg);
		} catch (DataIntegrityViolationException e) {
			String errMsg = "Error data integrity violation";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.UPDATE_KO, errMsg);	
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.UPDATE_KO, errMsg);
		}
	}
	
	/**
	 * delete address in company
	 * @param comId
	 * @param addId
	 * @return
	 */
	protected Response deleteAddress(Long comId, Long addId) {
		try {
			LOG.debug(" [" + (addId != null ? addId : NULL) + "]");
			Company company = ENTITY_SRV.getById(Company.class, comId); 			
			
			this.deleteAddresses(company, addId);
			
			return ResponseHelper.deleteSucess();
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (EntityNotValidParameterException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.PARAMETER_NOT_VALID, errMsg);
		} catch (DataIntegrityViolationException e) {
			String errMsg = I18N.messageObjectCanNotDelete("" + addId);
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.DELETE_KO, errMsg);
		} catch(Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.DELETE_KO, errMsg);
		}
	}
	
	/**
	 * List all employees in a company
	 * @param comId
	 * @return
	 */
	protected Response listEmployees(ETypeOrganization typeOrganization, Long comId) {		
		try {
	
			Company company = checkCompany(typeOrganization, comId);
			List<CompanyEmployeeDTO> companyEmployeeDTOs = toEmployeeDTOs(company.getCompanyEmployees());
			
			return ResponseHelper.ok(companyEmployeeDTOs);
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_LIST_KO, errMsg);
		} 
	}
	/**
	 * Get employee by id
	 * @param typeOrganization
	 * @param comId
	 * @param empId
	 * @return
	 */
	protected Response getEmployee(ETypeOrganization typeOrganization, Long comId, Long empId) {
		try {
			Company company = checkCompany(typeOrganization, comId);
			CompanyEmployee companyEmployee = checkEmployee(company, empId);
			CompanyEmployeeDTO addressDTO = toCompanyEmployeeDTO(companyEmployee);
			
			return ResponseHelper.ok(addressDTO);
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_UNIQUE_KO, errMsg);
		}
	}
	
	/**
	 * Create an employee 
	 * @param typeOrganization
	 * @param employeeDTO
	 * @param comId
	 * @return
	 */
	protected Response createEmployee(ETypeOrganization typeOrganization, CompanyEmployeeDTO employeeDTO, Long comId) {
		try {
			
			Company company = checkCompany(typeOrganization, comId);
			CompanyEmployee companyEmployee = toCompanyEmployee(employeeDTO);
			companyEmployee.setCompany(company);
			
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
			
			ENTITY_SRV.create(companyEmployee);
			
			return ResponseHelper.ok(toCompanyEmployeeDTO(companyEmployee));
		} catch (IllegalStateException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.KO, errMsg);
		} catch (EntityCreationException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.CREATION_KO, errMsg);
		} catch (EntityAlreadyExistsException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.ALREADY_EXISTS, errMsg);
		} catch (EntityNotValidParameterException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.PARAMETER_NOT_VALID, errMsg);
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.CREATION_KO, errMsg);
		}
	}
	
	/**
	 * Update an employee
	 * @param typeOrganization
	 * @param comId
	 * @param employeeDTO
	 * @param empId
	 * @return
	 */
	protected Response updateEmployee(ETypeOrganization typeOrganization, Long comId, CompanyEmployeeDTO employeeDTO, Long empId) {
		try {
			LOG.debug("Company [" + (comId != null ? comId : NULL) + "]");
			
			employeeDTO.setId(empId);
			CompanyEmployee companyEmployee = toCompanyEmployee(employeeDTO);
			
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.message(errMsg));
			} 
			
			ENTITY_SRV.update(companyEmployee);
			
			return ResponseHelper.updateSucess();
		} catch (IllegalStateException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.KO, errMsg);
		} catch (EntityCreationException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.UPDATE_KO, errMsg);
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (EntityNotValidParameterException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.PARAMETER_NOT_VALID, errMsg);
		} catch (DataIntegrityViolationException e) {
			String errMsg = "Error data integrity violation";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.UPDATE_KO, errMsg);	
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.UPDATE_KO, errMsg);
		}
	}
	
	/**
	 * Delete an employee
	 * @param comId
	 * @param empId
	 * @return
	 */
	protected Response deleteEmployee(Long comId, Long empId) {
		try {
			LOG.debug(" [" + (empId != null ? empId : NULL) + "]");			
			
			ENTITY_SRV.delete(CompanyEmployee.class, empId);
			
			return ResponseHelper.deleteSucess();
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (EntityNotValidParameterException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.PARAMETER_NOT_VALID, errMsg);
		} catch (DataIntegrityViolationException e) {
			String errMsg = I18N.messageObjectCanNotDelete("" + empId);
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.DELETE_KO, errMsg);
		} catch(Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.DELETE_KO, errMsg);
		}
	}
	
	/**
	 * Search company by code, name, licenceNo...
	 * @param companyCriteriaDTO
	 * @return
	 */
	protected Response searchCompany(CompanyCriteriaDTO companyCriteriaDTO) {
		try {
			LOG.debug("Companies [" + companyCriteriaDTO + "]");
			
			CompanyRestriction restrictions = toCompanyRestriction(companyCriteriaDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
			List<Company> companies = ENTITY_SRV.list(restrictions);
			
			return ResponseHelper.ok(toCompaniesDTO(companies));
			
		} catch (IllegalStateException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.KO, errMsg);
		} catch (Exception e) {
			String errMsg = "Error while searching Individual[" + companyCriteriaDTO + "]";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.KO, errMsg);
		}
	}
	
	/**
	 * 
	 * @param companies
	 * @return
	 */
	protected List<CompanyDTO> toCompaniesDTO(List<Company> companies) {
		List<CompanyDTO> dtoLst = new ArrayList<>();
		for (Company company : companies) {
			dtoLst.add(toCompanyDTO(company));
		}
		return dtoLst;
	}
	
	/**
	 * 
	 * @param criteria
	 * @return
	 */
	private CompanyRestriction toCompanyRestriction(CompanyCriteriaDTO criteria) {
		CompanyRestriction restrictions = new CompanyRestriction();
		restrictions.setCode(criteria.getCode());
		restrictions.setName(criteria.getName());
		restrictions.setExternalCode(criteria.getExternalCode());
		restrictions.setLicenceNo(criteria.getLicenceNo());
		restrictions.setVatRegistrationNo(criteria.getVatRegistrationNo());
		return restrictions;
	}
	
	/**
	 * 
	 * @param companies
	 * @param typeOrganization
	 * @return
	 */
	protected List<AddressDTO> toAddressDTOs(List<CompanyAddress> companyAddresses) {
		List<AddressDTO> addressDTOs = new ArrayList<>();
		for (CompanyAddress companyAddress : companyAddresses) {
			addressDTOs.add(toAddressDTO(companyAddress.getAddress()));
		}
		return addressDTOs;
	}
	
	/**
	 * 
	 * @param companyEmployees
	 * @return
	 */
	protected List<CompanyEmployeeDTO> toEmployeeDTOs(List<CompanyEmployee> companyEmployees) {
		List<CompanyEmployeeDTO> companyEmployeeDTOs = new ArrayList<>();
		for (CompanyEmployee companyEmployee : companyEmployees) {
			companyEmployeeDTOs.add(toCompanyEmployeeDTO(companyEmployee));
		}
		return companyEmployeeDTOs;
	}
	
	/**
	 * 
	 * @param companies
	 * @return
	 */
	protected List<CompanyDTO> toCompanyDTOs(List<Company> companies, ETypeOrganization typeOrganization) {
		List<CompanyDTO> companyDTOs = new ArrayList<>();
		for (Company company : companies) {
			companyDTOs.add(toCompanyDTO(company));
		}
		return companyDTOs;
	}
	
	/**
	 * 
	 * @param typeOrganization
	 * @return
	 */
	private List<Company> getListCompanies(ETypeOrganization typeOrganization){
		BaseRestrictions<Company> restrictions = new BaseRestrictions<>(Company.class);
		restrictions.addCriterion(Restrictions.eq("typeOrganization", typeOrganization));
		restrictions.addOrder(Order.asc("id"));
		return ENTITY_SRV.list(restrictions);
	}
	
	/**
	 * 
	 * @param company
	 * @param empId
	 * @return
	 */
	private CompanyEmployee checkEmployee(Company company, Long empId) {
		CompanyEmployee employee = null;
		for (CompanyEmployee companyEmployee : company.getCompanyEmployees()) {
			if(companyEmployee.getId().equals(empId)) {
				employee = companyEmployee;
			}
		}
		
		if(employee == null) {
			String errMsg = "Employee [" + empId + "] In Company [" + company.getId() + "]" ;
			throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
		}
		return employee;
	}
	
	/**
	 * 
	 * @param company
	 * @param addId
	 * @return
	 */
	private Address checkAddress(Company company, Long addId) {
		Address address = null;
		for (CompanyAddress companyAddress : company.getCompanyAddresses()) {
			if(companyAddress.getAddress().getId().equals(addId)) {
				address = companyAddress.getAddress();
			}
		}
		
		if(address == null) {
			String errMsg = "Address [" + addId + "] In Company [" + company.getId() + "]" ;
			throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
		}
		return address;
	}
	
	/**
	 * 
	 * @param company
	 */
	private void saveOrUpdateAddresses(Company company) {
		LOG.debug("[>> saveOrUpdateAddresses]");
		List<CompanyAddress> companyAddresses = company.getCompanyAddresses();
		if (companyAddresses != null && !companyAddresses.isEmpty()) {
			for (CompanyAddress companyAddress : companyAddresses) {
				if (CrudAction.DELETE.equals(companyAddress.getCrudAction())) {
					ENTITY_SRV.delete(companyAddress);
					if (CrudAction.DELETE.equals(companyAddress.getAddress())) {
						ENTITY_SRV.delete(companyAddress.getAddress());
					}
				} else {
					ENTITY_SRV.saveOrUpdate(companyAddress.getAddress());
					companyAddress.setCompany(company);
					ENTITY_SRV.saveOrUpdate(companyAddress);
				}
			}
		}
		
		LOG.debug("[>> saveOrUpdateAddresses]");
	}
	
	/**
	 * 
	 * @param company
	 * @param adrId
	 */
	private void deleteAddresses(Company company, Long adrId) {
		LOG.debug("[>> DeleteAddresses]");
		List<CompanyAddress> companyAddresses = company.getCompanyAddresses();
		if (companyAddresses != null && !companyAddresses.isEmpty()) {
			for (CompanyAddress companyAddress : companyAddresses) {
				if (adrId != null) {
					if (adrId.equals(companyAddress.getAddress().getId())) {
						ENTITY_SRV.delete(companyAddress);
						ENTITY_SRV.delete(companyAddress.getAddress());
					}
				} else {
					ENTITY_SRV.delete(companyAddress);
					ENTITY_SRV.delete(companyAddress.getAddress());
				}
			}
		}
		
		LOG.debug("[>> DeletedAddresses]");
	}
}
