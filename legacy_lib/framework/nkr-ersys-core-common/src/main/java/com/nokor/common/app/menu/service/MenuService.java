package com.nokor.common.app.menu.service;

import java.util.List;

import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.common.app.menu.model.MenuEntity;
import com.nokor.common.app.menu.model.MenuItemEntity;
import com.nokor.frmk.security.model.SecApplication;
import com.nokor.frmk.security.model.SecControl;

/**
 * Menu service functionality to manage menu and menu items.
 * @author prasnar
 */
public interface MenuService extends BaseEntityService {
	public List<MenuEntity> listMenus();
	
    public List<MenuEntity> listMenus(SecApplication app);

    public MenuEntity findMenuByCode(String code);

    public List<MenuItemEntity> findRootMenuItems(MenuEntity menu);
    
	public List<MenuItemEntity> findRootMenuItems(SecApplication app);

	public MenuItemEntity findMenuItemById(Long itemId);

    public MenuItemEntity findMenuItem(SecControl control);

    public List<MenuItemEntity> listMenuItems(SecApplication app);
	
    public MenuItemEntity findMenuItem(SecApplication app, String menuItemCode);

    public List<MenuItemEntity> listMenuItems(SecApplication app, String menuItemCode);

    public MenuItemEntity findMenuItemByAction(String appCode, String eventName);

    /**
	 * For each menu item (of all menus for the SecApplication/appCode)
	 * 		create a control for a given menuItem
	 * For each control (in the SecApplication/appCode), create by default all the privileges  
	 * 		SecControlPrivilege
	 * 			Used only for the display of control's access rights
	 * 			It is not used to store the access rights for a profile (for that case see SecControlProfilePrivilege) 
	 */
	void synchronizeMenu(SecApplication app);
	
	void synchronizeMenu(MenuEntity menu);

	void saveOrUpdateMenu(MenuEntity menu);

	void saveOrUpdateMenuItem(MenuItemEntity item);

	void deleteMenuItem(MenuItemEntity menuItem);

	void deleteMenuItemRecursive(MenuItemEntity item);

}
