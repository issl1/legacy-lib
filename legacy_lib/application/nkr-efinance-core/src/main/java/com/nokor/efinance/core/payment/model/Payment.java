package com.nokor.efinance.core.payment.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.nokor.common.app.workflow.model.EntityWkf;
import com.nokor.efinance.core.actor.model.Actor;
import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.frmk.security.model.SecUser;

@Entity
@Table(name = "td_payment", indexes = {
		@Index(name = "idx_pay_va_reference", columnList = "pay_va_reference"),
		@Index(name = "idx_pay_dt_payment", columnList = "pay_dt_payment"),
		@Index(name = "idx_pay_app_id", columnList = "app_id"),
		@Index(name = "idx_pay_sec_usr_id_received", columnList = "sec_usr_id_received"),
		@Index(name = "idx_pay_nu_penalty_days", columnList = "pay_nu_penalty_days"),
		@Index(name = "idx_pay_nu_overdue_days", columnList = "pay_nu_overdue_days"),
		@Index(name = "idx_pay_dea_id", columnList = "dea_id"),
		@Index(name = "idx_pay_con_id", columnList = "con_id")
})
public class Payment extends EntityWkf implements MPayment {
	
	private static final long serialVersionUID = 1359933355023414293L;

	private Applicant applicant;
	private EPaymentMethod paymentMethod;
	private String reference;
	private String externalReference;
	private String descEn;
	private Double tiPaidAmount;
	private Double vatPaidAmount;
	private Double tePaidAmount;
	
	private Date paymentDate;
	private boolean confirm;
	private SecUser receivedUser;
	private EPaymentType paymentType;
	private Integer numPenaltyDays;
	private Integer numOverdueDays;
	
	private Integer printPurchaseOrderVersion;
	private SecUser lastUserprintPurchaseOrder;
	private Date purchaseOrderDate;
	private Date secondPaymentDate;
	private List<Cashflow> cashflows;

	private Contract contract;
	
	private Dealer dealer;
	private Date dealerPaymentDate;
	private Date passToDealerPaymentDate;
	private SecUser dealerPaymentReceivedUser;
	
	private BankDeposit bankDeposit;
	
	private Actor payee;
	private Long payeeBankAccount;
	private Long payeeAccountHolder;
	
	private Actor payer;
	private Long payerBankAccount;
	private Long payerAccountHolder;
	
	
	/**
     * @return id.
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pay_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

	
	/**
	 * @return the paymentMethod
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pay_met_id", nullable = false)
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
	 * @return the reference
	 */
	@Column(name = "pay_va_reference", nullable = true, length = 25)
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
	 * @return the externalReference
	 */
	@Column(name = "pay_va_external_reference", nullable = true, length = 25)
	public String getExternalReference() {
		return externalReference;
	}


	/**
	 * @param externalReference the externalReference to set
	 */
	public void setExternalReference(String externalReference) {
		this.externalReference = externalReference;
	}


	/**
	 * @return the descEn
	 */
	@Column(name = "pay_desc_en", nullable = true, length = 255)
	public String getDescEn() {
		return descEn;
	}


	/**
	 * @param descEn the descEn to set
	 */
	public void setDescEn(String descEn) {
		this.descEn = descEn;
	}

	/**
	 * @return the tiUnpaidAmount
	 */
	@Column(name = "pay_am_ti_paid", nullable = true)
	public Double getTiPaidAmount() {
		return tiPaidAmount;
	}

	/**
	 * @param tiUnpaidAmount the tiUnpaidAmount to set
	 */
	public void setTiPaidAmount(Double tiUnpaidAmount) {
		this.tiPaidAmount = tiUnpaidAmount;
	}
		
	/**
	 * @return the vatPaidAmount
	 */
	@Column(name = "pay_am_vat_paid", nullable = true)
	public Double getVatPaidAmount() {
		return vatPaidAmount;
	}

	/**
	 * @param vatPaidAmount the vatPaidAmount to set
	 */
	public void setVatPaidAmount(Double vatPaidAmount) {
		this.vatPaidAmount = vatPaidAmount;
	}

	/**
	 * @return the tePaidAmount
	 */
	@Column(name = "pay_am_te_paid", nullable = true)
	public Double getTePaidAmount() {
		return tePaidAmount;
	}

	/**
	 * @param tePaidAmount the tePaidAmount to set
	 */
	public void setTePaidAmount(Double tePaidAmount) {
		this.tePaidAmount = tePaidAmount;
	}


	/**
	 * @return the paymentDate
	 */
	@Column(name = "pay_dt_payment", nullable = true)
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
	 * @return the confirm
	 */
	@Column(name = "pay_bl_confirm", nullable = true)
	public boolean isConfirm() {
		return confirm;
	}

	/**
	 * @param confirm the confirm to set
	 */
	public void setConfirm(boolean confirm) {
		this.confirm = confirm;
	}

	/**
	 * @return the applicant
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_id")
	public Applicant getApplicant() {
		return applicant;
	}

	/**
	 * @param applicant the applicant to set
	 */
	public void setApplicant(Applicant applicant) {
		this.applicant = applicant;
	}

	/**
	 * @return the cashflows
	 */
	@OneToMany(mappedBy="payment", fetch = FetchType.LAZY)
	public List<Cashflow> getCashflows() {
		return cashflows;
	}

	/**
	 * @param cashflows the cashflows to set
	 */
	public void setCashflows(List<Cashflow> cashflows) {
		this.cashflows = cashflows;
	}
	


	/**
	 * @return the receivedUser
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sec_usr_id_received")
	public SecUser getReceivedUser() {
		return receivedUser;
	}

	/**
	 * @param receivedUser the receivedUser to set
	 */
	public void setReceivedUser(SecUser receivedUser) {
		this.receivedUser = receivedUser;
	}
	
	/**
	 * @return the paymentType
	 */
    @Column(name = "pay_typ_id", nullable = false)
    @Convert(converter = EPaymentType.class)
	public EPaymentType getPaymentType() {
		return paymentType;
	}

	/**
	 * @param paymentType the paymentType to set
	 */
	public void setPaymentType(EPaymentType paymentType) {
		this.paymentType = paymentType;
	}

	/**
	 * @return the numPenaltyDays
	 */
	@Column(name = "pay_nu_penalty_days", nullable = true)
	public Integer getNumPenaltyDays() {
		return numPenaltyDays;
	}

	/**
	 * @param numPenaltyDays the numPenaltyDays to set
	 */
	public void setNumPenaltyDays(Integer numPenaltyDays) {
		this.numPenaltyDays = numPenaltyDays;
	}	
	
	/**
	 * @return the numOverdueDays
	 */
	@Column(name = "pay_nu_overdue_days", nullable = true)
	public Integer getNumOverdueDays() {
		return numOverdueDays;
	}

	/**
	 * @param numOverdueDays the numOverdueDays to set
	 */
	public void setNumOverdueDays(Integer numOverdueDays) {
		this.numOverdueDays = numOverdueDays;
	}


	/**
	 * @return the printPurchaseOrderVersion
	 */
	@Column(name = "pay_nu_print_purchase_order_version", nullable = true)
	public Integer getPrintPurchaseOrderVersion() {
		return printPurchaseOrderVersion;
	}

	/**
	 * @param printPurchaseOrderVersion the printPurchaseOrderVersion to set
	 */
	public void setPrintPurchaseOrderVersion(Integer printPurchaseOrderVersion) {
		this.printPurchaseOrderVersion = printPurchaseOrderVersion;
	}

	/**
	 * @return the lastUserprintPurchaseOrder
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sec_usr_id_last_print_po")
	public SecUser getLastUserprintPurchaseOrder() {
		return lastUserprintPurchaseOrder;
	}

	/**
	 * @param lastUserprintPurchaseOrder the lastUserprintPurchaseOrder to set
	 */
	public void setLastUserprintPurchaseOrder(SecUser lastUserprintPurchaseOrder) {
		this.lastUserprintPurchaseOrder = lastUserprintPurchaseOrder;
	}
	
	/**
	 * @return the dealer
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dea_id")
	public Dealer getDealer() {
		return dealer;
	}

	/**
	 * @param dealer the dealer to set
	 */
	public void setDealer(Dealer dealer) {
		this.dealer = dealer;
	}

	/**
	 * @return the dealerPaymentDate
	 */
	@Column(name = "pay_dt_dealer_payment", nullable = true)
	public Date getDealerPaymentDate() {
		return dealerPaymentDate;
	}

	/**
	 * @param dealerPaymentDate the dealerPaymentDate to set
	 */
	public void setDealerPaymentDate(Date dealerPaymentDate) {
		this.dealerPaymentDate = dealerPaymentDate;
	}

	/**
	 * @return the dealerPaymentReceivedUser
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sec_usr_id_received_dealer_payment")
	public SecUser getDealerPaymentReceivedUser() {
		return dealerPaymentReceivedUser;
	}

	/**
	 * @param dealerPaymentReceivedUser the dealerPaymentReceivedUser to set
	 */
	public void setDealerPaymentReceivedUser(SecUser dealerPaymentReceivedUser) {
		this.dealerPaymentReceivedUser = dealerPaymentReceivedUser;
	}
	
	/**
	 * @return the purchaseOrderDate
	 */
	@Column(name = "pay_dt_purchase_order", nullable = true)
	public Date getPurchaseOrderDate() {
		return purchaseOrderDate;
	}

	/**
	 * @param purchaseOrderDate the purchaseOrderDate to set
	 */
	public void setPurchaseOrderDate(Date purchaseOrderDate) {
		this.purchaseOrderDate = purchaseOrderDate;
	}

	/**
	 * @return the secondPaymentDate
	 */
	@Column(name = "pay_dt_second_payment", nullable = true)
	public Date getSecondPaymentDate() {
		return secondPaymentDate;
	}
	
	/**
	 * @param secondPaymentDate the secondPaymentDate to set
	 */
	public void setSecondPaymentDate(Date secondPaymentDate) {
		this.secondPaymentDate = secondPaymentDate;
	}	

	/**
	 * @return the bankDeposit
	 */
	/**
	 * @return the dealerPaymentReceivedUser
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bdp_id")
	public BankDeposit getBankDeposit() {
		return bankDeposit;
	}

	/**
	 * @param bankDeposit the bankDeposit to set
	 */
	public void setBankDeposit(BankDeposit bankDeposit) {
		this.bankDeposit = bankDeposit;
	}

	/**
	 * @return the contract
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "con_id")
	public Contract getContract() {
		return contract;
	}
	
	/**
	 * @param contract the contract to set
	 */
	public void setContract(Contract contract) {
		this.contract = contract;
	}	

	/**
	 * @return the passToDealerPaymentDate
	 */
	@Column(name = "pay_dt_pass_to_dealer_payment", nullable = true)
	public Date getPassToDealerPaymentDate() {
		return passToDealerPaymentDate;
	}

	/**
	 * @param passToDealerPaymentDate the passToDealerPaymentDate to set
	 */
	public void setPassToDealerPaymentDate(Date passToDealerPaymentDate) {
		this.passToDealerPaymentDate = passToDealerPaymentDate;
	}

	/**
	 * @return 
	 */
	@Transient
	@Override
	public List<PaymentWkfHistoryItem> getHistories() {
		return (List<PaymentWkfHistoryItem>) getHistories();
	}
	
	/**
	 * @return the payee
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pye_id")
	public Actor getPayee() {
		return payee;
	}

	/**
	 * @param payee the payee to set
	 */
	public void setPayee(Actor payee) {
		this.payee = payee;
	}

	/**
	 * @return the payeeBankAccount
	 */
	@Column(name = "pye_ban_acc_id", nullable = true)
	public Long getPayeeBankAccount() {
		return payeeBankAccount;
	}

	/**
	 * @param payeeBankAccount the payeeBankAccount to set
	 */
	public void setPayeeBankAccount(Long payeeBankAccount) {
		this.payeeBankAccount = payeeBankAccount;
	}

	/**
	 * @return the payeeAccountHolder
	 */
	@Column(name = "pye_acc_hol_id", nullable = true)
	public Long getPayeeAccountHolder() {
		return payeeAccountHolder;
	}

	/**
	 * @param payeeAccountHolder the payeeAccountHolder to set
	 */
	public void setPayeeAccountHolder(Long payeeAccountHolder) {
		this.payeeAccountHolder = payeeAccountHolder;
	}

	/**
	 * @return the payer
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pyr_id")
	public Actor getPayer() {
		return payer;
	}

	/**
	 * @param payer the payer to set
	 */
	public void setPayer(Actor payer) {
		this.payer = payer;
	}

	/**
	 * @return the payerBankAccount
	 */
	@Column(name = "pyr_ban_acc_id", nullable = true)
	public Long getPayerBankAccount() {
		return payerBankAccount;
	}

	/**
	 * @param payerBankAccount the payerBankAccount to set
	 */
	public void setPayerBankAccount(Long payerBankAccount) {
		this.payerBankAccount = payerBankAccount;
	}

	/**
	 * @return the payerAccountHolder
	 */
	@Column(name = "pyr_acc_hol_id", nullable = true)
	public Long getPayerAccountHolder() {
		return payerAccountHolder;
	}

	/**
	 * @param payerAccountHolder the payerAccountHolder to set
	 */
	public void setPayerAccountHolder(Long payerAccountHolder) {
		this.payerAccountHolder = payerAccountHolder;
	}
}
