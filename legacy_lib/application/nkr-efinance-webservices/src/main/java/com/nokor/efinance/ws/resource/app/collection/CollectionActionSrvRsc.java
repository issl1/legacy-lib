package com.nokor.efinance.ws.resource.app.collection;

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
import com.nokor.efinance.core.collection.model.CollectionAction;
import com.nokor.efinance.core.collection.model.EColAction;
import com.nokor.efinance.core.collection.service.CollectionActionRestriction;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.share.collection.CollectionActionDTO;
import com.nokor.efinance.share.common.FinWsMessage;
import com.nokor.efinance.ws.resource.app.contract.BaseContractSrvRsc;
import com.nokor.efinance.ws.resource.app.contract.ContractWsPath;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * 
 * @author uhout.cheng
 */
@Path(ContractWsPath.CONTRACTS + ContractWsPath.COLACTIONS)
public class CollectionActionSrvRsc extends BaseContractSrvRsc {
	
	/**
	 * List all collection actions by contract
	 * @param contractNo
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list(@QueryParam(ContractWsPath.REFERENCE) String contractNo) {
		try {
			LOG.debug("Contract - Reference. [" + contractNo + "]");
			CollectionActionRestriction restrictions = new CollectionActionRestriction();
			
			if (StringUtils.isNotEmpty(contractNo)) {
				Contract contra = CONT_SRV.getByReference(contractNo);
				if (contra != null) {
					restrictions.setContractId(contra.getId());
				} else {
					String errMsg = "Contract [" + contractNo + "]";
					throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));	
				}
			}
			
			List<CollectionAction> actions = ENTITY_SRV.list(restrictions);
			List<CollectionActionDTO> actionDTOs= toCollectionActionDTOs(actions);
			
			return ResponseHelper.ok(actionDTOs);
		} catch (Exception e) {
			String errMsg = "Error while searching contract collection action history [" + e.getMessage() + "]";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_LIST_KO, errMsg);
		}
	}
	
	/**
	 * List collection actions by id
	 * @param id
	 * @return
	 */
	@GET
	@Path(BaseWsPath.PATH_ID)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(@PathParam(BaseWsPath.ID) Long id) {
		try {
			LOG.debug("CollectionAction - id. [" + id + "]");
			
			CollectionAction action = ENTITY_SRV.getById(CollectionAction.class, id);
			if (action == null) {
				String errMsg = "Contract-CollectionAction [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));			
			}
			CollectionActionDTO actionDTO = toCollectionActionDTO(action);
			
			return ResponseHelper.ok(actionDTO);
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
	 * Create a collection action
	 * @param actionDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response create(CollectionActionDTO actionDTO) {
		try {
			if (actionDTO == null) {
				throw new EntityNotValidParameterException(I18N.messageMandatoryField("CollectionActionDTO"));
			}
			CollectionAction action = toCollectionAction(actionDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new EntityNotValidParameterException(I18N.messageObjectNotFound(errMsg));
			}
			COL_SRV.saveOrUpdateLatestColAction(action);
			
			return ResponseHelper.ok(toCollectionActionDTO(action));
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
	 * Update a collection action by id
	 * @param id
	 * @param actionDTO
	 * @return
	 */
	@PUT
	@Path(BaseWsPath.PATH_ID)
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response update(@PathParam("id") Long id, CollectionActionDTO actionDTO) {
		try {
			LOG.debug("CollectionAction - id. [" + id + "]");
			actionDTO.setId(id);
			CollectionAction action = toCollectionAction(actionDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			} 
			
			COL_SRV.saveOrUpdateLatestColAction(action);
			
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
	 * Delete a collection action by id
	 * @param id
	 * @return
	 */
	@DELETE
	@Path(BaseWsPath.PATH_ID)
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response delete(@PathParam(BaseWsPath.ID) Long id) {
		try {
			LOG.debug("CollectionAction [" + (id != null ? id : NULL) + "]");
			
			CollectionAction action = ENTITY_SRV.getById(CollectionAction.class, id);
			if (action == null) {
				String errMsg = "CollectionAction [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			
			COL_SRV.deleteLatestColAction(action);
			
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
	 * @param action
	 * @return
	 */
	private CollectionActionDTO toCollectionActionDTO(CollectionAction action) {
		CollectionActionDTO actionDTO = new CollectionActionDTO();
		actionDTO.setId(action.getId());
		actionDTO.setCreationUser(action.getCreateUser());
		actionDTO.setCreationDate(action.getCreateDate());
		actionDTO.setContractNo(action.getContract() != null ? action.getContract().getReference() : StringUtils.EMPTY);
		actionDTO.setColAction(action.getColAction() != null ? toRefDataDTO(action.getColAction()) : null);
		actionDTO.setNextActionDate(action.getNextActionDate());
		actionDTO.setComment(action.getComment());
		actionDTO.setUserLogin(action.getUserLogin());
		return actionDTO;
	}
	
	/**
	 * @param actions
	 * @return
	 */
	private List<CollectionActionDTO> toCollectionActionDTOs(List<CollectionAction> actions) {
		List<CollectionActionDTO> actionDTOs = new ArrayList<CollectionActionDTO>();
		for (CollectionAction action : actions) {
			actionDTOs.add(toCollectionActionDTO(action));
		}
		return actionDTOs;
	}
	
	/**
	 * @param actionDTO
	 * @return
	 */
	private CollectionAction toCollectionAction(CollectionActionDTO actionDTO) {
		CollectionAction action = null;
		if (actionDTO.getId() != null) {
			action = ENTITY_SRV.getById(CollectionAction.class, actionDTO.getId());
			if (action == null) {
				messages.add(FinWsMessage.COL_ACTION_NOT_FOUND);
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
		} else {
			 action = CollectionAction.createInstance();
		}
		
		Contract contract = null;
		if (StringUtils.isEmpty(actionDTO.getContractNo())) {
			messages.add(FinWsMessage.CONTRACT_MANDATORY);
		} else {
			contract = CONT_SRV.getByReference(actionDTO.getContractNo());
			if (contract == null) {
				messages.add(FinWsMessage.CONTRACT_NOT_FOUND);
			}
		}
		action.setContract(contract);
		
		if (actionDTO.getColAction() != null) {
			action.setColAction(EColAction.getById(actionDTO.getColAction().getId()));
		} else {
			action.setColAction(EColAction.getById(6l));
		}
		
		action.setNextActionDate(actionDTO.getNextActionDate());
		action.setUserLogin(actionDTO.getUserLogin());
		action.setComment(actionDTO.getComment());
		return action;
	}

}
