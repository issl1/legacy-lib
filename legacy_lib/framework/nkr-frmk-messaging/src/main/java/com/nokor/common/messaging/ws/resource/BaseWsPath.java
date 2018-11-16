package com.nokor.common.messaging.ws.resource;


/**
 * 
 * @author prasnar
 *
 */
public interface BaseWsPath {
	final static String PATH_ID = "/{id}";
	final static String ID = "id";
	final static String WKF_STATUS = "wkfStatus";

	final static String PATH_LOGS = "/logs"; //MessagingConfigHelper.getLogsPath();
	final static String PATH_CONFIGS = "/configs"; //MessagingConfigHelper.getRaConfigsPath();
	final static String PATH_PARAMS = "/params"; //MessagingConfigHelper.getRaParamsPath();
	final static String PATH_APPLICATIONS = "/applications"; //MessagingConfigHelper.getRaApplicationsPath();
	
	final static String SRV_FLUSH_ALL_APPS = "/flushAllApps";
	final static String SRV_FLUSH_BY_ATTRIBUTE_KEY = "/flushByAttributeKey";
	final static String SRV_FLUSH_ALL = "/flushAll";
	final static String SRV_FLUSH_REFDATA = "/flushRefdata";
	final static String SRV_FLUSH_REFDATA_IN_APP = "/flushRefdataInApp";
	final static String SRV_FLUSH_REFDATA_IN_ALL_APPS = "/flushRefdataInAllApps";

	final static String PATH_SECURITY = "/security";
	final static String PATH_MENUS = "/menus";
	final static String SYNCHRONIZE_MENU = "/synchronizeMenu";
	
	final static String INIT_APPLICATION = "/initApplication";
	final static String INIT_PROFILES_MENU = "/initProfilesMenus";
	final static String ADD_CONTROL_TO_PROFILE = "/addAccessControlToProfile";
	final static String ADD_MANAGED_PROFILE_TO_PROFILE = "/addManagedProfileToProfile";
	final static String INIT_ADMIN_MANAGE_ALL_NOT_ADMINP_ROFILES = "/initAdminManageAllNotAdminProfiles";

}
