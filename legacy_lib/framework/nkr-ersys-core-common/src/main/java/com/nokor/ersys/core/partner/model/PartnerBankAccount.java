package com.nokor.ersys.core.partner.model;

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

import com.nokor.ersys.core.finance.model.BaseBankAccount;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "tu_partner_bank_account")
public class PartnerBankAccount extends BaseBankAccount {
	/** */
	private static final long serialVersionUID = 1228724969202443516L;

	private Partner partner;
	
	/**
     * 
     * @return
     */
    public static PartnerBankAccount createInstance() {
    	PartnerBankAccount deaBankAcc = EntityFactory.createInstance(PartnerBankAccount.class);
        return deaBankAcc;
    }
    
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "par_ban_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

	/**
	 * @return the partner
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "par_id", nullable = false)
	public Partner getPartner() {
		return partner;
	}

	/**
	 * @param partner the partner to set
	 */
	public void setPartner(Partner partner) {
		this.partner = partner;
	}


	
}
