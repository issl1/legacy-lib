package com.nokor.frmk.vaadin.util;

import com.nokor.frmk.config.AppConfigFile;
import com.nokor.frmk.helper.SeuksaAppConfigFileHelper;


/**
 * 
 * @author prasnar
 * @version $Revision$
 *
 */
public class VaadinConfigHelper extends SeuksaAppConfigFileHelper  {
	

	public static final String DEFAULT_MAIN_WIDTH = "550px";
	public static final String DEFAULT_MAIN_HEIGHT = "350px";
	
    public final static String APP_LOGO_POSITION = "app.logo.position";

    public final static String APP_VAADIN_LOGIN_PANEL_WIDTH = "app.vaadin.login.panel.width";
    public final static String APP_VAADIN_LOGIN_PANEL_HEIGHT = "app.vaadin.login.panel.width";
    public final static String APP_VAADIN_LAYOUT_FOLDER = "core.app.vaadin.layout.folder";
 
	/**
	 * Get App logo position.<br/>
	 * Property key [app.log.position].
	 * @return
	 * - 0: display in top panel<br/>
	 * - 1: display in left panel
	 */
	public static int getAppLogoPosition() {
		return AppConfigFile.getInstance().getValueInt(APP_LOGO_POSITION);
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getLoginPanelWidth() {
		return getValue(APP_VAADIN_LOGIN_PANEL_WIDTH, DEFAULT_MAIN_WIDTH);
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getLoginPanelHeight() {
		return getValue(APP_VAADIN_LOGIN_PANEL_HEIGHT, DEFAULT_MAIN_HEIGHT);
	}
		
	/**
	 * 
	 * @return
	 */
	public static String getLayoutFolder() {
		return getValue(APP_VAADIN_LAYOUT_FOLDER);
	}
}
