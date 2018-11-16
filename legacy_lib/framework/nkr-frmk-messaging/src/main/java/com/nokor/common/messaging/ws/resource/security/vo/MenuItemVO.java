package com.nokor.common.messaging.ws.resource.security.vo;

import com.nokor.common.messaging.share.BaseEntityRefDTO;

/**
 * 
 * @author prasnar
 *
 */
public class MenuItemVO extends BaseEntityRefDTO {
	/** */
	private static final long serialVersionUID = -3167616348262007689L;
	
	private Long menuId;
	private Long parentItemId;
	private String action;
	private Boolean isPopup;
	private String iconPath;

	
	
	/**
	 * 
	 */
	public MenuItemVO() {
		
	}


	/**
	 * @return the menuId
	 */
	public Long getMenuId() {
		return menuId;
	}


	/**
	 * @param menuId the menuId to set
	 */
	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}


	/**
	 * @return the parentItemId
	 */
	public Long getParentItemId() {
		return parentItemId;
	}


	/**
	 * @param parentItemId the parentItemId to set
	 */
	public void setParentItemId(Long parentItemId) {
		this.parentItemId = parentItemId;
	}


	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}


	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}


	/**
	 * @return the isPopup
	 */
	public Boolean getIsPopup() {
		return isPopup;
	}


	/**
	 * @param isPopup the isPopup to set
	 */
	public void setIsPopup(Boolean isPopup) {
		this.isPopup = isPopup;
	}


	/**
	 * @return the iconPath
	 */
	public String getIconPath() {
		return iconPath;
	}


	/**
	 * @param iconPath the iconPath to set
	 */
	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	
}
