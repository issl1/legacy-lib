package com.nokor.frmk.testing;

import org.springframework.util.Assert;

import com.nokor.frmk.security.SecurityEntityFactory;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.security.service.AuthenticationService;
import com.nokor.frmk.security.service.SecurityService;

/**
 * 
 * @author prasnar
 *
 */
public class SampleSecurityTestCase extends BaseSecurityTestCase {
 
	/**
	 * 
	 */
	public SampleSecurityTestCase() {
	}
	
	
	
	/**
	 * 
	 */
	public void xxtestBuildDefaultSecurity() {
		buildDefaultSecurity();
	}

	public void xxtestAddRole() {
		try {
			String username = "admin";
			
			SecUser secUser = SECURITY_SRV.loadUserByUsername(username);
			Assert.notNull(secUser, "[" + username + "] does not exist.");
			
			
			
			Assert.notNull(secUser, "The creation of [" + username + "] has failed.");
			
			logger.info("New digested password [" + secUser.getPassword() + "]");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	public void xxtestCreateUser() {
		try {
			String username = "admin1";
			String password = "admin1";
			SecurityService secSrv = getBean(SecurityService.class);
			SecUser secUser = secSrv.loadUserByUsername(username);
//			SecUser secUser = authSrv.authenticate(username, password);
			Assert.isNull(secUser, "[" + username + "] already existed.");
			secUser = SecurityEntityFactory.createSecUserInstance();
			secUser.setLogin(username);
			secUser.setDesc(username);
			
			secSrv.createUser(secUser, password);
			
			Assert.notNull(secUser, "The creation of [" + username + "] has failed.");
			
			logger.info("Digested password [" + secUser.getPassword() + "]");
			logger.info("Salt password [" + secUser.getPasswordSalt() + "]");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * 
	 */
	public void testChangePasswordUser() {
		try {
			String username = "admin";
			String oldPassword = "admin";
			String newPassword = "admin";
			SecurityService secSrv = getBean(SecurityService.class);
			SecUser secUser = secSrv.loadUserByUsername(username);
			Assert.notNull(secUser, "[" + username + "] does not exist.");
			
			secSrv.changePasswordSalt(secUser);
			secSrv.changePassword(secUser, oldPassword, newPassword);
			//secSrv.changePassword(secUser, newPassword);
			
			Assert.notNull(secUser, "The creation of [" + username + "] has failed.");
			
			logger.info("New digested password [" + secUser.getPassword() + "]");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * SecUser authentication
	 */
	public void xxtestAuthentication() {
		try {
			String username = "admin";
			String password = "admin";
			AuthenticationService authSrv = getBean(AuthenticationService.class);
			//SecUser secUser = authSrv.loadUserByUsername(username);
			SecUser secUser = authSrv.authenticate(username, password);
			
			Assert.notNull(secUser, "[" + username + "] can not be authenticated.");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
}
