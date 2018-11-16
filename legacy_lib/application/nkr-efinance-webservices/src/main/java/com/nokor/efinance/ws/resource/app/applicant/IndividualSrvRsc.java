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

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.applicant.model.Employment;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.IndividualAddress;
import com.nokor.efinance.core.applicant.model.IndividualContactInfo;
import com.nokor.efinance.core.applicant.model.IndividualReferenceContactInfo;
import com.nokor.efinance.core.applicant.model.IndividualReferenceInfo;
import com.nokor.efinance.core.applicant.model.IndividualRestriction;
import com.nokor.efinance.core.applicant.model.IndividualSpouse;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.efinance.share.applicant.ContactInfoDTO;
import com.nokor.efinance.share.applicant.EmploymentDTO;
import com.nokor.efinance.share.applicant.IndividualCriteriaDTO;
import com.nokor.efinance.share.applicant.IndividualDTO;
import com.nokor.efinance.share.applicant.IndividualSpouseDTO;
import com.nokor.efinance.share.applicant.ReferenceInfoDTO;
import com.nokor.efinance.share.contract.ContractDTO;
import com.nokor.efinance.ws.resource.app.contract.BaseContractSrvRsc;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.organization.ContactInfo;
import com.nokor.ersys.messaging.share.address.AddressDTO;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;
/**
 * 
 * @author buntha.chea
 *
 */
@Path("/applicants/individuals")
public class IndividualSrvRsc extends BaseContractSrvRsc {

	/**
	 * LIST
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list() {		
		try {
			// get all individuals						
			List<Individual> individuals = ENTITY_SRV.list(Individual.class);
			List<IndividualDTO> individualDTOs = new ArrayList<>();
			for (Individual individual : individuals) {
				individualDTOs.add(toIndividualDTO(individual));
			}
			return ResponseHelper.ok(individualDTOs);
		} catch (Exception e) {
			String errMsg = "Error while searching Individuals [" + e.getMessage() + "]";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_LIST_KO, errMsg);
		} 
	}
	
	/**
	 * GET BY ID
	 * @param id
	 * @return
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(@PathParam("id") Long id) {
		try {
			LOG.debug("Individual - id. [" + id + "]");
		
			Individual individual = ENTITY_SRV.getById(Individual.class, id);
			
			if (individual == null) {
				String errMsg = "Individual [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));			
			}
			
			IndividualDTO individualDTO = toIndividualDTO(individual);
			
			return ResponseHelper.ok(individualDTO);
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_UNIQUE_KO, errMsg);
		}
	}
	
	/**
	 * 
	 * @param individualDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response create(IndividualDTO individualDTO) {
		try {
			Individual individual = toIndividual(individualDTO, null);
			
			INDIVI_SRV.saveOrUpdateIndividual(individual);
			
			individualDTO.setId(individual.getId());
			
			return ResponseHelper.ok(individualDTO);
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
	public Response update(@PathParam("id") Long id, IndividualDTO individualDTO) {
		try {
			LOG.debug("Update - id. [" + id + "]");
			Individual individual = toIndividual(individualDTO, id);
			
			INDIVI_SRV.saveOrUpdate(individual);
			
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
	 * @param ide
	 * @return
	 */
	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response delete(@PathParam("id") Long id) {
		
		try {
			LOG.debug(" [" + (id != null ? id : NULL) + "]");
			Individual individual = INDIVI_SRV.getById(Individual.class, id); 			
			INDIVI_SRV.deleteIndividual(individual);
			
			return ResponseHelper.deleteSucess();

		} catch (EntityNotValidParameterException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.PARAMETER_NOT_VALID, errMsg);
		} catch(Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.DELETE_KO, errMsg);
		}
	}
	
	/**
	 * get employment by id in Individual id
	 * @param indId
	 * @param empId
	 * @return
	 */
	@GET
	@Path("/{indId}/employments/{empId}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response getEmployment(@PathParam("indId") Long indId, @PathParam("empId") Long empId) {
		try {
			LOG.debug("Individual - id. [" + indId + "]");
			LOG.debug("Employment - id. [" + empId + "]");
		
			Individual individual = ENTITY_SRV.getById(Individual.class, indId);
			Employment employment = null;
			
			if (individual == null) {
				String errMsg = "Individual [" + indId + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));			
			} else {
				employment = checkEmployment(individual, empId);
				if (employment == null) {
					String errMsg = "Employment [" + empId + "]";
					throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));		
				}
			}
			
			EmploymentDTO employmentDTO = toEmploymentDTO(employment);
			
			return ResponseHelper.ok(employmentDTO);
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
	 * get list Employment in Individual id
	 * @param indId
	 * @return
	 */
	@GET
	@Path("/{indId}/employments")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response listEmployment(@PathParam("indId") Long indId) {		
		try {
			List<Employment> employments = null;
			// get all employment by indId	
			Individual individual = ENTITY_SRV.getById(Individual.class, indId);
			if (individual == null) {
				String errMsg = "Individual [" + indId + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));		
			} else {
				employments = individual.getEmployments();
			}
		
			List<EmploymentDTO> employmentDTOs = new ArrayList<>();
			for (Employment employment : employments) {
				employmentDTOs.add(toEmploymentDTO(employment));
			}
			return ResponseHelper.ok(employmentDTOs);
		} catch (Exception e) {
			String errMsg = "Error while searching Employments [" + e.getMessage() + "]";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_LIST_KO, errMsg);
		} 
	}
	
	/**
	 * CREATE Employment in Individual Id
	 * @param individualDTOimport com.nokor.efinance.share.applicant.EmploymentDTO;

	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/{indId}/employments")
	public Response createEmployment(EmploymentDTO employmentDTO, @PathParam("indId") Long indId) {
		try {
			Individual individual = ENTITY_SRV.getById(Individual.class, indId);
			Employment employment = toEmployment(employmentDTO, null);
			
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
			employment.setIndividual(individual);
			
			INDIVI_SRV.saveOrUpdateEmployment(employment);
			
			employmentDTO.setId(employment.getId());
			return ResponseHelper.ok(employmentDTO);
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
	 * UPDATE employment by id in Individual
	 * @param empId
	 * @param employmentDTO
	 * @return
	 */
	@PUT
	@Path("/{indId}/employments/{id}")
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response updateEmployment(@PathParam("indId") Long indId, @PathParam("id") Long empId, EmploymentDTO employmentDTO) {
		try {
			LOG.debug("Employment - id. [" + empId + "]");
			Employment employment = toEmployment(employmentDTO, empId);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
			
			INDIVI_SRV.saveOrUpdateEmployment(employment);
	
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
	 * @param indId
	 * @param empId
	 * @return
	 */
	@DELETE
	@Path("/{indId}/employments/{empId}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response deleteEmployment(@PathParam("indId") Long indId, @PathParam("empId") Long empId) {
		
		try {
			LOG.debug(" [" + (empId != null ? empId : NULL) + "]");
			Employment employment = INDIVI_SRV.getById(Employment.class, empId); 			
			INDIVI_SRV.deleteEmployment(employment);
			
			return ResponseHelper.deleteSucess();

		} catch (EntityNotValidParameterException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.PARAMETER_NOT_VALID, errMsg);
		} catch(Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.DELETE_KO, errMsg);
		}
	}
	
	/**
	 * get employment's address 
	 * @param indId
	 * @param addId
	 * @return
	 */
	@GET
	@Path("/{indId}/employments/{empId}/addresses/{addId}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response getEmploymentAddress(@PathParam("indId") Long indId, @PathParam("empId") Long empId, @PathParam("addId") Long addId) {
		try {
			LOG.debug("Individual - id. [" + indId + "]");
			LOG.debug("Address - id. [" + addId + "]");
		
			Employment employment = ENTITY_SRV.getById(Employment.class, indId);
			Address address = null;
			
			if (employment == null) {
				String errMsg = "Employment [" + indId + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));			
			} else {
				address = ENTITY_SRV.getById(Address.class, addId);
				if (address == null) {
					String errMsg = "Address [" + addId + "] In Individual [" + indId + "]";
					throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));		
				}
			}
			
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
	 * get list Address in Individual id
	 * @param indId
	 * @return
	 */
	@GET
	@Path("/{indId}/employments/{empId}/addresses")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response getEmploymentAddresses(@PathParam("indId") Long indId, @PathParam("empId") Long empId) {		
		try {
			// get all employment by indId	
			Employment employment = ENTITY_SRV.getById(Employment.class, empId);
			Address address = null;
			if (employment == null) {
				String errMsg = "Employment [" + indId + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));		
			} else {
				address = employment.getAddress();
			}		
			List<AddressDTO> addressDTOs = new ArrayList<>();
			addressDTOs.add(toAddressDTO(address));
			return ResponseHelper.ok(addressDTOs);
		} catch (Exception e) {
			String errMsg = "Error while searching Employments [" + e.getMessage() + "]";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_LIST_KO, errMsg);
		} 
	}
	
	/**
	 * CREATE Employment in Individual Id
	 * @param individualDTOimport com.nokor.efinance.share.applicant.EmploymentDTO;

	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/{indId}/employments/{empId}/addresses")
	public Response createEmploymentAddress(AddressDTO addressDTO, @PathParam("indId") Long indId, @PathParam("empId") Long empId) {
		try {
			Employment employment = ENTITY_SRV.getById(Employment.class, empId);
			Address address = toAddress(false, addressDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
			ENTITY_SRV.create(address);
			employment.setAddress(address);
			ENTITY_SRV.update(employment);

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
	 * Update address by id in Individual
	 * @param indId
	 * @param addId
	 * @param addressDTO
	 * @return
	 */
	@PUT
	@Path("/{indId}/employments/{empid}/addresses/{addId}")
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response updateEmploymentAddress(@PathParam("indId") Long indId, @PathParam("empId") Long empId, @PathParam("addId") Long addId, AddressDTO addressDTO) {
		try {
			LOG.debug("Address - id. [" + addId + "]");
			
			addressDTO.setId(addId);
			Address address = toAddress(false, addressDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
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
	 * @param indId
	 * @param empId
	 * @param 
	 * @return
	 */
	@DELETE
	@Path("/{indId}/employments/{empid}/addresses/{addId}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response deleteEmploymentAddress(@PathParam("indId") Long indId, @PathParam("empId") Long empId, @PathParam("addId") Long addId) {
		
		try {
			LOG.debug(" [" + (addId != null ? addId : NULL) + "]");
			Address address = INDIVI_SRV.getById(Address.class, addId); 			
			INDIVI_SRV.delete(address);
			
			return ResponseHelper.deleteSucess();

		} catch (EntityNotValidParameterException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.PARAMETER_NOT_VALID, errMsg);
		} catch(Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.DELETE_KO, errMsg);
		}
	}
	
	/**
	 * get address by in in Individual
	 * @param indId
	 * @param addId
	 * @return
	 */
	@GET
	@Path("/{indId}/addresses/{addId}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response getAddress(@PathParam("indId") Long indId, @PathParam("addId") Long addId) {
		try {
			LOG.debug("Individual - id. [" + indId + "]");
			LOG.debug("Address - id. [" + addId + "]");
		
			Individual individual = ENTITY_SRV.getById(Individual.class, indId);
			Address address = null;
			
			if (individual == null) {
				String errMsg = "Individual [" + indId + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			} else {
				address = checkAddress(individual, addId);
				if (address == null) {
					String errMsg = "Address [" + addId + "] In Individual [" + indId + "]";
					throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));		
				}
			}
			
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
	 * get list Address in Individual id
	 * @param indId
	 * @return
	 */
	@GET
	@Path("/{indId}/addresses")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response getAddresses(@PathParam("indId") Long indId) {		
		try {
			List<IndividualAddress> individualAddresses = null;
			// get all employment by indId	
			Individual individual = ENTITY_SRV.getById(Individual.class, indId);
			if (individual == null) {
				String errMsg = "Individual [" + indId + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));		
			} else {
				individualAddresses = individual.getIndividualAddresses();
			}
		
			List<AddressDTO> addressDTOs = new ArrayList<>();
			for (IndividualAddress individualAddress : individualAddresses) {
				addressDTOs.add(toAddressDTO(individualAddress.getAddress()));
			}
			return ResponseHelper.ok(addressDTOs);
		} catch (Exception e) {
			String errMsg = "Error while searching Employments [" + e.getMessage() + "]";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_LIST_KO, errMsg);
		} 
	}
	
	/**
	 * CREATE Employment in Individual Id
	 * @param individualDTOimport com.nokor.efinance.share.applicant.EmploymentDTO;

	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/{indId}/addresses")
	public Response createAddress(AddressDTO addressDTO, @PathParam("indId") Long indId) {
		try {
			Individual individual = ENTITY_SRV.getById(Individual.class, indId);
			Address address = toAddress(false, addressDTO);
			
			IndividualAddress individualAddress = new IndividualAddress();
			individualAddress.setIndividual(individual);
			individualAddress.setAddress(address);
			
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
			
			ENTITY_SRV.create(address);
			ENTITY_SRV.create(individualAddress);
			
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
	 * Update address by id in Individual
	 * @param indId
	 * @param addId
	 * @param addressDTO
	 * @return
	 */
	@PUT
	@Path("/{indId}/addresses/{addId}")
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response updateAddress(@PathParam("indId") Long indId, @PathParam("addId") Long addId, AddressDTO addressDTO) {
		try {
			LOG.debug("Address - id. [" + addId + "]");
			
			addressDTO.setId(addId);
			Address address = toAddress(false, addressDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
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
	 * @param indId
	 * @param addId
	 * @param 
	 * @return
	 */
	@DELETE
	@Path("/{indId}/addresses/{addId}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response deleteAddress(@PathParam("indId") Long indId, @PathParam("addId") Long addId) {
		
		try {
			LOG.debug(" [" + (addId != null ? addId : NULL) + "]");
			Individual individual = ENTITY_SRV.getById(Individual.class, indId);
			if (individual == null) {
				String errMsg = "Individual [" + indId + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			} else {
				List<IndividualAddress> individualAddresses = individual.getIndividualAddresses();
				if (individualAddresses != null && !individualAddresses.isEmpty()) {
					for (IndividualAddress individualAddress : individualAddresses) {
						if (individualAddress.getAddress().getId().equals(addId)) {
							ENTITY_SRV.delete(individualAddress);
							ENTITY_SRV.delete(individualAddress.getAddress());
							break;
						}
					}
				}
			}
			
			return ResponseHelper.deleteSucess();

		} catch (EntityNotValidParameterException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.PARAMETER_NOT_VALID, errMsg);
		} catch(Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.DELETE_KO, errMsg);
		}
	}
	
	@GET
	@Path("/{indId}/references/{refId}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response getReference(@PathParam("indId") Long indId, @PathParam("refId") Long refId) {
		try {
			LOG.debug("Individual - id. [" + indId + "]");
			LOG.debug("Reference - id. [" + refId + "]");
		
			Individual individual = ENTITY_SRV.getById(Individual.class, indId);
			IndividualReferenceInfo individualReferenceInfo = null;
			
			if (individual == null) {
				String errMsg = "Individual [" + indId + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));			
			} else {
				individualReferenceInfo = checkReference(individual, refId);
				if (individualReferenceInfo == null) {
					String errMsg = "ReferenceInfo [" + refId + "] In Individual [" + indId + "]";
					throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));		
				}
			}
			
			ReferenceInfoDTO referenceInfoDTO = toReferenceInfoDTO(individualReferenceInfo);
			referenceInfoDTO.setId(individualReferenceInfo.getId());
			return ResponseHelper.ok(referenceInfoDTO);
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
	 * Get List Reference info In Individual
	 * @param indId
	 * @param refId
	 * @return
	 */
	@GET
	@Path("/{indId}/references")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response getReferences(@PathParam("indId") Long indId) {		
		try {
			List<IndividualReferenceInfo> individualReferenceInfos = null;
			// get all referenceInfo by indId	
			Individual individual = ENTITY_SRV.getById(Individual.class, indId);
			if (individual == null) {
				String errMsg = "Individual [" + indId + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));		
			} else {
				individualReferenceInfos = individual.getIndividualReferenceInfos();
			}
		
			List<ReferenceInfoDTO> referenceInfoDTOs = new ArrayList<>();
			for (IndividualReferenceInfo individualReferenceInfo : individualReferenceInfos) {
				referenceInfoDTOs.add(toReferenceInfoDTO(individualReferenceInfo));
			}
			return ResponseHelper.ok(referenceInfoDTOs);
		} catch (Exception e) {
			String errMsg = "Error while searching Employments [" + e.getMessage() + "]";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_LIST_KO, errMsg);
		} 
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/{indId}/references")
	public Response createReference(ReferenceInfoDTO referenceInfoDTO, @PathParam("indId") Long indId) {
		try {
			Individual individual = ENTITY_SRV.getById(Individual.class, indId);
			IndividualReferenceInfo individualReferenceInfo = toIndividualReferenceInfo(referenceInfoDTO);
			
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
			individualReferenceInfo.setIndividual(individual);
			INDIVI_SRV.saveOrUpdateReferenceInfo(individualReferenceInfo);
			
			return ResponseHelper.ok(toReferenceInfoDTO(individualReferenceInfo));
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
	
	@PUT
	@Path("/{indId}/references/{refId}")
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response updateReference(@PathParam("indId") Long indId, @PathParam("refId") Long refId, ReferenceInfoDTO referenceInfoDTO) {
		try {
			LOG.debug("ReferenceInfo - id. [" + refId + "]");
			
			referenceInfoDTO.setId(refId);
			IndividualReferenceInfo individualReferenceInfo = toIndividualReferenceInfo(referenceInfoDTO);
			
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			} 
			
			INDIVI_SRV.saveOrUpdateReferenceInfo(individualReferenceInfo);
	
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
	 * @param indId
	 * @param addId
	 * @param 
	 * @return
	 */
	@DELETE
	@Path("/{indId}/references/{refId}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response deleteReference(@PathParam("refId") Long refId) {
		
		try {
			LOG.debug(" [" + (refId != null ? refId : NULL) + "]");
			IndividualReferenceInfo reference = INDIVI_SRV.getById(IndividualReferenceInfo.class, refId); 			
			INDIVI_SRV.deleteReference(reference);
			
			return ResponseHelper.deleteSucess();

		} catch (EntityNotValidParameterException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.PARAMETER_NOT_VALID, errMsg);
		} catch(Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.DELETE_KO, errMsg);
		}
	}
	
	/**
	 * Get contact info by id in individual
	 * @param indId
	 * @param conId
	 * @return
	 */
	@GET
	@Path("/{indId}/contactinfos/{conId}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response getContactInfo(@PathParam("indId") Long indId, @PathParam("conId") Long conId) {
		try {
			LOG.debug("Individual - id. [" + indId + "]");
			LOG.debug("ContactInfo - id. [" + conId + "]");
		
			Individual individual = ENTITY_SRV.getById(Individual.class, indId);
			ContactInfo contactInfo = null;
			
			if (individual == null) {
				String errMsg = "ContactInfo [" + indId + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));			
			} else {
				contactInfo = checkContactInfo(individual, conId);
				if (contactInfo == null) {
					String errMsg = "ContactInfo [" + conId + "] In Individual [" + indId + "]";
					throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));		
				}
			}
			
			ContactInfoDTO contactInfoDTO = toContactInfoDTO(contactInfo);
			
			return ResponseHelper.ok(contactInfoDTO);
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
	 * get list contact info in Individual id
	 * @param indId
	 * @return
	 */
	@GET
	@Path("/{indId}/contactinfos")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response getContactInfos(@PathParam("indId") Long indId) {		
		try {
			List<IndividualContactInfo> individualContactInfos = null;
			// get all contact info by indId	
			Individual individual = ENTITY_SRV.getById(Individual.class, indId);
			if (individual == null) {
				String errMsg = "Individual [" + indId + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));		
			} else {
				individualContactInfos = individual.getIndividualContactInfos();
			}
		
			List<ContactInfoDTO> contactInfoDTOs = new ArrayList<>();
			for (IndividualContactInfo individualContactInfo : individualContactInfos) {
				contactInfoDTOs.add(toContactInfoDTO(individualContactInfo.getContactInfo()));
			}
			return ResponseHelper.ok(contactInfoDTOs);
		} catch (Exception e) {
			String errMsg = "Error while searching ContactInfos [" + e.getMessage() + "]";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_LIST_KO, errMsg);
		} 
	}
	
	/**
	 * CREATE Contact Info in Individual
	 * @param contactInfoDTO
	 * @param indId
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/{indId}/contactinfos")
	public Response createContactInfo(ContactInfoDTO contactInfoDTO, @PathParam("indId") Long indId) {
		try {
			Individual individual = ENTITY_SRV.getById(Individual.class, indId);
			if (individual == null) {
				String errMsg = "Individual [" + indId + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			} else {
				ContactInfo contactInfo = toContactInfo(contactInfoDTO);
				
				IndividualContactInfo individualContactInfo = new IndividualContactInfo();
				individualContactInfo.setIndividual(individual);
				individualContactInfo.setContactInfo(contactInfo);
				
				if (!messages.isEmpty()) {
					String errMsg = messages.get(0).getDesc();
					throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
				}
				
				ENTITY_SRV.create(contactInfo);
				ENTITY_SRV.create(individualContactInfo);
				
				contactInfoDTO.setId(contactInfo.getId());
			}
			return ResponseHelper.ok(contactInfoDTO);
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
	 * Update contact info by id in Individual
	 * @param indId
	 * @param conId
	 * @param contactInfoDTO
	 * @return
	 */
	@PUT
	@Path("/{indId}/contactinfos/{conId}")
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response updateContactInfo(@PathParam("indId") Long indId, @PathParam("conId") Long conId, ContactInfoDTO contactInfoDTO) {
		try {
			LOG.debug("ContactInfo - id. [" + conId + "]");
			
			contactInfoDTO.setId(conId);
			ContactInfo contactInfo = toContactInfo(contactInfoDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			} 
			
			ENTITY_SRV.update(contactInfo);
	
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
	 * DELETE Contact info by id in Individual
	 * @param indId
	 * @param conId
	 * @return
	 */
	@DELETE
	@Path("/{indId}/contactinfos/{conId}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response deleteContactInfo(@PathParam("indId") Long indId, @PathParam("conId") Long conId) {
		
		try {
			LOG.debug(" [" + (conId != null ? conId : NULL) + "]");
			Individual individual = ENTITY_SRV.getById(Individual.class, indId);
			if (individual == null) {
				String errMsg = "Individual [" + indId + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			} else {
				List<IndividualContactInfo> individualContactInfos = individual.getIndividualContactInfos();
				if (individualContactInfos != null && !individualContactInfos.isEmpty()) {
					for (IndividualContactInfo individualContactInfo : individualContactInfos) {
						if (individualContactInfo.getContactInfo().getId().equals(conId)) {
							ENTITY_SRV.delete(individualContactInfo);
							ENTITY_SRV.delete(individualContactInfo.getContactInfo());
							break;
						}
					}
				}
			}		
			
			return ResponseHelper.deleteSucess();
			
		} catch (EntityNotValidParameterException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.PARAMETER_NOT_VALID, errMsg);
		} catch(Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.DELETE_KO, errMsg);
		}
	}
	
	/**
	 * 
	 * @param indId
	 * @param refId
	 * @param conId
	 * @param contactInfoDTO
	 * @return
	 */
	@PUT
	@Path("/{indId}/references/{refId}/contactinfos/{conId}")
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response updateIndReferenceContactInfo(@PathParam("indId") Long indId, @PathParam("refId") Long refId, 
			@PathParam("conId") Long conId, ContactInfoDTO contactInfoDTO) {
		
		try { 
			LOG.debug(" [" + (conId != null ? conId : NULL) + "]");
			contactInfoDTO.setId(conId);
			ContactInfo contactInfo = null;
			
			Individual individual = ENTITY_SRV.getById(Individual.class, indId);
			if (individual == null) {
				String errMsg = "Individual-ID [" + indId + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			} else {
				List<IndividualReferenceInfo> referenceInfos = individual.getIndividualReferenceInfos();
				IndividualReferenceInfo indRefInfo = null;
				if (referenceInfos != null && !referenceInfos.isEmpty()) {
					for (IndividualReferenceInfo referenceInfo : referenceInfos) {
						if (refId != null && refId.equals(referenceInfo.getId())) {
							indRefInfo = referenceInfo;
							break;
						}
					}
				}
				if (indRefInfo == null) {
					String errMsg = "Individual-ReferenceInfo-ID [" + refId + "]";
					throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
				} else {
					List<IndividualReferenceContactInfo> indRefContactInfos = indRefInfo.getIndividualReferenceContactInfos();
					if (indRefContactInfos != null && !indRefContactInfos.isEmpty()) {
						for (IndividualReferenceContactInfo indRefContactInfo : indRefContactInfos) {
							if (indRefContactInfo.getContactInfo().getId().equals(conId)) {
								contactInfo = toContactInfo(contactInfoDTO);
								if (!messages.isEmpty()) {
									String errMsg = messages.get(0).getDesc();
									throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
								}
							}
						}
					}
					if (contactInfo == null) {
						String errMsg = "ContactInfo-ID [" + conId + "]";
						throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
					} else {
						ENTITY_SRV.update(contactInfo);
					}
				}
			}		
			
			return ResponseHelper.updateSucess();
			
		} catch (EntityNotValidParameterException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.PARAMETER_NOT_VALID, errMsg);
		} catch(Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.DELETE_KO, errMsg);
		}
	}
	
	/**
	 * 
	 * @param indId
	 * @param refId
	 * @param conId
	 * @return
	 */
	@DELETE
	@Path("/{indId}/references/{refId}/contactinfos/{conId}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response deleteIndReferenceContactInfo(@PathParam("indId") Long indId, @PathParam("refId") Long refId, @PathParam("conId") Long conId) {
		
		try {
			LOG.debug(" [" + (conId != null ? conId : NULL) + "]");
			Individual individual = ENTITY_SRV.getById(Individual.class, indId);
			boolean isContactInfo = false;
			if (individual == null) {
				String errMsg = "Individual-ID [" + indId + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			} else {
				List<IndividualReferenceInfo> referenceInfos = individual.getIndividualReferenceInfos();
				IndividualReferenceInfo indRefInfo = null;
				if (referenceInfos != null && !referenceInfos.isEmpty()) {
					for (IndividualReferenceInfo referenceInfo : referenceInfos) {
						if (refId != null && refId.equals(referenceInfo.getId())) {
							indRefInfo = referenceInfo;
							break;
						}
					}
				}
				if (indRefInfo == null) {
					String errMsg = "Individual-ReferenceInfo-ID [" + refId + "]";
					throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
				} else {
					List<IndividualReferenceContactInfo> indRefContactInfos = indRefInfo.getIndividualReferenceContactInfos();
					if (indRefContactInfos != null && !indRefContactInfos.isEmpty()) {
						for (IndividualReferenceContactInfo indRefContactInfo : indRefContactInfos) {
							if (indRefContactInfo.getContactInfo().getId().equals(conId)) {
								isContactInfo = true;
							}
						}
					}
					if (!isContactInfo) {
						String errMsg = "ContactInfo-ID [" + conId + "]";
						throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
					} else {
						INDIVI_SRV.deleteContactInfo(indId, refId, conId);
					}
				}
			}		
			
			return ResponseHelper.deleteSucess();
			
		} catch (EntityNotValidParameterException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.PARAMETER_NOT_VALID, errMsg);
		} catch(Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.DELETE_KO, errMsg);
		}
	}
	
	/**
	 * get spouse by id in Individual id
	 * @param indId
	 * @param spoId
	 * @return
	 */
	@GET
	@Path("/{indId}/spouses/{spoId}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response getSpouse(@PathParam("indId") Long indId, @PathParam("spoId") Long spoId) {
		try {
			LOG.debug("Individual - id. [" + indId + "]");
			LOG.debug("Spouse - id. [" + spoId + "]");
		
			Individual individual = ENTITY_SRV.getById(Individual.class, indId);
			IndividualSpouse spouse = null;
			
			if (individual == null) {
				String errMsg = "Individual [" + indId + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));			
			} else {
				spouse = checkSpouse(individual, spoId);
				if (spouse == null) {
					String errMsg = "Spouse [" + spoId + "]";
					throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));		
				}
			}
			
			IndividualSpouseDTO spouseDTO = toIndividualSpouseDTO(spouse);
			
			return ResponseHelper.ok(spouseDTO);
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
	 * get list Spouse in Individual id
	 * @param indId
	 * @return
	 */
	@GET
	@Path("/{indId}/spouses")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response listSpouse(@PathParam("indId") Long indId) {		
		try {
			List<IndividualSpouse> spouses = null;
			Individual individual = ENTITY_SRV.getById(Individual.class, indId);
			if (individual == null) {
				String errMsg = "Individual [" + indId + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));		
			} else {
				spouses = individual.getIndividualSpouses();
			}
		
			List<IndividualSpouseDTO> spouseDTOs = new ArrayList<>();
			for (IndividualSpouse spouse : spouses) {
				spouseDTOs.add(toIndividualSpouseDTO(spouse));
			}
			return ResponseHelper.ok(spouseDTOs);
		} catch (Exception e) {
			String errMsg = "Error while searching Spouses [" + e.getMessage() + "]";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_LIST_KO, errMsg);
		} 
	}
	
	/**
	 * CREATE Spouse in Individual Id
	 * @param spouseDTO
	 * @param indId
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/{indId}/spouses")
	public Response createSpouse(IndividualSpouseDTO spouseDTO, @PathParam("indId") Long indId) {
		try {
			Individual individual = ENTITY_SRV.getById(Individual.class, indId);
			IndividualSpouse spouse = toIndividualSpouse(spouseDTO);
			
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
			spouse.setIndividual(individual);
			INDIVI_SRV.create(spouse);
			return ResponseHelper.ok(toIndividualSpouseDTO(spouse));
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
	 * UPDATE spouse by id in Individual
	 * @param spouseId
	 * @param spouseDTO
	 * @return
	 */
	@PUT
	@Path("/{indId}/spouses/{id}")
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response updateSpouse(@PathParam("indId") Long indId, @PathParam("id") Long spouseId, IndividualSpouseDTO spouseDTO) {
		try {
			LOG.debug("Spouse - id. [" + spouseId + "]");
			spouseDTO.setId(spouseId);
			IndividualSpouse spouse = toIndividualSpouse(spouseDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			} 
			INDIVI_SRV.update(spouse);
	
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
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.UPDATE_KO, errMsg);
		}
	}	
	
	/**
	 * Delete spouse
	 * @param indId
	 * @param spoId
	 * @return
	 */
	@DELETE
	@Path("/{indId}/spouses/{spoId}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response deleteSpouse(@PathParam("indId") Long indId, @PathParam("spoId") Long spoId) {
		
		try {
			LOG.debug(" [" + (spoId != null ? spoId : NULL) + "]");
			IndividualSpouse spouse = INDIVI_SRV.getById(IndividualSpouse.class, spoId); 			
			INDIVI_SRV.delete(spouse);
			
			return ResponseHelper.deleteSucess();
		} catch (EntityNotValidParameterException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.PARAMETER_NOT_VALID, errMsg);
		} catch(Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.DELETE_KO, errMsg);
		}
	}
	
	/**
	 * Search individual info by id number, birth date, first name & last name
	 * @param individualCriteriaDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/search")
	public Response searchIndividual(IndividualCriteriaDTO individualCriteriaDTO) {
		try {
			LOG.debug("Individual [" + individualCriteriaDTO + "]");
			
			IndividualRestriction restrictions = toIndividualRestriction(individualCriteriaDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
			List<Individual> individuals = ENTITY_SRV.list(restrictions);
			
			return ResponseHelper.ok(toIndividualsDTO(individuals));
			
		} catch (IllegalStateException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.KO, errMsg);
		} catch (Exception e) {
			String errMsg = "Error while searching Individual[" + individualCriteriaDTO + "]";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.KO, errMsg);
		}
	}
	
	/**
	 * Get contract histories of individual
	 * @param indId
	 * @return
	 */
	@GET
	@Path("/{indId}/contracthistories")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response getContractHistories(@PathParam("indId") Long indId) {
		try {
			List<ContractDTO> contractDTOs = new ArrayList<>();
			for (Contract contract : INDIVI_SRV.getContractsByIndividual(indId)) {
				EWkfStatus contractWkfStatus = contract.getWkfStatus(); 
				if (!(contractWkfStatus.equals(ContractWkfStatus.PEN)
						|| contractWkfStatus.equals(ContractWkfStatus.BLOCKED))) {					
					contractDTOs.add(toContractDTO(contract));
				}
			}
			return ResponseHelper.ok(contractDTOs);
		} catch (Exception e) {
			String errMsg = "Error while searching Contracts [" + e.getMessage() + "]";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_LIST_KO, errMsg);
		}
	}
	
	/**
	 * Get guarantee histories of individual
	 * @param indId
	 * @return
	 */
	@GET
	@Path("/{indId}/guaranteehistories")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response getGuaranteeHistories(@PathParam("indId") Long indId) {
		try {
			List<ContractDTO> contractDTOs = new ArrayList<>();
			for (Contract contract : INDIVI_SRV.getContractsGuarantorByIndividual(indId)) {
				EWkfStatus contractWkfStatus = contract.getWkfStatus(); 
				if (!(contractWkfStatus.equals(ContractWkfStatus.PEN)
						|| contractWkfStatus.equals(ContractWkfStatus.BLOCKED))) {					
					contractDTOs.add(toContractDTO(contract));
				}
			}
			return ResponseHelper.ok(contractDTOs);
		} catch (Exception e) {
			String errMsg = "Error while searching Contract [" + e.getMessage() + "]";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_LIST_KO, errMsg);
		}
	}
	
	/**
	 * 
	 * @param individuals
	 * @return
	 */
	protected List<IndividualDTO> toIndividualsDTO(List<Individual> individuals) {
		List<IndividualDTO> dtoLst = new ArrayList<>();
		for (Individual individual : individuals) {
			dtoLst.add(toIndividualDTO(individual));
		}
		return dtoLst;
	}
	
	/**
	 * 
	 * @param criteria
	 * @return
	 */
	private IndividualRestriction toIndividualRestriction(IndividualCriteriaDTO criteria) {
		IndividualRestriction restrictions = new IndividualRestriction();
		restrictions.setIdNumber(criteria.getIdNumber());
		restrictions.setLastName(criteria.getLastName());
		restrictions.setFirstName(criteria.getFirstName());
		restrictions.setBirthDate(criteria.getBirthDate());
		return restrictions;
	}
	
	/**
	 * 
	 * @param individual
	 * @param id
	 * @return
	 */
	private Employment checkEmployment(Individual individual, Long id) {
		for (Employment employment : individual.getEmployments()) {
			if (employment.getId().equals(id)) {
				return employment;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param individual
	 * @param id
	 * @return
	 */
	private Address checkAddress(Individual individual, Long id) {
		for (IndividualAddress individualAddress : individual.getIndividualAddresses()) {
			if (individualAddress.getAddress().getId().equals(id)) {
				return individualAddress.getAddress();
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param individual
	 * @param id
	 * @return
	 */
	private IndividualSpouse checkSpouse(Individual individual, Long id) {
		for (IndividualSpouse spouse : individual.getIndividualSpouses()) {
			if (spouse.getId().equals(id)) {
				return spouse;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param individual
	 * @param conId
	 * @return
	 */
	private ContactInfo checkContactInfo(Individual individual, Long conId) {
		for (IndividualContactInfo individualContactInfo : individual.getIndividualContactInfos()) {
			if (individualContactInfo.getContactInfo().getId().equals(conId)) {
				return individualContactInfo.getContactInfo();
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param individual
	 * @param refId
	 * @return
	 */
	private IndividualReferenceInfo checkReference(Individual individual, Long refId) {
		for (IndividualReferenceInfo individualReferenceInfo : individual.getIndividualReferenceInfos()) {
			if (individualReferenceInfo.getId().equals(refId)) {
				return individualReferenceInfo;
			}
		}
		return null;
	}
	
}