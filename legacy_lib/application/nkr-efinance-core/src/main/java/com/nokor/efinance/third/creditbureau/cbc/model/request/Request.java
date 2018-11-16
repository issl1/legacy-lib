package com.nokor.efinance.third.creditbureau.cbc.model.request;

import java.io.Serializable;

import com.nokor.efinance.third.creditbureau.cbc.model.Header;

/**
 * @author ly.youhort
 */
public class Request implements Serializable {
	
	private static final long serialVersionUID = 7630460646105960301L;
	
	public static String SERVICE = "ENQUIRY";
	public static String ACTION = "A";
	
	private String noNamespaceSchemaLocation = "Enquiry.xsd";
	private String service;
	private String action;
	private Header header;
	private Message message;
	
	/**
	 * @return the noNamespaceSchemaLocation
	 */
	public String getNoNamespaceSchemaLocation() {
		return noNamespaceSchemaLocation;
	}
	/**
	 * @param noNamespaceSchemaLocation the noNamespaceSchemaLocation to set
	 */
	public void setNoNamespaceSchemaLocation(String noNamespaceSchemaLocation) {
		this.noNamespaceSchemaLocation = noNamespaceSchemaLocation;
	}
	/**
	 * @return the service
	 */
	public String getService() {
		return service;
	}
	/**
	 * @param service the service to set
	 */
	public void setService(String service) {
		this.service = service;
	}
	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}
	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}
	/**
	 * @return the header
	 */
	public Header getHeader() {
		return header;
	}
	/**
	 * @param header the header to set
	 */
	public void setHeader(Header header) {
		this.header = header;
	}
	/**
	 * @return the message
	 */
	public Message getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(Message message) {
		this.message = message;
	}
	
}
