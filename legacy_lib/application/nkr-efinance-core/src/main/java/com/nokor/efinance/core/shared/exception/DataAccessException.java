package com.nokor.efinance.core.shared.exception;

/**
 * Database access exception
 * @author ly.youhort
 */
public class DataAccessException extends Exception {

	private static final long serialVersionUID = -4766708106666455914L;

	/**
	 */
	public DataAccessException() {
		super();
	}
	
	/**
	 * @param message
	 */
	public DataAccessException(String message) {
		super(message);
	}
	
	/**
	 * @param message
	 * @param e
	 */
	public DataAccessException(String message, Throwable e) {
		super(message, e);
	}
}
