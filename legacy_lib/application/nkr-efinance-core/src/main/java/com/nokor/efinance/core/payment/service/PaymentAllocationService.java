package com.nokor.efinance.core.payment.service;

import java.util.List;

import org.seuksa.frmk.service.MainEntityService;

import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.payment.model.PaymentFile;
import com.nokor.efinance.core.payment.model.PaymentFileItem;

/**
 * Payment Allocation Service
 * @author bunlong.taing
 */
public interface PaymentAllocationService extends MainEntityService {
	
	/**
	 * @param paymentFile
	 * @return
	 */
	List<PaymentFileItem> listPaymentFileItemToAllocate(PaymentFile paymentFile);
	
	/**
	 * @param paymentFiles
	 */
	List<Payment> allocatePayments(List<PaymentFile> paymentFiles);
	
	/**
	 * @param paymentFile
	 */
	List<Payment> allocatePayments(PaymentFile paymentFile);
	
	/**
	 * @param paymentFileItems
	 */
	List<Payment> allocatePaymentFileItems(List<PaymentFileItem> paymentFileItems);

	/**
	 * @param paymentFileItem
	 */
	Payment allocatePaymentFileItem(PaymentFileItem paymentFileItem);
	
	/**
	 * 
	 * @param paymentFileItem
	 */
	void allocatedPayment(PaymentFileItem paymentFileItem);

}
