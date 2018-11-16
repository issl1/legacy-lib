package com.nokor.efinance.core.payment.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;

import com.nokor.common.app.workflow.model.EntityWkf;
import com.nokor.efinance.core.collection.model.EPaymentChannel;

/**
 * The installment receipt
 * 
 * @author bunlong.taing
 * 
 */
@Entity
@Table(name = "td_payment_file_item")
public class PaymentFileItem extends EntityWkf implements MPaymentFileItem {

	/**
	 */
	private static final long serialVersionUID = -683361910651290167L;
	
	private PaymentFile paymentFile;
	
	private String sequence;
	private String bankCode;
	private String companyAccount;
	private Date paymentDate;
	private String customerId;
	private String customerName;
	private String customerRef1;
	private String customerRef2;
	private String customerRef3;
	private String dealerNo;
	private String branchNo;
	private String tellerNo;
	private String transactionKind;
	private String transactionCode;
	private String chequeNo;
	private Double amount;
	private String chequeBankCode;
	private String chequeBranchCode;
	private String filter1;
	private String chequeNoNew;
	private String filter2;
	private String chequeNoNew1;
	private String fixCode;
	private String postCode;
	private String receiveNo;
	private String payeeFeeSameZone;
	private String payeeFeeDiffZone;
	private Integer bankId;
	private String owner;
	private Integer bankBranchId;
	private EPaymentChannel paymentChannel;
	private EPaymentMethod paymentMethod;
	
	/**
	 * @return
	 */
	public static PaymentFileItem createInstance() {
		PaymentFileItem item = EntityFactory.createInstance(PaymentFileItem.class);
        return item;
    }
		
	/**
	 */
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pay_fil_ite_id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	/**
	 * @return the paymentFile
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pay_fil_id", nullable = true)
	public PaymentFile getPaymentFile() {
		return paymentFile;
	}

	/**
	 * @param paymentFile the paymentFile to set
	 */
	public void setPaymentFile(PaymentFile paymentFile) {
		this.paymentFile = paymentFile;
	}

	/**
	 * @return the sequence
	 */
	@Column(name = "pay_fil_ite_sequence", nullable = true, length = 30)
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
	@Column(name = "pay_fil_ite_bank_code", nullable = true, length = 30)
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
	@Column(name = "pay_fil_ite_company_account", nullable = true, length = 100)
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
	 * @return the paymentDate
	 */
	@Column(name = "pay_fil_ite_dt_payment", nullable = true, length = 10)
	public Date getPaymentDate() {
		return paymentDate;
	}

	/**
	 * @param paymentDate the paymentDate to set
	 */
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	/**
	 * @return the customerName
	 */
	@Column(name = "pay_fil_ite_customer_name", nullable = true, length = 100)
	public String getCustomerName() {
		return customerName;
	}

	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/**
	 * @return the customerRef1
	 */
	@Column(name = "pay_fil_ite_customer_ref_1", nullable = true, length = 100)
	public String getCustomerRef1() {
		return customerRef1;
	}

	/**
	 * @param customerRef1 the customerRef1 to set
	 */
	public void setCustomerRef1(String customerRef1) {
		this.customerRef1 = customerRef1;
	}

	/**
	 * @return the customerRef2
	 */
	@Column(name = "pay_fil_ite_customer_ref_2", nullable = true, length = 100)
	public String getCustomerRef2() {
		return customerRef2;
	}

	/**
	 * @param customerRef2 the customerRef2 to set
	 */
	public void setCustomerRef2(String customerRef2) {
		this.customerRef2 = customerRef2;
	}

	/**
	 * @return the customerRef3
	 */
	@Column(name = "pay_fil_ite_customer_ref_3", nullable = true, length = 100)
	public String getCustomerRef3() {
		return customerRef3;
	}

	/**
	 * @param customerRef3 the customerRef3 to set
	 */
	public void setCustomerRef3(String customerRef3) {
		this.customerRef3 = customerRef3;
	}

	/**
	 * @return the dealerNo
	 */
	@Column(name = "pay_fil_ite_dealer_no", nullable = true, length = 100)
	public String getDealerNo() {
		return dealerNo;
	}

	/**
	 * @param dealerNo the dealerNo to set
	 */
	public void setDealerNo(String dealerNo) {
		this.dealerNo = dealerNo;
	}

	/**
	 * @return the branchNo
	 */
	@Column(name = "pay_fil_ite_branch_no", nullable = true, length = 100)
	public String getBranchNo() {
		return branchNo;
	}

	/**
	 * @param branchNo the branchNo to set
	 */
	public void setBranchNo(String branchNo) {
		this.branchNo = branchNo;
	}

	/**
	 * @return the tellerNo
	 */
	@Column(name = "pay_fil_ite_teller_no", nullable = true, length = 100)
	public String getTellerNo() {
		return tellerNo;
	}

	/**
	 * @param tellerNo the tellerNo to set
	 */
	public void setTellerNo(String tellerNo) {
		this.tellerNo = tellerNo;
	}

	/**
	 * @return the transactionKind
	 */
	@Column(name = "pay_fil_ite_transaction_kind", nullable = true, length = 100)
	public String getTransactionKind() {
		return transactionKind;
	}

	/**
	 * @param transactionKind the transactionKind to set
	 */
	public void setTransactionKind(String transactionKind) {
		this.transactionKind = transactionKind;
	}

	/**
	 * @return the transactionCode
	 */
	@Column(name = "pay_fil_ite_transaction_code", nullable = true, length = 100)
	public String getTransactionCode() {
		return transactionCode;
	}

	/**
	 * @param transactionCode the transactionCode to set
	 */
	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	/**
	 * @return the chequeNo
	 */
	@Column(name = "pay_fil_ite_cheque_no", nullable = true, length = 100)
	public String getChequeNo() {
		return chequeNo;
	}

	/**
	 * @param chequeNo the chequeNo to set
	 */
	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}

	/**
	 * @return the amount
	 */
	@Column(name = "pay_fil_ite_amount", nullable = true, length = 100)
	public Double getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Double amount) {
		this.amount = amount;
	}

	/**
	 * @return the chequeBankCode
	 */
	@Column(name = "pay_fil_ite_cheque_bank_code", nullable = true, length = 100)
	public String getChequeBankCode() {
		return chequeBankCode;
	}

	/**
	 * @param chequeBankCode the chequeBankCode to set
	 */
	public void setChequeBankCode(String chequeBankCode) {
		this.chequeBankCode = chequeBankCode;
	}

	/**
	 * @return the chequeBranchCode
	 */
	@Column(name = "pay_fil_ite_cheque_branch_code", nullable = true, length = 100)
	public String getChequeBranchCode() {
		return chequeBranchCode;
	}

	/**
	 * @param chequeBranchCode the chequeBranchCode to set
	 */
	public void setChequeBranchCode(String chequeBranchCode) {
		this.chequeBranchCode = chequeBranchCode;
	}

	/**
	 * @return the filter1
	 */
	@Column(name = "pay_fil_ite_filter_1", nullable = true, length = 100)
	public String getFilter1() {
		return filter1;
	}

	/**
	 * @param filter1 the filter1 to set
	 */
	public void setFilter1(String filter1) {
		this.filter1 = filter1;
	}

	/**
	 * @return the chequeNoNew
	 */
	@Column(name = "pay_fil_ite_cheque_no_new", nullable = true, length = 100)
	public String getChequeNoNew() {
		return chequeNoNew;
	}

	/**
	 * @param chequeNoNew the chequeNoNew to set
	 */
	public void setChequeNoNew(String chequeNoNew) {
		this.chequeNoNew = chequeNoNew;
	}

	/**
	 * @return the filter2
	 */
	@Column(name = "pay_fil_ite_filter_2", nullable = true, length = 100)
	public String getFilter2() {
		return filter2;
	}

	/**
	 * @param filter2 the filter2 to set
	 */
	public void setFilter2(String filter2) {
		this.filter2 = filter2;
	}

	/**
	 * @return the chequeNoNew1
	 */
	@Column(name = "pay_fil_ite_cheque_no_new_1", nullable = true, length = 100)
	public String getChequeNoNew1() {
		return chequeNoNew1;
	}

	/**
	 * @param chequeNoNew1 the chequeNoNew1 to set
	 */
	public void setChequeNoNew1(String chequeNoNew1) {
		this.chequeNoNew1 = chequeNoNew1;
	}

	/**
	 * @return the fixCode
	 */
	@Column(name = "pay_fil_ite_fix_code", nullable = true, length = 100)
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
	 * @return the customerId
	 */
	@Column(name = "pay_fil_ite_customer_id", nullable = true, length = 100)
	public String getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	/**
	 * @return the postCode
	 */
	@Column(name = "pay_fil_ite_post_code", nullable = true, length = 100)
	public String getPostCode() {
		return postCode;
	}

	/**
	 * @param postCode the postCode to set
	 */
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	/**
	 * @return the receiveNo
	 */
	@Column(name = "pay_fil_ite_receive_no", nullable = true, length = 100)
	public String getReceiveNo() {
		return receiveNo;
	}

	/**
	 * @param receiveNo the receiveNo to set
	 */
	public void setReceiveNo(String receiveNo) {
		this.receiveNo = receiveNo;
	}

	/**
	 * @return the payeeFeeSameZone
	 */
	@Column(name = "pay_fil_ite_payee_fee_same_zone", nullable = true, length = 100)
	public String getPayeeFeeSameZone() {
		return payeeFeeSameZone;
	}

	/**
	 * @param payeeFeeSameZone the payeeFeeSameZone to set
	 */
	public void setPayeeFeeSameZone(String payeeFeeSameZone) {
		this.payeeFeeSameZone = payeeFeeSameZone;
	}

	/**
	 * @return the payeeFeeDiffZone
	 */
	@Column(name = "pay_fil_ite_payee_fee_diff_zone", nullable = true, length = 100)
	public String getPayeeFeeDiffZone() {
		return payeeFeeDiffZone;
	}

	/**
	 * @param payeeFeeDiffZone the payeeFeeDiffZone to set
	 */
	public void setPayeeFeeDiffZone(String payeeFeeDiffZone) {
		this.payeeFeeDiffZone = payeeFeeDiffZone;
	}
	
	/**
	 * @return the owner
	 */
	@Column(name = "pay_fil_ite_owner", nullable = true, length = 100)
	public String getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * @return the bankId
	 */
	@Column(name = "pay_fil_ite_bank_id", nullable = true, length = 100)
	public Integer getBankId() {
		return bankId;
	}

	/**
	 * @param bankId the bankId to set
	 */
	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}

	/**
	 * @return the bankBranchId
	 */
	@Column(name = "pay_fil_ite_bank_branch_id", nullable = true, length = 100)
	public Integer getBankBranchId() {
		return bankBranchId;
	}

	/**
	 * @param bankBranchId the bankBranchId to set
	 */
	public void setBankBranchId(Integer bankBranchId) {
		this.bankBranchId = bankBranchId;
	}

	/**
	 * @return the paymentChannel
	 */
	@Column(name = "pay_cha_id", nullable = true)
    @Convert(converter = EPaymentChannel.class)
	public EPaymentChannel getPaymentChannel() {
		return paymentChannel;
	}

	/**
	 * @param paymentChannel the paymentChannel to set
	 */
	public void setPaymentChannel(EPaymentChannel paymentChannel) {
		this.paymentChannel = paymentChannel;
	}
	
	/**
	 * @return the paymentMethod
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pay_met_id", nullable = true)
	public EPaymentMethod getPaymentMethod() {
		return paymentMethod;
	}
	
	/**
	 * @param paymentMethod the paymentMethod to set
	 */
	public void setPaymentMethod(EPaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	
}
