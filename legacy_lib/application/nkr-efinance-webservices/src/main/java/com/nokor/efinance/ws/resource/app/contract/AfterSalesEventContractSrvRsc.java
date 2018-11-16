package com.nokor.efinance.ws.resource.app.contract;

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.exception.EntityNotFoundException;
import org.seuksa.frmk.tools.spring.SpringUtils;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.service.aftersales.EarlySettlementService;
import com.nokor.efinance.core.contract.service.aftersales.EarlySettlementSimulateRequest;
import com.nokor.efinance.core.contract.service.aftersales.EarlySettlementSimulateResponse;
import com.nokor.efinance.core.contract.service.aftersales.LossSaveRequest;
import com.nokor.efinance.core.contract.service.aftersales.LossService;
import com.nokor.efinance.core.contract.service.aftersales.LossSimulateRequest;
import com.nokor.efinance.core.contract.service.aftersales.LossSimulateResponse;
import com.nokor.efinance.core.contract.service.aftersales.LossValidateRequest;
import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.efinance.share.common.BalanceAmountDTO;
import com.nokor.efinance.share.common.FinWsMessage;
import com.nokor.efinance.share.contract.CloseContractDTO;
import com.nokor.efinance.share.contract.ContractBalanceDTO;
import com.nokor.efinance.share.contract.EarlySettlementDTO;
import com.nokor.efinance.share.contract.EarlySettlementResponseDTO;
import com.nokor.efinance.share.contract.LossSimulateResponseDTO;
import com.nokor.efinance.share.contract.RepossessionDTO;
import com.nokor.efinance.share.contract.RepossessionResponseDTO;
import com.nokor.efinance.share.contract.ReverseContractDTO;
import com.nokor.efinance.share.contract.ReverseContractResponseDTO;
import com.nokor.efinance.share.contract.TerminateContractDTO;
import com.nokor.efinance.share.contract.withdraw.WithdrawContractDTO;
import com.nokor.efinance.share.contract.withdraw.WithdrawContractResponseDTO;
import com.nokor.ersys.finance.accounting.model.JournalEvent;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;


/**
 * 
 * @author uhout.cheng
 */
@Path("/contracts")
public class AfterSalesEventContractSrvRsc extends BaseContractSrvRsc {
	
	/**
	 * 
	 * @param earlySettlementDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/earlysettlement")
	public Response earlySettlementService(EarlySettlementDTO earlySettlementDTO) {
		try {
			LOG.debug("Contract Ref. [" + earlySettlementDTO.getContractID() + "]");
			Contract contract = CONT_SRV.getByReference(earlySettlementDTO.getContractID());
			
			if (contract == null) {
				String errMsg = "Contract [" + earlySettlementDTO.getContractID() + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			
			EarlySettlementSimulateResponse earlySettlementSimulateResponse = this.earlySettlementService(contract, earlySettlementDTO.getEarlySettlementDate(), true);
			EarlySettlementResponseDTO res = new EarlySettlementResponseDTO();
			res.setContractID(earlySettlementDTO.getContractID());
			ContractBalanceDTO contractBalanceDTO = new ContractBalanceDTO();
			contractBalanceDTO.setBalanceCapital(new BalanceAmountDTO(toJournalEventDTO(FIN_PROD_SRV.getByCode(JournalEvent.class, ECashflowType.CAP_JOURNAL_EVENT)), MyNumberUtils.getDouble(earlySettlementSimulateResponse.getBalanceCapital().getTiAmount())));
			contractBalanceDTO.setBalanceInterest(new BalanceAmountDTO(toJournalEventDTO(FIN_PROD_SRV.getByCode(JournalEvent.class, ECashflowType.IAP_JOURNAL_EVENT)), MyNumberUtils.getDouble(earlySettlementSimulateResponse.getBalanceInterest().getTiAmount())));
			contractBalanceDTO.setBalancePenalty(new BalanceAmountDTO(toJournalEventDTO(FIN_PROD_SRV.getByCode(JournalEvent.class, ECashflowType.PEN_JOURNAL_EVENT)), MyNumberUtils.getDouble(earlySettlementSimulateResponse.getBalancePenalty().getTiAmount())));
			contractBalanceDTO.setBalanceFollowingFee(new BalanceAmountDTO(toJournalEventDTO(FIN_PROD_SRV.getFinServiceByType(EServiceType.FOLLWFEE).getJournalEvent()), MyNumberUtils.getDouble(earlySettlementSimulateResponse.getBalanceFollowingFee().getTiAmount())));
			contractBalanceDTO.setBalanceRepossessionFee(new BalanceAmountDTO(toJournalEventDTO(FIN_PROD_SRV.getFinServiceByType(EServiceType.REPOSFEE).getJournalEvent()), MyNumberUtils.getDouble(earlySettlementSimulateResponse.getBalanceRepossessionFee().getTiAmount())));
			contractBalanceDTO.setBalanceCollectionFee(new BalanceAmountDTO(toJournalEventDTO(FIN_PROD_SRV.getFinServiceByType(EServiceType.COLFEE).getJournalEvent()), MyNumberUtils.getDouble(earlySettlementSimulateResponse.getBalanceCollectionFee().getTiAmount())));
			contractBalanceDTO.setBalanceOperationFee(new BalanceAmountDTO(toJournalEventDTO(FIN_PROD_SRV.getFinServiceByType(EServiceType.OPERFEE).getJournalEvent()), MyNumberUtils.getDouble(earlySettlementSimulateResponse.getBalanceOperationFee().getTiAmount())));
			contractBalanceDTO.setBalancePressingFee(new BalanceAmountDTO(toJournalEventDTO(FIN_PROD_SRV.getFinServiceByType(EServiceType.PRESSFEE).getJournalEvent()), MyNumberUtils.getDouble(earlySettlementSimulateResponse.getBalancePressingFee().getTiAmount())));
			contractBalanceDTO.setBalanceTransferFee(new BalanceAmountDTO(toJournalEventDTO(FIN_PROD_SRV.getFinServiceByType(EServiceType.TRANSFEE).getJournalEvent()), MyNumberUtils.getDouble(earlySettlementSimulateResponse.getBalanceTransferFee().getTiAmount())));
					
			res.setContractBalance(contractBalanceDTO);
					
			return ResponseHelper.ok(res);
			
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (Exception e) {
			String errMsg = "Error while invoking early settlement service - Ref. [" + earlySettlementDTO.getContractID() + "]";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.KO, errMsg);
		}
	}
	
	/**
	 * 
	 * @param repossessionDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/repossession")
	public Response repossessionService(final RepossessionDTO repossessionDTO) {
		
		try {
			LOG.debug("Contract Ref. [" + repossessionDTO.getContractID() + "]");
			Contract contract = CONT_SRV.getByReference(repossessionDTO.getContractID());
			
			if (contract == null) {
				String errMsg = "Contract Ref. [" + repossessionDTO.getContractID() + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			
			LossSimulateResponse lossSimulateResponse = this.repossessionService(contract);
			RepossessionResponseDTO res = new RepossessionResponseDTO();
			res.setContractID(repossessionDTO.getContractID());
			res.setTotalPrincipal(lossSimulateResponse.getTotalPrincipal().getTiAmount());
			res.setTotalInterest(lossSimulateResponse.getTotalInterest().getTiAmount());
			res.setTotalOther(lossSimulateResponse.getTotalOther().getTiAmount());
			
			return ResponseHelper.ok(res);
			
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (Exception e) {
			String errMsg = "Error while invoking early settlement service - Contract Ref. [" + repossessionDTO.getContractID() + "]";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.KO, errMsg);
		}
	}
	
	/**
	 * 
	 * @param contract
	 * @param earlySettlementDate
	 * @param includePenalty
	 */
	private EarlySettlementSimulateResponse earlySettlementService(Contract contract, Date earlySettlementDate, boolean includePenalty) {
		
		EarlySettlementService earlySettlementService = SpringUtils.getBean(EarlySettlementService.class);
		
		EarlySettlementSimulateRequest request = new EarlySettlementSimulateRequest();
		request.setCotraId(contract.getId());
		request.setEventDate(earlySettlementDate);
		request.setIncludePenalty(includePenalty);
		EarlySettlementSimulateResponse simulateResponse = earlySettlementService.simulate(request);
		
		/*EarlySettlementSaveRequest saveRequest = new EarlySettlementSaveRequest();
		saveRequest.setCotraId(simulateResponse.getCotraId());
		saveRequest.setEarlySettlementDate(simulateResponse.getEventDate());
		saveRequest.setBalanceCapital(simulateResponse.getBalanceCapital());
    	saveRequest.setBalanceCollectionFee(simulateResponse.getBalanceCollectionFee());
    	saveRequest.setBalanceFollowingFee(simulateResponse.getBalanceFollowingFee());
    	saveRequest.setBalanceInterest(simulateResponse.getBalanceInterest());
    	saveRequest.setBalanceOperationFee(simulateResponse.getBalanceOperationFee());
    	saveRequest.setBalancePenalty(simulateResponse.getBalancePenalty());
    	saveRequest.setBalancePressingFee(simulateResponse.getBalancePressingFee());
    	saveRequest.setBalanceRepossessionFee(simulateResponse.getBalanceRepossessionFee());  	
    	saveRequest.setBalanceTransferFee(simulateResponse.getBalanceTransferFee());    	
    	
    	saveRequest.setAdjustmentInterest(simulateResponse.getAdjustmentInterest());
    	saveRequest.setCashflows(simulateResponse.getCashflows());
    	saveRequest.setPaymentMethod(earlySettlementService.getByCode(EPaymentMethod.class, "CASH"));
    	saveRequest.setDealer(contract.getDealer());
       	
    	earlySettlementService.save(saveRequest);*/
       	
		return simulateResponse;
	}
	
	/**
	 * 
	 * @param contract
	 * @return
	 */
	private LossSimulateResponse repossessionService(Contract contract) {
		
		LossService lossService = SpringUtils.getBean(LossService.class);
		
		LossSimulateRequest request = new LossSimulateRequest();
		request.setCotraId(contract.getId());
		// simulate event date today for REPOSSESS
		request.setEventDate(DateUtils.todayH00M00S00());
		LossSimulateResponse simulateResponse = lossService.simulate(request);
		
		LossSaveRequest saveRequest = new LossSaveRequest();
		saveRequest.setCotraId(simulateResponse.getCotraId());
    	saveRequest.setTotalPrincipal(simulateResponse.getTotalPrincipal());
    	saveRequest.setTotalInterest(simulateResponse.getTotalInterest());
    	saveRequest.setTotalOther(simulateResponse.getTotalOther());
    	saveRequest.setInsuranceFee(simulateResponse.getInsuranceFee());
    	saveRequest.setServicingFee(simulateResponse.getServicingFee());
    	saveRequest.setCashflows(simulateResponse.getCashflows());
    	saveRequest.setDealer(contract.getDealer());
    	lossService.save(saveRequest);
    	
    	return simulateResponse;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/terminate")
	public Response terminateContract(TerminateContractDTO terminateContractDTO) {
		try {
			LOG.debug("Contract Ref. [" + terminateContractDTO.getContractID() + "]");
			Contract contract = CONT_SRV.getByReference(terminateContractDTO.getContractID());
			if (contract == null) {
				String errMsg = "Contract Ref. [" + terminateContractDTO.getContractID() + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			
			LossSimulateResponse lossSimulateResponse = this.terminateContractService(contract.getId(), DateUtils.today());
			
			LossSimulateResponseDTO lossSimulateResponseDTO = new LossSimulateResponseDTO();
			lossSimulateResponseDTO.setContractID(contract.getReference());
			lossSimulateResponseDTO.setTotalPrincipal(lossSimulateResponse.getTotalPrincipal() == null ? 
					0d : MyNumberUtils.getDouble(lossSimulateResponse.getTotalPrincipal().getTiAmount()));
			lossSimulateResponseDTO.setTotalInterest(lossSimulateResponse.getTotalInterest() == null ? 
					0d : MyNumberUtils.getDouble(lossSimulateResponse.getTotalInterest().getTiAmount()));
			lossSimulateResponseDTO.setTotalOther(lossSimulateResponse.getTotalOther() == null ?
					0d : MyNumberUtils.getDouble(lossSimulateResponse.getTotalOther().getTiAmount()));
		
			return ResponseHelper.ok(lossSimulateResponseDTO);
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (Exception e) {
			String errMsg = "Error while terminating Contract Ref. [" + terminateContractDTO.getContractID() + "]";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.KO, errMsg);
		}
	}
	
	/**
	 * 
	 * @param contractId
	 * @param eventDate
	 */
	private LossSimulateResponse terminateContractService(Long contractId, Date eventDate) {
		LossSimulateRequest request = new LossSimulateRequest();
		request.setCotraId(contractId);
		request.setEventDate(eventDate);
		LossSimulateResponse simulateResponse = LOSS_SRV.simulate(request);
		
		LossValidateRequest validateRequest = new LossValidateRequest();
		validateRequest.setCotraId(simulateResponse.getCotraId());
		validateRequest.setWkfStatus(ContractWkfStatus.LOS);
		validateRequest.setEventDate(simulateResponse.getEventDate());
		validateRequest.setTotalInterest(simulateResponse.getTotalInterest());
		validateRequest.setTotalPrincipal(simulateResponse.getTotalPrincipal());
		validateRequest.setTotalOther(simulateResponse.getTotalOther());
		validateRequest.setInsuranceFee(simulateResponse.getInsuranceFee());
		validateRequest.setServicingFee(simulateResponse.getServicingFee());
		validateRequest.setCashflows(simulateResponse.getCashflows());
		LOSS_SRV.validate(validateRequest);
		
		return simulateResponse;
		
	}
	
	/**
	 * 
	 * @param closeContractDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/close")
	public Response closeContract(CloseContractDTO closeContractDTO) {
		try {
			LOG.debug("Contract Ref. [" + closeContractDTO.getContractID() + "]");
			Contract contract = CONT_SRV.getByReference(closeContractDTO.getContractID());
			if (contract == null) {
				String errMsg = "Contract Ref. [" + closeContractDTO.getContractID() + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
				
			double intBalance = CONT_SRV.getRealInterestBalance(DateUtils.todayH00M00S00(), contract.getId()).getTiAmount();
			double outstanding = CONT_SRV.getRealOutstanding(DateUtils.todayH00M00S00(), contract.getId()).getTiAmount();
			double totalBalance = Math.round(intBalance + outstanding);
			if (totalBalance != 0) {
				String errMsg = FinWsMessage.CONTRACT_BALANCE_NOT_ZERO.getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
			CONT_SRV.closeContract(contract);
			
			return ResponseHelper.ok();
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (Exception e) {
			String errMsg = "Error while closing Contract Ref. [" + closeContractDTO.getContractID() + "]" + e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.KO, errMsg);
		}
	}	
	
	/**
	 * WITHDRAWN changed to PENDING
	 * @param reference
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/reverse")
	public Response reverseContract(ReverseContractDTO reverseContractDTO) {
		try {
			LOG.debug("Contract Ref. [" + reverseContractDTO.getContractID() + "]");
			Contract contract = CONT_SRV.getByReference(reverseContractDTO.getContractID());
			if (contract == null) {
				String errMsg = "Contract Ref. [" + reverseContractDTO.getContractID() + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
				
			CONT_SRV.reverseContract(contract);
			
			ReverseContractResponseDTO responseDTO = new ReverseContractResponseDTO();
			responseDTO.setContractID(contract.getReference());
			responseDTO.setStatus(contract.getWkfStatus().getDescLocale());
			
			return ResponseHelper.ok(responseDTO);
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (Exception e) {
			String errMsg = "Error while reverse Contract Ref. [" + reverseContractDTO.getContractID() + "]" + e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.KO, errMsg);
		}
	}	
	
	/**
	 * Changed status to WITHDRAWN
	 * @param reference
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/withdraw")
	public Response withdrawContract(WithdrawContractDTO withdrawContractDTO) {
		try {
			LOG.debug("Contract Ref. [" + withdrawContractDTO.getContractID() + "]");
			Contract contract = CONT_SRV.getByReference(withdrawContractDTO.getContractID());
			if (contract == null) {
				String errMsg = "Contract Ref. [" + withdrawContractDTO.getContractID() + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
				
			CONT_SRV.withdrawContract(contract);
			
			WithdrawContractResponseDTO responseDTO = new WithdrawContractResponseDTO();
			responseDTO.setContractID(contract.getReference());
			responseDTO.setStatus(contract.getWkfStatus().getDescLocale());
			
			return ResponseHelper.ok(responseDTO);
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (Exception e) {
			String errMsg = "Error while withdraw Contract Ref. [" + withdrawContractDTO.getContractID() + "]" + e.getMessage();
			LOG.error(errMsg, e);	
			throw new WsReponseException(EResponseStatus.KO, errMsg);
		}
	}	
	
}
