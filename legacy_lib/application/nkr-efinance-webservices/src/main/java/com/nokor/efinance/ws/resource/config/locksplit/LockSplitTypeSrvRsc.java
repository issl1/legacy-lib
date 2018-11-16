package com.nokor.efinance.ws.resource.config.locksplit;

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
import org.springframework.dao.DataIntegrityViolationException;

import com.nokor.efinance.core.collection.model.ELockSplitCashflowType;
import com.nokor.efinance.core.collection.model.ELockSplitType;
import com.nokor.efinance.core.collection.model.LockSplitTypeRestriction;
import com.nokor.efinance.share.common.FinWsMessage;
import com.nokor.efinance.share.locksplit.LockSplitCashflowTypeDTO;
import com.nokor.efinance.share.locksplit.LockSplitTypeDTO;
import com.nokor.efinance.ws.FinResourceSrvRsc;
import com.nokor.efinance.ws.resource.app.contract.ContractWsPath;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * Lock Split Type web service
 * @author uhout.cheng
 */
@Path("/configs/params/locksplittypes")
public class LockSplitTypeSrvRsc extends FinResourceSrvRsc {
	
	/**
	 * List all lock split types
	 * @param receiptcode
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list(@QueryParam(ContractWsPath.RECEIPTCODE) String receiptcode) {		
		try {				
			LOG.debug("ELockSplitType - Code. [" + receiptcode + "]");
			
			LockSplitTypeRestriction restrictions = new LockSplitTypeRestriction();
			
			if (StringUtils.isNotEmpty(receiptcode)) {
				restrictions.setCodes(new String[] { receiptcode });
			}
			
			List<ELockSplitType> lockSplitTypes = ENTITY_SRV.list(restrictions);
			List<LockSplitTypeDTO> lockSplitTypeDTOs = new ArrayList<>();
			for (ELockSplitType lockSplitType : lockSplitTypes) {
				lockSplitTypeDTOs.add(toLockSplitTypeDTO(lockSplitType));
			}
			return ResponseHelper.ok(lockSplitTypeDTOs);
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
	 * List lock split type by id
	 * @param id
	 * @return
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(@PathParam("id") Long id) {
		try {
			LOG.debug("Lock Split Type - id. [" + id + "]");
		
			ELockSplitType type = LCK_SPL_SRV.getById(ELockSplitType.class, id);
			
			if (type == null) {
				String errMsg = "Lock Split Type [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));			
			}
			
			LockSplitTypeDTO typeDTO = toLockSplitTypeDTO(type);
			
			return ResponseHelper.ok(typeDTO);
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
	 * Create Lock Split Type
	 * @param typeDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response create(LockSplitTypeDTO typeDTO) {
		try {
			ELockSplitType type = toLockSplitType(typeDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
			
			LCK_SPL_SRV.create(type);
			
			return ResponseHelper.ok(toLockSplitTypeDTO(type));
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
	 * Update Lock Split Type
	 * @param id
	 * @param typeDTO
	 * @return
	 */
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response update(@PathParam("id") Long id, LockSplitTypeDTO typeDTO) {
		try {
			LOG.debug("Lock Split Type - id. [" + id + "]");
			typeDTO.setId(id);
			ELockSplitType type = toLockSplitType(typeDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			} 
			
			LCK_SPL_SRV.update(type);
			
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
	 * Delete Lock Split Type
	 * @param id
	 * @return
	 */
	@DELETE
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response delete(@PathParam("id") Long id) {
		try {
			LOG.debug("Lock Split Type - id. [" + id + "]");
			
			ELockSplitType type = LCK_SPL_SRV.getById(ELockSplitType.class, id);
			
			if (type == null) {
				String errMsg = "Lock Split Type [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			
			LCK_SPL_SRV.delete(type);
			
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
			String errMsg = "This lock split type id [" + id + "] can not be deleted. Because, It is used by system.";
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
	 * @param type
	 * @return
	 */
	private LockSplitTypeDTO toLockSplitTypeDTO(ELockSplitType type) {
		LockSplitTypeDTO typeDTO = new LockSplitTypeDTO();
		typeDTO.setId(type.getId());
		typeDTO.setCode(type.getCode());
		typeDTO.setDesc(type.getDesc());
		typeDTO.setDescEn(type.getDescEn());
		
		List<ELockSplitCashflowType> splitCashflowTypes = type.getLockSplitCashflowTypes();
		List<LockSplitCashflowTypeDTO> splitCashflowTypeDTOs = new ArrayList<LockSplitCashflowTypeDTO>();
		if (splitCashflowTypes != null && !splitCashflowTypes.isEmpty()) {
			for (ELockSplitCashflowType splitCashflowType : splitCashflowTypes) {
				splitCashflowTypeDTOs.add(toLockSplitCashflowTypeDTO(splitCashflowType));
			}
		}
		
		typeDTO.setLockSplitCashflowTypes(splitCashflowTypeDTOs);
		return typeDTO;
	}
	
	/**
	 * 
	 * @param splitCashflowType
	 * @return
	 */
	private LockSplitCashflowTypeDTO toLockSplitCashflowTypeDTO(ELockSplitCashflowType splitCashflowType) {
		LockSplitCashflowTypeDTO splitCashflowTypeDTO = new LockSplitCashflowTypeDTO();
		splitCashflowTypeDTO.setId(splitCashflowType.getId());
		splitCashflowTypeDTO.setLockSplitType((splitCashflowType.getLockSplitType() != null ? getLockSplitTypeDTO(splitCashflowType.getLockSplitType().getId()) : null));
		splitCashflowTypeDTO.setCashflowType(splitCashflowType.getCashflowType() != null ? toRefDataDTO(splitCashflowType.getCashflowType()) : null);
		return splitCashflowTypeDTO;
	}
	
	/**
	 * 
	 * @param typeDTO
	 * @param id
	 * @return
	 */
	private ELockSplitType toLockSplitType(LockSplitTypeDTO typeDTO) {
		ELockSplitType type = null;
		if (typeDTO.getId() != null) {
			type = ENTITY_SRV.getById(ELockSplitType.class, typeDTO.getId());
			if (type == null) {
				messages.add(FinWsMessage.LOCKSPLIT_TYPE_NOT_FOUND);
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
		} else {
			type = ELockSplitType.createInstance();
		}
		
		if (StringUtils.isNotEmpty(typeDTO.getCode())) {
			type.setCode(typeDTO.getCode());
		} else {
			messages.add(FinWsMessage.LOCKSPLIT_TYPE_CODE_MANDATORY);
		}
		
		if (!StringUtils.isEmpty(typeDTO.getDescEn()) && !StringUtils.isEmpty(typeDTO.getDesc())) {
			type.setDesc(typeDTO.getDesc());
			type.setDescEn(typeDTO.getDescEn());
		} else if (!StringUtils.isEmpty(typeDTO.getDesc()) && StringUtils.isEmpty(typeDTO.getDescEn())) {
			type.setDesc(typeDTO.getDesc());
			type.setDescEn(typeDTO.getDesc());
		} else if (StringUtils.isEmpty(typeDTO.getDesc()) && !StringUtils.isEmpty(typeDTO.getDescEn())) {
			type.setDesc(typeDTO.getDescEn());
			type.setDescEn(typeDTO.getDescEn());
		} else {
			messages.add(FinWsMessage.LOCKSPLIT_TYPE_DESC_EN_MANDATORY);
		}
		
		return type;
	}
}
