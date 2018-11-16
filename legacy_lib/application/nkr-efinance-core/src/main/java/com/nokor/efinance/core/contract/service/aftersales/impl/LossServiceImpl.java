package com.nokor.efinance.core.contract.service.aftersales.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractAdjustment;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowCode;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.model.cashflow.ETreasuryType;
import com.nokor.efinance.core.contract.service.ContractService;
import com.nokor.efinance.core.contract.service.ContractUtils;
import com.nokor.efinance.core.contract.service.aftersales.EarlySettlementService;
import com.nokor.efinance.core.contract.service.aftersales.EarlySettlementSimulateRequest;
import com.nokor.efinance.core.contract.service.aftersales.EarlySettlementSimulateResponse;
import com.nokor.efinance.core.contract.service.aftersales.LossSaveRequest;
import com.nokor.efinance.core.contract.service.aftersales.LossSaveResponse;
import com.nokor.efinance.core.contract.service.aftersales.LossService;
import com.nokor.efinance.core.contract.service.aftersales.LossSimulateRequest;
import com.nokor.efinance.core.contract.service.aftersales.LossSimulateResponse;
import com.nokor.efinance.core.contract.service.aftersales.LossValidateRequest;
import com.nokor.efinance.core.contract.service.aftersales.LossValidateResponse;
import com.nokor.efinance.core.contract.service.cashflow.impl.CashflowUtils;
import com.nokor.efinance.core.financial.model.FinService;
import com.nokor.efinance.core.financial.model.ProductLine;
import com.nokor.efinance.core.payment.model.EPaymentCondition;
import com.nokor.efinance.core.shared.accounting.InsuranceIncome;
import com.nokor.efinance.core.shared.accounting.LeaseTransaction;
import com.nokor.efinance.core.shared.accounting.ReferalFee;
import com.nokor.efinance.core.shared.accounting.ServicingIncome;
import com.nokor.efinance.core.workflow.AuctionWkfStatus;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.efinance.glf.accounting.service.GLFLeasingAccountingService;
import com.nokor.efinance.third.finwiz.client.los.ClientLOSApplication;
import com.nokor.ersys.finance.accounting.model.JournalEvent;

/**
 * 
 * @author youhort.ly
 *
 */
@Service("lossService")
public class LossServiceImpl extends BaseEntityServiceImpl implements LossService {
	/** */
	private static final long serialVersionUID = -7054001806232224584L;

	@Autowired
    private EntityDao dao;
	
	@Autowired
	private ContractService contractService;
	
	@Autowired
	private EarlySettlementService earlySettlementService;
		
	@Autowired
	private GLFLeasingAccountingService accountingService;
	
	@Override
	public EntityDao getDao() {
		return dao;
	}
	
	/**
	 * @param request
	 * @return
	 */
	public LossSimulateResponse simulate(LossSimulateRequest request) {
		
		EarlySettlementSimulateRequest earlySettlementSimulateRequest = new EarlySettlementSimulateRequest();
		earlySettlementSimulateRequest.setCotraId(request.getCotraId());
		earlySettlementSimulateRequest.setEventDate(request.getEventDate());
		earlySettlementSimulateRequest.setIncludePenalty(false);		
		EarlySettlementSimulateResponse earlySettlementSimulateResponse = earlySettlementService.simulate(earlySettlementSimulateRequest);
		
		LossSimulateResponse response = new LossSimulateResponse();
		
/*		Amount adjustmentInterest = earlySettlementSimulateResponse.getAdjustmentInterest();
		adjustmentInterest.plus(earlySettlementSimulateResponse.getTotalInterest());
		
		response.setCotraId(request.getCotraId());
		response.setEventDate(request.getEventDate());
		response.setTotalPrincipal(earlySettlementSimulateResponse.getTotalPrincipal());
		response.setTotalOther(earlySettlementSimulateResponse.getTotalOther());
		response.setInsuranceFee(earlySettlementSimulateResponse.getInsuranceFee());
		response.setServicingFee(earlySettlementSimulateResponse.getServicingFee());
		response.setTotalInterest(adjustmentInterest);
		response.setCashflows(earlySettlementSimulateResponse.getCashflows());*/
		
		return response;
	}
	
	/**
	 * @param request
	 * @return
	 */
	public LossSaveResponse save(LossSaveRequest request) {
		LossSaveResponse lossSaveResponse = new LossSaveResponse();
		return lossSaveResponse;
	}
	
	/**
	 * @param request
	 */
	public LossValidateResponse validate(LossValidateRequest request) {
		Contract contract = getById(Contract.class, request.getCotraId());
		
		ContractAdjustment contractAdjustment = contract.getContractAdjustment();
		if (contractAdjustment == null) {
			contractAdjustment = new ContractAdjustment();
			contractAdjustment.setContract(contract);
		}
		
		ProductLine productLine = getById(ProductLine.class, contract.getProductLine().getId());
		Integer term = contract.getTerm();
		
		Date lossDate = request.getEventDate();
		EPaymentCondition lossPaymentCondition = productLine.getPaymentConditionLoss();
		
		List<Cashflow> lossCashflows = new ArrayList<>();
		
		Cashflow cashflowCAP = CashflowUtils.createCashflow(productLine, null,
				contract, contract.getVatValue(), ECashflowType.CAP, ETreasuryType.APP, getByCode(JournalEvent.class, ECashflowType.CAP_JOURNAL_EVENT),
				lossPaymentCondition, request.getTotalPrincipal().getTeAmount(), request.getTotalPrincipal().getVatAmount(), 
				request.getTotalPrincipal().getTiAmount(), lossDate, lossDate, lossDate, term);
		cashflowCAP.setCashflowCode(ECashflowCode.LOS);
		cashflowCAP.setPaid(true);
		saveOrUpdate(cashflowCAP);
		lossCashflows.add(cashflowCAP);
		
		if (MyNumberUtils.getDouble(request.getTotalInterest().getTeAmount()) > 0) {
			Cashflow cashflowIAP = CashflowUtils.createCashflow(productLine, null,
						contract, contract.getVatValue(), ECashflowType.IAP, ETreasuryType.APP, getByCode(JournalEvent.class, ECashflowType.IAP_JOURNAL_EVENT),
						lossPaymentCondition, request.getTotalInterest().getTeAmount(), request.getTotalInterest().getVatAmount(),
						request.getTotalInterest().getTiAmount(), lossDate, lossDate, lossDate, term);
			cashflowIAP.setCashflowCode(ECashflowCode.LOS);
			cashflowIAP.setPaid(true);
			saveOrUpdate(cashflowIAP);
			lossCashflows.add(cashflowIAP);
		}
				
		if (MyNumberUtils.getDouble(request.getInsuranceFee().getTeAmount()) > 0) {
			Cashflow cashflowFEEINS = CashflowUtils.createCashflow(productLine, null,
					contract, contract.getVatValue(), ECashflowType.FEE, ETreasuryType.APP, null,
					lossPaymentCondition, request.getInsuranceFee().getTeAmount(), request.getInsuranceFee().getVatAmount(),
					request.getInsuranceFee().getTiAmount(), lossDate, lossDate, lossDate, term);
			FinService insService = getByCode(FinService.class, "INSFEE");
			cashflowFEEINS.setService(insService);
			cashflowFEEINS.setCashflowCode(ECashflowCode.LOS);
			cashflowFEEINS.setPaid(true);
			cashflowFEEINS.setJournalEvent(insService.getJournalEvent());
			saveOrUpdate(cashflowFEEINS);
			lossCashflows.add(cashflowFEEINS);
			
			contractAdjustment.setTiUnpaidUnearnedInsuranceIncomeUsd(request.getInsuranceFee().getTiAmount());
			contractAdjustment.setVatUnpaidUnearnedInsuranceIncomeUsd(request.getInsuranceFee().getVatAmount());
			contractAdjustment.setTeUnpaidUnearnedInsuranceIncomeUsd(request.getInsuranceFee().getTeAmount());
		}
		
		if (MyNumberUtils.getDouble(request.getServicingFee().getTeAmount()) > 0) {
			Cashflow cashflowFEESER = CashflowUtils.createCashflow(productLine, null,
					contract, contract.getVatValue(), ECashflowType.FEE, ETreasuryType.APP, null,
					lossPaymentCondition, request.getServicingFee().getTeAmount(), request.getServicingFee().getVatAmount(),
					request.getServicingFee().getTiAmount(), lossDate, lossDate, lossDate, term);
			FinService feeService = getByCode(FinService.class, "SERFEE");
			cashflowFEESER.setService(feeService);
			cashflowFEESER.setCashflowCode(ECashflowCode.LOS);
			cashflowFEESER.setPaid(true);
			cashflowFEESER.setJournalEvent(feeService.getJournalEvent());
			saveOrUpdate(cashflowFEESER);
			lossCashflows.add(cashflowFEESER);
			
			contractAdjustment.setTiUnpaidUnearnedServicingIncomeUsd(request.getServicingFee().getTiAmount());
			contractAdjustment.setVatUnpaidUnearnedServicingIncomeUsd(request.getServicingFee().getVatAmount());
			contractAdjustment.setTeUnpaidUnearnedServicingIncomeUsd(request.getServicingFee().getTeAmount());
		}
		
		if (MyNumberUtils.getDouble(request.getTotalOther().getTeAmount()) > 0) {
			Cashflow cashflowFEE = CashflowUtils.createCashflow(productLine, null,
					contract, contract.getVatValue(), ECashflowType.FEE, ETreasuryType.APP, null, 
					lossPaymentCondition, request.getTotalOther().getTeAmount(), request.getTotalOther().getVatAmount(),
					request.getTotalOther().getTiAmount(), lossDate, lossDate, lossDate, term);
			cashflowFEE.setCashflowCode(ECashflowCode.LOS);
			cashflowFEE.setPaid(true);
			saveOrUpdate(cashflowFEE);
			lossCashflows.add(cashflowFEE);
		}
		
		for (Cashflow cashflow : request.getCashflows()) {
			if (!cashflow.isCancel() && !cashflow.isPaid() && !cashflow.isUnpaid()) {
				cashflow.setCancel(true);
				cashflow.setCancelationDate(DateUtils.today());
				cashflow.setCashflowCode(ECashflowCode.LOS);
				saveOrUpdate(cashflow);
			}
		}
				
		contractAdjustment.setTiAdjustmentPrincipal(request.getTotalPrincipal().getTiAmount());
		contractAdjustment.setVatAdjustmentPrincipal(request.getTotalPrincipal().getVatAmount());
		contractAdjustment.setTeAdjustmentPrincipal(request.getTotalPrincipal().getTeAmount());
		contractAdjustment.setTiAdjustmentInterest(request.getTotalInterest().getTiAmount());
		contractAdjustment.setTeAdjustmentInterest(request.getTotalInterest().getTeAmount());
		contractAdjustment.setVatAdjustmentInterest(request.getTotalInterest().getVatAmount());
		
		if (request.getWkfStatus().equals(ContractWkfStatus.REP)) {
			contract.setWkfStatus(AuctionWkfStatus.EVA);
		}
				
		Date calculDate = DateUtils.addDaysDate(request.getEventDate(), -1);
		List<LeaseTransaction> leaseTransactions = accountingService.getLeaseTransactions(null, contract.getDealer(), contract.getReference(), 
				DateUtils.getDateAtBeginningOfMonth(calculDate), calculDate);
		if (leaseTransactions != null && leaseTransactions.size() == 1) {
			LeaseTransaction leaseTransaction = leaseTransactions.get(0);
			contractAdjustment.setTiBalanceInterestInSuspendUsd(leaseTransaction.getInterestInSuspendCumulated().getTiAmount());
			contractAdjustment.setVatBalanceInterestInSuspendUsd(leaseTransaction.getInterestInSuspendCumulated().getVatAmount());
			contractAdjustment.setTeBalanceInterestInSuspendUsd(leaseTransaction.getInterestInSuspendCumulated().getTeAmount());
			contractAdjustment.setTiUnpaidAccruedInterestReceivableUsd(leaseTransaction.getInterestReceivable().getTiAmount());
			contractAdjustment.setVatUnpaidAccruedInterestReceivableUsd(leaseTransaction.getInterestReceivable().getVatAmount());
			contractAdjustment.setTeUnpaidAccruedInterestReceivableUsd(leaseTransaction.getInterestReceivable().getTeAmount());
		}
		
		List<ReferalFee> referalFees = accountingService.getReferalFees(null, contract.getDealer(), contract.getReference(), 
				DateUtils.getDateAtBeginningOfMonth(calculDate), calculDate);
		if (referalFees != null && referalFees.size() == 1) {
			ReferalFee referalFee = referalFees.get(0);
			contractAdjustment.setTiUnpaidDeferredCommissionReferalFeeUsd(referalFee.getDeferredCommissionReferalFee().getTiAmount());
			contractAdjustment.setVatUnpaidDeferredCommissionReferalFeeUsd(referalFee.getDeferredCommissionReferalFee().getVatAmount());
			contractAdjustment.setTeUnpaidDeferredCommissionReferalFeeUsd(referalFee.getDeferredCommissionReferalFee().getTeAmount());
			contractAdjustment.setTiUnpaidAcrrualExpensesReferalFeeUsd(referalFee.getAcrrualExpenses().getTiAmount());
			contractAdjustment.setVatUnpaidAcrrualExpensesReferalFeeUsd(referalFee.getAcrrualExpenses().getVatAmount());
			contractAdjustment.setTeUnpaidAcrrualExpensesReferalFeeUsd(referalFee.getAcrrualExpenses().getTeAmount());
		}
		
		List<InsuranceIncome> insuranceIncomes = accountingService.getInsuranceIncomes(null, contract.getDealer(), contract.getReference(), 
				DateUtils.getDateAtBeginningOfMonth(calculDate), calculDate);
		if (insuranceIncomes != null && insuranceIncomes.size() == 1) {
			InsuranceIncome insuranceIncome = insuranceIncomes.get(0);
			contractAdjustment.setTiBalanceInsuranceIncomeInSuspendUsd(insuranceIncome.getInsuranceIncomeInSuspendCumulated().getTiAmount());
			contractAdjustment.setVatBalanceInsuranceIncomeInSuspendUsd(insuranceIncome.getInsuranceIncomeInSuspendCumulated().getVatAmount());
			contractAdjustment.setTeBalanceInsuranceIncomeInSuspendUsd(insuranceIncome.getInsuranceIncomeInSuspendCumulated().getTeAmount());
			contractAdjustment.setTiUnpaidAccrualReceivableInsuranceIncomeUsd(insuranceIncome.getAccountReceivable().getTiAmount());
			contractAdjustment.setVatUnpaidAccrualReceivableInsuranceIncomeUsd(insuranceIncome.getAccountReceivable().getVatAmount());
			contractAdjustment.setTeUnpaidAccrualReceivableInsuranceIncomeUsd(insuranceIncome.getAccountReceivable().getTeAmount());
		}
		
		if (request.getWkfStatus().equals(ContractWkfStatus.WRI) || request.getWkfStatus().equals(ContractWkfStatus.REP)) {
			List<ServicingIncome> servicingIncomes = accountingService.getServicingIncomes(null, contract.getDealer(), contract.getReference(), 
					DateUtils.getDateAtBeginningOfMonth(calculDate), calculDate);
			if (servicingIncomes != null && servicingIncomes.size() == 1) {
				ServicingIncome servicingIncome = servicingIncomes.get(0);
				contractAdjustment.setTiBalanceServicingIncomeInSuspendUsd(servicingIncome.getServicingIncomeInSuspendCumulated().getTiAmount());
				contractAdjustment.setVatBalanceServicingIncomeInSuspendUsd(servicingIncome.getServicingIncomeInSuspendCumulated().getVatAmount());
				contractAdjustment.setTeBalanceServicingIncomeInSuspendUsd(servicingIncome.getServicingIncomeInSuspendCumulated().getTeAmount());
				contractAdjustment.setTiUnpaidAccrualReceivableServicingIncomeUsd(servicingIncome.getAccountReceivable().getTiAmount());
				contractAdjustment.setVatUnpaidAccrualReceivableServicingIncomeUsd(servicingIncome.getAccountReceivable().getVatAmount());
				contractAdjustment.setTeUnpaidAccrualReceivableServicingIncomeUsd(servicingIncome.getAccountReceivable().getTeAmount());
			}
		}
		
		saveOrUpdate(contractAdjustment);
		
		contractService.changeContractStatus(contract, request.getWkfStatus(), request.getEventDate());
		LossValidateResponse response = new LossValidateResponse();
		
		// Update application to in-active
		ClientLOSApplication.updateUnActiveApplication(ContractUtils.getApplicationID(contract));
		
		return response;
	}

}
