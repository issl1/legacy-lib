package com.nokor.ersys.messaging.ws.resource.organization;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.exception.EntityAlreadyExistsException;
import org.seuksa.frmk.tools.exception.EntityCreationException;
import org.seuksa.frmk.tools.exception.EntityNotValidParameterException;
import org.springframework.dao.DataIntegrityViolationException;

import com.nokor.common.app.eref.ECountry;
import com.nokor.common.messaging.share.UriDTO;
import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.efinance.core.financial.model.FinService;
import com.nokor.efinance.core.financial.model.InsuranceClaims;
import com.nokor.efinance.core.financial.model.InsuranceFinService;
import com.nokor.efinance.core.financial.service.InsuranceClaimsRestriction;
import com.nokor.efinance.core.organization.model.OrgPaymentMethod;
import com.nokor.efinance.core.payment.model.EPaymentFlowType;
import com.nokor.efinance.share.common.FinWsMessage;
import com.nokor.efinance.share.organization.InsuranceAOMDTO;
import com.nokor.efinance.share.organization.InsuranceCompanyDTO;
import com.nokor.efinance.share.organization.InsuranceLostClaimDTO;
import com.nokor.efinance.share.organization.InsuranceLostDTO;
import com.nokor.efinance.share.organization.InsuranceSerieDTO;
import com.nokor.efinance.share.payment.OrgDealerPaymentMethodDTO;
import com.nokor.efinance.share.payment.PaymentDetailDTO;
import com.nokor.efinance.ws.FinResourceSrvRsc;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.eref.ECivility;
import com.nokor.ersys.core.hr.model.eref.EGender;
import com.nokor.ersys.core.hr.model.eref.EJobPosition;
import com.nokor.ersys.core.hr.model.eref.ENationality;
import com.nokor.ersys.core.hr.model.eref.ESubTypeOrganization;
import com.nokor.ersys.core.hr.model.eref.ETypeOrganization;
import com.nokor.ersys.core.hr.model.organization.Employee;
import com.nokor.ersys.core.hr.model.organization.OrgAccountHolder;
import com.nokor.ersys.core.hr.model.organization.OrgAddress;
import com.nokor.ersys.core.hr.model.organization.OrgBankAccount;
import com.nokor.ersys.core.hr.model.organization.OrgStructure;
import com.nokor.ersys.core.hr.model.organization.Organization;
import com.nokor.ersys.messaging.share.address.AddressDTO;
import com.nokor.ersys.messaging.share.organization.BranchDTO;
import com.nokor.ersys.messaging.share.organization.EmployeeDTO;
import com.nokor.ersys.messaging.share.organization.OrganizationDTO;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * 
 * @author prasnar
 *
 */
public abstract class BaseOrganizationSrvRsc extends FinResourceSrvRsc {
	
	/**
	 * 
	 * @param typeOrganization
	 * @return
	 */
	protected Response listParentOrganizations(ETypeOrganization typeOrganization) {
		return listOrganizations(typeOrganization, null, true, null);
	}

	
	/**
	 * 
	 * @param typeOrganization
	 * @param parentOrgId
	 * @return
	 */
	protected Response listChildrenOrganizations(ETypeOrganization typeOrganization, Long parentOrgId) {
		return listOrganizations(typeOrganization, null, null, parentOrgId);
	}


	/**
	 * 
	 * @param typeOrganization
	 * @return
	 */
	protected Response listOrganizations(ETypeOrganization typeOrganization) {
		return listOrganizations(typeOrganization, null);
	}
	
	/**
	 * 
	 * @param typeOrganization
	 * @param subTypeOrganization
	 * @return
	 */
	protected Response listOrganizations(ETypeOrganization typeOrganization, ESubTypeOrganization subTypeOrganization) {
		return listOrganizations(typeOrganization, subTypeOrganization, null, null);
	}

	/**
	 * 
	 * @param typeOrganization
	 * @param subTypeOrganization
	 * @param rootOnly
	 * @param parentOrgId
	 * @return
	 */
	private Response listOrganizations(ETypeOrganization typeOrganization, ESubTypeOrganization subTypeOrganization, Boolean rootOnly, Long parentOrgId) {		
		try {
			List<Organization> orgs = ORG_SRV.listOrganizations(typeOrganization, subTypeOrganization, rootOnly, parentOrgId);
			List<OrganizationDTO> orgDTOs = toOrganizationDTOs(orgs);
			
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
	 * @param parentOrgId
	 * @param orgId
	 * @return
	 */
	protected Response getChildOrganization(ETypeOrganization typeOrganization, ESubTypeOrganization subTypeOrganization, Long parentOrgId, Long orgId) {		
		try {
			Organization org = ORG_SRV.getOrganization(typeOrganization, subTypeOrganization, null, parentOrgId, orgId);
			
			return ResponseHelper.ok(toOrganizationDTO(org));
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
	protected Response getRootOrganization(ETypeOrganization typeOrganization, Long id) {
		return getOrganization(typeOrganization, null, id, true, null);
	}
	
	/**
	 * 
	 * @param typeOrganization
	 * @param id
	 * @param rootOnly
	 * @param parentOrgId
	 * @param subTypeOrganization
	 * @return
	 */
	protected Response getOrganization(ETypeOrganization typeOrganization, ESubTypeOrganization subTypeOrganization, Long id, Boolean rootOnly, Long parentOrgId) {
		try {
			Organization org = checkOrganization(typeOrganization, subTypeOrganization, id, rootOnly, parentOrgId);
			OrganizationDTO orgDTO = toOrganizationDTO(org);
			
			return ResponseHelper.ok(orgDTO);
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
	 * @param parentOrgId
	 * @param orgDTO
	 * @return
	 */
	protected Response createChildOrganization(ETypeOrganization typeOrganization, Long parentOrgId, OrganizationDTO orgDTO) {
		orgDTO.setParentOrganizationId(parentOrgId);
		return createOrganization(typeOrganization, orgDTO);
	}
	
	/**
	 * 
	 * @param typeOrganization
	 * @param orgDTO
	 * @return
	 */
	protected Response createRootOrganization(ETypeOrganization typeOrganization, OrganizationDTO orgDTO) {
		orgDTO.setParentOrganizationId(null);
		return createOrganization(typeOrganization, orgDTO);
	}
	
	/**
	 * 
	 * @param typeOrganization
	 * @param orgDTO
	 * @return
	 */
	protected Response createOrganization(ETypeOrganization typeOrganization, OrganizationDTO orgDTO) {
		return createOrganization(typeOrganization, orgDTO, null);
	}
	
	/**
	 * 
	 * @param typeOrganization
	 * @param orgDTO
	 * @param subTypeOrganization
	 * @return
	 */
	protected Response createOrganization(ETypeOrganization typeOrganization, OrganizationDTO orgDTO, ESubTypeOrganization subTypeOrganization) {
		try {
			orgDTO.setSubTypeOrganizationId(subTypeOrganization == null ? null : subTypeOrganization.getId());
			Organization org = toOrganization(orgDTO);
			org.setTypeOrganization(typeOrganization);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
			
			ORG_SRV.createProcess(org);
			orgDTO.setId(org.getId());
			
			return ResponseHelper.ok(orgDTO);
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
	 * @param orgId
	 * @param orgDTO
	 * @return
	 */
	protected Response updateRootOrganization(ETypeOrganization typeOrganization, Long orgId, OrganizationDTO orgDTO) {
		orgDTO.setParentOrganizationId(null);
		return updateOrganization(typeOrganization, orgId, orgDTO);
	}

	/**
	 * 
	 * @param typeOrganization
	 * @param orgId
	 * @param parentOrgId
	 * @param orgDTO
	 * @return
	 */
	protected Response updateChildOrganization(ETypeOrganization typeOrganization, Long orgId, Long parentOrgId, OrganizationDTO orgDTO) {
		orgDTO.setParentOrganizationId(parentOrgId);
		return updateOrganization(typeOrganization, orgId, orgDTO);
	}
	
	/**
	 * 
	 * @param typeOrganization
	 * @param orgId
	 * @param orgDTO
	 * @return
	 */
	private Response updateOrganization(ETypeOrganization typeOrganization, Long orgId, OrganizationDTO orgDTO) {
		return updateOrganization(typeOrganization, orgId, orgDTO, null);
	}
	
	/**
	 * 
	 * @param typeOrganization
	 * @param orgId
	 * @param orgDTO
	 * @param subTypeOrganization
	 * @return
	 */
	protected Response updateOrganization(ETypeOrganization typeOrganization, Long orgId, OrganizationDTO orgDTO, ESubTypeOrganization subTypeOrganization) {
		try {
			LOG.debug("Organization [" + (orgId != null ? orgId : NULL) + "]");
			
			orgDTO.setId(orgId);
			orgDTO.setSubTypeOrganizationId(subTypeOrganization == null ? null : subTypeOrganization.getId());
			Organization org = toOrganization(orgDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.message(errMsg));
			} 
			
			ORG_SRV.updateProcess(org);
			
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
	 * @param orgId
	 * @return
	 */
	protected Response deleteRootOrganization(ETypeOrganization typeOrganization, Long orgId) {
		return deleteOrganization(typeOrganization, null, orgId, true, null);
	} 
	
	/**
	 * 
	 * @param typeOrganization
	 * @param orgId
	 * @param parentOrgId
	 * @return
	 */
	protected Response deleteChildOrganization(ETypeOrganization typeOrganization, Long orgId, Long parentOrgId) {
		return deleteOrganization(typeOrganization, null, orgId, null, parentOrgId);
	} 
	
	/**
	 * 
	 * @param typeOrganization
	 * @param orgId
	 * @param rootOnly
	 * @param parentOrgId
	 * @param subTypeOrganization
	 * @return
	 */
	protected Response deleteOrganization(ETypeOrganization typeOrganization, ESubTypeOrganization subTypeOrganization, Long orgId, Boolean rootOnly, Long parentOrgId) {
		try {
			LOG.debug("Organization [" + (orgId != null ? orgId : NULL) + "]");
			
			Organization org = checkOrganization(typeOrganization, subTypeOrganization, orgId, rootOnly, parentOrgId);
			
			ORG_SRV.deleteProcess(org);
			
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
			String errMsg = I18N.messageObjectCanNotDelete(""+orgId);
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
	 * @param typeOrganization
	 * @param orgId
	 * @return
	 */
	protected Organization checkRootOrganization(ETypeOrganization typeOrganization, Long orgId) {
		return checkOrganization(typeOrganization, null, orgId, true, null);
	}
	
	/**
	 * 
	 * @param typeOrganization
	 * @param orgId
	 * @param parentOrgId
	 * @return
	 */
	protected Organization checkChildOrganization(ETypeOrganization typeOrganization, Long orgId, Long parentOrgId) {
		return checkOrganization(typeOrganization, null, orgId, null, parentOrgId);
	}
	
	/**
	 * 
	 * @param typeOrganization
	 * @param orgId
	 * @return
	 */
	protected Organization checkOrganization(ETypeOrganization typeOrganization, Long orgId) {
		return checkOrganization(typeOrganization, null, orgId, null, null);
	}
	
	
	/**
	 * 
	 * @param typeOrganization
	 * @param orgId
	 * @param rootOnly
	 * @param parentOrgId
	 * @param subTypeOrganization
	 * @return
	 */
	private Organization checkOrganization(ETypeOrganization typeOrganization, ESubTypeOrganization subTypeOrganization, Long orgId, Boolean rootOnly, Long parentOrgId) {
		LOG.debug("Organization [" + (orgId != null ? orgId : NULL) + "]");
	
		Organization org = ORG_SRV.getOrganization(typeOrganization, subTypeOrganization, rootOnly, parentOrgId, orgId);
		
		if (org == null) {
			String errMsg = "Organization [" + orgId + "]";
			throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
		}
		
		return org;
	}
	
	/**
	 * 
	 * @param subTypeOrganizationId
	 */
	protected ESubTypeOrganization checkSubTypeOrganization(Long subTypeOrganizationId) {
		if (subTypeOrganizationId == null) {
			String errMsg = "subTypeOrganizationId";
			throw new EntityNotValidParameterException(I18N.messageFieldEmptyRequired(errMsg));
		}
		ESubTypeOrganization subTypeOrganization = ESubTypeOrganization.getById(subTypeOrganizationId);
		if (subTypeOrganization == null) {
			String errMsg = "SubTypeOrganization [" + subTypeOrganizationId + "]";
			throw new EntityNotValidParameterException(I18N.messageParameterNotValid(errMsg));
		}
		return subTypeOrganization;
	}
	
	/**
	 * 
	 * @param typeOrganization
	 * @param orgId
	 * @return
	 */
	protected Response listBranches(ETypeOrganization typeOrganization, Long orgId) {
		try {
			Organization org = checkOrganization(typeOrganization, orgId);
			
			List<BranchDTO> branchDTOs = toBranchDTOs(org.getOrgStructures());
			
			return ResponseHelper.ok(branchDTOs);
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
	 * @param orgId
	 * @param braId
	 * @return
	 */
	protected Response getBranch(ETypeOrganization typeOrganization, Long orgId, Long braId) {
		try {
			Organization org = checkOrganization(typeOrganization, orgId);
			
			BranchDTO branchDTO = findBranchDTO(org, braId);
			toBranchDTOs(org.getOrgStructures());
			
			return ResponseHelper.ok(branchDTO);
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
	 * @param orgId
	 * @param branchDTO
	 * @return
	 */
	public Response createBranch(ETypeOrganization typeOrganization, Long orgId, BranchDTO branchDTO) {
		try {
			Organization org = checkOrganization(typeOrganization, orgId);
			OrgStructure orgStr = toOrgStructure(org, branchDTO);

			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.message(errMsg));
			}
			
			ENTITY_SRV.create(orgStr);
			branchDTO.setId(orgStr.getId());
			
			return ResponseHelper.ok(branchDTO);
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
	 * @param orgId
	 * @param braId
	 * @param branchDTO
	 * @return
	 */
	public Response updateBranch(ETypeOrganization typeOrganization, Long orgId, Long braId, BranchDTO branchDTO) {
		try {
			Organization org = checkOrganization(typeOrganization, orgId);
			OrgStructure orgStr = toOrgStructure(org, branchDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.message(errMsg));
			} 
			
			ENTITY_SRV.update(orgStr);
			
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
	 * @param orgId
	 * @param braId
	 * @return
	 */
	public Response deleteBranch(ETypeOrganization typeOrganization, Long orgId, Long braId) {
		try {
			LOG.debug("Organization [" + (orgId != null ? orgId : NULL) + "] Branch [" + (braId != null ? braId : NULL) + "]");

			checkOrganization(typeOrganization, orgId);
			
			OrgStructure orgStr = ENTITY_SRV.getById(OrgStructure.class, braId);
			
			if (orgStr == null) {
				String errMsg = "Branch [" + braId + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			
			ENTITY_SRV.delete(orgStr);
			
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
			String errMsg = I18N.messageObjectCanNotDelete(""+braId);
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.DELETE_KO, errMsg);
		} catch(Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.DELETE_KO, errMsg);
		}
		
	}
	
	/**
	 * List Employees
	 * @param typeOrganization
	 * @param orgId
	 * @return
	 */
	protected Response listEmployees(ETypeOrganization typeOrganization, Long orgId) {
		try {
			Organization org = checkOrganization(typeOrganization, orgId);
			
			//List<BranchDTO> branchDTOs = toBranchDTOs(org.getOrgStructures());
			List<EmployeeDTO> employeeDTOs = toEmployeeDTOs(org.getEmployees());
			
			return ResponseHelper.ok(employeeDTOs);
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
	 * @param orgId
	 * @param braId
	 * @return
	 */
	protected Response getEmployee(ETypeOrganization typeOrganization, Long orgId, Long empId) {
		try {
			Organization org = checkOrganization(typeOrganization, orgId);
			
			EmployeeDTO employeeDTO = findEmployeeDTO(org, empId);
			
			return ResponseHelper.ok(employeeDTO);
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
	 * @param orgId
	 * @param branchDTO
	 * @return
	 */
	public Response createEmployee(ETypeOrganization typeOrganization, Long orgId, EmployeeDTO employeeDTO) {
		try {
			Organization org = checkOrganization(typeOrganization, orgId);
			Employee employee = toEmployee(org, employeeDTO);

			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.message(errMsg));
			}
			
			ENTITY_SRV.create(employee.getAddress());
			ENTITY_SRV.create(employee);
			employeeDTO.setId(employee.getId());
			
			return ResponseHelper.ok(employeeDTO);
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
	 * @param orgId
	 * @param braId
	 * @param employeeDTO
	 * @return
	 */
	public Response updateEmployee(ETypeOrganization typeOrganization, Long orgId, Long empId, EmployeeDTO employeeDTO) {
		try {
			Organization org = checkOrganization(typeOrganization, orgId);
			
			employeeDTO.setId(empId);
			Employee employee = toEmployee(org, employeeDTO);
			
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.message(errMsg));
			} 
			
			ENTITY_SRV.update(employee);
			
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
	 * @param orgId
	 * @param braId
	 * @return
	 */
	public Response deleteEmployee(ETypeOrganization typeOrganization, Long orgId, Long empId) {
		try {
			LOG.debug("Organization [" + (orgId != null ? orgId : NULL) + "] Employee [" + (empId != null ? empId : NULL) + "]");

			checkOrganization(typeOrganization, orgId);
			
			Employee employee = ENTITY_SRV.getById(Employee.class, empId);
			
			if (employee == null) {
				String errMsg = "Employee [" + empId + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			
			ENTITY_SRV.delete(employee);
			
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
	 * Convert from OrganizationDTO to Organization
	 * @param orgDTO
	 * @return
	 */
	protected Organization toOrganization(OrganizationDTO orgDTO) {
		Organization org = null;
		if (orgDTO.getId() != null) {
			org = ENTITY_SRV.getById(Organization.class, orgDTO.getId());
			if (org == null) {
				messages.add(FinWsMessage.ORGANIZATION_NOT_FOUND);
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
		} else {
			org = new Organization();
		}
		
		if (StringUtils.isNotEmpty(orgDTO.getNameEn())) {
			org.setNameEn(orgDTO.getNameEn());
		} else {
			messages.add(FinWsMessage.ORGANIZATION_NAME_EN_MANDATORY);
		}
		
		if (StringUtils.isNotEmpty(orgDTO.getName())) {
			org.setName(orgDTO.getName());
		} else {
			messages.add(FinWsMessage.ORGANIZATION_NAME_MANDATORY);
		}
		
		
		org.setCode(orgDTO.getCode());
		org.setDescEn(orgDTO.getDescEn());
		org.setDesc(orgDTO.getDesc());
		org.setTel(orgDTO.getTel());
		org.setMobile(orgDTO.getMobile());
		org.setEmail(orgDTO.getEmail());
		org.setSlogan(orgDTO.getSlogan());
		org.setWebsite(orgDTO.getWebsite());
		org.setLogoPath(orgDTO.getLogoPath());
		org.setExternalCode(orgDTO.getExternalCode());
		org.setVatRegistrationNo(orgDTO.getVatRegistrationNo());
		org.setLicenceNo(orgDTO.getLicenceNo());
		
		if (orgDTO.getSubTypeOrganizationId() != null) {
			org.setSubTypeOrganization(ESubTypeOrganization.getById(orgDTO.getSubTypeOrganizationId()));	
		}
		
		if (orgDTO.getCountry() != null) {
			org.setCountry(ECountry.getById(orgDTO.getCountry().getId()));
		}
		
		if (orgDTO.getBranches() != null) {
			for (UriDTO uriDTO : orgDTO.getBranches()) {
				OrgStructure branch = ENTITY_SRV.getById(OrgStructure.class, uriDTO.getId());
				if (branch == null) {
					messages.add(FinWsMessage.BRANCH_NOT_FOUND);
					String errMsg = messages.get(0).getDesc();
					throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
				}
				org.getBranches().add(branch);
			}
		}
		return org;
	}
		
	/**
	 * 
	 * @param organizations
	 * @return
	 */
	protected List<OrganizationDTO> toOrganizationDTOs(List<Organization> organizations) {
		List<OrganizationDTO> orgDTOs = new ArrayList<>();
		for (Organization org : organizations) {
			orgDTOs.add(toOrganizationDTO(org));
		}
		return orgDTOs;
	}
	
	/**
	 * Convert to OrganizationDTO
	 * @param org
	 * @return
	 */
	protected OrganizationDTO toOrganizationDTO(Organization org) {
		OrganizationDTO orgDTO = new OrganizationDTO();
		
		orgDTO.setId(org.getId());
		if (org.getParent() != null) {
			orgDTO.setParentOrganizationId(org.getParent().getId());
		}
		if (org.getSubTypeOrganization() != null) {
			orgDTO.setSubTypeOrganizationId(org.getSubTypeOrganization().getId());
		}
		
		orgDTO.setCode(org.getCode());
		orgDTO.setName(org.getName());
		orgDTO.setNameEn(org.getNameEn());
		orgDTO.setDescEn(org.getDescEn());
		orgDTO.setDesc(org.getDesc());
		orgDTO.setTel(org.getTel());
		orgDTO.setMobile(org.getMobile());
		orgDTO.setEmail(org.getEmail());
		orgDTO.setSlogan(org.getSlogan());
		orgDTO.setWebsite(org.getWebsite());
		orgDTO.setLogoPath(org.getLogoPath());
		orgDTO.setExternalCode(org.getExternalCode());
		orgDTO.setVatRegistrationNo(org.getVatRegistrationNo());
		orgDTO.setLicenceNo(org.getLicenceNo());
		orgDTO.setCountry(org.getCountry() != null ? toRefDataDTO(org.getCountry()) : null);
		
		
		orgDTO.setBranches(toOrgStructureUriDTOs(org.getOrgStructures()));
		orgDTO.setAddresses(toAddressesDTOs(org.getOrgAddresses()));
		
		return orgDTO;
	}
	
	/**
	 * 
	 * @param orgStructures
	 * @return
	 */
	protected List<BranchDTO> toBranchDTOs(List<OrgStructure> orgStructures) {
		List<BranchDTO> branchDTOs = new ArrayList<>();
		for (OrgStructure orgStr : orgStructures) {
			branchDTOs.add(toBranchDTO(orgStr));
		}
		
		return branchDTOs;
	}
	
	/**
	 * 
	 * @param orgStructures
	 * @return
	 */
	private List<EmployeeDTO> toEmployeeDTOs(List<Employee> employees) {
		List<EmployeeDTO> employeeDTOs = new ArrayList<>();
		for (Employee employee : employees) {
			employeeDTOs.add(toEmployeeDTO(employee));
		}
		
		return employeeDTOs;
	}
	
	
	/**
	 * 
	 * @param orgStructures
	 * @return
	 */
	private List<UriDTO> toOrgStructureUriDTOs(List<OrgStructure> orgStructures) {
		List<UriDTO> uriDTOs = new ArrayList<>();
		for (OrgStructure orgStr : orgStructures) {
			uriDTOs.add(new UriDTO(orgStr.getId(), null));
		}
		
		return uriDTOs;
	}
	
	/**
	 * 
	 * @param addresses
	 * @return
	 */
	private List<AddressDTO> toAddressesDTOs(List<OrgAddress> orgAddresses) {
		List<Address> addresses = new ArrayList<Address>();
		if (orgAddresses != null && !orgAddresses.isEmpty()) {
			for (OrgAddress orgAddr : orgAddresses) {
				addresses.add(orgAddr.getAddress());
			}
		}
		List<AddressDTO> addressDTOs = new ArrayList<>();
		if (addresses != null && !addresses.isEmpty()) {
			for (Address address : addresses) {
				addressDTOs.add(toAddressDTO(address));
			}
		}
		return addressDTOs;
	}
	
	/**
	 * 
	 * @param orgStructures
	 * @return
	 */
	private BranchDTO findBranchDTO(Organization org, Long braId) {
		for (OrgStructure orgStr : org.getOrgStructures()) {
			if (orgStr.getId().equals(braId)) {
				return toBranchDTO(orgStr);
			}
		}
		
		String errMsg = "Organization [" + org.getId() + "] Branch [" + braId + "]";
		throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
	}
	

	/**
	 * Find EmployeeDTO by ID
	 * @param orgStructures
	 * @return
	 */
	private EmployeeDTO findEmployeeDTO(Organization org, Long empId) {
		for (Employee employee : org.getEmployees()) {
			if (employee.getId().equals(empId)) {
				return toEmployeeDTO(employee);
			}
		}
		
		String errMsg = "Organization [" + org.getId() + "] Branch [" + empId + "]";
		throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
	}
	
	/**
	 * Convert to BranchDTO 
	 * @param orgStr
	 * @return
	 */
	protected BranchDTO toBranchDTO(OrgStructure orgStr) {
		BranchDTO branchDTO = new BranchDTO();
		branchDTO.setId(orgStr.getId());
		branchDTO.setOrgId(orgStr.getOrganization().getId());
		branchDTO.setCode(orgStr.getCode());
		branchDTO.setName(orgStr.getName());
		branchDTO.setNameEn(orgStr.getNameEn());
		branchDTO.setTel(orgStr.getTel());
		branchDTO.setMobile(orgStr.getMobile());
		branchDTO.setEmail(orgStr.getEmail());
		
		return branchDTO;
	}
	
	/**
	 * 
	 * @param orgId
	 * @param branchDTO
	 * @return
	 */
	private OrgStructure toOrgStructure(Organization org, BranchDTO branchDTO) {
		OrgStructure orgStr = OrgStructure.createInstance();
		
		if (orgStr == null) {
			messages.add(FinWsMessage.BRANCH_NOT_FOUND);
			String errMsg = messages.get(0).getDesc();
			throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
		}
		
		if (branchDTO.getId() != null) {
			orgStr = ENTITY_SRV.getById(OrgStructure.class, branchDTO.getId());
			if (orgStr == null) {
				messages.add(FinWsMessage.BRANCH_NOT_FOUND);
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
		} else {
			orgStr = OrgStructure.createInstance();
		}
		
		if (StringUtils.isNotEmpty(branchDTO.getNameEn())) {
			orgStr.setNameEn(branchDTO.getNameEn());
		} else {
			messages.add(FinWsMessage.BRANCH_NAME_EN_MANDATORY);
		}
		
		orgStr.setOrganization(org);
		orgStr.setCode(orgStr.getCode());
		orgStr.setName(orgStr.getName());
		orgStr.setTel(orgStr.getTel());
		orgStr.setMobile(orgStr.getMobile());
		orgStr.setEmail(orgStr.getEmail());
		
		return orgStr;
	}
	
	/**
	 * Convert to Employee data transfer
	 * @param employee
	 * @return
	 */
	protected EmployeeDTO toEmployeeDTO(Employee employee) {
		EmployeeDTO employeeDTO = new EmployeeDTO();
		// TODO BasePersonDTO
		employeeDTO.setId(employee.getId());
		employeeDTO.setReferance(employee.getReference());
		employeeDTO.setLastName(employee.getLastName());
		employeeDTO.setLastNameEn(employee.getLastNameEn());
		employeeDTO.setFirstName(employee.getFirstName());
		employeeDTO.setFirstNameEn(employee.getFirstNameEn());
		employeeDTO.setCivilityId(employee.getCivility() != null ? employee.getCivility().getId() : null);
		employeeDTO.setGenderId(employee.getGender() != null ? employee.getGender().getId() : null);
		employeeDTO.setNationalityId(employee.getNationality() != null ? employee.getNationality().getId() : null);
		employeeDTO.setBirthDate(employee.getBirthDate());
		employeeDTO.setEmail(employee.getEmailPerso());
		employeeDTO.setMobile(employee.getMobilePerso());
		employeeDTO.setJopPositionId(employee.getJobPosition() != null ? employee.getJobPosition().getId() : null);
		if (employee.getAddress() != null) {
			employeeDTO.setAddressDTO(toAddressDTO(employee.getAddress()));
		}
//		employeeDTO.setNickName(employee.getNickName());
//		employeeDTO.setTypeIdNumberId(employee.getTypeIdNumber() != null ? employee.getTypeIdNumber().getId() : null);
//		employeeDTO.setIdNumber(employee.getIdNumber());
//		employeeDTO.setIssuingIdNumberDate(employee.getIssuingIdNumberDate());
//		employeeDTO.setExpiringIdNumberDate(employee.getExpiringIdNumberDate());
//		employeeDTO.setCivilityId(employee.getCivility() != null ? employee.getCivility().getId() : null);
//		employeeDTO.setBirthDate(employee.getBirthDate());
//		employeeDTO.setGenderId(employee.getGender() != null ? employee.getGender().getId() : null);
//		employeeDTO.setMaritalStatusId(employee.getMaritalStatus() != null ? employee.getMaritalStatus().getId() : null);
//		employeeDTO.setNationalityId(employee.getNationality() != null ? employee.getNationality().getId() : null);
//		// TODO StaffDTO
//		employeeDTO.setReference(employee.getReference());
//		employeeDTO.setEnrollmentDate(employee.getEnrollmentDate());
//		employeeDTO.setQuitDate(employee.getQuitDate());
//		employeeDTO.setJobPositionId(employee.getJobPosition() != null ? employee.getJobPosition().getId() : null);
//		employeeDTO.setTypeContactId(employee.getTypeContact() != null ? employee.getTypeContact().getId() : null);
//		employeeDTO.setTel(employee.getTel());
		return employeeDTO;
	}
	
	/**
	 * 
	 * @param org
	 * @param employeeDTO
	 * @return
	 */
	protected Employee toEmployee(Organization org, EmployeeDTO employeeDTO) {
		Employee employee = Employee.createInstance();
		
		if (employee == null) {
			messages.add(FinWsMessage.EMPLOYEE_NOT_FOUND);
			String errMsg = messages.get(0).getDesc();
			throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
		}
		
		if (employeeDTO.getId() != null) {
			employee = ENTITY_SRV.getById(Employee.class, employeeDTO.getId());
			if (employee == null) {
				messages.add(FinWsMessage.EMPLOYEE_NOT_FOUND);
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
		} else {
			employee = Employee.createInstance();
		}
		
		employee.setOrganization(org);
		employee.setReference(employeeDTO.getReferance());
		employee.setCivility(employeeDTO.getCivilityId() != null ? ECivility.getById(employeeDTO.getCivilityId()) : null);
		employee.setLastName(employeeDTO.getLastName());
		employee.setLastNameEn(employeeDTO.getLastNameEn());
		employee.setFirstName(employeeDTO.getFirstName());
		employee.setFirstNameEn(employeeDTO.getFirstNameEn());
		employee.setGender(employeeDTO.getGenderId() != null ? EGender.getById(employeeDTO.getGenderId()) : null);
		employee.setNationality(employeeDTO.getNationalityId() != null ? ENationality.getById(employeeDTO.getNationalityId()) : null);
		employee.setBirthDate(employeeDTO.getBirthDate());
		employee.setEmailPerso(employeeDTO.getEmail());
		employee.setMobilePerso(employeeDTO.getMobile());
		employee.setJobPosition(employeeDTO.getJopPositionId() != null ? EJobPosition.getById(employeeDTO.getJopPositionId()) : null);
		if (employeeDTO.getAddressDTO() != null) {
			employee.setAddress(toAddress(false, employeeDTO.getAddressDTO()));
		}
		return employee;
	}
	
	/**
	 * 
	 * @return
	 */
	protected Response listInsuranceCompanies() {		
		try {
			List<Organization> orgs = ORG_SRV.listOrganizations(ETypeOrganization.INSURANCE, null, null, null);
			List<InsuranceCompanyDTO> insCompanyDTOs = toInsuranceCompanyDTOs(orgs);
			
			return ResponseHelper.ok(insCompanyDTOs);
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
	 * @return
	 */
	protected Response listRootInsuranceCompanies() {		
		try {
			List<Organization> orgs = ORG_SRV.listOrganizations(ETypeOrganization.INSURANCE, null, true, null);
			List<InsuranceCompanyDTO> insCompanyDTOs = toInsuranceCompanyDTOs(orgs);
			
			return ResponseHelper.ok(insCompanyDTOs);
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
	 * @param id
	 * @return
	 */
	protected Response getRootInsuranceCompany(Long id) {		
		try {
			Organization org = checkOrganization(ETypeOrganization.INSURANCE, null, id, true, null);
			InsuranceCompanyDTO insComDTO = toInsuranceCompanyDTO(org);
			
			return ResponseHelper.ok(insComDTO);
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
	 * @param organizations
	 * @return
	 */
	protected List<InsuranceCompanyDTO> toInsuranceCompanyDTOs(List<Organization> organizations) {
		List<InsuranceCompanyDTO> insComDTOs = new ArrayList<>();
		for (Organization org : organizations) {
			insComDTOs.add(toInsuranceCompanyDTO(org));
		}
		return insComDTOs;
	}
	
	/**
	 * 
	 * @param orgIns
	 * @return
	 */
	protected InsuranceCompanyDTO toInsuranceCompanyDTO(Organization orgIns) {
		InsuranceCompanyDTO insComapanyDTO = new InsuranceCompanyDTO();
		
		insComapanyDTO.setId(orgIns.getId());
		insComapanyDTO.setCode(orgIns.getCode());
		insComapanyDTO.setName(orgIns.getName());
		insComapanyDTO.setNameEn(orgIns.getNameEn());
		insComapanyDTO.setDescEn(orgIns.getDescEn());
		insComapanyDTO.setDesc(orgIns.getDesc());
		insComapanyDTO.setTel(orgIns.getTel());
		insComapanyDTO.setMobile(orgIns.getMobile());
		insComapanyDTO.setEmail(orgIns.getEmail());
		insComapanyDTO.setSlogan(orgIns.getSlogan());
		insComapanyDTO.setWebsite(orgIns.getWebsite());
		insComapanyDTO.setLogoPath(orgIns.getLogoPath());
		insComapanyDTO.setExternalCode(orgIns.getExternalCode());
		insComapanyDTO.setVatRegistrationNo(orgIns.getVatRegistrationNo());
		insComapanyDTO.setLicenceNo(orgIns.getLicenceNo());
		insComapanyDTO.setCountry(orgIns.getCountry() != null ? toRefDataDTO(orgIns.getCountry()) : null);
		insComapanyDTO.setBranches(toOrgStructureUriDTOs(orgIns.getOrgStructures()));
		insComapanyDTO.setAddresses(toAddressesDTOs(orgIns.getOrgAddresses()));
		
		List<InsuranceFinService> insFinLostServices = getInsuranceFinService(orgIns.getId(), EServiceType.INSLOS);
		InsuranceLostDTO insLostDTO = new InsuranceLostDTO();
		if (!insFinLostServices.isEmpty()) {
			insLostDTO.setInsuranceSeriesDTO(toInsuranceSerieDTOs(insFinLostServices));
		}
		
		List<InsuranceClaims> insClaims = getInsuranceClaims(orgIns.getId());
		if (!insClaims.isEmpty()) {
			insLostDTO.setInsuranceLostClaimsDTO(toInsuranceLostClaimDTOs(insClaims));
		}
		insComapanyDTO.setInsuranceLostDTO(insLostDTO);
		
		List<InsuranceFinService> insFinAOMServices = getInsuranceFinService(orgIns.getId(), EServiceType.INSAOM);
		InsuranceAOMDTO insAOMDTO = new InsuranceAOMDTO();
		if (!insFinAOMServices.isEmpty()) {
			insAOMDTO.setInsuranceSeriesDTO(toInsuranceSerieDTOs(insFinAOMServices));
			insComapanyDTO.setInsuranceAOMDTO(insAOMDTO);
		}
		
		insComapanyDTO.setPaymentDetail(toIsrPaymentDetailDTO(orgIns));
		
		return insComapanyDTO;
	}
	
	/**
	 * 
	 * @param insFinServices
	 * @return
	 */
	protected List<InsuranceSerieDTO> toInsuranceSerieDTOs(List<InsuranceFinService> insFinServices) {
		List<InsuranceSerieDTO> orgDTOs = new ArrayList<>();
		for (InsuranceFinService insFinSrv : insFinServices) {
			orgDTOs.add(toInsuranceSerieDTO(insFinSrv));
		}
		return orgDTOs;
	}
	
	/**
	 * 
	 * @param insFinService
	 * @return
	 */
	private InsuranceSerieDTO toInsuranceSerieDTO(InsuranceFinService insFinService) {
		InsuranceSerieDTO insSerieDTO = new InsuranceSerieDTO();
		insSerieDTO.setSerie(insFinService.getAssetModel() != null ? getAssetModelsDTO(insFinService.getAssetModel().getId()) : null);
		insSerieDTO.setPremium1Y(insFinService.getPremium1Y());
		insSerieDTO.setClaimAmount1Y(insFinService.getClaimAmount1Y());
		insSerieDTO.setPremium2Y(insFinService.getPremium2Y());
		insSerieDTO.setClaimAmount2YFirstYear(insFinService.getClaimAmount2YFirstYear());
		insSerieDTO.setClaimAmount2YSecondYear(insFinService.getClaimAmount2YSecondYear());
		return insSerieDTO;
	}
	
	/**
	 * 
	 * @param orgIns
	 * @return
	 */
	private PaymentDetailDTO toIsrPaymentDetailDTO(Organization orgIns) {
		PaymentDetailDTO paymentDetailDTO = new PaymentDetailDTO();
		
		List<OrgAccountHolder> orgAccountHolders = orgIns.getOrgAccountHolders();
		List<UriDTO> accHolderUriDTOs = new ArrayList<UriDTO>();
		if (orgAccountHolders != null && !orgAccountHolders.isEmpty()) {
			for (OrgAccountHolder orgAccountHolder : orgAccountHolders) {
				accHolderUriDTOs.add(orgAccountHolder.getAccountHolder() != null ? getAccountHoldersDTO(orgAccountHolder.getAccountHolder()) : null);
			}
		}
		paymentDetailDTO.setAccountHolders(accHolderUriDTOs);
		
		List<OrgBankAccount> orgBankAccounts = orgIns.getOrgBankAccounts();
		List<UriDTO> bankAccUriDTOs = new ArrayList<UriDTO>();
		if (orgBankAccounts != null && !orgBankAccounts.isEmpty()) {
			for (OrgBankAccount orgBankAccount : orgBankAccounts) {
				bankAccUriDTOs.add(orgBankAccount.getBankAccount() != null ? getBankAccountsDTO(orgBankAccount.getBankAccount()) : null);
			}
		}
		paymentDetailDTO.setBankAccounts(bankAccUriDTOs);
		
		List<OrgPaymentMethod> orgPaymentMethods = getOrgPaymentMethods(orgIns);
		List<OrgDealerPaymentMethodDTO> orgPaymentMethodsDTO = new ArrayList<OrgDealerPaymentMethodDTO>();
		if (orgPaymentMethods != null && !orgPaymentMethods.isEmpty()) {
			for (OrgPaymentMethod orgPaymentMethod : orgPaymentMethods) {
				orgPaymentMethodsDTO.add(toIsrPaymentMethodDTO(orgPaymentMethod));
			}
		}
		paymentDetailDTO.setPaymentMethods(orgPaymentMethodsDTO);
		
		return paymentDetailDTO;
	}
	
	/**
	 * 
	 * @param orgPaymentMethod
	 * @return
	 */
	private OrgDealerPaymentMethodDTO toIsrPaymentMethodDTO(OrgPaymentMethod orgPaymentMethod) {
		OrgDealerPaymentMethodDTO orgPaymentMethodDTO = new OrgDealerPaymentMethodDTO();
		orgPaymentMethodDTO.setPaymentFlowType(orgPaymentMethod.getType() != null ? toRefDataDTO(orgPaymentMethod.getType()) : null);
		orgPaymentMethodDTO.setPaymentMethod(orgPaymentMethod.getPaymentMethod() != null ? getPaymentMethodsDTO(orgPaymentMethod.getPaymentMethod().getId()) : null);
		
		OrgAccountHolder holder = orgPaymentMethod.getOrgAccountHolder();
		Long accHolderId = null;
		if (holder != null) {
			accHolderId = holder.getAccountHolder();
		}
		orgPaymentMethodDTO.setAccountHolder(accHolderId != null ? getAccountHoldersDTO(accHolderId) : null);
		
		OrgBankAccount account = orgPaymentMethod.getOrgBankAccount();
		Long bankAccId = null;
		if (account != null) {
			bankAccId = account.getBankAccount();
		}
		orgPaymentMethodDTO.setBankAccount(bankAccId != null ? getBankAccountsDTO(bankAccId) : null);
		return orgPaymentMethodDTO;
	}
	
	/**
	 * 
	 * @param orgIns
	 * @return
	 */
	private List<OrgPaymentMethod> getOrgPaymentMethods(Organization orgIns) {
		BaseRestrictions<OrgPaymentMethod> restrictions = new BaseRestrictions<>(OrgPaymentMethod.class);
		restrictions.addCriterion(Restrictions.eq(OrgPaymentMethod.ORGANIZATION, orgIns));
		restrictions.addCriterion(Restrictions.in(OrgPaymentMethod.TYPE, new EPaymentFlowType[] { EPaymentFlowType.AOM, EPaymentFlowType.LOS }));
		return ENTITY_SRV.list(restrictions);
	}
	
	/**
	 * 
	 * @param insClaims
	 * @return
	 */
	protected List<InsuranceLostClaimDTO> toInsuranceLostClaimDTOs(List<InsuranceClaims> insClaims) {
		List<InsuranceLostClaimDTO> insLostClaimSrv = new ArrayList<>();
		for (InsuranceClaims insClaim : insClaims) {
			insLostClaimSrv.add(toInsuranceClaimDTO(insClaim));
		}
		return insLostClaimSrv;
	}
	
	/**
	 * 
	 * @param insClaim
	 * @return
	 */
	private InsuranceLostClaimDTO toInsuranceClaimDTO(InsuranceClaims insClaim) {
		InsuranceLostClaimDTO insLostClaimDTO = new InsuranceLostClaimDTO();
		insLostClaimDTO.setRangeOfYear(insClaim.getRangeOfYear());
		insLostClaimDTO.setFrom(insClaim.getFrom());
		insLostClaimDTO.setTo(insClaim.getTo());
		insLostClaimDTO.setPremiumnRefundedPercentage(insClaim.getPremiumnRefundedPercentage());
		return insLostClaimDTO;
	}
	
	/**
	 * 
	 * @param insId
	 * @param serviceType
	 * @return
	 */
	private List<InsuranceFinService> getInsuranceFinService(Long insId, EServiceType serviceType) {
		FinService service = ENTITY_SRV.getByCode(FinService.class, serviceType.getCode());
		BaseRestrictions<InsuranceFinService> restrictions = new BaseRestrictions<>(InsuranceFinService.class);
		restrictions.addCriterion(Restrictions.eq(InsuranceFinService.INSURANCE + "." + InsuranceFinService.ID, insId));
		if (service.getId() != null) {
			restrictions.addCriterion(Restrictions.eq(InsuranceFinService.SERVICE + "." + InsuranceFinService.ID, service.getId()));
		}
		return ENTITY_SRV.list(restrictions);
	}
	
	/**
	 * 
	 * @param insId
	 * @return
	 */
	private List<InsuranceClaims> getInsuranceClaims(Long insId) {
		InsuranceClaimsRestriction restrictions = new InsuranceClaimsRestriction();
		restrictions.setInsuranceId(insId);
		return ENTITY_SRV.list(restrictions);
	}


}
