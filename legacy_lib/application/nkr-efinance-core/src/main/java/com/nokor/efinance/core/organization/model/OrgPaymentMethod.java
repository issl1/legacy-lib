package com.nokor.efinance.core.organization.model;

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

import com.nokor.efinance.core.payment.model.EPaymentFlowType;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.ersys.core.hr.model.organization.OrgAccountHolder;
import com.nokor.ersys.core.hr.model.organization.OrgBankAccount;
import com.nokor.ersys.core.hr.model.organization.Organization;

/**
 * @author ly.youhort
 *
 */
@Entity
@Table(name = "tu_org_payment_method")
public class OrgPaymentMethod extends EntityA implements MOrgPaymentMethod {
	
	/** */
	private static final long serialVersionUID = 5974641053385814926L;
	
	private Organization organization;
	private EPaymentMethod paymentMethod;
	private EPaymentFlowType type;
	private OrgAccountHolder orgAccountHolder;
	private OrgBankAccount orgBankAccount;
	
	/**
     * 
     * @return
     */
    public static OrgPaymentMethod createInstance() {
    	OrgPaymentMethod deaBankAcc = EntityFactory.createInstance(OrgPaymentMethod.class);
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
	 * @return the orgAccountHolder
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_acc_hol_id")
	public OrgAccountHolder getOrgAccountHolder() {
		return orgAccountHolder;
	}

	/**
	 * @param orgAccountHolder the orgAccountHolder to set
	 */
	public void setOrgAccountHolder(OrgAccountHolder orgAccountHolder) {
		this.orgAccountHolder = orgAccountHolder;
	}

	/**
	 * @return the orgBankAccount
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_ban_id")
	public OrgBankAccount getOrgBankAccount() {
		return orgBankAccount;
	}

	/**
	 * @param orgBankAccount the orgBankAccount to set
	 */
	public void setOrgBankAccount(OrgBankAccount orgBankAccount) {
		this.orgBankAccount = orgBankAccount;
	}

}
