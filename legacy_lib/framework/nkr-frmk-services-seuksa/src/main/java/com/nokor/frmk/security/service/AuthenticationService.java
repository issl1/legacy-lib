package com.nokor.frmk.security.service;

import org.springframework.context.ApplicationListener;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Responsible for user authentication with Spring Security and application.
 * {@link UserDetailsService} Spring Security
 *
 * @author prasnar
 */
public interface AuthenticationService extends AuthenticationServiceAware, UserDetailsService, ApplicationListener {
    

}
