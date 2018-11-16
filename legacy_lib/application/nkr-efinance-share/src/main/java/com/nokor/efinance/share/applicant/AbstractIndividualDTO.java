package com.nokor.efinance.share.applicant;

import java.util.Date;

import com.nokor.common.messaging.share.refdata.RefDataDTO;

/**
 * 
 * @author prasnar
 *
 */
public abstract class AbstractIndividualDTO extends BasePersonDTO {
	/** */
	private static final long serialVersionUID = 3834251079538576695L;

	private String applicantID;	
	private Date createDate;
	private Integer numberOfChildren;
	private Integer numberOfHousehold;
	
	private RefDataDTO religion;
	private RefDataDTO education;
	private RefDataDTO preferredLanguage;
	private RefDataDTO secondLanguage;
	
	private Double householdExpenses;
	private Double householdIncome;
	
	private Double fixedIncome;
	private Double otherIncomes;
	private Double debtOtherLoans;
	private String debtSource;
	private Double monthlyPersonalExpenses;
	private String grade;
	private String status;
	
	
	/**
	 * @return the applicantID
	 */
	public String getApplicantID() {
		return applicantID;
	}
	/**
	 * @param applicantID the applicantID to set
	 */
	public void setApplicantID(String applicantID) {
		this.applicantID = applicantID;
	}
	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * @return the numberOfChildren
	 */
	public Integer getNumberOfChildren() {
		return numberOfChildren;
	}
	/**
	 * @param numberOfChildren the numberOfChildren to set
	 */
	public void setNumberOfChildren(Integer numberOfChildren) {
		this.numberOfChildren = numberOfChildren;
	}
	/**
	 * @return the numberOfHousehold
	 */
	public Integer getNumberOfHousehold() {
		return numberOfHousehold;
	}
	/**
	 * @param numberOfHousehold the numberOfHousehold to set
	 */
	public void setNumberOfHousehold(Integer numberOfHousehold) {
		this.numberOfHousehold = numberOfHousehold;
	}	
	/**
	 * @return the religion
	 */
	public RefDataDTO getReligion() {
		return religion;
	}
	/**
	 * @param religion the religion to set
	 */
	public void setReligion(RefDataDTO religion) {
		this.religion = religion;
	}
	/**
	 * @return the education
	 */
	public RefDataDTO getEducation() {
		return education;
	}
	/**
	 * @param education the education to set
	 */
	public void setEducation(RefDataDTO education) {
		this.education = education;
	}
	/**
	 * @return the preferredLanguage
	 */
	public RefDataDTO getPreferredLanguage() {
		return preferredLanguage;
	}
	/**
	 * @param preferredLanguage the preferredLanguage to set
	 */
	public void setPreferredLanguage(RefDataDTO preferredLanguage) {
		this.preferredLanguage = preferredLanguage;
	}
	/**
	 * @return the secondLanguage
	 */
	public RefDataDTO getSecondLanguage() {
		return secondLanguage;
	}
	/**
	 * @param secondLanguage the secondLanguage to set
	 */
	public void setSecondLanguage(RefDataDTO secondLanguage) {
		this.secondLanguage = secondLanguage;
	}
	/**
	 * @return the householdExpenses
	 */
	public Double getHouseholdExpenses() {
		return householdExpenses;
	}
	/**
	 * @param householdExpenses the householdExpenses to set
	 */
	public void setHouseholdExpenses(Double householdExpenses) {
		this.householdExpenses = householdExpenses;
	}
	/**
	 * @return the householdIncome
	 */
	public Double getHouseholdIncome() {
		return householdIncome;
	}
	/**
	 * @param householdIncome the householdIncome to set
	 */
	public void setHouseholdIncome(Double householdIncome) {
		this.householdIncome = householdIncome;
	}
	/**
	 * @return the fixedIncome
	 */
	public Double getFixedIncome() {
		return fixedIncome;
	}
	/**
	 * @param fixedIncome the fixedIncome to set
	 */
	public void setFixedIncome(Double fixedIncome) {
		this.fixedIncome = fixedIncome;
	}
	/**
	 * @return the otherIncomes
	 */
	public Double getOtherIncomes() {
		return otherIncomes;
	}
	/**
	 * @param otherIncomes the otherIncomes to set
	 */
	public void setOtherIncomes(Double otherIncomes) {
		this.otherIncomes = otherIncomes;
	}
	/**
	 * @return the monthlyPersonalExpenses
	 */
	public Double getMonthlyPersonalExpenses() {
		return monthlyPersonalExpenses;
	}
	/**
	 * @param monthlyPersonalExpenses the monthlyPersonalExpenses to set
	 */
	public void setMonthlyPersonalExpenses(Double monthlyPersonalExpenses) {
		this.monthlyPersonalExpenses = monthlyPersonalExpenses;
	}
	/**
	 * @return the debtOtherLoans
	 */
	public Double getDebtOtherLoans() {
		return debtOtherLoans;
	}
	/**
	 * @param debtOtherLoans the debtOtherLoans to set
	 */
	public void setDebtOtherLoans(Double debtOtherLoans) {
		this.debtOtherLoans = debtOtherLoans;
	}
	/**
	 * @return the debtSource
	 */
	public String getDebtSource() {
		return debtSource;
	}
	/**
	 * @param debtSource the debtSource to set
	 */
	public void setDebtSource(String debtSource) {
		this.debtSource = debtSource;
	}
	/**
	 * @return the grade
	 */
	public String getGrade() {
		return grade;
	}
	/**
	 * @param grade the grade to set
	 */
	public void setGrade(String grade) {
		this.grade = grade;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}	
}
