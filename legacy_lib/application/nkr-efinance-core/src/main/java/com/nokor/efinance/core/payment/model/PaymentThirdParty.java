package com.nokor.efinance.core.payment.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;

import com.nokor.common.app.workflow.model.EntityWkf;
import com.nokor.efinance.core.actor.model.ThirdParty;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.share.contract.PaymentType;

/**
 * @author youhort.ly
 */
@Entity
@Table(name = "td_payment_third_party")
public class PaymentThirdParty extends EntityWkf {

	/**
	 */
	private static final long serialVersionUID = -1109845571686913544L;

	private ThirdParty thirdParty;
	private PaymentType paymentType;
	private String callBackUrl;
	
    private Cashflow cashflow;
    
    /**
     * 
     * @return
     */
    public static PaymentThirdParty createInstance() {
    	PaymentThirdParty entity = EntityFactory.createInstance(PaymentThirdParty.class);
        return entity;
    }
    
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pay_thi_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

	/**
	 * @return the cashflow
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cfw_id")
	public Cashflow getCashflow() {
		return cashflow;
	}
	
	/**
	 * @return the thirdParty
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thi_id")
	public ThirdParty getThirdParty() {
		return thirdParty;
	}

	/**
	 * @param thirdParty the thirdParty to set
	 */
	public void setThirdParty(ThirdParty thirdParty) {
		this.thirdParty = thirdParty;
	}

	/**
	 * @param cashflow the cashflow to set
	 */
	public void setCashflow(Cashflow cashflow) {
		this.cashflow = cashflow;
	}

	/**
	 * @return the paymentType
	 */
	@Column(name = "pay_typ_id", nullable = true, length = 20)
	@Enumerated(EnumType.STRING)
	public PaymentType getPaymentType() {
		return paymentType;
	}

	/**
	 * @param paymentType the paymentType to set
	 */
	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	} 
	/**
	 * @return the callBackUrl
	 */
	@Column(name = "pay_thi_call_back_url", nullable = true, length = 255)
	public String getCallBackUrl() {
		return callBackUrl;
	}

	/**
	 * @param callBackUrl the callBackUrl to set
	 */
	public void setCallBackUrl(String callBackUrl) {
		this.callBackUrl = callBackUrl;
	}
}
