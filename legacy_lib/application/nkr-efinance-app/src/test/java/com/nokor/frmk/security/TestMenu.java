package com.nokor.frmk.security;

import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.security.model.SecApplication;
import com.nokor.frmk.testing.BaseTestCase;
import com.nokor.frmk.vaadin.util.VaadinServicesHelper;


/**
 * @author prasnar
 * @version $Revision$
 */
public class TestMenu extends BaseTestCase implements VaadinServicesHelper {



    /**
     * 
     */
    public TestMenu() {
    }
    
    @Override
    protected void setAuthentication() {
        login = "admin";
        password = "admin@EFIN";
    }
    
    /**
     * 
     */
    public void testBuildMenuBar() {
    	try {
    		
    		authenticate(login, password);
    		
//    		VaadinMenuHelper.buildMenuBar(privileges)
    		SecApplication secApp = SECURITY_SRV.getApplication("EFINANCE_RA");
    		SecApplicationContextHolder.getContext().setSecApplication(secApp);
    		VAADIN_SESSION_MNG.getCurrent().getMainMenuBar();

        	
    		logger.info("************SUCCESS**********");
        	
    	} catch (Exception e) {
    		logger.error(e.getMessage(), e);
    	}
    }
   
   

}
