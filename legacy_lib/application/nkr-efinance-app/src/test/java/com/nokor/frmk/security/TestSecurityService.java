package com.nokor.frmk.security;

import java.util.List;

import org.seuksa.frmk.dao.sql.SqlScript;
import org.springframework.util.Assert;

import com.nokor.efinance.core.security.FinSecurity;
import com.nokor.frmk.security.model.SecApplication;
import com.nokor.frmk.security.model.SecControl;
import com.nokor.frmk.security.model.SecProfile;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.security.service.AuthenticationService;
import com.nokor.frmk.security.service.SecurityService;
import com.nokor.frmk.testing.BaseSecurityTestCase;

/**
 * 
 * @author prasnar
 *
 */
public class TestSecurityService extends BaseSecurityTestCase {

	private static final String EFINANCE_APP = FinSecurity.EFINANCE_APP;
	private static final String EFINANCE_RA = FinSecurity.EFINANCE_RA;
	private static final String EFINANCE_TM = FinSecurity.EFINANCE_TM;
	
	/**
	 * 
	 */
	public TestSecurityService() {
		requiredSecApplicationContext = false;
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
    public void xxtestDummy() {
//    	String proCode = IProfileCode.COL_PHO_STAFF;
//		SecProfile pro = ENTITY_SRV.getByCode(SecProfile.class, proCode);
//		String myLogin = "test";
//		String myPwd = "test";
//		SecUser usr = SECURITY_SRV.createUser(myLogin, myLogin,myPwd, pro, false);
//		authenticate(myLogin, myPwd);
    	
//    	List<String[]> USR_LIST = FinSecurity.USR_LIST;
//    	for (String[] usrObj : USR_LIST) {
//    		String sql = "delete from tu_sec_user_profile where sec_usr_id in (select sec_usr_id from tu_sec_user where sec_usr_login = '" + usrObj[1] + "');"
//    					+ "delete from tu_sec_user where sec_usr_login= '" + usrObj[1] + "';";
//    		logger.info(sql);
//    		List<Object[]> res = SqlScript.execSql(sql);
//    		
//    	}

    	logger.info("*********DUMMY************");
    }
   
	
	/**
	 * Build the default security 
	 * 
	 */
	public void xtestBuildDefaultSecurity() {
		try {
			FinSecurity.execBuildDefaultSecurity();
			
			logger.info("************SUCCESS**********");
        	
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * Menu/Control Access
	 */
	public void testBuildAdditionnalSecurity() {
		try {
			FinSecurity.execBuildAdditionnalSecurity();
			
    		logger.info("************SUCCESS**********");
        	
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
    /**
     * 
     */
    public void xxtestDeleteUsers() {
    	List<String[]> USR_LIST = FinSecurity.USR_LIST;
    	for (String[] usrObj : USR_LIST) {
    		String sql = "delete from tu_sec_user_profile where sec_usr_id in (select sec_usr_id from tu_sec_user where sec_usr_login = '" + usrObj[1] + "');"
    					+ "delete from tu_sec_user where sec_usr_login= '" + usrObj[1] + "';";
    		List<Object[]> res = SqlScript.execSql(sql);
    		
    	}

    	logger.info("*********DUMMY************");
    }
	
	/**
	 * 
	 */
	public void xxtestCreateManagedProfiles() {
		try {
			// ADMIN profile will be able to manage the profiles defined by PRO_LIST
			SecProfile proAdmin = ENTITY_SRV.getById(SecProfile.class, SecProfile.ADMIN.getId());
			SecurityHelper.createManagedProfiles(proAdmin, FinSecurity.PRO_LIST_APP);
			
    		logger.info("************SUCCESS**********");
        	
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 
	 */
	public void xxxtestAddRole() {
		try {
			String username = "admin";
			String role = "ROLE_ADMIN";
			
			
			SecurityService SECURITY_SRV = getBean(SecurityService.class);
			SecUser secUser = SECURITY_SRV.loadUserByUsername(username);
			Assert.notNull(secUser, "[" + username + "] does not exist.");
			
			
			
			Assert.notNull(secUser, "The creation of [" + username + "] has failed.");
			
			logger.info("New digested password [" + secUser.getPassword() + "]");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	public void xxxxtestCreateUser() {
		try {
			String username = "admin";
			String password = "admin";
			SecUser secUser = SECURITY_SRV.loadUserByUsername(username);
//			SecUser secUser = authSrv.authenticate(username, password);
			Assert.isNull(secUser, "[" + username + "] already existed.");
			secUser = SecurityEntityFactory.createInstance(SecUser.class, "adminCreator");
			secUser.setLogin(username);
			secUser.setDesc(username);
			
			SECURITY_SRV.createUser(secUser, password);
			
			Assert.notNull(secUser, "The creation of [" + username + "] has failed.");
			
			logger.info("Digested password [" + secUser.getPassword() + "]");
			logger.info("Salt password [" + secUser.getPasswordSalt() + "]");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 
	 */
	public void xxtestChangePasswordUser() {
		try {
			String username = "admin";
			String oldPassword = "admin";
			String newPassword = "admin";
			SecurityService SECURITY_SRV = getBean(SecurityService.class);
			SecUser secUser = SECURITY_SRV.loadUserByUsername(username);
			Assert.notNull(secUser, "[" + username + "] does not exist.");
			
			SECURITY_SRV.changePasswordSalt(secUser);
			SECURITY_SRV.changePassword(secUser, oldPassword, newPassword);
			
			Assert.notNull(secUser, "The creation of [" + username + "] has failed.");
			
			logger.info("New digested password [" + secUser.getPassword() + "]");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 
	 */
	public void xxxtestChangePasswordUserWithoutOldPassword() {
		try {
			String username = "admin";
			String newPassword = "admin";
			SecurityHelper.changeUserPassword(username, newPassword);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
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
			logger.error(e.getMessage(), e);
		}
	}
	
	 
    /**
     * 
     */
    public void xxtestProfileControls() {
    	try{
    	SecApplication app = ENTITY_SRV.getByCode(SecApplication.class, EFINANCE_RA);
		List<SecControl> controls = SECURITY_SRV.listControlsByAppIdAndProfile(app.getId(), SecProfile.ADMIN.getId());
		for (SecControl ctl : controls) {
			logger.info("- [" + ctl.getId() + "] " + ctl.getCode());
		}
    	
    	logger.info("*********SUCESS************");
    	logger.info("************SUCCESS**********");
    	
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
    }	
    
    
}
