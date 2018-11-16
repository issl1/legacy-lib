package com.nokor.frmk.security.ldap.service;

import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.frmk.security.ldap.model.LdapUser;
import com.nokor.frmk.security.model.SecProfile;

/**
 * 
 * @author prasnar
 *
 */
public interface SecLdapService extends BaseEntityService {
	LdapUser createUser(String login, String rawPwd);
	LdapUser authenticateUser(String login, String rawPwd);
	boolean checkUserByUsername(String username);
	LdapUser loadUserByUsername(String username);
	SecProfile convertToESecProfileFromLdap(String ldapProfile);
}
