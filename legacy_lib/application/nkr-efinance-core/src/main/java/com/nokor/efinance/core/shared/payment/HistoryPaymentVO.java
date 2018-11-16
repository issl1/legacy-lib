package com.nokor.efinance.core.shared.payment;

import java.io.Serializable;
import java.util.Date;

public class HistoryPaymentVO implements Serializable{

	private static final long serialVersionUID = -3572319928571504600L;
	private Long id;
	private String contractReference;
	private String lastName;
	private String firstName;
	private String mobilePhone;
	private String dealer;
	private String dealerType;
	private Integer contractTerm;
	private Date lastInstallmentPaid;
	private int numInstallmentPaid;
	private int numInstallmentPaidOnSchedule;
	private int numOverdue1to10;
	private int numOverdue11to30;
	private int numOverdueOver30;
	private boolean isCurrentContractInOverdue;
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the contractReference
	 */
	public String getContractReference() {
		return contractReference;
	}
	/**
	 * @param contractReference the contractReference to set
	 */
	public void setContractReference(String contractReference) {
		this.contractReference = contractReference;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the mobilePhone
	 */
	public String getMobilePhone() {
		return mobilePhone;
	}
	/**
	 * @param mobilePhone the mobilePhone to set
	 */
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	/**
	 * @return the dealer
	 */
	public String getDealer() {
		return dealer;
	}
	/**
	 * @param dealer the dealer to set
	 */
	public void setDealer(String dealer) {
		this.dealer = dealer;
	}
	/**
	 * @return the contractTerm
	 */
	public Integer getContractTerm() {
		return contractTerm;
	}
	/**
	 * @param contractTerm the contractTerm to set
	 */
	public void setContractTerm(Integer contractTerm) {
		this.contractTerm = contractTerm;
	}
	/**
	 * @return the lastInstallmentPaid
	 */
	public Date getLastInstallmentPaid() {
		return lastInstallmentPaid;
	}
	/**
	 * @param lastInstallmentPaid the lastInstallmentPaid to set
	 */
	public void setLastInstallmentPaid(Date lastInstallmentPaid) {
		this.lastInstallmentPaid = lastInstallmentPaid;
	}
	/**
	 * @return the numInstallmentPaid
	 */
	public int getNumInstallmentPaid() {
		return numInstallmentPaid;
	}
	/**
	 * @param numInstallmentPaid the numInstallmentPaid to set
	 */
	public void setNumInstallmentPaid(int numInstallmentPaid) {
		this.numInstallmentPaid = numInstallmentPaid;
	}
	/**
	 * @return the numInstallmentPaidOnSchedule
	 */
	public int getNumInstallmentPaidOnSchedule() {
		return numInstallmentPaidOnSchedule;
	}
	/**
	 * @param numInstallmentPaidOnSchedule the numInstallmentPaidOnSchedule to set
	 */
	public void setNumInstallmentPaidOnSchedule(int numInstallmentPaidOnSchedule) {
		this.numInstallmentPaidOnSchedule = numInstallmentPaidOnSchedule;
	}
	/**
	 * @return the numOverdue1to10
	 */
	public int getNumOverdue1to10() {
		return numOverdue1to10;
	}
	/**
	 * @param numOverdue1to10 the numOverdue1to10 to set
	 */
	public void setNumOverdue1to10(int numOverdue1to10) {
		this.numOverdue1to10 = numOverdue1to10;
	}
	/**
	 * @return the numOverdue11to30
	 */
	public int getNumOverdue11to30() {
		return numOverdue11to30;
	}
	/**
	 * @param numOverdue11to30 the numOverdue11to30 to set
	 */
	public void setNumOverdue11to30(int numOverdue11to30) {
		this.numOverdue11to30 = numOverdue11to30;
	}
	/**
	 * @return the numOverdueOver30
	 */
	public int getNumOverdueOver30() {
		return numOverdueOver30;
	}
	/**
	 * @param numOverdueOver30 the numOverdueOver30 to set
	 */
	public void setNumOverdueOver30(int numOverdueOver30) {
		this.numOverdueOver30 = numOverdueOver30;
	}
	/**
	 * @return the isCurrentContractInOverdue
	 */
	public boolean isCurrentContractInOverdue() {
		return isCurrentContractInOverdue;
	}
	/**
	 * @param isCurrentContractInOverdue the isCurrentContractInOverdue to set
	 */
	public void setCurrentContractInOverdue(boolean isCurrentContractInOverdue) {
		this.isCurrentContractInOverdue = isCurrentContractInOverdue;
	}
	/**
	 * @return the dealerType
	 */
	public String getDealerType() {
		return dealerType;
	}
	/**
	 * @param dealerType the dealerType to set
	 */
	public void setDealerType(String dealerType) {
		this.dealerType = dealerType;
	}
	
}
