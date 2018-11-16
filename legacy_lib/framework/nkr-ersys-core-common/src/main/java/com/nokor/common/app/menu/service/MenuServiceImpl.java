package com.nokor.common.app.menu.service;


import java.util.List;

import javax.persistence.NonUniqueResultException;

import org.hibernate.criterion.Order;
import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.seuksa.frmk.tools.exception.EntityNotValidParameterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.common.app.menu.model.MenuEntity;
import com.nokor.common.app.menu.model.MenuItemEntity;
import com.nokor.frmk.helper.SeuksaServicesHelper;
import com.nokor.frmk.security.model.ESecControlType;
import com.nokor.frmk.security.model.SecApplication;
import com.nokor.frmk.security.model.SecControl;

/**
 * 
 * @author prasnar
 *
 */
@Service("menuService")
public class MenuServiceImpl extends BaseEntityServiceImpl implements MenuService {
	/** */
	private static final long serialVersionUID = -3635288550191898337L;

	private static final String MENU_NAME_EXT = ".MENU";

	@Autowired
	private EntityDao dao;
    

	@Override
	public BaseEntityDao getDao() {
		return dao;
	}

	@Override
    public List<MenuItemEntity> findRootMenuItems(MenuEntity menu) {
    	MenuItemRestriction restrictions = new MenuItemRestriction();
    	restrictions.setMenuCode(menu.getCode());
    	restrictions.setParentOnly();
    	restrictions.addOrder(Order.asc(MenuItemEntity.SORTINDEX));
    	restrictions.addOrder(Order.asc(MenuItemEntity.ID));
    	List<MenuItemEntity> list = dao.list(restrictions);
    	forceLoadingChildren(list);
    	return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MenuItemEntity> findRootMenuItems(SecApplication app) {
    	MenuItemRestriction restrictions = new MenuItemRestriction();
    	restrictions.setMenuCode(app.getCode() + MENU_NAME_EXT);
    	restrictions.setParentOnly();
    	restrictions.addOrder(Order.asc(MenuItemEntity.SORTINDEX));
    	restrictions.addOrder(Order.asc(MenuItemEntity.ID));
    	List<MenuItemEntity> list = dao.list(restrictions);
    	forceLoadingChildren(list);
    	return list;
    }
    
    /**
     * 
     * @param menuItemEntities
     * @return
     */
    private void forceLoadingChildren(List<MenuItemEntity> menuItemEntities) {
    	if (!menuItemEntities.isEmpty()) {
	    	for (MenuItemEntity menuItemEntity : menuItemEntities) {
	    		forceLoadingChildren(menuItemEntity.getChildren());
			}
    	}
    }
    
    @Override
    public List<MenuItemEntity> listMenuItems(SecApplication app) {
    	MenuItemRestriction restrictions = new MenuItemRestriction();
    	restrictions.setAppCode(app.getCode());
    	List<MenuItemEntity>  lst = dao.list(restrictions);
    	return lst;
    }
    
    @Override
    public MenuItemEntity findMenuItem(SecApplication app, String menuItemCode) {
    	try {
	    	MenuItemRestriction restrictions = new MenuItemRestriction();
	    	restrictions.setAppCode(app.getCode());
	    	restrictions.setMenuItemCode(menuItemCode);
	    	return dao.getUnique(restrictions);
    	} catch (NonUniqueResultException e) {
			throw new IllegalStateException("Application [" + app.getCode() + "] - Found more than 1 menuItemCode [" + menuItemCode + "]");
		}
    }
    
    @Override
    public List<MenuItemEntity> listMenuItems(SecApplication app, String menuItemCode) {
    	MenuItemRestriction restrictions = new MenuItemRestriction();
    	restrictions.setAppCode(app.getCode());
    	restrictions.setMenuItemCode(menuItemCode);
    	return dao.list(restrictions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MenuEntity findMenuByCode(String code) {
        return dao.getByCode(MenuEntity.class, code);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public MenuItemEntity findMenuItemById(Long itemId) {
        return dao.getById(MenuItemEntity.class, itemId);
    }
    
    @Override
    public MenuItemEntity findMenuItem(SecControl control) {
    	MenuItemRestriction restrictions = new MenuItemRestriction();
    	restrictions.setControlId(control.getId());
        return dao.getUnique(restrictions);
    }
    

  
    /**
     * {@inheritDoc}
     */
    @Override
    public MenuItemEntity findMenuItemByAction(String appCode, String actionName) {
    	MenuItemRestriction restrictions = new MenuItemRestriction();
    	restrictions.setAppCode(appCode);
    	if (actionName == null) {
    		restrictions.setActionName("");
    	} else {
    		if (actionName.contains("/")) {
    			actionName = actionName.split("/")[0];
    		}
    		restrictions.setActionName(actionName);
    	}
    	
    	MenuItemEntity mnu = dao.getUnique(restrictions);
    	if (mnu == null) {
    		String errMsg = "Warning - Application [" + appCode + "] The menu action [" + actionName + "] can not be found";
    		logger.warn(errMsg); 
    	}
    	
    	return mnu;
    }

    @Override
	public List<MenuEntity> listMenus() {
		MenuRestriction restrictions = new MenuRestriction();
    	List<MenuEntity>  lst = dao.list(restrictions);
    	return lst;
    }

    
	@Override
	public List<MenuEntity> listMenus(SecApplication app) {
		MenuRestriction restrictions = new MenuRestriction();
    	restrictions.setAppCode(app.getCode());
    	List<MenuEntity>  lst = dao.list(restrictions);
    	return lst;
	}

	@Override
	public void synchronizeMenu(SecApplication app) {
		if (app == null) {
			throw new EntityNotValidParameterException("SecApplication is null");
		}
		
		List<MenuEntity> menus = listMenus(app);
		
		for (MenuEntity menu : menus) {
			synchronizeMenu(menu);
		}
		
		//
//		List<SecControl> controls = SeuksaServicesHelper.SECURITY_SRV.listControls(app, ESecControlType.MENU);
//		delete all SecControl not used neither by MenuEntity neither by MenuEntityItem
	}
	
	@Override
	public void synchronizeMenu(MenuEntity menu) {
		saveOrUpdateMenu(menu);
		List<MenuItemEntity> rootMenuItems = menu.getMenuItems();
		for (MenuItemEntity item : rootMenuItems) {
			saveOrUpdateMenuItem(item);
		}
	}

	@Override
	public void saveOrUpdateMenu(MenuEntity menu) {
		SecControl ctl = menu.getControl();
		if (ctl == null) {
			ctl = SecControl.createInstance(ESecControlType.MENU);
		}
		ctl.setApplication(menu.getApplication());
		ctl.setCode(menu.getCode());
		ctl.setDesc(menu.getDesc());
		ctl.setDescEn(menu.getDescEn());
        dao.saveOrUpdate(ctl);
        menu.setControl(ctl);
        dao.update(menu);

	}

	@Override
	public void saveOrUpdateMenuItem(MenuItemEntity item) {
		if (item == null) {
			return;
		}
		logger.debug("Menu [" + item.getMenu().getCode() + "] - Item [" + item.getId() + "-" + item.getCode() + "] of [" + (item.getParent() != null ? item.getParent().getCode() : "<Root>") + "]");

		SecControl ctl = item.getControl();
		
		// Create only of not exist
		if (ctl == null) {
			ctl = SecControl.createInstance(ESecControlType.MENU);
		}
		
		ctl.setApplication(item.getMenu().getApplication());
		ctl.setCode(item.getCode());
		ctl.setDesc(item.getDesc());
		ctl.setDescEn(item.getDescEn());{
		if (item.getParent() != null) 
			ctl.setParent(item.getParent().getControl());
		}
		ctl.setGroup(item.getMenu().getControl());
		SeuksaServicesHelper.SECURITY_SRV.saveOrUpdateControl(ctl);
		item.setControl(ctl);
		dao.update(item);
		
		// manage its children
		for (MenuItemEntity iteChild : item.getChildren()) {
			saveOrUpdateMenuItem(iteChild);
		}
		
	}
    
    @Override
    public void deleteMenuItem(MenuItemEntity menuItem) {
    	if (menuItem.getControl() != null) {
    		SeuksaServicesHelper.SECURITY_SRV.deleteControl(menuItem.getControl());
    	}
    	
    	dao.delete(menuItem);
    }

	@Override
	public void deleteMenuItemRecursive(MenuItemEntity item) {
		if (item == null) {
			return;
		}
		
		// manage its children
		for (MenuItemEntity iteChild : item.getChildren()) {
			logger.info("----Children control [" + iteChild.getId() + "-" + iteChild.getCode() + "]");
			deleteMenuItemRecursive(iteChild);
		}
				
		try {
			logger.info("Deletion of MenuItemEntity [" + item.getCode() + "]");
			deleteMenuItem(item);
			
		} catch (Exception e) {
			String errMsg = "Error while deleting MenuItemEntity [" + item.getCode() + "]";
			logger.error(errMsg , e);
			throw new IllegalStateException(errMsg, e);
		}
		
	}

}
