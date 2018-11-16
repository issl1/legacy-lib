package com.nokor.common.messaging.ws.resource.security.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author phirun.kong
 *
 */
public class RefTopicTableVO implements Serializable {
	
	/**	 */
	private static final long serialVersionUID = 6208847804330508953L;
	
	private String appCode;
	private String parentCtlCode;
	private List<RefTopicVO> topicTables = new ArrayList<>();
	
	/**
	 * 
	 */
	public RefTopicTableVO() {
		
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

	/**
	 * @return the parentCtlCode
	 */
	public String getParentCtlCode() {
		return parentCtlCode;
	}

	/**
	 * @param parentCtlCode the parentCtlCode to set
	 */
	public void setParentCtlCode(String parentCtlCode) {
		this.parentCtlCode = parentCtlCode;
	}

	/**
	 * @return the topicTables
	 */
	public List<RefTopicVO> getTopicTables() {
		return topicTables;
	}

	/**
	 * @param topicTables the topicTables to set
	 */
	public void setTopicTables(List<RefTopicVO> topicTables) {
		this.topicTables = topicTables;
	}

}
