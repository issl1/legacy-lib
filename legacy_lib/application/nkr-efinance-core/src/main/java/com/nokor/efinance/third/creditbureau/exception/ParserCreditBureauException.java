package com.nokor.efinance.third.creditbureau.exception;

import com.nokor.efinance.core.shared.exception.BusinessException;

/**
 * Parser Credit Bureau Exception
 * @author ly.youhort
 */
public class ParserCreditBureauException extends BusinessException {

	private static final long serialVersionUID = -1876516919210851235L;

	public ParserCreditBureauException(String message) {
		this(message, null);
	}
	
	public ParserCreditBureauException(String message, Throwable e) {
		super(message, e);
	}
}
