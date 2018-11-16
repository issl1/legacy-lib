package com.nokor.efinance.core.shared.exception;

/**
 * Business exception
 * @author ly.youhort
 */
public class BusinessException extends Exception {

	private static final long serialVersionUID = -4766708106666455914L;

	public BusinessException() {
		super();
	}
	
	public BusinessException(String message) {
		super(message);
	}
	
	public BusinessException(String message, Throwable e) {
		super(message, e);
	}
}
