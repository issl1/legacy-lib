package com.nokor.efinance.ws.resource.app.contract.appointment;

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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.exception.EntityAlreadyExistsException;
import org.seuksa.frmk.tools.exception.EntityCreationException;
import org.seuksa.frmk.tools.exception.EntityNotValidParameterException;
import org.seuksa.frmk.tools.exception.EntityUpdateException;
import org.springframework.dao.DataIntegrityViolationException;

import com.nokor.common.messaging.ws.resource.BaseWsPath;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.contract.model.Appointment;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.service.AppointmentRestriction;
import com.nokor.efinance.share.common.FinWsMessage;
import com.nokor.efinance.share.contract.AppointmentDTO;
import com.nokor.efinance.ws.resource.app.contract.BaseContractSrvRsc;
import com.nokor.efinance.ws.resource.app.contract.ContractWsPath;
import com.nokor.ersys.core.hr.model.eref.ELocation;
import com.nokor.ersys.core.hr.model.organization.OrgStructure;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * 
 * @author uhout.cheng
 */
@Path(ContractWsPath.CONTRACTS + ContractWsPath.APPOINTMENTS)
public class AppointmentSrvRsc extends BaseContractSrvRsc {
	
	/**
	 * List all appointments by contract
	 * @param contractNo
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list(@QueryParam(ContractWsPath.REFERENCE) String contractNo) {
		try {
			AppointmentRestriction restrictions = new AppointmentRestriction();
			restrictions.setContractNo(contractNo);
			
			List<Appointment> appointments = ENTITY_SRV.list(restrictions);
			List<AppointmentDTO> appointmentDTOs= toAppointmentDTOs(appointments);
			
			return ResponseHelper.ok(appointmentDTOs);
		} catch (Exception e) {
			String errMsg = "Error while searching appointment [" + e.getMessage() + "]";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_LIST_KO, errMsg);
		}
	}
	
	/**
	 * List appointment by id
	 * @param id
	 * @return
	 */
	@GET
	@Path(BaseWsPath.PATH_ID)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(@PathParam(BaseWsPath.ID) Long id) {
		try {
			LOG.debug("Appointment - id. [" + id + "]");
			
			Appointment appointment = ENTITY_SRV.getById(Appointment.class, id);
			if (appointment == null) {
				String errMsg = "Appointment [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));			
			}
			AppointmentDTO appointmentDTO = toAppointmentDTO(appointment);
			
			return ResponseHelper.ok(appointmentDTO);
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
	 * Create an appointment
	 * @param appointmentDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response create(AppointmentDTO appointmentDTO) {
		try {
			if (appointmentDTO == null) {
				throw new EntityNotValidParameterException(I18N.messageMandatoryField("AppointmentDTO"));
			}
			Appointment appointment = toAppointment(appointmentDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new EntityNotValidParameterException(I18N.messageObjectNotFound(errMsg));
			}
			ENTITY_SRV.create(appointment);
			
			return ResponseHelper.ok(toAppointmentDTO(appointment));
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
	 * Update an appointment by id
	 * @param id
	 * @param appointmentDTO
	 * @return
	 */
	@PUT
	@Path(BaseWsPath.PATH_ID)
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response update(@PathParam("id") Long id, AppointmentDTO appointmentDTO) {
		try {
			LOG.debug("Appointment - id. [" + id + "]");
			appointmentDTO.setId(id);
			Appointment appointment = toAppointment(appointmentDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			} 
			
			ENTITY_SRV.update(appointment);
			
			return ResponseHelper.updateSucess();
		} catch (IllegalStateException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.KO, errMsg);
		} catch (EntityUpdateException e) {
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
	 * Delete an appointment by id
	 * @param id
	 * @return
	 */
	@DELETE
	@Path(BaseWsPath.PATH_ID)
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response delete(@PathParam(BaseWsPath.ID) Long id) {
		try {
			LOG.debug("Appointment [" + (id != null ? id : NULL) + "]");
			
			Appointment appointment = ENTITY_SRV.getById(Appointment.class, id);
			if (appointment == null) {
				String errMsg = "Appointment [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			
			ENTITY_SRV.delete(appointment);
			
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
	 * @param appointment
	 * @return
	 */
	private AppointmentDTO toAppointmentDTO(Appointment appointment) {
		AppointmentDTO appointmentDTO = new AppointmentDTO();
		appointmentDTO.setId(appointment.getId());
		appointmentDTO.setCreationDate(appointment.getCreateDate());
		appointmentDTO.setContractNo(appointment.getContract() != null ? appointment.getContract().getReference() : StringUtils.EMPTY);
		appointmentDTO.setApplicantType(appointment.getBetween1() != null ? toRefDataDTO(appointment.getBetween1()) : null);
		appointmentDTO.setLocation(appointment.getLocation() != null ? toRefDataDTO(appointment.getLocation()) : null);
		appointmentDTO.setRemark(appointment.getRemark());
		appointmentDTO.setStartDate(appointment.getStartDate());
		appointmentDTO.setBranch(appointment.getBetween2() != null ? getBranchesDTO(appointment.getBetween2().getId()) : null);
		return appointmentDTO;
	}

	/**
	 * @param appointments
	 * @return
	 */
	private List<AppointmentDTO> toAppointmentDTOs(List<Appointment> appointments) {
		List<AppointmentDTO> appointmentDTOs = new ArrayList<AppointmentDTO>();
		for (Appointment appointment : appointments) {
			appointmentDTOs.add(toAppointmentDTO(appointment));
		}
		return appointmentDTOs;
	}
	
	/**
	 * @param appointmentDTO
	 * @return
	 */
	private Appointment toAppointment(AppointmentDTO appointmentDTO) {
		Appointment appointment = null;
		if (appointmentDTO.getId() != null) {
			appointment = ENTITY_SRV.getById(Appointment.class, appointmentDTO.getId());
			if (appointment == null) {
				messages.add(FinWsMessage.APPOINTMENT_NOT_FOUND);
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
		} else {
			 appointment = Appointment.createInstance();
		}
		
		Contract contract = null;
		if (StringUtils.isEmpty(appointmentDTO.getContractNo())) {
			messages.add(FinWsMessage.CONTRACT_MANDATORY);
		} else {
			contract = CONT_SRV.getByReference(appointmentDTO.getContractNo());
			if (contract == null) {
				messages.add(FinWsMessage.CONTRACT_NOT_FOUND);
			}
		}
		appointment.setContract(contract);
		
		if (appointmentDTO.getApplicantType() != null) {
			appointment.setBetween1(EApplicantType.getById(appointmentDTO.getApplicantType().getId()));
			if (appointment.getBetween1() == null) {
				appointment.setBetween1(EApplicantType.getById(1l));
			}
		} else {
			messages.add(FinWsMessage.APPLICANT_TYPE_MANDATORY);
		}
		
		if (appointmentDTO.getLocation() != null) {
			appointment.setLocation(ELocation.getById(appointmentDTO.getLocation().getId()));
			if (appointment.getLocation() == null) {
				messages.add(FinWsMessage.LOCATION_NOT_FOUND);
			}
		} else {
			messages.add(FinWsMessage.LOCATION_MANDATORY);
		}
		
		if (appointmentDTO.getBranch() != null) {
			appointment.setBetween2(CONT_SRV.getById(OrgStructure.class, appointmentDTO.getBranch().getId()));
		}
		
		appointment.setRemark(appointmentDTO.getRemark());
		appointment.setStartDate(appointmentDTO.getStartDate());
		return appointment;
	}

}
