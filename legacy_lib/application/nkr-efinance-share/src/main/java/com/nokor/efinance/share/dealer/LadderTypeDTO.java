package com.nokor.efinance.share.dealer;

import java.io.Serializable;

/**
 * 
 * @author uhout.cheng
 */
public class LadderTypeDTO implements Serializable {


	/**
	 */
	private static final long serialVersionUID = -8824912769487501136L;
	
	private Long id;
	private String desc;
	private String descEn;
	
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
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof LadderTypeDTO)) {
			 return false;
		 }
		 LadderTypeDTO ladderTypeDTO = (LadderTypeDTO) arg0;
		 return getId() != null && getId().equals(ladderTypeDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
