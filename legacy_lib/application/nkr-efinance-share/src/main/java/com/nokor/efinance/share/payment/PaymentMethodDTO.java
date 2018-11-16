package com.nokor.efinance.share.payment;

import java.io.Serializable;

import com.nokor.common.messaging.share.refdata.RefDataDTO;


/**
 * 
 * @author uhout.cheng
 */
public class PaymentMethodDTO implements Serializable {

	/** */
	private static final long serialVersionUID = -1188132024722553095L;
	
	private Long id;
	private String code;
	private String descEn;
	private String desc;
	private RefDataDTO paymentMethodCategory;
	private boolean autoConfirm;
	
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
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the descEn
	 */
	public String getDescEn() {
		return descEn;
	}
	
	/**
	 * @param descEn the descEn to set
	 */
	public void setDescEn(String descEn) {
		this.descEn = descEn;
	}
	
	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}
	
	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	/**
	 * @return the paymentMethodCategory
	 */
	public RefDataDTO getPaymentMethodCategory() {
		return paymentMethodCategory;
	}

	/**
	 * @param paymentMethodCategory the paymentMethodCategory to set
	 */
	public void setPaymentMethodCategory(RefDataDTO paymentMethodCategory) {
		this.paymentMethodCategory = paymentMethodCategory;
	}

	/**
	 * @return the autoConfirm
	 */
	public boolean isAutoConfirm() {
		return autoConfirm;
	}

	/**
	 * @param autoConfirm the autoConfirm to set
	 */
	public void setAutoConfirm(boolean autoConfirm) {
		this.autoConfirm = autoConfirm;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof PaymentMethodDTO)) {
			 return false;
		 }
		 PaymentMethodDTO paymentMethodDTO = (PaymentMethodDTO) arg0;
		 return getId() != null && getId().equals(paymentMethodDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
