package com.nokor.common.app.menu.service;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.common.app.menu.model.EMenuType;
import com.nokor.common.app.menu.model.MenuEntity;
import com.nokor.frmk.security.model.SecApplication;

/**
 * @author prasnar
 * 
 */
public class MenuRestriction extends BaseRestrictions<MenuEntity> {
    /** */
	private static final long serialVersionUID = -7858970276575397791L;

	private String menuCode;
    private String appCode;
	private EMenuType type;
	private Boolean isSynchronized;

	/**
     *
     */
    public MenuRestriction() {
        super(MenuEntity.class);
    }

    /**
     * 
     */
    public void preBuildAssociation() {
    	addAssociation(MenuEntity.APPLICATION);
   }
    
    /**
     * @see org.seuksa.frmk.mvc.dao.hql.BaseRestrictions#preBuildSpecificCriteria()
     */
    @Override
    public void preBuildSpecificCriteria() {

    	if (StringUtils.isNotEmpty(appCode)) {
    		addCriterion(Restrictions.eq(MenuEntity.APPLICATION + DOT + SecApplication.CODE, appCode));
    	}
    	
    	if (menuCode != null) {
    		addCriterion(Restrictions.eq(MenuEntity.CODE, menuCode));
    	} 

    	if (type != null) {
    		addCriterion(Restrictions.eq(MenuEntity.TYPE, type));
    	} 
    	
    	if (isSynchronized != null) {
    		addCriterion(Restrictions.eq(MenuEntity.ISSYNCHRONIZED, isSynchronized));
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
	 * @return the isSynchronized
	 */
	public Boolean getIsSynchronized() {
		return isSynchronized;
	}

	/**
	 * @param isSynchronized the isSynchronized to set
	 */
	public void setIsSynchronized(Boolean isSynchronized) {
		this.isSynchronized = isSynchronized;
	}

}
