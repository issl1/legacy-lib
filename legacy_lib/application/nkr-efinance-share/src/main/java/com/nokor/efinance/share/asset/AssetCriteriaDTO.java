package com.nokor.efinance.share.asset;

import java.io.Serializable;

import com.google.gson.Gson;


/**
 * 
 * @author uhout.cheng
 */
public class AssetCriteriaDTO implements Serializable {
	
	/** */
	private static final long serialVersionUID = -8539686576740331722L;
	
	private String chassisNumber;
	private String engineNumber;

	/**
	 * @return the chassisNumber
	 */
	public String getChassisNumber() {
		return chassisNumber;
	}

	/**
	 * @param chassisNumber the chassisNumber to set
	 */
	public void setChassisNumber(String chassisNumber) {
		this.chassisNumber = chassisNumber;
	}

	/**
	 * @return the engineNumber
	 */
	public String getEngineNumber() {
		return engineNumber;
	}

	/**
	 * @param engineNumber the engineNumber to set
	 */
	public void setEngineNumber(String engineNumber) {
		this.engineNumber = engineNumber;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
}
