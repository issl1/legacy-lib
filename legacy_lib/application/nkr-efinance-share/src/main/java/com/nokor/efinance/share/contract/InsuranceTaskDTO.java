package com.nokor.efinance.share.contract;

import java.io.Serializable;

/**
 * 
 * @author uhout.cheng
 */
public class InsuranceTaskDTO implements Serializable {

	/**
	 */
	private static final long serialVersionUID = -2144422082796049506L;
		
	private String contractNo;
	
	/**
	 * @return the contractNo
	 */
	public String getContractNo() {
		return contractNo;
	}

	/**
	 * @param contractNo the contractNo to set
	 */
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}	
}
