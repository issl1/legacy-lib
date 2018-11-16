package com.nokor.efinance.core.helper;

import com.nokor.common.app.tools.helper.AppSettingConfigHelper;

/**
 * 
 * @author prasnar
 */
public class FinSettingConfigHelper extends AppSettingConfigHelper {

	private static String MAX_FIRST_DUE_DATE_FIXATION = "max.first.due.date.fixation";
	private static String QUOTE_REF_NUMBER = "quo_ref_num";
	/**
	 * 
	 * @return
	 */
	public static int getMinMonthPayForContract(String code) {
		return SETTING_SRV.getValueInt(code, -1);
	}

	/**
	 * 
	 * @return
	 */
	public static long getQuotationReferenceNumber() {
		return SETTING_SRV.getValueLong(QUOTE_REF_NUMBER);
	}
	
	/**
	 * 
	 * @return
	 */
	public static int getMaxFirstDueDateFixation() {
		return SETTING_SRV.getValueInt(MAX_FIRST_DUE_DATE_FIXATION, 45);
	}

}
