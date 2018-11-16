package com.nokor.common.app.tools;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.i18n.I18NUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.common.app.menu.model.MenuItemEntity;
import com.nokor.common.app.tools.helper.AppServicesHelper;
import com.nokor.common.app.tools.helper.AppSettingConfigHelper;
import com.nokor.frmk.helper.FrmkAppConfigFileHelper;
import com.nokor.frmk.security.context.SecApplicationContext;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.security.model.SecApplication;

/**
 * 
 * @author prasnar
 *
 */
public class ApplicationManager implements AppServicesHelper {
    protected static Logger logger = LoggerFactory.getLogger(ApplicationManager.class);

    public static final String KEY_MENU_ITEMS = "@menuItems@";
    public static final String KEY_MENU_ACTION_ITEMS = "@menuActionItems@";
	
	private static SecApplicationContext secAppContext;

	/**
	 * 
	 */
	public static SecApplicationContext getSecApplicationContext() {
		if (secAppContext == null) {
			secAppContext = SecApplicationContextHolder.getContext();
		}
		return secAppContext;
	}

	/**
	 * 
	 * @return
	 */
	public static SecApplication getSecApplication() {
		return getSecApplicationContext().getSecApplication();
	}

	/**
	 * 
	 */
	public static void init() {
		init(true);
	}
	
	/**
	 * 
	 */
	public static void init(boolean isInitWorkflow) {
		getSecApplicationContext();
		
		// Initializing ERefData
        REFDATA_SRV.init();
        
        if (isInitWorkflow) {
	        // Initializing Workflows
	        WKF_SRV.init();
        }
        
        // Initializing Locale
    	Locale locale = AppSettingConfigHelper.getLocale();
    	logger.info("Initializing Locale: " + locale.getLanguage() + " / " + locale.getCountry() + " / " + locale.toLanguageTag());
        secAppContext.setLocale(locale);
        
        // Initializing Languages
    	List<String> languages = FrmkAppConfigFileHelper.getI18nLanguages();
    	logger.info("Initializing Languages: " + languages);
    	if (languages.size() == 0) {
        	languages.add(locale.getLanguage());
    	}

        // Initializing I18N
    	if (FrmkAppConfigFileHelper.getI18nBundlesName() != null && FrmkAppConfigFileHelper.getI18nBundlesName().size() > 0) {
        	logger.info("Init I18N");
    		I18NUtil.init(FrmkAppConfigFileHelper.getI18nBundlesName(), languages, FrmkAppConfigFileHelper.getI18nEncodingSource(), FrmkAppConfigFileHelper.getI18nEncodingTarget());
    		I18N.setApplicationLocale(locale);
    	}
    	
    	getMenuActionItems(true);
    	
	}

	/**
	 * 
	 * @return
	 */
	public static List<MenuItemEntity> getMenuItems() {
		return getMenuItems(false);
	}
	/**
	 * 
	 * @return
	 */
	public static List<MenuItemEntity> getMenuItems(boolean forceReload) {
		List<MenuItemEntity> menuItems = (List<MenuItemEntity>) getSecApplicationContext().getAttribute(KEY_MENU_ITEMS);
		if (menuItems == null || forceReload) {
	    	menuItems = MENU_SRV.listMenuItems(getSecApplication());
	    	setMenuItems(menuItems);
	    	setMenuActionItems(null);
		}
		return menuItems;
	}
	
	/**
	 * 
	 * @param menuItems
	 */
	public static void setMenuItems(List<MenuItemEntity> menuItems) {
		secAppContext.putAttribute(KEY_MENU_ITEMS, menuItems);
	}
	
	/**
	 * 
	 * @return
	 */
	public static Map<String, MenuItemEntity> getMenuActionItems() {
		return getMenuActionItems(false);
	}
	
	/**
	 * 
	 * @return
	 */
	public static Map<String, MenuItemEntity> getMenuActionItems(boolean forceReload) {
		Map<String, MenuItemEntity> mapMenuActionItems = (Map<String, MenuItemEntity>) getSecApplicationContext().getAttribute(KEY_MENU_ACTION_ITEMS);
		if (mapMenuActionItems == null || forceReload) {
			mapMenuActionItems = new HashMap<String, MenuItemEntity>();
			List<MenuItemEntity> menuItems = getMenuItems(true);
	    	for (MenuItemEntity mnuItem : menuItems) {
	    		if (StringUtils.isNotEmpty(mnuItem.getAction())) {
	    			mapMenuActionItems.put(mnuItem.getAction(), mnuItem);
	    		}
			}
	    	setMenuActionItems(mapMenuActionItems);
		}
		return mapMenuActionItems;
	}

	
	/**
	 * 
	 * @param mapMenuActionItems
	 */
	public static void setMenuActionItems(Map<String, MenuItemEntity> mapMenuActionItems) {
    	secAppContext.putAttribute(KEY_MENU_ACTION_ITEMS, mapMenuActionItems);
	}
	
	/**
	 * 
	 */
	public static void flushAll() {
		getSecApplicationContext().clear();
		init();
		
	}
	
	/**
	 * 
	 */
	public static void flush(String key) {
		if (key != null) {
			secAppContext.putAttribute(key, null);
		}
	}
}
