package com.nokor.efinance.share.dealer;

import java.io.Serializable;

import com.nokor.common.messaging.share.refdata.RefDataDTO;

/**
 * @author youhort.ly
 */
public class DealerPaymentMethodDTO implements Serializable {

	/** */
	private static final long serialVersionUID = 6772912426412431258L;

	private Long id;
	private RefDataDTO type;
	private DealerBankAccountDTO bankAccount;
	

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
	 * @return the type
	 */
	public RefDataDTO getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(RefDataDTO type) {
		this.type = type;
	}

	/**
	 * @return the bankAccount
	 */
	public DealerBankAccountDTO getBankAccount() {
		return bankAccount;
	}

	/**
	 * @param bankAccount the bankAccount to set
	 */
	public void setBankAccount(DealerBankAccountDTO bankAccount) {
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
		 if (arg0 == null || !(arg0 instanceof DealerPaymentMethodDTO)) {
			 return false;
		 }
		 DealerPaymentMethodDTO dealerPaymentMethodDTO = (DealerPaymentMethodDTO) arg0;
		 return getId() != null && getId().equals(dealerPaymentMethodDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
