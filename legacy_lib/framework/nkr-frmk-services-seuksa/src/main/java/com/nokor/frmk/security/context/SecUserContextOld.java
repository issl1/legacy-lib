package com.nokor.frmk.security.context;


import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.nokor.frmk.helper.SeuksaServicesHelper;
import com.nokor.frmk.security.model.SecControlProfilePrivilege;
import com.nokor.frmk.security.model.SecUser;

/**
 * The context of the SecUser
 * 
 * @author prasnar
 */
public class SecUserContextOld implements Serializable, SeuksaServicesHelper {
	/** */
	private static final long serialVersionUID = 8190642216531350716L;

	private static final Logger logger = LoggerFactory.getLogger(SecUserContextOld.class);

	protected static final String ATTR_CONNECTION_DATE_TIME = "@connectionDatetime@";
	protected static final String ATTR_LOCALE = "@locale@";
	protected static final String ATTR_SEC_APP = "@secApp@";

	public static final String SESS_KEY_CONTROL_PROFILE_PRIVILEGES = "@controlProfilePrivileges@";

    private Map<String, Object> attributes = null;
    
    /**
     * 
     */
    public SecUserContextOld() {
    	attributes = new HashMap<String, Object>();
        setCreated(new Date());
    }
    
    
    /**
     * 
     */
	public static void logout() {
		SecUserContextHolderOld.getInstance().getContext().clear();
		SecUserContextHolderOld.clearContext();

		SecurityContext context = SecurityContextHolder.getContext();
		context.setAuthentication(null);
		SecurityContextHolder.clearContext();
	}
	/**
	 * 
	 */
	public void clear() {
		 attributes = new HashMap<String, Object>();
		 setCreated(null);
	}

	
	/**
	 * @return the locale
	 */
	public Locale getLocale() {
    	if (getAttribute(ATTR_LOCALE) != null) {
    		return (Locale) getAttribute(ATTR_LOCALE);
    	} else {
    		return Locale.getDefault();
    	}
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(Locale locale) {
		attributes.put(ATTR_LOCALE, locale);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void putAttribute(String key, Object value) {
		attributes.put(key, value);
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getAttribute(String key) {
		return attributes.get(key);
	}
	
	/**
	 * 
	 * @param key
	 */
	public void removeAttribute(String key) {
		attributes.remove(key);
	}
    
	/**
	 * 
	 * @return
	 */
	public static Boolean isAuthenticated() {
		return SecUserContextHolderOld.isAuthenticated();
	}
	
	/**
     * 
     * @return
     */
	public static SecUser getSecUser() {
		return SecUserContextHolderOld.getSecUser(); 
	}
	
	/**
	 * @return the created
	 */
	public Date getCreated() {
		return (Date) attributes.get(ATTR_CONNECTION_DATE_TIME);
	}
	
	/**
	 * 
	 * @param created
	 */
	public void setCreated(Date created) {
		attributes.put(ATTR_CONNECTION_DATE_TIME, created);
	}
	

	/**
	 * 
	 * @return
	 */
	public List<SecControlProfilePrivilege> getControlProfilePrivileges() {
		List<SecControlProfilePrivilege> controlProfilePrivileges = (List<SecControlProfilePrivilege>) attributes.get(SESS_KEY_CONTROL_PROFILE_PRIVILEGES);
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
		attributes.put(SESS_KEY_CONTROL_PROFILE_PRIVILEGES, controlProfilePrivileges);
	}
	
	
}
