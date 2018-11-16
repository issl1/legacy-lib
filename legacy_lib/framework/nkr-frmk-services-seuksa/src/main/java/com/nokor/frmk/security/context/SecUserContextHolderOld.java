package com.nokor.frmk.security.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;

import com.nokor.frmk.security.SecurityHelper;
import com.nokor.frmk.security.model.SecUser;

/**
 * 
 * @author prasnar
 *
 */
public class SecUserContextHolderOld {
    private static Map<Long, SecUserContextOld> mapContext = new HashMap<Long, SecUserContextOld>();

    @Autowired
	private SessionRegistry sessionRegistry;

    /**
     * 
     * @return
     */
    public static SecUserContextHolderOld getInstance() {
    	return new SecUserContextHolderOld();
    }
    
    /**
     * 
     */
    public List<SecUser> getConnectedUsers() {
    	List<Object> principals = sessionRegistry.getAllPrincipals();
    	
    	List<SecUser> users = new ArrayList<SecUser>();
    	for (Object principal: principals) {
    		users.add((SecUser) principal);
    	}
    	
    	return users;
    }
    
    /**
     * 
     */
    public static void clearContext() {
    	Long keyContext = getKeyContext(); 
    	SecUserContextOld currentCtxt = mapContext.get(keyContext);
    	if (currentCtxt != null) {
    		mapContext.remove(keyContext);
    	}
    }
    
    /**
	 * 
	 * @return
	 */
	public static boolean isAuthenticated() {
		return SecurityContextHolder.getContext().getAuthentication() != null
				&& SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null
				&& SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof SecUser;
				//SecUserContextHolderOld.getContext().isAuthenticated();
	}
	
	/**
     * 
     * @return
     */
	public static SecUser getSecUser() {
		return isAuthenticated() ? (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal() : null; 
	}

	
    /**
     * 
     * @return
     */
    public SecUserContextOld getContext() {
    	Long keyContext = getKeyContext(); 
    	SecUserContextOld currentCtxt = mapContext.get(keyContext);
        if (currentCtxt == null) {
        	currentCtxt = createEmptyContext();
        	mapContext.put(keyContext, currentCtxt);
        }

        return currentCtxt;
    }

 
    /**
     * 
     * @return
     */
    public static SecUserContextOld createEmptyContext() {
    	Long keyContext = getKeyContext(); 
    	SecUserContextOld currentCtxt = mapContext.get(keyContext);
    	if (currentCtxt != null) {
    		mapContext.remove(keyContext);
    	}
        return new SecUserContextOld();
    }
    
 
    /**
     * 
     * @return
     */
    private static Long getKeyContext() {
    	SecUser secUser = getSecUser();
    	Long keyContext = secUser != null ? secUser.getId() : SecurityHelper.DEFAULT_ANONYMOUS_ID; 
    	return keyContext;
    }
}

