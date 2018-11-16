package com.nokor.efinance.file.integration;

import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.payment.model.EPaymentFileFormat;
import com.nokor.efinance.core.shared.conf.AppConfig;
import com.nokor.frmk.testing.BaseTestCase;

/**
 * @author bunlong.taing
 */
public class TestFileIntegration extends BaseTestCase implements FinServicesHelper {
	
	private static final String PATH = AppConfig.getInstance().getConfiguration().getString("document.path") + "/test";
	
	/**
	 */
	public TestFileIntegration() {
		
	}
	
	/**
	 * @see com.nokor.frmk.testing.BaseTestCase#isRequiredAuhentication()
	 */
	@Override
	protected boolean isRequiredAuhentication() {
		return false;
	}
	
	/**
	 * @see com.nokor.frmk.testing.BaseTestCase#setAuthentication()
	 */
	@Override
	protected void setAuthentication() {
		login = "admin";
		password = "admin@EFIN";
	}
	
	/**
	 */
	public void testFileIntegration() {
		String fileName = PATH + "/bay_150530_glpcl_d1.29p.txt";
		EPaymentFileFormat fileFormat = EPaymentFileFormat.BAY;
		
		logger.debug("File name : " + fileName);
		logger.debug("File format : " + fileFormat.getCode());
		FILE_INTEGRATION_SRV.integrateFilePayment(fileName, fileFormat);
		logger.debug("Finish Integration file payment");
	}
	
}
