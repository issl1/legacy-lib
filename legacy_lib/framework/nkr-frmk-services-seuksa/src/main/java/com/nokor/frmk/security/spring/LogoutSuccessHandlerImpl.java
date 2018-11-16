package com.nokor.frmk.security.spring;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.util.Assert;

/**
 * @author ly.youhort
 *
 */
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler, InitializingBean {

    private SimpleUrlLogoutSuccessHandler defaultHandler = new SimpleUrlLogoutSuccessHandler();
    private String logoutSuccessUrl = "/";

    /**
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(logoutSuccessUrl, "logoutSuccessUrl must be specified");
        defaultHandler.setDefaultTargetUrl(logoutSuccessUrl);
    }

    /**
     * 
     * @return
     */
    public String getLogoutSuccessUrl() {
        return logoutSuccessUrl;
    }

    /**
     * 
     * @param logoutSuccessUrl
     */
    public void setLogoutSuccessUrl(final String logoutSuccessUrl) {
        this.logoutSuccessUrl = logoutSuccessUrl;
        defaultHandler.setDefaultTargetUrl(logoutSuccessUrl);
    }

    /**
     * @see org.springframework.security.web.authentication.logout.LogoutSuccessHandler#onLogoutSuccess(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.Authentication)
     */
    @Override
    public void onLogoutSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException, ServletException {
        defaultHandler.onLogoutSuccess(request, response, authentication);
    }
    
    /**
     * @param response
     * @param responsePayload
     * @throws IOException
     */
    protected void writeResponse(final HttpServletResponse response, final String responsePayload) throws IOException {
        if (!response.isCommitted()) {
            PrintWriter writer = response.getWriter();
            writer.print(responsePayload);
            response.flushBuffer();
        }
    }
}
