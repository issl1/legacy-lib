package com.nokor.frmk.security.sys;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import com.nokor.frmk.helper.FrmkAppConfigFileHelper;
import com.nokor.frmk.security.model.SecProfile;
import com.nokor.frmk.security.model.SecUser;

/**
 * 
 * @author prasnar
 *
 */
public class SysAuthenticationToken extends AbstractAuthenticationToken {
	/** */
	private static final long serialVersionUID = -7107907990845033259L;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final String sysAdmLogin;
	private String sysAdmFullPassword;
	private SecUser sysAdminUser;

	/**
	 * 
	 * @param sysAdmlogin
	 * @param sysAdmFullPassword
	 */
	public SysAuthenticationToken(String sysAdmlogin, String sysAdmFullPassword) {
		super(null);
		this.sysAdmLogin = sysAdmlogin;
		this.sysAdmFullPassword = sysAdmFullPassword;
		setAuthenticated(false);
		
		validate();
	}



	/**
	 * @see org.springframework.security.core.Authentication#getCredentials()
	 */
	@Override
	public Object getCredentials() {
		return this.sysAdmFullPassword;
	}

	/**
	 * @see org.springframework.security.core.Authentication#getPrincipal()
	 */
	@Override
	public Object getPrincipal() {
		return this.sysAdminUser;
	}

	/**
	 * @see org.springframework.security.authentication.AbstractAuthenticationToken#setAuthenticated(boolean)
	 */
	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		super.setAuthenticated(isAuthenticated);
	}

	/**
	 * @see org.springframework.security.authentication.AbstractAuthenticationToken#eraseCredentials()
	 */
	@Override
	public void eraseCredentials() {
		super.eraseCredentials();
		this.sysAdmFullPassword = null;
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean validate() {
		String encryptedMessage = FrmkAppConfigFileHelper.getSysSpAdmMessage();
		
		boolean auth = SysAdminHelper.isValidMessage(sysAdmLogin, sysAdmFullPassword, encryptedMessage);
		setAuthenticated(auth);
		
		if (auth) {
			sysAdminUser = SecUser.createInstance();
			sysAdminUser.setLogin((String) sysAdmLogin);
			sysAdminUser.setDefaultProfile(SecProfile.SYS_ADMIN);
		} else {
			logger.warn("Can not authenticate [" + sysAdmLogin + "].");
			sysAdminUser = null;
		}
		
		return auth;
	}
}