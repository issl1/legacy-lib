package com.nokor.efinance.third.wing.server.payment.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author ly.youhort
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InfoRequestMessage")
public class InfoRequestMessage implements Serializable {

	private static final long serialVersionUID = 6176748043396694625L;

	@XmlElement(name = "serviceHeader", required = true)
	private ServiceHeader serviceHeader;
	
	@XmlElement(name = "reference", required = true)
	private String reference;
			
	/**
	 * @return the serviceHeader
	 */
	public ServiceHeader getServiceHeader() {
		return serviceHeader;
	}
	/**
	 * @param serviceHeader the serviceHeader to set
	 */
	public void setServiceHeader(ServiceHeader serviceHeader) {
		this.serviceHeader = serviceHeader;
	}
	/**
	 * @return the reference
	 */
	public String getReference() {
		return reference;
	}
	/**
	 * @param reference the reference to set
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}
}
