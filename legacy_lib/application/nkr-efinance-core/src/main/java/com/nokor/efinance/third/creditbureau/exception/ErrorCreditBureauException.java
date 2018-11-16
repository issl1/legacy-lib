package com.nokor.efinance.third.creditbureau.exception;

import com.nokor.efinance.core.shared.exception.BusinessException;

/**
 * Error Credit Bureau Exception
 * @author ly.youhort
 */
public class ErrorCreditBureauException extends BusinessException {

	private static final long serialVersionUID = -1876516919210851235L;

	public ErrorCreditBureauException(String message) {
		this(message, null);
	}
	
	public ErrorCreditBureauException(String message, Throwable e) {
		super(message, e);
	}
}
