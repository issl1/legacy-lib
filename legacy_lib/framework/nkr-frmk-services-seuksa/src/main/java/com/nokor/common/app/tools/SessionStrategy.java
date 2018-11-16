package com.nokor.common.app.tools;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.seuksa.frmk.tools.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 
 * @author prasnar
 *
 */
public class SessionStrategy implements UserSessionKeys {
	private static final Logger logger = LoggerFactory.getLogger(SessionStrategy.class);

	private static ESessionType sessionType = ESessionType.HTTP;
	private static Map<String, Object> sessions = new HashMap<>();

	/**
	 * 
	 * @return
	 */
	public static SessionInfo getSession() {
		if (sessionType.equals(ESessionType.HTTP)) {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
			return (UserSession) request.getSession().getAttribute(KEY_USER_SESSION);
		} else if (sessionType.equals(ESessionType.TEST)) {
			String key = UserSession.isAuthenticated() ? KEY_USER_SESSION + UserSession.getSecUser() : KEY_USER_SESSION + DateUtils.todayShort();
			return (SessionInfo) sessions.get(key);
		}
		throw new IllegalStateException("ESessionType[" + sessionType.getCode() + "] Not implemented yet");
	}


	/**
	 * 
	 * @param sessionClass
	 * @return
	 */
	public static <T extends SessionInfo> T createSession(Class<T> sessionClass) {
        try {
            T session = sessionClass.newInstance();
            session.start();
        	
            return session;
        } catch (Exception e) {
            logger.error("Cannot instantiate class " + sessionClass, e);
            throw new RuntimeException("Cannot instantiate class " + sessionClass, e);
        }
    }
	
	/**
	 * 
	 * @param session
	 */
	public static void setSession(SessionInfo session) {
		if (sessionType.equals(ESessionType.HTTP)) {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
			request.getSession().setAttribute(KEY_USER_SESSION, session);
		} else if (sessionType.equals(ESessionType.TEST)) {
			String key = UserSession.isAuthenticated() ? KEY_USER_SESSION + UserSession.getSecUser() : KEY_USER_SESSION + DateUtils.todayShort();
			sessions.put(key, session);
		} else {
			throw new IllegalStateException("ESessionType[" + sessionType.getCode() + "] Not implemented yet");
		}
	}

	/**
	 * 
	 */
	public static void removeSession() {
		if (sessionType.equals(ESessionType.HTTP)) {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
			request.getSession().removeAttribute(KEY_USER_SESSION);
			// Create a new session for the user.
			request.getSession(true);
		} else if (sessionType.equals(ESessionType.TEST)) {
			String key = UserSession.isAuthenticated() ? KEY_USER_SESSION + UserSession.getSecUser() : KEY_USER_SESSION + DateUtils.todayShort();
			sessions.remove(key);
		} else {
			throw new IllegalStateException("ESessionType[" + sessionType.getCode() + "] Not implemented yet");
		}
	}

	/**
	 * @return the sessionType
	 */
	public static ESessionType getSessionType() {
		return sessionType;
	}
	
	/**
	 * 
	 */
	public static void setSessionTEST() {
		SessionStrategy.sessionType = ESessionType.TEST;
	}

	/**
	 * @param sessionType
	 *            the sessionType to set
	 */
	public static void setSessionType(ESessionType sessionType) {
		SessionStrategy.sessionType = sessionType;
	}

	
}
