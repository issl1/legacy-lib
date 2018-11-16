package com.nokor.ersys.core.web.action.login;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.frmk.web.struts.action.ActionResult;

/**
 * 
 * @author prasnar
 *
 */
public class LoginForm implements Serializable {
	/** */
	private static final long serialVersionUID = 6698563899998538322L;

	private Long id;
	private String username;
	private String password;
	private Boolean isRemember;

	private Long empId;

	/**
	 * 
	 */
	public LoginForm() {
		
	}
	
	/**
	 * 
	 * @param result
	 * @return
	 */
	public boolean validateForm(ActionResult result) {
		String errMsg = "";
		if (StringUtils.isEmpty(username)) {
    		errMsg = I18N.message("error.field.mandatory", I18N.message("label.login")) + "<br>";
		}
		if (StringUtils.isEmpty(password)) {
    		errMsg += I18N.message("error.field.mandatory", I18N.message("label.pwd"));
		}

    	result.setErrorMessage(errMsg);
    	
    	return errMsg.equals("");
    }
	
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
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the isRemember
	 */
	public Boolean getIsRemember() {
		return isRemember;
	}
	/**
	 * @param isRemember the isRemember to set
	 */
	public void setIsRemember(Boolean isRemember) {
		this.isRemember = isRemember;
	}

	/**
	 * @return the empId
	 */
	public Long getEmpId() {
		return empId;
	}

	/**
	 * @param empId the empId to set
	 */
	public void setEmpId(Long empId) {
		this.empId = empId;
	}
}
