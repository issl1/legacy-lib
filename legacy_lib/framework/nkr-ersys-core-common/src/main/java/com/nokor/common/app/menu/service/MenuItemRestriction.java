package com.nokor.common.app.menu.service;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.common.app.menu.model.MenuEntity;
import com.nokor.common.app.menu.model.MenuItemEntity;
import com.nokor.frmk.security.model.SecApplication;
import com.nokor.frmk.security.model.SecControl;

/**
 * @author prasnar
 * 
 */
public class MenuItemRestriction extends BaseRestrictions<MenuItemEntity> {
    /** */
	private static final long serialVersionUID = 5960720212676853671L;

    private String menuCode;
    private String menuItemCode;
	private Long appId;
    private String appCode;
    private String actionName;
    private Long parentId;
    private Boolean parentOnly;
    private Long controlId;


	/**
     *
     */
    public MenuItemRestriction() {
        super(MenuItemEntity.class);
    }

    /**
     * 
     */
    public void preBuildAssociation() {
    	addAssociation(MenuItemEntity.MENU, "mnu", JoinType.INNER_JOIN);
    	addAssociation("mnu" + DOT + MenuEntity.APPLICATION, "app", JoinType.INNER_JOIN);
    }
    
    /**
     * @see org.seuksa.frmk.mvc.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
     */
    @Override
    public void preBuildSpecificCriteria() {

    	if (appId != null) {
    		addCriterion(Restrictions.eq("app" + DOT + SecApplication.ID, appId));
    	} else if (StringUtils.isNotEmpty(appCode)) {
    		addCriterion(Restrictions.eq("app" + DOT + SecApplication.CODE, appCode));
    	}
    	
    	if (menuCode != null) {
    		addCriterion(Restrictions.eq("mnu" + DOT + MenuEntity.CODE, menuCode));
    	} 

    	if (menuItemCode != null) {
    		addCriterion(Restrictions.eq(MenuItemEntity.CODE, menuItemCode));
    	} 
    	
    	if (parentId != null) {
    		addCriterion(Restrictions.eq(MenuItemEntity.PARENT + DOT + MenuItemEntity.ID, parentId));
    	} else if (Boolean.TRUE.equals(parentOnly)) {
			addCriterion(Restrictions.isNull("parent"));
    	}
    	
    	if (controlId != null) {
    		addCriterion(Restrictions.eq(MenuItemEntity.CONTROL + DOT + SecControl.ID, controlId));
    	} 
    	
    	if (StringUtils.isNotEmpty(actionName)) {
    		addCriterion(Restrictions.eq(MenuItemEntity.ACTION, actionName));
    	}
    	
    }

	/**
	 * @return the appCode
	 */
	public String getAppCode() {
		return appCode;
	}

	/**
	 * @param appCode the appCode to set
	 */
	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	/**
	 * @return the appId
	 */
	public Long getAppId() {
		return appId;
	}

	/**
	 * @param appId the appId to set
	 */
	public void setAppId(Long appId) {
		this.appId = appId;
	}

	/**
	 * @return the actionName
	 */
	public String getActionName() {
		return actionName;
	}

	/**
	 * @param actionName the actionName to set
	 */
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	/**
	 * @return the menuCode
	 */
	public String getMenuCode() {
		return menuCode;
	}

	/**
	 * @param menuCode the menuCode to set
	 */
	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	/**
	 * @return the menuItemCode
	 */
	public String getMenuItemCode() {
		return menuItemCode;
	}

	/**
	 * @param menuItemCode the menuItemCode to set
	 */
	public void setMenuItemCode(String menuItemCode) {
		this.menuItemCode = menuItemCode;
	}

	/**
	 * @return the parentId
	 */
	public Long getParentId() {
		return parentId;
	}

	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return the parentOnly
	 */
	public Boolean getParentOnly() {
		return parentOnly;
	}

	/**
	 * @param parentOnly the parentOnly to set
	 */
	public void setParentOnly() {
		this.parentOnly = true;
	}



    /**
	 * @return the controlId
	 */
	public Long getControlId() {
		return controlId;
	}

	/**
	 * @param controlId the controlId to set
	 */
	public void setControlId(Long controlId) {
		this.controlId = controlId;
	}    
}
