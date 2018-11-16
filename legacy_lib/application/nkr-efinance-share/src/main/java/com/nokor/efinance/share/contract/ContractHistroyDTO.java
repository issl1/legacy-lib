package com.nokor.efinance.share.contract;

import java.io.Serializable;
import java.util.Date;


public class ContractHistroyDTO implements Serializable {

	private static final long serialVersionUID = -378162214770200160L;
	
	private Long id;
	private String applicantType;
	private String applicantionNo;
	private Date applicantDate;
	private String contractNo;
	private Date contractDate;
	private String firstName;
	private String lastName;
	private String contractStatus;
	private Integer nbOverdue;
	private Double totalAR;
	private Double netIncome;
	
	private String blackListReson;
	private String phoneNumber;
	private String idNumber;
	
	
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
	 * @return the applicantDate
	 */
	public Date getApplicantDate() {
		return applicantDate;
	}
	/**
	 * @param applicantDate the applicantDate to set
	 */
	public void setApplicantDate(Date applicantDate) {
		this.applicantDate = applicantDate;
	}
	/**
	 * @return the contractNo
	 */
	public String getContractNo() {
		return contractNo;
	}
	/**
	 * @param contractNo the contractNo to set
	 */
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}
	/**
	 * @return the contractDate
	 */
	public Date getContractDate() {
		return contractDate;
	}
	/**
	 * @param contractDate the contractDate to set
	 */
	public void setContractDate(Date contractDate) {
		this.contractDate = contractDate;
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
	 * @return the contractStatus
	 */
	public String getContractStatus() {
		return contractStatus;
	}
	/**
	 * @param contractStatus the contractStatus to set
	 */
	public void setContractStatus(String contractStatus) {
		this.contractStatus = contractStatus;
	}
	/**
	 * @return the nbOverdue
	 */
	public Integer getNbOverdue() {
		return nbOverdue;
	}
	/**
	 * @param nbOverdue the nbOverdue to set
	 */
	public void setNbOverdue(Integer nbOverdue) {
		this.nbOverdue = nbOverdue;
	}
	/**
	 * @return the applicantType
	 */
	public String getApplicantType() {
		return applicantType;
	}
	/**
	 * @param applicantType the applicantType to set
	 */
	public void setApplicantType(String applicantType) {
		this.applicantType = applicantType;
	}
	/**
	 * @return the applicantNo
	 */
	public String getApplicantNo() {
		return applicantionNo;
	}
	/**
	 * @param applicantNo the applicantNo to set
	 */
	public void setApplicantNo(String applicantNo) {
		this.applicantionNo = applicantNo;
	}
	/**
	 * @return the blackListReson
	 */
	public String getBlackListReson() {
		return blackListReson;
	}
	/**
	 * @param blackListReson the blackListReson to set
	 */
	public void setBlackListReson(String blackListReson) {
		this.blackListReson = blackListReson;
	}
	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}
	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	/**
	 * @return the idNumber
	 */
	public String getIdNumber() {
		return idNumber;
	}
	/**
	 * @param idNumber the idNumber to set
	 */
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	/**
	 * @return the totalAR
	 */
	public Double getTotalAR() {
		return totalAR;
	}
	/**
	 * @param totalAR the totalAR to set
	 */
	public void setTotalAR(Double totalAR) {
		this.totalAR = totalAR;
	}
	/**
	 * @return the netIncome
	 */
	public Double getNetIncome() {
		return netIncome;
	}
	/**
	 * @param netIncome the netIncome to set
	 */
	public void setNetIncome(Double netIncome) {
		this.netIncome = netIncome;
	}
	
}
