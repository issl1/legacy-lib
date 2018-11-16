package com.nokor.ersys.messaging.share.organization;

import java.io.Serializable;

/**
 * 
 * @author uhout.cheng
 */
public class BranchDTO extends BaseOrgStructureDTO implements Serializable {

	/** */
	private static final long serialVersionUID = 3058656749209378442L;
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof BranchDTO)) {
			 return false;
		 }
		 BranchDTO branchDTO = (BranchDTO) arg0;
		 return getId() != null && getId().equals(branchDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
