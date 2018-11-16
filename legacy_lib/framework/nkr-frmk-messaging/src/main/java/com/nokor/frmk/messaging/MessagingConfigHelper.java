package com.nokor.frmk.messaging;

import com.nokor.common.app.tools.helper.AppConfigFileHelper;


/**
 * 
 * @author prasnar
 *
 */
public class MessagingConfigHelper extends AppConfigFileHelper  {

	protected static final String _SLASH = "/";
	private final static String WS_APPS_URI_PREFIX = "ws.apps.uri.";
	private final static String WS_MESSAGING_PATH = "ws.messaging.path";
    private final static String WS_RA_CONFIGS_PATH = "ws.ra.configs.path";
    private final static String WS_RA_PARAMS_PATH = "ws.ra.params.path";
    private final static String WS_RA_APPLICATIONS_PATH = "ws.ra.applications.path";
    private final static String WS_LOGS_PATH = "ws.logs.path";

    /**
	 * 
	 * @return
	 */
	public static String getAppUri(String appCode) {
		return getValue(WS_APPS_URI_PREFIX + appCode);
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getUriRa() {
		return getAppUri(getApplicationCodeRA());
	}
		
	/**
	 * 
	 * @return
	 */
	public static String getUriRaConfigs() {
		return getUriRa() + getMessagingPath() + getRaConfigsPath();
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getUriRaParams() {
		return getUriRa() + getMessagingPath() + getRaConfigsPath() + getRaParamsPath();
	}

	/**
	 * 
	 * @return
	 */
	public static String getMessagingPath() {
		return getValue(WS_MESSAGING_PATH);
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getRaConfigsPath() {
		return getValue(WS_RA_CONFIGS_PATH);
	}
		
	/**
	 * 
	 * @return
	 */
	public static String getRaParamsPath() {
		return getValue(WS_RA_PARAMS_PATH);
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getRaApplicationsPath() {
		return getValue(WS_RA_APPLICATIONS_PATH);
	}
	
	
	/**
	 * 
	 * @return
	 */
	public static String getLogsPath() {
		return getValue(WS_LOGS_PATH);
	}

}
