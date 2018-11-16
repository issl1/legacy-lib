package com.nokor.efinance.ws.resource.app.contract.request;

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
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractRequest;
import com.nokor.efinance.core.contract.model.ERequestType;
import com.nokor.efinance.core.contract.service.ContractRequestRestriction;
import com.nokor.efinance.share.common.FinWsMessage;
import com.nokor.efinance.share.contract.ContractRequestDTO;
import com.nokor.efinance.ws.resource.app.contract.BaseContractSrvRsc;
import com.nokor.efinance.ws.resource.app.contract.ContractWsPath;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * 
 * @author uhout.cheng
 */
@Path(ContractWsPath.CONTRACTS + ContractWsPath.REQUESTS)
public class ContractRequestSrvRsc extends BaseContractSrvRsc {
	
	/**
	 * List all requests by contract
	 * @param contractNo
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list(@QueryParam(ContractWsPath.REFERENCE) String contractNo) {
		try {
			ContractRequestRestriction restrictions = new ContractRequestRestriction();
			restrictions.setContractNo(contractNo);
			
			List<ContractRequest> contractRequests = ENTITY_SRV.list(restrictions);
			List<ContractRequestDTO> contractRequestDTOs= toContractRequestDTOs(contractRequests);
			
			return ResponseHelper.ok(contractRequestDTOs);
		} catch (Exception e) {
			String errMsg = "Error while searching contract request [" + e.getMessage() + "]";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_LIST_KO, errMsg);
		}
	}
	
	/**
	 * List request by id
	 * @param id
	 * @return
	 */
	@GET
	@Path(BaseWsPath.PATH_ID)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(@PathParam(BaseWsPath.ID) Long id) {
		try {
			LOG.debug("Request - id. [" + id + "]");
			
			ContractRequest request = ENTITY_SRV.getById(ContractRequest.class, id);
			if (request == null) {
				String errMsg = "Contract-Request [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));			
			}
			ContractRequestDTO requestDTO = toContractRequestDTO(request);
			
			return ResponseHelper.ok(requestDTO);
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
	 * Create a request
	 * @param requestDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response create(ContractRequestDTO requestDTO) {
		try {
			if (requestDTO == null) {
				throw new EntityNotValidParameterException(I18N.messageMandatoryField("ContractRequestDTO"));
			}
			ContractRequest request = toContractRequest(requestDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new EntityNotValidParameterException(I18N.messageObjectNotFound(errMsg));
			}
			NOTE_SRV.saveOrUpdateContractRequest(request);
			
			return ResponseHelper.ok(toContractRequestDTO(request));
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
	 * Update a request by id
	 * @param id
	 * @param requestDTO
	 * @return
	 */
	@PUT
	@Path(BaseWsPath.PATH_ID)
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response update(@PathParam("id") Long id, ContractRequestDTO requestDTO) {
		try {
			LOG.debug("ContractRequest - id. [" + id + "]");
			requestDTO.setId(id);
			ContractRequest request = toContractRequest(requestDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			} 
			
			NOTE_SRV.saveOrUpdateContractRequest(request);
			
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
	 * Delete a request by id
	 * @param id
	 * @return
	 */
	@DELETE
	@Path(BaseWsPath.PATH_ID)
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response delete(@PathParam(BaseWsPath.ID) Long id) {
		try {
			LOG.debug("ContractRequest [" + (id != null ? id : NULL) + "]");
			
			ContractRequest request = ENTITY_SRV.getById(ContractRequest.class, id);
			if (request == null) {
				String errMsg = "ContractRequest [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			
			ENTITY_SRV.delete(request);
			
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
	 * @param request
	 * @return
	 */
	private ContractRequestDTO toContractRequestDTO(ContractRequest request) {
		ContractRequestDTO requestDTO = new ContractRequestDTO();
		requestDTO.setId(request.getId());
		requestDTO.setCreationDate(request.getCreateDate());
		requestDTO.setContractNo(request.getContract() != null ? request.getContract().getReference() : StringUtils.EMPTY);
		requestDTO.setRequestType(request.getRequestType() != null ? toRefDataDTO(request.getRequestType()) : null);
		requestDTO.setOtherValue(request.getOthersValue());
		requestDTO.setRemark(request.getComment());
		requestDTO.setProcessed(request.isProcessed());
		requestDTO.setUserLogin(request.getUserLogin());
		return requestDTO;
	}
	
	/**
	 * @param contractRequests
	 * @return
	 */
	private List<ContractRequestDTO> toContractRequestDTOs(List<ContractRequest> contractRequests) {
		List<ContractRequestDTO> contractRequestDTOs = new ArrayList<ContractRequestDTO>();
		for (ContractRequest contractRequest : contractRequests) {
			contractRequestDTOs.add(toContractRequestDTO(contractRequest));
		}
		return contractRequestDTOs;
	}
	
	/**
	 * @param requestDTO
	 * @return
	 */
	private ContractRequest toContractRequest(ContractRequestDTO requestDTO) {
		ContractRequest request = null;
		if (requestDTO.getId() != null) {
			request = ENTITY_SRV.getById(ContractRequest.class, requestDTO.getId());
			if (request == null) {
				messages.add(FinWsMessage.CONTRACT_REQUEST_NOT_FOUND);
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
		} else {
			 request = ContractRequest.createInstance();
		}
		
		Contract contract = null;
		if (StringUtils.isEmpty(requestDTO.getContractNo())) {
			messages.add(FinWsMessage.CONTRACT_MANDATORY);
		} else {
			contract = CONT_SRV.getByReference(requestDTO.getContractNo());
			if (contract == null) {
				messages.add(FinWsMessage.CONTRACT_NOT_FOUND);
			}
		}
		request.setContract(contract);
		
		if (requestDTO.getRequestType() != null) {
			request.setRequestType(ERequestType.getById(requestDTO.getRequestType().getId()));
		} 
		
		request.setComment(requestDTO.getRemark());
		request.setOthersValue(requestDTO.getOtherValue());
		request.setProcessed(requestDTO.isProcessed());
		request.setUserLogin(requestDTO.getUserLogin());
		
		return request;
	}

}
