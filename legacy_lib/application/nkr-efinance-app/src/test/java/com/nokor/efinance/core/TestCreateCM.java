package com.nokor.efinance.core;

import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.quotation.FakeQuotationService;
import com.nokor.frmk.testing.BaseTestCase;


/**
 * 
 * @author prasnar
 *
 */
public class TestCreateCM extends BaseTestCase {

	/**
	 * 
	 */
	public TestCreateCM() {
	}
	
	public void testCreateQuotationCM() {
		try {
			FakeQuotationService cIService = getBean(FakeQuotationService.class);
			for (int i = 0; i < 10; i++) {
				cIService.simulateCreateQuotation(1l, DateUtils.todayH00M00S00(), DateUtils.todayH00M00S00());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
