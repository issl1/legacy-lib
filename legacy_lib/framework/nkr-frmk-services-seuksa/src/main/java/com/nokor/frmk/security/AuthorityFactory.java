package com.nokor.frmk.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.nokor.frmk.security.model.SecProfile;
import com.nokor.frmk.security.service.SecurityService;


/**
 * Same as org.springframework.security.core.authority.SimpleGrantedAuthority
 * 
 * @author prasnar
 */
public class AuthorityFactory {

    private final static Logger logger = LoggerFactory.getLogger(AuthorityFactory.class);
    
//    public static final String ADMINISTRATOR_ROLE = SecProfile.DEFAULT_ADMIN_PROFILE.startsWith(SPRING_SEC_PREF_ROLE) 
//    														? SecProfile.DEFAULT_ADMIN_PROFILE : SPRING_SEC_PREF_ROLE + SecProfile.DEFAULT_ADMIN_PROFILE;
//    public static final String AUTHENTICATED_USER = SPRING_SEC_PREF_ROLE + "AuthUser";

   
    @Autowired
    private SecurityService securitySrv = null;
    
    private static AuthorityFactory instance;
    
    /**
     * 
     * @return
     */
    public static AuthorityFactory getInstance() {
    	if (instance == null) {
    		instance = new AuthorityFactory();
    	}
    	return instance;
    }
    
//    /**
//     * 
//     * @return
//     */
//	public static SecProfileGrantedAuthority AUTHORITY_AUTHENTICATED_USER() {
//		return getInstance().grantAuthority(AUTHENTICATED_USER);
//	}
//
//	/**
//	 * 
//	 * @return
//	 */
//	public static SecProfileGrantedAuthority AUTHORITY_SUPER_ADMIN() {
//		return getInstance().grantAuthority(ADMINISTRATOR_ROLE);
//	}

    /**
     * 
     * @param profile
     */
    public SecProfileGrantedAuthority grantAuthority(SecProfile profile) {
    	SecProfileGrantedAuthority auth = new SecProfileGrantedAuthority();
        Assert.notNull(profile, "A granted authority (SecProfile) is required");
        auth.setProfile(profile);
        
        return auth;
    }
   

}
