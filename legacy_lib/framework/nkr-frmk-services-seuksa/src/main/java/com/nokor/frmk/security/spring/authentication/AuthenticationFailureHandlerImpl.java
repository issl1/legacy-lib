package com.nokor.frmk.security.spring.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.nokor.frmk.security.spring.MarkerResponseHandler;

/**
 * @author ly.youhort
 */
public class AuthenticationFailureHandlerImpl extends MarkerResponseHandler implements AuthenticationFailureHandler {
    
	private static final Logger LOG = LoggerFactory.getLogger(AuthenticationFailureHandlerImpl.class);

    public AuthenticationFailureHandlerImpl(){
        setMarkerSnippet(LOGIN_REQUIRED_MARKER);
    }

    /**
     * @see org.springframework.security.web.authentication.AuthenticationFailureHandler#onAuthenticationFailure(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.AuthenticationException)
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        if (LOG.isDebugEnabled()) {
        	LOG.debug("Responded with LOGIN_REQUIRED_MARKER");
        }
        handle(httpServletRequest,httpServletResponse,e);
    }
}
