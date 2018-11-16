package com.nokor.efinance.share.bank;

import java.io.Serializable;

/**
 * Bank account representation
 * @author youhort.ly
 */
public class BankDTO implements Serializable {

	/** */
	private static final long serialVersionUID = 770121686096053148L;

	private Long id;
	private String bankName;
	private String swiftCode;
	
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
	 * @return the bankName
	 */
	public String getBankName() {
		return bankName;
	}
	
	/**
	 * @param bankName the bankName to set
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
	/**
	 * @return the swiftCode
	 */
	public String getSwiftCode() {
		return swiftCode;
	}
	
	/**
	 * @param swiftCode the swiftCode to set
	 */
	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof BankDTO)) {
			 return false;
		 }
		 BankDTO bankDTO = (BankDTO) arg0;
		 return getId() != null && getId().equals(bankDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
