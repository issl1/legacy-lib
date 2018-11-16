package com.nokor.efinance.core.payment.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.efinance.core.dealer.model.Dealer;

/**
 * 
 * @author vina.sok
 *
 */
@Entity
@Table(name = "td_bank_deposit")
public class BankDeposit extends EntityA {
	
	private static final long serialVersionUID = -6696389166194161054L;
	
	private Dealer dealer;
	private Date requestDate;
	private Date requestDepositDate;
	private Double amountReceivedFromDealerUSD;
	
	private List<Payment> payments;
    
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bdp_id", unique = true, nullable = false)
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
	 * @return the requestDate
	 */
	@Column(name = "bdp_dt_request", nullable = true)
	public Date getRequestDate() {
		return requestDate;
	}

	/**
	 * @param requestDate the requestDate to set
	 */
	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	/**
	 * @return the requestDepositDate
	 */
	@Column(name = "bdp_dt_request_deposit", nullable = true)
	public Date getRequestDepositDate() {
		return requestDepositDate;
	}

	/**
	 * @param requestDepositDate the requestDepositDate to set
	 */
	public void setRequestDepositDate(Date requestDepositDate) {
		this.requestDepositDate = requestDepositDate;
	}

	/**
	 * @return the payments
	 */
	@OneToMany(mappedBy="bankDeposit", fetch = FetchType.LAZY)
	public List<Payment> getPayments() {
		return payments;
	}

	/**
	 * @param payments the payments to set
	 */
	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	}

	/**
	 * @return the amountReceivedFromDealerUSD
	 */
	@Column(name = "bdp_am_received_from_dealer_usd", nullable = true)
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
