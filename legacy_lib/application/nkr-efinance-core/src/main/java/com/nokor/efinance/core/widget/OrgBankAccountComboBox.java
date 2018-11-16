package com.nokor.efinance.core.widget;

import java.util.List;

import com.gl.finwiz.share.domain.AP.BankAccountDTO;
import com.nokor.efinance.third.finwiz.client.ap.ClientBankAccount;
import com.nokor.ersys.core.hr.model.organization.OrgBankAccount;

/**
 * 
 * @author uhout.cheng
 */
public class OrgBankAccountComboBox extends EntityAComboBox<OrgBankAccount> {

	/** */
	private static final long serialVersionUID = 8462517338032725457L;

	/**
	 * 
	 * @param orgBankAccounts
	 */
	public OrgBankAccountComboBox(List<OrgBankAccount> orgBankAccounts) {
		super(orgBankAccounts);
	}
	
	/**
	 * 
	 * @param caption
	 * @param orgBankAccounts
	 */
	public OrgBankAccountComboBox(String caption, List<OrgBankAccount> orgBankAccounts) {
		super(caption, orgBankAccounts);
	}

	/**
	 * @see com.nokor.efinance.core.widget.EntityAComboBox#getEntityACaption(org.seuksa.frmk.model.entity.EntityA)
	 */
	@Override
	protected String getEntityACaption(OrgBankAccount orgBankAccount) {
		BankAccountDTO bankAccountDTO = ClientBankAccount.getBankAccountById(orgBankAccount.getBankAccount());
		return bankAccountDTO == null ? null : bankAccountDTO.getPayeeAccountNumber();
	}

}
