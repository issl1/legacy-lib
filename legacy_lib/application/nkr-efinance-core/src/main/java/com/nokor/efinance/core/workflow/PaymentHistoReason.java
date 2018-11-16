package com.nokor.efinance.core.workflow;

import com.nokor.common.app.history.model.EHistoReason;

/**
 * 
 * @author prasnar
 *
 */
public class PaymentHistoReason  {
	public final static EHistoReason PAYMENT_CREATION = new EHistoReason("PAYMENT_CREATION", 11); // payment.creation
	public final static EHistoReason PAYMENT_CANCELLATION = new EHistoReason("PAYMENT_CANCELLATION", 13); // payment.cancellation
	public final static EHistoReason PAYMENT_CHANGE_STATUS = new EHistoReason("PAYMENT_CHANGE_STATUS", 14); // payment.change.status

	
}
