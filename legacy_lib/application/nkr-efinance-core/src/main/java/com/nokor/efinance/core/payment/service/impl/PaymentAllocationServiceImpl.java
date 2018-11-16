package com.nokor.efinance.core.payment.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.model.entity.MainEntity;
import org.seuksa.frmk.service.impl.MainEntityServiceImpl;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyMathUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.exception.DaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.LockSplit;
import com.nokor.efinance.core.contract.model.LockSplitItem;
import com.nokor.efinance.core.contract.model.LockSplitRestriction;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.service.cashflow.CashflowService;
import com.nokor.efinance.core.contract.service.cashflow.impl.CashflowUtils;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.payment.dao.PaymentFileDao;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.efinance.core.payment.model.EPaymentType;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.payment.model.PaymentFile;
import com.nokor.efinance.core.payment.model.PaymentFileItem;
import com.nokor.efinance.core.payment.service.LockSplitService;
import com.nokor.efinance.core.payment.service.PaymentAllocationService;
import com.nokor.efinance.core.payment.service.PaymentFileItemRestriction;
import com.nokor.efinance.core.payment.service.PaymentService;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.core.workflow.LockSplitWkfStatus;
import com.nokor.efinance.core.workflow.PaymentFileWkfStatus;
import com.nokor.efinance.core.workflow.PaymentWkfStatus;
import com.nokor.frmk.vaadin.util.i18n.I18N;

/**
 * Payment Allocation Service Implementation
 * @author bunlong.taing
 */
@Service("paymentAllocationService")
public class PaymentAllocationServiceImpl extends MainEntityServiceImpl implements PaymentAllocationService, CashflowEntityField {

	/** */
	private static final long serialVersionUID = -4426759172517699505L;
	
	@Autowired
	private PaymentFileDao dao;
	
	@Autowired
	private CashflowService cashflowService;
	
	@Autowired
	private LockSplitService lockSplitService;
	
	@Autowired
	private PaymentService paymentService;

	/**
	 */
	public PaymentAllocationServiceImpl() {
	}
	
	/**
	 * @see org.seuksa.frmk.service.impl.MainEntityServiceImpl#getDao()
	 */
	@Override
	public PaymentFileDao getDao() {
		return dao;
	}
	
	/**
	 * @see org.seuksa.frmk.service.impl.MainEntityServiceImpl#createProcess(org.seuksa.frmk.model.entity.MainEntity)
	 */
	@Override
	public void createProcess(MainEntity mainEntity) throws DaoException {
		super.createProcess(mainEntity);
	}
	
	/**
	 * @see org.seuksa.frmk.service.impl.MainEntityServiceImpl#updateProcess(org.seuksa.frmk.model.entity.MainEntity)
	 */
	@Override
	public void updateProcess(MainEntity mainEntity) throws DaoException {
		super.updateProcess(mainEntity);
	}
	
	/**
	 * @see org.seuksa.frmk.service.impl.MainEntityServiceImpl#deleteProcess(org.seuksa.frmk.model.entity.MainEntity)
	 */
	@Override
	public void deleteProcess(MainEntity mainEntity) throws DaoException {
		throwIntoRecycledBin(mainEntity);
	}
	
	/**
	 * @see com.nokor.efinance.core.payment.service.PaymentAllocationService#listPaymentFileItemToAllocate(com.nokor.efinance.core.payment.model.PaymentFile)
	 */
	@Override
	public List<PaymentFileItem> listPaymentFileItemToAllocate(PaymentFile paymentFile) {
		if (paymentFile == null) {
			return null;
		}
		PaymentFileItemRestriction restriction = new PaymentFileItemRestriction();
		restriction.setPaymentFileId(paymentFile.getId());
		restriction.getWkfStatusList().add(PaymentFileWkfStatus.NEW);
		return list(restriction);
	}

	/**
	 * @see com.nokor.efinance.core.payment.service.PaymentAllocationService#allocatePayments(java.util.List)
	 */
	@Override
	public List<Payment> allocatePayments(List<PaymentFile> paymentFiles) {
		List<Payment> payments = new ArrayList<>();
		for (PaymentFile paymentFile : paymentFiles) {
			payments.addAll(allocatePayments(paymentFile));
		}
		return payments;
	}

	/**
	 * @see com.nokor.efinance.core.payment.service.PaymentAllocationService#allocatePayments(com.nokor.efinance.core.payment.model.PaymentFile)
	 */
	@Override
	public List<Payment> allocatePayments(PaymentFile paymentFile) {
		List<Payment> payments = new ArrayList<>();
		if (paymentFile == null) {
			throw new IllegalArgumentException(I18N.message("error.no.payment.file"));
		}
		if (!PaymentFileWkfStatus.NEW.equals(paymentFile.getWkfStatus()) && !PaymentFileWkfStatus.PARTIAL_ALLOCATED.equals(paymentFile.getWkfStatus())) {
			throw new IllegalStateException(I18N.message("error.already.allocated.payment.file"));
		}
		
		List<PaymentFileItem> paymentFileItems = listPaymentFileItemToAllocate(paymentFile);
		payments = allocatePaymentFileItems(paymentFileItems);
		if (payments.size() == paymentFileItems.size()) {
			paymentFile.setWkfStatus(PaymentFileWkfStatus.ALLOCATED);
		} else if (!payments.isEmpty()) {
			paymentFile.setWkfStatus(PaymentFileWkfStatus.PARTIAL_ALLOCATED);
		}
		
		saveOrUpdate(paymentFile);
		return payments;
	}
	
	/**
	 * @see com.nokor.efinance.core.payment.service.PaymentAllocationService#allocatePaymentFileItems(java.util.List)
	 */
	@Override
	public List<Payment> allocatePaymentFileItems(List<PaymentFileItem> paymentFileItems) {
		List<Payment> payments  = new ArrayList<>();
		if (paymentFileItems != null) {
			for (PaymentFileItem paymentFileItem : paymentFileItems) {
				Payment payment = allocatePaymentFileItem(paymentFileItem);
				if (payment != null) {
					payments.add(payment);
				}
			}
		}
		return payments;
	}

	/**
	 * @see com.nokor.efinance.core.payment.service.PaymentAllocationService#allocatePaymentFileItem(com.nokor.efinance.core.payment.model.PaymentFileItem)
	 */
	@Override
	public Payment allocatePaymentFileItem(PaymentFileItem paymentFileItem) {
		Payment payment = null;
		if (paymentFileItem == null) {
			throw new IllegalArgumentException(I18N.message("error.no.payment.file.item"));
		}
		if (PaymentFileWkfStatus.ALLOCATED.equals(paymentFileItem.getWkfStatus())) {
			throw new IllegalStateException(I18N.message("error.already.allocated.payment.file.item"));
		}
		Contract contract = getByField(Contract.class, Contract.REFERENCE, StringUtils.trim(paymentFileItem.getCustomerRef1()));
		Dealer dealer = getByField(Dealer.class, Dealer.CODE, StringUtils.trim(paymentFileItem.getDealerNo()));
		if (contract == null && dealer == null) {
			paymentFileItem.setWkfStatus(PaymentFileWkfStatus.UNIDENTIFIED);
		} else {
			List<Cashflow> cashflowsToPaid = cashflowService.getCashflowsToPaid(contract.getId());
			double totalCashflowsAmount = getTotalCashflowsAmount(cashflowsToPaid);
			Double totalPaymentAmount = paymentFileItem.getAmount();
			
			if (totalPaymentAmount > totalCashflowsAmount) {
				paymentFileItem.setWkfStatus(PaymentFileWkfStatus.OVER);
			} else if (totalCashflowsAmount - totalPaymentAmount < 2000) {
				paymentFileItem.setWkfStatus(PaymentFileWkfStatus.SUSPENDED);
			} else {
				
				List<LockSplit> lockSplits = getLockSplitToAllocate(contract);
				
				if (lockSplits != null && !lockSplits.isEmpty()) {
					
					double totalLockSplitAmount = getTotalLockSplitAmount(lockSplits);
			
					// Payment total is equal to at least one LockSplit
					List<LockSplit> equalAmountLockSplits = getLockSplitEqualToPaymentTotal(lockSplits, totalPaymentAmount);
					if (!equalAmountLockSplits.isEmpty()) {
						paymentFileItem.setWkfStatus(PaymentFileWkfStatus.MATCHED);
						
					// Payment total is equal to all lock split
					} else if (totalPaymentAmount == totalLockSplitAmount) {
						paymentFileItem.setWkfStatus(PaymentFileWkfStatus.MATCHED);
						
					// Payment total is equal to all lock split and cash flow
					} else {
						paymentFileItem.setWkfStatus(PaymentFileWkfStatus.UNMATCHED);
					}
				} else {
					paymentFileItem.setWkfStatus(PaymentFileWkfStatus.MATCHED);
				}
			}
		}
		saveOrUpdate(paymentFileItem);
		return payment;
	}
	
	/**
	 * @see com.nokor.efinance.core.payment.service.PaymentAllocationService#allocatedPayment(com.nokor.efinance.core.payment.model.PaymentFileItem)
	 */
	@Override
	public void allocatedPayment(PaymentFileItem paymentFileItem) {
		allocatePaymentFileItem(paymentFileItem);
		if (PaymentFileWkfStatus.MATCHED.equals(paymentFileItem.getWkfStatus())) {
			Contract contract = getByField(Contract.class, Contract.REFERENCE, StringUtils.trim(paymentFileItem.getCustomerRef1()));
			List<LockSplit> lockSplits = getLockSplitToAllocate(contract);
			Double totalPaymentAmount = paymentFileItem.getAmount();
			List<Cashflow> cashflowsToPaid = cashflowService.getCashflowsToPaid(contract.getId());
			
			if (lockSplits != null && !lockSplits.isEmpty()) {
				
				double totalLockSplitAmount = getTotalLockSplitAmount(lockSplits);
				
		
				// Payment total is equal to at least one LockSplit
				List<LockSplit> equalAmountLockSplits = getLockSplitEqualToPaymentTotal(lockSplits, totalPaymentAmount);
				if (!equalAmountLockSplits.isEmpty()) {
					LockSplit lockSplit = getLatestLockSplit(equalAmountLockSplits);
					lockSplitService.validate(lockSplit);
					paymentFileItem.setWkfStatus(PaymentFileWkfStatus.ALLOCATED);
					
				// Payment total is equal to all lock split
				} else if (totalPaymentAmount == totalLockSplitAmount) {
					 for (LockSplit lockSplit : equalAmountLockSplits) {
					 	lockSplitService.validate(lockSplit);
					 }
					paymentFileItem.setWkfStatus(PaymentFileWkfStatus.ALLOCATED);
				} 
			} else {
				Payment payment = processCashflowPayments(totalPaymentAmount, cashflowsToPaid, contract);
				if (payment != null) {
					paymentFileItem.setWkfStatus(PaymentFileWkfStatus.ALLOCATED);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param totalPayment
	 * @param cashflowsToPaid
	 * @param contract
	 */
	private Payment processCashflowPayments(double totalPayment, List<Cashflow> cashflowsToPaid, Contract contract) {
		double teTotalAmount = 0d;
		double vatTotalAmount = 0d;
		double tiTotalAmount = 0d;
		List<Cashflow> cashflowsPaid = new ArrayList<>();
		
		for (Cashflow cashflow : cashflowsToPaid) {
			if (totalPayment >= cashflow.getTiInstallmentAmount()) {
				totalPayment -= cashflow.getTiInstallmentAmount();
			} else if (totalPayment > 0) {
				double tiPartialAmount = cashflow.getTiInstallmentAmount() - totalPayment;
				Amount amount = MyMathUtils.calculateFromAmountIncl(totalPayment, cashflow.getVatValue());
				Amount partialAmount = MyMathUtils.calculateFromAmountIncl(tiPartialAmount, cashflow.getVatValue());
				
				cashflow.setTeInstallmentAmount(amount.getTeAmount());
				cashflow.setVatInstallmentAmount(amount.getVatAmount());
				cashflow.setTiInstallmentAmount(amount.getTiAmount());
				
				Cashflow partialCashflow = createCashflow(cashflow, partialAmount);
				cashflowService.saveOrUpdate(partialCashflow);
				totalPayment = 0d;
			} else {
				break;
			}
			cashflow.setPaid(true);
			teTotalAmount += cashflow.getTeInstallmentAmount();
			vatTotalAmount += cashflow.getVatInstallmentAmount();
			tiTotalAmount += cashflow.getTiInstallmentAmount();
			cashflowsPaid.add(cashflow);
		}
		
		Payment payment = createPayment(contract, teTotalAmount, vatTotalAmount, tiTotalAmount);
		payment.setCashflows(cashflowsPaid);
		paymentService.createPayment(payment);
		return payment;
	}
	
	
	/**
	 * @param cashflows
	 * @return
	 */
	private double getTotalCashflowsAmount(List<Cashflow> cashflows) {
		double totalDue = 0d;
		if (cashflows != null) {
			for (Cashflow cashflow : cashflows) {
				totalDue += cashflow.getTiInstallmentAmount();
			}
		}
		return totalDue;
	}
	
	/**
	 * @param contract
	 * @return
	 */
	private List<LockSplit> getLockSplitToAllocate(Contract contract) {
		LockSplitRestriction restriction = new LockSplitRestriction();
		restriction.setContractID(contract.getReference());
		List<EWkfStatus> pendingStatus = new ArrayList<>();
		pendingStatus.add(LockSplitWkfStatus.LNEW);
		pendingStatus.add(LockSplitWkfStatus.LPEN);
		pendingStatus.add(LockSplitWkfStatus.LPAR);
		restriction.setWkfStatusList(pendingStatus);
		return list(restriction);
	}
	
	/**
	 * 
	 * @param lockSplits
	 * @return
	 */
	private Double getTotalLockSplitAmount(List<LockSplit> lockSplits) {
		double total = 0d;
		if (lockSplits != null) {
			for (LockSplit lockSplit : lockSplits) {
				List<LockSplitItem> items = lockSplit.getItems();
				if (items != null) {
					for (LockSplitItem item : items) {
						total += MyNumberUtils.getDouble(item.getTiAmount());
					}
				}
			}
		}
		return total;
	}
	
	/**
	 * @param lockSplits
	 * @param totalPaymentAmount
	 * @return
	 */
	private List<LockSplit> getLockSplitEqualToPaymentTotal(List<LockSplit> lockSplits, Double totalPaymentAmount) {
		List<LockSplit> result = new ArrayList<>();
		if (lockSplits != null) {
			for (LockSplit lockSplit : lockSplits) {
				double totalAmount = 0d;
				List<LockSplitItem> items = lockSplit.getItems();
				if (items != null) {
					for (LockSplitItem item : items) {
						totalAmount += MyNumberUtils.getDouble(item.getTiAmount());
					}
					if (totalAmount == totalPaymentAmount) {
						result.add(lockSplit);
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * @param lockSplits
	 * @return
	 */
	private LockSplit getLatestLockSplit(List<LockSplit> lockSplits) {
		LockSplit result = null;
		if (!lockSplits.isEmpty()) {
			result = lockSplits.get(0);
		}
		for (int i = 1; i < lockSplits.size() - 1; i++) {
			if (DateUtils.isAfterDay(lockSplits.get(i).getCreateDate(), result.getCreateDate())) {
				result = lockSplits.get(i);
			}
		}
		return result;
	}
	
	/**
	 * @param cashflow
	 * @param amount
	 * @return
	 */
	private Cashflow createCashflow(Cashflow cashflow, Amount amount) {
		Cashflow newCashflow = CashflowUtils.createCashflow(
				cashflow.getProductLine(), null, cashflow.getContract(), cashflow.getVatValue(),
				cashflow.getCashflowType(), cashflow.getTreasuryType(), cashflow.getJournalEvent(),
				cashflow.getPaymentMethod(), 
				amount.getTeAmount(), 
				amount.getVatAmount(),
				amount.getTiAmount(), 
				cashflow.getInstallmentDate(), 
				cashflow.getPeriodStartDate(), 
				cashflow.getPeriodEndDate(), 
				cashflow.getNumInstallment());
		newCashflow.setService(cashflow.getService());
		newCashflow.setOrigin(cashflow.getOrigin());
		return newCashflow;
	}
	
	/**
	 * @param contract
	 * @param teTotalAmount
	 * @param vatTotalAmount
	 * @param tiTotalAmount
	 * @return
	 */
	private Payment createPayment(Contract contract, double teTotalAmount, double vatTotalAmount, double tiTotalAmount) {
		Payment payment = new Payment();
		payment.setApplicant(contract.getApplicant());
		payment.setContract(contract);
		payment.setPaymentDate(DateUtils.today());
		payment.setPaymentMethod(EPaymentMethod.CASH);
		payment.setTePaidAmount(teTotalAmount);
		payment.setVatPaidAmount(vatTotalAmount);
		payment.setTiPaidAmount(tiTotalAmount);
		payment.setWkfStatus(PaymentWkfStatus.PAI);
		payment.setPaymentType(EPaymentType.IRC);
		payment.setDealer(contract.getDealer());
		return payment;
	}

}
