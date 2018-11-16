package com.nokor.efinance.tools.sync;

import java.io.Serializable;

/**
 * @author nora.ky
 *
 */
public class ConnectionSetting implements Serializable{
	private static final long serialVersionUID = 1184778476778965935L;
	private String appCode;
	private String mailHost;
    private String mailFrom;
    private String smsFrom;
	private String syncServer;
	
	/**
	 * @return
	 */
	public String getMailHost() {
		return mailHost;
	}

	/**
	 * @param mailHost
	 */
	public void setMailHost(String mailHost) {
		this.mailHost = mailHost;
	}

	/**
	 * @return
	 */
	public String getMailFrom() {
		return mailFrom;
	}

	/**
	 * @param mailFrom
	 */
	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}

	/**
	 * @return
	 */
	public String getSmsFrom() {
		return smsFrom;
	}

	/**
	 * @param smsFrom
	 */
	public void setSmsFrom(String smsFrom) {
		this.smsFrom = smsFrom;
	}

	/**
	 * @return
	 */
	public String getSyncServer() {
		return syncServer;
	}

	/**
	 * @param syncServer
	 */
	public void setSyncServer(String syncServer) {
		this.syncServer = syncServer;
	}

	/**
	 * @return
	 */
	public String getAppCode() {
		return appCode;
	}

	/**
	 * @param appCode
	 */
	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

}
