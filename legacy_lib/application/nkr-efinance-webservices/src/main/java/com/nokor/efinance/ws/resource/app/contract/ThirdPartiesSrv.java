package com.nokor.efinance.ws.resource.app.contract;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.MyMathUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.exception.EntityAlreadyExistsException;
import org.seuksa.frmk.tools.exception.EntityCreationException;
import org.seuksa.frmk.tools.exception.EntityNotValidParameterException;
import org.seuksa.frmk.tools.exception.EntityUpdateException;

import com.nokor.efinance.core.actor.model.ThirdParty;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.MContract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.model.cashflow.ETreasuryType;
import com.nokor.efinance.core.contract.service.cashflow.impl.CashflowUtils;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.efinance.core.payment.model.PaymentThirdParty;
import com.nokor.efinance.share.contract.CashflowDTO;
import com.nokor.efinance.ws.FinResourceSrvRsc;
import com.nokor.ersys.finance.accounting.model.JournalEvent;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;


/**
 * 
 * @author uhout.cheng
 */
@Path("/thirdparties")
public class ThirdPartiesSrv extends FinResourceSrvRsc {
	
	
	/**
	 * Create contract
	 * @param contractDTO
	 * @return
	 */
	@POST
	@Path("/cashflows")
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response createCashflow(CashflowDTO cashflowDTO) {
		try {
			if (StringUtils.isEmpty(cashflowDTO.getPayeeLastName())) {
				throw new EntityNotValidParameterException("Payee Lastname could not be empty");
			}
			if (StringUtils.isEmpty(cashflowDTO.getContractNo())) {
				throw new EntityNotValidParameterException("Contract No could not be empty");
			}
			if (StringUtils.isEmpty(cashflowDTO.getEventCode())) {
				throw new EntityNotValidParameterException("Event could not be empty");
			}
			if (cashflowDTO.getPaymentType() == null) {
				throw new EntityNotValidParameterException("Payment type could not be empty");
			}
			if (cashflowDTO.getTiAmount() == null || cashflowDTO.getTiAmount().doubleValue() <= 0) {
				throw new EntityNotValidParameterException("Amount could not be ZERO");
			}
			
			LOG.debug("Contract Ref. [" + cashflowDTO.getPayeeReference() + "]");
			Contract contract = CONT_SRV.getByReference(cashflowDTO.getContractNo());
			if (contract == null) {
				String errMsg = "Contract [" + cashflowDTO.getContractNo() + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));			
			}
			
			JournalEvent journalEvent = CONT_SRV.getByCode(JournalEvent.class, cashflowDTO.getEventCode());
			if (journalEvent == null) {
				String errMsg = "Journal Event [" + cashflowDTO.getEventCode() + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));			
			}
			
			Amount amount = MyMathUtils.calculateFromAmountIncl(cashflowDTO.getTiAmount(), cashflowDTO.getVat());
			
			Cashflow cashflow = CashflowUtils.createCashflow(contract.getProductLine(), 
					null, 
					contract, 
					cashflowDTO.getVat(), 
					ECashflowType.THI, 
					ETreasuryType.PRO, 
					journalEvent,
					EPaymentMethod.CASH,
					amount.getTeAmount(), 
					amount.getVatAmount(), 
					amount.getTiAmount(), 
					cashflowDTO.getInstallmentDate(), 
					cashflowDTO.getInstallmentDate(), 
					cashflowDTO.getInstallmentDate(), 
					null);
			cashflow.setApplyPenalty(cashflow.isApplyPenalty());
			
			CONT_SRV.saveOrUpdate(cashflow);
			
			PaymentThirdParty paymentThirdParty = PaymentThirdParty.createInstance();
			paymentThirdParty.setCashflow(cashflow);
			paymentThirdParty.setPaymentType(cashflowDTO.getPaymentType());
			
			ThirdParty thirdParty = CONT_SRV.getByField(ThirdParty.class, MContract.REFERENCE, cashflowDTO.getPayeeReference());
			
			if (thirdParty == null) {
				thirdParty = ThirdParty.createInstance();				
			}
			thirdParty.setFirstName(cashflowDTO.getPayeeFirstName());
			thirdParty.setLastName(cashflowDTO.getPayeeLastName());
			thirdParty.setReference(cashflowDTO.getPayeeReference());
			CONT_SRV.saveOrUpdate(thirdParty);				
			
			paymentThirdParty.setThirdParty(thirdParty);
			
			CONT_SRV.saveOrUpdate(paymentThirdParty);
			return ResponseHelper.ok();
			
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
	
}
