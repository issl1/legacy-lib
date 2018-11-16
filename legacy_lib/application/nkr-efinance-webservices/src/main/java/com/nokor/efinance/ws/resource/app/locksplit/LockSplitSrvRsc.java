package com.nokor.efinance.ws.resource.app.locksplit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.tools.exception.EntityAlreadyExistsException;
import org.seuksa.frmk.tools.exception.EntityCreationException;
import org.seuksa.frmk.tools.exception.EntityNotValidParameterException;
import org.springframework.dao.DataIntegrityViolationException;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.aftersale.EAfterSaleEventType;
import com.nokor.efinance.core.collection.model.ELockSplitCategory;
import com.nokor.efinance.core.collection.model.EPaymentChannel;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.LockSplit;
import com.nokor.efinance.core.contract.model.LockSplitItem;
import com.nokor.efinance.core.contract.model.LockSplitRecapVO;
import com.nokor.efinance.core.contract.model.LockSplitRestriction;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.workflow.LockSplitWkfStatus;
import com.nokor.efinance.share.common.FinWsMessage;
import com.nokor.efinance.share.locksplit.LockSplitCriteriaDTO;
import com.nokor.efinance.share.locksplit.LockSplitDTO;
import com.nokor.efinance.share.locksplit.LockSplitEventType;
import com.nokor.efinance.share.locksplit.LockSplitItemDTO;
import com.nokor.efinance.share.locksplit.LockSplitRecapDTO;
import com.nokor.efinance.share.locksplit.LockSplitStatus;
import com.nokor.efinance.ws.FinResourceSrvRsc;
import com.nokor.ersys.finance.accounting.model.JournalEvent;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * Lock split web service
 * @author uhout.cheng
 */
@Path("/contracts/locksplits")
public class LockSplitSrvRsc extends FinResourceSrvRsc implements FinServicesHelper {
	
	/**
	 * List all lock splits
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list() {		
		try {				
			List<LockSplit> lockSplits = LCK_SPL_SRV.list(LockSplit.class);
			List<LockSplitDTO> lockSplitDTOs = new ArrayList<>();
			for (LockSplit lockSplit : lockSplits) {
				lockSplitDTOs.add(toLockSplitDTO(lockSplit));
			}
			return ResponseHelper.ok(lockSplitDTOs);
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
	 * Get lock split by id
	 * @param id
	 * @return
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(@PathParam("id") Long id) {
		try {
			LOG.debug("LockSplit - id. [" + id + "]");
		
			LockSplit lockSplit = ENTITY_SRV.getById(LockSplit.class, id);
			
			if (lockSplit == null) {
				String errMsg = "LockSplit [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			
			LockSplitDTO lockSplitDTO = toLockSplitDTO(lockSplit);
			
			return ResponseHelper.ok(lockSplitDTO);
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
	 * Get lock split items by category id
	 * @param lckId
	 * @param catId
	 * @return
	 */
	@GET
	@Path("/{lckId}/items/{catId}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response getLockSplitItemsByCategory(@PathParam("lckId") Long lckId, @PathParam("catId") Long catId) {
		try {
			LOG.debug("LockSplit - id. [" + lckId + "]");
			LOG.debug("LockSplit Category - id. [" + catId + "]");
		
			LockSplit lockSplit = ENTITY_SRV.getById(LockSplit.class, lckId);
			ELockSplitCategory category = ELockSplitCategory.getById(catId);
			
			if (lockSplit == null) {
				String errMsg = "LockSplit [" + lckId + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));			
			} else if (category == null) {
				String errMsg = "LockSplit-Category [" + catId + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));	
			} 
		
			Map<String, List<LockSplitItem>> map = LCK_SPL_SRV.getLockSplitItemsByCategory(lockSplit, category);
			
			List<LockSplitItemDTO> lockSplitItemsDTO = new ArrayList<>();
			if (map != null && !map.isEmpty()) {
				List<LockSplitItem> items = map.get(category.getCode());
				for (LockSplitItem item : items) {
					lockSplitItemsDTO.add(toLockSplitItemDTO(item));
				}
			}
			return ResponseHelper.ok(lockSplitItemsDTO);
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
	 * Create Lock split 
	 * @param lockSplitDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response create(LockSplitDTO lockSplitDTO) {
		try {
					
			LockSplit lockSplit = toLockSplit(lockSplitDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
	
			LCK_SPL_SRV.saveLockSplit(lockSplit);

			return ResponseHelper.ok(toLockSplitDTO(lockSplit));
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
	 * Search lock split
	 * @param lockSplitCriteriaDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/search")
	public Response search(LockSplitCriteriaDTO lockSplitCriteriaDTO) {
		try {
			LOG.debug("LockSplit [" + lockSplitCriteriaDTO + "]");
			
			LockSplitRestriction restrictions = toLockSplitRestriction(lockSplitCriteriaDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
			List<LockSplit> lockSplits = ENTITY_SRV.list(restrictions);
			
			return ResponseHelper.ok(toLockSplitsDTO(lockSplits));
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
	 * Update a lock split by id
	 * @param id
	 * @param lockSplitDTO
	 * @return
	 */
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response update(@PathParam("id") Long id, LockSplitDTO lockSplitDTO) {
		try {
			LockSplit oldLockSplit = null;
			LOG.debug("Lock Split - id. [" + id + "]");
			if (id != null) {
				oldLockSplit = LCK_SPL_SRV.getById(LockSplit.class, id);
				if (oldLockSplit == null) {
					String errMsg = messages.get(0).getDesc();
					throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
				}
			} 
			
			LockSplit newLockSplit = toLockSplit(lockSplitDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			} 
			
			LCK_SPL_SRV.updateLockSplit(oldLockSplit, newLockSplit);
			
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
	 * Delete a Lock split by id
	 * @param id
	 * @return
	 */
	// @DELETE
	// @Path("/{id}")
	// @Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	// @Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response delete(@PathParam("id") Long id) {
		try {
			LOG.debug("Lock Split - id. [" + id + "]");
			
			LockSplit lockSplit = LCK_SPL_SRV.getById(LockSplit.class, id);
			
			if (lockSplit == null) {
				String errMsg = "Lock Split [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			} else if (!LockSplitWkfStatus.LNEW.equals(lockSplit.getWkfStatus())) {
				throw new EntityNotValidParameterException("Could not delete Locksplit, the status is not NEW");
			}
			
			LCK_SPL_SRV.deleteLockSplit(lockSplit);
			
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
			String errMsg = "Lock Split id [" + id + "] can not be deleted. Because, It is used by system.";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.DELETE_KO, errMsg);
		} catch(Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.DELETE_KO, errMsg);
		}
	}
	
	/**
	 * Delete a Lock split item by id
	 * @param id
	 * @return
	 */
	// @DELETE
	// @Path("/{id}/items/{iteId}")
	// @Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	// @Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response deleteItem(@PathParam("id") Long id, @PathParam("iteId") Long iteId) {
		try {
			LOG.debug("Lock Split - id. [" + id + "]");
			LOG.debug("Lock Split Item - id. [" + iteId + "]");
			
			LockSplitItem lockSplitItem = LCK_SPL_SRV.getLockSplitItemByLockSplit(id, iteId);
			
			if (lockSplitItem == null) {
				String errMsg = "Lock Split Item ID [" + iteId + "] In LockSplit ID [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}  else if (!LockSplitWkfStatus.LNEW.equals(lockSplitItem.getWkfStatus())) {
				throw new EntityNotValidParameterException("Could not delete Locksplit item, the status is not NEW");
			}
			
			LCK_SPL_SRV.delete(lockSplitItem);
			
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
			String errMsg = "Lock Split Item id [" + id + "] can not be deleted. Because, It is used by system.";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.DELETE_KO, errMsg);
		} catch(Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.DELETE_KO, errMsg);
		}
	}
	
	/**
	 * Convert to LockSplit data transfer
	 * @param lockSplit
	 * @return
	 */
	private LockSplitDTO toLockSplitDTO(LockSplit lockSplit) {
		LockSplitDTO lockSplitDTO = new LockSplitDTO();
		lockSplitDTO.setId(lockSplit.getId());
		lockSplitDTO.setCreatedUser(lockSplit.getCreatedBy());
		lockSplitDTO.setCreatedDate(lockSplit.getCreateDate());
		lockSplitDTO.setLockSplitNo(lockSplit.getReference());
		lockSplitDTO.setContractID(lockSplit.getContract().getReference());
		lockSplitDTO.setFrom(lockSplit.getFrom());
		lockSplitDTO.setTo(lockSplit.getTo());
		lockSplitDTO.setTotalAmount(lockSplit.getTotalAmount());
		lockSplitDTO.setTotalVatAmount(lockSplit.getTotalVatAmount());
		lockSplitDTO.setSendEmail(lockSplit.isSendEmail());
		lockSplitDTO.setSendSms(lockSplit.isSendSms());
		lockSplitDTO.setPromise(lockSplit.isPromise());
		lockSplitDTO.setComment(lockSplit.getComment());
		lockSplitDTO.setPaymentChannel(toRefDataDTO(lockSplit.getPaymentChannel()));
		if (lockSplit.getAfterSaleEventType() != null) {
			lockSplitDTO.setEventType(LockSplitEventType.valueOf(lockSplit.getAfterSaleEventType().getCode()));
		}
		if (lockSplit.getDealer() != null) {
			lockSplitDTO.setDealer(toDealerDTO(lockSplit.getDealer()));
		}
		if (lockSplit.getWkfStatus() != null) {
			String code = lockSplit.getWkfStatus().getCode();
			lockSplitDTO.setStatus(LockSplitStatus.getByCode(code));
		}
		
		List<String> callBackUrls = new ArrayList<String>();
		String callBackUrl = lockSplit.getCallBackUrl();
		if (StringUtils.isNotEmpty(callBackUrl)) {
			String[] callBackUrlParts = callBackUrl.split(";");
			for (int i = 0; i < callBackUrlParts.length; i++) {
				callBackUrls.add(callBackUrlParts[i]);
			}
		}
		lockSplitDTO.setCallBackUrls(callBackUrls);
		
		List<LockSplitItemDTO> lockSplitItemDTOs = new ArrayList<>();
		for (LockSplitItem lockSplitItem : lockSplit.getItems()) {
			lockSplitItemDTOs.add(toLockSplitItemDTO(lockSplitItem));
		}
		lockSplitDTO.setLockSplitItemDTOs(lockSplitItemDTOs);
		
		List<LockSplitRecapDTO> lockSplitRecapDTOs = toLockSplitRecapDTOs(
				LCK_SPL_SRV.getLockSplitRecapVOs(lockSplit.getContract().getId(), lockSplit));
		lockSplitDTO.setLockSplitRecapDTOs(lockSplitRecapDTOs);
		
		return lockSplitDTO;
	}
	
	/**
	 * To LockSplitRecap DTOs
	 * 
	 * @param lockSplitRecapVos
	 * @return
	 */
	private List<LockSplitRecapDTO> toLockSplitRecapDTOs(List<LockSplitRecapVO> lockSplitRecapVos) {
		List<LockSplitRecapDTO> lockSplitRecapDTOs = new ArrayList<LockSplitRecapDTO>();
		for (LockSplitRecapVO lockSplitRecapVo : lockSplitRecapVos) {
			lockSplitRecapDTOs.add(toLockSplitRecapDTO(lockSplitRecapVo));
		}
		return lockSplitRecapDTOs;
	}

	/**
	 * To LockSplitRecap DTO
	 * 
	 * @param lockSplitRecapVo
	 * @return
	 */
	private LockSplitRecapDTO toLockSplitRecapDTO(LockSplitRecapVO lockSplitRecapVo) {
		LockSplitRecapDTO lockSplitRecapDTO = new LockSplitRecapDTO();
		lockSplitRecapDTO.setDesc(lockSplitRecapVo.getDesc());
		lockSplitRecapDTO.setAmountToPay(lockSplitRecapVo.getAmountToPay());
		lockSplitRecapDTO.setInLockSplitAmount(lockSplitRecapVo
				.getInLockSplitAmount());
		if (lockSplitRecapVo.getSubLockSplitRecap() != null
				&& !lockSplitRecapVo.getSubLockSplitRecap().isEmpty()) {
			lockSplitRecapDTO.setSubLockSplitRecapDTOs(toLockSplitRecapDTOs(lockSplitRecapVo.getSubLockSplitRecap()));
		}
		return lockSplitRecapDTO;
	}
	
	/**
	 * Convert from LockSplitDTO to LockSplit
	 * @param lockSplitDTO
	 * @param id
	 * @return
	 */
	private LockSplit toLockSplit(LockSplitDTO lockSplitDTO) {
		LockSplit lockSplit = LockSplit.createInstance();		
		Contract contract = null;
		if (StringUtils.isNotEmpty(lockSplitDTO.getContractID())) {
			contract = CONT_SRV.getByReference(lockSplitDTO.getContractID());
			if (contract == null) {
				messages.add(FinWsMessage.CONTRACT_NOT_FOUND);
			}
			lockSplit.setContract(contract);
		}
		
		lockSplit.setFrom(lockSplitDTO.getFrom());
		lockSplit.setTo(lockSplitDTO.getTo());
		lockSplit.setTotalAmount(lockSplitDTO.getTotalAmount());
		lockSplit.setTotalVatAmount(lockSplitDTO.getTotalVatAmount());
		lockSplit.setSendEmail(lockSplitDTO.isSendEmail());
		lockSplit.setSendSms(lockSplitDTO.isSendSms());
		lockSplit.setPromise(lockSplitDTO.isPromise());
		lockSplit.setComment(lockSplitDTO.getComment());
		if (lockSplitDTO.getPaymentChannel() != null) {
			EPaymentChannel paymentChannel = EPaymentChannel.getById(lockSplitDTO.getPaymentChannel().getId());
			lockSplit.setPaymentChannel(paymentChannel);
			
			if (contract != null && EPaymentChannel.DEALERCOUNTER.equals(paymentChannel)) {
				lockSplit.setDealer(contract.getDealer());
			} else {
				Dealer dealer = LCK_SPL_SRV.getById(Dealer.class, lockSplitDTO.getDealer().getId());
				lockSplit.setDealer(dealer);
			}
		}
		
		if (lockSplitDTO.getStatus() != null) {
			lockSplit.setWkfStatus(EWkfStatus.getByCode(lockSplitDTO.getStatus().getCode()));
		}
		
		if (lockSplitDTO.getEventType() != null) {
			lockSplit.setAfterSaleEventType(EAfterSaleEventType.getByCode(lockSplitDTO.getEventType().name()));
		}
		
		List<String> callBackUrls = lockSplitDTO.getCallBackUrls();
		if (callBackUrls != null && !callBackUrls.isEmpty()) {
			lockSplit.setCallBackUrl(getCallBackUrl(callBackUrls));
		}
		
		List<LockSplitItem> newLockSplitItems = new ArrayList<>();
		if (lockSplitDTO.getLockSplitItemDTOs() != null && !lockSplitDTO.getLockSplitItemDTOs().isEmpty()) {			
			// Create new lock split item
			for (LockSplitItemDTO lockSplitItemDTO : lockSplitDTO.getLockSplitItemDTOs()) {
				newLockSplitItems.add(toLockSplitItem(lockSplitItemDTO));
			}
		}
		lockSplit.setCreatedBy(lockSplitDTO.getCreatedUser());
		lockSplit.setItems(newLockSplitItems);
		return lockSplit;
	}
	
	/**
	 * 
	 * @param callBackUrls
	 * @return
	 */
	private String getCallBackUrl(List<String> callBackUrls) {
		StringBuffer url = new StringBuffer(); 
		for (String string : callBackUrls) {
			url.append(string);
			if (StringUtils.isNotEmpty(string)) {
				url.append(";");
			}
		}
		int lastIndex = url.lastIndexOf(";");
		url.replace(lastIndex, lastIndex + 1, "");
		return url.toString();
	}
	
	/**
	 * Convert from LockSplitItemDTO to LockSplitItem
	 * @param lockSplitItemDTO
	 * @param id
	 * @return
	 */
	private LockSplitItem toLockSplitItem(LockSplitItemDTO lockSplitItemDTO) {
		LockSplitItem lockSplitItem = EntityFactory.createInstance(LockSplitItem.class);
		if (StringUtils.isNotEmpty(lockSplitItemDTO.getReceiptCode())){
			lockSplitItem.setJournalEvent(ENTITY_SRV.getByCode(JournalEvent.class, lockSplitItemDTO.getReceiptCode()));
		} else {
			messages.add(FinWsMessage.RECEIPT_CODE_MANDATORY);
		}
		
		lockSplitItem.setTiAmount(lockSplitItemDTO.getAmount());
		lockSplitItem.setVatAmount(lockSplitItemDTO.getVatAmount());
		lockSplitItem.setPriority(lockSplitItemDTO.getPriority());
		if (lockSplitItemDTO.getStatus() != null) {
			lockSplitItem.setWkfStatus(EWkfStatus.getByCode(lockSplitItemDTO.getStatus().getCode()));
		} else {
			messages.add(FinWsMessage.LOCKSPLIT_STATUS_MANDATORY);
		}
		return lockSplitItem;
	}
	
	/**
	 * Convert to LockSplitItem data transfer
	 * @param lockSplitItem
	 * @return
	 */
	private LockSplitItemDTO toLockSplitItemDTO(LockSplitItem lockSplitItem) {
		LockSplitItemDTO lockSplitItemDTO = new LockSplitItemDTO();
		lockSplitItemDTO.setId(lockSplitItem.getId());
		lockSplitItemDTO.setCreatedUser(lockSplitItem.getCreateUser());
		lockSplitItemDTO.setCreatedDate(lockSplitItem.getCreateDate());
		lockSplitItemDTO.setReceiptCode(lockSplitItem.getJournalEvent() != null ? lockSplitItem.getJournalEvent().getCode() : null);
		lockSplitItemDTO.setAmount(lockSplitItem.getTiAmount());
		lockSplitItemDTO.setVatAmount(lockSplitItem.getVatAmount());
		lockSplitItemDTO.setPriority(lockSplitItem.getPriority());
		if (lockSplitItem.getWkfStatus() != null) {
			String code = lockSplitItem.getWkfStatus().getCode();
			lockSplitItemDTO.setStatus(LockSplitStatus.getByCode(code));
		}
		lockSplitItemDTO.setCategory(lockSplitItem.getLockSplitCategory() != null ? toRefDataDTO(lockSplitItem.getLockSplitCategory()) : null);
		if (lockSplitItem.getOperation() != null) {
			lockSplitItemDTO.setOperation((lockSplitItem.getOperation() != null ? getContractOperationDTO(lockSplitItem.getOperation().getId()) : null));
		}
		return lockSplitItemDTO;
	}
	
	/**
	 * @param lockSplits
	 * @return
	 */
	protected List<LockSplitDTO> toLockSplitsDTO(List<LockSplit> lockSplits) {
		List<LockSplitDTO> dtoLst = new ArrayList<>();
		for (LockSplit lockSplit : lockSplits) {
			dtoLst.add(toLockSplitDTO(lockSplit));
		}
		return dtoLst;
	}
	
	
	/**
	 * @param criteria
	 * @return
	 */
	private LockSplitRestriction toLockSplitRestriction(LockSplitCriteriaDTO criteria) {
		LockSplitRestriction restrictions = new LockSplitRestriction();
		restrictions.setLockSplitNo(criteria.getLockSplitNo());
		restrictions.setContractID(criteria.getContractID());
		restrictions.setDueDateFrom(criteria.getDueDateFrom());
		restrictions.setDueDateTo(criteria.getDueDateTo());
		if (criteria.getPaymentChannelId() != null) {
			EPaymentChannel channel = EPaymentChannel.getById(criteria.getPaymentChannelId());
			if (channel == null) {
				messages.add(FinWsMessage.PAYMENT_CHANNEL_NOT_FOUND);
			} else {
				restrictions.setPaymentChannel(channel);
			}
		}
		return restrictions;
	}
}
