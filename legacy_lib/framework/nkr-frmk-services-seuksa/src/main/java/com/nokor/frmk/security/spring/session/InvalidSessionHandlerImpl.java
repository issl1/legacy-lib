package com.nokor.frmk.security.spring.session;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.frmk.security.spring.MarkerResponseHandler;


/**
 * @author ly.youhort
 */
public class InvalidSessionHandlerImpl extends MarkerResponseHandler implements InvalidSessionHandler {
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public InvalidSessionHandlerImpl(){
        setMarkerSnippet(LOGIN_REQUIRED_MARKER);
    }

    @Override
    public void sessionInvalidated(HttpServletRequest request, HttpServletResponse response) 
    	throws IOException, ServletException {
        if (logger.isDebugEnabled()) {
        	logger.debug("Responded with LOGIN_REQUIRED_MARKER");
        }
        //handle(request, response);
        throw new InvalidSessionException();
    }
}
