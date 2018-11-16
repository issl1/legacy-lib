package com.nokor.frmk.testing;

import com.nokor.common.messaging.ws.resource.security.BaseSecuritySrvRsc;
import com.nokor.frmk.security.SecurityHelper;
import com.nokor.frmk.security.model.SecApplication;
import com.nokor.frmk.security.model.SecProfile;
import com.nokor.frmk.security.model.SecUser;

/**
 * 
 * @author prasnar
 *
 */
public class BaseSecurityTestCase extends BaseTestCase {

	/**
	 * 
	 */
	public BaseSecurityTestCase() {
		super();
	}
	
	/**
     * 
     * @return
     */
    protected boolean isInitWorkflow() {
    	return false;
    }
	
	/**
	 * 
	 */
	public void buildDefaultSecurity() {
		BaseSecuritySrvRsc.execBuildSecurityWithApp(SecApplication.DEFAULT_APP);
	}
	
	
	/**
	 * 
	 * @return
	 */
	public SecUser getDefaultFOUser() {
		SecUser user = SecurityHelper.getDefaultFOUser();
		logger.info("Default FO user [" + user.getLogin() + "].");
		SecProfile defaultUserPro = user.getDefaultProfile();
		logger.info("Default FO user profile [" + defaultUserPro.getCode() + "].");
		return user;
	}
	
	/**
	 * 
	 * @param user
	 * @param app
	 */
	public void addUserInApp(SecUser user, SecApplication app) {
		SecProfile pro = user.getDefaultProfile();

		// Authorize profile to access to the app
		if (!app.getProfiles().contains(pro)) {
			SECURITY_SRV.addApplicationToProfile(app, pro);
			logger.info("The profile [" + pro.getCode() + "] has been given access to the app [" + app.getCode() + "].");
		}
			
	}

	

}
