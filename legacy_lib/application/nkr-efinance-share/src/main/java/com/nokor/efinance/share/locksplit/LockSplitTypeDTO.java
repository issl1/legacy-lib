package com.nokor.efinance.share.locksplit;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author uhout.cheng
 */
public class LockSplitTypeDTO implements Serializable {
	
	/** */
	private static final long serialVersionUID = -3066444172907358363L;
	
	private Long id;
	private String code;
	private String desc;
	private String descEn;
	private List<LockSplitCashflowTypeDTO> lockSplitCashflowTypes;
		
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
	 * @return the lockSplitCashflowTypes
	 */
	public List<LockSplitCashflowTypeDTO> getLockSplitCashflowTypes() {
		return lockSplitCashflowTypes;
	}

	/**
	 * @param lockSplitCashflowTypes the lockSplitCashflowTypes to set
	 */
	public void setLockSplitCashflowTypes(
			List<LockSplitCashflowTypeDTO> lockSplitCashflowTypes) {
		this.lockSplitCashflowTypes = lockSplitCashflowTypes;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof LockSplitTypeDTO)) {
			 return false;
		 }
		 LockSplitTypeDTO lockSplitTypeDTO = (LockSplitTypeDTO) arg0;
		 return getId() != null && getId().equals(lockSplitTypeDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
