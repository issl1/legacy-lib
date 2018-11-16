package com.nokor.frmk.security.spring.session;

/**
 * Thrown in case of invalid session
 * @author ly.youhort
 */
public class InvalidSessionException extends RuntimeException {

	/**	 */
	private static final long serialVersionUID = 6077769941455031828L;

	/**
	 * 
	 */
	public InvalidSessionException() {
	}

	/**
	 * 
	 * @param message
	 */
	public InvalidSessionException(String message) {
		super(message);
	}
}
