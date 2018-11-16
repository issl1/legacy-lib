package com.nokor.efinance.core.shared.exception;

import java.io.Serializable;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.shared.system.DomainType;

/**
 * Error message
 * @author ly.youhort
 *
 */
public class ErrorMessage implements Serializable {
	
	private static final long serialVersionUID = -696221126975750185L;

	private DomainType domainType;
	private String code;
	private String messageKey;
	private String[] args;
	
	/**
	 * @param code
	 * @param messageKey
	 */
	public ErrorMessage(DomainType domainType, String code, String messageKey) {
		this(domainType, code, messageKey, "");
	}
	
	/**
	 * @param code
	 * @param messageKey
	 * @param args
	 */
	public ErrorMessage(DomainType domainType, String code, String messageKey, String... args) {
		this.domainType = domainType;
		this.code = code;
		this.messageKey = messageKey;
		this.args = args;
	}

	
	/**
	 * @return the domainType
	 */
	public DomainType getDomainType() {
		return domainType;
	}

	/**
	 * @param domainType the domainType to set
	 */
	public void setDomainType(DomainType domainType) {
		this.domainType = domainType;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
		
	/**
	 * @return the messageKey
	 */
	public String getMessageKey() {
		return messageKey;
	}

	/**
	 * @param messageKey the messageKey to set
	 */
	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	/**
	 * @return the args
	 */
	public String[] getArgs() {
		return args;
	}
	
	/**
	 * @param args the args to set
	 */
	public void setArgs(String[] args) {
		this.args = args;
	}
	
	
	/**
	 * Get message
	 * @return
	 */
	public String getMessage() {
		if (args == null || args.length == 0) {
			return I18N.message(messageKey);
		} else {
			return I18N.message(messageKey, args);
		}
	}
}
