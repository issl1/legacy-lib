package com.nokor.efinance.core.contract.service.aftersales;

import java.io.Serializable;

/**
 * 
 * @author kim.meng
 *
 */
public class ReverseContractStatusValidateResponse implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4098732589411947529L;
	
	public static final int ERROR_NONE = 0;
	public static final int ERROR_OTHER = -1;
	
	private int errorCode;
	private String errorMessage;
	/**
	 * @return the errorCode
	 */
	public int getErrorCode() {
		return errorCode;
	}
	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
