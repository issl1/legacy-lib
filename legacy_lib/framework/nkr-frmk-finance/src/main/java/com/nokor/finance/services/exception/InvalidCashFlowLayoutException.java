package com.nokor.finance.services.exception;

/**
 * Cash Flow Layout Configuration error. * 
 * @author ly.youhort
 */
public class InvalidCashFlowLayoutException extends Exception {
	
	/**
	 * Creates a new instance of InvalidCashFlowLayoutException
	 * @param message explanation message
	 */
	public InvalidCashFlowLayoutException(String message) {
		super(message);
	}
}
