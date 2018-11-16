package com.nokor.efinance.core.shared.exception;

import java.util.ArrayList;
import java.util.List;


/**
 * Validation fields quotation exception
 * @author ly.youhort
 */
public class ValidationFieldsException extends BusinessException {
	
	private static final long serialVersionUID = -3131217462805735240L;

	private List<ErrorMessage> errorMessages = new ArrayList<ErrorMessage>();
	
	/**
	 * @param errorMessages
	 */
	public ValidationFieldsException(List<ErrorMessage> errorMessages) {
		this.errorMessages = errorMessages;
	}

	/**
	 * @return the errorMessages
	 */
	public List<ErrorMessage> getErrorMessages() {
		return errorMessages;
	}

	/**
	 * @param errorMessages the errorMessages to set
	 */
	public void setErrorMessages(List<ErrorMessage> errorMessages) {
		this.errorMessages = errorMessages;
	}
	
}
