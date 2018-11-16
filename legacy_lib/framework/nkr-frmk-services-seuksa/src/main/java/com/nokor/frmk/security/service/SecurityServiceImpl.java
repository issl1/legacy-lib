package com.nokor.frmk.security.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.seuksa.frmk.tools.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.frmk.helper.SeuksaAppConfigFileHelper;
import com.nokor.frmk.security.SecurityEntityFactory;
import com.nokor.frmk.security.SecurityHelper;
import com.nokor.frmk.security.dao.SecurityDao;
import com.nokor.frmk.security.ldap.model.LdapUser;
import com.nokor.frmk.security.model.ESecControlType;
import com.nokor.frmk.security.model.ESecPrivilege;
import com.nokor.frmk.security.model.SecApplication;
import com.nokor.frmk.security.model.SecControl;
import com.nokor.frmk.security.model.SecControlPrivilege;
import com.nokor.frmk.security.model.SecControlProfilePrivilege;
import com.nokor.frmk.security.model.SecManagedProfile;
import com.nokor.frmk.security.model.SecPage;
import com.nokor.frmk.security.model.SecProfile;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.security.model.SecUserAware;
import com.nokor.frmk.security.spring.SecSaltGenerator;
import com.nokor.frmk.security.spring.encoding.PasswordEncoderUtils;

/**
 * 
 * @author prasnar
 * @version $Revision$
 */
@Service("securityService")
public class SecurityServiceImpl extends BaseEntityServiceImpl implements SecurityService {
	/** */
	private static final long serialVersionUID = -8332304023054641690L;

	@Autowired
    private SecurityDao dao;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private SaltSource saltSource;
	
	private static final String NATIVE_SQL_CREATE_WITH_SEC_ID = 
			"INSERT INTO tu_sec_user (sec_usr_id, sec_usr_login,sec_usr_desc, sec_usr_pwd, sta_rec_id, dt_cre, usr_cre, dt_upd,usr_upd, version) "
			+ "   VALUES "
			+ "(%1, '%2', '%3', '%4', 1, now(), 'admin', now(), 'admin', 1)";
	

	
    /**
     * 
     */
    public SecurityServiceImpl() {
    }

    /**
     * @see org.seuksa.frmk.service.impl.BaseEntityServiceImpl#getDao()
     */
	@Override
	public BaseEntityDao getDao() {
		return dao;
	}
	
	
    /**
	 * @return the passwordEncoder
	 */
	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	/**
	 * @param passwordEncoder the passwordEncoder to set
	 */
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * Get secApplication by code.
	 * 
	 * @param code The secApplication's code.
	 * 
	 * @return The secApplication or null.
	 */
	@Override
	public SecApplication getApplication(String code) {
		if (StringUtils.isEmpty(code)) {
    		return null;
    	}
        return getByCode(SecApplication.class, code);
	}
	
	/**
	 * Create secProfile and create control privileges by secProfile.
	 * 
	 * @param secProfile The SecProfile.
	 * 
	 * @throws IllegalArgumentException<br/>
	 * 		- The secProfile is null.<br/>
	 * 		- The secProfile's id is not null or id greater 0.
	 */
	@Override
	public SecProfile createProfile(SecProfile secProfile) {
		if(secProfile == null) {
			throw new IllegalArgumentException("SecProfile is null.");
		}
		if (secProfile.getId() != null && secProfile.getId() > 0) {
        	throw new IllegalArgumentException("The secProfile should have an empty id.");
        }
		if (StringUtils.isEmpty(secProfile.getDesc())) {
			secProfile.setDesc(secProfile.getDescEn());
		}
		secProfile.setStatusRecord(EStatusRecord.ACTIV);
		dao.create(secProfile);
		
		updateProfileAccessControls(secProfile, secProfile.getControlProfilePrivileges());
		createManagedProfile(UserSessionManager.getCurrentUser().getDefaultProfile(), secProfile);
		
		return secProfile;
	}
	
	/**
	 * Update secProfile and update control privilege by secProfile.
	 * 
	 * @param secProfile The SecProfile.
	 * 
	 * @throws IllegalArgumentException<br/>
	 * 		- The secProfile is null.<br/>
	 * 		- The secProfile's id is null or id equal 0.
	 */
	@Override
	public SecProfile updateProfile(SecProfile secProfile) {
		if(secProfile == null) {
			throw new IllegalArgumentException("SecProfile is null.");
		}
		if(secProfile.getId() == null || secProfile.getId().equals(0)) {
			throw new IllegalArgumentException("SecProfile should not have an empty id.");
		}
		dao.update(secProfile);
		
		updateProfileAccessControls(secProfile, secProfile.getControlProfilePrivileges());
		
		return secProfile;
	}
	
	@Override
	public void bulkUpdateProfileAccessControls(SecApplication app, List<SecProfile> profiles) {
		for (SecProfile pro : profiles) {
			updateProfileAccessControls(app, pro, pro.getControlProfilePrivileges());
			logger.debug("The controls accesses are granted successfully to [" + pro.getCode() + "]");
		}
		logger.info("bulkUpdateProfileAccessControls on [" + app.getCode() + "]- OK");
	}
	
	@Override
	public void grantProfileAllAccessControls(SecApplication app, SecProfile secProfile) {
		secProfile.initControlAccess();
		
		List<SecControl> ctls = listControlsByAppId(app.getId());
		for (SecControl ctl : ctls) {
			secProfile.addControlAccess(ctl);
		}
		updateProfileAccessControls(app, secProfile, secProfile.getControlProfilePrivileges());

	}

	/**
	 * @see com.nokor.frmk.security.service.SecurityService#updateProfileAccessControls(com.nokor.frmk.security.model.SecProfile, java.util.List)
	 */
	@Override
	public void updateProfileAccessControls(SecProfile secProfile, List<SecControlProfilePrivilege> lstCpp) {
		updateProfileAccessControls(null, secProfile, lstCpp);
	}

	/**
	 * @see com.nokor.frmk.security.service.SecurityService#updateProfileAccessControls(com.nokor.frmk.security.model.SecProfile, java.util.List, com.nokor.frmk.security.model.SecApplication)
	 */
	@Override
	public void updateProfileAccessControls(SecApplication app, SecProfile secProfile, List<SecControlProfilePrivilege> lstCpp) {
		// If access controls, then Nothing
		if (secProfile == null || lstCpp == null || lstCpp.isEmpty()) {
			return;
		}

		try {
			// Remove the old controls accesses if any
			List<SecControlProfilePrivilege> oldLstCpp = null;
			if (app != null) {
				oldLstCpp = listControlsPrivilegesByProfileIdAndAppId(secProfile.getId(), app.getId());
			} else {
				oldLstCpp = listControlsPrivilegesByProfileId(secProfile.getId());
			}
			for (SecControlProfilePrivilege oldCpp : oldLstCpp) {
				dao.delete(oldCpp);
			}
		} catch (Exception e) {
			String errMsg = "Error while removing the old controls accesses.";
			throw new IllegalStateException(errMsg, e);
		}

		try {
			// Add the new controls accesses
			for (SecControlProfilePrivilege cpp : lstCpp) {
				cpp.setProfile(secProfile);
				dao.create(cpp);
			}
			secProfile.setControlProfilePrivileges(lstCpp);
		} catch (Exception e) {
			String errMsg = "Error while creating the new controls accesses.";
			throw new IllegalStateException(errMsg, e);
		}
	}
	
	@Override
	public void initProfileControlAccess(SecProfile pro, SecControl ctl) {
		List<ESecPrivilege> lstPriv = ESecPrivilege.values();
		initProfileControlAccess(pro, ctl, lstPriv);
	}
	
	@Override
	public void initProfileControlAccess(SecProfile pro, SecControl ctl, List<ESecPrivilege> privileges) {
		if (privileges == null || privileges.isEmpty()) {
			privileges = ESecPrivilege.values();
		}
		
		// remove the old privileges
		try {
			for (SecControlProfilePrivilege ctlProPri : ctl.getControlProfilePrivileges()) {
				dao.delete(ctlProPri);
			}
		} catch (Exception e) {
			String errMsg = "Error while removing the old privileges on the control [" + ctl.getCode() + "] for the profile [" + pro.getCode() + "].";
			throw new IllegalStateException(errMsg, e);
		}
		
		// Add the new privileges
		try {
			List<SecControlProfilePrivilege> lstCpp = new ArrayList<SecControlProfilePrivilege>();
			for (ESecPrivilege pri : privileges) {
				SecControlProfilePrivilege ctlPri = SecControlProfilePrivilege.createInstance(pro, ctl, pri);
				dao.create(ctlPri);
				lstCpp.add(ctlPri);
			}
			ctl.setControlProfilePrivileges(lstCpp);
			pro.setControlProfilePrivileges(lstCpp);
		} catch (Exception e) {
			String errMsg = "Error while creating the new privileges for the control [" + ctl.getCode() + "] for the profile [" + pro.getCode() + "].";
			throw new IllegalStateException(errMsg, e);
		}
		
		
	}

	/**
	 * 
	 * @param parent
	 * @param child
	 */
	@Override
	public void createManagedProfile(SecProfile parent, SecProfile child) {
		List<SecProfile> listParents = getAllParentProfiles(parent);
		listParents.add(parent);
		for (SecProfile secProfile : listParents) {
			SecManagedProfile managedProfile = SecManagedProfile.createInstance();
			managedProfile.setParent(secProfile);
			managedProfile.setChild(child);
			dao.create(managedProfile);
		}	
	}
	
	/**
	 * 
	 * @param parent
	 * @return
	 */
	private List<SecProfile> getAllParentProfiles(SecProfile parent) {
		List<SecProfile> profiles = new ArrayList<SecProfile>();
		List<SecManagedProfile> listParents = listManagedProfileByChild(parent.getId());
		for (SecManagedProfile secManagedProfile : listParents) {
			profiles.add(secManagedProfile.getParent());
			profiles.addAll(getAllParentProfiles(secManagedProfile.getParent()));
		}
		return profiles;
	}
	
	/**
	 * Find secProfile by code.
	 * 
	 * @param secProCode The secProfile's code.
	 * 
	 * @return The secProfile or null.
	 */
	@Override
    public SecProfile findProfileByCode(String secProCode) {
        return dao.getByCode(SecProfile.class, secProCode);
    }

	/**
	 * List secPrivileges.
	 * 
	 * @return The secPrivilegeList or empty array list.
	 */
	@Override
	public List<ESecPrivilege> getListPrivileges() {
		return list(ESecPrivilege.class);
	}
	
	/**
	 * List secProfiles.
	 * 
	 * @return The secProfile List or empty array list.
	 */
	@Override
	public List<SecProfile> getListProfiles() {
		return list(SecProfile.class);
	}

	/**
	 * List secApplications.
	 * 
	 * @return The secApplication List or empty array list.
	 */
	@Override
	public List<SecApplication> getListApplications() {
		return list(SecApplication.class);
	}

	/**
	 * List secUsers.
	 * 
	 * @return The secUser List or empty array list.
	 */
	@Override
	public List<SecUser> getListUsers() {
		return getListUsers(null);
	}
	
	/**
	 * List secUser with order.
	 * 
	 * @param order The order.
	 * 
	 * @return The secUser List or empty array list.
	 */
	@Override
	public List<SecUser> getListUsers(Order order) {
		List<SecUser> users = list(SecUser.class, order);
		for (SecUser secUser : users) {
			for (SecProfile profile : secUser.getProfiles()) {
	        	List<SecControlProfilePrivilege> list = profile.getControlProfilePrivileges();
	        	for (SecControlProfilePrivilege secControlProfilePrivilege : list) {
	        		Hibernate.initialize(secControlProfilePrivilege);
				}
			} 
		}
		return users;
	}

	/**
	 * Load secUser by username(login).
	 * 
	 * @param username The user login.
	 * 
	 * @return The secUser or null.
	 */
	@Override
	public SecUser loadUserByUsername(String username) {
		SecUserRestriction restriction = new SecUserRestriction();
		restriction.setLogin(username);
		return dao.getUnique(restriction);
	}

	 
    /**
     * Check secApplication is in secProfile list of secUser.
     * 
     *  @param secUser The secUser.
     *  @param secApp The secApplication.
     *  
     *  @return <b>True</b> The secApplication in secProfile list of secUser.<br/>
     * 	 		<b>False</b> The secApplication not in secProfile list of secUser.
     */
    @Override
	public boolean checkUserInProfileApplication(SecUser secUser, SecApplication secApp) {
    	return secUser.hasAccessToApp(secApp.getId());
	}

    /**
     * Check secProfile is in secUser's profiles.
     * 
     *  @param secUser The secUser.
     *  @param secProfile The secProfile.
     *  
     *  @return <b>True</b> The secProfile in secUser's profiles.<br/>
     * 	 		<b>False</b> The secProfile not in secUser's profiles.
     */
    @Override
	public boolean checkProfileInUserProfile(SecUser secUser, SecProfile secProfile) {
    	if (secUser.getProfiles() == null || secUser.getProfiles().size() == 0) {
    		return false;
    	}
        for (SecProfile currPro : secUser.getProfiles()) {
			if (currPro.getId().equals(secProfile.getId())) {
				return true;
			}
		}
        return false;
	}
    
    /**
     * Check exists secUser by username.
     * 
     * @param username The secUser login.
     * 
     * @return <b>True</b> The secUser exists. <br/><b>False</b> The secUser doesn't exists.
     */
    @Override
	public boolean checkUserByUsername(String username) {
		SecUser secUser = loadUserByUsername(username);
		return secUser != null;
	}

	/**
	 * Create new secUser with default profile.
	 * 
	 * @param login The user login name.
	 * @param desc The user desc.
	 * @param password The user password.
	 * @param defaultProfile The default profile.
	 * 
	 * @throws SecUserCreationException
	 */
	@Override
	public SecUser createUser(String login, String desc, String password, SecProfile defaultProfile) throws SecUserCreationException {
		return createUser(login, desc, password, defaultProfile, true);
    }
    
    @Override
	public SecUser createUser(String login, String desc, String password, SecProfile defaultProfile, boolean createAlsoInLdap) throws SecUserCreationException {
		SecUser user = null;
		try {
			user = SecUser.createInstance();
			user.setLogin(login);
			user.setDesc(desc);
			user.setDefaultProfile(defaultProfile);

			createUser(user, password, createAlsoInLdap);
		} catch (Exception e) {
    		String errMsg ="Error during the creation of the user [" + user.getLogin() + "]";
    		logger.error(errMsg);
    		throw new SecUserCreationException(errMsg, e);
		} finally {
			if(user == null) {
				logger.error("The creation of [" + login + "] has failed.");
			} else {
				logger.debug("The creation of [" + login + "] is successful.");
			}
		}
		return user;
	}
	
	
	@Override
	public SecUser getAnonynmousUser() {
		SecUser user = dao.getById(SecUser.class, SecurityHelper.getAnonymousId());
		if (user == null) {
			user = createAnonynmousUser();
		}
		return user;
	}
	
	/**
	 * 
	 * @return
	 */
	private SecUser createAnonynmousUser() {
		SecUser user = loadUserByUsername(SecurityHelper.getAnonymous());
		if (user != null) {
        	String errMsg = "The user [" + user.getId() + "] has already the login [" + SecurityHelper.getAnonymous() + "]";
        	logger.error(errMsg);
        	throw new IllegalStateException(errMsg);
		}
		
		user = SecurityEntityFactory.createSecUserInstance();
		user.setId(SecurityHelper.getAnonymousId());
		user.setLogin(SecurityHelper.getAnonymous());
		user.setDesc(user.getLogin());
		if (StringUtils.isEmpty(user.getPasswordSalt())) {
			user.setPasswordSalt(SecSaltGenerator.generateSaltFromUser(user));
		}
		String encodedPwd = passwordEncoder.encodePassword(user.getLogin(), saltSource.getSalt(user));
        user.setPassword(encodedPwd);
        user.setStatusRecord(EStatusRecord.ACTIV);

        String sql = NATIVE_SQL_CREATE_WITH_SEC_ID;
        try {
        	sql = NATIVE_SQL_CREATE_WITH_SEC_ID
				.replaceFirst("%1", user.getId() + "")
				.replaceFirst("%2", user.getLogin())
				.replaceFirst("%3", user.getLogin())
				.replaceFirst("%4", user.getPassword());
			SQLQuery sqlQuery = dao.createSqlQuery(sql);
			sqlQuery.executeUpdate();
        } catch (Exception e) {
        	String errMsg = "Error while creating anonymous user - [" + sql + "]";
        	logger.error(errMsg, e);
        	throw new IllegalStateException(errMsg, e);
        }
		user = dao.getById(SecUser.class, user.getId());
       
		if (user == null) {
        	String errMsg = "The creation of Anomymous user [" + SecurityHelper.getAnonymous() + "] has failed.";
        	logger.error(errMsg);
			throw new IllegalStateException(errMsg);
		}
		
		logger.info("The Anonymous user [" + user.getId() + "][" + user.getLogin() +  "]  has been created sucessfully.");

		return user;
	}

	@Override
	public SecApplication creatApplication(String code, String desc) {
		SecApplication app = EntityFactory.createInstance(SecApplication.class);
		app.setCode(code);
		app.setDesc(desc);
		app.setDescEn(app.getDesc());
		app.setSortIndex(1);
		dao.saveOrUpdate(app);
		return app;
	}
	
	@Override
	public SecProfile createProfile(String code, String desc) {
		SecProfile pro = EntityFactory.createInstance(SecProfile.class);
		pro.setCode(code);
		pro.setDesc(desc);
		pro.setDescEn(pro.getDesc());
		pro.setSortIndex(1);
		dao.saveOrUpdate(pro);
		return pro;
	}
	
	@Override
	public ESecPrivilege createPrivilege(String code, String desc) {
		ESecPrivilege pri = EntityFactory.createInstance(ESecPrivilege.class);
		pri.setCode(code);
		pri.setDesc(code);
		pri.setDescEn(pri.getDesc());
		pri.setSortIndex(1);
		dao.saveOrUpdate(pri);
		return pri;
	}
	
	@Override
	public void initControlPrivilege(SecControl ctl) {
		initControlPrivilege(ctl, null);
	}
	
	@Override
	public void initControlPrivilege(SecControl ctl, List<ESecPrivilege> privileges) {
		boolean defaultPrivileges = privileges == null || privileges.isEmpty();
		
		if (defaultPrivileges && !ctl.getControlPrivileges().isEmpty()) {
			return;
		}
		
		if (defaultPrivileges) {
			privileges = ESecPrivilege.values();
		}
		
		// remove the old privileges
		List<SecControlPrivilege> ctlPrivileges = ctl.getControlPrivileges();
		try {
			for (SecControlPrivilege ctlPri : ctlPrivileges) {
				dao.delete(ctlPri);
			}
		} catch (Exception e) {
			String errMsg = "Error while removing the old privileges for the control [" + ctl.getCode() + "].";
			throw new IllegalStateException(errMsg, e);
		}
		
		// Add the new privileges
		List<SecControlPrivilege> lstCtlPri = new ArrayList<SecControlPrivilege>();
		for (ESecPrivilege pri : privileges) {
			SecControlPrivilege ctlPri = SecControlPrivilege.createInstance(ctl, pri);
			dao.create(ctlPri);
			lstCtlPri.add(ctlPri);
		}
		ctl.setControlPrivileges(lstCtlPri);
	}


	@Override
	public SecPage creatPage(String code, String desc, SecApplication app) {
		SecPage page = EntityFactory.createInstance(SecPage.class);
		page.setCode(code);
		page.setDesc(desc != null ? desc : code);
		page.setDescEn(page.getDesc());
		page.setApplication(app);
		dao.saveOrUpdate(page);
		return page;
	}
	
    @Override
    public SecUser createUser(SecUser user, String rawPwd) throws SecUserCreationException {
    	return createUser(user, rawPwd, true);
    }
    
    /**
	 * Create new secUser.
	 * 
	 * @param user The secUser.
	 * @param rawPwd The password.
	 * 
	 * @throws SecUserCreationException<br/>
	 *   - The secUser is null.<br/>
	 *   - The secUser'is > 0.<br/>
	 *   - The rawPwd is null or empty.<br/>
	 *   - The default profile is null.<br/>
	 *   - The duplicate login.
	 */
    @Override
    public SecUser createUser(SecUser user, String rawPwd, boolean createAlsoInLdap) throws SecUserCreationException {

        if (user == null) {
    		throw new SecUserCreationException("SecUser is null");
    	}
    	if (user.getId() != null && user.getId() > 0) {
        	throw new SecUserCreationException("SecUser [" + user.getLogin() + "] - Should have an empty id.");
        }
    	if (StringUtils.isEmpty(rawPwd)) {
    		throw new SecUserCreationException("SecUser [" + user.getLogin() + "] - An empty password is not allowed");
    	}
    	if (user.getDefaultProfile() == null) {
        	throw new SecUserCreationException("SecUser [" + user.getLogin() + "] - A default profile is required.");
        }
    	if (checkUserByUsername(user.getLogin())) {
			throw new SecUserCreationException("The [" + user.getLogin() + "] already exists.");
		}
    	
    	if (createAlsoInLdap) {
    		LdapUser ldapUser = createUserInLdap(user.getLogin(), rawPwd);
    	}
    	
    	
    	try {
    		// if OK create normally in local
    		if (StringUtils.isEmpty(user.getPasswordSalt())) {
    			user.setPasswordSalt(SecSaltGenerator.generateSaltFromUser(user));
    		}
	        String encodedPwd = passwordEncoder.encodePassword(rawPwd, saltSource.getSalt(user));
	        user.setPassword(encodedPwd);
	        user.setStatusRecord(EStatusRecord.ACTIV);
	        getDao().create(user);
	        getDao().saveOrUpdateList(user.getProfiles());
	
			logger.info("The creation of the user [" 
						+ user.getId() + "] ]"
						+ user.getLogin() + "] [" 
						+ user.getDesc() +  "] is successful.");
			logger.info("Default profile [" + user.getDefaultProfile().getDesc() + "]");
			logger.info("Digested password [" + user.getPassword() + "]");
			logger.info("Salt password [" + user.getPasswordSalt() + "]");
		
			return user;
    	} catch (Exception e) {
    		String errMsg ="Error during the creation of the user [" + user.getLogin() + "]";
    		logger.error(errMsg);
    		throw new SecUserCreationException(errMsg, e);
    	}
    }
    
    /**
     * 
     * @param login
     * @param rawPwd
     * @return
     */
    private LdapUser createUserInLdap(String login, String rawPwd) {
    	LdapUser ldpUser = null;
    	try {
    		boolean isLdapMode = SeuksaAppConfigFileHelper.isSecurityLdapMode();
    		// if LDAP, create in LDAP 
    		logger.info(" [" + isLdapMode + "]");
    		if (isLdapMode) {
        		logger.info(" SecLdapService [" + SecurityHelper.SEC_LDAP_SRV.toString() + "]");
    			ldpUser = SecurityHelper.SEC_LDAP_SRV.createUser(login, rawPwd);
    		} 
    	} catch (Exception e) {
    		String errMsg ="Error SecLdapService [" + SecurityHelper.SEC_LDAP_SRV.toString() + "] - createUser [" + login + "]";
    		logger.error(errMsg);
    		throw new SecUserCreationException(errMsg, e);
    	}
    	
    	return ldpUser;
    }
    
    /**
     * Update the secUser.
     * 
     * @param secUser The secUser.
     * @throws IllegalArgumentException The secUser is null or id <= 0.
     */
    @Override
    public void update(SecUser secUser) {
    	if (secUser == null || secUser.getId() <= 0) {
        	throw new IllegalArgumentException("The secUser should not have an empty id.");
        }
        getDao().update(secUser);
    }
    
    /**
     * Update the secUser and change password if the new password is set.
     * 
     * @param secUser The secUser.
     * @param newPassword The new password.
     * @throws IllegalArgumentException The secUser is null or id <= 0.
     */
    @Override
    public void update(SecUser secUser, String newPassword) {
    	if (secUser == null || secUser.getId() <= 0) {
        	throw new IllegalArgumentException("The secUser should not have an empty id.");
        }
    	update(secUser);
    	
		if (StringUtils.isNotEmpty(newPassword)) {
			changePassword(secUser, newPassword);
		}
    }
    
    /**
     * Archive the secUser.<br/>
     * This method execute the {@link #removeUser} method.
     * 
     * @param secUser The secUser.
     */
    @Override
    public void archive(SecUser secUser) {
    	removeUser(secUser);
	}

    /**
     * Change the secUser password salt.
     * 
     * @param secUser The secUser.
     */
    @Override
    public void changePasswordSalt(SecUser secUser) {
    	secUser.setPasswordSalt(SecSaltGenerator.generateSaltFromUser(secUser));
        getDao().merge(secUser);
    }
    
    /**
     * Change the secUser password with validate the old password.
     * 
     * @param secUser The secUser.
     * @param oldRawPwd The old password.
     * @param newRawPwd The new password.
     * @throws BadCredentialsException The old password doesn't match current passowrd in database.
     */
    @Override
    public void changePassword(SecUser secUser, String oldRawPwd, String newRawPwd) {
    	// should decode ?? from the digested pwd
        String encodedOldPwd = passwordEncoder.encodePassword(oldRawPwd, saltSource.getSalt(secUser));
        if (!PasswordEncoderUtils.equals(secUser.getPassword(), encodedOldPwd)) {
        	throw new BadCredentialsException("The old password does not correspond to the existing one.");
        }
    	changePassword(secUser, newRawPwd);
    }

    /**
     * Change the secUser password.
     * 
     * @param secUser The secUser.
     * @param newRawPwd The new password.
     */
    @Override
    public void changePassword(SecUser secUser, String newRawPwd) {
        String encodedPwd = passwordEncoder.encodePassword(newRawPwd, saltSource.getSalt(secUser));
        secUser.setPassword(encodedPwd);
        secUser.setLastPasswordChangeDate(new Date());
        getDao().update(secUser);
    }

    /**
     * Add secProfile into secUser and update secUser.
     * 
     * @param secUser The secUser.
     * @param secPro The secProfile.
     */
	@Override
	public void addProfileToUser(SecUser secUser, SecProfile secPro) {
		// add in both bi-directionnal mapping
		secUser.getProfiles().add(secPro);
		secPro.getUsers().add(secUser);
		
		if (secUser.getDefaultProfile() == null) {
			secUser.setDefaultProfile(secPro);
		}
		update(secUser);
	}
	
	/**
     * Add secApplication into secProfile and update secProfile.
     * 
     * @param secApp The secApplication.
     * @param secPro The secProfile.
     */
	@Override
	public void addApplicationToProfile(SecApplication secApp, SecProfile secPro) {
		// add in both bi-directionnal mapping
		secApp.getProfiles().add(secPro);
		secPro.getApplications().add(secApp);
		update(secPro);
	}

	/**
	 * Remove by archiving the user
	 *  (by ARCHI status + by renaming the login)  
	 * @param secUser The secUser.
	 * @return
	 */
	@Override
	public boolean removeUser(SecUser secUser) {
		if (secUser == null) {
			return false;
		}
    	// trick to archive the user (the login is unique)
		try {
			String login = EStatusRecord.ARCHI + "|" + DateUtils.todayFullNoSeparator() + "|" + secUser.getLogin();
			if (login != null && login.length() > 50) {
				secUser.setComment(login);
				login = login.substring(0, 50);
			}
			secUser.setLogin(login);
			secUser.setStatusRecord(EStatusRecord.ARCHI);
	    	dao.saveOrUpdate(secUser);
		} catch (Exception e) {
			logger.error("Error while removing user [" + secUser.getLogin() + "]", e);
			return false;
		}

    	return true;
	}

	/**
	 * Force remove the secUser.
	 * 
	 * @param secUser The secUser.
	 * @param entitySecLog The entitySecLog Class.
	 * 
	 * @return
	 */
	@Override
	public <T extends SecUserAware> boolean forceRemoveUser(SecUser secUser, Class<T> entitySecLog)  {
		if (secUser == null || secUser.getId() == null) {
			return false;
		}
		try {
			// employee
			Criteria criteriaEmployee = getDao().createCriteria(entitySecLog);
			criteriaEmployee.add(Restrictions.eq("secUser.id", secUser.getId()));
			
			List<T> entities = criteriaEmployee.list();
			if (entities != null && entities.size() > 0) {
				for (T emp : entities) {
					emp.setSecUser(null);
					getDao().saveOrUpdate(emp);
				}
			}

			if (secUser.getProfiles() != null && secUser.getProfiles().size() > 0) {
				getDao().deleteList(secUser.getProfiles());
			}
			getDao().delete(secUser);

		} catch (Exception e) {
			logger.error("Error while removing user [" + secUser.getLogin() + "]", e);
			return false;
		}
		return true;
	}

	
	@Override
	public List<SecControlProfilePrivilege> listControlsPrivilegesByProfileIdAndAppId(Long profileId, Long appId) {
		SecControlProfilePrivilegeRestriction restrictions = new SecControlProfilePrivilegeRestriction();
		restrictions.setAppId(appId);
		restrictions.setProfileId(profileId);
		return super.list(restrictions);
	}

	@Override
	public List<SecControlProfilePrivilege> listControlsPrivilegesByParentControlAndProfileIdAndAppId(Long parentControlId, Long profileId, Long appId) {
		SecControlProfilePrivilegeRestriction restrictions = new SecControlProfilePrivilegeRestriction();
		restrictions.setAppId(appId);
		restrictions.setProfileId(profileId);
		restrictions.setParentControlId(parentControlId);
		return super.list(restrictions);
	}

	@Override
	public List<SecControlPrivilege> listControlsPrivileges(SecControl control) {
		SecControlPrivilegeRestriction restrictions = new SecControlPrivilegeRestriction();
		restrictions.setControl(control);
		return super.list(restrictions);
	}

	@Override
	public List<SecControlProfilePrivilege> listControlsPrivilegesByProfileId(Long profileId) {
		return listControlsPrivilegesByProfileIdAndAppId(profileId, null);
	}
	
	@Override
	public List<SecControl> listControlsByParentId(Long parentId) {
		SecControlRestriction restrictions = new SecControlRestriction();
		restrictions.setParentId(parentId);
		return super.list(restrictions);
	}
	
	@Override
	public List<SecControl> listControlsByParentIdAndProfile(Long parentId, Long profileId) {
		SecControlRestriction restrictions = new SecControlRestriction();
		restrictions.setParentId(parentId);
		restrictions.setProfileId(profileId);
		return super.list(restrictions);
	}

	@Override
	public List<SecControl> listParentControlsByAppId(Long appId) {
		SecControlRestriction restrictions = new SecControlRestriction();
		restrictions.setAppId(appId);
		restrictions.setParentOnly();
		return super.list(restrictions);
	}
	
	@Override
	public List<SecControl> listParentControlsByAppIdAndProfile(Long appId, Long profileId) {
		SecControlRestriction restrictions = new SecControlRestriction();
		restrictions.setAppId(appId);
		restrictions.setProfileId(profileId);
		restrictions.setParentOnly();
		return super.list(restrictions);
	}

	@Override
	public List<SecControl> listControlsByAppId(Long appId) {
		SecControlRestriction restrictions = new SecControlRestriction();
		restrictions.setAppId(appId);
		return super.list(restrictions);
	}
	
	@Override
	public List<SecControl> listControlsByAppIdAndProfile(Long appId, Long profileId) {
		SecControlRestriction restrictions = new SecControlRestriction();
		restrictions.setProfileId(profileId);
		restrictions.setAppId(appId);
		return super.list(restrictions);
	}
	
	
	@Override
	public SecControl findControlByAppId(String ctlCode, Long appId) {
		SecControlRestriction restrictions = new SecControlRestriction();
		restrictions.setControlCode(ctlCode);
		restrictions.setAppId(appId);
        return dao.getUnique(restrictions);
	}

	@Override
	public SecControl findControlByAppCode(String ctlCode, String appCode) {
		SecControlRestriction restrictions = new SecControlRestriction();
		restrictions.setControlCode(ctlCode);
		restrictions.setAppCode(appCode);
        return dao.getUnique(restrictions);
	}

	@Override
	public List<SecProfile> listProfiles() {
		SecProfileRestriction restriction = new SecProfileRestriction();
		return dao.list(restriction);
	}
	
	@Override
	public List<SecProfile> listProfilesNotAdmin() {
		SecProfileRestriction restriction = new SecProfileRestriction();
		restriction.setFetchOnlyNormalProfiles();
		return dao.list(restriction);
	}

	@Override
	public List<SecProfile> listProfilesByManagedProfile(Long profileId) {
		BaseRestrictions<SecProfile> restrictions = new BaseRestrictions<SecProfile>(SecProfile.class);
		List<Criterion> criterions = new ArrayList<Criterion>();
		
		criterions.add(Restrictions.ne("id", profileId));
		
		DetachedCriteria subCriteria = DetachedCriteria.forClass(SecManagedProfile.class);
		subCriteria.add(Restrictions.eq("parent.id", profileId));
		subCriteria.setProjection(Projections.property("child.id") );
		criterions.add(Subqueries.propertyIn("id", subCriteria));
		
		restrictions.setCriterions(criterions);
		return list(restrictions);
	}
	
	@Override
	public SecManagedProfile findManagedProfile(Long mainProId, Long managedProId) {
		BaseRestrictions<SecManagedProfile> restrictions = new BaseRestrictions<SecManagedProfile>(SecManagedProfile.class);
		List<Criterion> criterions = new ArrayList<Criterion>();
		
		criterions.add(Restrictions.eq("parent.id", mainProId));
		criterions.add(Restrictions.eq("child.id", managedProId));
		
		restrictions.setCriterions(criterions);
		return getUnique(restrictions);
	}
	
	/**
	 * 
	 * @param childId
	 * @return
	 */
	public List<SecManagedProfile> listManagedProfileByChild(Long childId) {
		BaseRestrictions<SecManagedProfile> restrictions = new BaseRestrictions<SecManagedProfile>(SecManagedProfile.class);
		List<Criterion> criterions = new ArrayList<Criterion>();
		
		criterions.add(Restrictions.eq("child.id", childId));
		
		restrictions.setCriterions(criterions);
		return list(restrictions);
	}

	@Override
	public List<SecManagedProfile> listManagedProfileByParent(Long parentId) {
		BaseRestrictions<SecManagedProfile> restrictions = new BaseRestrictions<SecManagedProfile>(SecManagedProfile.class);
		List<Criterion> criterions = new ArrayList<Criterion>();
		
		criterions.add(Restrictions.eq("parent.id", parentId));
		
		restrictions.setCriterions(criterions);
		return list(restrictions);
	}
	
	@Override
	public void saveOrUpdateControl(SecControl ctl) {
//		boolean isNew = ctl.getId() == null || ctl.getId() == 0;
		dao.saveOrUpdate(ctl);
		initControlPrivilege(ctl);
//		dao.saveOrUpdate(ctl);
	}
	
	@Override
	public void deleteControl(SecControl ctl) {
		List<SecControlPrivilege> ctlPrivileges = ctl.getControlPrivileges();
    	for (SecControlPrivilege ctlPri : ctlPrivileges) {
			dao.delete(ctlPri);
		}
    	List<SecControlProfilePrivilege> ctlProPrivileges = ctl.getControlProfilePrivileges();
    	for (SecControlProfilePrivilege ctlProPri : ctlProPrivileges) {
			dao.delete(ctlProPri);
		}
		dao.delete(ctl);
	}


	@Override
	public List<SecControl> listControls(ESecControlType type) {
		return listControls(null, type);
	}
	
	@Override
	public List<SecControl> listControls(SecApplication app, ESecControlType type) {
		SecControlRestriction restrictions = new SecControlRestriction();
		restrictions.setAppCode(app.getCode());
		restrictions.setType(type);
		return super.list(restrictions);
	}
}