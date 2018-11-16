package com.nokor.frmk.security.spring.session;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ly.youhort
 */
public interface InvalidSessionHandler {
	void sessionInvalidated(HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException;
}
