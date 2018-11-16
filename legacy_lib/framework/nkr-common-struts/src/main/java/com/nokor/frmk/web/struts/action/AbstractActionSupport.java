package com.nokor.frmk.web.struts.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.service.BaseEntityService;
import org.seuksa.frmk.tools.exception.ErrorCode;
import org.seuksa.frmk.tools.exception.ErrorHandler;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.common.app.tools.helper.AppConfigFileHelper;
import com.nokor.common.app.tools.helper.AppServicesHelper;
import com.nokor.common.app.web.ISearchCriteriaVO;
import com.nokor.frmk.security.model.SecUser;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @author prasnar
 *
 */
public abstract class AbstractActionSupport extends ActionSupport implements AppServicesHelper, ServletRequestAware, RequestAware, SessionAware, ServletResponseAware {

    private static final long serialVersionUID = 2279430216282145422L;

    protected static final Logger logger = LoggerFactory.getLogger(AbstractActionSupport.class);

    public static final Long DEFAULT_SEC_APPLICATION_ID = Long.valueOf(1);
    public static final String CURRENT_APPLICATION = "currentApplication";
    public static final String ACTION_RESULT = "actionResult";

    private HttpServletRequest request;
    private HttpServletResponse response;
    private Map<String, Object> sessionMap = new HashMap<String, Object>();
    private Map<String, Object> requestMap = new HashMap<String, Object>();
    private ActionResult actionResult;
    private String urlRedirect;
    
    private String urlReferer;
    
    @Override
    public String execute() throws Exception {
        logger.info("execute - start");

        try {
            logger.info("Service called by : IP [" + request.getRemoteAddr() + "]");
            logger.info("Service called by : Host [" + request.getRemoteHost() + "]");
            logger.info("Service called by : Port [" + request.getRemotePort() + "]");
            logger.info("Service called by : User [" + request.getRemoteUser() + "]");
            this.urlReferer = request.getHeader("referer");

            invokeMethod();

        } catch (AuthenticationCredentialsNotFoundException e) {
            String errMsg = I18N.message("error.not.authorized.page", getRequest().getRequestURI());
            logger.error(errMsg);
            setResultNotAuthorized();
            getActionResult().getErrorHandler().setCode(ErrorCode.ERR_SYS);
            getActionResult().getErrorHandler().setTechnicalMessage(errMsg);
        } catch (Exception e) {
            // Any exceptions thrown by invoking the server
            // method should be sent back to the client. 
            // Make sure we are working with a 'pure' output stream
            // that does not contain any other data	
            String techErrMsg = "An expected error has occurred.";
            logger.error(techErrMsg, e);
            String errMsg = "The action has failed. A technical error has occured.";
            getActionResult().getErrorHandler().setCode(ErrorCode.ERR_SYS);
            setActionResultSysError(errMsg);
            getActionResult().getErrorHandler().setTechnicalMessage(techErrMsg + ActionResult.BREAKLINE + e.getStackTrace());
            
        } finally {
            logger.info("execute - result: " + getActionResult().getResult());
            getRequest().setAttribute(ErrorHandler.ERROR_HANDLER_KEY, getActionResult().getErrorHandler());
            getRequest().getSession().setAttribute(ACTION_RESULT, getActionResult());
        }

        return getActionResult().getResult();
    }
    

    /**
     * @return the actionResult
     */
    public ActionResult getActionResult() {
        if (actionResult == null) {
            actionResult = new ActionResult();
        }
        return actionResult;
    }
    
    /**
     * 
     * @param urlRedirect
     */
    public void setActionResultRedirect(String urlRedirect) {
    	getActionResult().setResultRedirect(urlRedirect);
    	this.urlRedirect = urlRedirect;
    	
    }
    
    /**
     * 
     */
    public void setResultNotAuthorized() {
    	getActionResult().setResultNotAuthorized();
    }
    
    /**
     * 
     */
    public void setActionResultSysError() {
    	getActionResult().setResultSysError();
    }
    
    public void setActionResultSysError(String sysMsg) {
    	getActionResult().setErrorSysMessage(sysMsg);
    }

    public void setActionResultError(String errMsg) {
    	getActionResult().setErrorMessage(errMsg);
    }

    /**
     * 
     * @return
     */
    protected static ApplicationContext getAppContext() {
        return SpringUtils.getAppContext();
    }

    /**
     * 
     * @param serviceClass
     * @return
     */
    protected static <T extends BaseEntityService> T getService(final Class<T> serviceClass) {
        return getAppContext().getBean(serviceClass);
    }

    /**
     * @see org.apache.struts2.interceptor.SessionAware#setSession(java.util.Map)
     */
    @Override
    public void setSession(final Map<String, Object> value) {
        sessionMap = value;
    }

    /**
     * 
     * @return
     */
    public Object getSessionAttribute(final String key) {
        return sessionMap.get(key);
    }

    /**
     * 
     * @param key
     * @param value
     */
    public void setSessionAttribute(final String key, final Object value) {
        this.sessionMap.put(key, value);
    }

    /**
     * @see org.apache.struts2.interceptor.RequestAware#setRequest(java.util.Map)
     */
    @Override
    public void setRequest(final Map<String, Object> map) {
        this.requestMap = map;
    }

    /**
     * 
     * @param key
     * @return
     */
    public Object getRequestAttribute(final String key) {
        return this.requestMap.get(key);
    }

    /**
     * 
     * @param key
     * @param value
     */
    public void setRequestAttribute(final String key, final Object value) {
        this.requestMap.put(key, value);
    }

    /**
     * @see org.apache.struts2.interceptor.ServletRequestAware#setServletRequest(javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void setServletRequest(final HttpServletRequest request) {
        this.request = request;
    }
    
    /**
     * 
     * @param classCriteria
     * @return
     */
    public <T> ISearchCriteriaVO getSearchCriteriaVOFromSession(Class<T> classCriteria) {
    	return APP_SESSION_MNG.getCurrent().getSearchCriteria(classCriteria);
    }
    
    /**
     * 
     * @param criteriaVO
     */
    public void setSearchCriteriaVOFromSession(ISearchCriteriaVO criteriaVO) {
    	APP_SESSION_MNG.getCurrent().setSearchCriteria(criteriaVO);;
    }

    /**
     * 
     * @return
     */
    public HttpServletRequest getRequest() {
        return request;
    }
    
    /**
     * 
     * @return
     */
    public HttpSession getSession() {
        return request.getSession();
    }

    /**
     * @see org.apache.struts2.interceptor.ServletResponseAware#setServletResponse(javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void setServletResponse(final HttpServletResponse response) {
        this.response = response;
    }

    
    /**
	 * @return the response
	 */
	public HttpServletResponse getResponse() {
		return response;
	}

	/**
     * 
     * @return
     */
    public String getUrlReferer() {
        return urlReferer;
    }

    /**
     * 
     * @param urlReferer
     */
    public void setUrlReferer(final String urlReferer) {
        this.urlReferer = urlReferer;
    }

    /**
     * 
     * @return
     * @throws Exception
     */
    public abstract void invokeMethod() throws Exception;

    /**
     * 
     * @return
     */
	public SecUser getSecUser() {
		return UserSessionManager.getCurrentUser();
	}
	
	public boolean isInAdminProfile() {
		return isAuthenticated() && getSecUser().isInAdminProfile();
	}
	
	/**
	 * 
	 * @return
	 */
	public Boolean isAuthenticated() {
		return SecurityContextHolder.getContext().getAuthentication() != null
				&& SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null
				&& SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof SecUser;
				//SecUserContextHolder.getContext().isAuthenticated();
	}
	
	
	
	/**
	 * 
	 * @return
	 */
	public boolean isAdmin() {
		SecUser user = getSecUser();
		return user != null && user.isAdmin();
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isSysAdmin() {
		return UserSessionManager.getCurrentUser().isSysAdmin();
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isApplicationBO() {
		return AppConfigFileHelper.isApplicationBO();
	}

	/**
	 * 
	 * @return
	 */
	public boolean isApplicationFO() {
		return AppConfigFileHelper.isApplicationFO();
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isApplicationRA() {
		return AppConfigFileHelper.isApplicationRA();
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isApplicationCMS() {
		return AppConfigFileHelper.isApplicationCMS();
	}


	/**
	 * @return the urlRedirect
	 */
	public String getUrlRedirect() {
		return urlRedirect;
	}

	/**
	 * @param urlRedirect the urlRedirect to set
	 */
	public void setUrlRedirect(String urlRedirect) {
		this.urlRedirect = urlRedirect;
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public String getValue(String code) {
		return I18N.value(code);
	}
	
	@Override
	public String getText(String code) {
		return getValue(code);
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Cookie getCookie(String key) {
		for (Cookie c : request.getCookies()) {
			if (c.getName().equals(key)) {
				return c;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void addCookie(String key, String value) {
		Cookie cookie = new Cookie(key, value);
		cookie.setMaxAge(60 * 60 * 24 * 365); // Make the cookie last a year
		getResponse().addCookie(cookie);	
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTokenForPwdAutoGeneration() {
		return AppConfigFileHelper.getTokenForPwdAutoGeneration();
	}

	/**
	 * 
	 * @return
	 */
	public boolean isForDevelopmentOnlyMode() {
		return AppConfigFileHelper.isForDevelopmentOnlyMode();
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isTestingMode() {
		return AppConfigFileHelper.isTestingMode();
	}
}
