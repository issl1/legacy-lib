package com.nokor.efinance.core.contract.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.meta.NativeRow;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyMathUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.exception.NativeQueryException;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nokor.common.app.action.model.ActionDefinition;
import com.nokor.common.app.action.model.ActionExecution;
import com.nokor.common.app.action.model.EActionType;
import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.common.app.workflow.service.WkfHistoryItemRestriction;
import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.collection.service.ContractOtherDataService;
import com.nokor.efinance.core.common.IProfileCode;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractProfileQueue;
import com.nokor.efinance.core.contract.model.ContractSimulation;
import com.nokor.efinance.core.contract.model.ContractUserInbox;
import com.nokor.efinance.core.contract.model.ContractWkfHistoryItem;
import com.nokor.efinance.core.contract.model.Transaction;
import com.nokor.efinance.core.contract.model.TransactionItem;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.model.cashflow.TransactionVO;
import com.nokor.efinance.core.contract.service.ContractRestriction;
import com.nokor.efinance.core.contract.service.ContractService;
import com.nokor.efinance.core.contract.service.ContractUtils;
import com.nokor.efinance.core.contract.service.Summary;
import com.nokor.efinance.core.contract.service.UserInboxService;
import com.nokor.efinance.core.contract.service.penalty.impl.PenaltyMRRImpl;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.history.FinHistoryType;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.efinance.core.payment.model.EPaymentType;
import com.nokor.efinance.core.payment.model.EPenaltyCalculMethod;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.payment.service.PaymentService;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.core.shared.contract.ContractEntityField;
import com.nokor.efinance.core.shared.contract.PenaltyVO;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.efinance.core.workflow.PaymentWkfStatus;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.ersys.collab.project.model.Task;
import com.nokor.ersys.collab.project.model.TaskTemplate;
import com.nokor.ersys.collab.project.model.TaskWkfHistoryItem;
import com.nokor.ersys.core.hr.model.PublicHoliday;
import com.nokor.frmk.security.model.SecProfile;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.util.i18n.I18N;

/**
 * Contract service
 * @author mao.heng
 *
 */
@Service("contractService")
public class ContractServiceImpl extends BaseEntityServiceImpl implements ContractService, ContractEntityField, CashflowEntityField, FinServicesHelper {
	
	/** */
	private static final long serialVersionUID = 8654485874847755500L;

	protected Logger LOG = LoggerFactory.getLogger(getClass());

	@Autowired
    private EntityDao dao;

	@Autowired
	private PaymentService paymentService;	
	
	@Autowired
	private UserInboxService userInboxService;
	
	/**
     * @see org.seuksa.frmk.mvc.service.impl.BaseEntityServiceImpl#getDao()
     */
	@Override
	public EntityDao getDao() {
		return dao;
	}
	
	/** 
	 * @param critieria
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Contract> getListContract(BaseRestrictions<Contract> critieria) {
		return list(critieria);
	}
	
	/**
	 * Get contract object by it's reference
	 * @param reference
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public Contract getByReference(String reference) {
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<>(Contract.class);
		restrictions.addCriterion(Restrictions.eq(REFERENCE, reference));
		List<Contract> contracts = list(restrictions);
		if (contracts != null && !contracts.isEmpty()) {
			return contracts.get(0);
		}
		return null;
	}
	
	/**
	 * Get contract object by it's reference
	 * @param reference
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public Contract getByExternalReference(String externalReference) {
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<>(Contract.class);
		List<Criterion> criterions = new ArrayList<>();
		criterions.add(Restrictions.eq(EXTERNAL_REFERENCE, externalReference));
		
		DetachedCriteria simulateSubCriteria = DetachedCriteria.forClass(ContractSimulation.class, "simul");
		simulateSubCriteria.add(Restrictions.eq("simul." + ContractSimulation.EXTERNALREFERENCE, externalReference));
		simulateSubCriteria.setProjection(Projections.projectionList().add(Projections.property("simul.contract.id")));
		criterions.add(Property.forName("id").in(simulateSubCriteria));
		
		restrictions.addCriterion(Restrictions.or(criterions.toArray(new Criterion[criterions.size()])));
		List<Contract> contracts = list(restrictions);
		if (contracts != null && !contracts.isEmpty()) {
			for (Contract contract : contracts) {
				List<ContractUserInbox> inboxs = INBOX_SRV.getContractUserInboxByContract(contract.getId());
				if (inboxs.isEmpty()) {
					return contract;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Get contract object by FO reference
	 * @param quotaId
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public Contract getByFoReference(Long quotaId) {
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<>(Contract.class);
		restrictions.addCriterion(Restrictions.eq("foReference", quotaId));
		List<Contract> contracts = list(restrictions);
		if (contracts != null && !contracts.isEmpty()) {
			return contracts.get(0);
		}
		return null;
	}
	
	/**
	 * Get real outstanding of contract
	 * @param cotraId
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public Amount getRealOutstanding(Date calculDate, Long cotraId) {
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<>(Cashflow.class);
		restrictions.addCriterion(Restrictions.eq(CANCEL, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.eq(UNPAID, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.eq(CASHFLOW_TYPE, ECashflowType.CAP));
		restrictions.addCriterion(Restrictions.eq(CONTRACT + "." + ID, cotraId));
		
		return getRealOutstanding(calculDate, list(restrictions));
	}
	
	/**
	 * Get real outstanding of contract
	 * @param calculDate
	 * @param cashflows
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public Amount getRealOutstanding(Date calculDate, List<Cashflow> cashflows) {
		Amount outstanding = new Amount(0d, 0d, 0d);
		for (Cashflow cashflow : cashflows) {
			if (cashflow.getCashflowType().equals(ECashflowType.CAP)
					&& !cashflow.isCancel()
					&& (!cashflow.isPaid() || (cashflow.getPayment() != null && DateUtils.getDateAtBeginningOfDay(cashflow.getPayment().getPaymentDate()).compareTo(calculDate) > 0))
					&& !cashflow.isUnpaid()) {
				outstanding.plusTiAmount(cashflow.getTiInstallmentAmount());
				outstanding.plusTeAmount(cashflow.getTeInstallmentAmount());
				outstanding.plusVatAmount(cashflow.getVatInstallmentAmount());
			}
		}
		
		return outstanding;
	}
	
	/**
	 * Get real outstanding of contract
	 * @param calculDate
	 * @param cashflows
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public Amount getTheoricalOutstanding(Date calculDate, List<Cashflow> cashflows) {
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
	 * Get interest balance
	 * @param cotraId
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public Amount getRealInterestBalance(Date calculDate, Long cotraId) {
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<>(Cashflow.class);
		restrictions.addCriterion(Restrictions.eq(CANCEL, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.eq(UNPAID, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.eq(CASHFLOW_TYPE, ECashflowType.IAP));
		restrictions.addCriterion(Restrictions.eq(CONTRACT + "." + ID, cotraId));
		
		return getRealInterestBalance(calculDate, list(restrictions));
	}
	
	/**
	 * Get interest balance
	 * @param calculDate
	 * @param cashflows
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public Amount getRealInterestBalance(Date calculDate, List<Cashflow> cashflows) {
		Amount interestBalance = new Amount(0d, 0d, 0d);
		for (Cashflow cashflow : cashflows) {
			if (cashflow.getCashflowType().equals(ECashflowType.IAP)
					&& !cashflow.isCancel()
					&& (!cashflow.isPaid() || (cashflow.getPayment() != null && DateUtils.getDateAtBeginningOfDay(cashflow.getPayment().getPaymentDate()).compareTo(calculDate) > 0))
					&& !cashflow.isUnpaid()) {
				interestBalance.plusTiAmount(cashflow.getTiInstallmentAmount());
				interestBalance.plusTeAmount(cashflow.getTeInstallmentAmount());
				interestBalance.plusVatAmount(cashflow.getVatInstallmentAmount());
			}
		}
		
		return interestBalance;
	}
	
	/**
	 * Get interest balance
	 * @param calculDate
	 * @param cashflows
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public Amount getInterestBalance(Date calculDate, List<Cashflow> cashflows) {
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
	 * Get total interest
	 * @param cotraId
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public Amount getTotalInterest(Long cotraId) {
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<>(Cashflow.class);
		restrictions.addCriterion(Restrictions.eq(CANCEL, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.eq(CASHFLOW_TYPE, ECashflowType.IAP));
		restrictions.addCriterion(Restrictions.eq(CONTRACT + "." + ID, cotraId));
		
		Amount interest = new Amount(0d, 0d, 0d);
		List<Cashflow> cashflows = list(restrictions);
		for (Cashflow cashflow : cashflows) {
			interest.plusTiAmount(cashflow.getTiInstallmentAmount());
			interest.plusTeAmount(cashflow.getTeInstallmentAmount());
			interest.plusVatAmount(cashflow.getVatInstallmentAmount());
		}
		
		return interest;
	}

	/**
	 * Get total interest
	 * @param cashflows
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public Amount getTotalInterest(List<Cashflow> cashflows) {
		Amount interest = new Amount(0d, 0d, 0d);
		for (Cashflow cashflow : cashflows) {
			if (cashflow.getCashflowType().equals(ECashflowType.IAP)
					&& !cashflow.isCancel()) {
				interest.plusTiAmount(cashflow.getTiInstallmentAmount());
				interest.plusTeAmount(cashflow.getTeInstallmentAmount());
				interest.plusVatAmount(cashflow.getVatInstallmentAmount());
			}
		}
		return interest;
	}
	
	/**
	 * Calculate penalty
	 * @param contract
	 * @param installmentDate
	 * @param installmentAmountUsd
	 */
	@Transactional(readOnly = true)
	public PenaltyVO calculatePenalty2(Contract contract, Date installmentDate, Date paymentDate, double installmentAmountUsd) {
		PenaltyVO penaltyVO = new PenaltyVO();
		if (contract.getPenaltyRule() != null) {
			installmentDate = DateUtils.getDateAtBeginningOfDay(installmentDate);
			paymentDate = DateUtils.getDateAtBeginningOfDay(paymentDate);
			int numExcludeOverdue = getNumExcludeOverdue(contract, installmentDate, paymentDate, contract.getPenaltyRule().getGracePeriod());
			Date installmentAndDelayDay = DateUtils.addDaysDate(installmentDate, MyNumberUtils.getInteger(contract.getPenaltyRule().getGracePeriod() + numExcludeOverdue)) ;
			installmentAndDelayDay = DateUtils.getDateAtBeginningOfDay(installmentAndDelayDay);
			if (paymentDate.compareTo(installmentAndDelayDay) >= 0) {
				Amount penaltyAmount = new Amount(0d, 0d, 0d);
				int nbPenaltyDays = DateUtils.getDiffInDays(paymentDate, installmentDate).intValue() - numExcludeOverdue + 1;
				nbPenaltyDays = nbPenaltyDays < 0 ? 0 : nbPenaltyDays;
				
				if (contract.getPenaltyRule().getPenaltyCalculMethod().equals(EPenaltyCalculMethod.FPD)) {
					penaltyVO.setNumPenaltyDays(nbPenaltyDays);
					penaltyAmount.setTiAmount(penaltyVO.getNumPenaltyDays() * contract.getPenaltyRule().getTiPenaltyAmounPerDaytUsd());
				} else if (contract.getPenaltyRule().getPenaltyCalculMethod().equals(EPenaltyCalculMethod.PIS)) {
					penaltyVO.setNumPenaltyDays(nbPenaltyDays);
					penaltyAmount.setTiAmount(penaltyVO.getNumPenaltyDays() * (contract.getPenaltyRule().getPenaltyRate() / 100) * installmentAmountUsd);
				}
				penaltyAmount.setVatAmount(0d);
				penaltyAmount.setTeAmount(penaltyAmount.getTiAmount());
				penaltyVO.setPenaltyAmount(penaltyAmount);
			}
		}
		return penaltyVO;
	}
	
	@Override
	public PenaltyVO calculatePenalty(Contract contract, Date installmentDate, Date paymentDate, double installmentAmountUsd) {
		PenaltyVO penaltyVO = new PenaltyVO();
		if (contract.getPenaltyRule() != null) {
			installmentDate = DateUtils.getDateAtBeginningOfDay(installmentDate);
			paymentDate = DateUtils.getDateAtBeginningOfDay(paymentDate);
			
			Date installmentAndDelayDay = installmentDate;
			Date maxPanaltyDate = DateUtils.addDaysDate(DateUtils.addMonthsDate(installmentDate, 1), -1);
			if (paymentDate.before(maxPanaltyDate)) {
				maxPanaltyDate = paymentDate;
			}
						
			int nbGracePeriod = 0;
			int nbExculdeHoliday = 0;
			for (int i = 0; i < contract.getPenaltyRule().getGracePeriod() + 1; i++) {
				if (isDueDateOnNonOperationDay(installmentAndDelayDay)) {
					i--;
					nbExculdeHoliday++;
				}
				nbGracePeriod++;
				installmentAndDelayDay = DateUtils.addDaysDate(installmentAndDelayDay, 1);
			}
						
			installmentAndDelayDay = DateUtils.getDateAtBeginningOfDay(DateUtils.addDaysDate(installmentDate, nbGracePeriod));

			if (maxPanaltyDate.compareTo(installmentAndDelayDay) >= 0) {
				Amount penaltyAmount = new Amount(0d, 0d, 0d);
				int nbPenaltyDays = DateUtils.getDiffInDays(maxPanaltyDate, installmentDate).intValue() + 1 - nbExculdeHoliday;
				nbPenaltyDays = nbPenaltyDays < 0 ? 0 : nbPenaltyDays;
				penaltyVO.setNumPenaltyDays(nbPenaltyDays);
				penaltyVO.setNumOverdueDays(DateUtils.getDiffInDays(paymentDate, installmentDate).intValue());
				if (contract.getPenaltyRule().getPenaltyCalculMethod().equals(EPenaltyCalculMethod.FPD)) {
					penaltyAmount.setTiAmount(penaltyVO.getNumPenaltyDays() * contract.getPenaltyRule().getTiPenaltyAmounPerDaytUsd());
				} else if (contract.getPenaltyRule().getPenaltyCalculMethod().equals(EPenaltyCalculMethod.PIS)) {
					penaltyAmount.setTiAmount(penaltyVO.getNumPenaltyDays() * (contract.getPenaltyRule().getPenaltyRate() / 100) * installmentAmountUsd);
				} else if (contract.getPenaltyRule().getPenaltyCalculMethod().equals(EPenaltyCalculMethod.MRR)) {
					penaltyAmount.setTiAmount(new PenaltyMRRImpl().getPenaltyAmont(installmentAmountUsd, paymentDate, MyNumberUtils.getDouble(contract.getPenaltyRule().getPenaltyRate())));
				}
				penaltyAmount.setVatAmount(0d);
				penaltyAmount.setTeAmount(penaltyAmount.getTiAmount());
				penaltyVO.setPenaltyAmount(penaltyAmount);
			}
		}
		return penaltyVO;
	}
	
	/**
	 * Calculate penalty
	 * @param contract
	 * @param installmentDate
	 * @param installmentAmountUsd
	 */
	@Override
	@Transactional(readOnly = true)
	public int calculateOverdueDays(Contract contract, double penaltyAmountUsd, double installmentAmountUsd) {
		int nbPenaltyDays = 0;
		if (contract.getPenaltyRule() != null) {
			if (contract.getPenaltyRule().getPenaltyCalculMethod().equals(EPenaltyCalculMethod.FPD)) {
				nbPenaltyDays = (int) (penaltyAmountUsd / contract.getPenaltyRule().getTiPenaltyAmounPerDaytUsd());
			} else if (contract.getPenaltyRule().getPenaltyCalculMethod().equals(EPenaltyCalculMethod.PIS)) {
				nbPenaltyDays = (int) (penaltyAmountUsd / ((contract.getPenaltyRule().getPenaltyRate() / 100) * installmentAmountUsd));
			}
		}
		return nbPenaltyDays;
	}
	
	/**
	 * @param contract
	 * @param installmentDate
	 * @return numExcludeOverdue
	 */
	private int getNumExcludeOverdue(Contract contract, Date installmentDate, Date paymentDate, int gracePeriod) {
		List<PublicHoliday> publicHolidays = DataReference.getInstance().getPublicHoliday();
		int numExcludeOverdue = 0;
		if (publicHolidays != null && !publicHolidays.isEmpty()) {
			for (PublicHoliday publicHoliday : publicHolidays) {
				Date installmentAndDelayDay = DateUtils.addDaysDate(installmentDate, MyNumberUtils.getInteger(contract.getPenaltyRule().getGracePeriod() + 1)) ;
				installmentAndDelayDay = DateUtils.getDateAtBeginningOfDay(installmentAndDelayDay);
				Date holidayDate = DateUtils.getDateAtBeginningOfDay(publicHoliday.getDay());
				
				if (publicHoliday.isDayOff() 
						&& holidayDate.compareTo(DateUtils.getDateAtBeginningOfDay(installmentDate)) >= 0
						&& holidayDate.compareTo(paymentDate) <= 0) {
					numExcludeOverdue++;
				}	
			}
		}
		return numExcludeOverdue;
	}
		
	/**
	 * @param installmentDate
	 * @return
	 */
	private boolean isDueDateOnNonOperationDay(Date installmentDate) {
		List<PublicHoliday> publicHolidays = DataReference.getInstance().getPublicHoliday();
		for (PublicHoliday publicHoliday : publicHolidays) {
			if (publicHoliday.isDayOff() && installmentDate
					.compareTo(DateUtils.getDateAtBeginningOfDay(publicHoliday.getDay())) == 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @param cotraId
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)	 
	public List<Cashflow> getCashflowsNoCancel(Long cotraId) {
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<>(Cashflow.class);
		restrictions.addCriterion(Restrictions.eq(CANCEL, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.eq(CONTRACT + "." + ID, cotraId));
		restrictions.addOrder(Order.asc(NUM_INSTALLMENT));
		return list(restrictions);
	}
	
	/**
	 * @param cotraId
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Cashflow> getCashflows(Long cotraId) {
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<>(Cashflow.class);
		restrictions.addCriterion(Restrictions.eq(CONTRACT + "." + ID, cotraId));
		restrictions.addOrder(Order.asc(INSTALLMENT_DATE));
		return list(restrictions);
	}
	
	/**
	 * @param cotraId
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<Cashflow> getCashflowsEarlySettlement(Long cotraId, Date simulateDate) {
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<>(Cashflow.class);
		restrictions.addCriterion(Restrictions.eq(CANCEL, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.eq(PAID, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.ge(INSTALLMENT_DATE, DateUtils.getDateAtBeginningOfDay(simulateDate)));
		restrictions.addCriterion(Restrictions.eq(CONTRACT + "." + ID, cotraId));
		restrictions.addOrder(Order.asc(INSTALLMENT_DATE));
		return list(restrictions);
	}
	
	/**
	 * @param cotraId
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean isOneInstallmentAlreadyPaid(Long cotraId) {
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<>(Cashflow.class);
		restrictions.addCriterion(Restrictions.eq(CANCEL, false));
		restrictions.addCriterion(Restrictions.eq(CONTRACT + "." + ID, cotraId));
		restrictions.addCriterion(Restrictions.gt(NUM_INSTALLMENT, 0));
		restrictions.addCriterion(Restrictions.eq(PAID, true));
		List<Cashflow> cashflows = list(restrictions);
		return cashflows != null && !cashflows.isEmpty();
	}
	
	/**
	 * @param cotraId
	 * @param firstDueDate
	 */
	@Override
	public void updateInstallmentDate(Long cotraId, Date firstDueDate) {
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<>(Cashflow.class);
		restrictions.addCriterion(Restrictions.eq(CANCEL, false));
		restrictions.addCriterion(Restrictions.eq(CONTRACT + "." + ID, cotraId));
		restrictions.addCriterion(Restrictions.gt(NUM_INSTALLMENT, 0));
		restrictions.addOrder(Order.asc(NUM_INSTALLMENT));
		
		List<Cashflow> cashflows = list(restrictions);
		int numberInstallment = 0;
		Date installmentDate = firstDueDate;
		for (Cashflow cashflow : cashflows) {
			if (numberInstallment != cashflow.getNumInstallment().intValue()) {
				numberInstallment = cashflow.getNumInstallment();
				if (numberInstallment != 1) {
					installmentDate = DateUtils.addMonthsDate(installmentDate, 1);
				}
			}
			
			if (cashflow.getCashflowType() != ECashflowType.PEN) {
				cashflow.setInstallmentDate(installmentDate);
				saveOrUpdate(cashflow);
			}
		}
		
		Contract contract = getById(Contract.class, cotraId);
		contract.setFirstDueDate(firstDueDate);
		saveOrUpdate(contract);
	}
	
	/**
	 * @param cotraId
	 * @param startContractDate
	 */
	@Override
	public void updateOfficialPaymentDate(Quotation quotation, Date startContractDate) {
		Contract contract = quotation.getApplicant().getContract();
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<>(Cashflow.class);
		restrictions.addCriterion(Restrictions.eq(CANCEL, false));
		restrictions.addCriterion(Restrictions.eq(CONTRACT + "." + ID, contract.getId()));
		restrictions.addCriterion(Restrictions.eq(NUM_INSTALLMENT, 0));
		List<Cashflow> cashflows = list(restrictions);
		contract.setSigatureDate(startContractDate);
		contract.setStartDate(startContractDate);
		contract.setInitialStartDate(startContractDate);
		contract.setEndDate(DateUtils.addDaysDate(DateUtils.addMonthsDate(startContractDate, quotation.getTerm() * quotation.getFrequency().getNbMonths()), -1));
		contract.setInitialEndDate(contract.getEndDate());
		saveOrUpdate(contract);
		for (Cashflow cashflow : cashflows) {
			cashflow.setInstallmentDate(startContractDate);
			cashflow.setPeriodStartDate(startContractDate);
			cashflow.setPeriodEndDate(startContractDate);
			saveOrUpdate(cashflow);
		}
		
		BaseRestrictions<Cashflow> restrictions2 = new BaseRestrictions<>(Cashflow.class);
		restrictions2.addCriterion(Restrictions.eq(CANCEL, false));
		restrictions2.addCriterion(Restrictions.eq(CONTRACT + "." + ID, contract.getId()));
		restrictions2.addCriterion(Restrictions.gt(NUM_INSTALLMENT, 0));
		restrictions2.addOrder(Order.asc(NUM_INSTALLMENT));
		
		List<Cashflow> cashflows2 = list(restrictions2);
		for (Cashflow cashflow : cashflows2) {
			if (cashflow.getCashflowType() != ECashflowType.PEN) {
				Date periodStartDate = DateUtils.addMonthsDate(startContractDate, -1 * (1 - cashflow.getNumInstallment()));
				Date periodEndDate = DateUtils.addMonthsDate(periodStartDate, 1);
				cashflow.setPeriodStartDate(periodStartDate);
				cashflow.setPeriodEndDate(DateUtils.addDaysDate(periodEndDate, -1));
				saveOrUpdate(cashflow);
			}
		}
	}
	
	/**
	 * @param contract
	 * @param status
	 */
	@Override
	public void changeContractStatus(Contract contract, EWkfStatus status) {
		changeContractStatus(contract, status, DateUtils.today());
	}
	
	/**
	 * @param contract
	 * @param status
	 * @param date
	 */
	public void changeContractStatus(Contract contract, EWkfStatus status, Date date) {
		contract.setWkfStatus(status);
		saveOrUpdate(contract);
		
//		HistoryContract history = new HistoryContract();
//		history.setContract(contract);
//		history.setHistoryDate(date);
//		if (status == WkfContractStatus.REP) {
//			history.sethisReason(EhisReason.CONTRACT_REP);
//		} else if (status == WkfContractStatus.THE) {
//			history.sethisReason(EhisReason.CONTRACT_THE);
//		} else if (status == WkfContractStatus.ACC) {
//			history.sethisReason(EhisReason.CONTRACT_ACC);
//		} else if (status == WkfContractStatus.FRA) {
//			history.sethisReason(EhisReason.CONTRACT_FRA);
//		} else if (status == WkfContractStatus.WRI) {
//			history.sethisReason(EhisReason.CONTRACT_WRI);
//		} else if (status == WkfContractStatus.CLO) {
//			history.sethisReason(EhisReason.CONTRACT_0002);
//		} else if (status == WkfContractStatus.EAR) {
//			history.sethisReason(EhisReason.CONTRACT_0003);
//		} else if (status == WkfContractStatus.FIN) {
//			history.sethisReason(EhisReason.CONTRACT_BACK_FIN);
//		}
//		saveOrUpdate(history);
	}
	
	/**
	 * @param cotraId
	 */
	@Override
	@Transactional(readOnly = true)
	public Payment getLastPayment(Long cotraId) {
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<>(Cashflow.class);
		restrictions.addCriterion(Restrictions.eq(CANCEL, false));
		restrictions.addCriterion(Restrictions.eq(CONTRACT + "." + ID, cotraId));
		restrictions.addCriterion(Restrictions.gt(NUM_INSTALLMENT, 0));
		restrictions.addCriterion(Restrictions.eq(PAID, true));
		restrictions.addOrder(Order.desc(NUM_INSTALLMENT));
		List<Cashflow> cashflows = list(restrictions);
		if (cashflows != null && !cashflows.isEmpty()) {
			return cashflows.get(0).getPayment(); 
		}
		return null;
	}
	
	/**
	 * @param cotraId
	 * @return
	 */
	
	@Override
	@Transactional(readOnly = true)
	public Payment getNextPayment(Long cotraId) {
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<>(Cashflow.class);
		restrictions.addCriterion(Restrictions.eq(CANCEL, false));
		restrictions.addCriterion(Restrictions.eq(CONTRACT + "." + ID, cotraId));
		restrictions.addCriterion(Restrictions.gt(NUM_INSTALLMENT, 0));
		restrictions.addCriterion(Restrictions.eq(PAID, false));
		restrictions.addOrder(Order.asc(NUM_INSTALLMENT));
		List<Cashflow> cashflows = list(restrictions);
		SecUser processByUser = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (cashflows != null && !cashflows.isEmpty()) {
			Date installmentDate = cashflows.get(0).getInstallmentDate();
			List<Cashflow> cashflowsToPaid = getCashflowsToPaid(cotraId, installmentDate);
			double paymentAmount = 0d;
			for (Cashflow cashflow : cashflowsToPaid) {
				paymentAmount += cashflow.getTiInstallmentAmount();
			}
			Contract contract = cashflows.get(0).getContract();
			Payment payment = new Payment();
			payment.setApplicant(contract.getApplicant());
			payment.setContract(contract);
			payment.setPaymentDate(installmentDate);
			payment.setPaymentMethod(getByCode(EPaymentMethod.class, "CASH"));
			payment.setTiPaidAmount(MyMathUtils.roundAmountTo(paymentAmount));
			payment.setWkfStatus(PaymentWkfStatus.PAI);
			payment.setConfirm(payment.getPaymentMethod().isAutoConfirm());
			payment.setReceivedUser(processByUser);
			payment.setPaymentType(EPaymentType.IRC);
			payment.setNumPenaltyDays(0);
			payment.setDealer(contract.getDealer());
			payment.setCashflows(cashflowsToPaid);
			return payment;
		}
		return null;
	}
		
	/**
	 * @param cotraId
	 * @param installmentDate
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Cashflow> getCashflowsToPaid(Long cotraId, Date installmentDate) {
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<>(Cashflow.class);
		restrictions.addCriterion(Restrictions.eq(PAID, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.eq(CANCEL, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.ne(CASHFLOW_TYPE, ECashflowType.FIN));
		restrictions.addCriterion(Restrictions.eq(CONTRACT + "." + ID, cotraId));
		restrictions.addCriterion(Restrictions.ge(INSTALLMENT_DATE, DateUtils.getDateAtBeginningOfDay(installmentDate)));
		restrictions.addCriterion(Restrictions.le(INSTALLMENT_DATE, DateUtils.getDateAtEndOfDay(installmentDate)));
		return list(restrictions);
	}
	
	/**
	 * @param cotraId
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean isPrintedPurchaseOrder(Long cotraId) {
		boolean printPurchaseOrder = false;
		Payment payment = paymentService.getPaymentByContract(cotraId, 0);
		if (payment != null) {
			printPurchaseOrder = (payment.getWkfStatus().equals(PaymentWkfStatus.VAL) || payment.getWkfStatus().equals(PaymentWkfStatus.PAI))
					&& payment.getPrintPurchaseOrderVersion() != null && payment.getPrintPurchaseOrderVersion() > 0;
		}
		return printPurchaseOrder;
	}
	
	/**
	 * @param contract
	 * @return
	 */
	public void closeContract(Contract contract) {
		changeContractStatus(contract, ContractWkfStatus.CLO);
		ContractOtherDataService contractOtherDataService = SpringUtils.getBean(ContractOtherDataService.class);
		contractOtherDataService.calculateOtherDataContract(contract);
	}
	
	/**
	 * @see com.nokor.efinance.core.contract.service.ContractService#reverseContract(com.nokor.efinance.core.contract.model.Contract)
	 */
	@Override
	public void reverseContract(Contract contract) {
		changeContractStatus(contract, ContractWkfStatus.PEN);
	}
	
	/**
	 * @see com.nokor.efinance.core.contract.service.ContractService#withdrawContract(com.nokor.efinance.core.contract.model.Contract)
	 */
	@Override
	public void withdrawContract(Contract contract) {
		changeContractStatus(contract, ContractWkfStatus.WITHDRAWN);
	}
	
	/**
	 * @see com.nokor.efinance.core.contract.service.ContractService#updateContractAndAsset(com.nokor.efinance.core.contract.model.Contract)
	 */
	@Override
	public void updateContractAndAsset(Contract contract) {
		saveOrUpdate(contract);
		saveOrUpdate(contract.getAsset());
	}

	/**
	 * @see com.nokor.efinance.core.contract.service.ContractService#getNbOverdueInDays(java.util.Date, java.util.List)
	 */
	@Override
	public int getNbOverdueInDays(Date calDate, List<Cashflow> cashflows) {
		Cashflow cashflowValue = getOldestCashflowNotPaid(cashflows);
		long nbOverdue = 0;
		
		if (cashflowValue != null) {
			nbOverdue = DateUtils.getDiffInDays(calDate, DateUtils.getDateAtBeginningOfDay(cashflowValue.getInstallmentDate())) - 1;
		}
		
		return (nbOverdue < 0) ? 0 : (int) nbOverdue;
	}
	
	/**
	 * Get Oldest Cashflow Not Paid
	 * @param cashflows
	 * @return
	 */
	private Cashflow getOldestCashflowNotPaid(List<Cashflow> cashflows) {
		Cashflow cashflowValue = null;
		for (Cashflow cashflow : cashflows) {
			if (cashflow != null 
					&& cashflow.getCashflowType().equals(ECashflowType.IAP)
					&& !cashflow.isCancel()
					&& !cashflow.isUnpaid()
					&& !cashflow.isPaid()) {
				if (cashflowValue == null || DateUtils.getDateAtBeginningOfDay(cashflowValue.getInstallmentDate()).compareTo(
						DateUtils.getDateAtBeginningOfDay(cashflow.getInstallmentDate())) > 0) {
					cashflowValue = cashflow;
				} 
			}
		}
		return cashflowValue;
	}
	
	/**
	 * @see com.nokor.efinance.core.contract.service.ContractService#getDebtLevel(java.util.Date, java.util.List)
	 */
	@Override
	public int getDebtLevel(Date calDate, List<Cashflow> cashflows) {
		Cashflow cashflowValue = getOldestCashflowNotPaid(cashflows);
		calDate = DateUtils.getDateAtBeginningOfMonth(calDate);
		
		int todayYear = DateUtils.getYear(calDate);
		int todayMonth = DateUtils.getMonth(calDate);
		long debtLevel = 0l;
		
		if (cashflowValue != null) {
			int installmentDateYear = DateUtils.getYear(cashflowValue.getInstallmentDate());
			int installmentDateMonth = DateUtils.getMonth(cashflowValue.getInstallmentDate());
			debtLevel = ((todayYear * 12) + todayMonth) - ((installmentDateYear * 12) + installmentDateMonth);
			if (debtLevel < 0) {
				debtLevel = 0;
			}
		}
		
		return (int) debtLevel;
	}

	/**
	 * @see com.nokor.efinance.core.contract.service.ContractService#getNbOverdueInDays(java.util.Date, java.lang.Long)
	 */
	@Override
	public int getNbOverdueInDays(Date calDate, Long cotraId) {
		List<Cashflow> cashflows = getCashflows(cotraId);
		return getNbOverdueInDays(calDate, cashflows);
	}
	
	/**
	 * @see com.nokor.efinance.core.contract.service.ContractService#unbookContract(com.nokor.frmk.security.model.SecUser, java.lang.Long)
	 */
	@Override
	public void unbookContract(SecUser user, Long conId) {
		Contract contract = getById(Contract.class, conId);
		userInboxService.unbookContract(user, contract);	
	}
	
	/**
	 * @see com.nokor.efinance.core.contract.service.ContractService#receiveContract(java.lang.Long)
	 */
	@Override
	public void receiveContracts(List<Long> conIds) {
		if (conIds != null && !conIds.isEmpty()) {
			for (Long id : conIds) {
				receiveContract(id);
			}
		}
	}
	
	/**
	 * @see com.nokor.efinance.core.contract.service.ContractService#receiveContract(java.lang.Long)
	 */
	@Override
	public void receiveContract(Long conId) {
		Contract contract = getById(Contract.class, conId);
		userInboxService.receivedContract(contract);
	}
	/**
	 * Booking contracts
	 * @param conIds
	 */
	@Override
	public void bookContracts(List<Long> conIds) {
		if (conIds != null && !conIds.isEmpty()) {
			for (Long id : conIds) {
				bookContract(id);
			}
		}
	}
	
	/**
	 * @see com.nokor.efinance.core.contract.service.ContractService#bookContract(java.lang.Long)
	 */
	@Override
	public void bookContract(Long conId) {
		Contract contract = getById(Contract.class, conId);
		contract.setBookingDate(DateUtils.today());
		saveOrUpdate(contract); // saveOrUpdate contract
		
		// TaskTemplate taskTemplate = ENTITY_SRV.getById(TaskTemplate.class, 1l);
		// Task parentTask = null;
		// generateTask(taskTemplate, parentTask, contract);
		
		userInboxService.bookContract(UserSessionManager.getCurrentUser(), contract);
	}
	
	/**
	 * @see com.nokor.efinance.core.contract.service.ContractService#cancelContract(java.lang.Long)
	 */
	@Override
	public Contract cancelContract(Long contraId) {
		Contract contract = getById(Contract.class, contraId);
		contract.setWkfStatus(ContractWkfStatus.CAN);
		saveOrUpdate(contract);
		return contract;
	}
	
	/**
	 * @see com.nokor.efinance.core.contract.service.ContractService#transfer(java.lang.Long, com.nokor.frmk.security.model.SecProfile)
	 */
	@Override
	public void transfer(Long contraId, SecProfile profile) {
		SecUser secUser = UserSessionManager.getCurrentUser();
		Contract contract = getById(Contract.class, contraId);
		if (IProfileCode.CMSTAFF.equals(secUser.getDefaultProfile().getCode())) {
			contract.setWkfStatus(QuotationWkfStatus.ASS_LEV1);
		} else {
			contract.setWkfStatus(QuotationWkfStatus.INPRO);
		}
		saveOrUpdate(contract);
		
		ContractProfileQueue profileQueue = ContractProfileQueue.createInstance();
		profileQueue.setProfile(profile);
		profileQueue.setContract(contract);
		saveOrUpdate(profileQueue);
	}

	/**
	 * @see com.nokor.efinance.core.contract.service.ContractService#reject(java.lang.Long)
	 */
	@Override
	public void reject(Long contraId) {
		Contract contract = getById(Contract.class, contraId);
		contract.setWkfStatus(QuotationWkfStatus.REJECT);
		saveOrUpdate(contract);
	}

	/**
	 * @see com.nokor.efinance.core.contract.service.ContractService#hold(java.lang.Long)
	 */
	@Override
	public void hold(Long conId) {
		Contract contract = getById(Contract.class, conId);
		if (contract.isTransfered() && ContractUtils.isActivated(contract)) {
			contract.setWkfSubStatus(ContractWkfStatus.BLOCKED_TRAN);
		} else {
			contract.setWkfStatus(ContractWkfStatus.BLOCKED);
		}
		saveOrUpdate(contract);
		String desc = I18N.message("msg.contract.blocked", new String[] {contract.getReference(), contract.getCreateUser()});
		FIN_HISTO_SRV.addFinHistory(contract, FinHistoryType.FIN_HIS_SYS, desc);
	}
	
	/**
	 * @param conId
	 */
	public void printContract(Long conId) {
		Contract contract = getById(Contract.class, conId);
		if (contract.getNbPrints() != null) {
			contract.setNbPrints(contract.getNbPrints() + 1);
		} else {
			contract.setNbPrints(1);
		}
		userInboxService.deleteContractFromInbox(contract);
	}
	
	/**
	 * 
	 * @param taskTemplate
	 * @param parentTask
	 * @param contract
	 */
	private void generateTask(TaskTemplate taskTemplate, Task parentTask, Contract contract) {
		if (taskTemplate != null) {
			ActionExecution action = null;
			if (taskTemplate.getParent() == null) {
				action = getActionExecution(contract, taskTemplate);
				// create new action execution
				create(action); 
			
				parentTask = copyTask(null, taskTemplate, action);
				TASK_SRV.createProcess(parentTask); // Create parent task
			}
			List<TaskTemplate> taskTempletes = taskTemplate.getChildren();
			if (!taskTempletes.isEmpty()) {
				for (TaskTemplate taskTmp : taskTempletes) {
					action = getActionExecution(contract, taskTmp);
					// create new action execution
					create(action); 
					if (taskTmp.getChildren().isEmpty()) {
						TASK_SRV.createProcess(copyTask(parentTask, taskTmp, action));
						generateTask(taskTmp, parentTask, contract);
					} else {
						Task subParentTask = copyTask(parentTask, taskTmp, action);
						TASK_SRV.createProcess(subParentTask);
						generateTask(taskTmp, subParentTask, contract);
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param contract
	 * @param child
	 * @return
	 */
	private ActionExecution getActionExecution(Contract contract, TaskTemplate child) {
		ActionExecution execution = EntityFactory.createInstance(ActionExecution.class);
		ActionDefinition definition = child.getAction();
		if (definition != null) {
			execution.setAction(definition);
			execution.setInputDefinition(definition.getInputDefinition());
			execution.setOutputDefinition(definition.getOutputDefinition());
			execution.setComment(definition.getComment());
		}
		execution.setExecValue(definition == null ? "" : definition.getExecValue());
		execution.setType(definition == null ? EActionType.JAVA_ACTION_SUPPORT : definition.getType());
		execution.setExecDate(DateUtils.today());
		execution.setEntityClass("");
		execution.setEntityId(contract.getId());
		return execution;
	}
	
	/**
	 * 
	 * @param parent
	 * @param child
	 * @param action
	 * @return
	 */
	private Task copyTask(Task parent, TaskTemplate child, ActionExecution action) {
		Task task = Task.createInstance();
		task.setParent(parent);
		task.setForcedHistory(child.isForcedHistory());
		task.setCode(child.getCode());
		task.setDesc(child.getDesc());
		task.setDescEn(child.getDescEn());
		task.setProject(child.getProject());
		task.setReporter(child.getReporter());
		task.setAssignee(child.getAssignee());
		task.setType(child.getType());
		task.setProject(child.getProject());
		task.setDeadline(child.getDeadline());
		task.setComment(child.getComment());
		task.setKeywords(child.getKeywords());
		task.setKeywordsEn(child.getKeywordsEn());
		task.setEstimatedDuration(child.getEstimatedDuration());
		task.setEstimatedEndDate(child.getEstimatedEndDate());
		task.setStartDate(child.getStartDate());
		task.setEndDate(child.getEndDate());
		task.setAction(action);
		return task;
	}
	
	/**
	 * @see com.nokor.efinance.core.contract.service.ContractService#cancelReceiveContracts(java.util.List)
	 */
	@Override
	public void cancelReceivedContracts(List<Long> conIds) {
		if (conIds != null && !conIds.isEmpty()) {
			for (Long id : conIds) {
				cancelReceivedContract(id);
			}
		}
		
	}
	
	/**
	 * @see com.nokor.efinance.core.contract.service.ContractService#cancelReceivceContract(java.lang.Long)
	 */
	@Override
	public void cancelReceivedContract(Long conId) {
		Contract contract = getById(Contract.class, conId);
		userInboxService.deleteContractFromInbox(null, contract);
	}

	/**
	 * @see com.nokor.efinance.core.contract.service.ContractService#getListContractHistories(com.nokor.efinance.core.contract.model.Contract)
	 */
	@Override
	public List<ContractWkfHistoryItem> getListContractHistories(Contract contract) {
		WkfHistoryItemRestriction<ContractWkfHistoryItem> restrictions = new WkfHistoryItemRestriction<>(ContractWkfHistoryItem.class);
		restrictions.setEntity(contract.getMainEntity());
		restrictions.setEntityId(contract.getId());
		restrictions.addOrder(Order.desc(TaskWkfHistoryItem.CHANGEDATE));
		return dao.list(restrictions);
	}

	@Override
	public List<Transaction> getTransaction(List<TransactionVO> transactionVOs) {
		List<Transaction> transactions = new ArrayList<>();
		
		for (TransactionVO transactionVO : transactionVOs) {
			List<TransactionItem> transactionItems = new ArrayList<>();
			Transaction transaction = new Transaction();
			transaction.setTransactionDate(transactionVO.getDate());
			transaction.setTransactionId(transactionVO.getReference());
			transaction.setDescription(I18N.message("installment"));
			transaction.setAmount(transactionVO.getDueAmount());
			transaction.setBalance(transactionVO.getBalanceAmount().getTiAmount());
			//transaction.setIsPaid(transactionVO.getPaidAmount().getTiAmount() > 0d ? true : false);
			transaction.setWkfStatus(transactionVO.getWkfStatus());
			
			TransactionItem principalItem = new TransactionItem();
			principalItem.setDescription(I18N.message("principal"));
			principalItem.setAmount(transactionVO.getPrincipal().getTeAmount());
			principalItem.setBalance(transactionVO.getPrincipal().getTeAmount());
			
			TransactionItem interestItem = new TransactionItem();
			interestItem.setDescription(I18N.message("interest"));
			interestItem.setAmount(transactionVO.getInterest().getTeAmount());
			interestItem.setBalance(transactionVO.getInterest().getTeAmount());
			
			TransactionItem vatItem = new TransactionItem();
			vatItem.setDescription(I18N.message("vat"));
			vatItem.setAmount(transactionVO.getDueVatAmount());
			vatItem.setBalance(transactionVO.getDueVatAmount());
			
			transactionItems.add(principalItem);
			transactionItems.add(interestItem);
			transactionItems.add(vatItem);
			transaction.setItems(transactionItems);
			
			transactions.add(transaction);
		}
		
		return transactions;
	}

	
	/**
	 * @return
	 */
	public List<String> getRemainingOneInstallmentContracts() {
		List<String> contracts = new ArrayList<>();
		String query = 
				" select c.con_va_reference" +
				" from td_contract c, td_collection col" +
				" where c.con_id = col.con_id"  +
				" and (c.con_nu_term - col.col_nu_nb_installments_paid) = 1";			
		try {
			List<NativeRow> cashflowRows = executeSQLNativeQuery(query);
			if (cashflowRows != null && cashflowRows.size() == 1) {
				for (NativeRow row : cashflowRows) {
			      	contracts.add(String.valueOf(row.getColumns().get(0).getValue()));
			    }
			}
		} catch (NativeQueryException e) {
			LOG.error(e.getMessage(), e);
		}
		return contracts;
	}
	
	/**
	 * 
	 * @param assetId
	 * @return
	 */
	@Override
	public Contract getContractByAssetId(Long assetId) {
		ContractRestriction restrictions = new ContractRestriction();
		restrictions.addCriterion(Restrictions.eq("asset.id", assetId));
		restrictions.addOrder(Order.desc("id"));
		List<Contract> contracts = list(restrictions);
		if (!contracts.isEmpty()) {
			return contracts.get(0);
		}
		return null;
	}
	
	/**
	 * @param contract
	 * @return
	 */
	public Summary getContractSummary(Contract contract) {
		Summary summary = new Summary();
		
		Collection col = contract.getCollection();
		summary.setBalanceInstallment(new Amount(col.getTeBalanceCapital() + col.getTeBalanceInterest(), col.getVatBalanceCapital() + col.getVatBalanceInterest(), col.getTiBalanceCapital() + col.getTiBalanceInterest()));
		
		double teBalanceOthers = MyNumberUtils.getDouble(col.getTeBalanceCollectionFee())
				+ MyNumberUtils.getDouble(col.getTeBalanceOperationFee())
				+ MyNumberUtils.getDouble(col.getTeBalancePressingFee())
				+ MyNumberUtils.getDouble(col.getTeBalanceRepossessionFee())
				+ MyNumberUtils.getDouble(col.getTeBalanceTransferFee())
				+ MyNumberUtils.getDouble(col.getTePenaltyAmount());
		
		double vatBalanceOthers = MyNumberUtils.getDouble(col.getVatBalanceCollectionFee())
				+ MyNumberUtils.getDouble(col.getVatBalanceOperationFee())
				+ MyNumberUtils.getDouble(col.getVatBalancePressingFee())
				+ MyNumberUtils.getDouble(col.getVatBalanceRepossessionFee())
				+ MyNumberUtils.getDouble(col.getVatBalanceTransferFee())
				+ MyNumberUtils.getDouble(col.getVatPenaltyAmount());
		
		double tiBalanceOthers = MyNumberUtils.getDouble(col.getTiBalanceCollectionFee())
				+ MyNumberUtils.getDouble(col.getTiBalanceOperationFee())
				+ MyNumberUtils.getDouble(col.getTiBalancePressingFee())
				+ MyNumberUtils.getDouble(col.getTiBalanceRepossessionFee())
				+ MyNumberUtils.getDouble(col.getTiBalanceTransferFee())
				+ MyNumberUtils.getDouble(col.getTiPenaltyAmount());
		summary.setBalanceOthers(new Amount(teBalanceOthers, vatBalanceOthers, tiBalanceOthers));
		
		List<Cashflow>  cashflowsDue = CASHFLOW_SRV.getCashflowsToPaidLessThanToday(contract.getId(), DateUtils.addDaysDate(DateUtils.today(), 30));
		
		Date toDay = DateUtils.todayH23M59S59();
		Amount dueInstallmentIn30Days = new Amount(0d, 0d, 0d);
		Amount dueInstallmentToDate = new Amount(0d, 0d, 0d);
		Amount balanceDueToDate = new Amount(0d, 0d, 0d);
		
		if (cashflowsDue != null && !cashflowsDue.isEmpty()) {
			for (Cashflow cashflow : cashflowsDue) {
				if (cashflow.getInstallmentDate().compareTo(toDay) <= 0) {
					if (ECashflowType.IAP.equals(cashflow.getCashflowType()) || ECashflowType.CAP.equals(cashflow.getCashflowType())) {
						dueInstallmentToDate.plusTeAmount(MyNumberUtils.getDouble(cashflow.getTeInstallmentAmount()));
						dueInstallmentToDate.plusVatAmount(MyNumberUtils.getDouble(cashflow.getVatInstallmentAmount()));
						dueInstallmentToDate.plusTiAmount(MyNumberUtils.getDouble(cashflow.getTiInstallmentAmount()));
					}
					balanceDueToDate.plusTeAmount(MyNumberUtils.getDouble(cashflow.getTeInstallmentAmount()));
					balanceDueToDate.plusVatAmount(MyNumberUtils.getDouble(cashflow.getVatInstallmentAmount()));
					balanceDueToDate.plusTiAmount(MyNumberUtils.getDouble(cashflow.getTiInstallmentAmount()));
				} else {
					if (ECashflowType.IAP.equals(cashflow.getCashflowType()) || ECashflowType.CAP.equals(cashflow.getCashflowType())) {
						dueInstallmentIn30Days.plusTeAmount(MyNumberUtils.getDouble(cashflow.getTeInstallmentAmount()));
						dueInstallmentIn30Days.plusVatAmount(MyNumberUtils.getDouble(cashflow.getVatInstallmentAmount()));
						dueInstallmentIn30Days.plusTiAmount(MyNumberUtils.getDouble(cashflow.getTiInstallmentAmount()));
					}
				}
			}
		}
		summary.setBalanceDueToDate(balanceDueToDate);
		summary.setDueInstallmentIn30Days(dueInstallmentIn30Days);
		summary.setDueInstallmentToDate(dueInstallmentToDate);
		
		return summary;
	}
}


