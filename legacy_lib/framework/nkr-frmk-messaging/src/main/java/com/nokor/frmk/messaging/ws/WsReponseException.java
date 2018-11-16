package com.nokor.frmk.messaging.ws;

import javax.ws.rs.WebApplicationException;

/**
 * 
 * @author prasnar
 *
 */
public class WsReponseException extends WebApplicationException {
	/** */
	private static final long serialVersionUID = 1511112890534131381L;

	/**
	 * 
	 * @param message
	 */
	public WsReponseException(String message) {
		this(message, null);
	}
		
	/**
	 * 
	 * @param message
	 * @param location
	 */
	public WsReponseException(String message, String location) {
		super(ResponseHelper.error(message, location));
	}
	
	/**
	 * 
	 * @param errStatus
	 * @param message
	 */
	public WsReponseException(EResponseStatus errStatus, String message) {
		super(ResponseHelper.error(errStatus, message, null));
	}
	
	/**
	 * 
	 * @param errStatus
	 * @param message
	 * @param location
	 */
	public WsReponseException(EResponseStatus errStatus, String message, String location) {
		super(ResponseHelper.error(errStatus, message, location));
	}
}
