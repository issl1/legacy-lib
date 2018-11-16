package com.nokor.ersys.core.finance.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "tu_bank_account")
public class BankAccount extends BaseBankAccount {
	/** */
	private static final long serialVersionUID = -6102960016914026521L;

	/**
     * 
     * @return
     */
    public static BankAccount createInstance() {
    	BankAccount bankAcc = EntityFactory.createInstance(BankAccount.class);
        return bankAcc;
    }
    
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ban_acc_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

}
