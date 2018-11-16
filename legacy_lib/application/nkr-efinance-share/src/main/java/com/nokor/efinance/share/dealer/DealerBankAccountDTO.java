package com.nokor.efinance.share.dealer;

import java.io.Serializable;

import com.nokor.efinance.share.bank.BankDTO;

/**
 * @author youhort.ly
 */
public class DealerBankAccountDTO implements Serializable {

	/** */
	private static final long serialVersionUID = 6772912426412431258L;

	private Long id;
	private BankDTO bankAccount;
	private String accountHolder;
	private String accountNumber;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the accountHolder
	 */
	public String getAccountHolder() {
		return accountHolder;
	}
	
	/**
	 * @param accountHolder the accountHolder to set
	 */
	public void setAccountHolder(String accountHolder) {
		this.accountHolder = accountHolder;
	}
	
	/**
	 * @return the accountNumber
	 */
	public String getAccountNumber() {
		return accountNumber;
	}
	
	/**
	 * @param accountNumber the accountNumber to set
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
		
	/**
	 * @return the bankAccount
	 */
	public BankDTO getBankAccount() {
		return bankAccount;
	}
	
	/**
	 * @param bankAccount the bankAccount to set
	 */
	public void setBankAccount(BankDTO bankAccount) {
		this.bankAccount = bankAccount;
	}	
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof DealerBankAccountDTO)) {
			 return false;
		 }
		 DealerBankAccountDTO bankAccountDTO = (DealerBankAccountDTO) arg0;
		 return getId() != null && getId().equals(bankAccountDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
