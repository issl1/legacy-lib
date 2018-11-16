package com.nokor.common.messaging.ws.resource.security.vo;

import java.util.List;

import com.nokor.common.messaging.share.BaseEntityRefDTO;

/**
 * 
 * @author phirun.kong
 *
 */
public class RefTopicVO extends BaseEntityRefDTO {
	
	/**	 */
	private static final long serialVersionUID = -4329420413930565108L;
	
	private String parentCode;
	private List<String> tableCodes;

	/**
	 * 
	 */
	public RefTopicVO() {
		
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
	 * @return the tableCodes
	 */
	public List<String> getTableCodes() {
		return tableCodes;
	}

	/**
	 * @param tableCodes the tableCodes to set
	 */
	public void setTableCodes(List<String> tableCodes) {
		this.tableCodes = tableCodes;
	}

}
