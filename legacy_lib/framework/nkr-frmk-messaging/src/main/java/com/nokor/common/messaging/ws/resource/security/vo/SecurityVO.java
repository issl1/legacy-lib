package com.nokor.common.messaging.ws.resource.security.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.tools.exception.EntityNotFoundException;

/**
 * 
 * @author prasnar
 *
 */
public class SecurityVO implements Serializable {
	/** */
	private static final long serialVersionUID = 4344958876582019932L;

	private List<SecApplicationVO> applications = new ArrayList<>();
	private List<SecUserVO> users = new ArrayList<>();
	private List<SecManagedProfileVO> managedProfiles = new ArrayList<>();
	private RefTopicTableVO topicTableVO = new RefTopicTableVO();
	private List<TabVO> tabs = new ArrayList<>();
	
	/**
	 * 
	 */
	public SecurityVO() {
		
	}

	/**
	 * @return the applications
	 */
	public List<SecApplicationVO> getApplications() {
		return applications;
	}

	/**
	 * @param applications the applications to set
	 */
	public void setApplications(List<SecApplicationVO> applications) {
		this.applications = applications;
	}
	
	/**
	 * 
	 * @param application
	 */
	public void addApplication(SecApplicationVO application) {
		this.applications.add(application);
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
	 * 
	 * @param user
	 */
	public void addUser(SecUserVO user) {
		this.users.add(user);
	}
	
	/**
	 * @return the managedProfiles
	 */
	public List<SecManagedProfileVO> getManagedProfiles() {
		return managedProfiles;
	}

	/**
	 * @param managedProfiles the managedProfiles to set
	 */
	public void setManagedProfiles(List<SecManagedProfileVO> managedProfiles) {
		this.managedProfiles = managedProfiles;
	}
	
	/**
	 * 
	 * @param managedProfile
	 */
	public void addManagedProfile(SecManagedProfileVO managedProfile) {
		this.managedProfiles.add(managedProfile);
	}
	
	/**
	 * 
	 * @param appCode
	 * @return
	 */
	public SecApplicationVO getApplication(String appCode) {
		for (SecApplicationVO app : applications) {
			if (app.getCode().equals(appCode)) {
				return app;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param proCode
	 * @return
	 */
	public SecProfileVO existsProfile(String proCode) {
		for (SecApplicationVO app : applications) {
			SecProfileVO pro = app.getProfile(proCode);
			if (pro != null) {
				return pro;
			}
		}
		return null;
	}
	
	/** -----------------------------*/
	
	/**
	 * 
	 * @param lstApps
	 */
	public void initApplications(List<String> lstApps) {
		applications = new ArrayList<>();
		for (String appCode : lstApps) {
			SecApplicationVO appVO = new SecApplicationVO();
			appVO.setCode(appCode);
			applications.add(appVO);
		}
	}

	/**
	 * 
	 */
	public void initUsers(List<String[]> lstUsers) {
		users = new ArrayList<>();
		
		for (String[] usr : lstUsers) {
			SecUserVO userVO = new SecUserVO();
			userVO.setProCode(usr[0]);
			userVO.setLogin(usr[1]);
			userVO.setDesc(usr[2]);
			userVO.setPwd(usr[3]);
			
			if (StringUtils.isEmpty(userVO.getProCode())
				|| StringUtils.isEmpty(userVO.getLogin())
				|| StringUtils.isEmpty(userVO.getDesc())
				|| StringUtils.isEmpty(userVO.getPwd())
					) {
				throw new IllegalStateException("SecUserVO - null value is not accepted.");
			}
			if (existsProfile(userVO.getProCode()) == null) {
				throw new EntityNotFoundException("SecProfileVO [" + userVO.getProCode() + "]");
			}
			users.add(userVO);
		}
	}

	/**
	 * 
	 * @param parentProCode
	 * @param lstPro
	 */
	public void initManagedProfiles(String parentProCode, List<String[]> lstPro) {
		managedProfiles = new ArrayList<>();
		for (String[] pro : lstPro) {
			SecManagedProfileVO manPro = new SecManagedProfileVO();
			manPro.setParentProCode(parentProCode);
			manPro.setChildProCode(pro[0]);
		}
	}

	/**
	 * 
	 * @param appCode
	 * @param profiles
	 */
	public void initProfiles(String appCode, List<String[]> profiles) {
		SecApplicationVO app = getApplication(appCode);
		if (app == null) {
			throw new IllegalStateException("ApplicationVO [" + appCode + "] not found.");
		}
		app.setProfiles(new ArrayList<>());
		for (String[] pro : profiles) {
			SecProfileVO proVO = new SecProfileVO();
			proVO.setCode(pro[0]);
			proVO.setDesc(pro[1]);
			app.getProfiles().add(proVO);
		}
	}
	
	/**
	 * 
	 * @param appCode
	 * @param lstProPrivVO
	 */
	public void initControlsProfiles(String appCode, List<Object[]> lstProPrivVO) {
		SecApplicationVO app = getApplication(appCode);
		if (app == null) {
			throw new IllegalStateException("ApplicationVO [" + appCode + "] not found.");
		}

		int HAS_RIGHT = 1;
		int COL_MENU_ITEM_ID = 0;
		int COL_PROFILE_FIRST = 2;

		// The header row contains the profiles'list
		// For each profile, check whether it exists or not
		Object[] rowHeaderVO = lstProPrivVO.get(0);
		List<SecProfileVO> profiles = new ArrayList<>();
		for (int iCol = COL_PROFILE_FIRST; iCol < rowHeaderVO.length; iCol++) {
			String proCode = (String) rowHeaderVO[iCol];
			SecProfileVO proVO = new SecProfileVO();
			proVO.setCode(proCode);
			profiles.add(proVO);
		}
		 
		app.setControlsAccess(new ArrayList<>());
		for (int iRow = 1; iRow < lstProPrivVO.size(); iRow++) {
			Object[] currentRowVO = lstProPrivVO.get(iRow);
			
			long menuItemId = Long.valueOf("" + currentRowVO[COL_MENU_ITEM_ID]);
			MenuItemAccessVO menuItemAccess = new MenuItemAccessVO();
			
			menuItemAccess.setId(menuItemId);
			menuItemAccess.setProfilesAccesses(new HashMap<>());
			
			for (int iCol = COL_PROFILE_FIRST; iCol < currentRowVO.length; iCol++) {
				String proCode = (String) rowHeaderVO[iCol];
				boolean hasAccess =  (int)currentRowVO[iCol] == HAS_RIGHT;
				menuItemAccess.getProfilesAccesses().put(proCode, hasAccess);
			}
			app.getControlsAccess().add(menuItemAccess);
		}
	}
	
	/**
	 * 
	 */
	public void initTableTopics(List<String[]> lstTopic, Map<String, List<String>> mapTableTopic, String appCode, String parentCtlCode) {
		topicTableVO = new RefTopicTableVO();
		List<RefTopicVO> topicTables = new ArrayList<>();
		
		for (String[] topic : lstTopic) {
			RefTopicVO topicVO = new RefTopicVO();
			topicVO.setParentCode(topic[0]);
			topicVO.setCode(topic[1]);
			topicVO.setDesc(topic[2]);
			List<String> tableCodes = mapTableTopic.get(topicVO.getCode());
			topicVO.setTableCodes(tableCodes);
			topicTables.add(topicVO);
		}
		topicTableVO.setAppCode(appCode);
		topicTableVO.setParentCtlCode(parentCtlCode);
		topicTableVO.setTopicTables(topicTables);
	}
	
	/**
	 * 
	 * @param lstTab
	 * @param appCode
	 * @param parentCtlCode
	 */
	public void initTabs(List<String[]> lstTab, String appCode, String parentCtlCode) {
		tabs = new ArrayList<>();
		
		for (String[] tab : lstTab) {
			TabVO tabVO = new TabVO();
			tabVO.setAppCode(appCode);
			tabVO.setParentCode(parentCtlCode);
			tabVO.setCode(tab[0]);
			tabVO.setDesc(tab[1]);
			tabs.add(tabVO);
		}
	}

	/**
	 * @return the topicTableVO
	 */
	public RefTopicTableVO getTopicTableVO() {
		return topicTableVO;
	}

	/**
	 * @param topicTableVO the topicTableVO to set
	 */
	public void setTopicTableVO(RefTopicTableVO topicTableVO) {
		this.topicTableVO = topicTableVO;
	}

	/**
	 * @return the tabs
	 */
	public List<TabVO> getTabs() {
		return tabs;
	}

	/**
	 * @param tabs the tabs to set
	 */
	public void setTabs(List<TabVO> tabs) {
		this.tabs = tabs;
	}

	/**
	 * 
	 * @param appCode
	 * @param menuItems
	 */
	public void initMenu(String appCode, List<Object[]> menuItems) {
		SecApplicationVO app = getApplication(appCode);
		if (app == null) {
			throw new IllegalStateException("ApplicationVO [" + appCode + "] not found.");
		}
		app.initMenu(menuItems);
	}
}
