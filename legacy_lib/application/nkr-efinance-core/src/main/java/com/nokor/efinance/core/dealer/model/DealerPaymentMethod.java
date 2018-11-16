package com.nokor.efinance.core.dealer.model;

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
import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.efinance.core.payment.model.EChargePoint;
import com.nokor.efinance.core.payment.model.EPaymentFlowType;
import com.nokor.efinance.core.payment.model.EPaymentMethod;

/**
 * @author ly.youhort
 *
 */
@Entity
@Table(name = "tu_dealer_payment_method")
public class DealerPaymentMethod extends EntityA implements MDealerPaymentMethod {
	/** */
	private static final long serialVersionUID = 4577291219218599010L;

	private Dealer dealer;
	private String payeeName;
	private EPaymentMethod paymentMethod;
	private EPaymentFlowType type;
	private DealerAccountHolder dealerAccountHolder;
	private DealerBankAccount dealerBankAccount;
	private EChargePoint chargePoint;
	
	/**
     * 
     * @return
     */
    public static DealerPaymentMethod createInstance() {
    	DealerPaymentMethod deaBankAcc = EntityFactory.createInstance(DealerPaymentMethod.class);
        return deaBankAcc;
    }
    
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dea_pay_met_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

	/**
	 * @return the dealer
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dea_id", nullable = false)
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
	 * @return the paymentMethod
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pay_met_id")
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
	 * @return the payeeName
	 */
	@Column(name = "dea_pay_met_payee_name", nullable = true, length = 100)
	public String getPayeeName() {
		return payeeName;
	}

	/**
	 * @param payeeName the payeeName to set
	 */
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}	

	/**
	 * @return the type
	 */
	@Column(name = "pay_flw_typ_id", nullable = true)
    @Convert(converter = EPaymentFlowType.class)
	public EPaymentFlowType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(EPaymentFlowType type) {
		this.type = type;
	}

	/**
	 * @return the dealerAccountHolder
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dea_acc_hol_id")
	public DealerAccountHolder getDealerAccountHolder() {
		return dealerAccountHolder;
	}

	/**
	 * @param dealerAccountHolder the dealerAccountHolder to set
	 */
	public void setDealerAccountHolder(DealerAccountHolder dealerAccountHolder) {
		this.dealerAccountHolder = dealerAccountHolder;
	}

	/**
	 * @return the dealerBankAccount
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dea_ban_id")
	public DealerBankAccount getDealerBankAccount() {
		return dealerBankAccount;
	}

	/**
	 * @param dealerBankAccount the dealerBankAccount to set
	 */
	public void setDealerBankAccount(DealerBankAccount dealerBankAccount) {
		this.dealerBankAccount = dealerBankAccount;
	}

	/**
	 * @return the chargePoint
	 */
	@Column(name = "cha_pnt_id", nullable = true)
    @Convert(converter = EChargePoint.class)
	public EChargePoint getChargePoint() {
		return chargePoint;
	}

	/**
	 * @param chargePoint the chargePoint to set
	 */
	public void setChargePoint(EChargePoint chargePoint) {
		this.chargePoint = chargePoint;
	}
}
