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
 * @author ly.youhort
 *
 */
@Entity
@Table(name = "tu_dealer_bank_account")
public class DealerBankAccount extends EntityA implements MDealerBankAccount {
	/** */
	private static final long serialVersionUID = 4577291219218599010L;

	private Dealer dealer;
	private Long bankAccount;
	
	/**
     * 
     * @return
     */
    public static DealerBankAccount createInstance() {
    	DealerBankAccount deaBankAcc = EntityFactory.createInstance(DealerBankAccount.class);
        return deaBankAcc;
    }
    
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dea_ban_id", unique = true, nullable = false)
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
	 * @return the bankAccount
	 */
	@Column(name = "ban_acc_id", nullable = true)
	public Long getBankAccount() {
		return bankAccount;
	}

	/**
	 * @param bankAccount the bankAccount to set
	 */
	public void setBankAccount(Long bankAccount) {
		this.bankAccount = bankAccount;
	}
}
