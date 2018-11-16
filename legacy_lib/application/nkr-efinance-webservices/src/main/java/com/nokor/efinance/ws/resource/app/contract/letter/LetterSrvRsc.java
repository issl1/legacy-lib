package com.nokor.efinance.ws.resource.app.contract.letter;

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

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Order;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.exception.EntityAlreadyExistsException;
import org.seuksa.frmk.tools.exception.EntityCreationException;
import org.seuksa.frmk.tools.exception.EntityNotValidParameterException;
import org.seuksa.frmk.tools.exception.EntityUpdateException;
import org.springframework.dao.DataIntegrityViolationException;

import com.nokor.common.app.workflow.model.WkfHistoryItem;
import com.nokor.common.app.workflow.service.WkfHistoryItemRestriction;
import com.nokor.common.messaging.ws.resource.BaseWsPath;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.common.reference.model.ELetterTemplate;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.Letter;
import com.nokor.efinance.core.contract.service.LetterRestriction;
import com.nokor.efinance.share.common.FinWsMessage;
import com.nokor.efinance.share.contract.LetterDTO;
import com.nokor.efinance.ws.resource.app.contract.BaseContractSrvRsc;
import com.nokor.efinance.ws.resource.app.contract.ContractWsPath;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * 
 * @author uhout.cheng
 */
@Path(ContractWsPath.CONTRACTS + ContractWsPath.LETTERS)
public class LetterSrvRsc extends BaseContractSrvRsc {
	
	/**
	 * List all letters by contract
	 * @param contractNo
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list(@QueryParam(ContractWsPath.REFERENCE) String contractNo) {
		try {
			LetterRestriction restrictions = new LetterRestriction();
			restrictions.setContractNo(contractNo);
			
			List<Letter> letters = ENTITY_SRV.list(restrictions);
			List<LetterDTO> letterDTOs= toLetterDTOs(letters);
			
			return ResponseHelper.ok(letterDTOs);
		} catch (Exception e) {
			String errMsg = "Error while searching letters [" + e.getMessage() + "]";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_LIST_KO, errMsg);
		}
	}
	
	/**
	 * List letter by id
	 * @param id
	 * @return
	 */
	@GET
	@Path(BaseWsPath.PATH_ID)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(@PathParam(BaseWsPath.ID) Long id) {
		try {
			LOG.debug("Letter - id. [" + id + "]");
			
			Letter letter = ENTITY_SRV.getById(Letter.class, id);
			if (letter == null) {
				String errMsg = "Letter [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));			
			}
			LetterDTO letterDTO = toLetterDTO(letter);
			
			return ResponseHelper.ok(letterDTO);
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
	 * Create a letter
	 * @param letterDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response create(LetterDTO letterDTO) {
		try {
			if (letterDTO == null) {
				throw new EntityNotValidParameterException(I18N.messageMandatoryField("LetterDTO"));
			}
			Letter letter = toLetter(letterDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new EntityNotValidParameterException(I18N.messageObjectNotFound(errMsg));
			}
			LETTER_SRV.saveOrUpdateLetter(letter);
			
			return ResponseHelper.ok(toLetterDTO(letter));
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
	 * Update a letter by id
	 * @param id
	 * @param letterDTO
	 * @return
	 */
	@PUT
	@Path(BaseWsPath.PATH_ID)
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response update(@PathParam("id") Long id, LetterDTO letterDTO) {
		try {
			LOG.debug("Letter - id. [" + id + "]");
			letterDTO.setId(id);
			Letter letter = toLetter(letterDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			} 
			
			LETTER_SRV.saveOrUpdateLetter(letter);
			
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
	 * Delete a letter by id
	 * @param id
	 * @return
	 */
	@DELETE
	@Path(BaseWsPath.PATH_ID)
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response delete(@PathParam(BaseWsPath.ID) Long id) {
		try {
			LOG.debug("Letter [" + (id != null ? id : NULL) + "]");
			
			Letter letter = ENTITY_SRV.getById(Letter.class, id);
			if (letter == null) {
				String errMsg = "Letter [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			
			ENTITY_SRV.delete(letter);
			
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
	 * @param letter
	 * @return
	 */
	private LetterDTO toLetterDTO(Letter letter) {
		LetterDTO letterDTO = new LetterDTO();
		letterDTO.setId(letter.getId());
		letterDTO.setContractNo(letter.getContract() != null ? letter.getContract().getReference() : StringUtils.EMPTY);
		letterDTO.setApplicantType(letter.getSendTo() != null ? toRefDataDTO(letter.getSendTo()) : null);
		letterDTO.setLetterTemplated((letter.getLetterTemplate() != null ? getLetterTemplatesDTO(letter.getLetterTemplate().getId()) : null));
		letterDTO.setAddress(letter.getSendAddress() != null ? toAddressDTO(letter.getSendAddress()) : null);
		letterDTO.setUserLogin(letter.getUserLogin());
		letterDTO.setMessage(letter.getMessage());
		letterDTO.setStatus(letter.getWkfStatus().getDescLocale());
		
		WkfHistoryItemRestriction<WkfHistoryItem> restrictions = new WkfHistoryItemRestriction<>(WkfHistoryItem.class);
		restrictions.setEntity(letter.getWkfFlow().getEntity());
		restrictions.setEntityId(letter.getId());
		restrictions.addOrder(Order.desc(WkfHistoryItem.ID));
		List<WkfHistoryItem> histories = ENTITY_SRV.list(restrictions);
		if (histories != null && !histories.isEmpty()) {
			letterDTO.setStatusDate(histories.get(0).getChangeDate());
		}
		
		letterDTO.setSendDate(letter.getSendDate());
		return letterDTO;
	}
	
	/**
	 * @param letters
	 * @return
	 */
	private List<LetterDTO> toLetterDTOs(List<Letter> letters) {
		List<LetterDTO> letterDTOs = new ArrayList<LetterDTO>();
		for (Letter letter : letters) {
			letterDTOs.add(toLetterDTO(letter));
		}
		return letterDTOs;
	}
	
	/**
	 * @param letterDTO
	 * @return
	 */
	private Letter toLetter(LetterDTO letterDTO) {
		Letter letter = null;
		if (letterDTO.getId() != null) {
			letter = ENTITY_SRV.getById(Letter.class, letterDTO.getId());
			if (letter == null) {
				messages.add(FinWsMessage.LETTER_NOT_FOUND);
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
		} else {
			 letter = Letter.createInstance();
		}
		
		Contract contract = null;
		if (StringUtils.isEmpty(letterDTO.getContractNo())) {
			messages.add(FinWsMessage.CONTRACT_MANDATORY);
		} else {
			contract = CONT_SRV.getByReference(letterDTO.getContractNo());
			if (contract == null) {
				messages.add(FinWsMessage.CONTRACT_NOT_FOUND);
			}
		}
		letter.setContract(contract);
		
		if (letterDTO.getApplicantType() != null) {
			letter.setSendTo(EApplicantType.getById(letterDTO.getApplicantType().getId()));
		} 
		
		if (letterDTO.getAddress() != null) {
			letter.setSendAddress(toAddress(false, letterDTO.getAddress()));
		} else {
			messages.add(FinWsMessage.ADDRESS_MANDATORY);
		}
		
		ELetterTemplate letterTemplate = null;
		if (letterDTO.getLetterTemplated() != null) {
			letterTemplate = ENTITY_SRV.getById(ELetterTemplate.class, letterDTO.getLetterTemplated().getId());
		}
		letter.setLetterTemplate(letterTemplate);
		
		letter.setMessage(letterDTO.getMessage());
		letter.setSendDate(letterDTO.getSendDate());
		
		letter.setUserLogin(letterDTO.getUserLogin());
		
		return letter;
	}

}
