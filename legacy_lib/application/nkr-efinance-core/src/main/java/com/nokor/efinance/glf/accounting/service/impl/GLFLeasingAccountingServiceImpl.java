package com.nokor.efinance.glf.accounting.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.model.meta.NativeColumn;
import org.seuksa.frmk.model.meta.NativeRow;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyMathUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.exception.NativeQueryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.common.app.history.model.EHistoReason;
import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.common.app.workflow.model.WkfHistoryItem;
import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.applicant.model.Employment;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractAdjustment;
import com.nokor.efinance.core.contract.model.ContractApplicant;
import com.nokor.efinance.core.contract.model.ContractWkfHistoryItem;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowCode;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.service.ContractService;
import com.nokor.efinance.core.contract.service.cashflow.CashflowService;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.efinance.core.financial.service.FinanceCalculationService;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.QuotationService;
import com.nokor.efinance.core.shared.accounting.InsuranceExpense;
import com.nokor.efinance.core.shared.accounting.InsuranceExpenseSchedule;
import com.nokor.efinance.core.shared.accounting.InsuranceExpenseSchedules;
import com.nokor.efinance.core.shared.accounting.InsuranceIncome;
import com.nokor.efinance.core.shared.accounting.InsuranceIncomeAdjustment;
import com.nokor.efinance.core.shared.accounting.InsuranceIncomeSchedule;
import com.nokor.efinance.core.shared.accounting.InsuranceIncomeSchedules;
import com.nokor.efinance.core.shared.accounting.InterestIncomeSchedule;
import com.nokor.efinance.core.shared.accounting.InterestIncomeSchedules;
import com.nokor.efinance.core.shared.accounting.LeaseAdjustment;
import com.nokor.efinance.core.shared.accounting.LeaseTransaction;
import com.nokor.efinance.core.shared.accounting.LeasesReport;
import com.nokor.efinance.core.shared.accounting.ReferalFee;
import com.nokor.efinance.core.shared.accounting.ReferalFeeAdjustment;
import com.nokor.efinance.core.shared.accounting.ReferalFeeSchedule;
import com.nokor.efinance.core.shared.accounting.ReferalFeeSchedules;
import com.nokor.efinance.core.shared.accounting.RegistrationExpense;
import com.nokor.efinance.core.shared.accounting.RegistrationExpenseSchedule;
import com.nokor.efinance.core.shared.accounting.RegistrationExpenseSchedules;
import com.nokor.efinance.core.shared.accounting.ServiceCalculation;
import com.nokor.efinance.core.shared.accounting.ServiceIncomeSchedule;
import com.nokor.efinance.core.shared.accounting.ServiceIncomeSchedules;
import com.nokor.efinance.core.shared.accounting.ServiceTransaction;
import com.nokor.efinance.core.shared.accounting.ServicingIncome;
import com.nokor.efinance.core.shared.accounting.ServicingIncomeAdjustment;
import com.nokor.efinance.core.shared.accounting.ServicingIncomeSchedule;
import com.nokor.efinance.core.shared.accounting.ServicingIncomeSchedules;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.core.shared.service.ServiceEntityField;
import com.nokor.efinance.core.workflow.ContractHistoReason;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.efinance.glf.accounting.service.GLFLeasingAccountingService;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.finance.services.shared.AmortizationSchedules;
import com.nokor.finance.services.shared.CalculationParameter;
import com.nokor.finance.services.shared.Schedule;
import com.nokor.finance.services.shared.system.EFrequency;
import com.nokor.finance.services.tools.LoanUtils;


/**
 * Contract service
 * @author mao.heng
 *
 */
@Service("gLFLeasingAccountingService")
public class GLFLeasingAccountingServiceImpl extends BaseEntityServiceImpl implements GLFLeasingAccountingService, CashflowEntityField {
	/** */
	private static final long serialVersionUID = 8800816886214543276L;

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
    private EntityDao dao;
	
	@Autowired
	private FinanceCalculationService financeCalculationService;
	
	@Autowired
	private ContractService contractService;
	
	@Autowired
	private CashflowService cashflowService;
	
	/**
     * @see org.seuksa.frmk.mvc.service.impl.BaseEntityServiceImpl#getDao()
     */
	@Override
	public EntityDao getDao() {
		return dao;
	}
		
	private EHistoReason[] getHistoReasons() {
		return new EHistoReason[] {
				ContractHistoReason.CONTRACT_0003,
				ContractHistoReason.CONTRACT_ACC, 
				ContractHistoReason.CONTRACT_FRA,
				ContractHistoReason.CONTRACT_LOSS,
				ContractHistoReason.CONTRACT_REP,
				ContractHistoReason.CONTRACT_THE,
				ContractHistoReason.CONTRACT_WRI,
			};
	}
	
	/**
	 * Get lease adjustments
	 * @param dealer
	 * @param reference
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Override
	public List<LeaseAdjustment> getLeaseAdjustments(EDealerType dealerType,
			Dealer dealer, EWkfStatus contractStatus, String reference, Date startDate, Date endDate) {
		List<LeaseAdjustment> leaseAdjustments = new ArrayList<>();
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<>(Contract.class);
		restrictions.addCriterion(Restrictions.in(CONTRACT_STATUS, new EWkfStatus[] {
				ContractWkfStatus.LOS, ContractWkfStatus.THE, ContractWkfStatus.EAR, ContractWkfStatus.FRA, 
				ContractWkfStatus.ACC, ContractWkfStatus.REP, ContractWkfStatus.WRI}));
		
		DetachedCriteria historySubCriteria = DetachedCriteria.forClass(WkfHistoryItem.class, "history");
		historySubCriteria.add(Restrictions.in("history.hisReason", getHistoReasons()));
		if (startDate != null) {       
			historySubCriteria.add(Restrictions.ge("history.historyDate", DateUtils.getDateAtBeginningOfDay(startDate)));
		}
		if (endDate != null) {
			historySubCriteria.add(Restrictions.le("history.historyDate", DateUtils.getDateAtEndOfDay(endDate)));
		}
		
		historySubCriteria.setProjection(Projections.projectionList().add(Projections.property("history.contract.id")));
		restrictions.addCriterion(Property.forName("id").in(historySubCriteria) );
		
		if (dealer != null) {
			restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, dealer.getId()));
		}
		
		if (StringUtils.isNotEmpty(reference)) {
			restrictions.addCriterion(Restrictions.ilike(REFERENCE, reference, MatchMode.ANYWHERE));
		}
		
		if (contractStatus != null) {
			restrictions.addCriterion(Restrictions.eq(CONTRACT_STATUS, contractStatus));
		}
		
		restrictions.addOrder(Order.desc("startDate"));
		
		List<Contract> contracts = list(restrictions);
		for (Contract contract : contracts) {
			LeaseAdjustment leaseAdjustment = new LeaseAdjustment();
			leaseAdjustment.setId(contract.getId());
			leaseAdjustment.setReference(contract.getReference());
			leaseAdjustment.setWkfStatus(contract.getWkfStatus());
			leaseAdjustment.setContractStartDate(contract.getStartDate());
			leaseAdjustment.setFirstInstallmentDate(contract.getFirstDueDate());
			leaseAdjustment.setChangeStatusDate(getEventDate(contract.getHistories(), contract.getWkfStatus()));
			leaseAdjustment.setFirstNameEn(contract.getApplicant().getIndividual().getFirstNameEn());
			leaseAdjustment.setLastNameEn(contract.getApplicant().getIndividual().getLastNameEn());
			ContractAdjustment contractAdjustment = contract.getContractAdjustment();
			if (contractAdjustment != null) {
				leaseAdjustment.setBalanceInterestInSuspend(new Amount(contractAdjustment.getTeBalanceInterestInSuspendUsd(), 
						contractAdjustment.getVatBalanceInterestInSuspendUsd(), contractAdjustment.getTiBalanceInterestInSuspendUsd()));
				leaseAdjustment.setUnpaidAccruedInterestReceivable(new Amount(contractAdjustment.getTeUnpaidAccruedInterestReceivableUsd(), 
						contractAdjustment.getVatUnpaidAccruedInterestReceivableUsd(), 
						contractAdjustment.getTiUnpaidAccruedInterestReceivableUsd()));
				leaseAdjustment.setUnpaidInterestBalance(new Amount(contractAdjustment.getTeAdjustmentInterest(), contractAdjustment.getVatAdjustmentInterest(), contractAdjustment.getTiAdjustmentInterest()));
				leaseAdjustment.setUnpaidPrincipalBalance(new Amount(contractAdjustment.getTeAdjustmentPrincipal(), contractAdjustment.getVatAdjustmentPrincipal(), contractAdjustment.getTiAdjustmentPrincipal()));
			}
			leaseAdjustments.add(leaseAdjustment);
		}
		return leaseAdjustments;
	}
	
	/**
	 * Get referal fee adjustments
	 * @param dealer
	 * @param reference
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<ReferalFeeAdjustment> getReferalFeeAdjustments(EDealerType dealerType, Dealer dealer, String reference, Date startDate, Date endDate) {
		List<ReferalFeeAdjustment> referalFeeAdjustments = new ArrayList<>();
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<>(Contract.class);
		restrictions.addCriterion(Restrictions.in(CONTRACT_STATUS, new EWkfStatus[] {
				ContractWkfStatus.LOS, ContractWkfStatus.THE, ContractWkfStatus.EAR, ContractWkfStatus.FRA, ContractWkfStatus.ACC, ContractWkfStatus.REP}));
		
		DetachedCriteria historySubCriteria = DetachedCriteria.forClass(WkfHistoryItem.class, "history");
		historySubCriteria.add(Restrictions.in("history.hisReason", getHistoReasons()));
		if (startDate != null) {       
			historySubCriteria.add(Restrictions.ge("history.historyDate", DateUtils.getDateAtBeginningOfDay(startDate)));
		}
		if (endDate != null) {
			historySubCriteria.add(Restrictions.le("history.historyDate", DateUtils.getDateAtEndOfDay(endDate)));
		}
		
		historySubCriteria.setProjection(Projections.projectionList().add(Projections.property("history.contract.id")));
		restrictions.addCriterion(Property.forName("id").in(historySubCriteria) );
		
		if (dealer != null) {
			restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, dealer.getId()));
		}
		
		if (StringUtils.isNotEmpty(reference)) {
			restrictions.addCriterion(Restrictions.ilike(REFERENCE, reference, MatchMode.ANYWHERE));
		}
		
		restrictions.addOrder(Order.desc("startDate"));
		
		List<Contract> contracts = list(restrictions);
		for (Contract contract : contracts) {
			ReferalFeeAdjustment referalFeeAdjustment = new ReferalFeeAdjustment();
			referalFeeAdjustment.setId(contract.getId());
			referalFeeAdjustment.setReference(contract.getReference());
			referalFeeAdjustment.setWkfStatus(contract.getWkfStatus());
			referalFeeAdjustment.setContractStartDate(contract.getStartDate());
			referalFeeAdjustment.setFirstInstallmentDate(contract.getFirstDueDate());
			referalFeeAdjustment.setFirstNameEn(contract.getApplicant().getIndividual().getFirstNameEn());
			referalFeeAdjustment.setLastNameEn(contract.getApplicant().getIndividual().getLastNameEn());
			if (contract.getContractAdjustment() != null) {
			} else {
				referalFeeAdjustment.setUnpaidDeferredCommissionReferalFee(new Amount(0d, 0d, 0d));
				referalFeeAdjustment.setUnpaidAcrrualExpenses(new Amount(0d, 0d, 0d));
			}
			referalFeeAdjustments.add(referalFeeAdjustment);
		}
		return referalFeeAdjustments;
	}
	
	/**
	 * Get insurance income adjustments
	 * @param dealer
	 * @param reference
	 * @param startDate
	 * @param endDate
	 * @return
	 */
    public List<InsuranceIncomeAdjustment> getInsuranceIncomeAdjustments(EDealerType dealerType, Dealer dealer, String reference, Date startDate, Date endDate) {
    	List<InsuranceIncomeAdjustment> insuranceIncomeAdjustments = new ArrayList<>();
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<>(Contract.class);
		restrictions.addCriterion(Restrictions.in(CONTRACT_STATUS, new EWkfStatus[] {
				ContractWkfStatus.LOS, ContractWkfStatus.THE, ContractWkfStatus.EAR, ContractWkfStatus.FRA, ContractWkfStatus.ACC, ContractWkfStatus.REP}));
		
		DetachedCriteria historySubCriteria = DetachedCriteria.forClass(WkfHistoryItem.class, "history");
		historySubCriteria.add(Restrictions.in("history.hisReason", getHistoReasons()));
		if (startDate != null) {       
			historySubCriteria.add(Restrictions.ge("history.historyDate", DateUtils.getDateAtBeginningOfDay(startDate)));
		}
		if (endDate != null) {
			historySubCriteria.add(Restrictions.le("history.historyDate", DateUtils.getDateAtEndOfDay(endDate)));
		}
		
		historySubCriteria.setProjection(Projections.projectionList().add(Projections.property("history.contract.id")));
		restrictions.addCriterion(Property.forName("id").in(historySubCriteria) );
		
		if (dealer != null) {
			restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, dealer.getId()));
		}
		
		if (StringUtils.isNotEmpty(reference)) {
			restrictions.addCriterion(Restrictions.ilike(REFERENCE, reference, MatchMode.ANYWHERE));
		}
		
		restrictions.addOrder(Order.desc("startDate"));
		
		List<Contract> contracts = list(restrictions);
		for (Contract contract : contracts) {
			InsuranceIncomeAdjustment insuranceIncomeAdjustment = new InsuranceIncomeAdjustment();
			insuranceIncomeAdjustment.setId(contract.getId());
			insuranceIncomeAdjustment.setReference(contract.getReference());
			insuranceIncomeAdjustment.setWkfStatus(contract.getWkfStatus());
			insuranceIncomeAdjustment.setContractStartDate(contract.getStartDate());
			insuranceIncomeAdjustment.setFirstInstallmentDate(contract.getFirstDueDate());
			insuranceIncomeAdjustment.setFirstNameEn(contract.getApplicant().getIndividual().getFirstNameEn());
			insuranceIncomeAdjustment.setLastNameEn(contract.getApplicant().getIndividual().getLastNameEn());
			if (contract.getContractAdjustment() != null) {
			} else {
				insuranceIncomeAdjustment.setBalanceInsuranceIncomeInSuspend(new Amount(0d, 0d, 0d));
					insuranceIncomeAdjustment.setUnpaidUnearnedInsuranceIncome(new Amount(0d, 0d, 0d));
					insuranceIncomeAdjustment.setUnpaidAccrualReceivable(new Amount(0d, 0d, 0d));
			}
			insuranceIncomeAdjustments.add(insuranceIncomeAdjustment);
		}
		return insuranceIncomeAdjustments;
    }
	
    /**
	 * Get servicing income adjustments
	 * @param dealer
	 * @param reference
	 * @param startDate
	 * @param endDate
	 * @return
	 */
    public List<ServicingIncomeAdjustment> getServicingIncomeAdjustments(EDealerType dealerType, Dealer dealer, String reference, Date startDate, Date endDate) {
    	List<ServicingIncomeAdjustment> servicingIncomeAdjustments = new ArrayList<>();
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<>(Contract.class);
		restrictions.addCriterion(Restrictions.in(CONTRACT_STATUS, new EWkfStatus[] {
				ContractWkfStatus.LOS, ContractWkfStatus.THE, ContractWkfStatus.EAR, ContractWkfStatus.FRA, ContractWkfStatus.ACC, ContractWkfStatus.REP}));
		
		DetachedCriteria historySubCriteria = DetachedCriteria.forClass(WkfHistoryItem.class, "history");
		historySubCriteria.add(Restrictions.in("history.hisReason", getHistoReasons()));
		if (startDate != null) {       
			historySubCriteria.add(Restrictions.ge("history.historyDate", DateUtils.getDateAtBeginningOfDay(startDate)));
		}
		if (endDate != null) {
			historySubCriteria.add(Restrictions.le("history.historyDate", DateUtils.getDateAtEndOfDay(endDate)));
		}
		
		historySubCriteria.setProjection(Projections.projectionList().add(Projections.property("history.contract.id")));
		restrictions.addCriterion(Property.forName("id").in(historySubCriteria) );
		
		if (dealer != null) {
			restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, dealer.getId()));
		}
		
		if (StringUtils.isNotEmpty(reference)) {
			restrictions.addCriterion(Restrictions.ilike(REFERENCE, reference, MatchMode.ANYWHERE));
		}
		
		restrictions.addOrder(Order.desc("startDate"));
		
		List<Contract> contracts = list(restrictions);
		for (Contract contract : contracts) {
			ServicingIncomeAdjustment servicingIncomeAdjustment = new ServicingIncomeAdjustment();
			servicingIncomeAdjustment.setId(contract.getId());
			servicingIncomeAdjustment.setReference(contract.getReference());
			servicingIncomeAdjustment.setWkfStatus(contract.getWkfStatus());
			servicingIncomeAdjustment.setContractStartDate(contract.getStartDate());
			servicingIncomeAdjustment.setFirstInstallmentDate(contract.getFirstDueDate());
			servicingIncomeAdjustment.setFirstNameEn(contract.getApplicant().getIndividual().getFirstNameEn());
			servicingIncomeAdjustment.setLastNameEn(contract.getApplicant().getIndividual().getLastNameEn());
			if (contract.getContractAdjustment() != null) {
			} else {
				servicingIncomeAdjustment.setBalanceServicingIncomeInSuspend(new Amount(0d, 0d, 0d));
				servicingIncomeAdjustment.setUnpaidUnearnedServicingIncome(new Amount(0d, 0d, 0d));
				servicingIncomeAdjustment.setUnpaidAccrualReceivable(new Amount(0d, 0d, 0d));
			}
			servicingIncomeAdjustments.add(servicingIncomeAdjustment);
		}
		return servicingIncomeAdjustments;
    }
	
	
	/**
	 * @param histories
	 * @return
	 */
	private Date getEventDate(List<ContractWkfHistoryItem> histories, EWkfStatus contractStatus) {
		Date earlySettlementDate = null;
		EHistoReason hisReason = null;
		if (contractStatus == ContractWkfStatus.EAR) {
			hisReason = ContractHistoReason.CONTRACT_0003;
		} else if (contractStatus == ContractWkfStatus.LOS) {
			hisReason = ContractHistoReason.CONTRACT_LOSS;
		} else if (contractStatus == ContractWkfStatus.REP) {
			hisReason = ContractHistoReason.CONTRACT_REP;
		} else if (contractStatus == ContractWkfStatus.THE) {
			hisReason = ContractHistoReason.CONTRACT_THE;
		} else if (contractStatus == ContractWkfStatus.ACC) {
			hisReason = ContractHistoReason.CONTRACT_ACC;
		} else if (contractStatus == ContractWkfStatus.FRA) {
			hisReason = ContractHistoReason.CONTRACT_FRA;
		} else if (contractStatus == ContractWkfStatus.WRI) {
			hisReason = ContractHistoReason.CONTRACT_WRI;
		}
		
		for (ContractWkfHistoryItem history : histories) {
			if (history.getReason().equals(hisReason)) {
				earlySettlementDate = DateUtils.getDateAtBeginningOfDay(history.getChangeDate());
			}
		}
		return earlySettlementDate;
	}
	
	/**
	 * Get interest unearned balance
	 * @param calculDate
	 * @param cashflows
	 * @return
	 */
	public Amount getAccountingInterestUnearnedBalance(Date calculDate, List<Cashflow> cashflows) {
		Amount interestBalance = new Amount(0d, 0d, 0d);		
		for (Cashflow cashflow : cashflows) {
			if (cashflow.getCashflowType().equals(ECashflowType.IAP)
					&& !cashflow.isCancel()
					&& !cashflow.isUnpaid()
					&& (!cashflow.isPaid() || cashflow.getInstallmentDate().compareTo(calculDate) > 0 || (cashflow.getPayment() != null && cashflow.getPayment().getPaymentDate().compareTo(calculDate) > 0))) {
				interestBalance.plusTiAmount(cashflow.getTiInstallmentAmount());
				interestBalance.plusTeAmount(cashflow.getTeInstallmentAmount());
				interestBalance.plusVatAmount(cashflow.getVatInstallmentAmount());
			}
		}
		return interestBalance;
	}
	
	/**
	 * @param calculDate
	 * @param cashflows
	 * @return
	 */
	private Amount getTheoricalInterestUnearnedBalance(Date calculDate, List<Cashflow> cashflows) {
		Amount interestBalance = new Amount(0d, 0d, 0d);
		for (Cashflow cashflow : cashflows) {
			if (cashflow.getCashflowType().equals(ECashflowType.IAP)
					&& !cashflow.isCancel()
					&& (cashflow.getInstallmentDate().compareTo(calculDate) >= 0 || !cashflow.isPaid() || (cashflow.getPayment() != null && cashflow.getPayment().getPaymentDate().compareTo(calculDate) > 0))
					&& !cashflow.isUnpaid()) {
				interestBalance.plusTiAmount(cashflow.getTiInstallmentAmount());
				interestBalance.plusTeAmount(cashflow.getTeInstallmentAmount());
				interestBalance.plusVatAmount(cashflow.getVatInstallmentAmount());
			}
		}		
		return interestBalance;
	}
	
	/**
	 * Get interest unearned balance
	 * @param calculDate
	 * @param cashflows
	 * @return
	 */
	public Amount getInterestUnearnedBalance(Date calculDate, List<Cashflow> cashflows) {
		Amount interestBalance = new Amount(0d, 0d, 0d);		
		for (Cashflow cashflow : cashflows) {
			if (cashflow.getCashflowType().equals(ECashflowType.IAP)
					&& !cashflow.isCancel()
					&& (!cashflow.isPaid() || (cashflow.getPayment() != null && cashflow.getPayment().getPaymentDate().compareTo(calculDate) > 0))
					&& !cashflow.isUnpaid()) {
				interestBalance.plusTiAmount(cashflow.getTiInstallmentAmount());
				interestBalance.plusTeAmount(cashflow.getTeInstallmentAmount());
				interestBalance.plusVatAmount(cashflow.getVatInstallmentAmount());
			}
		}
		return interestBalance;
	}
	
	/**
	 * Get principal balance
	 * @param calculDate
	 * @param cashflows
	 * @return
	 */
	public Amount getAccountingPrincipalBalance(Date calculDate, List<Cashflow> cashflows) {
		Amount principalBalance = new Amount(0d, 0d, 0d);		
		for (Cashflow cashflow : cashflows) {
			if (cashflow.getCashflowType().equals(ECashflowType.CAP)
					&& !cashflow.isCancel()
					&& !cashflow.isUnpaid()
					&& (!cashflow.isPaid() || cashflow.getInstallmentDate().compareTo(calculDate) > 0 || (cashflow.getPayment() != null && cashflow.getPayment().getPaymentDate().compareTo(calculDate) > 0))) {
				principalBalance.plusTiAmount(cashflow.getTiInstallmentAmount());
				principalBalance.plusTeAmount(cashflow.getTeInstallmentAmount());
				principalBalance.plusVatAmount(cashflow.getVatInstallmentAmount());
			}
		}
		return principalBalance;
	}
	
	/**
	 * Get principal balance
	 * @param calculDate
	 * @param cashflows
	 * @return
	 */
	public Amount getAccountingServiceBalance(Date calculDate, List<Cashflow> cashflows) {
		Amount principalBalance = new Amount(0d, 0d, 0d);		
		for (Cashflow cashflow : cashflows) {
			if (!cashflow.isCancel()
					&& !cashflow.isUnpaid()
					&& (!cashflow.isPaid() || cashflow.getInstallmentDate().compareTo(calculDate) > 0 || (cashflow.getPayment() != null && cashflow.getPayment().getPaymentDate().compareTo(calculDate) > 0))) {
				principalBalance.plusTiAmount(cashflow.getTiInstallmentAmount());
				principalBalance.plusTeAmount(cashflow.getTeInstallmentAmount());
				principalBalance.plusVatAmount(cashflow.getVatInstallmentAmount());
			}
		}
		return principalBalance;
	}
	
	/**
	 * @param calculDate
	 * @param cashflows
	 * @return
	 */
	private Amount getTheoricalPrincipalBalance(Date calculDate, List<Cashflow> cashflows) {
		Amount outstanding = new Amount(0d, 0d, 0d);
		for (Cashflow cashflow : cashflows) {
			if (cashflow.getCashflowType().equals(ECashflowType.CAP)
					&& !cashflow.isCancel()
					&& (cashflow.getInstallmentDate().compareTo(calculDate) >= 0 || !cashflow.isPaid() || (cashflow.getPayment() != null && cashflow.getPayment().getPaymentDate().compareTo(calculDate) > 0))
					&& !cashflow.isUnpaid()) {
				outstanding.plusTiAmount(cashflow.getTiInstallmentAmount());
				outstanding.plusTeAmount(cashflow.getTeInstallmentAmount());
				outstanding.plusVatAmount(cashflow.getVatInstallmentAmount());
			}
		}		
		return outstanding;
	}
	
	/**
	 * Get principal balance
	 * @param calculDate
	 * @param cashflows
	 * @return
	 */
	public Amount getPrincipalBalance(Date calculDate, List<Cashflow> cashflows) {
		Amount principalBalance = new Amount(0d, 0d, 0d);		
		for (Cashflow cashflow : cashflows) {
			if (cashflow.getCashflowType().equals(ECashflowType.CAP)
					&& !cashflow.isCancel()
					&& (!cashflow.isPaid() || (cashflow.getPayment() != null && cashflow.getPayment().getPaymentDate().compareTo(calculDate) > 0))
					&& !cashflow.isUnpaid()) {
				principalBalance.plusTiAmount(cashflow.getTiInstallmentAmount());
				principalBalance.plusTeAmount(cashflow.getTeInstallmentAmount());
				principalBalance.plusVatAmount(cashflow.getVatInstallmentAmount());
			}
		}
		return principalBalance;
	}
		
	/**
	 * Get referal fees
	 * @param dealer
	 * @param reference
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<ReferalFee> getReferalFees(EDealerType dealerType, Dealer dealer, String reference, Date startDate, Date endDate) {
		List<ReferalFee> referalFees = new ArrayList<>();
		
		startDate = DateUtils.getDateAtBeginningOfDay(startDate);
		endDate = DateUtils.getDateAtBeginningOfDay(endDate);
		
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<>(Quotation.class);
		restrictions.addCriterion(Restrictions.in("wkfStatus", 
				new EWkfStatus[] {QuotationWkfStatus.ACT, QuotationWkfStatus.ACG, QuotationWkfStatus.RVG, QuotationWkfStatus.RCG, QuotationWkfStatus.LCG}));
		
		restrictions.addCriterion(Restrictions.le("contractStartDate", endDate));
				
		if (dealer != null) {
			restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, dealer.getId()));
		}
		
		if (StringUtils.isNotEmpty(reference)) {
			restrictions.addCriterion(Restrictions.ilike(REFERENCE, reference, MatchMode.ANYWHERE));
		}
		
		List<Quotation> quotations = list(restrictions);
		for (Quotation quotation : quotations) {			
			CalculationParameter calculationParameter = new CalculationParameter();
			calculationParameter.setInitialPrincipal(quotation.getTiFinanceAmount());
			calculationParameter.setNumberOfPeriods(LoanUtils.getNumberOfPeriods(quotation.getTerm(), quotation.getFrequency()));
			calculationParameter.setPeriodicInterestRate(quotation.getInterestRate() / 100d);
			calculationParameter.setFrequency(quotation.getFrequency());
			
			GLFReferalFeeCalculatorImpl calculator = new GLFReferalFeeCalculatorImpl();
			ReferalFeeSchedules referalFeeSchedules = calculator.getSchedules(quotation.getContractStartDate(), quotation.getFirstDueDate(), calculationParameter);
						
			ReferalFee referalFee = new ReferalFee();
			referalFee.setId(quotation.getId());
			referalFee.setReference(quotation.getReference());
			referalFee.setContractStartDate(quotation.getContractStartDate());
			referalFee.setFirstInstallmentDate(quotation.getFirstDueDate());
			referalFee.setFirstNameEn(quotation.getApplicant().getIndividual().getFirstNameEn());
			referalFee.setLastNameEn(quotation.getApplicant().getIndividual().getLastNameEn());
			for (ReferalFeeSchedule referalFeeSchedule : referalFeeSchedules.getSchedules()) {
				if (startDate.compareTo(referalFeeSchedule.getPeriodStartDate()) <= 0
						&& endDate.compareTo(referalFeeSchedule.getPeriodEndDate()) >= 0) {
					referalFee.getReferalFeeDistribution2().plus(new Amount(referalFeeSchedule.getReferalFeeDistribution2(), 0d, referalFeeSchedule.getReferalFeeDistribution2()));
					referalFee.getReferalFeeDistribution3().plus(new Amount(referalFeeSchedule.getReferalFeeDistribution3(), 0d, referalFeeSchedule.getReferalFeeDistribution3()));
					referalFee.getCumulativeBalance().plus(new Amount(referalFeeSchedule.getCumulativeBalance(), 0d, referalFeeSchedule.getCumulativeBalance()));
					referalFee.getPaymentToDealer().plus(new Amount(referalFeeSchedule.getPaymentToDealer(), 0d, referalFeeSchedule.getPaymentToDealer()));
				}
				if (referalFeeSchedule.getPeriodEndDate().compareTo(endDate) >= 0
						&& referalFee.getDeferredCommissionReferalFee().getTiAmount() == null) {
					referalFee.setDeferredCommissionReferalFee(new Amount(referalFeeSchedule.getDeferredCommissionReferalFee(), 0d, referalFeeSchedule.getDeferredCommissionReferalFee()));
					referalFee.setAcrrualExpenses(new Amount(referalFeeSchedule.getAcrrualExpenses(), 0d, referalFeeSchedule.getAcrrualExpenses()));
				}
			}
			
			if (referalFee.getRealReferalFeeDistributed().getTiAmount() != null
					&& referalFee.getRealReferalFeeDistributed().getTiAmount() > 0d) {
				referalFees.add(referalFee);
			}
						
		}
		
		return referalFees;
	}
	
	
	/**
	 * Get insurance incomes
	 * @param dealer
	 * @param reference
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<InsuranceExpense> getInsuranceExpenses(EDealerType dealerType, Dealer dealer, String reference, Date startDate, Date endDate) {
		List<InsuranceExpense> insuranceExpenses = new ArrayList<>();
		
		startDate = DateUtils.getDateAtBeginningOfDay(startDate);
		endDate = DateUtils.getDateAtBeginningOfDay(endDate);
		
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<>(Quotation.class);
		restrictions.addCriterion(Restrictions.in("wkfStatus", 
				new EWkfStatus[] {QuotationWkfStatus.ACT, QuotationWkfStatus.ACG, QuotationWkfStatus.RVG, QuotationWkfStatus.RCG, QuotationWkfStatus.LCG}));
		
		restrictions.addCriterion(Restrictions.le("contractStartDate", endDate));
		
		if (dealer != null) {
			restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, dealer.getId()));
		}
		
		if (StringUtils.isNotEmpty(reference)) {
			restrictions.addCriterion(Restrictions.ilike(REFERENCE, reference, MatchMode.ANYWHERE));
		}
		
		List<Quotation> quotations = list(restrictions);
		for (Quotation quotation : quotations) {
			CalculationParameter calculationParameter = new CalculationParameter();
			calculationParameter.setAssetPrice(quotation.getAsset().getTiAssetPrice());
			calculationParameter.setInitialPrincipal(quotation.getTiFinanceAmount());
			calculationParameter.setNumberOfPeriods(LoanUtils.getNumberOfPeriods(quotation.getTerm(), quotation.getFrequency()));
			calculationParameter.setPeriodicInterestRate(quotation.getInterestRate() / 100d);
			calculationParameter.setFrequency(quotation.getFrequency());
			
			GLFInsuranceExpenseCalculatorImpl calculator = new GLFInsuranceExpenseCalculatorImpl();
			InsuranceExpenseSchedules insuranceExpenseSchedules = calculator.getSchedules(quotation.getContractStartDate(), quotation.getFirstDueDate(), calculationParameter);
						
			InsuranceExpense insuranceExpense = new InsuranceExpense();
			insuranceExpense.setId(quotation.getId());
			insuranceExpense.setReference(quotation.getReference());
			insuranceExpense.setContractStartDate(quotation.getContractStartDate());
			insuranceExpense.setFirstInstallmentDate(quotation.getFirstDueDate());
			insuranceExpense.setFirstNameEn(quotation.getApplicant().getIndividual().getFirstNameEn());
			insuranceExpense.setLastNameEn(quotation.getApplicant().getIndividual().getLastNameEn());
			for (InsuranceExpenseSchedule insuranceExpenseSchedule : insuranceExpenseSchedules.getSchedules()) {
				if (startDate.compareTo(insuranceExpenseSchedule.getPeriodStartDate()) <= 0
						&& endDate.compareTo(insuranceExpenseSchedule.getPeriodEndDate()) >= 0) {
					insuranceExpense.getInsuranceExpenseDistribution2().plus(new Amount(insuranceExpenseSchedule.getInsuranceExpenseDistribution2(), 0d, insuranceExpenseSchedule.getInsuranceExpenseDistribution2()));
					insuranceExpense.getInsuranceExpenseDistribution3().plus(new Amount(insuranceExpenseSchedule.getInsuranceExpenseDistribution3(), 0d, insuranceExpenseSchedule.getInsuranceExpenseDistribution3()));
					insuranceExpense.getCumulativeBalance().plus(new Amount(insuranceExpenseSchedule.getCumulativeBalance(), 0d, insuranceExpenseSchedule.getCumulativeBalance()));
					insuranceExpense.getInsuranceExpensePaid().plus(new Amount(insuranceExpenseSchedule.getInsuranceExpensePaid(), 0d, insuranceExpenseSchedule.getInsuranceExpensePaid()));
				}
				if (insuranceExpenseSchedule.getPeriodEndDate().compareTo(endDate) >= 0
						&& insuranceExpense.getBalanceInsuranceExpense().getTiAmount() == null) {
					insuranceExpense.setBalanceInsuranceExpense(new Amount(insuranceExpenseSchedule.getBalanceInsuranceExpense(), 0d, insuranceExpenseSchedule.getBalanceInsuranceExpense()));
				}
			}			
			
			if (insuranceExpense.getRealInsuranceExpenseDistributed().getTiAmount() != null
					&& insuranceExpense.getRealInsuranceExpenseDistributed().getTiAmount() > 0d) {
				insuranceExpenses.add(insuranceExpense);
			}
		}
		return insuranceExpenses;
	}
	
	/**
	 * Get registration expenses
	 * @param dealer
	 * @param reference
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<RegistrationExpense> getRegistrationExpenses(EDealerType dealerType, Dealer dealer, String reference, Date startDate, Date endDate) {
		List<RegistrationExpense> registrationExpenses = new ArrayList<>();
		
		startDate = DateUtils.getDateAtBeginningOfDay(startDate);
		endDate = DateUtils.getDateAtBeginningOfDay(endDate);
		
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<>(Quotation.class);
		restrictions.addCriterion(Restrictions.in("wkfStatus", 
				new EWkfStatus[] {QuotationWkfStatus.ACT, QuotationWkfStatus.ACG, QuotationWkfStatus.RVG, QuotationWkfStatus.RCG, QuotationWkfStatus.LCG}));
		
		restrictions.addCriterion(Restrictions.le("contractStartDate", endDate));
		
		if (dealer != null) {
			restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, dealer.getId()));
		}
		
		if (StringUtils.isNotEmpty(reference)) {
			restrictions.addCriterion(Restrictions.ilike(REFERENCE, reference, MatchMode.ANYWHERE));
		}
		
		List<Quotation> quotations = list(restrictions);
		for (Quotation quotation : quotations) {
			
			// double registrationPlateNumberFee = quotation.getDealer().getRegistrationPlateNumberFee() == null ? 30d : quotation.getDealer().getRegistrationPlateNumberFee();
			
			double registrationPlateNumberFee = 30d;
			CalculationParameter calculationParameter = new CalculationParameter();
			calculationParameter.setInitialPrincipal(quotation.getTiFinanceAmount());
			calculationParameter.setNumberOfPeriods(LoanUtils.getNumberOfPeriods(quotation.getTerm(), quotation.getFrequency()));
			calculationParameter.setPeriodicInterestRate(quotation.getInterestRate() / 100d);
			calculationParameter.setFrequency(quotation.getFrequency());
			calculationParameter.setRegistrationFee(registrationPlateNumberFee);
			
			GLFRegistrationExpenseCalculatorImpl calculator = new GLFRegistrationExpenseCalculatorImpl();
			RegistrationExpenseSchedules registrationExpenseSchedules = calculator.getSchedules(quotation.getContractStartDate(), quotation.getFirstDueDate(), calculationParameter, registrationPlateNumberFee);
						
			RegistrationExpense registrationExpense = new RegistrationExpense();
			registrationExpense.setId(quotation.getId());
			registrationExpense.setReference(quotation.getReference());
			registrationExpense.setContractStartDate(quotation.getContractStartDate());
			registrationExpense.setFirstInstallmentDate(quotation.getFirstDueDate());
			registrationExpense.setFirstNameEn(quotation.getApplicant().getIndividual().getFirstNameEn());
			registrationExpense.setLastNameEn(quotation.getApplicant().getIndividual().getLastNameEn());
			for (RegistrationExpenseSchedule registrationExpenseSchedule : registrationExpenseSchedules.getSchedules()) {
				if (startDate.compareTo(registrationExpenseSchedule.getPeriodStartDate()) <= 0
						&& endDate.compareTo(registrationExpenseSchedule.getPeriodEndDate()) >= 0) {
					registrationExpense.getRegistrationExpenseDistribution2().plus(new Amount(registrationExpenseSchedule.getRegistrationExpenseDistribution2(), 0d, registrationExpenseSchedule.getRegistrationExpenseDistribution2()));
					registrationExpense.getRegistrationExpenseDistribution3().plus(new Amount(registrationExpenseSchedule.getRegistrationExpenseDistribution3(), 0d, registrationExpenseSchedule.getRegistrationExpenseDistribution3()));
					registrationExpense.getCumulativeBalance().plus(new Amount(registrationExpenseSchedule.getCumulativeBalance(), 0d, registrationExpenseSchedule.getCumulativeBalance()));
					registrationExpense.getRegistrationPlateNumberFee().plus(new Amount(registrationExpenseSchedule.getRegistrationPlateNumberFee(), 0d, registrationExpenseSchedule.getRegistrationPlateNumberFee()));
				}
				if (registrationExpenseSchedule.getPeriodEndDate().compareTo(endDate) >= 0
						&& registrationExpense.getBalanceRegistrationExpense().getTiAmount() == null) {
					registrationExpense.setBalanceRegistrationExpense(new Amount(registrationExpenseSchedule.getBalanceRegistrationExpense(), 0d, registrationExpenseSchedule.getBalanceRegistrationExpense()));
				}
			}
			if (registrationExpense.getRealRegistrationExpenseDistributed().getTiAmount() != null
					&& registrationExpense.getRealRegistrationExpenseDistributed().getTiAmount() > 0d) {
				registrationExpenses.add(registrationExpense);
			}
						
		}
		return registrationExpenses;
	}
	
	/**
	 * @see com.nokor.efinance.GLFLeasingAccountingService.service.accounting.AccountingService#getServicingIncomesAdjustment(com.nokor.efinance.core.dealer.model.Dealer, java.lang.String, java.util.Date, java.util.Date)
	 */
	@Override
	public List<ServicingIncome> getServicingIncomes(EDealerType dealerType, Dealer dealer, String reference, Date startDate, Date endDate) {
		List<ServicingIncome> servicingIncomes = new ArrayList<>();
		
		startDate = DateUtils.getDateAtBeginningOfDay(startDate);
		endDate = DateUtils.getDateAtBeginningOfDay(endDate);
		
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<>(Quotation.class);
		restrictions.addCriterion(Restrictions.in("wkfStatus", 
				new EWkfStatus[] {QuotationWkfStatus.ACT, QuotationWkfStatus.ACG, QuotationWkfStatus.RVG, QuotationWkfStatus.RCG, QuotationWkfStatus.LCG}));
		
		restrictions.addCriterion(Restrictions.le("contractStartDate", endDate));
		
		if (dealer != null) {
			restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, dealer.getId()));
		}
		
		if (StringUtils.isNotEmpty(reference)) {
			restrictions.addCriterion(Restrictions.ilike(REFERENCE, reference, MatchMode.ANYWHERE));
		}
		
		List<Quotation> quotations = list(restrictions);
		for (Quotation quotation : quotations) {
			
			QuotationService servicingService = quotation.getQuotationService("SERFEE");
			
			if (servicingService != null) {			
				CalculationParameter calculationParameter = new CalculationParameter();
				calculationParameter.setInitialPrincipal(quotation.getTiFinanceAmount());
				calculationParameter.setNumberOfPeriods(LoanUtils.getNumberOfPeriods(quotation.getTerm(), quotation.getFrequency()));
				calculationParameter.setPeriodicInterestRate(quotation.getInterestRate() / 100d);
				calculationParameter.setFrequency(quotation.getFrequency());
				calculationParameter.setServicingFee(servicingService.getTiPrice());
				
				GLFServicingIncomeCalculatorImpl calculator = new GLFServicingIncomeCalculatorImpl();
				Long cotraId = quotation.getContract().getId();
				Map<Integer, Cashflow> servicingClashflow = getServicingCashflow(cotraId);
				ServicingIncomeSchedules servicingIncomeSchedules = calculator.getSchedules(quotation.getContractStartDate(), quotation.getFirstDueDate(), calculationParameter, servicingClashflow);
				System.out.println(servicingIncomeSchedules.toString());
				ServicingIncome servicingIncome = new ServicingIncome();
				servicingIncome.setId(quotation.getId());
				servicingIncome.setReference(quotation.getReference());
				servicingIncome.setContractStartDate(quotation.getContractStartDate());
				servicingIncome.setFirstInstallmentDate(quotation.getFirstDueDate());
				servicingIncome.setFirstNameEn(quotation.getApplicant().getIndividual().getFirstNameEn());
				servicingIncome.setLastNameEn(quotation.getApplicant().getIndividual().getLastNameEn());
				for (ServicingIncomeSchedule servicingIncomeSchedule : servicingIncomeSchedules.getSchedules()) {
					
					Date calculEndDate = DateUtils.getDateAtEndOfMonth(endDate);
					Date calculStartDate = DateUtils.getDateAtBeginningOfMonth(startDate);
					if (calculStartDate.compareTo(servicingIncomeSchedule.getPeriodStartDate()) <= 0
							&& calculEndDate.compareTo(servicingIncomeSchedule.getPeriodEndDate()) >= 0) {
//					if (startDate.compareTo(servicingIncomeSchedule.getPeriodStartDate()) <= 0
//							&& endDate.compareTo(servicingIncomeSchedule.getPeriodEndDate()) >= 0) {
						servicingIncome.getServicingIncomeDistribution2().plus(new Amount(servicingIncomeSchedule.getServicingIncomeDistribution2(), 0d, servicingIncomeSchedule.getServicingIncomeDistribution2()));
						servicingIncome.getServicingIncomeDistribution3().plus(new Amount(servicingIncomeSchedule.getServicingIncomeDistribution3(), 0d, servicingIncomeSchedule.getServicingIncomeDistribution3()));
						servicingIncome.getCumulativeBalance().plus(new Amount(servicingIncomeSchedule.getCumulativeBalance(), 0d, servicingIncomeSchedule.getCumulativeBalance()));
						servicingIncome.getAccountReceivable().plus(new Amount(servicingIncomeSchedule.getAccountReceivable(), 0d, servicingIncomeSchedule.getAccountReceivable()));
						servicingIncome.getServicingIncomeReceived().plus(new Amount(servicingIncomeSchedule.getServicingIncomeReceived(), 0d, servicingIncomeSchedule.getServicingIncomeReceived()));
						
						
						//Insurance income calculate as daily base 
						Date calculEndDate2 = DateUtils.addDaysDate(servicingIncomeSchedule.getPeriodEndDate(), 1);
						Date calculStartDate2 = servicingIncomeSchedule.getPeriodStartDate();
						if (DateUtils.isBeforeDay(calculStartDate2, startDate)) {
							calculStartDate2 = startDate;
						}
						if (DateUtils.isBeforeDay(endDate, calculEndDate2)) {
							calculEndDate2 = endDate;
						}
						
						long coeff = DateUtils.getDiffInDaysPlusOneDay(servicingIncomeSchedule.getPeriodEndDate(), servicingIncomeSchedule.getPeriodStartDate()); // DateUtils.getDiffInDaysPlusOneDay(periodEndDate, periodStartDate);
						long nbDays = 0;
						if (DateUtils.isAfterDay(servicingIncomeSchedule.getPeriodEndDate(), calculEndDate2)) {
							nbDays = DateUtils.getDiffInDaysPlusOneDay(calculEndDate2, calculStartDate2);
						} else {
							nbDays = coeff - DateUtils.getDiffInDays(calculStartDate2, servicingIncomeSchedule.getPeriodStartDate());
						}
						
						logger.info("nbDays - [" + nbDays + "]");
						logger.info("coeff - [" + coeff + "]");
						
						double realServicingIncomeDistributedInNDays = 0d;
						double servicingIncomeInSuspendInNDays = 0d;
						
						realServicingIncomeDistributedInNDays = MyMathUtils.roundAmountTo((servicingIncomeSchedule.getRealServicingIncomeDistributed() / coeff) * nbDays);
						servicingIncomeInSuspendInNDays = MyMathUtils.roundAmountTo((servicingIncomeSchedule.getServicingIncomeInSuspend() / coeff) * nbDays);
						
						servicingIncome.getServicingIncomeInSuspend().plus(new Amount(servicingIncomeInSuspendInNDays, 0d, servicingIncomeInSuspendInNDays));
						servicingIncome.getServicingIncomeInSuspendCumulated().plus(new Amount(servicingIncomeSchedule.getServicingIncomeInSuspendCumulated(), 0d, servicingIncomeSchedule.getServicingIncomeInSuspendCumulated()));
						servicingIncome.getRealServicingIncomeDistributed().plus(new Amount(realServicingIncomeDistributedInNDays, 0d, realServicingIncomeDistributedInNDays));

					}
					
					if (servicingIncomeSchedule.getPeriodEndDate().compareTo(endDate) >= 0
							&& servicingIncome.getUnearnedServicingIncome().getTiAmount() == null) {
						servicingIncome.setUnearnedServicingIncome(new Amount(servicingIncomeSchedule.getUnearnedServicingIncome(), 0d, servicingIncomeSchedule.getUnearnedServicingIncome()));
					}
				}
				servicingIncomes.add(servicingIncome);
//				if (servicingIncome.getRealServicingIncomeDistributed().getTiAmountUsd() != null
//						&& servicingIncome.getRealServicingIncomeDistributed().getTiAmountUsd() > 0d) {
//					servicingIncomes.add(servicingIncome);
//				}
			}
		}
		
		return servicingIncomes;
	}
	
	/**
	 * @param cotraId
	 * @return
	 */
	private List<Cashflow> getCashflowsNoCancel(Long cotraId) {
		List<Cashflow> cashflows = new ArrayList<>();
		String query = 
				"SELECT "
				+ " c.cfw_id, "
				+ " c.catyp_code, "
				+ " c.cfw_dt_installment, "
				+ " c.cfw_dt_period_start, "
				+ " c.cfw_dt_period_end, "
				+ " c.cfw_bl_cancel, "
				+ " c.cfw_bl_paid, "
				+ " c.cfw_bl_unpaid, "
				+ " c.cfw_am_te_installment_usd, "
				+ " c.cfw_am_vat_installment_usd, "
				+ " c.cfw_am_ti_installment_usd, "
				+ " c.cfw_nu_num_installment, "
				+ " c.cacode_code, "
				+ " c.paymn_id, "
				+ " p.paymn_dt_payment"
				+ " FROM td_cashflow c"
				+ " left join td_payment p on p.paymn_id = c.paymn_id" 
				+ " WHERE c.cotra_id = " + cotraId			
				+ " AND (c.cfw_bl_cancel is false)"
				+ " AND catyp_code in('CAP', 'IAP', 'PER')"
				+ " ORDER BY c.cfw_nu_num_installment asc";
		
		try {
			List<NativeRow> cashflowRows = executeSQLNativeQuery(query);
			for (NativeRow row : cashflowRows) {
		      	List<NativeColumn> columns = row.getColumns();
		      	int i = 0;
		      	Cashflow cashflow = new Cashflow();
		      	cashflow.setId((Long) columns.get(i++).getValue());
		      	cashflow.setCashflowType(ECashflowType.getByCode(columns.get(i++).getValue().toString()));
		      	cashflow.setInstallmentDate((Date) columns.get(i++).getValue());
		      	cashflow.setPeriodStartDate((Date) columns.get(i++).getValue());
		      	cashflow.setPeriodEndDate((Date) columns.get(i++).getValue());
		      	cashflow.setCancel((Boolean) columns.get(i++).getValue());
		      	cashflow.setPaid((Boolean) columns.get(i++).getValue());
		      	cashflow.setUnpaid((Boolean) columns.get(i++).getValue());
		      	cashflow.setTeInstallmentAmount((Double) columns.get(i++).getValue());
		      	cashflow.setVatInstallmentAmount((Double) columns.get(i++).getValue());
		      	cashflow.setTiInstallmentAmount((Double) columns.get(i++).getValue());
		      	cashflow.setNumInstallment((Integer) columns.get(i++).getValue());
		      	String cashflowCode = (String) columns.get(i++).getValue();
		      	if (StringUtils.isNotEmpty(cashflowCode)) {
		      		cashflow.setCashflowCode(ECashflowCode.getByCode(cashflowCode));
		      	}
		      	
		      	Long paymnId = (Long) columns.get(i).getValue();
		      	if (paymnId != null && paymnId.longValue() > 0) {
		      		Payment payment = new Payment();
		      		payment.setId((Long) columns.get(i++).getValue());
		      		payment.setPaymentDate((Date) columns.get(i++).getValue());
		      		cashflow.setPayment(payment);
		      	}
		      	cashflows.add(cashflow);
		    }
		} catch (NativeQueryException e) {
			logger.error(e.getMessage(), e);
		}
		
		return cashflows;
	}
	
	/**
	 * @param cotraId
	 * @return
	 */
	private List<Cashflow> getFeesAndDirectCosts(Long cotraId) {
		List<Cashflow> cashflows = new ArrayList<>();
		String query = 
				"SELECT "
				+ " c.cfw_id, "
				+ " c.catyp_code, "
				+ " c.cfw_dt_installment, "
				+ " c.cfw_bl_cancel, "
				+ " c.cfw_bl_paid, "
				+ " c.cfw_am_te_installment_usd, "
				+ " c.cfw_am_vat_installment_usd, "
				+ " c.cfw_am_ti_installment_usd, "
				+ " c.cfw_nu_num_installment, "
				+ " s.servi_id, "
				+ " s.servi_code, "
				+ " s.servi_desc_en, "
				+ " s.setyp_code, "
				+ " c.paymn_id, "
				+ " p.paymn_dt_payment"
				+ " FROM td_cashflow c"
				+ " inner join tu_service s on c.servi_id = s.servi_id"
				+ " left join td_payment p on p.paymn_id = c.paymn_id"
				+ " WHERE c.cotra_id = " + cotraId			
				+ " AND (c.cfw_bl_cancel is false or (c.cacode_code is not null and c.cfw_bl_paid is false))"
				+ " AND catyp_code in('FEE', 'SRV')"
				+ " ORDER BY c.cfw_nu_num_installment asc";
		
		try {
			List<NativeRow> cashflowRows = executeSQLNativeQuery(query);
			for (NativeRow row : cashflowRows) {
		      	List<NativeColumn> columns = row.getColumns();
		      	int i = 0;
		      	Cashflow cashflow = new Cashflow();
		      	cashflow.setId((Long) columns.get(i++).getValue());
		      	cashflow.setCashflowType(ECashflowType.getByCode(columns.get(i++).getValue().toString()));
		      	cashflow.setInstallmentDate((Date) columns.get(i++).getValue());
		      	cashflow.setCancel((Boolean) columns.get(i++).getValue());
		      	cashflow.setPaid((Boolean) columns.get(i++).getValue());
		      	cashflow.setTeInstallmentAmount(Math.abs((Double) columns.get(i++).getValue()));
		      	cashflow.setVatInstallmentAmount(Math.abs((Double) columns.get(i++).getValue()));
		      	cashflow.setTiInstallmentAmount(Math.abs((Double) columns.get(i++).getValue()));
		      	cashflow.setNumInstallment((Integer) columns.get(i++).getValue());
		      	Long serviId = (Long) columns.get(i++).getValue();
		      	if (serviId != null && serviId.longValue() > 0) {
		      		com.nokor.efinance.core.financial.model.FinService service = new com.nokor.efinance.core.financial.model.FinService();
		      		service.setId(serviId);
		      		service.setCode((String) columns.get(i++).getValue());
		      		service.setDescEn((String) columns.get(i++).getValue());
			      	String setypCode = (String) columns.get(i++).getValue();
			      	service.setServiceType(EServiceType.getByCode(setypCode));
			      	cashflow.setService(service);
		      	}
		      	Long paymnId = (Long) columns.get(i++).getValue();
		      	if (paymnId != null && paymnId.longValue() > 0) {
		      		Payment payment = new Payment();
		      		payment.setId(paymnId);
		      		payment.setPaymentDate((Date) columns.get(i++).getValue());
		      		cashflow.setPayment(payment);
		      	}
		      	cashflows.add(cashflow);
		    }
		} catch (NativeQueryException e) {
			logger.error(e.getMessage(), e);
		}
		
		return cashflows;
	}
	
	/**
	 * 
	 * @param cotraId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private Map<Integer, Cashflow> getInsuranceCashflow(Long cotraId) {
		Map<Integer, Cashflow> mapCashflow = new HashMap<Integer, Cashflow>();
		List<Cashflow> cashflows = getServiceCashflowByContractId(cotraId);
		for (Cashflow cashflow : cashflows)
		{
			if(cashflow.getService() != null && ServiceEntityField.INSFEE.equals(cashflow.getService().getCode())) {
				mapCashflow.put(cashflow.getNumInstallment(), cashflow);
			}
		}
		return mapCashflow;
	}
	
	/**
	 * 
	 * @param cotraId
	 * @return
	 */
	private Map<Integer, Cashflow> getServicingCashflow(Long cotraId) {
		Map<Integer, Cashflow> mapCashflow = new HashMap<Integer, Cashflow>();
		List<Cashflow> cashflows = getServiceCashflowByContractId(cotraId);
		for (Cashflow cashflow : cashflows)
		{
			if(cashflow.getService() != null && ServiceEntityField.SERFEE.equals(cashflow.getService().getCode())) {
				mapCashflow.put(cashflow.getNumInstallment(), cashflow);
			}
		}
		return mapCashflow;
	}
	
	/**
	 * 
	 * @param cotraId
	 * @return
	 */
	private List<Cashflow> getServiceCashflowByContractId(Long cotraId) {
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<Cashflow>(Cashflow.class);
		restrictions.addCriterion(Restrictions.eq(CASHFLOW_TYPE, ECashflowType.FEE));
		restrictions.addCriterion(Restrictions.eq(CANCEL, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.eq(CONTRACT + "." + ID, cotraId));
		restrictions.addOrder(Order.asc(NUM_INSTALLMENT));
		List<Cashflow> cashflows = list(restrictions);
		return cashflows;
	}
	
	/**
	 * @param dealerType
	 * @param dealer
	 * @param reference
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	protected List<Contract> getRemainingBalanceContracts(EDealerType dealerType, Dealer dealer, String reference, Date endDate) {
		List<Contract> contracts = new ArrayList<>();		
		String query = 
				"SELECT "
				+ " c.cotra_id, "
				+ " c.cotra_va_reference, "
				+ " c.cotra_dt_start, "
				+ " c.cotra_dt_first_installment, "
				+ " c.cotra_am_ti_financed_usd, "
				+ " c.cotra_nu_term, "
				+ " c.frque_code, "
				+ " c.cotra_rt_interest_rate, "
				+ " c.cotra_rt_irr_rate, "
				+ " c.costa_code, "
				+ " bapp.appli_bo_id, "
				+ " bapp.perso_va_lastname_en, "
				+ " bapp.perso_va_firstname_en "
				+ " FROM td_contract c"
				+ " inner join tu_dealer dea on dea.dea_id = c.dea_id" 
				+ " inner join td_contract_applicant capp on capp.cotra_id = c.cotra_id and capp.aptyp_code = 'C'"
				+ " inner join td_bo_applicant bapp on bapp.appli_bo_id = capp.appli_bo_id"
				+ " WHERE c.costa_code not in ('" + ContractWkfStatus.PEN + "')"
				+ " AND c.cotra_dt_start <= '" + DateUtils.getDateLabel(DateUtils.getDateAtEndOfDay(endDate), "yyyy-MM-dd HH:mm:ss") + "'";
						
		if (dealerType != null) {
			query += " AND dea.detyp_code = '" + dealerType + "'";
		}
			
		if (dealer != null) {
			query += " AND dea.dea_id = " + dealer.getId();
		}
		
		if (StringUtils.isNotEmpty(reference)) {
			query += " AND c.cotra_va_reference ilike '%" + reference + "%'";
		}
		
		query += " ORDER BY c.cotra_dt_start desc";
				
		try {
			List<NativeRow> contractRows = executeSQLNativeQuery(query);
			for (NativeRow row : contractRows) {
		      	List<NativeColumn> columns = row.getColumns();
		      	int i = 0;
		      	Contract contract = new Contract();
		      	contract.setId((Long) columns.get(i++).getValue());
		      	contract.setReference((String) columns.get(i++).getValue());
		      	contract.setStartDate((Date) columns.get(i++).getValue());
		      	contract.setFirstDueDate((Date) columns.get(i++).getValue());
		      	contract.setTiFinancedAmount((Double) columns.get(i++).getValue());
		      	contract.setTerm((Integer) columns.get(i++).getValue());
		      	contract.setFrequency(EFrequency.getByCode(columns.get(i++).getValue().toString()));
		      	contract.setInterestRate((Double) columns.get(i++).getValue());
		      	contract.setIrrRate((Double) columns.get(i++).getValue());
		      	contract.setWkfStatus(EWkfStatus.getByCode(columns.get(i++).getValue().toString()));
		      	
		      	List<ContractApplicant> contractApplicants = new ArrayList<>();
		      	
		      	Applicant applicant = new Applicant();
		      	applicant.setId((Long) columns.get(i++).getValue());
		      	applicant.getIndividual().setLastNameEn((String) columns.get(i++).getValue());
		      	applicant.getIndividual().setFirstNameEn((String) columns.get(i++).getValue());
		      	
		      	ContractApplicant contractApplicant = new ContractApplicant();
		      	contractApplicant.setApplicant(applicant);
		      	contractApplicant.setContract(contract);
		      	contractApplicant.setApplicantType(EApplicantType.C);
		      	
		      	contractApplicants.add(contractApplicant);
		      	contract.setContractApplicants(contractApplicants);
		      	
		      	contracts.add(contract);
		    }
		} catch (NativeQueryException e) {
			logger.error(e.getMessage(), e);
		}
		
		return contracts;
	}
	
	/**
	 * @param dealerType
	 * @param dealer
	 * @param reference
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	protected List<Contract> getLeaseTransactionContracts(EDealerType dealerType, Dealer dealer, String reference, Date endDate) {
		List<Contract> contracts = new ArrayList<>();		
		String query = 
				"SELECT "
				+ " c.con_id, "
				+ " c.con_va_reference, "
				+ " c.con_dt_start, "
				+ " c.con_dt_end, "
				+ " c.con_dt_first_due, "
				+ " c.con_am_ti_financed_amount, "
				+ " c.con_nu_term, "
				+ " c.fre_id, "
				+ " c.con_rt_interest_rate, "
				+ " c.con_rt_irr_rate, "
				+ " c.wkf_sta_id, "
				+ " ca.con_adj_am_ti_unpaid_accrued_interest_receivable_usd, "
				+ " bapp.app_id, "
				+ " bapp.per_lastname_en, "
				+ " bapp.per_firstname_en "
				+ " FROM td_contract c"
				+ " inner join tu_dealer dea on dea.dea_id = c.dea_id" 
				+ " inner join td_contract_applicant capp on capp.con_id = c.con_id and capp.app_typ_id = 1"
				+ " inner join td_applicant bapp on bapp.app_id = capp.app_id"
				+ " left join td_contract_adjustment ca on ca.con_id = c.con_id"
				+ " WHERE c.wkf_sta_id not in ('" + ContractWkfStatus.PEN.getId() + "')"
				+ " AND c.con_dt_start <= '" + DateUtils.getDateLabel(DateUtils.getDateAtEndOfDay(endDate), "yyyy-MM-dd HH:mm:ss") + "'";
						
		if (dealerType != null) {
			query += " AND dea.dea_typ_id = '" + dealerType.getId() + "'";
		}
			
		if (dealer != null) {
			query += " AND dea.dea_id = " + dealer.getId();
		}
		
		if (StringUtils.isNotEmpty(reference)) {
			query += " AND c.con_va_reference ilike '%" + reference + "%'";
		}
		
		query += " ORDER BY c.con_dt_start desc";
				
		try {
			List<NativeRow> contractRows = executeSQLNativeQuery(query);
			for (NativeRow row : contractRows) {
		      	List<NativeColumn> columns = row.getColumns();
		      	int i = 0;
		      	Contract contract = new Contract();
		      	contract.setId((Long) columns.get(i++).getValue());
		      	contract.setReference((String) columns.get(i++).getValue());
		      	contract.setStartDate((Date) columns.get(i++).getValue());
		      	contract.setEndDate((Date) columns.get(i++).getValue());
		      	contract.setFirstDueDate((Date) columns.get(i++).getValue());
		      	contract.setTiFinancedAmount((Double) columns.get(i++).getValue());
		      	contract.setTerm((Integer) columns.get(i++).getValue());
		      	contract.setFrequency(EFrequency.getByCode(columns.get(i++).getValue().toString()));
		      	contract.setInterestRate((Double) columns.get(i++).getValue());
		      	contract.setIrrRate((Double) columns.get(i++).getValue());
		      	contract.setWkfStatus(EWkfStatus.getByCode(columns.get(i++).getValue().toString()));
		      	
		      	/*ContractAdjustment contractAdjustment = new ContractAdjustment();
		      	contractAdjustment.setTiUnpaidAccruedInterestReceivableUsd((Double) columns.get(i++).getValue());
		      	List<ContractAdjustment> contractAdjustments = new ArrayList<>();
		      	contract.setContractAdjustments(contractAdjustments);*/
		      	
		      	List<ContractApplicant> contractApplicants = new ArrayList<>();
		      	
		      	Applicant applicant = new Applicant();
		      	applicant.setId((Long) columns.get(i++).getValue());
		      	applicant.getIndividual().setLastNameEn((String) columns.get(i++).getValue());
		      	applicant.getIndividual().setFirstNameEn((String) columns.get(i++).getValue());
		      	
		      	ContractApplicant contractApplicant = new ContractApplicant();
		      	contractApplicant.setApplicant(applicant);
		      	contractApplicant.setContract(contract);
		      	contractApplicant.setApplicantType(EApplicantType.C);
		      	
		      	contractApplicants.add(contractApplicant);
		      	contract.setContractApplicants(contractApplicants);
		      	
		      	contracts.add(contract);
		    }
		} catch (NativeQueryException e) {
			logger.error(e.getMessage(), e);
		}
		
		return contracts;
	}

	
	
	@Override
	public List<LeaseTransaction> getRemainingBalance(EDealerType dealerType, Dealer dealer, String reference, Date endDate) {
		List<LeaseTransaction> leaseTransactions = new ArrayList<>();
		List<Contract> contracts = getRemainingBalanceContracts(dealerType, dealer, reference, endDate);
		
		int totalContracts = contracts.size();
		int i = 1;
		
		for (Contract contract : contracts) {
			
			if (i % 100 == 0 || i == contracts.size()) {
				System.out.println(i + "/" + totalContracts);
			}
			i++;
			
			List<Cashflow> cashflows = getCashflowsNoCancel(contract.getId());
			LeaseTransaction leaseTransaction = new LeaseTransaction();
			leaseTransaction.setId(contract.getId());
			leaseTransaction.setReference(contract.getReference());
			leaseTransaction.setContractStartDate(contract.getStartDate());
			leaseTransaction.setFirstInstallmentDate(contract.getFirstDueDate());
			leaseTransaction.setFirstNameEn(contract.getApplicant().getIndividual().getFirstNameEn());
			leaseTransaction.setLastNameEn(contract.getApplicant().getIndividual().getLastNameEn());
			leaseTransaction.setInterestRate(contract.getInterestRate());
			leaseTransaction.setIrrRate(MyNumberUtils.getDouble(contract.getIrrRate()) * 100);
			
			CalculationParameter calculationParameter = new CalculationParameter();
			calculationParameter.setInitialPrincipal(contract.getTiFinancedAmount());
			calculationParameter.setNumberOfPeriods(LoanUtils.getNumberOfPeriods(contract.getTerm(), contract.getFrequency()));
			calculationParameter.setPeriodicInterestRate(contract.getInterestRate() / 100d);
			calculationParameter.setFrequency(contract.getFrequency());
			
			Amount principalBalance = getAccountingPrincipalBalance(endDate, cashflows);
			Amount unearnedInterestBalance = getAccountingInterestUnearnedBalance(endDate, cashflows);
			leaseTransaction.getPrincipalBalance().plus(principalBalance);
			leaseTransaction.getUnearnedInterestBalance().plus(unearnedInterestBalance);			
			leaseTransactions.add(leaseTransaction);
		}
		return leaseTransactions;
	}
	
	@Override
	public List<LeaseTransaction> getLeaseTransactions(EDealerType dealerType, Dealer dealer,
			String reference, Date startDate, Date endDate) {
		List<LeaseTransaction> leaseTransactions = new ArrayList<>();		
		startDate = DateUtils.getDateAtBeginningOfDay(startDate);
		endDate = DateUtils.getDateAtEndOfDay(endDate);		
		List<Contract> contracts = getLeaseTransactionContracts(dealerType, dealer, reference, endDate);		
		int totalContracts = contracts.size();
		int i = 1;		
		for (Contract contract : contracts) {			
			if (i % 100 == 0 || i == contracts.size()) {
				System.out.println(i + "/" + totalContracts);
			}
			i++;
			List<Cashflow> cashflows = getCashflowsNoCancel(contract.getId());
			CalculationParameter calculationParameter = new CalculationParameter();
			calculationParameter.setInitialPrincipal(contract.getTiFinancedAmount());
			calculationParameter.setNumberOfPeriods(LoanUtils.getNumberOfPeriods(contract.getTerm(), contract.getFrequency()));
			calculationParameter.setPeriodicInterestRate(contract.getInterestRate() / 100d);
			calculationParameter.setFrequency(contract.getFrequency());
			leaseTransactions.add(getLeaseTransaction(contract, cashflows, startDate, endDate, calculationParameter, null));
		}		
		return leaseTransactions;
	}
	
	
	
	/**
	 * @param dealerType
	 * @param dealer
	 * @param reference
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<LeaseTransaction> getNetLeasings(EDealerType dealerType, Dealer dealer,
			String reference, Date startDate, Date endDate) {
		List<LeaseTransaction> leaseTransactions = new ArrayList<>();		
		startDate = DateUtils.getDateAtBeginningOfDay(startDate);
		endDate = DateUtils.getDateAtEndOfDay(endDate);		
		List<Contract> contracts = getLeaseTransactionContracts(dealerType, dealer, reference, endDate);		
		int totalContracts = contracts.size();
		int i = 1;		
		for (Contract contract : contracts) {			
			if (i % 100 == 0 || i == contracts.size()) {
				logger.debug(i + "/" + totalContracts);
			}
			i++;
			List<Cashflow> feesAndDirectCosts = getFeesAndDirectCosts(contract.getId());
			double feesAndDirectCostsAmountUsd = 0d;
			for (Cashflow cashflow : feesAndDirectCosts) {
				if (EServiceType.listDirectCosts().contains(cashflow.getService().getServiceType())) {
					feesAndDirectCostsAmountUsd += Math.abs(cashflow.getTiInstallmentAmount());
				} else {
					feesAndDirectCostsAmountUsd -= Math.abs(cashflow.getTiInstallmentAmount());
				}
			}
			List<Cashflow> cashflows = getCashflowsNoCancel(contract.getId());
			
			CalculationParameter calculationParameter = new CalculationParameter();
			calculationParameter.setInitialPrincipal(contract.getTiFinancedAmount());
			calculationParameter.setNumberOfPeriods(LoanUtils.getNumberOfPeriods(contract.getTerm(), contract.getFrequency()));
			calculationParameter.setPeriodicInterestRate(contract.getInterestRate() / 100d);
			calculationParameter.setFrequency(contract.getFrequency());
			calculationParameter.setNumberOfPrincipalGracePeriods(MyNumberUtils.getInteger(contract.getNumberOfPrincipalGracePeriods()));
			
			CalculationParameter calculationParameter2 = new CalculationParameter();
			calculationParameter2.setInitialPrincipal(contract.getTiFinancedAmount() + feesAndDirectCostsAmountUsd);
			calculationParameter2.setNumberOfPeriods(LoanUtils.getNumberOfPeriods(contract.getTerm(), contract.getFrequency()));
			calculationParameter2.setPeriodicInterestRate(contract.getInterestRate() / 100d);
			calculationParameter2.setFrequency(contract.getFrequency());
			calculationParameter2.setNumberOfPrincipalGracePeriods(MyNumberUtils.getInteger(contract.getNumberOfPrincipalGracePeriods()));
			
			AmortizationSchedules amortizationSchedules = financeCalculationService.getAmortizationSchedules(contract.getStartDate(), 
					contract.getFirstDueDate(), calculationParameter2);
			List<Schedule> schedules = amortizationSchedules.getSchedules();
			for (Cashflow cashflow : cashflows) {
				for (Schedule schedule : schedules) {
					if (DateUtils.isSameDay(cashflow.getInstallmentDate(), schedule.getInstallmentDate())) {
						if (cashflow.getCashflowType().equals(ECashflowType.CAP)) {
							cashflow.setTiInstallmentAmount(schedule.getPrincipalAmount());
							cashflow.setTeInstallmentAmount(cashflow.getTiInstallmentAmount());
						} else if (cashflow.getCashflowType().equals(ECashflowType.IAP)) {
							cashflow.setTiInstallmentAmount(schedule.getInterestAmount());
							cashflow.setTeInstallmentAmount(cashflow.getTiInstallmentAmount());
						}
						break;
					}
				}
			}
			
			leaseTransactions.add(getLeaseTransaction(contract, cashflows, startDate, endDate, calculationParameter, calculationParameter2));
		}		
		return leaseTransactions;
	}
	
	/**
	 * @param dealerType
	 * @param dealer
	 * @param reference
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<ServiceTransaction> getServiceTransactions(EDealerType dealerType, Dealer dealer,
			String reference, Date startDate, Date endDate) {
		List<ServiceTransaction> serviceTransactions = new ArrayList<>();
		List<Contract> contracts = getLeaseTransactionContracts(dealerType, dealer, reference, endDate);
		for (Contract contract : contracts) {
			
			
			List<Cashflow> feesAndDirectCosts = getFeesAndDirectCosts(contract.getId());
			double feesAndDirectCostsAmountUsd = 0d;
			for (Cashflow cashflow : feesAndDirectCosts) {
				if (EServiceType.listDirectCosts().contains(cashflow.getService().getServiceType())) {
					feesAndDirectCostsAmountUsd += Math.abs(cashflow.getTiInstallmentAmount());
				} else {
					feesAndDirectCostsAmountUsd -= Math.abs(cashflow.getTiInstallmentAmount());
				}
			}
			List<Cashflow> cashflows = getCashflowsNoCancel(contract.getId());
			
			CalculationParameter calculationParameter = new CalculationParameter();
			calculationParameter.setInitialPrincipal(contract.getTiFinancedAmount());
			calculationParameter.setNumberOfPeriods(LoanUtils.getNumberOfPeriods(contract.getTerm(), contract.getFrequency()));
			calculationParameter.setPeriodicInterestRate(contract.getInterestRate() / 100d);
			calculationParameter.setFrequency(contract.getFrequency());
			calculationParameter.setNumberOfPrincipalGracePeriods(MyNumberUtils.getInteger(contract.getNumberOfPrincipalGracePeriods()));
			
			CalculationParameter calculationParameter2 = new CalculationParameter();
			calculationParameter2.setInitialPrincipal(contract.getTiFinancedAmount() + feesAndDirectCostsAmountUsd);
			calculationParameter2.setNumberOfPeriods(LoanUtils.getNumberOfPeriods(contract.getTerm(), contract.getFrequency()));
			calculationParameter2.setPeriodicInterestRate(contract.getInterestRate() / 100d);
			calculationParameter2.setFrequency(contract.getFrequency());
			calculationParameter2.setNumberOfPrincipalGracePeriods(MyNumberUtils.getInteger(contract.getNumberOfPrincipalGracePeriods()));
			
			
			Date eventDate = null;
			if (contract.getWkfStatus() != ContractWkfStatus.FIN) {
				eventDate = getEventDate(contract.getHistories(), contract.getWkfStatus());
			}
			Date lastInstallmentDate = getLastInstallmentDate(cashflows);
			Date calculStartDate = startDate;
			Date calculEndDate = endDate;
			
			if (calculStartDate.before(contract.getStartDate())) {
				calculStartDate = contract.getStartDate();
			}
			if (calculEndDate.after(lastInstallmentDate)) {
				calculEndDate = lastInstallmentDate;
			}
			
			Map<String, ServiceCalculation> services = getServices(feesAndDirectCosts);
			
			for (Iterator<String> iter = services.keySet().iterator(); iter.hasNext(); ) {
				
				ServiceCalculation serviceCalculation = services.get(iter.next());
				
				ServiceTransaction serviceTransaction = new ServiceTransaction();
				serviceTransaction.setId(contract.getId() * serviceCalculation.getService().getId());
				serviceTransaction.setReference(contract.getReference());
				serviceTransaction.setContractStartDate(contract.getStartDate());
				serviceTransaction.setFirstInstallmentDate(contract.getFirstDueDate());				
				serviceTransaction.setServiceDescEn(serviceCalculation.getService().getDescEn());
				
				double serviceAmountUsd = serviceCalculation.getServiceAmountUsd();
				
				System.out.println(serviceCalculation.getService().getCode() + " : " + serviceAmountUsd);
				
				GLFServiceDailyCalculatorImpl calculator = new GLFServiceDailyCalculatorImpl();
				ServiceIncomeSchedules schedules2 = calculator.getSchedules(contract, DateUtils.getDateAtBeginningOfDay(contract.getStartDate()), 
						DateUtils.getDateAtBeginningOfDay(contract.getFirstDueDate()), serviceAmountUsd, calculationParameter, calculationParameter2, 
						serviceCalculation.getCashflows(), DateUtils.getDateAtBeginningOfDay(calculStartDate), DateUtils.getDateAtBeginningOfDay(calculEndDate),
						contract.getWkfStatus(), eventDate);
				
				System.out.println(schedules2);
				
				Amount revenue = new Amount(0d, 0d, 0d, 8);
				Amount accruedIncome = new Amount(0d, 0d, 0d, 8);
				Amount principalRepayment = new Amount(0d, 0d, 0d);
				
				for (int j = 0; j < schedules2.getSchedules().size(); j++) {
					ServiceIncomeSchedule dailySchedule = schedules2.getSchedules().get(j);
					if (DateUtils.getDateAtBeginningOfDay(startDate).compareTo(DateUtils.getDateAtBeginningOfDay(dailySchedule.getPeriodStartDate())) <= 0
							&& DateUtils.getDateAtBeginningOfDay(endDate).compareTo(DateUtils.getDateAtBeginningOfDay(dailySchedule.getPeriodEndDate())) >= 0) {
						
						double principal = dailySchedule.getPrincipalRepayment();
						principalRepayment.plus(new Amount(principal, 0d, principal));
						revenue.plus(new Amount(dailySchedule.getRevenue(), 0d, dailySchedule.getRevenue()));
						if (!dailySchedule.isContractInOverdueMoreThanOneMonth() && DateUtils.isSameDay(dailySchedule.getInstallmentDate(), lastInstallmentDate)) {
							revenue.plus(new Amount(-1 * dailySchedule.getAccruedIncome(), 0d, -1 * dailySchedule.getAccruedIncome()));
							accruedIncome = new Amount(0d, 0d, 0d, 8);
						} else {
							accruedIncome = new Amount(dailySchedule.getAccruedIncome(), 0d, dailySchedule.getAccruedIncome(), 8);
						}
					}
				}			
				serviceTransaction.setRevenue(revenue);
				serviceTransaction.setPrincipalRepayment(principalRepayment);
				serviceTransaction.setAccruedIncome(accruedIncome);
				if (eventDate == null || endDate.compareTo(eventDate) < 0) {
					serviceTransaction.getPrincipalBalance().plus(getAccountingServiceBalance(endDate, serviceCalculation.getCashflows()));
				} else {
					serviceTransaction.getPrincipalBalance().plus(new Amount(0d, 0d, 0d));
				}
				
				serviceTransactions.add(serviceTransaction);
			}
		}
		
		return serviceTransactions;
	}
	
	
	/**
	 * @param feesAndDirectCosts
	 * @return
	 */
	private Map<String, ServiceCalculation> getServices(List<Cashflow> feesAndDirectCosts) {
		Map<String, ServiceCalculation> servicesMap = new HashMap<>();
		for (Cashflow cashflow : feesAndDirectCosts) {
			ServiceCalculation serviceCalculation = servicesMap.get(cashflow.getService().getCode());
			if (serviceCalculation == null) {
				serviceCalculation = new ServiceCalculation();
				serviceCalculation.setService(cashflow.getService());
				serviceCalculation.setCashflows(new ArrayList<Cashflow>());
				servicesMap.put(cashflow.getService().getCode(), serviceCalculation);
			}
			serviceCalculation.getCashflows().add(cashflow);
			serviceCalculation.setServiceAmountUsd(serviceCalculation.getServiceAmountUsd() + Math.abs(cashflow.getTiInstallmentAmount()));
		}
		
		return servicesMap;
	}
	
	/**
	 * @param contract
	 * @param cashflows
	 * @param startDate
	 * @param endDate
	 * @param calculationParameter
	 * @param calculationParameter2
	 * @return
	 */
	private LeaseTransaction getLeaseTransaction(Contract contract, List<Cashflow> cashflows, Date startDate, Date endDate, 
			CalculationParameter calculationParameter, CalculationParameter calculationParameter2) {
		LeaseTransaction leaseTransaction = new LeaseTransaction();
		leaseTransaction.setId(contract.getId());
		leaseTransaction.setReference(contract.getReference());
		leaseTransaction.setContractStartDate(contract.getStartDate());
		leaseTransaction.setFirstInstallmentDate(contract.getFirstDueDate());
		leaseTransaction.setInterestRate(contract.getInterestRate());
		leaseTransaction.setIrrRate(MyNumberUtils.getDouble(contract.getIrrRate()) * 100);
				
		Date eventDate = null;
		if (contract.getWkfStatus() != ContractWkfStatus.FIN) {
			eventDate = getEventDate(contract.getHistories(), contract.getWkfStatus());
		}
		Date lastInstallmentDate = getLastInstallmentDate(cashflows);
		Date calculStartDate = startDate;
		Date calculEndDate = endDate;
		
		if (calculStartDate.before(contract.getStartDate())) {
			calculStartDate = contract.getStartDate();
		}
		if (calculEndDate.after(lastInstallmentDate)) {
			calculEndDate = lastInstallmentDate;
		}
		
		GLFInterestIncomeDailyCalculatorImpl calculator = new GLFInterestIncomeDailyCalculatorImpl();
		InterestIncomeSchedules schedules2 = calculator.getSchedules(contract, DateUtils.getDateAtBeginningOfDay(contract.getStartDate()), 
				DateUtils.getDateAtBeginningOfDay(contract.getFirstDueDate()), calculationParameter, calculationParameter2, cashflows, 
				DateUtils.getDateAtBeginningOfDay(calculStartDate), DateUtils.getDateAtBeginningOfDay(calculEndDate),
				contract.getWkfStatus(), eventDate);
		
		Amount interestRevenue = new Amount(0d, 0d, 0d, 8);
		Amount interestInSuspend = new Amount(0d, 0d, 0d, 8);
		Amount interestInSuspendCumulated = new Amount(0d, 0d, 0d, 8);
		Amount interestIncomeRepayment = new Amount(0d, 0d, 0d);
		Amount interestIncomeReceivable = new Amount(0d, 0d, 0d, 8);
		Amount principalRepayment = new Amount(0d, 0d, 0d);
		Amount penalty = new Amount(0d, 0d, 0d);
		
		for (int j = 0; j < schedules2.getSchedules().size(); j++) {
			InterestIncomeSchedule dailySchedule = schedules2.getSchedules().get(j);
			if (DateUtils.getDateAtBeginningOfDay(startDate).compareTo(DateUtils.getDateAtBeginningOfDay(dailySchedule.getPeriodStartDate())) <= 0
					&& DateUtils.getDateAtBeginningOfDay(endDate).compareTo(DateUtils.getDateAtBeginningOfDay(dailySchedule.getPeriodEndDate())) >= 0) {
				if (dailySchedule.isContractInOverdueMoreThanOneMonth()) {
					interestInSuspendCumulated = new Amount(dailySchedule.getInterestInSuspendCumulated(), 0d, dailySchedule.getInterestInSuspendCumulated(), 8);
				} else {
					interestInSuspendCumulated = new Amount(0d, 0d, 0d, 8);
				}
				double interest = dailySchedule.getInterestIncomeRepayment();
				double principal = dailySchedule.getPrincipalRepayment();
				principalRepayment.plus(new Amount(principal, 0d, principal));
				interestIncomeRepayment.plus(new Amount(interest, 0d, interest));
				penalty.plus(new Amount(dailySchedule.getPenalty(), 0d, dailySchedule.getPenalty()));
				interestRevenue.plus(new Amount(dailySchedule.getInterestRevenue(), 0d, dailySchedule.getInterestRevenue()));
				if (!dailySchedule.isContractInOverdueMoreThanOneMonth() && interest > 0 && DateUtils.isSameDay(dailySchedule.getInstallmentDate(), lastInstallmentDate)) {
					interestRevenue.plus(new Amount(-1 * dailySchedule.getInterestIncomeReceivable(), 0d, -1 * dailySchedule.getInterestIncomeReceivable()));
					interestIncomeReceivable = new Amount(0d, 0d, 0d, 8);
				} else {
					interestIncomeReceivable = new Amount(dailySchedule.getInterestIncomeReceivable(), 0d, dailySchedule.getInterestIncomeReceivable(), 8);
				}
				if (dailySchedule.isContractInOverdueMoreThanOneMonth()) {
					interestInSuspend.plus(new Amount(dailySchedule.getInterestInSuspend(), 0d, dailySchedule.getInterestInSuspend()));
				}
			}
		}
		
		leaseTransaction.setInterestRevenue(interestRevenue);
		leaseTransaction.setInterestInSuspend(interestInSuspend);
		leaseTransaction.setInterestInSuspendCumulated(interestInSuspendCumulated);
		leaseTransaction.setInterestIncome(interestIncomeRepayment);
		leaseTransaction.setPrincipalRepayment(principalRepayment);
		leaseTransaction.setInterestReceivable(interestIncomeReceivable);
		leaseTransaction.setPenalty(penalty);			
		if (eventDate == null || endDate.compareTo(eventDate) < 0) {
			leaseTransaction.getPrincipalBalance().plus(getAccountingPrincipalBalance(endDate, cashflows));
			leaseTransaction.getUnearnedInterestBalance().plus(getAccountingInterestUnearnedBalance(endDate, cashflows));
		} else {
			leaseTransaction.getPrincipalBalance().plus(new Amount(0d, 0d, 0d));
			leaseTransaction.getUnearnedInterestBalance().plus(new Amount(0d, 0d, 0d));
		}
		return leaseTransaction;
	}
	
	/**
	 * @see com.nokor.efinance.GLFLeasingAccountingService.service.accounting.AccountingService#getInsuranceIncomesAdjustment(com.nokor.efinance.core.dealer.model.Dealer, java.lang.String, java.util.Date, java.util.Date)
	 */
	@Override
	public List<InsuranceIncome> getInsuranceIncomes(EDealerType dealerTyep, Dealer dealer, String reference, Date startDate, Date endDate) {
		List<InsuranceIncome> insuranceIncomes = new ArrayList<>();
		
		startDate = DateUtils.getDateAtBeginningOfDay(startDate);
		endDate = DateUtils.getDateAtBeginningOfDay(endDate);
		
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<>(Quotation.class);
		restrictions.addCriterion(Restrictions.in("wkfStatus", 
				new EWkfStatus[] {QuotationWkfStatus.ACT, QuotationWkfStatus.ACG, QuotationWkfStatus.RVG, QuotationWkfStatus.RCG, QuotationWkfStatus.LCG}));
		
		restrictions.addCriterion(Restrictions.le("contractStartDate", endDate));

		if (dealer != null) {
			restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, dealer.getId()));
		}
		
		if (StringUtils.isNotEmpty(reference)) {
			restrictions.addCriterion(Restrictions.ilike(REFERENCE, reference, MatchMode.ANYWHERE));
		}
		
		List<Quotation> quotations = list(restrictions);
		for (Quotation quotation : quotations) {
			
			QuotationService insuranceService = quotation.getQuotationService("INSFEE");
			if (insuranceService != null) {
			
				CalculationParameter calculationParameter = new CalculationParameter();
				calculationParameter.setInitialPrincipal(quotation.getTiFinanceAmount());
				calculationParameter.setNumberOfPeriods(LoanUtils.getNumberOfPeriods(quotation.getTerm(), quotation.getFrequency()));
				calculationParameter.setPeriodicInterestRate(quotation.getInterestRate() / 100d);
				calculationParameter.setFrequency(quotation.getFrequency());
				calculationParameter.setInsuranceFee(insuranceService.getTiPrice());
				
				GLFInsuranceIncomeCalculatorImpl calculator = new GLFInsuranceIncomeCalculatorImpl();
				Long cotraId = quotation.getContract().getId();
				InsuranceIncomeSchedules insuranceIncomeSchedules  = null;
				if (insuranceService.isSplitWithInstallment()) {
					Map<Integer, Cashflow> insuranceClashflow = getInsuranceCashflow(cotraId);
					insuranceIncomeSchedules = calculator.getSchedules(quotation.getContractStartDate(), quotation.getFirstDueDate(), calculationParameter, insuranceClashflow);
					System.out.println(insuranceIncomeSchedules.toString());
				}
				else {
					insuranceIncomeSchedules = calculator.getSchedules(quotation.getContractStartDate(), quotation.getFirstDueDate(), calculationParameter);
				}
				InsuranceIncome insuranceIncome = new InsuranceIncome();
				insuranceIncome.setId(quotation.getId());
				insuranceIncome.setReference(quotation.getReference());
				insuranceIncome.setContractStartDate(quotation.getContractStartDate());
				insuranceIncome.setFirstInstallmentDate(quotation.getFirstDueDate());
				insuranceIncome.setFirstNameEn(quotation.getApplicant().getIndividual().getFirstNameEn());
				insuranceIncome.setLastNameEn(quotation.getApplicant().getIndividual().getLastNameEn());
				
				for (InsuranceIncomeSchedule insuranceIncomeSchedule : insuranceIncomeSchedules.getSchedules()) {
					
					Date calculEndDate = DateUtils.getDateAtEndOfMonth(endDate);
					Date calculStartDate = DateUtils.getDateAtBeginningOfMonth(startDate);
					if (calculStartDate.compareTo(insuranceIncomeSchedule.getPeriodStartDate()) <= 0
							&& calculEndDate.compareTo(insuranceIncomeSchedule.getPeriodEndDate()) >= 0) {
//					if (startDate.compareTo(insuranceIncomeSchedule.getPeriodStartDate()) <= 0
//							&& endDate.compareTo(insuranceIncomeSchedule.getPeriodEndDate()) >= 0) {
						insuranceIncome.getInsuranceIncomeDistribution2().plus(new Amount(insuranceIncomeSchedule.getInsuranceIncomeDistribution2(), 0d, insuranceIncomeSchedule.getInsuranceIncomeDistribution2()));
						insuranceIncome.getInsuranceIncomeDistribution3().plus(new Amount(insuranceIncomeSchedule.getInsuranceIncomeDistribution3(), 0d, insuranceIncomeSchedule.getInsuranceIncomeDistribution3()));
						insuranceIncome.getCumulativeBalance().plus(new Amount(insuranceIncomeSchedule.getCumulativeBalance(), 0d, insuranceIncomeSchedule.getCumulativeBalance()));
						insuranceIncome.getAccountReceivable().plus(new Amount(insuranceIncomeSchedule.getAccountReceivable(), 0d, insuranceIncomeSchedule.getAccountReceivable()));
						insuranceIncome.getInsuranceIncomeReceived().plus(new Amount(insuranceIncomeSchedule.getInsuranceIncomeReceived(), 0d, insuranceIncomeSchedule.getInsuranceIncomeReceived()));
						
						insuranceIncome.getInsuranceIncomeInSuspendCumulated().plus(new Amount(insuranceIncomeSchedule.getInsuranceIncomeInSuspendCumulated(), 0d, insuranceIncomeSchedule.getInsuranceIncomeInSuspendCumulated()));
						
						//Insurance income calculate as daily base 
						Date calculEndDate2 = DateUtils.addDaysDate(insuranceIncomeSchedule.getPeriodEndDate(), 1);
						Date calculStartDate2 = insuranceIncomeSchedule.getPeriodStartDate();
						if (DateUtils.isBeforeDay(calculStartDate2, startDate)) {
							calculStartDate2 = startDate;
						}
						if (DateUtils.isBeforeDay(endDate, calculEndDate2)) {
							calculEndDate2 = endDate;
						}
						
						long coeff = DateUtils.getDiffInDaysPlusOneDay(insuranceIncomeSchedule.getPeriodEndDate(), insuranceIncomeSchedule.getPeriodStartDate()); // DateUtils.getDiffInDaysPlusOneDay(periodEndDate, periodStartDate);
						long nbDays = 0;
						if (DateUtils.isAfterDay(insuranceIncomeSchedule.getPeriodEndDate(), calculEndDate2)) {
							nbDays = DateUtils.getDiffInDaysPlusOneDay(calculEndDate2, calculStartDate2);
						} else {
							nbDays = coeff - DateUtils.getDiffInDays(calculStartDate2, insuranceIncomeSchedule.getPeriodStartDate());
						}
						
						logger.info("nbDays - [" + nbDays + "]");
						logger.info("coeff - [" + coeff + "]");
						
						double realInsuranceIncomeDistributedInNDays = 0d;
						double insuranceIncomeInSuspendInNDays = 0d;
						double insuranceIncomeInSuspendCumulatedInNDays = 0d;
						double insuranceIncomeInSuspendCumulatd = insuranceIncomeSchedule.getInsuranceIncomeInSuspendCumulated() - insuranceIncomeSchedule.getInsuranceIncomeInSuspend();

						double insuranceIncomeLatePayment = insuranceIncomeSchedule.getInsuranceIncomeRevenueLatePayment();
						double insuranceIncomeCal = insuranceIncomeSchedule.getRealInsuranceIncomeDistributed() - insuranceIncomeLatePayment;
						
						realInsuranceIncomeDistributedInNDays = MyMathUtils.roundAmountTo((insuranceIncomeCal / coeff) * nbDays) + insuranceIncomeLatePayment;
						insuranceIncomeInSuspendInNDays = MyMathUtils.roundAmountTo((insuranceIncomeSchedule.getInsuranceIncomeInSuspend() / coeff) * nbDays);
						insuranceIncomeInSuspendCumulatedInNDays = insuranceIncomeInSuspendCumulatd + insuranceIncomeInSuspendInNDays;
						
						insuranceIncome.getRealInsuranceIncomeDistributed().plus(new Amount(realInsuranceIncomeDistributedInNDays, 0d, realInsuranceIncomeDistributedInNDays));
						insuranceIncome.getInsuranceIncomeInSuspend().plus(new Amount(insuranceIncomeInSuspendInNDays, 0d, insuranceIncomeInSuspendInNDays));
						insuranceIncome.getInsuranceIncomeInSuspendCumulated().plus(new Amount(insuranceIncomeInSuspendCumulatedInNDays, 0d, insuranceIncomeInSuspendCumulatedInNDays));
					}
					if (insuranceIncomeSchedule.getPeriodEndDate().compareTo(endDate) >= 0
							&& insuranceIncome.getUnearnedInsuranceIncome().getTiAmount() == null) {
						insuranceIncome.setUnearnedInsuranceIncome(new Amount(insuranceIncomeSchedule.getUnearnedInsuranceIncome(), 0d, insuranceIncomeSchedule.getUnearnedInsuranceIncome()));
					}
				}
				insuranceIncomes.add(insuranceIncome);							
			}
		}
		
		return insuranceIncomes;
	}

	/**
	 * @param calculDate
	 * @param restrictions
	 * @return
	 */
	public List<LeasesReport> getLeaseReports(Date calculDate, BaseRestrictions<Contract> restrictions) {
		
		List<Contract> contracts = list(restrictions);
		List<LeasesReport> leasesReports = new ArrayList<>();
		calculDate = DateUtils.getDateAtEndOfDay(calculDate);
		
		int i = 0;
		int nbContract = contracts.size();
		
		for (Contract contract : contracts) {
			i++;
			// if (i % 500 == 0 || i == nbContract) {
				System.out.println("LeasesReport === " + i + "/" + nbContract);
			//}
			
			LeasesReport lease = new LeasesReport();
			Applicant applicant = contract.getApplicant();
			Individual individual = applicant.getIndividual();
			Asset asset = contract.getAsset();
			Address address = individual.getMainAddress();
			Dealer dealer = contract.getDealer();
			
			List<Cashflow> cashflows = cashflowService.getNativeCashflowsNoCancel(contract.getId());
			
			Employment emp = individual.getCurrentEmployment();
			
			lease.setId(contract.getId());
			lease.setPoNo("");
			lease.setLidNo(contract.getReference());
			lease.setWkfStatus(contract.getWkfStatus());
			lease.setFullName(individual.getLastNameEn() + " " + individual.getFirstNameEn());
			
			if (emp != null) {
				lease.setBusinessType(emp.getEmploymentStatus().getDescEn());
				lease.setBusinessIndustry(emp.getEmploymentIndustry().getDescEn());
			}
			// TODO YLY
			// lease.setTel(applicant.getMobilePhone());
			
			lease.setHouseNo(StringUtils.defaultString(address.getHouseNo()));
			lease.setStreet(StringUtils.defaultString(address.getStreet()));
			lease.setVillage(address.getVillage().getDescEn());
			lease.setVillageKh(StringUtils.defaultString(address.getVillage().getDesc()));
			lease.setCommune(address.getCommune().getDescEn());
			lease.setCommuneKh(StringUtils.defaultString(address.getCommune().getDesc()));
			lease.setDistrict(address.getDistrict().getDescEn());
			lease.setDistrictKh(StringUtils.defaultString(address.getDistrict().getDesc()));
			lease.setProvince(address.getProvince().getDescEn());
			lease.setProvinceKh(StringUtils.defaultString(address.getProvince().getDesc()));
			
			lease.setVillage(address.getVillage().getDescEn());
			lease.setCommune(address.getCommune().getDescEn());
			lease.setDistrict(address.getDistrict().getDescEn());
			lease.setProvince(address.getProvince().getDescEn());
			lease.setSex(individual.getGender().getDesc());
			lease.setDealerName(dealer.getNameEn());
			lease.setAssetModel(asset.getDescEn());
			lease.setCoName("");
			lease.setDateOfContract(contract.getSigatureDate());
			lease.setAssetPrice(asset.getTiAssetPrice() == null ? 0.0d : asset.getTiAssetPrice());
			lease.setTerm(contract.getTerm());
			lease.setFirstInstallmentDate(contract.getFirstDueDate());
			lease.setRate(contract.getInterestRate() == null ? 0.0d : contract.getInterestRate());
			lease.setIrrMonth(contract.getIrrRate() == null ? 0.0d : contract.getIrrRate() * 100);
			if (lease.getIrrMonth() != null) {
				lease.setIrrYear(lease.getIrrMonth() * 12);
			}
			lease.setLoanAmount(contract.getTiFinancedAmount() == null ? 0.0d : contract.getTiFinancedAmount());
			lease.setNbOverdueInDays(contractService.getNbOverdueInDays(calculDate, cashflows));
			lease.setDownPay(contract.getTiAdvancePaymentAmount() == null ? 0.0d : contract.getTiAdvancePaymentAmount());
			lease.setAdvPaymentPer(contract.getAdvancePaymentPercentage() == null ? 0.0d : contract.getAdvancePaymentPercentage());
			
			lease.setInsurance(getServiceFee(ServiceEntityField.INSFEE, cashflows));
			lease.setRegistration(getServiceFee(ServiceEntityField.REGFEE, cashflows));
			lease.setServiceFee(getServiceFee(ServiceEntityField.SERFEE, cashflows));
			
			lease.setAdvPayment(MyMathUtils.roundAmountTo(lease.getDownPay() 
					+ getFirstInstallmentServiceFee(ServiceEntityField.INSFEE, cashflows) 
					+ getFirstInstallmentServiceFee(ServiceEntityField.REGFEE, cashflows)
					+ getFirstInstallmentServiceFee(ServiceEntityField.SERFEE, cashflows)));
			lease.setSecondPay(MyMathUtils.roundAmountTo(lease.getAssetPrice() - lease.getAdvPayment()));
			
			lease.setTotalInt(MyMathUtils.roundAmountTo(contract.getTiFinancedAmount() * contract.getTerm() * (contract.getInterestRate() / 100)));
			if (contract.getWkfStatus().equals(ContractWkfStatus.EAR)) {
				Date earlySettlementDate = getEventDate(contract.getHistories(), ContractWkfStatus.EAR);
				if (earlySettlementDate.compareTo(calculDate) > 0) {
					ContractAdjustment contractAdjustment = contract.getContractAdjustment();
					lease.setPrinBalance(getTheoricalPrincipalBalance(calculDate, cashflows).getTiAmount());
					lease.setRealPrinBalance(getPrincipalBalance(calculDate, cashflows).getTiAmount());
					lease.setIntBalance(MyMathUtils.roundAmountTo(MyNumberUtils.getDouble(contractAdjustment.getTiAdjustmentInterest()) + MyNumberUtils.getDouble(getTheoricalInterestUnearnedBalance(calculDate, cashflows).getTiAmount())));
					lease.setRealIntBalance(MyMathUtils.roundAmountTo(MyNumberUtils.getDouble(contractAdjustment.getTiAdjustmentInterest()) + MyNumberUtils.getDouble(getInterestUnearnedBalance(calculDate, cashflows).getTiAmount())));
					lease.setTotalReceive(MyMathUtils.roundAmountTo(MyNumberUtils.getDouble(lease.getIntBalance()) + MyNumberUtils.getDouble(lease.getPrinBalance())));
					lease.setRealTotalReceive(MyMathUtils.roundAmountTo(MyNumberUtils.getDouble(lease.getRealIntBalance()) + MyNumberUtils.getDouble(lease.getRealPrinBalance())));
				} else {
					lease.setPrinBalance(0d);
					lease.setRealPrinBalance(0d);
					lease.setIntBalance(0d);
					lease.setRealIntBalance(0d);
					lease.setTotalReceive(0d);
					lease.setRealTotalReceive(0d);
				}
			} else if (contract.getWkfStatus().equals(ContractWkfStatus.LOS)
					|| contract.getWkfStatus().equals(ContractWkfStatus.REP)
					|| contract.getWkfStatus().equals(ContractWkfStatus.THE)
					|| contract.getWkfStatus().equals(ContractWkfStatus.ACC)
					|| contract.getWkfStatus().equals(ContractWkfStatus.FRA)
					|| contract.getWkfStatus().equals(ContractWkfStatus.WRI)) {
				Date lossDate = getEventDate(contract.getHistories(), contract.getWkfStatus());
				
				if (lossDate.compareTo(calculDate) > 0) {
					ContractAdjustment contractAdjustment = contract.getContractAdjustment();
					lease.setPrinBalance(getTheoricalPrincipalBalance(calculDate, cashflows).getTiAmount());
					lease.setRealPrinBalance(contractAdjustment.getTiAdjustmentPrincipal() + getPrincipalBalance(calculDate, cashflows).getTiAmount());
					lease.setIntBalance(MyMathUtils.roundAmountTo(getTheoricalInterestUnearnedBalance(calculDate, cashflows).getTiAmount()));
					lease.setRealIntBalance(contractAdjustment.getTiAdjustmentInterest() + MyMathUtils.roundAmountTo(getInterestUnearnedBalance(calculDate, cashflows).getTiAmount()));
					lease.setTotalReceive(MyMathUtils.roundAmountTo(MyNumberUtils.getDouble(lease.getIntBalance()) + MyNumberUtils.getDouble(lease.getPrinBalance())));
					lease.setRealTotalReceive(MyMathUtils.roundAmountTo(MyNumberUtils.getDouble(lease.getRealIntBalance()) + MyNumberUtils.getDouble(lease.getRealPrinBalance())));
				} else {
					lease.setPrinBalance(0d);
					lease.setRealPrinBalance(0d);
					lease.setIntBalance(0d);
					lease.setRealIntBalance(0d);
					lease.setTotalReceive(0d);
					lease.setRealTotalReceive(0d);
				}
			} else if (contract.getWkfStatus().equals(ContractWkfStatus.CLO)) {
				lease.setPrinBalance(0d);
				lease.setRealPrinBalance(0d);
				lease.setIntBalance(0d);
				lease.setRealIntBalance(0d);
				lease.setTotalReceive(0d);
				lease.setRealTotalReceive(0d);
			} else {
				lease.setPrinBalance(getTheoricalPrincipalBalance(calculDate, cashflows).getTiAmount());
				lease.setRealPrinBalance(getPrincipalBalance(calculDate, cashflows).getTiAmount());
				lease.setIntBalance(MyMathUtils.roundAmountTo(getTheoricalInterestUnearnedBalance(calculDate, cashflows).getTiAmount()));
				lease.setRealIntBalance(MyMathUtils.roundAmountTo(getInterestUnearnedBalance(calculDate, cashflows).getTiAmount()));
				lease.setTotalReceive(MyMathUtils.roundAmountTo(MyNumberUtils.getDouble(lease.getIntBalance()) + MyNumberUtils.getDouble(lease.getPrinBalance())));
				lease.setRealTotalReceive(MyMathUtils.roundAmountTo(MyNumberUtils.getDouble(lease.getRealIntBalance()) + MyNumberUtils.getDouble(lease.getRealPrinBalance())));
			}
			lease.setInstallmentAmount(MyMathUtils.roundAmountTo(MyNumberUtils.getDouble(contract.getTiInstallmentAmount())));
			lease.setPoNumber(cashflows.get(0).getPayment().getReference().replaceAll("-OR", ""));
			leasesReports.add(lease);
		}
		return leasesReports;
	}
	
	/**
	 * @param serviceCode
	 * @param services
	 * @return
	 */
	private Double getFirstInstallmentServiceFee(String code, List<Cashflow> cashflows) {
		double tiServiceAmount = 0d;
		for (Cashflow cashflow : cashflows) {
			if (cashflow.getCashflowType().equals(ECashflowType.FEE) && code.equals(cashflow.getService().getCode()) && cashflow.getNumInstallment() == 0) {
				tiServiceAmount += cashflow.getTiInstallmentAmount();
			}
		}
		return tiServiceAmount;
	}
	
	/**
	 * @param code
	 * @param cashflows
	 * @return
	 */
	private Double getServiceFee(String code, List<Cashflow> cashflows) {
		double tiServiceAmount = 0d;
		for (Cashflow cashflow : cashflows) {
			if (cashflow.getCashflowType().equals(ECashflowType.FEE) && code.equals(cashflow.getService().getCode())) {
				tiServiceAmount += cashflow.getTiInstallmentAmount();
			}
		}
		return tiServiceAmount;
	}
	
	/**
	 * @param cashflows
	 * @return
	 */
	private Date getLastInstallmentDate(List<Cashflow> cashflows) {
		Date lastInstallmentDate = null;
		for (Cashflow cashflow : cashflows) {
			if (!cashflow.isCancel()
					&& (lastInstallmentDate == null || lastInstallmentDate.compareTo(cashflow.getInstallmentDate()) < 0)) {
				lastInstallmentDate = cashflow.getInstallmentDate();
			}
		}
		return lastInstallmentDate;
	}
}
