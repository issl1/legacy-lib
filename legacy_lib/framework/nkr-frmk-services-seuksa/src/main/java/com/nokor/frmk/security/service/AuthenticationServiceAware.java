package com.nokor.frmk.security.service;

import com.nokor.frmk.security.model.SecUser;

/**
 *
 * @author prasnar
 */
public interface AuthenticationServiceAware {
    
    public SecUser authenticate(String username, String password);

	void logout();

}
