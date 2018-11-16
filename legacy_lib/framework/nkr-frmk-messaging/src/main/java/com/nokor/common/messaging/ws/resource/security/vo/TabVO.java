package com.nokor.common.messaging.ws.resource.security.vo;

import com.nokor.common.messaging.share.BaseEntityRefDTO;

/**
 * 
 * @author phirun.kong
 *
 */
public class TabVO extends BaseEntityRefDTO {
	
	/**	 */
	private static final long serialVersionUID = 6341044379808046372L;

	private String appCode;
	private String parentCode;
	
	/**
	 * 
	 */
	public TabVO() {
		
	}

	/**
	 * @return the parentCode
	 */
	public String getParentCode() {
		return parentCode;
	}

	/**
	 * @param parentCode the parentCode to set
	 */
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	/**
	 * @return the appCode
	 */
	public String getAppCode() {
		return appCode;
	}

	/**
	 * @param appCode the appCode to set
	 */
	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

}
