package com.nokor.efinance.share.campaign;

import java.io.Serializable;
import java.util.List;


/**
 * 
 * @author uhout.cheng
 */
public class CreditControlDTO implements Serializable {

	/**
	 */
	private static final long serialVersionUID = -2963916702096248322L;
	
	private Long id;
	private String desc;
	private String descEn;
	private List<CreditControlItemDTO> creditControlItems;
		
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
	 * @return the creditControlItems
	 */
	public List<CreditControlItemDTO> getCreditControlItems() {
		return creditControlItems;
	}

	/**
	 * @param creditControlItems the creditControlItems to set
	 */
	public void setCreditControlItems(List<CreditControlItemDTO> creditControlItems) {
		this.creditControlItems = creditControlItems;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof CreditControlDTO)) {
			 return false;
		 }
		 CreditControlDTO rreditControlDTO = (CreditControlDTO) arg0;
		 return getId() != null && getId().equals(rreditControlDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
