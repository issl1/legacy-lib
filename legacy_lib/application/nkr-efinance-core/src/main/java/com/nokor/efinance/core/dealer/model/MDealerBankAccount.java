package com.nokor.efinance.core.dealer.model;

import org.seuksa.frmk.model.entity.MEntityRefA;

/**
 * Meta data of com.nokor.efinance.core.dealer.model.DealerBankAccount
 * @author uhout.cheng
 */
public interface MDealerBankAccount extends MEntityRefA {
	
	// For Vaadin Grid
	public final static String ACCOUNTNAME = "account.name";
	public final static String PAYEEACCOUNTNUMBER = "payee.account.number";
	public final static String BANK = "bank";
	public final static String BANKBRANCH = "bank.branch";
	
	public final static String DEALER = "dealer";
	public final static String BANKACCOUNTID = "bankAccount";
	
}
