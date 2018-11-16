package com.nokor.efinance.core.widget;

import java.util.List;

import com.gl.finwiz.share.domain.AP.BankAccountDTO;
import com.nokor.efinance.core.dealer.model.DealerBankAccount;
import com.nokor.efinance.third.finwiz.client.ap.ClientBankAccount;

/**
 * 
 * @author uhout.cheng
 */
public class DealerBankAccountComboBox extends EntityAComboBox<DealerBankAccount> {

	/** */
	private static final long serialVersionUID = -5880875513671974484L;

	/**
	 * 
	 * @param dealerBankAccounts
	 */
	public DealerBankAccountComboBox(List<DealerBankAccount> dealerBankAccounts) {
		super(dealerBankAccounts);
	}
	
	/**
	 * 
	 * @param caption
	 * @param dealerBankAccounts
	 */
	public DealerBankAccountComboBox(String caption, List<DealerBankAccount> dealerBankAccounts) {
		super(caption, dealerBankAccounts);
	}

	/**
	 * @see com.nokor.efinance.core.widget.EntityAComboBox#getEntityACaption(org.seuksa.frmk.model.entity.EntityA)
	 */
	@Override
	protected String getEntityACaption(DealerBankAccount dealerBankAccount) {
		BankAccountDTO bankAccountDTO = ClientBankAccount.getBankAccountById(dealerBankAccount.getBankAccount());
		return bankAccountDTO == null ? null : bankAccountDTO.getPayeeAccountNumber();
	}

}
