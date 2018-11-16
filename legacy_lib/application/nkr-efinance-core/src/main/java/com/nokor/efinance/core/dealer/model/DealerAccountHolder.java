package com.nokor.efinance.core.dealer.model;

import javax.persistence.Column;
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

/**
 * 
 * @author uhout.cheng
 */
@Entity
@Table(name = "tu_dealer_account_holder")
public class DealerAccountHolder extends EntityA implements MDealerAccountHolder {
	
	/** */
	private static final long serialVersionUID = 329903628163675743L;
	
	private Dealer dealer;
	private Long accountHolder;
	
	/**
     * 
     * @return
     */
    public static DealerAccountHolder createInstance() {
    	DealerAccountHolder deaBankAcc = EntityFactory.createInstance(DealerAccountHolder.class);
        return deaBankAcc;
    }
    
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dea_acc_hol_id", unique = true, nullable = false)
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
	 * @return the accountHolder
	 */
	@Column(name = "acc_hol_id", nullable = true)
	public Long getAccountHolder() {
		return accountHolder;
	}

	/**
	 * @param accountHolder the accountHolder to set
	 */
	public void setAccountHolder(Long accountHolder) {
		this.accountHolder = accountHolder;
	}
	
}
