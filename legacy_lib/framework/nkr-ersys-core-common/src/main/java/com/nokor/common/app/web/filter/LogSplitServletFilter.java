package com.nokor.common.app.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.MDC;

import com.nokor.common.app.tools.UserSession;
import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.common.app.tools.helper.AppConfigFileHelper;


/**
 * 
 * @author prasnar
 * 
 */
public class LogSplitServletFilter implements Filter {
	private static final String LOG_SPLIT_BY_USER_KEY = "Username";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		boolean successfulRegistration = initLogUser();;

		try {
			filterChain.doFilter(request, response);
		} finally {
			if (successfulRegistration) {
				MDC.remove(LOG_SPLIT_BY_USER_KEY);
			}
		}

	}
	
	/**
	 * 
	 * @param username
	 * @return
	 */
	public static boolean initLogUser() {
		if (AppConfigFileHelper.isLogSplitByUser() && UserSession.isAuthenticated()) {
        	MDC.put(LOG_SPLIT_BY_USER_KEY, UserSessionManager.getCurrentUser().getLogin());
			return true;
        }
		return false;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	/**
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
	}

}