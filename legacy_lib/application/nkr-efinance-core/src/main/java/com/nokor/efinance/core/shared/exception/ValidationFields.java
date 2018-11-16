package com.nokor.efinance.core.shared.exception;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.nokor.efinance.core.shared.system.DomainType;

/**
 * Validation fields
 * @author ly.youhort
 */
public class ValidationFields implements Serializable {
	
	private static final long serialVersionUID = -8251320996363241901L;
	
	private List<ErrorMessage> errorMessages = new ArrayList<ErrorMessage>();
	
	/**
	 * @param errorMessages
	 */
	public ValidationFields() {
	}
	
	/**
	 * @param required
	 * @param messageKey
	 */
	public void add(boolean errorValidation, DomainType domainType, String messageKey) {
		add(errorValidation, domainType, messageKey, messageKey);
	}
	
	/**
	 * @param required
	 * @param code
	 * @param messageKey
	 */
	public void add(boolean errorValidation, DomainType domainType, String code, String messageKey) {
		add(errorValidation, new ErrorMessage(domainType, code, messageKey));
	}
	
	/**
	 * @param required
	 * @param code
	 * @param messageKey
	 */
	public void add(boolean errorValidation, DomainType domainType, String code, String messageKey, String... args) {
		add(errorValidation, new ErrorMessage(domainType, code, messageKey, args));
	}
	
	/**
	 * @param required
	 * @param errorMessage
	 */
	public void add(boolean errorValidation, ErrorMessage errorMessage) {
		if (errorValidation) {
			errorMessages.add(errorMessage);
		}
	}
	
	/**
	 * @param errorValidation
	 * @param domainType
	 * @param messageKey
	 */
	public void addRequired(boolean errorValidation, DomainType domainType, String messageKey) {
		add(errorValidation, domainType, "field.required", messageKey);
	}
	
	/**
	 * @param errorValidation
	 * @param domainType
	 * @param messageKey
	 * @param args
	 */
	public void addRequired(boolean errorValidation, DomainType domainType, String messageKey, String... args) {
		add(errorValidation, domainType, "field.required", messageKey, args);
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
