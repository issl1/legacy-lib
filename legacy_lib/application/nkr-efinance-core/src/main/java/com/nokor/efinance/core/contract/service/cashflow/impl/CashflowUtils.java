package com.nokor.efinance.core.contract.service.cashflow.impl;

import java.util.Date;
import java.util.List;

import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyMathUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.model.cashflow.ETreasuryType;
import com.nokor.efinance.core.financial.model.CreditLine;
import com.nokor.efinance.core.financial.model.EProductLineType;
import com.nokor.efinance.core.financial.model.ProductLine;
import com.nokor.efinance.core.payment.model.EPaymentCondition;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.ersys.finance.accounting.model.JournalEvent;

public class CashflowUtils {

	/**
	 * @param productLine
	 * @param creditLine
	 * @param contract
	 * @param cashfolwType
	 * @param treasuryType
	 * @param paymentCondition
	 * @param teAmount
	 * @param vatAmount
	 * @param tiAmount
	 * @param intallmentDate
	 * @param numInstallment
	 * @return
	 */
	public static Cashflow createCashflow(ProductLine productLine,
			CreditLine creditLine, Contract contract, double vatValue,
			ECashflowType cashfolwType, ETreasuryType treasuryType, JournalEvent journalEvent, EPaymentCondition paymentCondition, 
			Double teAmount, Double vatAmount, Double tiAmount,
			Date intallmentDate, Date periodStartDate, Date periodEndDate, Integer numInstallment) {
		EntityService entityService = SpringUtils.getBean(EntityService.class);
		EPaymentMethod paymentMethod = (paymentCondition == null || paymentCondition.getPaymentMethod() == null) ? entityService.getByCode(EPaymentMethod.class, "CASH") : paymentCondition.getPaymentMethod();
		return createCashflow(productLine, 
				creditLine, contract, vatValue, cashfolwType, treasuryType, journalEvent, paymentMethod, 
				teAmount, vatAmount, tiAmount, intallmentDate, periodStartDate, periodEndDate, numInstallment);
	}
	
	/**
	 * @param productLine
	 * @param creditLine
	 * @param contract
	 * @param cashfolwType
	 * @param treasuryType
	 * @param paymentMethod
	 * @param teAmount
	 * @param vatAmount
	 * @param tiAmount
	 * @param intallmentDate
	 * @param numInstallment
	 * @return
	 */
	public static Cashflow createCashflow(ProductLine productLine,
			CreditLine creditLine, Contract contract, double vatValue,
			ECashflowType cashfolwType, ETreasuryType treasuryType, JournalEvent journalEvent, EPaymentMethod paymentMethod, 
			Double teAmount, Double vatAmount, Double tiAmount,
			Date intallmentDate, Date periodStartDate, Date periodEndDate, Integer numInstallment) {
		return createCashflow(EProductLineType.FNC, productLine, 
				creditLine, contract, vatValue,
				cashfolwType, treasuryType, journalEvent, paymentMethod, 
				teAmount, vatAmount, tiAmount, intallmentDate, periodStartDate, periodEndDate, numInstallment);
	}
	
	/**
	 * @param productLineType
	 * @param productLine
	 * @param creditLine
	 * @param contract
	 * @param cashfolwType
	 * @param treasuryType
	 * @param paymentMethod
	 * @param teAmount
	 * @param vatAmount
	 * @param tiAmount
	 * @param intallmentDate
	 * @param numInstallment
	 * @return
	 */
	public static Cashflow createCashflow(EProductLineType productLineType, ProductLine productLine,
			CreditLine creditLine, Contract contract, double vatValue,
			ECashflowType cashfolwType, ETreasuryType treasuryType, JournalEvent journalEvent, EPaymentMethod paymentMethod, 
			Double teAmount, Double vatAmount, Double tiAmount,
			Date intallmentDate, Date periodStartDate, Date periodEndDate, Integer numInstallment) {
		Cashflow cashflow = new Cashflow();
		cashflow.setCashflowType(cashfolwType);
		cashflow.setTreasuryType(treasuryType);
		cashflow.setProductLineType(productLineType);
		cashflow.setProductLine(productLine);
		cashflow.setContract(contract);
		cashflow.setPaymentMethod(paymentMethod);
		cashflow.setVatValue(vatValue);
		cashflow.setTiInstallmentAmount(MyMathUtils.roundAmountTo(tiAmount));
		cashflow.setVatInstallmentAmount(MyMathUtils.roundAmountTo(vatAmount));
		cashflow.setTeInstallmentAmount(MyMathUtils.roundAmountTo(teAmount));
		cashflow.setInstallmentDate(DateUtils.getDateAtBeginningOfDay(intallmentDate));
		cashflow.setPeriodStartDate(DateUtils.getDateAtBeginningOfDay(periodStartDate));
		cashflow.setPeriodEndDate(DateUtils.getDateAtBeginningOfDay(periodEndDate));
		cashflow.setNumInstallment(numInstallment);
		cashflow.setJournalEvent(journalEvent);
		return cashflow;
	}
	
	/**
	 * check the duplicate cashflow if exist 
	 * 
	 * @param cashflows
	 * @param cashflow
	 * @return
	 */
	public static boolean isCashflowDuplicate(List<Cashflow> cashflows, Cashflow cashflow) {
		boolean result = false;
		if (cashflow == null || cashflows == null || cashflows.isEmpty()) {
			return result;
		}
		
		for (Cashflow caflw : cashflows) {
			boolean isNumInstallment = caflw.getNumInstallment() == cashflow.getNumInstallment();
			boolean isCashflowType = caflw.getCashflowType() == cashflow.getCashflowType();
			boolean isCashflowDuplicateNumInstallmentType = isNumInstallment && isCashflowType;
			if (isCashflowDuplicateNumInstallmentType) {
				if (cashflow.getCashflowType().equals(ECashflowType.FEE)) {
					if (caflw.getService().getId() == cashflow.getService().getId()) {
						result = true;
						break;
					}
				} else {
					result = true;
					break;
				}
			}
		}
		return result;
	}
}
