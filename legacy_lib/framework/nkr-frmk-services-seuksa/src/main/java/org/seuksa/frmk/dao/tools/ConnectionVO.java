package org.seuksa.frmk.dao.tools;

import java.io.Serializable;

/**
 * Connection informations 
 * @author prasnar
 */
public class ConnectionVO implements Serializable {

	/** */
	private static final long serialVersionUID = 1906902168379415160L;
	
	/** */
	private String driver;
	/** */
	private String user;
	/** */
	private String pwd;
	/** */
	private String url;
	
	/**
	 * 
	 * @param driver
	 * @param user
	 * @param pwd
	 * @param url
	 */
	public ConnectionVO(String driver, String user, String pwd, String url) {
		this.driver = driver;
		this.user = user;
		this.pwd = pwd;
		this.url = url;
	}
	
	/**
	 * @return the driver
	 */
	public String getDriver() {
		return driver;
	}
	/**
	 * @param driver the driver to set
	 */
	public void setDriver(String driver) {
		this.driver = driver;
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
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}
	

}
