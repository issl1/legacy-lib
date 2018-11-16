package com.nokor.efinance.share.contract;

import java.io.Serializable;
import java.util.Date;

/**
 * @author youhort.ly
 *
 */
public class RepossessionDTO implements Serializable {

	/** 
	 */
	private static final long serialVersionUID = 8138605591419855003L;
	
	private String contractID;
	private Date foreclosureDate;
	private String foreclosureStaffID;
	private Date deliveryDate;
	private String deliveryStaffID;
	
	
	/**
	 * @return the contractID
	 */
	public String getContractID() {
		return contractID;
	}
	/**
	 * @param contractID the contractID to set
	 */
	public void setContractID(String contractID) {
		this.contractID = contractID;
	}
	/**
	 * @return the foreclosureDate
	 */
	public Date getForeclosureDate() {
		return foreclosureDate;
	}
	/**
	 * @param foreclosureDate the foreclosureDate to set
	 */
	public void setForeclosureDate(Date foreclosureDate) {
		this.foreclosureDate = foreclosureDate;
	}
	/**
	 * @return the foreclosureStaffID
	 */
	public String getForeclosureStaffID() {
		return foreclosureStaffID;
	}
	/**
	 * @param foreclosureStaffID the foreclosureStaffID to set
	 */
	public void setForeclosureStaffID(String foreclosureStaffID) {
		this.foreclosureStaffID = foreclosureStaffID;
	}
	/**
	 * @return the deliveryDate
	 */
	public Date getDeliveryDate() {
		return deliveryDate;
	}
	/**
	 * @param deliveryDate the deliveryDate to set
	 */
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	/**
	 * @return the deliveryStaffID
	 */
	public String getDeliveryStaffID() {
		return deliveryStaffID;
	}
	/**
	 * @param deliveryStaffID the deliveryStaffID to set
	 */
	public void setDeliveryStaffID(String deliveryStaffID) {
		this.deliveryStaffID = deliveryStaffID;
	}		
}
