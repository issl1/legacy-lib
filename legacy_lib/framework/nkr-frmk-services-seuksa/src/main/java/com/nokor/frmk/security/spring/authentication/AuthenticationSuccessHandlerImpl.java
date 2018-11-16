package com.nokor.frmk.security.spring.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.nokor.frmk.security.spring.MarkerResponseHandler;

/**
 * @author ly.youhort
 */
public class AuthenticationSuccessHandlerImpl extends MarkerResponseHandler implements AuthenticationSuccessHandler {
	
	private static final Logger LOG = LoggerFactory.getLogger(AuthenticationSuccessHandlerImpl.class);
	
	/**
	 * 
	 */
    public AuthenticationSuccessHandlerImpl(){
        setMarkerSnippet(SUCCESS_MARKER);
    }
    
    /**
     * @see org.springframework.security.web.authentication.AuthenticationSuccessHandler#onAuthenticationSuccess(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.Authentication)
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) 
    	throws IOException, ServletException {
        if (LOG.isDebugEnabled()) {
        	LOG.debug("Responded with SUCCESS_MARKER");
        }
        handle(httpServletRequest, httpServletResponse, authentication);
    }
}
