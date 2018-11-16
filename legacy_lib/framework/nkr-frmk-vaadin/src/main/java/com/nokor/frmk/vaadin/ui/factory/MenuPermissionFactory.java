/*
 * Created on 24/03/2015.
 */
package com.nokor.frmk.vaadin.ui.factory;

import com.nokor.common.app.menu.model.MenuItemEntity;

/**
 * Menu Permission Factory
 * 
 * @author pengleng.huot
 *
 */
public interface MenuPermissionFactory {

	/**
	 * Check allowed menu to display in menu-bar.
	 * 
	 * @param menuItem: menu to check.
	 * @return Display menu if value is true.
	 */
	boolean allowTopMenu(MenuItemEntity menuItem);
	
	/**
	 * Check allowed sub menu to display in menu-bar.
	 * 
	 * @param subMenuItem: sub menu to check.
	 * @return Display menu if value is true.
	 */
	boolean allowSubMenu(MenuItemEntity subMenuItem);
}
