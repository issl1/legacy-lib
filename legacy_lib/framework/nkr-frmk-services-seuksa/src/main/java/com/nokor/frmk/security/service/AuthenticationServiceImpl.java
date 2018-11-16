package com.nokor.frmk.security.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Provider;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.security.access.event.AuthorizationFailureEvent;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nokor.frmk.helper.SeuksaAppConfigFileHelper;
import com.nokor.frmk.helper.SeuksaServicesHelper;
import com.nokor.frmk.helper.SeuksaSettingConfigHelper;
import com.nokor.frmk.security.SecurityHelper;
import com.nokor.frmk.security.dao.SecurityDao;
import com.nokor.frmk.security.ldap.SecLdapException;
import com.nokor.frmk.security.ldap.model.LdapUser;
import com.nokor.frmk.security.model.SecApplication;
import com.nokor.frmk.security.model.SecControlProfilePrivilege;
import com.nokor.frmk.security.model.SecProfile;
import com.nokor.frmk.security.model.SecUser;



/**
 * Responsible for user authentication with Spring Security and application.
 * {@link UserDetailsService} Spring Security
 * 
 * @author prasnar
 *
 */
@Service("authenticationService")
public class AuthenticationServiceImpl implements AuthenticationService, SeuksaServicesHelper {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	
    @Autowired
    private SecurityDao dao;

	
	/** 
	 * javax.inject.Provider: make possible Spring beans against circular references
	 */
	@Autowired
	private Provider<AuthenticationManager> authenticationManager;

	/**
	 * Call by Spring Security
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		SecUser secUser = null;
		try {
			secUser = SECURITY_SRV.loadUserByUsername(username);
		} catch (Exception e) {
            throw new UsernameNotFoundException("The user " + username + "] can not be found.", e);
		}
        if (secUser == null) {
            throw new UsernameNotFoundException("The user " + username + "] can not be found.");
        }
        final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(secUser.getAuthorities());
        if (authorities.size() == 0) {
        	 throw new InsufficientAuthenticationException("NO_ROLE_AVAILABLE");
        }
        if (authorities.size() > 0 ) {
        	boolean anonymousOnly = true;
        	for (GrantedAuthority grantedAuthority : authorities) {
        		logger.debug("- " +  grantedAuthority.getAuthority());
        		anonymousOnly = grantedAuthority.getAuthority().toLowerCase().contains("anonymous");
			}
        	if (anonymousOnly) {
        		throw new InsufficientAuthenticationException("ONLY_ANONYMOUS_ROLE_IS_FOUND");
        	}
        }
        
        SecApplication secApp = SecurityHelper.getSecApplication();
        SecProfile secPro = secUser.getDefaultProfile();
        for (SecProfile profile : secUser.getProfiles()) {
        	List<SecControlProfilePrivilege> list = profile.getControlProfilePrivileges();
        	for (SecControlProfilePrivilege secControlProfilePrivilege : list) {
        		Hibernate.initialize(secControlProfilePrivilege);
			}
		} 
        
        // check security tables
        if (SecurityHelper.checkSecurityTableUserProfile() 
        		&& !SECURITY_SRV.checkProfileInUserProfile(secUser, secPro)) {
        	throw new InsufficientAuthenticationException("The user is not found in [UserProfile]");
        }
        if (SecurityHelper.checkSecurityTableApplicationProfile() 
        		&& !SECURITY_SRV.checkUserInProfileApplication(secUser, secApp)) {
        	throw new InsufficientAuthenticationException("The user is not found in [ApplicationProfile]");
        }
        return secUser;
	}
	
	/**
	 * Manual call to authenticate
	 * Authentication can be customized here
	 * 
	 * @see com.nokor.frmk.security.service.AuthenticationService#authenticate(java.lang.String, java.lang.String)
	 */
	@Override
	public SecUser authenticate(String login, String password) {
        try {
        	SecUser user;
    		// if LDAP, load from LDAP 
    		if (SeuksaAppConfigFileHelper.isSecurityLdapExceptAdmin(login)) {
    			user = authenticateLdap(login, password);
    		} else {
    			user = authenticateNormal(login, password);
    		}
    		
    		if (user != null 
            		&& user.isLockedOut()
            		&& !user.isEnabled()) {
                throw new LockedException("USER_ACCOUNT_HAS_BEEN_LOCKED");
            }
            
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(login, password);
            Authentication auth = authenticationManager.get().authenticate(token);
            if (auth != null) {
                SecurityContextHolder.getContext().setAuthentication(auth);
                clearFailedLoginAttempts(user);
            } else {
                incrementFailedLoginAttempts(login);
            }
        
    		return user;
        } catch (AuthenticationException ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        }
       
	}
	
	@Override
	public void logout() {
		SESSION_MNG.logoutCurrent();
	}
	
	/**
	 * 
	 * @param login
	 * @param password
	 * @return
	 */
	private SecUser authenticateLdap(String login, String password) {
		LdapUser ldpUser = SecurityHelper.SEC_LDAP_SRV.authenticateUser(login, password);
		if (ldpUser == null) {
			throw new InsufficientAuthenticationException("Can not authenticate the ldap user");
		}
		SecUser user = SECURITY_SRV.loadUserByUsername(login);
		if (user == null) {
			logger.debug("The user can not be found [" + login + "]");
			SecProfile profile  = null;
			if (ldpUser.getDefaultProfile() != null) {
				profile = SecurityHelper.SEC_LDAP_SRV.convertToESecProfileFromLdap(ldpUser.getDefaultProfile());
			}
			logger.debug("The user wil be created in a local with the default profile [" + (profile != null ? profile : "<none>") + "]");
			user = SECURITY_SRV.createUser(login, login, password, profile, false);
			logger.debug("The user [" + login + "] is created");
		}
		return user;
	}
	
	/**
	 * 
	 * @param login
	 * @param password
	 * @return
	 */
	private SecUser authenticateNormal(String login, String password) {
		// if OK normal flow
		SecUser user = SECURITY_SRV.loadUserByUsername(login);
		if (user == null) {
			throw new SecLdapException("The user can not be found [" + login + "]");
		}
		
        return user;
	}
	
	/**
	 * 
	 * @param userAccount
	 */
	private void clearFailedLoginAttempts(SecUser secUser) {
        if (secUser.getFailedPasswordAnswerAttemptsCount() > 0) {
        	secUser.clearFailedPasswordAnswerAttemptsCount();
        	dao.saveOrUpdate(secUser);
        }
    }
	
	/**
	 * 
	 * @param username
	 */
	private void incrementFailedLoginAttempts(String username) {
		final SecUser secUser = SECURITY_SRV.loadUserByUsername(username);
        if (secUser == null) {
            return;
        }
        secUser.incrementFailedPasswordAnswerAttemptsCount();

        if (secUser.getFailedPasswordAnswerAttemptsCount() > SeuksaSettingConfigHelper.getLoginNbMaxAttemps()) {
            secUser.setLockedOut(true);
        }
        dao.saveOrUpdate(secUser);
	}
    

	@Override
	public void onApplicationEvent(ApplicationEvent applicationEvent) {
		if (applicationEvent instanceof AuthenticationSuccessEvent) {
            AuthenticationSuccessEvent event = (AuthenticationSuccessEvent) applicationEvent;
            logger.info("[AuthenticationSuccessEvent] User " + event.getAuthentication().getName() + " has been successfully authenticated");
        } else if (applicationEvent instanceof AuthenticationFailureBadCredentialsEvent) {
        	logger.error("[AuthenticationFailureBadCredentialsEvent] User has not been authenticated due to bad credentials");
        } else if (applicationEvent instanceof AuthorizationFailureEvent) {
            AuthorizationFailureEvent event = (AuthorizationFailureEvent) applicationEvent;
            logger.error("[AuthorizationFailureEvent] Unauthorized access to [" + event.getSource() + "] has been detected.");
        } else {
            logger.info("[" + applicationEvent.getClass().getSimpleName() + "] Unauthorized access to" + applicationEvent.getSource() + " has been detected.");
        }
	}
	
}
