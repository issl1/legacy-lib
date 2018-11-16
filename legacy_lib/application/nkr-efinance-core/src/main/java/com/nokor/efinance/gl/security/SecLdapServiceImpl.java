package com.nokor.efinance.gl.security;

import java.util.HashMap;
import java.util.Map;

import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.efinance.third.finwiz.client.FinwizSecurityHelper;
import com.nokor.frmk.security.ldap.SecLdapException;
import com.nokor.frmk.security.ldap.model.LdapUser;
import com.nokor.frmk.security.ldap.service.SecLdapService;
import com.nokor.frmk.security.model.SecProfile;

/**
 * 
 * @author prasnar
 *
 */
@Service("glSecLdapService")
public class SecLdapServiceImpl extends BaseEntityServiceImpl implements SecLdapService {
	/** */
	private static final long serialVersionUID = -4573776482149454986L;

	@Autowired
    private EntityDao dao;
	
	public final static String LDAP_PROFILE_CM_STAFF  = "CM_STAFF_ROLE"; 
	public final static String LDAP_PROFILE_CM_LEADER  = "CM_LEADER_ROLE"; 

	public final static String PROFILE_CM_STAFF  = "CMSTAFF"; 
	public final static String PROFILE_CM_LEADER  = "CMLEADE"; 
	

	private final static Map<String, String> mapSecProfiles = new HashMap<>();
	
	static {
		mapSecProfiles.put(LDAP_PROFILE_CM_STAFF, PROFILE_CM_STAFF);
		mapSecProfiles.put(LDAP_PROFILE_CM_LEADER, PROFILE_CM_LEADER);
	}
	
	@Override
	public BaseEntityDao getDao() {
		return dao;
	}

	/**
	 * 
	 */
	public SecLdapServiceImpl() {
		
	}
	
	@Override
	public LdapUser createUser(String login, String rawPwd) {
		LdapUser user = FinwizSecurityHelper.createUser(login, rawPwd);
		if (user == null) {
			throw new SecLdapException("SecLdap - Can not create the user [" + login + "]");
		}
		return user;
	}
	
	@Override
	public LdapUser authenticateUser(String login, String rawPwd) {
		LdapUser user = FinwizSecurityHelper.authenticateUser(login, rawPwd);
		if (user == null) {
			throw new SecLdapException("SecLdap - Can not authenticate the user [" + login + "]");
		}
		// TODO - to change later when GL will applied the multi-roles
//		if (user.getProfiles().size() == 0) {
//			if (user.getLogin().toLowerCase().contains("staff")) {
//				user.addProfile(LDAP_PROFILE_CM_STAFF);
//			}
//			else if (user.getLogin().toLowerCase().contains("leader")) {
//				user.addProfile(LDAP_PROFILE_CM_LEADER);
//			}
//		}
		return user;
	}

	@Override
	public boolean checkUserByUsername(String username) {
		throw new IllegalStateException("Not implemented yet");
	}

	@Override
	public LdapUser loadUserByUsername(String username) {
		throw new IllegalStateException("Not implemented yet");
	}

	@Override
	public SecProfile convertToESecProfileFromLdap(String ldapProfile) {
		String proCode = mapSecProfiles.get(ldapProfile);
		if (proCode != null) {
			logger.debug("The LDAP profile [" + ldapProfile + " ] is mapped successfully.");
		} else {
			logger.debug("The LDAP profile [" + ldapProfile + " ] can not be mapped.");
		}
		
		SecProfile profile = dao.getByCode(SecProfile.class, proCode);
		if (profile == null) {
			logger.debug("The secProfile [" + proCode + " ] can not be found.");
		}
		return profile;
	}
}
