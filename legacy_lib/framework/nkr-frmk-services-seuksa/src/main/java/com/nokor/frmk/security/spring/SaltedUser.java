/**
 * 
 */
package com.nokor.frmk.security.spring;

import org.springframework.security.core.userdetails.UserDetails;


/**
 * @author prasnar
 * 
 */
public interface SaltedUser extends UserDetails {
	Long getId();
	String getLogin();
	String getPasswordSalt();	
	
}
