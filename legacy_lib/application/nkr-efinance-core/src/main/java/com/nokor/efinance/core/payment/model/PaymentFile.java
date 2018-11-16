package com.nokor.efinance.core.payment.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;

import com.nokor.common.app.workflow.model.EntityWkf;

/**
 * The installment receipt
 * 
 * @author bunlong.taing
 * 
 */
@Entity
@Table(name = "td_payment_file")
public class PaymentFile extends EntityWkf implements MPaymentFile {

	/**
	 */
	private static final long serialVersionUID = -683361910651290167L;
	
	private EPaymentFileFormat format;
	
	// Header
	private String sequence;
	private String bankCode;
	private String companyAccount;
	private String companyAccountOptional;
	private String companyName;
	private Date effectiveDate;
	private String serviceCode;
	private String filter;
	
	// Total
	private String lastSequence;
	private String footerBankCode;
	private String footerCompanyAccount;
	private Double totalDebitAmount;
	private Integer totalDebitTransaction;
	private Double totalCreditAmount;
	private Integer totalCreditTransaction;
	private Integer totalTransaction;
	private String footerFilter;
	private String fixCode;
	private Double totalAmount;
	
	/**
	 * @return
	 */
	public static PaymentFile createInstance() {
		PaymentFile file = EntityFactory.createInstance(PaymentFile.class);
        return file;
    }
	
	/**
	 */
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pay_fil_id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	/**
	 * @return the format
	 */
	@Column(name = "pay_fil_for_id", nullable = false)
    @Convert(converter = EPaymentFileFormat.class)
	public EPaymentFileFormat getFormat() {
		return format;
	}

	/**
	 * @param format the format to set
	 */
	public void setFormat(EPaymentFileFormat format) {
		this.format = format;
	}

	/**
	 * @return the sequence
	 */
	@Column(name = "pay_fil_sequence", nullable = true, length = 30)
	public String getSequence() {
		return sequence;
	}

	/**
	 * @param sequence the sequence to set
	 */
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	/**
	 * @return the bankCode
	 */
	@Column(name = "pay_fil_bank_code", nullable = true, length = 30)
	public String getBankCode() {
		return bankCode;
	}

	/**
	 * @param bankCode the bankCode to set
	 */
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	/**
	 * @return the companyAccount
	 */
	@Column(name = "pay_fil_company_bank_account", nullable = true, length = 100)
	public String getCompanyAccount() {
		return companyAccount;
	}

	/**
	 * @param companyAccount the companyAccount to set
	 */
	public void setCompanyAccount(String companyAccount) {
		this.companyAccount = companyAccount;
	}

	/**
	 * @return the companyName
	 */
	@Column(name = "pay_fil_company_name", nullable = true, length = 100)
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * @return the effectiveDate
	 */
	@Column(name = "pay_fil_dt_effective", nullable = true)
	public Date getEffectiveDate() {
		return effectiveDate;
	}

	/**
	 * @param effectiveDate the effectiveDate to set
	 */
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	/**
	 * @return the serviceCode
	 */
	@Column(name = "pay_fil_service_code", nullable = true, length = 100)
	public String getServiceCode() {
		return serviceCode;
	}

	/**
	 * @param serviceCode the serviceCode to set
	 */
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	/**
	 * @return the filter
	 */
	@Column(name = "pay_fil_filter", nullable = true, length = 200)
	public String getFilter() {
		return filter;
	}

	/**
	 * @param filter the filter to set
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}

	/**
	 * @return the lastSequence
	 */
	@Column(name = "pay_fil_last_sequence", nullable = true, length = 30)
	public String getLastSequence() {
		return lastSequence;
	}

	/**
	 * @param lastSequence the lastSequence to set
	 */
	public void setLastSequence(String lastSequence) {
		this.lastSequence = lastSequence;
	}

	/**
	 * @return the footerBankCode
	 */
	@Column(name = "pay_fil_ft_bank_code", nullable = true, length = 30)
	public String getFooterBankCode() {
		return footerBankCode;
	}

	/**
	 * @param footerBankCode the footerBankCode to set
	 */
	public void setFooterBankCode(String footerBankCode) {
		this.footerBankCode = footerBankCode;
	}

	/**
	 * @return the footerCompanyAccount
	 */
	@Column(name = "pay_fil_ft_company_account", nullable = true, length = 100)
	public String getFooterCompanyAccount() {
		return footerCompanyAccount;
	}

	/**
	 * @param footerCompanyAccount the footerCompanyAccount to set
	 */
	public void setFooterCompanyAccount(String footerCompanyAccount) {
		this.footerCompanyAccount = footerCompanyAccount;
	}

	/**
	 * @return the totalDebitAmount
	 */
	@Column(name = "pay_fil_total_debit_amount", nullable = true)
	public Double getTotalDebitAmount() {
		return totalDebitAmount;
	}

	/**
	 * @param totalDebitAmount the totalDebitAmount to set
	 */
	public void setTotalDebitAmount(Double totalDebitAmount) {
		this.totalDebitAmount = totalDebitAmount;
	}

	/**
	 * @return the totalDebitTransaction
	 */
	@Column(name = "pay_fil_total_debit_transaction", nullable = true)
	public Integer getTotalDebitTransaction() {
		return totalDebitTransaction;
	}

	/**
	 * @param totalDebitTransaction the totalDebitTransaction to set
	 */
	public void setTotalDebitTransaction(Integer totalDebitTransaction) {
		this.totalDebitTransaction = totalDebitTransaction;
	}

	/**
	 * @return the totalCreditAmount
	 */
	@Column(name = "pay_fil_total_credit_amount", nullable = true)
	public Double getTotalCreditAmount() {
		return totalCreditAmount;
	}

	/**
	 * @param totalCreditAmount the totalCreditAmount to set
	 */
	public void setTotalCreditAmount(Double totalCreditAmount) {
		this.totalCreditAmount = totalCreditAmount;
	}

	/**
	 * @return the totalCreditTransaction
	 */
	@Column(name = "pay_fil_total_credit_transaction", nullable = true)
	public Integer getTotalCreditTransaction() {
		return totalCreditTransaction;
	}

	/**
	 * @param totalCreditTransaction the totalCreditTransaction to set
	 */
	public void setTotalCreditTransaction(Integer totalCreditTransaction) {
		this.totalCreditTransaction = totalCreditTransaction;
	}

	/**
	 * @return the footerFilter
	 */
	@Column(name = "pay_fil_ft_filter", nullable = true, length = 200)
	public String getFooterFilter() {
		return footerFilter;
	}

	/**
	 * @param footerFilter the footerFilter to set
	 */
	public void setFooterFilter(String footerFilter) {
		this.footerFilter = footerFilter;
	}

	/**
	 * @return the fixCode
	 */
	@Column(name = "pay_fil_fix_code", nullable = true, length = 100)
	public String getFixCode() {
		return fixCode;
	}

	/**
	 * @param fixCode the fixCode to set
	 */
	public void setFixCode(String fixCode) {
		this.fixCode = fixCode;
	}

	/**
	 * @return the totalAmount
	 */
	@Column(name = "pay_fil_total_amount", nullable = true)
	public Double getTotalAmount() {
		return totalAmount;
	}

	/**
	 * @param totalAmount the totalAmount to set
	 */
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	/**
	 * @return the totalTransaction
	 */
	@Column(name = "pay_fil_total_transaction", nullable = true)
	public Integer getTotalTransaction() {
		return totalTransaction;
	}

	/**
	 * @param totalTransaction the totalTransaction to set
	 */
	public void setTotalTransaction(Integer totalTransaction) {
		this.totalTransaction = totalTransaction;
	}

	/**
	 * @return the companyAccountOptional
	 */
	@Column(name = "pay_fil_company_account_optional", nullable = true, length = 100)
	public String getCompanyAccountOptional() {
		return companyAccountOptional;
	}

	/**
	 * @param companyAccountOptional the companyAccountOptional to set
	 */
	public void setCompanyAccountOptional(String companyAccountOptional) {
		this.companyAccountOptional = companyAccountOptional;
	}	
	
}
