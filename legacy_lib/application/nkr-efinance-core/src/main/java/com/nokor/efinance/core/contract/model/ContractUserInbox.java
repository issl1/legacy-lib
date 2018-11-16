package com.nokor.efinance.core.contract.model;

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

import com.nokor.frmk.security.model.SecUser;

/**
 * @author ly.youhort
 *
 */
@Entity
@Table(name = "td_contract_user_inbox")
public class ContractUserInbox extends EntityA implements MContractUserInbox {

	private static final long serialVersionUID = 59892883030537102L;

	private SecUser secUser;
	private Contract contract;
	
	/**
	 * @return
	 */
	public static ContractUserInbox createInstance() {
		ContractUserInbox contractUserQueue = EntityFactory.createInstance(ContractUserInbox.class);
        return contractUserQueue;
    }
	
	/**
     * @return the id
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "con_usr_inb_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
	
	/**
	 * @return the secUser
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sec_usr_id")
	public SecUser getSecUser() {
		return secUser;
	}

	/**
	 * @param secUser the secUser to set
	 */
	public void setSecUser(SecUser secUser) {
		this.secUser = secUser;
	}

	/**
	 * @return the contract
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "con_id")
	public Contract getContract() {
		return contract;
	}

	/**
	 * @param contract the contract to set
	 */
	public void setContract(Contract contract) {
		this.contract = contract;
	}	
}
