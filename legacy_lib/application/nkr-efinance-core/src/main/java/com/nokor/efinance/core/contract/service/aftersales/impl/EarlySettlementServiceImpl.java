package com.nokor.efinance.core.contract.service.aftersales.impl;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyMathUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.efinance.core.aftersale.EAfterSaleEventType;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.LockSplit;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowCode;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.model.cashflow.ETreasuryType;
import com.nokor.efinance.core.contract.service.ContractService;
import com.nokor.efinance.core.contract.service.aftersales.EarlySettlementSaveRequest;
import com.nokor.efinance.core.contract.service.aftersales.EarlySettlementSaveResponse;
import com.nokor.efinance.core.contract.service.aftersales.EarlySettlementService;
import com.nokor.efinance.core.contract.service.aftersales.EarlySettlementSimulateRequest;
import com.nokor.efinance.core.contract.service.aftersales.EarlySettlementSimulateResponse;
import com.nokor.efinance.core.contract.service.aftersales.EarlySettlementValidateRequest;
import com.nokor.efinance.core.contract.service.aftersales.EarlySettlementValidateResponse;
import com.nokor.efinance.core.contract.service.cashflow.impl.CashflowUtils;
import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.efinance.core.financial.model.FinService;
import com.nokor.efinance.core.payment.service.LockSplitService;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.ersys.finance.accounting.model.JournalEvent;

/**
 * 
 * @author prasnar
 *
 */
@Service("earlySettlementService")
public class EarlySettlementServiceImpl extends BaseEntityServiceImpl implements EarlySettlementService, FMEntityField {
	/** */
	private static final long serialVersionUID = 7726232467770673141L;
	
	@Autowired
    private EntityDao dao;
	
	@Autowired
	private ContractService contractService;
	
	@Autowired
	private LockSplitService lockSplitService;
			
	/**
	 * @see org.seuksa.frmk.service.impl.BaseEntityServiceImpl#getDao()
	 */
	@Override
	public EntityDao getDao() {
		return dao;
	}
	
	/**
	 * @param request
	 * @return
	 */
	public EarlySettlementSimulateResponse simulate(EarlySettlementSimulateRequest request) {
		EarlySettlementSimulateResponse response = new EarlySettlementSimulateResponse();
		
		// AfterSaleEvent afterSaleEvent = getById(AfterSaleEvent.class, request.getAftEvtId());
		
		// Paid off - new rule based on %advance payment
		List<Cashflow> cashflows = contractService.getCashflows(request.getCotraId());
	
		Amount balanceCapital = new Amount();
		Amount balanceInterest = new Amount();
		Amount discountInterest = new Amount();
		Amount balancePenalty = new Amount();
		Amount balanceFollowingFee = new Amount();
		Amount balanceRepossessionFee = new Amount();
		Amount balanceCollectionFee = new Amount();
		Amount balanceOperationFee = new Amount();
		Amount balancePressingFee = new Amount();
		Amount balanceTransferFee = new Amount();
		Amount adjustmentInterest = new Amount();
					
		for (Cashflow cashflow : cashflows) {
			if (!cashflow.isCancel() && !cashflow.isPaid() && !cashflow.isUnpaid()) {
				
				
/*				PenaltyVO penaltyVo = contractService.calculatePenalty(contract, cashflow.getInstallmentDate(), request.getEventDate(), oustandingAmountUsd);
				if (penaltyVo != null && penaltyVo.getPenaltyAmount() != null) {
					totalPenalty.plusTiAmount(penaltyVo.getPenaltyAmount().getTiAmount());
					totalPenalty.plusTeAmount(penaltyVo.getPenaltyAmount().getTeAmount());
					totalPenalty.plusVatAmount(penaltyVo.getPenaltyAmount().getVatAmount());
				}*/
				
				if (cashflow.getCashflowType().equals(ECashflowType.CAP)) {
					balanceCapital.plusTiAmount(MyMathUtils.roundAmountTo(cashflow.getTiInstallmentAmount()));
					balanceCapital.plusTeAmount(MyMathUtils.roundAmountTo(cashflow.getTeInstallmentAmount()));
					balanceCapital.plusVatAmount(MyMathUtils.roundAmountTo(cashflow.getVatInstallmentAmount()));
				} else if (cashflow.getCashflowType().equals(ECashflowType.FEE)) {
					FinService service = cashflow.getService();
					if (EServiceType.COLFEE.equals(service.getServiceType())) {
						balanceCollectionFee.plusTiAmount(MyMathUtils.roundAmountTo(cashflow.getTiInstallmentAmount()));
						balanceCollectionFee.plusTeAmount(MyMathUtils.roundAmountTo(cashflow.getTeInstallmentAmount()));
						balanceCollectionFee.plusVatAmount(MyMathUtils.roundAmountTo(cashflow.getVatInstallmentAmount()));
					} else if (EServiceType.OPERFEE.equals(service.getServiceType())) {
						balanceOperationFee.plusTiAmount(MyMathUtils.roundAmountTo(cashflow.getTiInstallmentAmount()));
						balanceOperationFee.plusTeAmount(MyMathUtils.roundAmountTo(cashflow.getTeInstallmentAmount()));
						balanceOperationFee.plusVatAmount(MyMathUtils.roundAmountTo(cashflow.getVatInstallmentAmount()));
					} else if (EServiceType.TRANSFEE.equals(service.getServiceType())) {
						balanceTransferFee.plusTiAmount(MyMathUtils.roundAmountTo(cashflow.getTiInstallmentAmount()));
						balanceTransferFee.plusTeAmount(MyMathUtils.roundAmountTo(cashflow.getTeInstallmentAmount()));
						balanceTransferFee.plusVatAmount(MyMathUtils.roundAmountTo(cashflow.getVatInstallmentAmount()));
					} else if (EServiceType.REPOSFEE.equals(service.getServiceType())) {
						balanceRepossessionFee.plusTiAmount(MyMathUtils.roundAmountTo(cashflow.getTiInstallmentAmount()));
						balanceRepossessionFee.plusTeAmount(MyMathUtils.roundAmountTo(cashflow.getTeInstallmentAmount()));
						balanceRepossessionFee.plusVatAmount(MyMathUtils.roundAmountTo(cashflow.getVatInstallmentAmount()));
					} else if (EServiceType.PRESSFEE.equals(service.getServiceType())) {
						balancePressingFee.plusTiAmount(MyMathUtils.roundAmountTo(cashflow.getTiInstallmentAmount()));
						balancePressingFee.plusTeAmount(MyMathUtils.roundAmountTo(cashflow.getTeInstallmentAmount()));
						balancePressingFee.plusVatAmount(MyMathUtils.roundAmountTo(cashflow.getVatInstallmentAmount()));
					}
				}
			}
		}
				
		balanceInterest = contractService.getRealInterestBalance(request.getEventDate(), cashflows);
		
		if (balanceInterest != null && balanceInterest.getTiAmount() > 0) {
			
			discountInterest.plusTiAmount(MyMathUtils.roundAmountTo(balanceInterest.getTiAmount() / 2));
			discountInterest.plusTeAmount(MyMathUtils.roundAmountTo(balanceInterest.getTeAmount() / 2));
			discountInterest.plusVatAmount(MyMathUtils.roundAmountTo(balanceInterest.getVatAmount() / 2));
			
						
			adjustmentInterest.plusTiAmount(MyMathUtils.roundAmountTo(balanceInterest.getTiAmount() - discountInterest.getTiAmount()));
			adjustmentInterest.plusTeAmount(MyMathUtils.roundAmountTo(balanceInterest.getTeAmount() - discountInterest.getTeAmount()));
			adjustmentInterest.plusVatAmount(MyMathUtils.roundAmountTo(balanceInterest.getVatAmount() - discountInterest.getVatAmount()));
		}
		
		
		response.setCotraId(request.getCotraId());
		response.setEventDate(request.getEventDate());
		response.setBalanceCapital(balanceCapital);
		response.setBalanceCollectionFee(balanceCollectionFee);
		response.setBalanceFollowingFee(balanceFollowingFee);
		response.setBalanceInterest(balanceInterest);
		response.setBalanceInterest(discountInterest);
		response.setBalanceOperationFee(balanceOperationFee);
		response.setBalancePenalty(balancePenalty);
		response.setBalancePressingFee(balancePressingFee);
		response.setBalanceRepossessionFee(balanceRepossessionFee);
		response.setBalanceTransferFee(balanceTransferFee);
		response.setAdjustmentInterest(adjustmentInterest);
		response.setCashflows(cashflows);
		return response;
	}
	
	/**
	 * @param request
	 * @return
	 */
	public EarlySettlementSaveResponse save(EarlySettlementSaveRequest request) {
		EarlySettlementSaveResponse earlySettlementSaveResponse = new EarlySettlementSaveResponse();
		
		LockSplit lockSplit = new LockSplit();
		lockSplit.setContract(getById(Contract.class, request.getCotraId()));
		lockSplit.setFrom(request.getEarlySettlementDate());
		lockSplit.setTo(request.getEarlySettlementDate());
		lockSplit.setWhiteClearance(true);
		lockSplit.setAfterSaleEventType(EAfterSaleEventType.EARLYSETTLEMENT);
				
/*		List<LockSplitItem> items = new ArrayList<>();
		lockSplitService.addLockSplitItem(lockSplit, items, ECashflowType.CAP, request.getTotalPrincipal());
		lockSplitService.addLockSplitItem(lockSplit, items, ECashflowType.IAP, request.getTotalInterest());
		if (MyNumberUtils.getDouble(request.getTotalPenalty().getTiAmount()) > 0) {
			lockSplitService.addLockSplitItem(lockSplit, items, ECashflowType.PEN, request.getTotalPenalty());
		}
		
		if (MyNumberUtils.getDouble(request.getInsuranceFee().getTiAmount()) > 0) {
			lockSplitService.addLockSplitItem(lockSplit, items, ECashflowType.FEE, getByCode(FinService.class, EServiceType.INSFEE.getCode()), request.getInsuranceFee());
		}
		
		if (MyNumberUtils.getDouble(request.getServicingFee().getTiAmount()) > 0) {
			lockSplitService.addLockSplitItem(lockSplit, items, ECashflowType.FEE, getByCode(FinService.class, EServiceType.SRVFEE.getCode()), request.getServicingFee());
		}*/
		
		// lockSplit.setItems(items);
		
		lockSplitService.saveLockSplit(lockSplit);
		
		earlySettlementSaveResponse.setLockSplit(lockSplit);
		return earlySettlementSaveResponse;
	}
	
		
	/**
	 * @param request
	 */
	public EarlySettlementValidateResponse validate(EarlySettlementValidateRequest request) {
		
		Contract contract = getById(Contract.class, request.getCotraId());
		Integer term = contract.getTerm();
		
		List<Cashflow> earlySettlementCashflows = new ArrayList<>();
		
		Cashflow cashflowCAP = CashflowUtils.createCashflow(contract.getProductLine(), null,
				contract, contract.getVatValue(), ECashflowType.CAP, ETreasuryType.APP, getByCode(JournalEvent.class, ECashflowType.CAP_JOURNAL_EVENT),
				contract.getProductLine().getPaymentConditionCap(), request.getBalanceCapital().getTeAmount(), request.getBalanceCapital().getVatAmount(), 
				request.getBalanceCapital().getTiAmount(), request.getEarlySettlementDate(), request.getEarlySettlementDate(), request.getEarlySettlementDate(), term);
		cashflowCAP.setCashflowCode(ECashflowCode.EAR);
		saveOrUpdate(cashflowCAP);
		earlySettlementCashflows.add(cashflowCAP);
		
		if (MyNumberUtils.getDouble(request.getBalanceInterest().getTeAmount()) > 0) {
			// Discount interest 50%
			request.setBalanceInterest(discount50Percent(request.getBalanceInterest()));
			Cashflow cashflowIAP = CashflowUtils.createCashflow(contract.getProductLine(), null,
						contract, contract.getVatValue(), ECashflowType.IAP, ETreasuryType.APP, getByCode(JournalEvent.class, ECashflowType.IAP_JOURNAL_EVENT),
						contract.getProductLine().getPaymentConditionIap(), request.getBalanceInterest().getTeAmount(), request.getBalanceInterest().getVatAmount(),
						request.getBalanceInterest().getTiAmount(), request.getEarlySettlementDate(), request.getEarlySettlementDate(), request.getEarlySettlementDate(), term);
			cashflowIAP.setCashflowCode(ECashflowCode.EAR);
			saveOrUpdate(cashflowIAP);
			earlySettlementCashflows.add(cashflowIAP);
		}
		
		if (MyNumberUtils.getDouble(request.getBalancePenalty().getTeAmount()) > 0) {
			Cashflow cashflowPER = CashflowUtils.createCashflow(contract.getProductLine(), null,
					contract, contract.getVatValue(), ECashflowType.PEN, ETreasuryType.APP, getByCode(JournalEvent.class, ECashflowType.PEN_JOURNAL_EVENT),
					contract.getProductLine().getPaymentConditionIap(), request.getBalancePenalty().getTeAmount(), request.getBalancePenalty().getVatAmount(),
					request.getBalancePenalty().getTiAmount(), request.getEarlySettlementDate(), request.getEarlySettlementDate(), request.getEarlySettlementDate(), term);
			cashflowPER.setCashflowCode(ECashflowCode.EAR);
			saveOrUpdate(cashflowPER);
			earlySettlementCashflows.add(cashflowPER);
		}
		
		for (Cashflow cashflow : request.getCashflows()) {
			if (!cashflow.isCancel() && !cashflow.isPaid() && !cashflow.isUnpaid()) {
				cashflow.setCancel(true);
				cashflow.setCancelationDate(DateUtils.today());
				cashflow.setCashflowCode(ECashflowCode.EAR);
				saveOrUpdate(cashflow);
			}
		}
				
		contractService.changeContractStatus(contract, ContractWkfStatus.EAR, request.getEarlySettlementDate());
		
		EarlySettlementValidateResponse response = new EarlySettlementValidateResponse();
		return response;
	}
	
	/**
	 * Discount amount 50%
	 * @param value
	 * @return
	 */
	private Amount discount50Percent(Amount value) {
		if (value == null) {
			return null;
		} else {
			Amount amount = new Amount();
			amount.setNbDecimal(value.getNbDecimal());
			if (value.getTiAmount() != null) {
				amount.setTiAmount(value.getTiAmount() / 2);
			}
			if (value.getTeAmount() != null) {
				amount.setTeAmount(value.getTeAmount() / 2);
			}
			if (value.getVatAmount() != null) {
				amount.setVatAmount(value.getVatAmount() / 2);
			}
			return amount;
		}
	}

}
