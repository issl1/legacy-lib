package com.nokor.efinance.migration;

import java.util.List;

import org.springframework.util.Assert;

import com.nokor.efinance.core.security.FinSecurity;
import com.nokor.ersys.core.hr.model.organization.Employee;
import com.nokor.frmk.security.SecurityEntityFactory;
import com.nokor.frmk.security.SecurityHelper;
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
public class TestUserMigration extends BaseSecurityTestCase {

	private static final String EFINANCE_APP = FinSecurity.EFINANCE_APP;
	private static final String EFINANCE_RA = FinSecurity.EFINANCE_RA;
	private static final String EFINANCE_TM = FinSecurity.EFINANCE_TM;
	
	/**
	 * 
	 */
	public TestUserMigration() {
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
    public void testCreateUsers() {
    	String errMsg = null;
    	try {
    		
    		List<TmpUser> lstTmpUserVOs = ENTITY_SRV.list(TmpUser.class);
    		for (TmpUser userVo : lstTmpUserVOs) {
    			SecProfile pro = ENTITY_SRV.getByCode(SecProfile.class, userVo.getProfileCode());
    			if (pro == null) {
    				errMsg = "Can not find profile [" + userVo.getProfileCode() + "]";
    				throw new IllegalStateException(errMsg);
    			}
    			SecUser secUser = SecurityEntityFactory.createInstance(SecUser.class, "adminCreator");
    			secUser.setLogin(userVo.getLogin());
    			secUser.setDesc(userVo.getLastName() + " " + userVo.getFirstName());
    			secUser.setDefaultProfile(pro);
    			
    			SECURITY_SRV.createUser(secUser, userVo.getPwd());
    			
    			Assert.notNull(secUser, "The creation of [" + secUser.getLogin() + "] has failed.");
    			
    			Employee emp = Employee.createInstance();
    			emp.setLastName(userVo.getLastName());
    			emp.setLastNameEn(emp.getLastName());
    			emp.setFirstName(userVo.getFirstName());
    			emp.setFirstNameEn(emp.getFirstName());
    			emp.setSecUser(secUser);
    			
    			ENTITY_SRV.create(emp);
    			
    			
    		}
    		
    		
    	} catch (Exception e) {
    		errMsg = e.getMessage();
			logger.error(errMsg, e);
			throw new IllegalStateException(errMsg);
		}
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
