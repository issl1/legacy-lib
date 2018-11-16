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

import com.nokor.ersys.core.finance.model.BaseBankAccount;

/**
 * @author prasnar
 *
 */
@Entity
@Table(name="tu_org_bank_account")
public class OrgBankAccount extends BaseBankAccount implements MOrgBankAccount {
	/** */
	private static final long serialVersionUID = -8617498096447536925L;

	private Organization organization;
	private Long bankAccount;
	 
	/**
     * 
     * @return
     */
    public static OrgBankAccount createInstance() {
        OrgBankAccount orgBankAcc = EntityFactory.createInstance(OrgBankAccount.class);
        return orgBankAcc;
    }

    /**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "org_ban_id", unique = true, nullable = false)
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
