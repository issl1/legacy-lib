package com.nokor.efinance.testservice.finwiz;

import com.nokor.frmk.testing.BaseTestCase;

/**
 * 
 * @author prasnar
 *
 */
public class TestUser extends BaseTestCase {

	/**
	 * 
	 */
	public TestUser() {
		requiredAuhentication = false;
	}
	
    
	/**
	 * 
	 */
	public void testLoginUser() {
		String login = "";
		String password = "";
		try {
			login = "admin";
			password = "admin";
			AUTHENTICAT_SRV.authenticate(login, password);
			logger.info("************authentication success [" + login + "]**********");
		} catch (Exception e) {
			logger.error("Can not authenticate [" + login + "]");
			logger.error(e.getMessage(), e);
		}
		
		try {
			login = "cm_staff";
			password = "password";
			AUTHENTICAT_SRV.authenticate(login, password);

			logger.info("************authentication success [" + login + "]**********");
		} catch (Exception e) {
			logger.error("Can not authenticate [" + login + "]");
			logger.error(e.getMessage(), e);
		}
	}
	

}
