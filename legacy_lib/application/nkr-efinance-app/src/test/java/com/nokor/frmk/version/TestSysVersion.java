package com.nokor.frmk.version;

import java.util.Date;

import com.nokor.common.app.systools.model.SysVersion;
import com.nokor.frmk.testing.BaseSecurityTestCase;

/**
 * 
 * @author prasnar
 *
 */
public class TestSysVersion extends BaseSecurityTestCase {

	/**
	 * 
	 */
	public TestSysVersion() {
	}
	
	/**
     * 
     * @return
     */
    protected boolean isRequiredAuhentication() {
    	return false;
    }
	
    /**
	 * 
	 */
	public void testAppVersion() {
		try {
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 
	 */
	public void initVersion() {
		SysVersion ver = SysVersion.createInstance();
		ver.setDbVersion("0");
		ver.setAppVersion("0");
		ver.setWhen(new Date());
		ver.setScript(null);
		ENTITY_SRV.create(ver);
		logger.info("************Init SUCCESS**********");
	}

	

}
