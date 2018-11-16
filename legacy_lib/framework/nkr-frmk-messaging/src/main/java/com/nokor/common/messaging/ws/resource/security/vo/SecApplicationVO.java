package com.nokor.common.messaging.ws.resource.security.vo;

import java.util.ArrayList;
import java.util.List;

import com.nokor.common.messaging.share.BaseEntityRefDTO;

/**
 * 
 * @author prasnar
 *
 */
public class SecApplicationVO extends BaseEntityRefDTO {

	/** */
	private static final long serialVersionUID = 8207574243740311179L;

	private List<SecUserVO> users = new ArrayList<>();
	private List<SecProfileVO> profiles = new ArrayList<>();
	private List<MenuItemAccessVO> controlsAccess = new ArrayList<>();
	private List<MenuVO> menus = new ArrayList<>();
	
	/**
	 * 
	 */
	public SecApplicationVO() {
		
	}

	/**
	 * @return the users
	 */
	public List<SecUserVO> getUsers() {
		return users;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(List<SecUserVO> users) {
		this.users = users;
	}

	/**
	 * @return the profiles
	 */
	public List<SecProfileVO> getProfiles() {
		return profiles;
	}

	/**
	 * @param profiles the profiles to set
	 */
	public void setProfiles(List<SecProfileVO> profiles) {
		this.profiles = profiles;
	}

	/**
	 * @return the controls
	 */
	public List<MenuItemAccessVO> getControlsAccess() {
		return controlsAccess;
	}

	/**
	 * @param controlsAccess the controls to set
	 */
	public void setControlsAccess(List<MenuItemAccessVO> controlsAccess) {
		this.controlsAccess = controlsAccess;
	}

	/**
	 * @return the menus
	 */
	public List<MenuVO> getMenus() {
		return menus;
	}

	/**
	 * @param menus the menus to set
	 */
	public void setMenus(List<MenuVO> menus) {
		this.menus = menus;
	}

	/**
	 * 
	 * @param menuItems
	 */
	public void initMenu(List<Object[]> menuItems) {
		menus.add(new MenuVO());
		menus.get(0).initItems(menuItems);
	}

	/**
	 * 
	 * @param proCode
	 * @return
	 */
	public SecProfileVO getProfile(String proCode) {
		for (SecProfileVO pro : profiles) {
			if (pro.getCode().equals(proCode)) {
				return pro;
			}
		}
		return null;
	}
}
