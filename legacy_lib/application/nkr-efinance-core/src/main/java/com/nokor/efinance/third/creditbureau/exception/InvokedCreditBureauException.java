package com.nokor.efinance.third.creditbureau.exception;

import com.nokor.efinance.core.shared.exception.BusinessException;

/**
 * Credit Bureau Exception
 * @author ly.youhort
 */
public class InvokedCreditBureauException extends BusinessException {

	private static final long serialVersionUID = 2145784765720583857L;
	
	public InvokedCreditBureauException(String message) {
		this(message, null);
	}
	
	public InvokedCreditBureauException(String message, Throwable e) {
		super(message, e);
	}
}
