package com.nokor.efinance.migration.model.loan;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;

@Entity
@Table(name = "tm_loan")
public class Loan extends EntityRefA implements Serializable{

	private static final long serialVersionUID = 1L;
	private Long id;
	private String migrationId;
	private String productVatCode;
	private String marketingCompaigneCode;
	private Double defaultFinancialAmount;
	private Double financialAmount;
	private Double downPayment;
	private Double downPaymentPercentage;
	private Double effRate;
	private Double flatRate;
	private Integer terms;
	private Date applicantionDate;
	private Date approvalDate;
	private Date activationDate;
	private Date firstDueDate;
	private Date lastDueDate;
	private Double installmentAmount;
	private Double totalAR;
	private Double totalUE;
	private Double totalVAT;
	private Double prepaidInstallment;
	private Integer numberPrepaidTerm;
	private Double serviceFee;
	private Double insuranceFee;
	
	
	/**
	 * @return the id
	 */
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
	 * @return the migrationId
	 */
	public String getMigrationId() {
		return migrationId;
	}
	/**
	 * @param migrationId the migrationId to set
	 */
	public void setMigrationId(String migrationId) {
		this.migrationId = migrationId;
	}
	/**
	 * @return the productVatCode
	 */
	public String getProductVatCode() {
		return productVatCode;
	}
	/**
	 * @param productVatCode the productVatCode to set
	 */
	public void setProductVatCode(String productVatCode) {
		this.productVatCode = productVatCode;
	}
	/**
	 * @return the marketingCompaigneCode
	 */
	public String getMarketingCompaigneCode() {
		return marketingCompaigneCode;
	}
	/**
	 * @param marketingCompaigneCode the marketingCompaigneCode to set
	 */
	public void setMarketingCompaigneCode(String marketingCompaigneCode) {
		this.marketingCompaigneCode = marketingCompaigneCode;
	}
	/**
	 * @return the defaultFinancialAmount
	 */
	public Double getDefaultFinancialAmount() {
		return defaultFinancialAmount;
	}
	/**
	 * @param defaultFinancialAmount the defaultFinancialAmount to set
	 */
	public void setDefaultFinancialAmount(Double defaultFinancialAmount) {
		this.defaultFinancialAmount = defaultFinancialAmount;
	}
	/**
	 * @return the financialAmount
	 */
	public Double getFinancialAmount() {
		return financialAmount;
	}
	/**
	 * @param financialAmount the financialAmount to set
	 */
	public void setFinancialAmount(Double financialAmount) {
		this.financialAmount = financialAmount;
	}
	/**
	 * @return the downPayment
	 */
	public Double getDownPayment() {
		return downPayment;
	}
	/**
	 * @param downPayment the downPayment to set
	 */
	public void setDownPayment(Double downPayment) {
		this.downPayment = downPayment;
	}
	/**
	 * @return the downPaymentPercentage
	 */
	public Double getDownPaymentPercentage() {
		return downPaymentPercentage;
	}
	/**
	 * @param downPaymentPercentage the downPaymentPercentage to set
	 */
	public void setDownPaymentPercentage(Double downPaymentPercentage) {
		this.downPaymentPercentage = downPaymentPercentage;
	}
	/**
	 * @return the effRate
	 */
	public Double getEffRate() {
		return effRate;
	}
	/**
	 * @param effRate the effRate to set
	 */
	public void setEffRate(Double effRate) {
		this.effRate = effRate;
	}
	/**
	 * @return the flatRate
	 */
	public Double getFlatRate() {
		return flatRate;
	}
	/**
	 * @param flatRate the flatRate to set
	 */
	public void setFlatRate(Double flatRate) {
		this.flatRate = flatRate;
	}
	/**
	 * @return the terms
	 */
	public Integer getTerms() {
		return terms;
	}
	/**
	 * @param terms the terms to set
	 */
	public void setTerms(Integer terms) {
		this.terms = terms;
	}
	/**
	 * @return the applicantionDate
	 */
	public Date getApplicantionDate() {
		return applicantionDate;
	}
	/**
	 * @param applicantionDate the applicantionDate to set
	 */
	public void setApplicantionDate(Date applicantionDate) {
		this.applicantionDate = applicantionDate;
	}
	/**
	 * @return the approvalDate
	 */
	public Date getApprovalDate() {
		return approvalDate;
	}
	/**
	 * @param approvalDate the approvalDate to set
	 */
	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}
	/**
	 * @return the activationDate
	 */
	public Date getActivationDate() {
		return activationDate;
	}
	/**
	 * @param activationDate the activationDate to set
	 */
	public void setActivationDate(Date activationDate) {
		this.activationDate = activationDate;
	}
	/**
	 * @return the firstDueDate
	 */
	public Date getFirstDueDate() {
		return firstDueDate;
	}
	/**
	 * @param firstDueDate the firstDueDate to set
	 */
	public void setFirstDueDate(Date firstDueDate) {
		this.firstDueDate = firstDueDate;
	}
	/**
	 * @return the lastDueDate
	 */
	public Date getLastDueDate() {
		return lastDueDate;
	}
	/**
	 * @param lastDueDate the lastDueDate to set
	 */
	public void setLastDueDate(Date lastDueDate) {
		this.lastDueDate = lastDueDate;
	}
	/**
	 * @return the installmentAmount
	 */
	public Double getInstallmentAmount() {
		return installmentAmount;
	}
	/**
	 * @param installmentAmount the installmentAmount to set
	 */
	public void setInstallmentAmount(Double installmentAmount) {
		this.installmentAmount = installmentAmount;
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
	 * @return the totalUE
	 */
	public Double getTotalUE() {
		return totalUE;
	}
	/**
	 * @param totalUE the totalUE to set
	 */
	public void setTotalUE(Double totalUE) {
		this.totalUE = totalUE;
	}
	/**
	 * @return the totalVAT
	 */
	public Double getTotalVAT() {
		return totalVAT;
	}
	/**
	 * @param totalVAT the totalVAT to set
	 */
	public void setTotalVAT(Double totalVAT) {
		this.totalVAT = totalVAT;
	}
	/**
	 * @return the prepaidInstallment
	 */
	public Double getPrepaidInstallment() {
		return prepaidInstallment;
	}
	/**
	 * @param prepaidInstallment the prepaidInstallment to set
	 */
	public void setPrepaidInstallment(Double prepaidInstallment) {
		this.prepaidInstallment = prepaidInstallment;
	}
	/**
	 * @return the numberPrepaidTerm
	 */
	public Integer getNumberPrepaidTerm() {
		return numberPrepaidTerm;
	}
	/**
	 * @param numberPrepaidTerm the numberPrepaidTerm to set
	 */
	public void setNumberPrepaidTerm(Integer numberPrepaidTerm) {
		this.numberPrepaidTerm = numberPrepaidTerm;
	}
	/**
	 * @return the serviceFee
	 */
	public Double getServiceFee() {
		return serviceFee;
	}
	/**
	 * @param serviceFee the serviceFee to set
	 */
	public void setServiceFee(Double serviceFee) {
		this.serviceFee = serviceFee;
	}
	/**
	 * @return the insuranceFee
	 */
	public Double getInsuranceFee() {
		return insuranceFee;
	}
	/**
	 * @param insuranceFee the insuranceFee to set
	 */
	public void setInsuranceFee(Double insuranceFee) {
		this.insuranceFee = insuranceFee;
	}
}
