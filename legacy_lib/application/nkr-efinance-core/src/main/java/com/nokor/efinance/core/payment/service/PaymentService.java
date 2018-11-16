package com.nokor.efinance.core.payment.service;

import java.util.Date;
import java.util.List;

import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.payment.model.EPaymentType;
import com.nokor.efinance.core.payment.model.InstallmentChecked;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.payment.model.PaymentThirdParty;
import com.nokor.efinance.core.shared.payment.HistoryPaymentVO;
import com.nokor.efinance.share.contract.PaymentType;


/**
 * Payment service interface
 * @author mao.heng
 *
 */
public interface PaymentService extends BaseEntityService {

		
	/**
	 * Create a direct cost
	 * @param paymentDate
	 * @param paymentMethod
	 * @return
	 */
	List<Payment> createDirectCosts(List<Long> caflwIds);
	
	/**
	 * Create a direct cost
	 * @param paymentDate
	 * @param paymentMethod
	 * @return
	 */
	Payment createDirectCost(Long caflwId);
	
	/**
	 * 
	 * @param contract
	 * @param paymentDate
	 * @return
	 */
	Payment createDownPayment(Contract contract, Date paymentDate);
	
	/**
	 * Cancel a down payment
	 * @param paymnId
	 */
	void cancelDownPayment(Long paymnId);
	
	/**
	 * Create a new payment
	 * @param payment
	 * @return
	 */
	Payment createPayment(Payment payment);
	
	/**
	 * Change payment status
	 * @param payment
	 * @param paymentStatus
	 */
	void changePaymentStatus(Payment payment, EWkfStatus paymentStatus);
	
	/**
	 * Cancel a payment
	 * @param payment
	 */
	void cancelPayment(Payment payment);
	
	/**
	 * Delete an payment
	 * @param payment
	 */
	void deletePayment(Payment payment);

	/**
	 * @param payment
	 */
	void issuePurchaseOrder(Payment payment);
	
	/**
	 * @param payment
	 */
	void secondPayment(Payment payment);
	
	/**
	 * @param cotraId
	 * @param numInstallment
	 * @return
	 */
	Payment getPaymentByContract(Long cotraId, int numInstallment);
	
	/**
	 * @param ids
	 */
	void cancelPayments(List<Long> ids);
	
	/**
	 * 
	 * @param payments
	 * @return
	 */
	List<HistoryPaymentVO> getHistoryPaymentOnOverdue(List<Payment> payments);
	/**
	 * 
	 * @param cashflows
	 * @return
	 */
	List<HistoryPaymentVO> getHistoryPaymentCurrentOverdue(List<Cashflow> cashflows);
	
	/**
	 * 
	 * @param dealer
	 * @param firstDate
	 * @return
	 */
	List<Payment> getListPaymentRemaining(Dealer dealer, Date firstDate);
	
	/**
	 * Get list payments by contract id
	 * @param conId
	 * @param paymentTypes
	 * @return
	 */
	List<Payment> getListPaymentsByContractID(Long conId, EPaymentType[] paymentTypes);
	
	/**
	 * 
	 * @param conId
	 * @return
	 */
	List<Payment> getListPaymentPaidInCurrentMonth(Long conId);
	
	/**
	 * 
	 * @param dealer
	 * @param date
	 * @return
	 */
	List<InstallmentChecked> getInstallmentChecked(Dealer dealer, Date startDate, Date endDate);
	
	/**
	 * 
	 * @param paymentType
	 * @return
	 */
	List<PaymentThirdParty> getPaymentThirdParty(PaymentType paymentType);
}

