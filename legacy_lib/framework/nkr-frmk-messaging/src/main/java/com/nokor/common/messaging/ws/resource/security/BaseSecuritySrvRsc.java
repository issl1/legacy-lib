package com.nokor.common.messaging.ws.resource.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.tools.exception.EntityAlreadyExistsException;
import org.seuksa.frmk.tools.exception.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.nokor.common.app.menu.model.MenuItemEntity;
import com.nokor.common.app.tools.ApplicationManager;
import com.nokor.common.messaging.ws.resource.cfg.BaseConfigSrvRsc;
import com.nokor.common.messaging.ws.resource.cfg.tools.ApplicationManagerSrvRsc;
import com.nokor.common.messaging.ws.resource.security.vo.MenuItemAccessVO;
import com.nokor.common.messaging.ws.resource.security.vo.MenuItemVO;
import com.nokor.common.messaging.ws.resource.security.vo.MenuVO;
import com.nokor.common.messaging.ws.resource.security.vo.RefTopicTableVO;
import com.nokor.common.messaging.ws.resource.security.vo.RefTopicVO;
import com.nokor.common.messaging.ws.resource.security.vo.SecApplicationVO;
import com.nokor.common.messaging.ws.resource.security.vo.SecProfileVO;
import com.nokor.common.messaging.ws.resource.security.vo.SecUserVO;
import com.nokor.common.messaging.ws.resource.security.vo.SecurityVO;
import com.nokor.common.messaging.ws.resource.security.vo.TabVO;
import com.nokor.frmk.config.model.RefTable;
import com.nokor.frmk.config.model.RefTopic;
import com.nokor.frmk.helper.SeuksaAppConfigFileHelper;
import com.nokor.frmk.security.SecurityHelper;
import com.nokor.frmk.security.model.ESecControlType;
import com.nokor.frmk.security.model.ESecPrivilege;
import com.nokor.frmk.security.model.SecApplication;
import com.nokor.frmk.security.model.SecControl;
import com.nokor.frmk.security.model.SecManagedProfile;
import com.nokor.frmk.security.model.SecProfile;
import com.nokor.frmk.security.model.SecUser;

/**
 * 
 * @author prasnar
 *
 */
public class BaseSecuritySrvRsc extends BaseConfigSrvRsc {
	protected static final Logger LOGGER = LoggerFactory.getLogger(BaseSecuritySrvRsc.class);
	
	
	/**
	 * 
	 * @param applicationVOs
	 */
	public static void execBuildSecurityWithApp(List<SecApplicationVO> applicationVOs) {
		for (SecApplicationVO appVO : applicationVOs) {
			execBuildSecurityWithApp(appVO.getCode());
		}
	}
	
	/**
	 * 
	 * @param appCode
	 */
	public static void execBuildSecurityWithApp(String appCode) {
		try {
			execBuildSecurityWithApp(appCode, true);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new IllegalStateException(e);
		}
	}
	
	/**
	 * Build the default records for secAppCode = appCode
	 * . ts_sec_application
	 * . tu_sec_user (ADMIN)
	 * . ts_sec_profile (ADMIN)
	 * . tu_sec_application_user
	 * . tu_sec_application_profile
	 * . tu_sec_user_profile
	 */
	public static void execBuildSecurityWithApp(String appCode, boolean includeAnonymousUser) {
		try {
			SeuksaAppConfigFileHelper.setSecurityLdapMode(false);
			
			SecApplication app = SECURITY_SRV.getApplication(appCode);
			if (app == null) {
				app = SECURITY_SRV.creatApplication(appCode, appCode);
			} 
			Assert.notNull(app, "Could not create the application [" + appCode + "]");
			LOGGER.info("Application [" + app.getCode() + "] created successfully.");
			
			SecUser defaultAdmin = SecurityHelper.getAdminUser();
			LOGGER.info("Default Admin user [" + defaultAdmin.getLogin() + "].");
			SecProfile defaultAdminPro = defaultAdmin.getDefaultProfile();
			LOGGER.info("Default Admin profile [" + defaultAdmin.getDefaultProfile().getCode() + "].");	
			
			// Authorize the admin profile to access to the app
			if (!app.getProfiles().contains(defaultAdminPro)) {
				SECURITY_SRV.addApplicationToProfile(app, defaultAdminPro);
			}

			LOGGER.info("The default security configuration has been built successfully:");
			LOGGER.info(". Default Application [" + app.getCode() + "].");
			LOGGER.info(". Default Admin profile [" + defaultAdminPro.getCode() + "].");
			LOGGER.info(". Default Admin user [" + defaultAdmin.getLogin() + "].");
			
			// Anonymous user with no right
			if (includeAnonymousUser) {
				SecUser anoUser = SECURITY_SRV.getAnonynmousUser();
				LOGGER.info(". An anonymous user with no right [" + anoUser.getLogin() + "] has been created.");
			}
			
			
			// Read/Write/Execute are default privileges
			SecurityHelper.buildDefaultPrivileges();
			List<ESecPrivilege> lstPri = SECURITY_SRV.getListPrivileges();
			LOGGER.info("List of all privileges.");
			for (ESecPrivilege pri : lstPri) {
				LOGGER.info(". Privilege [" + pri.getCode() + "]");
			}
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new IllegalStateException(e);
		}			
	}
	
	
	
	/**
	 * 
	 * @param securityVO
	 */
	public static void execBuildAdditionnalSecurity(SecurityVO securityVO) {
		
		List<SecApplicationVO> applicationVOs = securityVO.getApplications();
//		List<SecProfileVO> profiles = securityVO.getProfiles();
		List<SecUserVO> users = securityVO.getUsers();
		RefTopicTableVO topicTableVO = securityVO.getTopicTableVO();
		List<TabVO> tabs = securityVO.getTabs();
		
		try {
			MenuJdbcDao.resetMenu();
			
			for (SecApplicationVO appVO : applicationVOs) {
				SecApplication app = ENTITY_SRV.getByCode(SecApplication.class, appVO.getCode());
				createProfiles(app, appVO.getProfiles());
			}
			
			createUsers(users);
			
			
			// ADMIN profile will be able to manage the profiles defined by PRO_LIST
			SecProfile proAdmin = SecurityHelper.getAdminProfile();
			for (SecApplicationVO appVO : applicationVOs) {
				initManagedProfiles(proAdmin, appVO.getProfiles());
			}

			// ----
			for (SecApplicationVO appVO : applicationVOs) {
				SecApplication app = SECURITY_SRV.getApplication(appVO.getCode());
	
				BaseSecuritySrvRsc.createMenuItems(appVO.getMenus());

				MENU_SRV.synchronizeMenu(app);
				
				if (appVO.getControlsAccess().size() > 0) {
					initProfilesPrivileges(appVO.getCode(), appVO.getControlsAccess());
				} else {
					SECURITY_SRV.grantProfileAllAccessControls(app, proAdmin);
				}
			}
			

			createTopics(topicTableVO);
			createTabControls(tabs);
			

			ApplicationManagerSrvRsc.callWsFlushCacheInAllApps(ApplicationManager.KEY_MENU_ITEMS);
			LOGGER.info("************SUCCESS**********");
        	
		} catch (Exception e) {
			String errMsg = "ERROR - execBuildAdditionnalSecurity - \r\n" + e.getMessage();
			LOGGER.error(errMsg, e);
			throw new IllegalStateException(errMsg, e);
		}
	}
    

	/**
	 * 
	 * @param appCode
	 * @param lstProPrivVO
	 */
	@Deprecated
	public static void initProfilesPrivilegesByArray(String appCode, List<Object[]> lstProPrivVO) {
		throw new IllegalStateException("initProfilesPrivilegesByArray - DEPRECATED");
	}
	
	/**
	 * 
	 * @param appCode
	 * @param menuItemAccessVOs
	 */
	public static void initProfilesPrivileges(String appCode, List<MenuItemAccessVO> menuItemAccessVOs) {
		if (menuItemAccessVOs == null || menuItemAccessVOs.size() < 1) {
			return;
		}
		
		SecApplication app = ENTITY_SRV.getByCode(SecApplication.class, appCode);
		Map<String, SecProfile> mapProfiles = new HashMap<>();
		Map<Long, SecControl> mapControls = new HashMap();
		
		try {
	
			// Check the existence of the Profiles
			// Init controls access
			for (String proCode : menuItemAccessVOs.get(0).getProfiles()) {
				SecProfile pro = ENTITY_SRV.getByCode(SecProfile.class, proCode);
				if (pro == null) {
					throw new IllegalStateException("The profile [" + proCode + "] can not be found.");
				}
				pro.initControlAccess();
				mapProfiles.put(proCode, pro);
			}
			
			// Check the existence of the Controls
			for (MenuItemAccessVO mnuItemAccessVO : menuItemAccessVOs) {
				MenuItemEntity mnuItem = ENTITY_SRV.getById(MenuItemEntity.class, mnuItemAccessVO.getId());
				if (mnuItem == null) {
					throw new IllegalStateException("App [" + appCode + "] - " + "The MenuItemEntity [" + mnuItemAccessVO.getId() + "] can not be found.");
				}
				if (mnuItem.getControl() == null) {
					throw new IllegalStateException("MenuItemEntity [" + mnuItemAccessVO.getCode() + "] - " + "The SecCOntrol is empty.");
				}
				mapControls.put(mnuItemAccessVO.getId(), mnuItem.getControl());
			}
		} catch (Exception e) {
			String errMsg = "Error check existence";
			LOGGER.error(errMsg, e);
			throw new IllegalStateException(errMsg, e);
		}
		
		try {
			
			// for each Control, grant control to profile if has privilege
			for (MenuItemAccessVO mnuItemAccessVO : menuItemAccessVOs) {
				SecControl ctl = mapControls.get(mnuItemAccessVO.getId()) ;//SECURITY_SRV.findControlByAppCode(ctlCode, appCode);
				
				LOGGER.info("Granting controls accesses to  profiles Control[" + ctl.getCode() + "]");
				
				Iterator<String> itPro = mnuItemAccessVO.getProfilesAccesses().keySet().iterator();
				while (itPro.hasNext()) {
					String proCode = itPro.next();
					SecProfile pro = mapProfiles.get(proCode);
					boolean hasAccess =  mnuItemAccessVO.hasAccess(proCode);
					if (hasAccess) {
						pro.addControlAccess(ctl);
						LOGGER.debug("	- Control [" + ctl.getCode() + "] - Add access to Profile [" + pro.getCode() + "]");
					}
				}
			}
		} catch (Exception e) {
			String errMsg = "Error on initProfilesPrivileges controls accesses";
			LOGGER.error(errMsg, e);
			throw new IllegalStateException(errMsg, e);
		}
		
		try {
			// bulk update
			List<SecProfile> profiles = new ArrayList<>();
			Iterator<String> itPro = mapProfiles.keySet().iterator();
			while (itPro.hasNext()) {
				String proCode = itPro.next();
				profiles.add(mapProfiles.get(proCode));
			}
			SECURITY_SRV.bulkUpdateProfileAccessControls(app, profiles);
		} catch (Exception e) {
			String errMsg = "Error bulkUpdateProfileAccessControls";
			LOGGER.error(errMsg, e);
			throw new IllegalStateException(errMsg, e);
		}
		
		

	}
	


	/**
	 * 
	 * @param userVOs
	 */
	public static void createUsers(List<SecUserVO> userVOs) {
		// For all Users
		for (SecUserVO usrVO : userVOs) {
			String proCode = usrVO.getProCode();
			String login = usrVO.getLogin();
			String desc = usrVO.getDesc();
			String pwd = usrVO.getPwd();
			SecUser usr = SECURITY_SRV.loadUserByUsername(login);
			
			// Create if does not exist
			if (usr == null) {
				SecProfile pro = ENTITY_SRV.getByCode(SecProfile.class, proCode);
				if (pro == null) {
					throw new EntityNotFoundException("SecProfile [" + proCode + "]");
				}
				usr = SECURITY_SRV.createUser(login, desc, pwd, pro, false);
			} else {
				SECURITY_SRV.changePassword(usr, pwd);
			}
			
		}

	}
	

	/**
	 * 
	 * @param admProfile
	 * @param lstProVO
	 */
	public static void initManagedProfiles(SecProfile admProfile, List<SecProfileVO> lstProVO) {
		// Delete the  SecManagedProfile where pro.id = admProfile.id
		List<SecManagedProfile> secMgdProfiles = SECURITY_SRV.listManagedProfileByParent(admProfile.getId());
		for (SecManagedProfile secManagedProfile : secMgdProfiles) {
			SECURITY_SRV.delete(secManagedProfile);
		}
		
		// For all profiles
		for (SecProfileVO proVO : lstProVO) {
			SecProfile pro = ENTITY_SRV.getByCode(SecProfile.class, proVO.getCode());
			if (pro == null) { 
				throw new IllegalStateException("The profile [" + proVO.getCode() + "] can not be found.");
			}
			
			SECURITY_SRV.createManagedProfile(admProfile, pro);
			
		}
	}
	
	
	/**
	 * 
	 * @param appCode
	 * @param lstProData: profiles list [0] code, [1] desc
	 */
	public static void createProfiles(String appCode, List<String[]> lstProData) {
		List<SecProfileVO> profileVOs = new ArrayList<SecProfileVO>();
		for (String[] proData : lstProData) {
			SecProfile pro = ENTITY_SRV.getByCode(SecProfile.class, proData[0]);
			if (pro != null) {
				throw new EntityAlreadyExistsException(SecProfile.class.getSimpleName() + "[" + proData[0] + "]");
			}
			SecProfileVO proVO = new SecProfileVO();
			proVO.setCode(proData[0]);
			proVO.setDesc(proData[1]);
			profileVOs.add(proVO);
		}
		SecApplication app = ENTITY_SRV.getByCode(SecApplication.class, appCode);
		createProfiles(app, profileVOs);
	}

	/**
	 * 
	 * @param menus
	 */
	public static void createMenuItems(List<MenuVO> menus) {
		for (MenuVO menuVO : menus) {
			createMenuItems(menuVO);
		}
	}
	
	/**
	 * 
	 * @param menuVO
	 */
	public static void createMenuItems(MenuVO menuVO) {
		if (menuVO != null && menuVO.getItems().size() > 0) {
			for (MenuItemVO mnuIteVO : menuVO.getItems()) {
				MenuJdbcDao.createMenuItem(mnuIteVO);
			}
		}
	}

	/**
	 * 
	 * @param app
	 * @param profileVOs
	 */
	public static void createProfiles(SecApplication app, List<SecProfileVO> profileVOs) {
		
		// For all profiles
		for (SecProfileVO proVO : profileVOs) {
			SecProfile pro = ENTITY_SRV.getByCode(SecProfile.class, proVO.getCode());
			
			// Create if does not exist
			if (pro == null) {
				pro = SECURITY_SRV.createProfile(proVO.getCode(), proVO.getDesc());
			}
			
			// Authorize the profile to access to the app
			if (!app.getProfiles().contains(pro)) {
				SECURITY_SRV.addApplicationToProfile(app, pro);
			}
			
		}
		
		

	}
	
	/**
	 * 
	 * @param lstTopicVO
	 */
	public static void createTopics(RefTopicTableVO topicTableVO) {
		List<RefTopicVO> topicTableVOs = topicTableVO.getTopicTables();
		if (!topicTableVOs.isEmpty()) { 
			for (RefTopicVO topicVO : topicTableVOs) {
				String parentCode = topicVO.getParentCode();
				String code = topicVO.getCode();
				String desc = topicVO.getDesc();
				RefTopic topic = REFTABLE_SRV.getByCode(RefTopic.class, code);
				
				// Create if does not exist
				if (topic == null) {
					List<String> refTableCodes = topicVO.getTableCodes();
					topic = REFTABLE_SRV.createRefTopic(parentCode, code, desc, refTableCodes);
				}
			}
			processSyncControlsVsRefTable(topicTableVO.getAppCode(), topicTableVO.getParentCtlCode());
		}
	}
	
	/**
	 * 
	 * @param lstTopicVO
	 */
	public static void createTabControls(List<TabVO> tabVOs) {
		try {
			for (TabVO tabVO : tabVOs) {
				SecControl ctlTab = EntityFactory.createInstance(SecControl.class);
				ctlTab.setCode(tabVO.getCode());
				ctlTab.setDesc(tabVO.getDesc());
				ctlTab.setDescEn(tabVO.getDescEn());
				ctlTab.setType(ESecControlType.OTHER);
				
				SecApplication app = SECURITY_SRV.getApplication(tabVO.getAppCode());
				SecControl parentCtl = ENTITY_SRV.getByCode(SecControl.class, tabVO.getParentCode());
				if (parentCtl == null) {
					throw new IllegalStateException("The Control [" + tabVO.getParentCode() + "] can not be created.");
				}
				ctlTab.setApplication(app);
				ctlTab.setParent(parentCtl);
				
				ENTITY_SRV.create(ctlTab);
				SECURITY_SRV.initControlPrivilege(ctlTab);
			}
			
		} catch (Exception e) {
			String errMsg = "Error createTabControls";
			LOGGER.error(errMsg, e);
			throw new IllegalStateException(errMsg, e);
		}
	}
	
	/**
	 * 
	 * @param appCode
	 * @param parentCtlCode
	 */
	public static void processSyncControlsVsRefTable(String appCode, String parentCtlCode) {
		try {
			SecApplication app = SECURITY_SRV.getApplication(appCode);
			SecControl secCtl = ENTITY_SRV.getByCode(SecControl.class, parentCtlCode);
			if (secCtl == null) {
				throw new IllegalStateException("The Control [" + parentCtlCode + "] can not be created.");
			}
			List<RefTopic> refTopics = ENTITY_SRV.list(RefTopic.class);
			for (RefTopic refTopic : refTopics) {
				SecControl ctlTopic = EntityFactory.createInstance(SecControl.class);
				ctlTopic.setCode(refTopic.getCode());
				ctlTopic.setDesc(refTopic.getDesc());
				ctlTopic.setDescEn(refTopic.getDescEn());
				ctlTopic.setParent(secCtl);
				ctlTopic.setType(ESecControlType.OTHER);
				ctlTopic.setApplication(app);
				ENTITY_SRV.create(ctlTopic);
				
				for (RefTable refTable : refTopic.getTables()) {
					SecControl ctlTable = EntityFactory.createInstance(SecControl.class);
					ctlTable.setCode(refTable.getCode());
					ctlTable.setDesc(refTable.getDesc());
					ctlTable.setDescEn(refTable.getDescEn());
					ctlTable.setParent(ctlTopic);
					ctlTable.setType(ESecControlType.OTHER);
					ctlTable.setApplication(app);
					ENTITY_SRV.create(ctlTable);
					SECURITY_SRV.initControlPrivilege(ctlTable);
				}
				
			}
			
		} catch (Exception e) {
			String errMsg = "Error processSyncControlsVsRefTable";
			LOGGER.error(errMsg, e);
			throw new IllegalStateException(errMsg, e);
		}
	}

}
