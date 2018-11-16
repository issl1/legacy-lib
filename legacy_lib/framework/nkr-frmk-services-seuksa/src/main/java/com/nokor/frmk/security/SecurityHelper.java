package com.nokor.frmk.security;

import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.tools.MyEmailUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.nokor.frmk.helper.SeuksaAppConfigFileHelper;
import com.nokor.frmk.helper.SeuksaServicesHelper;
import com.nokor.frmk.helper.SeuksaSettingConfigHelper;
import com.nokor.frmk.security.ldap.service.SecLdapService;
import com.nokor.frmk.security.model.SecPage;
import com.nokor.frmk.security.model.ESecPrivilege;
import com.nokor.frmk.security.model.SecApplication;
import com.nokor.frmk.security.model.SecControl;
import com.nokor.frmk.security.model.SecManagedProfile;
import com.nokor.frmk.security.model.SecProfile;
import com.nokor.frmk.security.model.SecUser;


/**
 * 
 * @author prasnar
 * @version $Revision$
 *
 */
public class SecurityHelper implements SeuksaServicesHelper {
	private static final Logger logger = LoggerFactory.getLogger(SecurityHelper.class);
	
    public static final String SESS_KEY_AUTHENTICATION_ERROR = "@Sec0-0Auth0_0Error0@";


	public static String RANDOM_STR_ID = "abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOGIN1_PATTERN = "^[a-zA-Z0-9\\._\\-@]{5,50}$";

    /** ---------------------------------------------------------
     *  SECURITY
     ** ---------------------------------------------------------*/
	private static final String DEFAULT_ADMIN_PROFILE = SecProfile.ADMIN.getCode();
	public static final String DEFAULT_ADMIN_LOGIN = "admin";
	public static final Long DEFAULT_ADMIN_ID = 1L;
	public static final Long DEFAULT_ADMIN_PROFILE_ID = 1L;
	public static final String ADMIN_LOGIN = "admin";

	private static final String DEFAULT_USER_PROFILE = "SimpleFO";
	private static final String DEFAULT_USER = "userfo";

	private static final String DEFAULT_ANONYMOUS_LOGIN = "Anonymous";
	public static final Long DEFAULT_ANONYMOUS_ID = -1L;
	
	public static final Long SYS_ADMIN_ID = -10000L;
	
	private static final Long DEFAULT_EXCECUTE_PRI_ID = 1L;
	private static final Long DEFAULT_READ_PRI_ID = 2L;
	private static final Long DEFAULT_WRITE_PRI_ID = 3L;

	private static final String DEFAULT_EXCECUTE_PRI = "EXECUTE";
	private static final String DEFAULT_READ_PRI = "READ";
	private static final String DEFAULT_WRITE_PRI = "WRITE";

	public static final SecLdapService SEC_LDAP_SRV = SecurityHelper.getSecLdapService();


    /**
     * Constructor
     * Can not be instantiated
     */
	private SecurityHelper() {
	}
	
	/**
	 * 
	 * @return
	 */
	public static SecLdapService getSecLdapService() {
		SecLdapService service = null;
		String serviceBeanName = SeuksaAppConfigFileHelper.getSecurityLdapName();
		if (StringUtils.isNotEmpty(serviceBeanName)) {
			try {
				service = SpringUtils.getBean(serviceBeanName);
			} catch (Exception e) {
				logger.error("Can not the service [" + serviceBeanName + "]", e);
			}
		} else {
			logger.info("No Security Ldap Name");
		}
		return service;
	}

	
	/**
	 * 
	 * @return
	 */
	public static long getAnonymousId() {
		return DEFAULT_ANONYMOUS_ID;
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getAnonymous() {
		return DEFAULT_ANONYMOUS_LOGIN;
	}
	
	/**
	 * 
	 * @return
	 */
	public static SecUser getAdminUser() {
		SecUser user = ENTITY_SRV.getById(SecUser.class, DEFAULT_ADMIN_ID);
		if (user == null) {
			user = createAdminUser();
		}
		Assert.notNull(user, "Could not create a default admin user.");
		return user;
	}
	
	/**
	 * 
	 * @param appCode
	 */
	public static SecPage getDummyPage(String appCode) {
		SecPage page = null;
		SecApplication app = ENTITY_SRV.getByCode(SecApplication.class, appCode);
		String pageCode = "Dummy." + app.getCode();
		page = ENTITY_SRV.getByCode(SecPage.class, pageCode);
		if (page == null) {
			page = SECURITY_SRV.creatPage("Dummy." + app.getCode() , null, app);
		}
		Assert.notNull(page, "Could not create a dummy page [" + pageCode + "].");
		return page;
	}
	
	/**
	 * 
	 * @return
	 */
	public static SecUser getDefaultFOUser() {
		SecUser user = SECURITY_SRV.loadUserByUsername(DEFAULT_USER);
		if (user == null) {
			user = createDefaultFOUser();
		}
		Assert.notNull(user, "Could not create a default FO user.");
		return user;
	}
	
	/**
	 * 
	 * @return
	 */
	public static void buildDefaultPrivileges() {
		ESecPrivilege pri = ENTITY_SRV.getById(ESecPrivilege.class, DEFAULT_EXCECUTE_PRI_ID);
		if (pri == null) {
			pri = SECURITY_SRV.createPrivilege(DEFAULT_EXCECUTE_PRI, DEFAULT_EXCECUTE_PRI);
		}
		Assert.notNull(pri, "Could not create the EXECUTE Privilege.");
		
		pri = ENTITY_SRV.getById(ESecPrivilege.class, DEFAULT_READ_PRI_ID);
		if (pri == null) {
			pri = SECURITY_SRV.createPrivilege(DEFAULT_READ_PRI, DEFAULT_READ_PRI);
		}
		Assert.notNull(pri, "Could not create the READ Privilege.");
		
		pri = ENTITY_SRV.getById(ESecPrivilege.class, DEFAULT_WRITE_PRI_ID);
		if (pri == null) {
			pri = SECURITY_SRV.createPrivilege(DEFAULT_WRITE_PRI, DEFAULT_WRITE_PRI);
		}
		Assert.notNull(pri, "Could not create the WRITE Privilege.");
	}
	
	
	/**
	 * 
	 */
	private static SecUser createAdminUser() {
		return SECURITY_SRV.createUser(DEFAULT_ADMIN_LOGIN, DEFAULT_ADMIN_LOGIN, DEFAULT_ADMIN_LOGIN, getAdminProfile());
	}
	
	/**
	 * 
	 */
	private static SecUser createDefaultFOUser() {
		return SECURITY_SRV.createUser(DEFAULT_USER, DEFAULT_USER, DEFAULT_USER, getDefaultUserProfile());
	}
	
	/**
	 * 
	 * @return
	 */
	public static SecProfile getAdminProfile() {
		SecProfile pro = ENTITY_SRV.getById(SecProfile.class, DEFAULT_ADMIN_PROFILE_ID);
		if (pro == null) {
			pro = createAdminProfile();
		}
		Assert.notNull(pro, "Could not create the default admin profile.");

		return pro;
	}
	

	/**
	 * 
	 * @return
	 */
	public static SecProfile getDefaultUserProfile() {
		SecProfile pro =  ENTITY_SRV.getByCode(SecProfile.class, DEFAULT_USER_PROFILE);
		if (pro == null) {
			pro = createDefaultUserProfile();
		}
		Assert.notNull(pro, "Could not create a default user profile.");
		return pro;
	}
	
	
	/**
	 * 
	 */
	private static SecProfile createAdminProfile() {
		return SECURITY_SRV.createProfile(DEFAULT_ADMIN_PROFILE, DEFAULT_ADMIN_PROFILE);
	}
	
	/**
	 * 
	 */
	public static SecProfile createDefaultUserProfile() {
		return SECURITY_SRV.createProfile(DEFAULT_USER_PROFILE, DEFAULT_USER_PROFILE);
	}
	
	
	
	/**
	 * 
	 * @return
	 */
	public static SecApplication getSecApplication() {
		return SeuksaAppConfigFileHelper.getSecApplication();
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean checkSecurityTableApplicationUser() {
		return SeuksaAppConfigFileHelper.checkSecurityTableApplicationUser();
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean checkSecurityTableUserProfile() {
		return SeuksaAppConfigFileHelper.checkSecurityTableUserProfile();
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean checkSecurityTableApplicationProfile() {
		return SeuksaAppConfigFileHelper.checkSecurityTableApplicationProfile();
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean checkSecurityTableApplicationUserProfile() {
		return SeuksaAppConfigFileHelper.checkSecurityTableApplicationUserProfile();
	}
	 
	/**
     * 
     * @return
     */
    public static int getCredentialExpiredAfterNbDays() {
    	return SeuksaSettingConfigHelper.getCredentialExpiredAfterNbDays();
    }
	
    /**
     * 
     * @param login
     * @return
     */
    public static boolean isValidLoginAsEmail(String login) {
    	return MyEmailUtils.isEmailValid(login);
    }
    
    /**
     * 
     * @param login
     * @param sPattern
     * @return
     */
    public static boolean isValidLogin(String login) {
    	return isValidLogin(login, LOGIN1_PATTERN);
    }
    
    /**
     * 
     * @param login
     * @param sPattern
     * @return
     */
    public static boolean isValidLogin(String login, String sPattern) {
    	Pattern pattern = Pattern.compile(sPattern);
    	Matcher matcher = pattern.matcher(login);
		return matcher.matches();
	}
    
    /**
     * 
     * @return
     */
    public static String getGenerateRandomSecurePassword(){
		int limit = 10;
		String chars = RANDOM_STR_ID;
	    Random r = new Random();
	    StringBuffer randomPassword = new StringBuffer();

	    randomPassword.append(chars.charAt(r.nextInt(26)));
	    for (int i = 0; i < limit ; i++) {
	    	randomPassword.append(chars.charAt(r.nextInt(chars.length())));
	    }
	    
		return randomPassword.toString();
	}
    

	/**
	 * 
	 * @param profiles
	 * @param secPro
	 * @return
	 */
	public static boolean hasProfile(List<SecProfile> profiles, SecProfile secPro) {
		return SecUser.hasProfile(profiles, secPro);
	}
	
	// -----------------------------------------------------------
	// Profiles - System super Admin           
	// -----------------------------------------------------------
	public static boolean isProfileAdmin(SecUser user) {
		return hasProfile(user.getProfiles(), SecProfile.ADMIN);
	}
	
	/**
	 * 
	 * @param login
	 * @return
	 */
	public static boolean isSysAdminLogin(String login) {
		 return DEFAULT_ADMIN_LOGIN.equals(login);
	}
	

	// -----------------------------------------------------------
	// Profiles - Default user
	// -----------------------------------------------------------
	public static boolean isProfileDefaultUser(SecUser user) {
		return hasProfile(user.getProfiles(), getDefaultUserProfile());
	}
	


	
	/**
	 * 
	 * @param mainPro
	 * @param lstManagedProVO
	 */
	public static void createManagedProfiles(SecProfile mainPro, List<String[]> lstManagedProVO) {
		// For all profiles to manage
		for (String[] proVO : lstManagedProVO) {
			SecProfile pro = ENTITY_SRV.getByCode(SecProfile.class, proVO[0]);
			if (pro == null) { 
				throw new IllegalStateException("The profile [" + proVO[0] + "] can not be found.");
			}
			SecManagedProfile secMgdPro = SECURITY_SRV.findManagedProfile(mainPro.getId(), pro.getId());
			if (secMgdPro == null) {
				SECURITY_SRV.createManagedProfile(mainPro, pro);
			}
			
		}
	}
	
	/**
	 * 
	 */
	public static void initAdminManageAllNotAdminProfiles() {
		List<SecProfile> profiles = SECURITY_SRV.listProfilesNotAdmin();
		for (SecProfile pro : profiles) {
			addManagedProfileToProfile(SecProfile.ADMIN.getCode(), pro.getCode());
		}
		
	}
	
	/**
	 * 
	 * @param mainProCode
	 * @param managedProCode
	 */
	public static void addManagedProfileToProfile(String mainProCode, String managedProCode) {
		SecProfile mainPro = ENTITY_SRV.getByCode(SecProfile.class, mainProCode);
		SecProfile managedPro = ENTITY_SRV.getByCode(SecProfile.class, managedProCode);

		if (mainPro == null || managedPro == null) {
			throw new IllegalArgumentException("Please, check you parameters.");
		}
		SecManagedProfile secMgdPro = SECURITY_SRV.findManagedProfile(mainPro.getId(), managedPro.getId());
		if (secMgdPro == null) {
			SECURITY_SRV.createManagedProfile(mainPro, managedPro);
		}
	}

	
	/**
	 * 
	 * @param app
	 * @param pro
	 * @param ctlCode
	 */
	public static void addAccessControlToProfile(SecApplication app, SecProfile pro, String ctlCode) {
		SecControl ctl = SECURITY_SRV.findControlByAppCode(ctlCode, app.getCode());
		addAccessControlToProfile(pro, ctl);
	}

	/**
	 * 
	 * @param pro
	 * @param ctl
	 */
	public static void addAccessControlToProfile(SecProfile pro, SecControl ctl) {
		try {
			logger.info("Granting control access to  [" + pro.getCode() + "] - AppCode [" + ctl.getApplication().getCode() + "]");
			SECURITY_SRV.initProfileControlAccess(pro, ctl);
			logger.info("The controls accesses are granted successfully to [" + pro.getCode() + "]");

		} catch (Exception e) {
			String errMsg = "Error Profile [" + pro.getCode() + "] Control [" + ctl.getCode() + "] - AppCode [" + ctl.getApplication().getCode() + "]";
			logger.error(errMsg, e);
			throw new IllegalStateException(errMsg, e);
		}
	}
	
	
	
	/**
	 * 
	 * @param username
	 * @param newPassword
	 */
	public static void changeUserPassword(String username, String newPassword) {
		try {
			SecUser secUser = SECURITY_SRV.loadUserByUsername(username);
			Assert.notNull(secUser, "[" + username + "] does not exist.");
			
			SECURITY_SRV.changePasswordSalt(secUser);
			SECURITY_SRV.changePassword(secUser, newPassword);
			
			logger.info("New digested password [" + secUser.getPassword() + "]");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new IllegalStateException(e);
		}
	}
}
