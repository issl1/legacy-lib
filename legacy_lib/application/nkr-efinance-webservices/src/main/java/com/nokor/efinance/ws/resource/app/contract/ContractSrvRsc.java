package com.nokor.efinance.ws.resource.app.contract;

import java.util.ArrayList;
import java.util.List;

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

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.exception.EntityAlreadyExistsException;
import org.seuksa.frmk.tools.exception.EntityCreationException;
import org.seuksa.frmk.tools.exception.EntityNotValidParameterException;
import org.seuksa.frmk.tools.exception.EntityUpdateException;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.service.ContractRestriction;
import com.nokor.efinance.core.contract.service.aftersales.TransferApplicantSimulateRequest;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.share.applicant.ApplicantDTO;
import com.nokor.efinance.share.contract.ContractDTO;
import com.nokor.efinance.share.contract.TransferApplicantDTO;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;


/**
 * 
 * @author uhout.cheng
 */
@Path("/contracts")
public class ContractSrvRsc extends BaseContractSrvRsc {
	
	/**
	 * List all contracts 
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list() {
		try {
			// get all quotations
			ContractRestriction restrictions = new ContractRestriction();
			List<Contract> contracts = CONT_SRV.list(restrictions);
			List<ContractDTO> contractDTOs = toContractsDTO(contracts);
			
			return ResponseHelper.ok(contractDTOs);
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
	 * List contract by reference
	 * @param reference
	 * @return
	 */
	@GET
	@Path("/{reference}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(@PathParam("reference") String reference) {
		try {
			LOG.debug("Contract - Ex-Ref. [" + reference + "]");
		
			Contract contract = CONT_SRV.getByReference(reference);
			
			if (contract == null) {
				String errMsg = "Contract [" + reference + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));			
			}
			
			ContractDTO contractDTO = toContractDTO(contract);
			
			return ResponseHelper.ok(contractDTO);
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
	 * Create contract
	 * @param contractDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response create(ContractDTO contractDTO) {
		
		try {
			LOG.debug("Contract Ref. [" + contractDTO.getApplicationID() + "]");
			
			Quotation quotation = toQuotation(contractDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
			Contract contract = CONT_ACTIVATION_SRV.createContract(quotation);
			ContractDTO newContraDTO = toContractDTO(contract);
			
			return ResponseHelper.ok(newContraDTO);
		} catch (EntityCreationException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.CREATION_KO, errMsg);
		} catch (EntityAlreadyExistsException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.ALREADY_EXISTS, errMsg);
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
			throw new WsReponseException(EResponseStatus.CREATION_KO, errMsg);
		}
	}
	
	/**
	 * Update asset by registration number
	 * @param id
	 * @param assetDTO
	 * @return
	 */
	@PUT
	@Path("/{reference}")
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response update(ContractDTO contractDTO, @PathParam("reference") String reference) {
		try {
			LOG.debug("Contract - Reference. [" + reference + "]");
			Contract contract = toContract(contractDTO, reference);
			
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			} 
			
			CONT_ACTIVATION_SRV.updateContractData(contract);
			
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
	 * Transfer applicant
	 * @param 
	 * @return
	 */
	@POST
	@Path("/transfer")
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response transfer(TransferApplicantDTO transferApplicantDTO) {
		try {
			LOG.debug("Contract Ref. [" + transferApplicantDTO.getApplicationID() + "]");
			
			TransferApplicantSimulateRequest transferApplicantSimulateRequest = toTransferApplicantDTO(transferApplicantDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
			Contract contract = TRANSFERT_SRV.simulate(transferApplicantSimulateRequest);
			ContractDTO newContraDTO = toContractDTO(contract);
			
			return ResponseHelper.ok(newContraDTO);
		} catch (EntityCreationException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.CREATION_KO, errMsg);
		} catch (EntityAlreadyExistsException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.ALREADY_EXISTS, errMsg);
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
			throw new WsReponseException(EResponseStatus.CREATION_KO, errMsg);
		}
	}
	
	/**
	 * @param transferApplicantDTO
	 * @return
	 */
	private TransferApplicantSimulateRequest toTransferApplicantDTO(TransferApplicantDTO transferApplicantDTO) {
		TransferApplicantSimulateRequest transferApplicantSimulateRequest = new TransferApplicantSimulateRequest();
		transferApplicantSimulateRequest.setApplicant(toApplicant(transferApplicantDTO.getLessee(), LESSEE));
		if (StringUtils.isNotEmpty(transferApplicantDTO.getContractID())) {
			Contract contract = CONT_SRV.getByReference(transferApplicantDTO.getContractID());
			transferApplicantSimulateRequest.setCotraId(contract == null ? null : contract.getId());
		}
		transferApplicantSimulateRequest.setApplicationID(transferApplicantDTO.getApplicationID());
		transferApplicantSimulateRequest.setApplicationDate(transferApplicantDTO.getApplicationDate());
		transferApplicantSimulateRequest.setApprovalDate(transferApplicantDTO.getApprovalDate());
		transferApplicantSimulateRequest.setEventDate(transferApplicantDTO.getEventDate());
		
		if (transferApplicantDTO.getGuarantors() != null) {
			List<Applicant> guarantors = new ArrayList<>();
			for (ApplicantDTO guarantorDTO : transferApplicantDTO.getGuarantors()) {
				guarantors.add(toApplicant(guarantorDTO, GUARANTOR));
			}
			transferApplicantSimulateRequest.setGuarantors(guarantors);
		}
		return transferApplicantSimulateRequest;
	}
	
	
	/**
	 * @return
	 */
	@GET
	@Path("/remainingoneinstallment")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response getRemainingOneInstallmentContracts() {
		try {		
			List<String> contracts = CONT_SRV.getRemainingOneInstallmentContracts();			
			return ResponseHelper.ok(contracts);
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_UNIQUE_KO, errMsg);
		}
	}
}
