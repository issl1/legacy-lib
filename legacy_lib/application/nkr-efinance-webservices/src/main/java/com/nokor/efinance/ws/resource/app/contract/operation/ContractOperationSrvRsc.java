package com.nokor.efinance.ws.resource.app.contract.operation;

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
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.exception.EntityAlreadyExistsException;
import org.seuksa.frmk.tools.exception.EntityCreationException;
import org.seuksa.frmk.tools.exception.EntityNotValidParameterException;
import org.seuksa.frmk.tools.exception.EntityUpdateException;
import org.springframework.dao.DataIntegrityViolationException;

import com.nokor.common.messaging.ws.resource.BaseWsPath;
import com.nokor.efinance.core.collection.model.ContractOperation;
import com.nokor.efinance.core.collection.model.EOperationType;
import com.nokor.efinance.core.collection.service.ContractOperationRestriction;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.share.common.FinWsMessage;
import com.nokor.efinance.share.contract.ContractOperationDTO;
import com.nokor.efinance.ws.resource.app.contract.BaseContractSrvRsc;
import com.nokor.efinance.ws.resource.app.contract.ContractWsPath;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * 
 * @author uhout.cheng
 */
@Path(ContractWsPath.CONTRACTS + ContractWsPath.OPERATIONS)
public class ContractOperationSrvRsc extends BaseContractSrvRsc {
	
	/**
	 * List all operations by contract
	 * @param contractNo
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list(@QueryParam(ContractWsPath.REFERENCE) String contractNo) {
		try {
			LOG.debug("Contract - Ref. [" + contractNo + "]");
			ContractOperationRestriction restrictions = new ContractOperationRestriction();
			
			if (StringUtils.isNotEmpty(contractNo)) {
				Contract contra = CONT_SRV.getByReference(contractNo);
				if (contra != null) {
					restrictions.setConId(contra.getId());
				} else {
					String errMsg = "Contract [" + contractNo + "]";
					throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));	
				}
			}
			
			List<ContractOperation> operations = ENTITY_SRV.list(restrictions);
			List<ContractOperationDTO> operationDTOs= toContractOperationDTOs(operations);
			
			return ResponseHelper.ok(operationDTOs);
		} catch (Exception e) {
			String errMsg = "Error while searching contract operation [" + e.getMessage() + "]";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_LIST_KO, errMsg);
		}
	}
	
	/**
	 * List operation by id
	 * @param id
	 * @return
	 */
	@GET
	@Path(BaseWsPath.PATH_ID)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(@PathParam(BaseWsPath.ID) Long id) {
		try {
			LOG.debug("Operation - id. [" + id + "]");
			
			ContractOperation operation = ENTITY_SRV.getById(ContractOperation.class, id);
			if (operation == null) {
				String errMsg = "Contract-Operation [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));			
			}
			ContractOperationDTO operationDTO = toContractOperationDTO(operation);
			
			return ResponseHelper.ok(operationDTO);
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
	 * Create a operation
	 * @param operationDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response create(ContractOperationDTO operationDTO) {
		try {
			if (operationDTO == null) {
				throw new EntityNotValidParameterException(I18N.messageMandatoryField("ContractOperationDTO"));
			}
			ContractOperation operation = toContractOperation(operationDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new EntityNotValidParameterException(I18N.messageObjectNotFound(errMsg));
			}
			ENTITY_SRV.create(operation);
			
			return ResponseHelper.ok(toContractOperationDTO(operation));
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
	 * Update a operation by id
	 * @param id
	 * @param operationDTO
	 * @return
	 */
	@PUT
	@Path(BaseWsPath.PATH_ID)
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response update(@PathParam("id") Long id, ContractOperationDTO operationDTO) {
		try {
			LOG.debug("ContractOperation - id. [" + id + "]");
			operationDTO.setId(id);
			ContractOperation operation = toContractOperation(operationDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			} 
			
			ENTITY_SRV.update(operation);
			
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
	 * Delete a operation by id
	 * @param id
	 * @return
	 */
	@DELETE
	@Path(BaseWsPath.PATH_ID)
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response delete(@PathParam(BaseWsPath.ID) Long id) {
		try {
			LOG.debug("ContractOperation [" + (id != null ? id : NULL) + "]");
			
			ContractOperation operation = ENTITY_SRV.getById(ContractOperation.class, id);
			if (operation == null) {
				String errMsg = "ContractOperation [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			
			ENTITY_SRV.delete(operation);
			
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
	 * @param operation
	 * @return
	 */
	private ContractOperationDTO toContractOperationDTO(ContractOperation operation) {
		ContractOperationDTO operationDTO = new ContractOperationDTO();
		operationDTO.setId(operation.getId());
		operationDTO.setContractNo(operation.getContract() != null ? operation.getContract().getReference() : StringUtils.EMPTY);
		operationDTO.setOperationType(operation.getOperationType() != null ? toRefDataDTO(operation.getOperationType()) : null);
		operationDTO.setPrice(MyNumberUtils.getDouble(operation.getTiPrice()));
		return operationDTO;
	}
	
	/**
	 * @param operations
	 * @return
	 */
	private List<ContractOperationDTO> toContractOperationDTOs(List<ContractOperation> operations) {
		List<ContractOperationDTO> operationDTOs = new ArrayList<ContractOperationDTO>();
		for (ContractOperation operation : operations) {
			operationDTOs.add(toContractOperationDTO(operation));
		}
		return operationDTOs;
	}
	
	/**
	 * @param operationDTO
	 * @return
	 */
	private ContractOperation toContractOperation(ContractOperationDTO operationDTO) {
		ContractOperation operation = null;
		if (operationDTO.getId() != null) {
			operation = ENTITY_SRV.getById(ContractOperation.class, operationDTO.getId());
			if (operation == null) {
				messages.add(FinWsMessage.CONTRACT_OPERATION_NOT_FOUND);
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
		} else {
			 operation = ContractOperation.createInstance();
		}
		
		Contract contract = null;
		if (StringUtils.isEmpty(operationDTO.getContractNo())) {
			messages.add(FinWsMessage.CONTRACT_MANDATORY);
		} else {
			contract = CONT_SRV.getByReference(operationDTO.getContractNo());
			if (contract == null) {
				messages.add(FinWsMessage.CONTRACT_NOT_FOUND);
			}
		}
		operation.setContract(contract);
		
		if (operationDTO.getOperationType() != null) {
			operation.setOperationType(EOperationType.getById(operationDTO.getOperationType().getId()));
		} 
		
		operation.setTiPrice(MyNumberUtils.getDouble(operationDTO.getPrice()));
		return operation;
	}

}
