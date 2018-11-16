package com.nokor.efinance.third.wing.server.payment.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.nokor.efinance.third.integration.model.EThirdParty;


/**
 * @author ly.youhort
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ServiceHeader")
public class ServiceHeader implements Serializable {

	private static final long serialVersionUID = 6504707871502553141L;
	
	@XmlElement(name = "thirdParty", required = true)
	private EThirdParty thirdParty;
	
	@XmlElement(name = "username", required = true)
	private String username;
	
	@XmlElement(name = "password", required = true)
	private String password;
		
	/**
	 * @return the thirdParty
	 */
	public EThirdParty getThirdParty() {
		return thirdParty;
	}
	/**
	 * @param thirdParty the thirdParty to set
	 */
	public void setThirdParty(EThirdParty thirdParty) {
		this.thirdParty = thirdParty;
	}
	
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
