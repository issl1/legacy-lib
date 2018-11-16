package com.nokor.common.app.systools;

import com.nokor.frmk.helper.SeuksaAppConfigFileHelper;

/**
 * 
 * @author prasnar
 *
 */
public class SysVersionHelper extends SeuksaAppConfigFileHelper  {
    public final static String APP_NAME = "core.app.name";
    public final static String APP_VERSION_NUMBER = "core.app.version.number";
    public final static String APP_BUILD_NUMBER = "core.app.build.number";
 

	private static final String _DEFAULT_APP_NAME = "_APP_NAME_";
	private static final String DEFAULT_VERSION_NEUMBER = "_VERSION_NEUMBER";
	private static final String _DEFAULT_BUILD_NUMBER = "_BUILD_NUMBER";
	
	protected String appName;
	protected String versionNumber;
	protected String buildNumber;
	
	private static SysVersionHelper appVersion = new SysVersionHelper();


	/**
	 * 
	 */
	public static SysVersionHelper getInstance() {
//		SecApplication app = SecApplicationContextHolder.getContext().getSecApplication();
		return appVersion;
	}
	
	/**
	 * 
	 */
	private SysVersionHelper() {
		
	}
	
	/**
	 * 
	 * @return
	 */
	public String getAppName() {
		appName = getValue(APP_NAME);
		if (appName == null) {
			appName = _DEFAULT_APP_NAME;
		}
		return appName;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getAppVersionNumber() {
		versionNumber = getValue(APP_VERSION_NUMBER);
		if (versionNumber == null) {
			versionNumber = DEFAULT_VERSION_NEUMBER;
		}
		return versionNumber;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getAppBuildNumber() {
		buildNumber = getValue(APP_BUILD_NUMBER);
		if (buildNumber == null) {
			buildNumber = _DEFAULT_BUILD_NUMBER;
		}
		return buildNumber;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getAppFullVersion() {
		return "Name [" + getAppName() + "] - " + "Version [" + getAppVersionNumber() + "] - Build [" + getAppBuildNumber() + "]";
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDBSchemaName() {
		return ENTITY_SRV.getDBSchemaName();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDBDriverName() {
		return ENTITY_SRV.getDBDriverName();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDBUserName() {
		return ENTITY_SRV.getDBUserName();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDBFullInfo() {
		return "Schema [" + getDBSchemaName() + "] - " + "Driver [" + getDBDriverName() + "] - User [" + getDBUserName() + "]";
	}
}
