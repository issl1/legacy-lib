package com.nokor.efinance.core.payment.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.efinance.core.dealer.model.Dealer;

/**
 * 
 * @author vina.sok
 *
 */
@Entity
@Table(name = "td_bank_deposit_received_dealer")
public class BankDepositReceivedFromDealer extends EntityA {
	
	private static final long serialVersionUID = -6696389166194161054L;
	private Dealer dealer;
	private Date passToDealerPaymentDate;
	private Double amountReceivedFromDealerUSD;
    
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bdp_rec_dea_id", unique = true, nullable = false)
    public Long getId() {
        return id;
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
	 * @return the passToDealerDate
	 */
	@Column(name = "bdp_rec_dea_dt_pass_dealer", nullable = true)
	public Date getPassToDealerPaymentDate() {
		return passToDealerPaymentDate;
	}

	/**
	 * @param passToDealerDate the passToDealerDate to set
	 */
	public void setPassToDealerPaymentDate(Date passToDealerPaymentDate) {
		this.passToDealerPaymentDate = passToDealerPaymentDate;
	}

	/**
	 * @return the amountReceivedFromDealerUSD
	 */
	@Column(name = "bdp_rec_dea_am_received_from_dealer_usd", nullable = true)
	public Double getAmountReceivedFromDealerUSD() {
		return amountReceivedFromDealerUSD;
	}

	/**
	 * @param amountReceivedFromDealerUSD the amountReceivedFromDealerUSD to set
	 */
	public void setAmountReceivedFromDealerUSD(Double amountReceivedFromDealerUSD) {
		this.amountReceivedFromDealerUSD = amountReceivedFromDealerUSD;
	}

}
