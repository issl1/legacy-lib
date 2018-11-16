package com.nokor.efinance.ws.resource.app.callcenter;

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

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.common.messaging.ws.resource.BaseWsPath;
import com.nokor.efinance.core.callcenter.CallCenterHistoryRestriction;
import com.nokor.efinance.core.callcenter.model.CallCenterHistory;
import com.nokor.efinance.core.callcenter.model.ECallCenterResult;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.share.common.FinWsMessage;
import com.nokor.efinance.share.contract.CallCenterHistoryDTO;
import com.nokor.efinance.ws.resource.app.contract.BaseContractSrvRsc;
import com.nokor.efinance.ws.resource.app.contract.ContractWsPath;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * 
 * @author uhout.cheng
 */
@Path(ContractWsPath.CONTRACTS + ContractWsPath.CALLHISTORIES)
public class CallCenterHistorySrvRsc extends BaseContractSrvRsc {
	
	/**
	 * List all call center histories by contract
	 * @param contractNo
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list(@QueryParam(ContractWsPath.REFERENCE) String contractNo) {
		try {
			LOG.debug("Contract - Ref. [" + contractNo + "]");
			CallCenterHistoryRestriction restrictions = new CallCenterHistoryRestriction();
			
			if (StringUtils.isNotEmpty(contractNo)) {
				Contract contra = CONT_SRV.getByReference(contractNo);
				if (contra != null) {
					restrictions.setContractId(contra.getId());
				} else {
					String errMsg = "Contract [" + contractNo + "]";
					throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));	
				}
			}
			
			List<CallCenterHistory> histories = ENTITY_SRV.list(restrictions);
			List<CallCenterHistoryDTO> historyDTOs= toCallCenterHistoryDTOs(histories);
			
			return ResponseHelper.ok(historyDTOs);
		} catch (Exception e) {
			String errMsg = "Error while searching contract call center history [" + e.getMessage() + "]";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_LIST_KO, errMsg);
		}
	}
	
	/**
	 * List call center history by id
	 * @param id
	 * @return
	 */
	@GET
	@Path(BaseWsPath.PATH_ID)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(@PathParam(BaseWsPath.ID) Long id) {
		try {
			LOG.debug("CallCenterHistory - id. [" + id + "]");
			
			CallCenterHistory history = ENTITY_SRV.getById(CallCenterHistory.class, id);
			if (history == null) {
				String errMsg = "Contract-CallCenterHistory [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));			
			}
			CallCenterHistoryDTO historyDTO = toCallCenterHistoryDTO(history);
			
			return ResponseHelper.ok(historyDTO);
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
	 * Create a call center history
	 * @param historyDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response create(CallCenterHistoryDTO historyDTO) {
		try {
			if (historyDTO == null) {
				throw new EntityNotValidParameterException(I18N.messageMandatoryField("CallCenterHistoryDTO"));
			}
			CallCenterHistory history = toCallCenterHistory(historyDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new EntityNotValidParameterException(I18N.messageObjectNotFound(errMsg));
			}
			CALL_CTR_SRV.saveOrUpdateCallCenterHistory(UserSessionManager.getCurrentUser(), history);
			
			return ResponseHelper.ok(toCallCenterHistoryDTO(history));
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
	 * Update a call center history by id
	 * @param id
	 * @param historyDTO
	 * @return
	 */
	@PUT
	@Path(BaseWsPath.PATH_ID)
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response update(@PathParam("id") Long id, CallCenterHistoryDTO historyDTO) {
		try {
			LOG.debug("CallCenterHistory - id. [" + id + "]");
			historyDTO.setId(id);
			CallCenterHistory history = toCallCenterHistory(historyDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			} 
			
			CALL_CTR_SRV.saveOrUpdateCallCenterHistory(UserSessionManager.getCurrentUser(), history);
			
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
	 * Delete a call center history by id
	 * @param id
	 * @return
	 */
	@DELETE
	@Path(BaseWsPath.PATH_ID)
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response delete(@PathParam(BaseWsPath.ID) Long id) {
		try {
			LOG.debug("CallCenterHistory [" + (id != null ? id : NULL) + "]");
			
			CallCenterHistory history = ENTITY_SRV.getById(CallCenterHistory.class, id);
			if (history == null) {
				String errMsg = "CallCenterHistory [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			
			ENTITY_SRV.delete(history);
			
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
	 * @param history
	 * @return
	 */
	private CallCenterHistoryDTO toCallCenterHistoryDTO(CallCenterHistory history) {
		CallCenterHistoryDTO historyDTO = new CallCenterHistoryDTO();
		historyDTO.setId(history.getId());
		historyDTO.setContractNo(history.getContract() != null ? history.getContract().getReference() : StringUtils.EMPTY);
		historyDTO.setResult(history.getResult() != null ? toCallCenterResultDTO(history.getResult()) : null);
		historyDTO.setComment(history.getComment());
		return historyDTO;
	}
	
	/**
	 * @param histories
	 * @return
	 */
	private List<CallCenterHistoryDTO> toCallCenterHistoryDTOs(List<CallCenterHistory> histories) {
		List<CallCenterHistoryDTO> historyDTOs = new ArrayList<CallCenterHistoryDTO>();
		for (CallCenterHistory history : histories) {
			historyDTOs.add(toCallCenterHistoryDTO(history));
		}
		return historyDTOs;
	}
	
	/**
	 * @param historyDTO
	 * @return
	 */
	private CallCenterHistory toCallCenterHistory(CallCenterHistoryDTO historyDTO) {
		CallCenterHistory history = null;
		if (historyDTO.getId() != null) {
			history = ENTITY_SRV.getById(CallCenterHistory.class, historyDTO.getId());
			if (history == null) {
				messages.add(FinWsMessage.CALL_CENTER_HISTORY_NOT_FOUND);
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
		} else {
			 history = CallCenterHistory.createInstance();
		}
		
		Contract contract = null;
		if (StringUtils.isEmpty(historyDTO.getContractNo())) {
			messages.add(FinWsMessage.CONTRACT_MANDATORY);
		} else {
			contract = CONT_SRV.getByReference(historyDTO.getContractNo());
			if (contract == null) {
				messages.add(FinWsMessage.CONTRACT_NOT_FOUND);
			}
		}
		history.setContract(contract);
		
		ECallCenterResult other = ENTITY_SRV.getByCode(ECallCenterResult.class, ECallCenterResult.OTHER);
		if (historyDTO.getResult() != null) {
			history.setResult(ENTITY_SRV.getById(ECallCenterResult.class, historyDTO.getResult().getId()));
		} else {
			if (other != null) {
				history.setResult(other);
			}
		}
		
		history.setComment(historyDTO.getComment());
		return history;
	}

}
