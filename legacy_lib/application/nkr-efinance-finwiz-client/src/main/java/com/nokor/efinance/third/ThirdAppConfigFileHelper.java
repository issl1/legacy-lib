package com.nokor.efinance.third;

import com.nokor.common.app.tools.helper.AppConfigFileHelper;

/**
 * 
 * @author prasnar
 * 
 */
public class ThirdAppConfigFileHelper extends AppConfigFileHelper {
	public final static String FINWIZ_ENABLE = "third.finzwiz.enable";
	public final static String FINWIZ_SERVER_URL = "third.finzwiz.server.endpoint";
	public final static String FINWIZ_REG_URL = "third.finzwiz.reg.endpoint";
	public final static String FINWIZ_INS_URL = "third.finzwiz.ins.endpoint";
	public final static String FINWIZ_SMS_URL = "third.finzwiz.sms.endpoint";
	public final static String FINWIZ_AP_URL = "third.finzwiz.ap.endpoint";
	public final static String FINWIZ_APP_URL = "third.finzwiz.app.endpoint";
	public final static String FINWIZ_ADMIN_URL = "third.finzwiz.admin.endpoint";
	
	/**
	 * @return
	 */
	public static boolean isFinwizEnable() {
		return getValueBoolean(FINWIZ_ENABLE);
	}
	
	/**
	 * @return
	 */
	public static String getFinwizServerURL() {
		return getValue(FINWIZ_SERVER_URL);
	}
	
	/**
	 * @return
	 */
	public static String getRegistrationURL() {
		return getValue(FINWIZ_REG_URL);
	}
	
	/**
	 * @return
	 */
	public static String getInsuranceURL() {
		return getValue(FINWIZ_INS_URL);
	}
	
	/**
	 * @return
	 */
	public static String getAdministrationURL() {
		return getValue(FINWIZ_ADMIN_URL);
	}
	
	/**
	 * @return
	 */
	public static String getSmsURL() {
		return getValue(FINWIZ_SMS_URL);
	}
	
	/**
	 * @return
	 */
	public static String getAPURL() {
		return getValue(FINWIZ_AP_URL);
	}
	
	/**
	 * @return
	 */
	public static String getEFinanceApplicationURL() {
		return getValue(FINWIZ_APP_URL);
	}
}
