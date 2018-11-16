package com.nokor.frmk.vaadin.ui.menu;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.slf4j.LoggerFactory;

import com.nokor.common.app.menu.MenuHelper;
import com.nokor.common.app.menu.model.MenuItemEntity;
import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.common.app.tools.helper.AppConfigFileHelper;
import com.nokor.frmk.security.model.SecControlProfilePrivilege;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;

/**
 * @author prasnar
 *
 */
public class VaadinMenuHelper extends MenuHelper {
	private static final String MENU_ICON_STYLE = "menu-icon";
	private static final String TOKEN = "%";
	private static final String MENUBAR_NAME = "------MAIN MENU [" + TOKEN + "]------";
	private static final String _CR = "\r\n";
	private static final String _BULLET_1 = "#";
	private static final String _BULLET_2 = "	+";
	private static final String _BULLET_3 = "	-";
	private static final String _BULLET_4 = "		.";
	private static final String _BULLET_5 = "			.";
	private static final List<String> _BULLETS = Arrays.asList( _BULLET_1, _BULLET_2, _BULLET_3, _BULLET_4, _BULLET_5 );
	private static final String _SEP = "-";
	private static String menuDisplay;
	
    /**
     * Find Parent Menu
     * @param menuBar
     * @param menuItemEntity
     * @return MenuItem
     */
    public static MenuItem findParentMenu(MenuBar menuBar, MenuItemEntity menuItemEntity) {
    	if (menuItemEntity == null || menuBar == null) {
    		return null;
    	}
    	
    	MenuItem parent = null;
    	
		List<MenuItem> menuItems = menuBar.getItems();
		String menuName = "";
		if (AppConfigFileHelper.isUsedMenuDescToDisplay()) {
			menuName = I18N.message(menuItemEntity.getDesc());
		} else {
			menuName = I18N.message(menuItemEntity.getCode());
		}
		
		if (menuItems != null && menuItems.size() > 0) {
    		for (MenuItem menuItem : menuItems) {
    			if (menuItem.getText().equals(menuName) 
    					|| validateMenuNameInParentMenu(menuItem, menuName)) {
    				parent = menuItem;
    				break;
    			}
    		}
    	}
    	
    	return parent;
    }
    
    /**
     * Validate Menu Name In Parent Menu
     * @param parentMenu
     * @param menuName
     * @return boolean
     */
    private static boolean validateMenuNameInParentMenu(MenuItem parentMenu, String menuName) {
    	boolean isValid = false;
    	List<MenuItem> menuItems = parentMenu.getChildren();
    	
    	if (menuItems != null && menuItems.size() > 0) {
    		for (MenuItem menuItem : menuItems) {
    			if (menuItem.getText().equals(menuName)) {
    				isValid = true;
    				break;
    			}
    			else if (menuItem.getChildren() != null 
    					&& menuItem.getChildren().size() > 0) {
    				isValid = validateMenuNameInParentMenu(menuItem, menuName);
    				
    				if (isValid) {
    					break;
    				}
    			}
    		}
    	}
    	return isValid;
    }

    /**
     * 
     * @return
     */
    public static String displayMenuBar() {
    	return menuDisplay;
    }

    /**
     * 
     * @param menuItems
     * @return
     */
    public static MenuBar buildMenuBar(List<MenuItemEntity> menuItems, List<SecControlProfilePrivilege> privileges) {
    	if (menuItems == null || menuItems.isEmpty()) {
    		return null;
    	}

       	boolean isSysAdmin = UserSessionManager.getCurrentUser().isSysAdmin();
    	boolean isEnabledUserMenuProfile = AppConfigFileHelper.isEnabledUserMenuProfile();

    	if (!isSysAdmin && !isEnabledUserMenuProfile
        	&& (privileges == null || privileges.isEmpty())) {
    		return null;
    	}
    	
//    	menuDisplay = _CR + MENUBAR_NAME.replace(TOKEN, ApplicationManager.getSecApplication().getCode()) + _CR;
    	MenuBar menuBar = addMenuItems(1, null, menuItems, false, privileges);
	   	
        return menuBar;
    }
   
    /**
     * Add sub menus
     * @param uiMenuItem
     * @param menuItems
     * @param visibleChildrenParam
     */
    private static MenuBar addMenuItems(int level, MenuItem uiMenuItem, List<MenuItemEntity> menuItems, boolean visibleChildrenParam, List<SecControlProfilePrivilege> privileges) {
    	MenuBar menuBar = null;
    	if (menuItems == null) {
    		return null;
    	}
    	
        for (MenuItemEntity menuItem : menuItems) {
        	if (!menuItem.isActive()) {
        		continue;
        	}
        	
    		boolean menuVisible = visibleChildrenParam || isVisibleMenu(menuItem, privileges);
    		boolean visibleChildren = false; //menuVisible;
    		
    		if (!menuVisible) {
    			continue;
    		}
	            
			String name = menuItem.getCode();
//			menuDisplay += _BULLETS.get(level + 1) + (menuItem.getId() != null ? menuItem.getId() : 0) + _SEP + menuItem.getCode() + _CR;
			
			MenuItem newUiMenuItem = null;
			
			if (name.equals(MENU_SEPARATOR)) {
                uiMenuItem.addSeparator();
            } else {
            	Command command = null;
                if (menuItem.getChildren().isEmpty()) {
                    command = CommandFactory.create(menuItem);
                }
                
                if (AppConfigFileHelper.isUsedMenuDescToDisplay()) {
                	name = menuItem.getDesc();
    			}
                
            	if (uiMenuItem == null) {
            		if (menuBar == null) {
            			menuBar = new MenuBar();
            		}
    				newUiMenuItem = menuBar.addItem(I18N.message(name), command);
    			} else {
    				newUiMenuItem = uiMenuItem.addItem(I18N.message(name), command);
    			}
                
                drawIcon(menuItem, newUiMenuItem);

                addMenuItems(level + 1, newUiMenuItem, menuItem.getChildren(), visibleChildren, privileges);
                
            }
        	
        }
        
		return menuBar;
    }

    /**
     * 
     * @param mnuItem
     * @param uiMenuItem
     */
    private static void drawIcon(MenuItemEntity mnuItem, MenuItem uiMenuItem) {
    	if (StringUtils.isEmpty(mnuItem.getIconPath())) {
    		return;
    	}
		String strFontAwesome = "";
		try {
        	if (AppConfigFileHelper.isFontAwesomeIcon()) {
        		strFontAwesome = mnuItem.getIconPath();
            	FontAwesome font = FontAwesome.valueOf(strFontAwesome);
            	uiMenuItem.setStyleName(MENU_ICON_STYLE);
            	uiMenuItem.setIcon(font);
        	} else {
        		uiMenuItem.setIcon(new ThemeResource(mnuItem.getIconPath()));
        	}
		} catch (Exception e) {
    		LoggerFactory.getLogger(VaadinMenuHelper.class).warn("The IconPath" + (strFontAwesome != null ? strFontAwesome: "<null>") + " can not be found.");
    	}
        
    }
    
}
