package com.nokor.common.app.tools;

import java.util.List;
import java.util.Locale;

import org.springframework.security.core.context.SecurityContextHolder;

import com.nokor.frmk.helper.SeuksaServicesHelper;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.security.model.SecControlProfilePrivilege;
import com.nokor.frmk.security.model.SecUser;

/**
 * 
 * @author prasnar
 *
 */
public class UserSession extends SessionInfo implements UserSessionKeys, SeuksaServicesHelper {
	/** */
	private static final long serialVersionUID = 8207791651757677901L;

    /**
     * 
     */
    public UserSession() {
    	start();
    }

    /**
     * 
     */
    @Override
    public void start() {
    	super.start();
    }
    
    /**
     * 
     */
    @Override
    public void end() {
    	super.end();
       	SecurityContextHolder.clearContext();
    }
    
	/**
     * 
     * @return
     */
	public static SecUser getSecUser() {
		try {
			return (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (Exception e) {
//			logger.warn("Not authenticated user.");
			return null;
		}
	}
	
    /**
	 * 
	 * @return
	 */
	public static boolean isAuthenticated() {
		return SecurityContextHolder.getContext().getAuthentication() != null
				&& SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null
				&& SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof SecUser
				&& !((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).isAnonymous();
				//SecUserContextHolderOld.getContext().isAuthenticated();
	}
	


	/**
	 * @return the created
	 */
	public Locale getLocale() {
		Locale locale = (Locale) attributes.get(KEY_I18N_LOCALE);
		return locale != null ? locale : SecApplicationContextHolder.getContext().getLocale();
	}
	
	/**
	 * 
	 * @param locale
	 */
	public void setLocale(Locale locale) {
		attributes.put(KEY_I18N_LOCALE, locale);
	}
	

	/**
	 * 
	 * @return
	 */
	public List<SecControlProfilePrivilege> getControlProfilePrivileges() {
		List<SecControlProfilePrivilege> controlProfilePrivileges = (List<SecControlProfilePrivilege>) attributes.get(KEY_CONTROL_PROFILE_PRIVILEGES);
		if (controlProfilePrivileges == null) {
			controlProfilePrivileges = SECURITY_SRV.listControlsPrivilegesByProfileIdAndAppId(getSecUser().getDefaultProfile().getId(), SecApplicationContextHolder.getContext().getSecApplication().getId());
			setControlProfilePrivileges(controlProfilePrivileges);
		}
		return controlProfilePrivileges;
	}
	
	/**
	 * 
	 * @param controlProfilePrivileges
	 */
	public void setControlProfilePrivileges(List<SecControlProfilePrivilege> controlProfilePrivileges) {
		attributes.put(KEY_CONTROL_PROFILE_PRIVILEGES, controlProfilePrivileges);
	}
	
	

}
