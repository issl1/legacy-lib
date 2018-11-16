package com.nokor.efinance.share.credit;

import java.io.Serializable;
/**
 * 
 * @author buntha.chea
 *
 */
public class CreditControlDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Boolean isBacklistActivated;
	private Boolean isBacklistBorrower;
	private Boolean isBacklistGuarantor;
	private Boolean isBacklistReference;
	
	private Boolean isSuspiciousPhoneNumberActivated;
	
	private Boolean isEmploymentActiveated;
	
	private Boolean isNetIncomeActivated;
	private String netIncomeLimit;
	
	private Boolean isAgeActivated;
	private String minAge;
	private String maxAge;
	
	private Boolean isResidenceRegistrationAddressActivated;
	private String minMonth;
	
	private Boolean isLoanToIncome;
	private String PaymentIncomeLimit;
	
	private Boolean isNumberActiviveLoans;
	private String maxActiveLoan;
	
	private Boolean isDelinquencyCheckActivated;
	private String minorDPD;
	private String majorDPD;
	
	private Boolean isMaximumCumulatedDebtRatioActivated;

	/**
	 * @return the isBacklistActivated
	 */
	public Boolean getIsBacklistActivated() {
		return isBacklistActivated;
	}

	/**
	 * @param isBacklistActivated the isBacklistActivated to set
	 */
	public void setIsBacklistActivated(Boolean isBacklistActivated) {
		this.isBacklistActivated = isBacklistActivated;
	}

	/**
	 * @return the isBacklistBorrower
	 */
	public Boolean getIsBacklistBorrower() {
		return isBacklistBorrower;
	}

	/**
	 * @param isBacklistBorrower the isBacklistBorrower to set
	 */
	public void setIsBacklistBorrower(Boolean isBacklistBorrower) {
		this.isBacklistBorrower = isBacklistBorrower;
	}

	/**
	 * @return the isBacklistGuarantor
	 */
	public Boolean getIsBacklistGuarantor() {
		return isBacklistGuarantor;
	}

	/**
	 * @param isBacklistGuarantor the isBacklistGuarantor to set
	 */
	public void setIsBacklistGuarantor(Boolean isBacklistGuarantor) {
		this.isBacklistGuarantor = isBacklistGuarantor;
	}

	/**
	 * @return the isBacklistReference
	 */
	public Boolean getIsBacklistReference() {
		return isBacklistReference;
	}

	/**
	 * @param isBacklistReference the isBacklistReference to set
	 */
	public void setIsBacklistReference(Boolean isBacklistReference) {
		this.isBacklistReference = isBacklistReference;
	}

	/**
	 * @return the isSuspiciousPhoneNumberActivated
	 */
	public Boolean getIsSuspiciousPhoneNumberActivated() {
		return isSuspiciousPhoneNumberActivated;
	}

	/**
	 * @param isSuspiciousPhoneNumberActivated the isSuspiciousPhoneNumberActivated to set
	 */
	public void setIsSuspiciousPhoneNumberActivated(
			Boolean isSuspiciousPhoneNumberActivated) {
		this.isSuspiciousPhoneNumberActivated = isSuspiciousPhoneNumberActivated;
	}

	/**
	 * @return the isEmploymentActiveated
	 */
	public Boolean getIsEmploymentActiveated() {
		return isEmploymentActiveated;
	}

	/**
	 * @param isEmploymentActiveated the isEmploymentActiveated to set
	 */
	public void setIsEmploymentActiveated(Boolean isEmploymentActiveated) {
		this.isEmploymentActiveated = isEmploymentActiveated;
	}

	/**
	 * @return the isNetIncomeActivated
	 */
	public Boolean getIsNetIncomeActivated() {
		return isNetIncomeActivated;
	}

	/**
	 * @param isNetIncomeActivated the isNetIncomeActivated to set
	 */
	public void setIsNetIncomeActivated(Boolean isNetIncomeActivated) {
		this.isNetIncomeActivated = isNetIncomeActivated;
	}

	/**
	 * @return the netIncomeLimit
	 */
	public String getNetIncomeLimit() {
		return netIncomeLimit;
	}

	/**
	 * @param netIncomeLimit the netIncomeLimit to set
	 */
	public void setNetIncomeLimit(String netIncomeLimit) {
		this.netIncomeLimit = netIncomeLimit;
	}

	/**
	 * @return the isAgeActivated
	 */
	public Boolean getIsAgeActivated() {
		return isAgeActivated;
	}

	/**
	 * @param isAgeActivated the isAgeActivated to set
	 */
	public void setIsAgeActivated(Boolean isAgeActivated) {
		this.isAgeActivated = isAgeActivated;
	}

	/**
	 * @return the minAge
	 */
	public String getMinAge() {
		return minAge;
	}

	/**
	 * @param minAge the minAge to set
	 */
	public void setMinAge(String minAge) {
		this.minAge = minAge;
	}

	/**
	 * @return the maxAge
	 */
	public String getMaxAge() {
		return maxAge;
	}

	/**
	 * @param maxAge the maxAge to set
	 */
	public void setMaxAge(String maxAge) {
		this.maxAge = maxAge;
	}

	/**
	 * @return the isResidenceRegistrationAddressActivated
	 */
	public Boolean getIsResidenceRegistrationAddressActivated() {
		return isResidenceRegistrationAddressActivated;
	}

	/**
	 * @param isResidenceRegistrationAddressActivated the isResidenceRegistrationAddressActivated to set
	 */
	public void setIsResidenceRegistrationAddressActivated(
			Boolean isResidenceRegistrationAddressActivated) {
		this.isResidenceRegistrationAddressActivated = isResidenceRegistrationAddressActivated;
	}

	/**
	 * @return the minMonth
	 */
	public String getMinMonth() {
		return minMonth;
	}

	/**
	 * @param minMonth the minMonth to set
	 */
	public void setMinMonth(String minMonth) {
		this.minMonth = minMonth;
	}

	/**
	 * @return the isLoanToIncome
	 */
	public Boolean getIsLoanToIncome() {
		return isLoanToIncome;
	}

	/**
	 * @param isLoanToIncome the isLoanToIncome to set
	 */
	public void setIsLoanToIncome(Boolean isLoanToIncome) {
		this.isLoanToIncome = isLoanToIncome;
	}

	/**
	 * @return the paymentIncomeLimit
	 */
	public String getPaymentIncomeLimit() {
		return PaymentIncomeLimit;
	}

	/**
	 * @param paymentIncomeLimit the paymentIncomeLimit to set
	 */
	public void setPaymentIncomeLimit(String paymentIncomeLimit) {
		PaymentIncomeLimit = paymentIncomeLimit;
	}

	/**
	 * @return the isNumberActiviveLoans
	 */
	public Boolean getIsNumberActiviveLoans() {
		return isNumberActiviveLoans;
	}

	/**
	 * @param isNumberActiviveLoans the isNumberActiviveLoans to set
	 */
	public void setIsNumberActiviveLoans(Boolean isNumberActiviveLoans) {
		this.isNumberActiviveLoans = isNumberActiviveLoans;
	}

	/**
	 * @return the maxActiveLoan
	 */
	public String getMaxActiveLoan() {
		return maxActiveLoan;
	}

	/**
	 * @param maxActiveLoan the maxActiveLoan to set
	 */
	public void setMaxActiveLoan(String maxActiveLoan) {
		this.maxActiveLoan = maxActiveLoan;
	}

	/**
	 * @return the isDelinquencyCheckActivated
	 */
	public Boolean getIsDelinquencyCheckActivated() {
		return isDelinquencyCheckActivated;
	}

	/**
	 * @param isDelinquencyCheckActivated the isDelinquencyCheckActivated to set
	 */
	public void setIsDelinquencyCheckActivated(Boolean isDelinquencyCheckActivated) {
		this.isDelinquencyCheckActivated = isDelinquencyCheckActivated;
	}

	/**
	 * @return the minorDPD
	 */
	public String getMinorDPD() {
		return minorDPD;
	}

	/**
	 * @param minorDPD the minorDPD to set
	 */
	public void setMinorDPD(String minorDPD) {
		this.minorDPD = minorDPD;
	}

	/**
	 * @return the majorDPD
	 */
	public String getMajorDPD() {
		return majorDPD;
	}

	/**
	 * @param majorDPD the majorDPD to set
	 */
	public void setMajorDPD(String majorDPD) {
		this.majorDPD = majorDPD;
	}

	/**
	 * @return the isMaximumCumulatedDebtRatioActivated
	 */
	public Boolean getIsMaximumCumulatedDebtRatioActivated() {
		return isMaximumCumulatedDebtRatioActivated;
	}

	/**
	 * @param isMaximumCumulatedDebtRatioActivated the isMaximumCumulatedDebtRatioActivated to set
	 */
	public void setIsMaximumCumulatedDebtRatioActivated(
			Boolean isMaximumCumulatedDebtRatioActivated) {
		this.isMaximumCumulatedDebtRatioActivated = isMaximumCumulatedDebtRatioActivated;
	}
}
