package com.nokor.common.app.menu;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.common.app.menu.model.MenuEntity;
import com.nokor.common.app.menu.model.MenuItemEntity;
import com.nokor.common.app.tools.ApplicationManager;
import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.common.app.tools.helper.AppConfigFileHelper;
import com.nokor.common.app.tools.helper.AppServicesHelper;
import com.nokor.frmk.security.model.SecControl;
import com.nokor.frmk.security.model.SecControlProfilePrivilege;

/**
 * @author prasnar
 *
 */
public class MenuHelper implements AppServicesHelper {
    protected static Logger logger = LoggerFactory.getLogger(MenuHelper.class);
	public static final String MENU_SEPARATOR = "menu.separator";
	public static final String SLASH = "/";

	/**
	 * 
	 */
	public static void synchronizeMenus() {
		List<MenuEntity> menus = MENU_SRV.listMenus();
		for (MenuEntity menu : menus) {
			MENU_SRV.synchronizeMenu(menu);
		}
	}
	
    /**
     * 
     * @param menuItem
     * @param privileges
     * @return
     */
    public static boolean isVisibleMenu(MenuItemEntity menuItem, List<SecControlProfilePrivilege> privileges) {
    	boolean isSysAdmin = UserSessionManager.getCurrentUser().isSysAdmin();
    	boolean isEnabledUserMenuProfile = AppConfigFileHelper.isEnabledUserMenuProfile();

    	if (isSysAdmin || isEnabledUserMenuProfile) {
    		return true;
    	}
    	if (menuItem == null || privileges == null) {
			return false;
		}
    	
    	return isVisibleMenu(Arrays.asList(menuItem), privileges);
    }
    
   /**
    * 
    * @param menuItemEntities
    * @param privileges
    * @return True if one of menuItemEntities is visible; else False
    */
    private static boolean isVisibleMenu(List<MenuItemEntity> menuItemEntities, List<SecControlProfilePrivilege> privileges) {
    	if (privileges == null || menuItemEntities == null) {
    		return false;
    	}
    	for (MenuItemEntity menu: menuItemEntities) {
			if (menu.getControl() != null && hasPrivilegeOnControl(privileges, menu.getControl())) {
				return true;
			}
			return isVisibleMenu(menu.getChildren(), privileges);
		}
		return false;
    }
    
    /**
     * 
     * @param privileges
     * @param control
     * @return
     */
    public static boolean hasPrivilegeOnControl(List<SecControlProfilePrivilege> privileges, SecControl control) {
    	if (privileges == null || control == null) {
    		return false;
    	}
		for (SecControlProfilePrivilege privilege: privileges) {
			try {
				if (control.getId().equals(privilege.getControl().getId())) {
					return true;
				}
			} catch (Exception e) {
				logger.error("**********ERROR hasPrivilegeOnControl*********", e);
				throw new IllegalStateException(e);
			}
		}
		return false;
    }
    /**
     * 
     * @param actionName
     * @return
     */
    public static MenuItemEntity findByAction(String actionName) {
    	Map<String, MenuItemEntity> mapMenuActionItems = ApplicationManager.getMenuActionItems();
		if (actionName.contains(SLASH)) {
			actionName = actionName.split(SLASH)[0];
		}
		MenuItemEntity menuItem = mapMenuActionItems.get(actionName);
		if (menuItem == null) {
			logger.info("The action [" + actionName + "] can not be found in the MapMenuActionItems");
		}
		return menuItem;

    }

    /**
     * 
     * @param actionName
     * @param privileges
     * @return
     */
    public static boolean isAllowedMenuAction(String actionName, List<SecControlProfilePrivilege> privileges) {
    	MenuItemEntity menuItem = findByAction(actionName);
    	return isVisibleMenu(menuItem, privileges);
    }


}
