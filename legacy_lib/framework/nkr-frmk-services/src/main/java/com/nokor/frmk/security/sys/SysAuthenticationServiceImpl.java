package com.nokor.frmk.security.sys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.frmk.helper.SeuksaServicesHelper;
import com.nokor.frmk.security.model.SecUser;

/**
 * 
 * @author prasnar
 *
 */
@Service("sysAuthenticationService")
public class SysAuthenticationServiceImpl implements SysAuthenticationService, SeuksaServicesHelper {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 
	 * @param sysAdmlogin
	 * @param sysAdmFullPassword
	 * @return
	 */
	@Override
	public SecUser authenticate(String sysAdmlogin, String sysAdmFullPassword) {
        try {
        	logger.debug("Authenticating SysAdmin [" + sysAdmlogin + "]");
        	
        	SysAuthenticationToken auth = new SysAuthenticationToken(sysAdmlogin, sysAdmFullPassword);
        	if (auth.isAuthenticated()) {
        		logout();
        	}
        	SecurityContextHolder.getContext().setAuthentication(auth);
        	
        	return UserSessionManager.getCurrentUser();
        } catch (AuthenticationException ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        }
       
	}
	
	@Override
	public void logout() {
		SESSION_MNG.logoutCurrent();
	}
}
