package com.nokor.ersys.core.hr.model.organization;

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
 * @author poevminea.sann
 */
@Entity
@Table(name = "tu_org_account_holder")
public class OrgAccountHolder extends EntityA implements MOrgAccountHolder {
	
	/** */
	private static final long serialVersionUID = 329903628163675743L;
	
	private Organization organization;
	private Long accountHolder;
	
	/**
     * 
     * @return
     */
    public static OrgAccountHolder createInstance() {
    	OrgAccountHolder orgBankAcc = EntityFactory.createInstance(OrgAccountHolder.class);
        return orgBankAcc;
    }
    
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "org_acc_hol_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

	/**
	 * @return the organization
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id", nullable = false)
	public Organization getOrganization() {
		return organization;
	}

	/**
	 * @param organization the organization to set
	 */
	public void setOrganization(Organization organization) {
		this.organization = organization;
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
