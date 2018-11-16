package com.nokor.efinance.core.contract.model;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;

import com.nokor.common.app.workflow.model.EntityWkf;
import com.nokor.efinance.core.aftersale.EAfterSaleEventType;
import com.nokor.efinance.core.collection.model.EPaymentChannel;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.workflow.LockSplitWkfStatus;

/**
 * @author ly.youhort
 */
@Entity
@Table(name = "td_lock_split")
public class LockSplit extends EntityWkf implements MLockSplit {

	private static final long serialVersionUID = 8935751397622214350L;
	
	private Contract contract;
	private String reference;
	private Date from;
	private Date to;
	private boolean whiteClearance;
	private boolean sendEmail;
 	private boolean sendSms;
 	private boolean promise;
	private Double totalAmount;
	private Double totalVatAmount;
	private String callBackUrl;
	private EPaymentChannel paymentChannel;
	private Dealer dealer;
	private String comment;
	private List<LockSplitItem> items;
	private String createdBy;
	
	private EAfterSaleEventType afterSaleEventType;
	
	/**
     * 
     * @return
     */
    public static LockSplit createInstance() {
    	LockSplit instance = EntityFactory.createInstance(LockSplit.class);
    	instance.setWkfStatus(LockSplitWkfStatus.LNEW);
        return instance;
    }
	
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loc_spl_id", unique = true, nullable = false)
    public Long getId() {
        return id;
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
	 * @return the reference
	 */
	@Column(name = "loc_spl_va_reference", nullable = true, length = 20)
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
	 * @return the from
	 */
	@Column(name = "loc_spl_dt_from", nullable = true)
	public Date getFrom() {
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(Date from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	@Column(name = "loc_spl_dt_to", nullable = true)
	public Date getTo() {
		return to;
	}

	/**
	 * @param to the to to set
	 */
	public void setTo(Date to) {
		this.to = to;
	}

	/**
	 * @return the whiteClearance
	 */
	@Column(name = "loc_spl_b_white_clearance", nullable = true, columnDefinition = "boolean default false")
	public boolean isWhiteClearance() {
		return whiteClearance;
	}

	/**
	 * @param whiteClearance the whiteClearance to set
	 */
	public void setWhiteClearance(boolean whiteClearance) {
		this.whiteClearance = whiteClearance;
	}

	/**
	 * @return the sendEmail
	 */
	@Column(name = "loc_spl_b_send_email", nullable = true, columnDefinition = "boolean default false")
	public boolean isSendEmail() {
		return sendEmail;
	}

	/**
	 * @param sendEmail the sendEmail to set
	 */
	public void setSendEmail(boolean sendEmail) {
		this.sendEmail = sendEmail;
	}

	/**
	 * @return the sendSms
	 */
	@Column(name = "loc_spl_b_send_sms", nullable = true, columnDefinition = "boolean default false")
	public boolean isSendSms() {
		return sendSms;
	}

	/**
	 * @param sendSms the sendSms to set
	 */
	public void setSendSms(boolean sendSms) {
		this.sendSms = sendSms;
	}

	/**
	 * @return the promise
	 */
	@Column(name = "loc_spl_b_promise", nullable = true, columnDefinition = "boolean default false")
	public boolean isPromise() {
		return promise;
	}

	/**
	 * @param promise the promise to set
	 */
	public void setPromise(boolean promise) {
		this.promise = promise;
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
	 * @return the dealer
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dea_id", nullable = true)
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
	 * @return the comment
	 */
	@Column(name = "loc_spl_va_comment", nullable = true, length = 500)
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the items
	 */
	@OneToMany(mappedBy="lockSplit", fetch = FetchType.LAZY)
	@OrderBy("priority")
	public List<LockSplitItem> getItems() {
		return items;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(List<LockSplitItem> items) {
		this.items = items;
	}

	/**
	 * @return the totalAmount
	 */
	@Column(name = "loc_spl_am_total_amount", nullable = true)
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
	 * @return the totalVatAmount
	 */
	@Column(name = "loc_spl_am_total_vat_amount", nullable = true)
	public Double getTotalVatAmount() {
		return totalVatAmount;
	}

	/**
	 * @param totalVatAmount the totalVatAmount to set
	 */
	public void setTotalVatAmount(Double totalVatAmount) {
		this.totalVatAmount = totalVatAmount;
	}
	
	/**
	 * @return the callBackUrl
	 */
	@Column(name = "loc_spl_va_call_back_url", nullable = true, length = 255)
	public String getCallBackUrl() {
		return callBackUrl;
	}

	/**
	 * @param callBackUrl the callBackUrl to set
	 */
	public void setCallBackUrl(String callBackUrl) {
		this.callBackUrl = callBackUrl;
	}
	
	/**
	 * @return the createdBy
	 */
	@Column(name = "loc_spl_va_created_by", nullable = true, length = 50)	
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the afterSaleEventType
	 */
    @Column(name = "aft_evt_typ_id", nullable = true)
	@Convert(converter = EAfterSaleEventType.class)
	public EAfterSaleEventType getAfterSaleEventType() {
		return afterSaleEventType;
	}

	/**
	 * @param afterSaleEventType the afterSaleEventType to set
	 */
	public void setAfterSaleEventType(EAfterSaleEventType afterSaleEventType) {
		this.afterSaleEventType = afterSaleEventType;
	}
}
