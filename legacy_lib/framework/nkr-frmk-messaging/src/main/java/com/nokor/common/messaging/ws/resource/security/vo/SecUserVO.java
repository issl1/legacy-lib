package com.nokor.common.messaging.ws.resource.security.vo;

import com.nokor.common.messaging.share.BaseEntityDTO;

/**
 * 
 * @author prasnar
 *
 */
public class SecUserVO extends BaseEntityDTO {
	/** */
	private static final long serialVersionUID = -5260659510476190885L;
	private String login;
	private String desc;
	private String pwd;

	private String proCode;

	/**
	 * 
	 */
	public SecUserVO() {
		
	}
	
	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
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
	 * @return the pwd
	 */
	public String getPwd() {
		return pwd;
	}

	/**
	 * @param pwd the pwd to set
	 */
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	/**
	 * @return the proCode
	 */
	public String getProCode() {
		return proCode;
	}

	/**
	 * @param proCode the proCode to set
	 */
	public void setProCode(String proCode) {
		this.proCode = proCode;
	}

	
	
}
