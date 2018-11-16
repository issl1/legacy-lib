package com.nokor.frmk.security.spring;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * @author ly.youhort
 */
public abstract class MarkerResponseHandler {
	
	private String markerSnippet;
	
	protected static final String LOGIN_REQUIRED_MARKER = "<SCRIPT>//'\"]]>>isc_loginRequired\n"
			+ "//\n"
			+ "// Embed this whole script block VERBATIM into your login page to enable\n"
			+ "// SmartClient RPC relogin.\n"
			+ "\n"
			+ "while (!window.isc && document.domain.indexOf(\".\") != -1) {\n"
			+ "    try {\n"
			+ "\t\n"
			+ "        if (parent.isc == null) {\n"
			+ "            document.domain = document.domain.replace(/.*?\\./, '');\n"
			+ "            continue;\n"
			+ "        } \n"
			+ "        break;\n"
			+ "    } catch (e) {\n"
			+ "        document.domain = document.domain.replace(/.*?\\./, '');\n"
			+ "    }\n"
			+ "}\n"
			+ "\n"
			+ "var isc = top.isc ? top.isc : window.opener ? window.opener.isc : null;\n"
			+ "if (isc) isc.RPCManager.delayCall(\"handleLoginRequired\", [window]);\n"
			+ "</SCRIPT>";

	protected final String SUCCESS_MARKER = "<SCRIPT>//'\"]]>>isc_loginSuccess\n"
			+ "//\n"
			+ "// When doing relogin with a webserver-based authenticator, protect this page with it and\n"
			+ "// target your login attempts at this page such that when the login succeeds, this page is\n"
			+ "// returned.\n"
			+ "//\n"
			+ "// If you are integrating with a web service that returns a fault, paste this entire script\n"
			+ "// block VERBATIM into the fault text.\n"
			+ "\n"
			+ "while (!window.isc && document.domain.indexOf(\".\") != -1) {\n"
			+ "    try {\n"
			+ "        if (parent.isc == null) {\n"
			+ "            document.domain = document.domain.replace(/.*?\\./, '');\n"
			+ "            continue;\n"
			+ "        } \n"
			+ "        break;\n"
			+ "    } catch (e) {\n"
			+ "        document.domain = document.domain.replace(/.*?\\./, '');\n"
			+ "    }\n"
			+ "}\n"
			+ "\n"
			+ "var isc = top.isc ? top.isc : window.opener ? window.opener.isc : null;\n"
			+ "if (isc) isc.RPCManager.delayCall(\"handleLoginSuccess\", [window]);\n"
			+ "</SCRIPT>";

	/**
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @param authentication
	 * @throws IOException
	 * @throws ServletException
	 */
	protected void handle(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			Authentication authentication) throws IOException, ServletException {
		PrintWriter writer = httpServletResponse.getWriter();
		writer.print(markerSnippet);
		httpServletResponse.flushBuffer();
	}

	/**
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @param e
	 * @throws IOException
	 * @throws ServletException
	 */
	protected void handle(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, AuthenticationException e)
			throws IOException, ServletException {
		PrintWriter writer = httpServletResponse.getWriter();
		writer.print(markerSnippet);
		httpServletResponse.flushBuffer();
	}

	/**
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @throws IOException
	 */
	protected void handle(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws IOException {
		PrintWriter writer = httpServletResponse.getWriter();
		writer.print(markerSnippet);
		httpServletResponse.flushBuffer();
	}

	/**
	 * @return
	 */
	public String getMarkerSnippet() {
		return markerSnippet;
	}

	/**
	 * @param markerSnippet
	 */
	public void setMarkerSnippet(String markerSnippet) {
		this.markerSnippet = markerSnippet;
	}
}
