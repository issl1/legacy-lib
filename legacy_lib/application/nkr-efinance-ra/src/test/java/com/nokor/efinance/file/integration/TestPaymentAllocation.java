package com.nokor.efinance.file.integration;

import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.payment.model.PaymentFile;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.frmk.testing.BaseTestCase;

/**
 * Test Payment Allocation
 * @author bunlong.taing
 */
public class TestPaymentAllocation extends BaseTestCase implements FinServicesHelper, CashflowEntityField {
	
	public TestPaymentAllocation() {
	}
	
	/**
	 * @see com.nokor.frmk.testing.BaseTestCase#isRequiredAuhentication()
	 */
	@Override
	protected boolean isRequiredAuhentication() {
		return false;
	}
	
	/**
	 * @see com.nokor.frmk.testing.BaseTestCase#setAuthentication()
	 */
	@Override
	protected void setAuthentication() {
		login = "admin";
		password = "admin@EFIN";
	}
	
	/**
	 */
	public void testPaymentAllocation() {
		PaymentFile paymentFile = PAYMENT_ALLOCATION_SRV.getById(PaymentFile.class, 48l);
		PAYMENT_ALLOCATION_SRV.allocatePayments(paymentFile);
	}

}
