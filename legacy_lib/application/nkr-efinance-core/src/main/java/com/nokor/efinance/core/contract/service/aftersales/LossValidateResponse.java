package com.nokor.efinance.core.contract.service.aftersales;

import java.io.Serializable;

import com.nokor.efinance.core.payment.model.Payment;

public class LossValidateResponse implements Serializable {

	private static final long serialVersionUID = -5160304702412573735L;	
	
	private Payment payment;

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}
	
}
