package com.nokor.efinance.core.contract.model.cashflow;

import java.util.Date;

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
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.efinance.core.collection.model.ELockSplitType;
import com.nokor.efinance.core.collection.model.EPaymentChannel;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.LockSplit;
import com.nokor.efinance.core.financial.model.EProductLineType;
import com.nokor.efinance.core.financial.model.FinService;
import com.nokor.efinance.core.financial.model.ProductLine;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.ersys.finance.accounting.model.JournalEvent;

/**
 * @author ly.youhort
 *
 */
@Entity
@Table(name = "td_cashflow", indexes = {
		@Index(name = "idx_cfw_con_id", columnList = "con_id"),
		@Index(name = "idx_cfw_pay_id", columnList = "pay_id"),
		@Index(name = "idx_cfw_nu_num_installment", columnList = "cfw_nu_num_installment"),
		@Index(name = "idx_cfw_dt_installment", columnList = "cfw_dt_installment"),
		@Index(name = "idx_cfw_bl_paid", columnList = "cfw_bl_paid"),
		@Index(name = "idx_cfw_bl_cancel", columnList = "cfw_bl_cancel")
})
public class Cashflow extends EntityA implements MCashflow {
	
	private static final long serialVersionUID = -3407656976106918261L;

	public static final String INSFEE = "INSFEE";
	public static final String SERFEE = "SERFEE";
	public static final String WINFEE = "WING";
	public static final String PAYGO = "PAYGO";
	
	private ECashflowType cashflowType;
	private ETreasuryType treasuryType;
	
	private EProductLineType productLineType;
	private ProductLine productLine;
	private EPaymentChannel paymentChannel;
	private ELockSplitType lockSplitType;
	private JournalEvent journalEvent; 
	
	private Contract contract;
	private EPaymentMethod paymentMethod;
	private Payment payment;
	private Integer numInstallment;
	
	private Double tiInstallmentAmount;
	private Double teInstallmentAmount;
	private Double vatInstallmentAmount;
	private double vatValue;
	
	private Date installmentDate;	
	private Date cancelationDate;
	
	private boolean paid;
	private boolean unpaid;
	private boolean cancel;
	private boolean confirm;
	
	private Date periodStartDate;
	private Date periodEndDate;
	
	private FinService service;
	private ECashflowCode cashflowCode;
	private LockSplit lockSplit;
	private Cashflow origin;
	private boolean applyPenalty = true;
	
	private String reason;
	
	/**
     * @return id.
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cfw_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

	/**
	 * @return the cashflowType
	 */
    @Column(name = "cfw_typ_id", nullable = false)
    @Convert(converter = ECashflowType.class)
	public ECashflowType getCashflowType() {
		return cashflowType;
	}
    
	/**
	 * @param cashflowType the cashflowType to set
	 */
	public void setCashflowType(ECashflowType cashflowType) {
		this.cashflowType = cashflowType;
	}

	/**
	 * @return the treasuryType
	 */
    @Column(name = "tre_typ_id", nullable = false)
    @Convert(converter = ETreasuryType.class)
	public ETreasuryType getTreasuryType() {
		return treasuryType;
	}

	/**
	 * @param treasuryType the treasuryType to set
	 */
	public void setTreasuryType(ETreasuryType treasuryType) {
		this.treasuryType = treasuryType;
	}

	/**
	 * @return the productLineType
	 */
    @Column(name = "pro_lin_typ_id", nullable = true)
    @Convert(converter = EProductLineType.class)
	public EProductLineType getProductLineType() {
		return productLineType;
	}

	/**
	 * @param productLineType the productLineType to set
	 */
	public void setProductLineType(EProductLineType productLineType) {
		this.productLineType = productLineType;
	}
	
	/**
	 * @return the productLine
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pro_lin_id", nullable = true)
	public ProductLine getProductLine() {
		return productLine;
	}

	/**
	 * @param productLine the productLine to set
	 */
	public void setProductLine(ProductLine productLine) {
		this.productLine = productLine;
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
	 * @return the journalEvent
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jou_eve_id")
	public JournalEvent getJournalEvent() {
		return journalEvent;
	}

	/**
	 * @param journalEvent the journalEvent to set
	 */
	public void setJournalEvent(JournalEvent journalEvent) {
		this.journalEvent = journalEvent;
	}

	/**
	 * @return the lockSplitType
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loc_spl_typ_id")
	public ELockSplitType getLockSplitType() {
		return lockSplitType;
	}

	/**
	 * @param lockSplitType the lockSplitType to set
	 */
	public void setLockSplitType(ELockSplitType lockSplitType) {
		this.lockSplitType = lockSplitType;
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

	/**
	 * @return the payment
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pay_id")
	public Payment getPayment() {
		return payment;
	}

	/**
	 * @param payment the payment to set
	 */
	public void setPayment(Payment payment) {
		this.payment = payment;
	}
	
	/**
	 * @return the numInstallment
	 */
	@Column(name = "cfw_nu_num_installment")
	public Integer getNumInstallment() {
		return numInstallment;
	}

	/**
	 * @param numInstallment the numInstallment to set
	 */
	public void setNumInstallment(Integer numInstallment) {
		this.numInstallment = numInstallment;
	}
	
	/**
	 * @return the tiInstallmentAmount
	 */
	@Column(name = "cfw_am_ti_installment")
	public Double getTiInstallmentAmount() {
		return tiInstallmentAmount;
	}

	/**
	 * @param tiInstallmentAmount the tiInstallmentAmount to set
	 */
	public void setTiInstallmentAmount(Double tiInstallmentAmount) {
		this.tiInstallmentAmount = tiInstallmentAmount;
	}

	/**
	 * @return the teInstallmentAmount
	 */
	@Column(name = "cfw_am_te_installment")
	public Double getTeInstallmentAmount() {
		return teInstallmentAmount;
	}

	/**
	 * @param teInstallmentAmount the teInstallmentAmount to set
	 */
	public void setTeInstallmentAmount(Double teInstallmentAmount) {
		this.teInstallmentAmount = teInstallmentAmount;
	}

	/**
	 * @return the vatInstallmentAmount
	 */
	@Column(name = "cfw_am_vat_installment")
	public Double getVatInstallmentAmount() {
		return vatInstallmentAmount;
	}

	/**
	 * @param vatInstallmentAmount the vatInstallmentAmount to set
	 */
	public void setVatInstallmentAmount(Double vatInstallmentAmount) {
		this.vatInstallmentAmount = vatInstallmentAmount;
	}
	
	/**
	 * @return the vatValue
	 */
	@Column(name = "cfw_rt_vat", nullable = true, columnDefinition="double precision default '0'")
	public double getVatValue() {
		return vatValue;
	}

	/**
	 * @param vatValue the vatValue to set
	 */
	public void setVatValue(double vatValue) {
		this.vatValue = vatValue;
	}
	
	/**
	 * @return the installmentDate
	 */
	@Column(name = "cfw_dt_installment")
	public Date getInstallmentDate() {
		return installmentDate;
	}

	/**
	 * @param installmentDate the installmentDate to set
	 */
	public void setInstallmentDate(Date installmentDate) {
		this.installmentDate = installmentDate;
	}

	/**
	 * @return the cancelationDate
	 */
	@Column(name = "cfw_dt_cancellation")
	public Date getCancelationDate() {
		return cancelationDate;
	}

	/**
	 * @param cancelationDate the cancelationDate to set
	 */
	public void setCancelationDate(Date cancelationDate) {
		this.cancelationDate = cancelationDate;
	}

	/**
	 * @return the paid
	 */
	@Column(name = "cfw_bl_paid", columnDefinition = "boolean default false")
	public boolean isPaid() {
		return paid;
	}

	/**
	 * @param paid the paid to set
	 */
	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	/**
	 * @return the unpaid
	 */
	@Column(name = "cfw_bl_unpaid", columnDefinition = "boolean default false")
	public boolean isUnpaid() {
		return unpaid;
	}

	/**
	 * @param unpaid the unpaid to set
	 */
	public void setUnpaid(boolean unpaid) {
		this.unpaid = unpaid;
	}

	/**
	 * @return the cancel
	 */
	@Column(name = "cfw_bl_cancel", columnDefinition = "boolean default false")
	public boolean isCancel() {
		return cancel;
	}

	/**
	 * @param cancel the cancel to set
	 */
	public void setCancel(boolean cancel) {
		this.cancel = cancel;
	}

	/**
	 * @return the confirm
	 */
	@Column(name = "cfw_bl_confirm", columnDefinition = "boolean default false")
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
	 * @return the periodStartDate
	 */
	@Column(name = "cfw_dt_period_start", nullable = true)
	public Date getPeriodStartDate() {
		return periodStartDate;
	}

	/**
	 * @param periodStartDate the periodStartDate to set
	 */
	public void setPeriodStartDate(Date periodStartDate) {
		this.periodStartDate = periodStartDate;
	}

	/**
	 * @return the periodEndDate
	 */
	@Column(name = "cfw_dt_period_end", nullable = true)
	public Date getPeriodEndDate() {
		return periodEndDate;
	}

	/**
	 * @param periodEndDate the periodEndDate to set
	 */
	public void setPeriodEndDate(Date periodEndDate) {
		this.periodEndDate = periodEndDate;
	}

	/**
	 * @return the service
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fin_srv_id")
	public FinService getService() {
		return service;
	}

	/**
	 * @param service the service to set
	 */
	public void setService(FinService service) {
		this.service = service;
	}

	/**
	 * @return the cashflowCode
	 */
    @Column(name = "cfw_cod_id", nullable = true)
    @Convert(converter = ECashflowCode.class)
	public ECashflowCode getCashflowCode() {
		return cashflowCode;
	}

	/**
	 * @param cashflowCode the cashflowCode to set
	 */
	public void setCashflowCode(ECashflowCode cashflowCode) {
		this.cashflowCode = cashflowCode;
	}

	/**
	 * @return the lockSplit
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loc_spl_id")
	public LockSplit getLockSplit() {
		return lockSplit;
	}

	/**
	 * @param lockSplit the lockSplit to set
	 */
	public void setLockSplit(LockSplit lockSplit) {
		this.lockSplit = lockSplit;
	}

	/**
	 * @return the origin
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cfw_id_origin")
	public Cashflow getOrigin() {
		return origin;
	}

	/**
	 * @param origin the origin to set
	 */
	public void setOrigin(Cashflow origin) {
		this.origin = origin;
	}

	/**
	 * @return the applyPenalty
	 */
	@Column(name = "cfw_bl_apply_penalty", nullable = true, columnDefinition = "boolean default true")
	public boolean isApplyPenalty() {
		return applyPenalty;
	}

	/**
	 * @param applyPenalty the applyPenalty to set
	 */
	public void setApplyPenalty(boolean applyPenalty) {
		this.applyPenalty = applyPenalty;
	}

	/**
	 * @return the reason
	 */
	@Column(name = "cfw_reason", nullable = true, length = 255)
	public String getReason() {
		return reason;
	}

	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}
	
}
