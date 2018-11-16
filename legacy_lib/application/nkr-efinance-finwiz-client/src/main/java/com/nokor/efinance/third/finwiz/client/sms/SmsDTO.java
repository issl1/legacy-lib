package com.nokor.efinance.third.finwiz.client.sms;

import java.io.Serializable;

/**
 * 
 * @author buntha.chea
 *
 */
public class SmsDTO implements Serializable {

	/** */
	private static final long serialVersionUID = 2348696578068043670L;
	
	private String Contents;
	private String Module;
	private String PhoneNo;
	private String refObjectId;
	private String trigger;
	private String user;
	
	
	/**
	 * @return the contents
	 */
	public String getContents() {
		return Contents;
	}
	/**
	 * @param contents the contents to set
	 */
	public void setContents(String contents) {
		Contents = contents;
	}
	/**
	 * @return the module
	 */
	public String getModule() {
		return Module;
	}
	/**
	 * @param module the module to set
	 */
	public void setModule(String module) {
		Module = module;
	}
	/**
	 * @return the phoneNo
	 */
	public String getPhoneNo() {
		return PhoneNo;
	}
	/**
	 * @param phoneNo the phoneNo to set
	 */
	public void setPhoneNo(String phoneNo) {
		PhoneNo = phoneNo;
	}
	/**
	 * @return the refObjectId
	 */
	public String getRefObjectId() {
		return refObjectId;
	}
	/**
	 * @param refObjectId the refObjectId to set
	 */
	public void setRefObjectId(String refObjectId) {
		this.refObjectId = refObjectId;
	}
	/**
	 * @return the trigger
	 */
	public String getTrigger() {
		return trigger;
	}
	/**
	 * @param trigger the trigger to set
	 */
	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}
	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}	
}
