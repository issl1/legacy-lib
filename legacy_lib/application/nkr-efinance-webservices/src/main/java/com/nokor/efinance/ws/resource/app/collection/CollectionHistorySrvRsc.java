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

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.exception.EntityAlreadyExistsException;
import org.seuksa.frmk.tools.exception.EntityCreationException;
import org.seuksa.frmk.tools.exception.EntityNotValidParameterException;
import org.seuksa.frmk.tools.exception.EntityUpdateException;
import org.springframework.dao.DataIntegrityViolationException;

import com.nokor.common.messaging.ws.resource.BaseWsPath;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.collection.model.CollectionHistory;
import com.nokor.efinance.core.collection.model.ECallType;
import com.nokor.efinance.core.collection.model.EColResult;
import com.nokor.efinance.core.collection.model.EColSubject;
import com.nokor.efinance.core.collection.model.EContactPerson;
import com.nokor.efinance.core.collection.service.CollectionHistoryRestriction;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.share.collection.CollectionHistoryDTO;
import com.nokor.efinance.share.common.FinWsMessage;
import com.nokor.efinance.ws.resource.app.contract.BaseContractSrvRsc;
import com.nokor.efinance.ws.resource.app.contract.ContractWsPath;
import com.nokor.efinance.ws.resource.config.ConfigWsPath;
import com.nokor.ersys.core.hr.model.eref.ETypeContactInfo;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * 
 * @author uhout.cheng
 */
@Path(ContractWsPath.CONTRACTS + ContractWsPath.COLHISTORIES)
public class CollectionHistorySrvRsc extends BaseContractSrvRsc {
	
	/**
	 * List all collection actions by contract
	 * @param contractNo
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list(@QueryParam(ContractWsPath.REFERENCE) String contractNo, @QueryParam(ConfigWsPath.TYPE) Long type) {
		try {
			LOG.debug("Contract - Reference. [" + contractNo + "]");
			LOG.debug("ECallType - TypeID. [" + type + "]");
			
			CollectionHistoryRestriction restrictions = new CollectionHistoryRestriction();
			
			if (StringUtils.isNotEmpty(contractNo)) {
				Contract contra = CONT_SRV.getByReference(contractNo);
				if (contra != null) {
					restrictions.setContractId(contra.getId());
				} else {
					String errMsg = "Contract [" + contractNo + "]";
					throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));	
				}
			}
			ECallType callType = null;
			if (type != null) {
				callType = ECallType.getById(type);
				if (callType == null) {
					String errMsg = "Type [" + type + "]";
					throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));	
				} else {
					restrictions.setCallTypes(new ECallType[] { callType });
				}
			}
			
			List<CollectionHistory> histories = ENTITY_SRV.list(restrictions);
			List<CollectionHistoryDTO> historyDTOs= toCollectionHistoryDTOs(histories);
			
			return ResponseHelper.ok(historyDTOs);
		} catch (Exception e) {
			String errMsg = "Error while searching contract collection history [" + e.getMessage() + "]";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_LIST_KO, errMsg);
		}
	}
	
	/**
	 * List collection histories by id
	 * @param id
	 * @return
	 */
	@GET
	@Path(BaseWsPath.PATH_ID)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(@PathParam(BaseWsPath.ID) Long id) {
		try {
			LOG.debug("CollectionHistory - id. [" + id + "]");
			
			CollectionHistory history = ENTITY_SRV.getById(CollectionHistory.class, id);
			if (history == null) {
				String errMsg = "Contract-CollectionHistory [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));			
			}
			CollectionHistoryDTO historyDTO = toCollectionHistoryDTO(history);
			
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
	 * Create a collection history
	 * @param historyDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response create(CollectionHistoryDTO historyDTO) {
		try {
			if (historyDTO == null) {
				throw new EntityNotValidParameterException(I18N.messageMandatoryField("CollectionHistory"));
			}
			CollectionHistory history = toCollectionHistory(historyDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new EntityNotValidParameterException(I18N.messageObjectNotFound(errMsg));
			}
			COL_SRV.saveOrUpdateLatestColHistory(history);
			
			return ResponseHelper.ok(toCollectionHistoryDTO(history));
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
	 * Update a collection history by id
	 * @param id
	 * @param historyDTO
	 * @return
	 */
	@PUT
	@Path(BaseWsPath.PATH_ID)
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response update(@PathParam("id") Long id, CollectionHistoryDTO historyDTO) {
		try {
			LOG.debug("CollectionHistory - id. [" + id + "]");
			historyDTO.setId(id);
			CollectionHistory history = toCollectionHistory(historyDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			} 
			
			COL_SRV.saveOrUpdateLatestColHistory(history);
			
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
	 * Delete a collection history by id
	 * @param id
	 * @return
	 */
	@DELETE
	@Path(BaseWsPath.PATH_ID)
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response delete(@PathParam(BaseWsPath.ID) Long id) {
		try {
			LOG.debug("CollectionHistory [" + (id != null ? id : NULL) + "]");
			
			CollectionHistory history = ENTITY_SRV.getById(CollectionHistory.class, id);
			if (history == null) {
				String errMsg = "CollectionHistory [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			
			COL_SRV.deleteLatestColHistory(history);
			
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
	private CollectionHistoryDTO toCollectionHistoryDTO(CollectionHistory history) {
		CollectionHistoryDTO historyDTO = new CollectionHistoryDTO();
		historyDTO.setId(history.getId());
		historyDTO.setCreationUser(history.getCreateUser());
		historyDTO.setCreationDate(history.getCreateDate());
		historyDTO.setContractNo(history.getContract() != null ? history.getContract().getReference() : StringUtils.EMPTY);
		historyDTO.setCallType(history.getCallType() != null ? toRefDataDTO(history.getCallType()) : null);
		historyDTO.setApplicantType(history.getContactedPerson() != null ? toRefDataDTO(history.getContactedPerson()) : null);
		historyDTO.setContactInfoType(history.getContactedTypeInfo() != null ? toRefDataDTO(history.getContactedTypeInfo()) : null);
		historyDTO.setContactPerson(history.getReachedPerson() != null ? toRefDataDTO(history.getReachedPerson()) : null);
		historyDTO.setCallIn(history.isCallIn());
		
		if (history.getResult() != null) {
			historyDTO.setColResult(history.getResult() != null ? getColResultDTO(history.getResult().getId()) : null);
		}
		
		if (history.getSubject() != null) {
			historyDTO.setColSubject(history.getSubject() != null ? getColSubjectDTO(history.getSubject().getId()) : null);
		}
		
		historyDTO.setComment(history.getComment());
		historyDTO.setOtherValue(history.getOther());
		historyDTO.setContactInfoValue(history.getContactedInfoValue());
		if (history.getCallType() != null && ECallType.FIELD.equals(history.getCallType())) {
			if (history.getAddress() != null) {
				historyDTO.setAddress(toAddressDTO(history.getAddress()));
			}
		}
		return historyDTO;
	}
	
	/**
	 * @param histories
	 * @return
	 */
	private List<CollectionHistoryDTO> toCollectionHistoryDTOs(List<CollectionHistory> histories) {
		List<CollectionHistoryDTO> historyDTOs = new ArrayList<CollectionHistoryDTO>();
		for (CollectionHistory history : histories) {
			historyDTOs.add(toCollectionHistoryDTO(history));
		}
		return historyDTOs;
	}
	
	/**
	 * @param historyDTO
	 * @return
	 */
	private CollectionHistory toCollectionHistory(CollectionHistoryDTO historyDTO) {
		CollectionHistory history = null;
		if (historyDTO.getId() != null) {
			history = ENTITY_SRV.getById(CollectionHistory.class, historyDTO.getId());
			if (history == null) {
				messages.add(FinWsMessage.COL_HISTORY_NOT_FOUND);
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
		} else {
			 history = CollectionHistory.createInstance();
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
		
		if (historyDTO.getCallType() != null) {
			history.setCallType(ECallType.getById(historyDTO.getCallType().getId()));
			if (history.getCallType() == null) {
				messages.add(FinWsMessage.CALL_TYPE_NOT_FOUND);
			} else {
				if (ECallType.FIELD.equals(history.getCallType())) {
					history.setAddress(toAddress(false, historyDTO.getAddress()));
				}
			}
		} else {
			messages.add(FinWsMessage.CALL_TYPE_MANDATORY);
		}
		
		if (historyDTO.getApplicantType() != null) {
			history.setContactedPerson(EApplicantType.getById(historyDTO.getApplicantType().getId()));
			if (history.getContactedPerson() == null) {
				history.setContactedPerson(EApplicantType.getById(1l));
			}
		} else {
			history.setContactedPerson(EApplicantType.getById(1l));
		}
		
		if (historyDTO.getContactInfoType() != null) {
			history.setContactedTypeInfo(ETypeContactInfo.getById(historyDTO.getContactInfoType().getId()));
			if (history.getContactedTypeInfo() == null) {
				history.setContactedTypeInfo(ETypeContactInfo.getById(1l));
			}
		} else {
			history.setContactedTypeInfo(ETypeContactInfo.getById(1l));
		}
		
		if (historyDTO.getContactPerson() != null) {
			history.setReachedPerson(EContactPerson.getById(historyDTO.getContactPerson().getId()));
			if (history.getReachedPerson() == null) {
				history.setReachedPerson(EContactPerson.getById(1l));
			}
		} else {
			history.setReachedPerson(EContactPerson.getById(1l));
		}
		
		if (historyDTO.getColResult() != null) {
			history.setResult(ENTITY_SRV.getById(EColResult.class, historyDTO.getColResult().getId()));
		}
		
		if (historyDTO.getColSubject() != null) {
			history.setSubject(ENTITY_SRV.getById(EColSubject.class, historyDTO.getColSubject().getId()));
		}
		
		history.setCallIn(historyDTO.isCallIn());
		history.setOther(historyDTO.getOtherValue());
		history.setComment(historyDTO.getComment());
		history.setContactedInfoValue(historyDTO.getContactInfoValue());
		return history;
	}

}
