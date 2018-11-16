package com.nokor.frmk.security;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.testing.BaseSecurityTestCase;
import com.nokor.frmk.vaadin.ui.menu.VaadinMenuHelper;
import com.nokor.frmk.vaadin.util.VaadinServicesHelper;

/**
 * 
 * @author prasnar
 *
 */
public class TestSessionManager extends BaseSecurityTestCase implements VaadinServicesHelper {
	
	/**
	 * 
	 */
	public TestSessionManager() {
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
	public void testMenuBar() {
		try {
			
			authenticate(getLogin(), getPassword());
			
			SecUser user = UserSessionManager.getCurrentUser();
			String logUser = APP_SESSION_MNG.isAuthenticated() ? user.getLogin() : SecurityHelper.getAnonymous();
			logger.info("Login: " + logUser);
			
			VAADIN_SESSION_MNG.init();
			VAADIN_SESSION_MNG.getCurrent().getMainMenuBar();
			
			logger.info(VaadinMenuHelper.displayMenuBar());

			logger.info("************SUCCESS**********");
        	
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
		
}
