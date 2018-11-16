package com.nokor.efinance.ws.resource.config.credit;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;

import com.nokor.efinance.core.financial.model.CreditControlItem;
import com.nokor.efinance.core.shared.credit.CreditControlEntityField;
import com.nokor.efinance.share.credit.CreditControlDTO;
import com.nokor.efinance.ws.FinResourceSrvRsc;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;
/**
 * 
 * @author buntha.chea
 *
 */
@Path("/configs/credits/controls")
public class CreditControlSrvRsc extends FinResourceSrvRsc implements CreditControlEntityField {
	
	/**
	 * GET LIST
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list() {		
		try {				
			List<CreditControlItem> creditControls = ENTITY_SRV.list(CreditControlItem.class);
			List<CreditControlDTO> creditControlDTOs = new ArrayList<>();
			creditControlDTOs.add(toCreditControlDTO(creditControls));
		
			return ResponseHelper.ok(creditControlDTOs);
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
	 * 
	 * @param creditControl
	 * @return
	 */
	private CreditControlDTO toCreditControlDTO(List<CreditControlItem> creditControls) {
		CreditControlDTO creditControlDTO = new CreditControlDTO();
		for (CreditControlItem creditControl : creditControls) {
			if (BLACKLISTED.equals(creditControl.getCode())) {
				creditControlDTO.setIsBacklistActivated(EStatusRecord.isActive(creditControl.getStatusRecord()));
				creditControlDTO.setIsBacklistBorrower(Boolean.valueOf(creditControl.getValue1()));
				creditControlDTO.setIsBacklistGuarantor(Boolean.valueOf(creditControl.getValue2()));
				creditControlDTO.setIsBacklistReference(Boolean.valueOf(creditControl.getValue3()));
			} else if (SUSPICIUS_PHONE_NUMBER.equals(creditControl.getCode())) {
				creditControlDTO.setIsSuspiciousPhoneNumberActivated(EStatusRecord.isActive(creditControl.getStatusRecord()));
			} else if (EMPLOYMENT.equals(creditControl.getCode())) {
				creditControlDTO.setIsEmploymentActiveated(EStatusRecord.isActive(creditControl.getStatusRecord()));
			} else if (NET_INCOME.equals(creditControl.getCode())) {
				creditControlDTO.setIsNetIncomeActivated(EStatusRecord.isActive(creditControl.getStatusRecord()));
				creditControlDTO.setNetIncomeLimit(creditControl.getValue1());
			} else if (AGE.equals(creditControl.getCode())) {
				creditControlDTO.setIsAgeActivated(EStatusRecord.isActive(creditControl.getStatusRecord()));
				creditControlDTO.setMinAge(creditControl.getValue1());
				creditControlDTO.setMaxAge(creditControl.getValue2());
			} else if (RESIDENCE_A_THE_REGISTRATION_ADDRESS.equals(creditControl.getCode())) {
				creditControlDTO.setIsResidenceRegistrationAddressActivated(EStatusRecord.isActive(creditControl.getStatusRecord()));
				creditControlDTO.setMinMonth(creditControl.getValue1());
			} else if (LOAN_TO_INCOME.equals(creditControl.getCode())) {
				creditControlDTO.setIsLoanToIncome(EStatusRecord.isActive(creditControl.getStatusRecord()));
				creditControlDTO.setPaymentIncomeLimit(creditControl.getValue1());
			} else if (NUMBER_OF_ACTIVIVE_LOAN.equals(creditControl.getCode())) {
				creditControlDTO.setIsNumberActiviveLoans(EStatusRecord.isActive(creditControl.getStatusRecord()));
				creditControlDTO.setMaxActiveLoan(creditControl.getValue1());
			} else if (DELINQUENCY_CHECK.equals(creditControl.getCode())) {
				creditControlDTO.setIsDelinquencyCheckActivated(EStatusRecord.isActive(creditControl.getStatusRecord()));
				creditControlDTO.setMinorDPD(creditControl.getValue1());
				creditControlDTO.setMajorDPD(creditControl.getValue2());
			} else if (MAXIMUM_CUMULATED_DEBT_RATION.equals(creditControl.getCode())) {
				creditControlDTO.setIsMaximumCumulatedDebtRatioActivated(EStatusRecord.isActive(creditControl.getStatusRecord()));
			} 
		}
		
		return creditControlDTO;
	}
}
