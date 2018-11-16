package com.nokor.ersys.finance.billing.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;

import com.nokor.common.app.workflow.model.EntityWkf;
import com.nokor.ersys.core.finance.model.BankAccount;
import com.nokor.ersys.core.finance.model.eref.ECurrency;
import com.nokor.ersys.core.hr.model.organization.Employee;

/**
 * 
 * Payment class
 * @author kong.phirun
 *
 */
@Entity
@Table(name = "td_payment")
public class Payment extends EntityWkf {
	/** */
	private static final long serialVersionUID = 3907913191342451018L;

	private String reference;
	private Double paidAmount;
	private ECurrency currency;
	private Date paidDate;
	private Date receiptDate;	// ??
	private Date entryDate;		// ??

	private EPaymentMethod paymentMethod;
	private BankAccount payerBankAccount;
	private String payerChequeNumber;
	private BankAccount recipientBankAccount;

	private Employee proceedBy;

	private List<Bill> invoices; 

	/**
     * 
     */
    public static Payment createInstance(Bill invoice) {
    	Payment payment = EntityFactory.createInstance(Payment.class);
//    	payment.setPaymentMode(ReferenceDataHelper.getPaymentModeCASH());
//		payment.setCurrency(ReferenceDataHelper.getCurrencyEURO());
//    	payment.setInvoice(invoice);
//    	payment.setEntryDate(new Date());
    	return payment;
    }
	
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pay_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

	/**
	 * @return the reference
	 */
    @Column(name = "pay_rec_reference", nullable = false)
	public String getReference() {
		return reference;
	}

	/**
	 * @param reference the reference to set
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * @return the currency
	 */
    @Column(name = "cur_id", nullable = false)
    @Convert(converter = ECurrency.class)
	public ECurrency getCurrency() {
		return currency;
	}

	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(ECurrency currency) {
		this.currency = currency;
	}


	/**
	 * @return the paidAmount
	 */
    @Column(name = "pay_paid_amount", nullable = false)
	public Double getPaidAmount() {
		return paidAmount;
	}

	/**
	 * @param paidAmount the paidAmount to set
	 */
	public void setPaidAmount(Double paidAmount) {
		this.paidAmount = paidAmount;
	}
	
	/**
	 * @return the paidDate
	 */
    @Column(name = "pay_pai_date", nullable = false)
	public Date getPaidDate() {
		return paidDate;
	}

	/**
	 * @param paidDate the paidDate to set
	 */
	public void setPaidDate(Date paidDate) {
		this.paidDate = paidDate;
	}

	/**
	 * @return the receiptDate
	 */
    @Column(name = "pay_rec_date", nullable = false)
	public Date getReceiptDate() {
		return receiptDate;
	}

	/**
	 * @param receiptDate the receiptDate to set
	 */
	public void setReceiptDate(Date receiptDate) {
		this.receiptDate = receiptDate;
	}

	/**
	 * @return the entryDate
	 */
    @Column(name = "pay_ent_date", nullable = false)
	public Date getEntryDate() {
		return entryDate;
	}

	/**
	 * @param entryDate the entryDate to set
	 */
	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	/**
	 * @return the paymentMethod
	 */
    @Column(name = "pay_met_id", nullable = true)
    @Convert(converter = EPaymentMethod.class)
	public EPaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	/**
	 * @param paymentMethod the paymentMethod to set
	 */
	public void setPaymentMethod(EPaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	/**
	 * @return the payerBankAccount
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payer_ban_acc_id", nullable = true)
	public BankAccount getPayerBankAccount() {
		return payerBankAccount;
	}

	/**
	 * @param payerBankAccount the payerBankAccount to set
	 */
	public void setPayerBankAccount(BankAccount payerBankAccount) {
		this.payerBankAccount = payerBankAccount;
	}

	/**
	 * @return the payerChequeNumber
	 */
    @Column(name = "pay_cheque_number", nullable = true)
	public String getPayerChequeNumber() {
		return payerChequeNumber;
	}

	/**
	 * @param payerChequeNumber the payerChequeNumber to set
	 */
	public void setPayerChequeNumber(String payerChequeNumber) {
		this.payerChequeNumber = payerChequeNumber;
	}

	/**
	 * @return the recipientBankAccount
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_ban_acc_id", nullable = true)
	public BankAccount getRecipientBankAccount() {
		return recipientBankAccount;
	}

	/**
	 * @param recipientBankAccount the recipientBankAccount to set
	 */
	public void setRecipientBankAccount(BankAccount recipientBankAccount) {
		this.recipientBankAccount = recipientBankAccount;
	}

	/**
	 * @return the proceedBy
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proceed_by_emp_id", nullable = false)
	public Employee getProceedBy() {
		return proceedBy;
	}

	/**
	 * @param proceedBy the proceedBy to set
	 */
	public void setProceedBy(Employee proceedBy) {
		this.proceedBy = proceedBy;
	}

	/**
	 * @return the invoices
	 */
	@ManyToMany(mappedBy = "payments")
	public List<Bill> getInvoices() {
		return invoices;
	}

	/**
	 * @param invoices the invoices to set
	 */
	public void setInvoices(List<Bill> invoices) {
		this.invoices = invoices;
	}

}
