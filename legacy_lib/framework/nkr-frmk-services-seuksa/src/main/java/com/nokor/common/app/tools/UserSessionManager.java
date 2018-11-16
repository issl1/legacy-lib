package com.nokor.common.app.tools;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.nokor.frmk.helper.SeuksaServicesHelper;
import com.nokor.frmk.security.context.SecApplicationContext;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.security.model.SecApplication;
import com.nokor.frmk.security.model.SecUser;

/**
 * 
 * @author prasnar
 *
 */
@Service("sessionManager")
public class UserSessionManager implements Serializable, HttpSessionBindingListener, SeuksaServicesHelper { 
	/** */
	private static final long serialVersionUID = -8322227346223303295L;
	
    protected static Logger logger = LoggerFactory.getLogger(UserSessionManager.class);

	private final Map<SecUser, List<HttpSession>> logins = new HashMap<>();

	
	/**
	 * 
	 */
	public UserSessionManager() {
	}
	
	/**
	 * 
	 */
	public void init() {
		logger.info("*************Init*************");
	}
	
	/**
	 * @see javax.servlet.http.HttpSessionBindingListener#valueBound(javax.servlet.http.HttpSessionBindingEvent)
	 */
	@Override
    public void valueBound(HttpSessionBindingEvent event) {
		logger.info("***valueBound***");
	}

	/**
	 * @see javax.servlet.http.HttpSessionBindingListener#valueUnbound(javax.servlet.http.HttpSessionBindingEvent)
	 */
	@Override
    public void valueUnbound(HttpSessionBindingEvent event) {
		logger.info("***valueUnbound***");
	}
	
	/**
	 * 
	 * @return
	 */
	public UserSession getCurrent() {
		UserSession session = (UserSession) SessionStrategy.getSession();
		if (session == null) {
			session = new UserSession();
			session.start();
			SessionStrategy.setSession(session);
		}
		return session;
	}
	
	/**
	 * 
	 */
	private void invalidateCurrent() {
//		flush a user
		SessionInfo session = SessionStrategy.getSession();
		if (session != null) {
			session.end();
			SessionStrategy.removeSession();
		}
		SecurityContextHolder.clearContext();
	}
	

	/**
	 * 
	 */
	public void logoutCurrent() {
		invalidateCurrent();
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isAuthenticated() {
		return UserSession.isAuthenticated();
	}

	/**
	 * 
	 * @return
	 */
	public static SecUser getSecUser() {
		return UserSession.getSecUser();
	}
	
	/**
	 * 
	 * @return
	 */
	public static SecUser getCurrentUser() {
		return UserSession.getSecUser();
	}
		

	/**
	 * 
	 * @return
	 */
	public static SecApplicationContext getApplicationContext() {
		return SecApplicationContextHolder.getContext();
	}
	
	/**
	 * 
	 * @return
	 */
	public static SecApplication getApplication() {
		return getApplicationContext().getSecApplication();
	}

	
	/**
	 * 
	 */
	public void invalidateAll() {
//		flush all users
	}
}
