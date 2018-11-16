package com.nokor.efinance.ws.resource.app.applicant;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
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

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.exception.EntityAlreadyExistsException;
import org.seuksa.frmk.tools.exception.EntityCreationException;
import org.seuksa.frmk.tools.exception.EntityNotValidParameterException;
import org.springframework.dao.DataIntegrityViolationException;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.ApplicantRestriction;
import com.nokor.efinance.core.applicant.model.Company;
import com.nokor.efinance.core.applicant.model.EApplicantCategory;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.share.applicant.ApplicantCriteriaDTO;
import com.nokor.efinance.share.applicant.ApplicantDTO;
import com.nokor.efinance.share.applicant.CompanyDTO;
import com.nokor.efinance.share.applicant.IndividualDTO;
import com.nokor.efinance.ws.FinResourceSrvRsc;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;
/**
 * 
 * @author buntha.chea
 *
 */
@Path("/applicants")
public class ApplicantSrvRsc extends FinResourceSrvRsc {

	/**
	 * GET LIST APPLICANTS
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list() {		
		try {
			// get all applicants						
			List<Applicant> applicants = ENTITY_SRV.list(Applicant.class);
			List<ApplicantDTO> applicantDTOs = new ArrayList<>();
			for (Applicant applicant : applicants) {
				applicantDTOs.add(toApplicantDTO(applicant));
			}
			return ResponseHelper.ok(applicantDTOs);
		} catch (Exception e) {
			String errMsg = "Error while searching Individuals [" + e.getMessage() + "]";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_LIST_KO, errMsg);
		} 
	}
	
	/**
	 * GET APPLICANT BY ID
	 * @param id
	 * @return
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(@PathParam("id") Long id) {
		try {
			LOG.debug("Applicant - id. [" + id + "]");
		
			Applicant applicant = ENTITY_SRV.getById(Applicant.class, id);
			
			if (applicant == null) {
				String errMsg = "Applicant [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));			
			}
			
			ApplicantDTO applicantDTO = toApplicantDTO(applicant);
			
			return ResponseHelper.ok(applicantDTO);
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
	 * @param applicantDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response create(ApplicantDTO applicantDTO) {
		try {
			Applicant applicant = toApplicant(applicantDTO, (Long) null);
			ENTITY_SRV.saveOrUpdate(applicant);
			applicantDTO.setId(applicant.getId());
			return ResponseHelper.ok(applicantDTO);
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
	 * UPDATE 
	 * @param id
	 * @param individualDTO
	 * @return
	 */
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response update(@PathParam("id") Long id, ApplicantDTO applicantDTO) {
		try {
			LOG.debug("Applicant - id. [" + id + "]");
			Applicant applicant = toApplicant(applicantDTO, id);
			
			ENTITY_SRV.saveOrUpdate(applicant);
			
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
	 * DELETE Applicant
	 * @param id
	 * @return
	 */
	@DELETE
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response delete(@PathParam("id") Long id) {
		try {
			LOG.debug("Applicant [" + (id != null ? id : NULL) + "]");
			
			Applicant applicant = APP_SRV.getById(Applicant.class, id);
			
			APP_SRV.deleteApplicant(applicant);
			
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
			String errMsg = I18N.messageObjectCanNotDelete("" + id);
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.DELETE_KO, errMsg);
		} catch(Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.DELETE_KO, errMsg);
		}
	}
	
	/**
	 * Search applicant
	 * @param applicantCriteriaDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/search")
	public Response searchApplicant(ApplicantCriteriaDTO applicantCriteriaDTO) {
		try {
			LOG.debug("Applicant [" + applicantCriteriaDTO + "]");
			
			ApplicantRestriction restrictions = toApplicantRestriction(applicantCriteriaDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
			List<Applicant> applicants = ENTITY_SRV.list(restrictions);
			
			return ResponseHelper.ok(toApplicantsDTO(applicants));
			
		} catch (IllegalStateException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.KO, errMsg);
		} catch (Exception e) {
			String errMsg = "Error while searching Applicant[" + applicantCriteriaDTO + "]";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.KO, errMsg);
		}
	}
	
	/**
	 * GET INDIVIDUAL BY APPLICANT BY ID
	 * @param id
	 * @return
	 */
	@GET
	@Path("/{appId}/individual")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response getIndividualByAppId(@PathParam("appId") Long id) {
		try {
			LOG.debug("Applicant - id. [" + id + "]");
		
			Applicant applicant = ENTITY_SRV.getById(Applicant.class, id);
			
			if (applicant == null) {
				String errMsg = "Applicant [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));			
			}
			
			Individual individual = applicant.getIndividual();
			IndividualDTO individualDTO = null;
			if (individual != null) {
				individualDTO = toIndividualDTO(individual);
			}
			
			return ResponseHelper.ok(individualDTO);
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
	 * GET COMPANY BY APPLICANT BY ID
	 * @param id
	 * @return
	 */
	@GET
	@Path("/{appId}/company")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response getCompanyByAppId(@PathParam("appId") Long id) {
		try {
			LOG.debug("Applicant - id. [" + id + "]");
		
			Applicant applicant = ENTITY_SRV.getById(Applicant.class, id);
			
			if (applicant == null) {
				String errMsg = "Applicant [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));			
			}
			
			Company company = applicant.getCompany();
			CompanyDTO companyDTO = null;
			if (company != null) {
				companyDTO = toCompanyDTO(company);
			}
			
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
	 * @param applicants
	 * @return
	 */
	protected List<ApplicantDTO> toApplicantsDTO(List<Applicant> applicants) {
		List<ApplicantDTO> applicantDTOs = new ArrayList<>();
		for (Applicant applicant : applicants) {
			applicantDTOs.add(toApplicantDTO(applicant));
		}
		return applicantDTOs;
	}
	
	/**
	 * 
	 * @param criteria
	 * @return
	 */
	private ApplicantRestriction toApplicantRestriction(ApplicantCriteriaDTO criteria) {
		ApplicantRestriction restrictions = new ApplicantRestriction();
		restrictions.setApplicantCategory(criteria.getApplicantCategoryID() == ApplicantCriteriaDTO.SEARCH_BY_COMPANY ? 
				EApplicantCategory.COMPANY : EApplicantCategory.INDIVIDUAL);
		restrictions.setApplicantID(criteria.getApplicantID());
		restrictions.setCreateDate(criteria.getCreateDate());
		restrictions.setIdNumber(criteria.getIdNumber());
		restrictions.setStatus(criteria.getStatus());
		restrictions.setPhoneNumber(criteria.getPhoneNumber());
		restrictions.setFirstName(criteria.getFirstName());
		restrictions.setLastName(criteria.getLastName());
		return restrictions;
	}
}
