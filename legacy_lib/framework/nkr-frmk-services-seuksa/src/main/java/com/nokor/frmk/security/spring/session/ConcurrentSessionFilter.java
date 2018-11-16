package com.nokor.frmk.security.spring.session;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;



/**
 * @author ly.youhort
 */
public class ConcurrentSessionFilter extends GenericFilterBean {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private SessionRegistry sessionRegistry;
	private InvalidSessionHandler invalidSessionHandler;
	private LogoutHandler[] handlers = new LogoutHandler[] { new SecurityContextLogoutHandler() };

	public void afterPropertiesSet() {
		Assert.notNull(sessionRegistry, "SessionRegistry required");
		Assert.notNull(invalidSessionHandler, "InvalidSessionHandler required");
	}

	/**
	 * @param req
	 * @param res
	 * @param chain
	 * @throws IOException
	 * @throws ServletException
	 */
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		HttpSession session = request.getSession(false);
		
		if (session != null) {
			SessionInformation info = sessionRegistry.getSessionInformation(session.getId());
			
			logger.info("Session " + session.getId());
			
			if (info != null) {
				if (info.isExpired()) {
					
					logger.info("Session " + session.getId() + " has been expired");
					
					doLogout(request, response);
					if (invalidSessionHandler != null) {
						invalidSessionHandler.sessionInvalidated(request, response);
					} else {
						response.getWriter().print(
										"This session has been expired (possibly due to multiple concurrent "
												+ "logins being attempted as the same user).");
						response.flushBuffer();
					}
					return;
				} else {
					if (logger.isDebugEnabled()) {
						logger.info("Last request of session " + session.getId() + " : " + info.getLastRequest());
					}
					info.refreshLastRequest();
				}
			}
		}
		chain.doFilter(request, response);
	}

	/**
	 * @param request
	 * @param response
	 */
	private void doLogout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		for (int i = 0; i < handlers.length; i++) {
			handlers[i].logout(request, response, auth);
		}
	}

	/**
	 * @param invalidSessionHandler
	 */
	public void setInvalidSessionHandler(InvalidSessionHandler invalidSessionHandler) {
		this.invalidSessionHandler = invalidSessionHandler;
	}

	/**
	 * @param handlers
	 */
	public void setLogoutHandlers(LogoutHandler[] handlers) {
		Assert.notNull(handlers);
		this.handlers = handlers;
	}

	/**
	 * @param sessionRegistry
	 */
	public void setSessionRegistry(SessionRegistry sessionRegistry) {
		this.sessionRegistry = sessionRegistry;
	}
}
