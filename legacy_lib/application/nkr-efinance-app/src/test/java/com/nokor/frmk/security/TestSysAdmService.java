package com.nokor.frmk.security;

import com.nokor.frmk.security.sys.SysAdminHelper;
import com.nokor.frmk.testing.BaseSecurityTestCase;

/**
 * 
 * @author prasnar
 *
 */
public class TestSysAdmService extends BaseSecurityTestCase {
	private String sysAdmlogin = "superAdmin@EFIN";
	private String sysAdmPassword = "H3110@NKR855";
	
	/**
	 * 
	 */
	public TestSysAdmService() {
	}
	
	
	/**
     * 
     * @return
     */
    protected boolean isRequiredAuhentication() {
    	return false;
    }
	
    @Override
    protected void setAuthentication() {
        login = "admin";
        password = "admin@EFIN";
    }
    
	
    /**
	 * 
	 */
	public void testLoginSysAdmin() {
		try {
			
			authenticate(getLogin(), getPassword());
			
			String logUser = APP_SESSION_MNG.isAuthenticated() ? APP_SESSION_MNG.getCurrentUser().getLogin() : SecurityHelper.getAnonymous();
			logger.info("Login: " + logUser);
			
			authenticateSysAdmin(sysAdmlogin, sysAdmPassword);
			
			String logSysUser = APP_SESSION_MNG.isAuthenticated() ? APP_SESSION_MNG.getCurrentUser().getLogin() : SecurityHelper.getAnonymous();
			logger.info("SysLogin authenticated: " + logSysUser);

			logger.info("************SUCCESS**********");
        	
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 
	 */
	public void xxtestGenerateSysSpAdmString() {
		try {
			authenticate(getLogin(), getPassword());
			
			String encryptedMessage = SysAdminHelper.createSysAdmin(sysAdmlogin, sysAdmPassword);
			logger.debug("Encrypted message [" + encryptedMessage + "]");
			
			String logUser = APP_SESSION_MNG.isAuthenticated() ? APP_SESSION_MNG.getCurrentUser().getLogin() : SecurityHelper.getAnonymous();
			logger.info("Login successfull for [" + logUser + "]");

			logger.info("************SUCCESS**********");
        	
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	
}
