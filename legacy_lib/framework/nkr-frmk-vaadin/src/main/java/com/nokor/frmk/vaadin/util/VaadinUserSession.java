package com.nokor.frmk.vaadin.util;

import java.util.List;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.common.app.menu.model.MenuItemEntity;
import com.nokor.common.app.tools.ApplicationManager;
import com.nokor.common.app.web.session.AppUserSession;
import com.nokor.frmk.security.model.SecControlProfilePrivilege;
import com.nokor.frmk.vaadin.ui.menu.VaadinMenuHelper;
import com.vaadin.ui.MenuBar;

/**
 * 
 * @author prasnar
 *
 */
public class VaadinUserSession extends AppUserSession implements VaadinSessionKeys {
	/** */
	private static final long serialVersionUID = 1817615498981251357L;

	/**
	 * 
	 */
	public VaadinUserSession() {
	}
	
	
	/**
     * 
     */
    @Override
    public void start() {
    	super.start();
    }
    
    /**
     * 
     */
    @Override
    public void end() {
    	super.end();
    }
    
    /**
	 * 
	 * @return
	 */
	public List<MenuItemEntity> getRootMenuItems() {
		List<MenuItemEntity> rootMenuItems = (List<MenuItemEntity>) attributes.get(KEY_ROOT_MENU_ITEMS);
		if (rootMenuItems == null) {
			rootMenuItems = MENU_SRV.findRootMenuItems(ApplicationManager.getSecApplication());
			setRootMenuItems(rootMenuItems);
		}
		return rootMenuItems;
	}
	
	/**
	 * 
	 * @param rootMenuItems
	 */
	public void setRootMenuItems(List<MenuItemEntity> rootMenuItems) {
		attributes.put(KEY_ROOT_MENU_ITEMS, rootMenuItems);
	}

	/**
	 * 
	 * @return
	 */
	private String getMainMenuBarKey() {
		return KEY_MAIN_MENU_BAR + I18N.getLocale().toString();
	}
	
	/**
	 * 
	 * @return
	 */
	public MenuBar getMainMenuBar() {
		MenuBar mainMenuBar = (MenuBar) attributes.get(getMainMenuBarKey());
		if (mainMenuBar == null) {
			List<SecControlProfilePrivilege> privileges = getControlProfilePrivileges();
	    	List<MenuItemEntity> menuItems = getRootMenuItems();
			mainMenuBar = VaadinMenuHelper.buildMenuBar(menuItems, privileges);
		}
		return mainMenuBar;
	}
	
	/**
	 * 
	 * @param mainMenuBar
	 */
	public void setMainMenuBar(MenuBar mainMenuBar) {
		setControlProfilePrivileges(null);
		setRootMenuItems(null);
		attributes.put(getMainMenuBarKey(), mainMenuBar);
	}
}
