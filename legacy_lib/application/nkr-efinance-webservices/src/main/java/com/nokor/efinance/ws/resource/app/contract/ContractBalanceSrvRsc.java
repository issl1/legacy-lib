package com.nokor.efinance.ws.resource.app.contract;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.MyNumberUtils;

import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.efinance.share.common.BalanceAmountDTO;
import com.nokor.efinance.share.contract.ContractBalanceDTO;
import com.nokor.ersys.finance.accounting.model.JournalEvent;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * 
 * @author uhout.cheng
 */
@Path(ContractWsPath.CONTRACTS)
public class ContractBalanceSrvRsc extends BaseContractSrvRsc {
	
	/**
	 * List contract by reference
	 * @param reference
	 * @return
	 */
	@GET
	@Path("/{reference}" + ContractWsPath.BALANCE)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(@PathParam(ContractWsPath.REFERENCE) String reference) {
		try {
			LOG.debug("Contract-Ref. [" + reference + "]");
		
			Contract contract = CONT_SRV.getByReference(reference);
			
			if (contract == null) {
				String errMsg = "Contract [" + reference + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));			
			}
			
			return ResponseHelper.ok(toContractBalanceDTO(contract));
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
	 * 
	 * @param contract
	 * @return
	 */
	private ContractBalanceDTO toContractBalanceDTO(Contract contract) {
		ContractBalanceDTO contraBalance = new ContractBalanceDTO();
		Collection col = contract.getCollection();
		if (col != null) {
			contraBalance.setId(contract.getId());
			contraBalance.setContractID(contract.getReference());		
			contraBalance.setBalanceCapital(new BalanceAmountDTO(toJournalEventDTO(FIN_PROD_SRV.getByCode(JournalEvent.class, ECashflowType.CAP_JOURNAL_EVENT)), MyNumberUtils.getDouble(col.getTiBalanceCapital())));
			contraBalance.setBalanceInterest(new BalanceAmountDTO(toJournalEventDTO(FIN_PROD_SRV.getByCode(JournalEvent.class, ECashflowType.IAP_JOURNAL_EVENT)), MyNumberUtils.getDouble(col.getTiBalanceInterest())));
			contraBalance.setBalancePenalty(new BalanceAmountDTO(toJournalEventDTO(FIN_PROD_SRV.getByCode(JournalEvent.class, ECashflowType.PEN_JOURNAL_EVENT)), MyNumberUtils.getDouble(col.getTiPenaltyAmount())));
			contraBalance.setBalanceFollowingFee(new BalanceAmountDTO(toJournalEventDTO(FIN_PROD_SRV.getFinServiceByType(EServiceType.FOLLWFEE).getJournalEvent()), MyNumberUtils.getDouble(col.getTiFollowingFeeAmount())));
			contraBalance.setBalanceRepossessionFee(new BalanceAmountDTO(toJournalEventDTO(FIN_PROD_SRV.getFinServiceByType(EServiceType.REPOSFEE).getJournalEvent()), MyNumberUtils.getDouble(col.getTiBalanceRepossessionFee())));
			contraBalance.setBalanceCollectionFee(new BalanceAmountDTO(toJournalEventDTO(FIN_PROD_SRV.getFinServiceByType(EServiceType.COLFEE).getJournalEvent()), MyNumberUtils.getDouble(col.getTiBalanceCollectionFee())));
			contraBalance.setBalanceOperationFee(new BalanceAmountDTO(toJournalEventDTO(FIN_PROD_SRV.getFinServiceByType(EServiceType.OPERFEE).getJournalEvent()), MyNumberUtils.getDouble(col.getTiBalanceOperationFee())));
			contraBalance.setBalancePressingFee(new BalanceAmountDTO(toJournalEventDTO(FIN_PROD_SRV.getFinServiceByType(EServiceType.PRESSFEE).getJournalEvent()), MyNumberUtils.getDouble(col.getTiBalancePressingFee())));
			contraBalance.setBalanceTransferFee(new BalanceAmountDTO(toJournalEventDTO(FIN_PROD_SRV.getFinServiceByType(EServiceType.TRANSFEE).getJournalEvent()), MyNumberUtils.getDouble(col.getTiBalanceTransferFee())));
		}
		return contraBalance;
	}

}
