package com.nokor.frmk.web.struts.action;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.reflect.MethodUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.exception.ErrorCode;
import org.seuksa.frmk.tools.http.HttpUtils;
import org.seuksa.frmk.tools.security.CryptoHelper;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

import com.nokor.common.app.eref.ELanguage;
import com.nokor.common.app.eventlog.model.ESecEventMode;
import com.nokor.common.app.eventlog.model.SecEventAction;
import com.nokor.common.app.eventlog.model.SecEventLog;
import com.nokor.common.app.tools.helper.AppConfigFileHelper;
import com.nokor.common.app.tools.helper.AppServicesHelper;
import com.nokor.common.app.tools.helper.AppSettingConfigHelper;
import com.nokor.common.app.web.cookie.AppCookieKeys;
import com.nokor.common.app.web.session.AppUserSession;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.security.model.SecUser;
import com.opensymphony.xwork2.ActionContext;


/**
 * @author prasnar
 *
 */
public abstract class BasePageAction extends AbstractActionSupport implements AppServicesHelper {
    /** */
    private static final long serialVersionUID = 4408808685827821088L;
    
    protected static final String SESS_KEY_FILTER_NAME = "@filterName@";
    protected static final String SESS_KEY_ENTITY_IDS = "@entityIds@";

    private Map<String, String> parametersQueryString;
    private String queryString;
    
    private Boolean authenticationRequired;
    private Boolean authenticationSysAdminRequired;
    private Boolean authenticationSuperAdminRequired;
    private Boolean secUserAuthenticationRequired;

    private String action;
    private String secondAction;
    private String filterName;
    private String selectedId;
    private String firstSelectedId;
    private String secondSelectedId;
    private String thirdSelectedId;

    private Date today;

    private String param1;
    private String param2;
    private String param3;
    private String param4;

    private Integer defaultOrderIndex;
    private String defaultOrderType;

    /** First Sort and Pagination */
    private Integer firstOrderIndex;
    private String firstOrderType;
    private int firstCurrentPageIndex;

    /** Second Sort and Pagination */
    private Integer secondOrderIndex;
    private String secondOrderType;
    private int secondCurrentPageIndex;

    /** Third Sort and Pagination */
    private Integer thirdOrderIndex;
    private String thirdOrderType;
    private int thirdCurrentPageIndex;

    /** Message Title */
    private String firstMessageTitleLabel;
    private String firstMessageTitle;
    private String secondMessageTitleLabel;
    private String secondMessageTitle;
  	private String thirdMessageTitleLabel;
    private String thirdMessageTitle;

    private Boolean loginRequired = Boolean.FALSE;
    private String pageTitle;
    private String secondPageTitle;
    private String thirdPageTitle;
    
    private Integer tabLevel;
    
    private ActionMapping actionMapping;
    
    private AppUserSession userInfo;
    
    private ELanguage languageContent;
    
    /**
     *
     */
    public BasePageAction() {
    }

    @Override
    public void invokeMethod() throws Exception {
    	/* *********************************************************
    	 * Log to track the problem of session
    	 **********************************************************/
    	today = DateUtils.today();
    	logger.info("***************START**Log to track the problem of session**************");
    	logger.info("Service called by : IP [" + getRequest().getRemoteAddr() + "]");
        logger.info("Service called by : Host [" + getRequest().getRemoteHost() + "]");
        logger.info("Service called by : Port [" + getRequest().getRemotePort() + "]");
        logger.info("Service called by : User [" + getRequest().getRemoteUser() + "]");
//        UserInfo userInfo = SessionManager.getUserInfo(getRequest().getSession());
//        
//        
//        if (userInfo != null) {
//            logger.info("User info : SecUsrId [" + userInfo.getSecUser().getId() + "]");
//            logger.info("User info : Login [" + userInfo.getSecUser().getLogin() + "]");
//            logger.info("User info : Name [" + userInfo.getName() + "]");
//            
//        }
    	logger.info("***************END**Log to track the problem of session**************");

    	// Remove: performance cost
//    	saveManualAppEventForStatistic();
    	
//    	displayRequestParameters();
//    	displayRequestAttributes();
    	
    	
        init();
        if (AppConfigFileHelper.isApplicationBO()) {
        	checkAuthenticationBO();
        	
        	if (hasAccessRightBO()) {
        		doProcess();
        	}
        } else {
        	checkAuthentication();
        	doProcess();
        }
    }
    
//	public static void initCryptoWithMasterkey() {
//    	CryptoHelper.init(AppConfigUtil.getMasterKeyTmp().toCharArray());
//    }
    
    /**
	 * @return the parametersQueryString
	 */
	public Map<String, String> getParametersQueryString() {
		return parametersQueryString;
	}

	/**
	 * @param parametersQueryString the parametersQueryString to set
	 */
	public void setParametersQueryString(Map<String, String> parametersQueryString) {
		this.parametersQueryString = parametersQueryString;
	}

	/**
	 * @return the queryString
	 */
	public String getQueryString() {
		return queryString;
	}

	/**
	 * @param queryString the queryString to set
	 */
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	/**
     * 
     * @return
     */
    public void decodeQueryString() {
    	queryString = decode(getRequest().getQueryString());
    	if (queryString == null) {
    		queryString = getRequest().getQueryString();
    	}
    	initParameters();
    }
    		
    /**
     * 
     * @return
     */
    public String decode(String str) {
    	if (str == null) {
    		return null;
    	}
    	String res = null;
    	try {
    		res = CryptoHelper.decrypt(str);
    	} catch (Exception e) {
    		logger.error("Error while decode(" + (str != null ? str : "") + ")");
    	}
    	return res;
    }
    
    /**
     * 
     * @return
     */
    public String encode(String str) {
    	if (str == null) {
    		return null;
    	}
    	String res = null;
    	try {
    		res = CryptoHelper.encrypt(str);
    	} catch (Exception e) {
    		logger.error("Error while getEncodeQueryString(" + (str != null ? str : "") + ")");
    	}
    	return res;
    }
    
    /**
     * 
     * @param queryString
     * @return
     */
    public void initParameters() {
    	parametersQueryString = new HashMap<String, String>();
    	if (queryString != null) {
	    	String[] params = queryString.split("&");
	    	if (params != null) {
		    	for (String param : params) {
		    		String[] parVal = param.split("=");
		    		if (parVal != null && parVal.length == 2) {
		    			parametersQueryString.put(parVal[0], parVal[1]);
		    		}
		    	}
	    	}
    	}
    	logger.debug("initParameters: parametersQueryString: " + parametersQueryString.size());
    }
    
    /**
     * 
     * @param key
     * @return
     */
    public String getParamValue(String key) {
    	if (parametersQueryString == null) {
    		initParameters();
    	}
    	return parametersQueryString.get(key);
    }
    
    /**
     * 
     * @param key
     * @return
     */
    public Long getParamValueAsLong(String key) {
    	if (parametersQueryString == null) {
    		initParameters();
    	}
    	try {
    		return Long.valueOf(parametersQueryString.get(key));
    	} catch (Exception e) {
    		logger.error("Error - getParamValueAsLong - key [" + (key != null ? key : null) + "]");
    		return null;
    	}
    }
    

    
    /**
     * 
     * @return
     */
    public String getUrl() {
    	try {
    		return (StringUtils.isNotEmpty(getUrlReferer()) ? getUrlReferer() : getRequest().getContextPath());
    	} catch (Exception e) {
    		logger.error("Error while getting the URL", e);
    		return null;
    	}
    }
    
  
    
    /**
     * 
     * @param request
     * @return
     */
    public String getFullUrlPath(HttpServletRequest request) {
    	try {
        	String url = request.getScheme() + "://" + request.getServerName();
        	
        	// in case default port, do not need to display the port
        	if (!"http".equals(request.getScheme()) && request.getServerPort() != 80
        		&& !"https".equals(request.getScheme()) && request.getServerPort() != 443) {
        		url += ":" + request.getServerPort(); 
        	}
        	url += request.getRequestURI();
        	
             if (StringUtils.isNotEmpty(request.getQueryString())) {
            	 url += request.getQueryString();
             }
             return url;
        } catch (Exception e) {
    		logger.error("Error while getting the full URL", e);
    		return null;
    	}
    }
    

    /**
     * 
     */
    private void displayRequestAttributes() {
    	int iAtt = 0;
    	Enumeration attrs =  getRequest().getAttributeNames();
    	while(attrs.hasMoreElements()) {
    		String paramName = (String)attrs.nextElement();
            logger.debug("==[" + iAtt + "]===" + paramName + "===");
            Object paramValue = getRequest().getAttribute(paramName);
            logger.debug("  " + paramValue.getClass().getSimpleName() + ": " + (paramValue != null? paramValue.toString() : "NULL"));
    		iAtt++;
    	}
    }
    
    /**
     * 
     */
    private void displayRequestParameters() {
    	int iPar = 0;
    	Enumeration paramNames = getRequest().getParameterNames();
    	while(paramNames.hasMoreElements()) {
            String paramName = (String)paramNames.nextElement();
            logger.debug("==[" + iPar + "]===" + paramName + "===");
            String[] paramValues = getRequest().getParameterValues(paramName);
            if (paramValues.length == 1) {
                String paramValue = paramValues[0];
                logger.debug("  " + (paramValue.length() == 0 ?  "No Value" : paramValue));
            } else {
                for(int i = 0; i < paramValues.length; i++) {
                    logger.debug("  " + paramValues[i] + ", ");
                }
            }
        }
    }
    
    /**
     * 
     * @throws Exception
     */
    private void doProcess() throws Exception {
    	beforeProcessPage();
        processPage();
        processAction();
        afterProcessPage();
    }

    /**
     *
     */
    private void init() {
    }

    /**
     * 
     * @param action
     * @param msg
     * @param urlForView
     */
    protected void saveManualAppEvent(SecEventAction action, String msg, String urlForView) {
    	saveAppEvent(action, ESecEventMode.USER, msg, urlForView);
    }
    
    /**
     * 
     * @param action
     * @param msg
     * @param urlForView
     */
    protected void saveTriggeredAppEvent(SecEventAction action, String msg, String urlForView) {
    	saveAppEvent(action, ESecEventMode.TRIGGERED, msg, urlForView);
    }
    
    /**
     * 
     * @param action
     * @param msg
     * @param urlForView
     */
    protected void saveSystemAppEvent(SecEventAction action, String msg, String urlForView) {
    	saveAppEvent(action, ESecEventMode.SYSTEM, msg, urlForView);
    }
    
    /**
     * 
     * @param action
     * @param mode
     * @param msg
     * @param urlForView
     */
    protected void saveAppEvent(SecEventAction action, ESecEventMode mode, String msg, String urlForView) {
    	if (StringUtils.isEmpty(msg)) {
    		logger.error("The message is empty.");
    		return;
    	}
        try {
        	if (action.isLogin()) {
        		SecUser user = getSecUser();
        		user.setLastLoginDate(DateUtils.today());
        		user.setLastLoginIp(HttpUtils.getIpAddress(getRequest()));
        		user.setConnected(true);
        		ENTITY_SRV.update(user);
        	} else if (action.isLogout()) {
        		SecUser user = getSecUser();
        		user.setLastLogoutDate(DateUtils.today());
        		user.setLastLoginIp(HttpUtils.getIpAddress(getRequest()));
        		user.setConnected(false);
        		ENTITY_SRV.update(user);
        	}
        	
        	
        	AppUserSession userInfo = APP_SESSION_MNG.getCurrent();
            SecEventLog eveLog = SecEventLog.createInstance(getRequest().getSession());

            if (userInfo != null && userInfo.getSecUser() != null) {
	        	eveLog.setSecUser(userInfo.getSecUser());
	        }
			eveLog.setSecApplication(SecApplicationContextHolder.getContext().getSecApplication());
	    	eveLog.setEvenDate(DateUtils.today());
	    	eveLog.setText(msg);
	    	eveLog.setAction(action);
	        eveLog.setMode(mode);
	    	eveLog.setUrlForView(urlForView);
	        eveLog.setUrl(getRequest().getRequestURI() + " - " + (getActionMapping() != null ? getActionMapping().getName() : ""));
	        eveLog.setRemoteIPAddress(HttpUtils.getIpAddress(getRequest()));
	        eveLog.setRemoteHost(getRequest().getRemoteHost());
	        eveLog.setRemotePort(String.valueOf(getRequest().getRemotePort()));
	        eveLog.setRemoteUser(getRequest().getRemoteUser());
        
        	ENTITY_SRV.create(eveLog);
        } catch (Exception e) {
    		logger.error("Error in saveAppEvent ["+ msg + "]", e);
    	}
    }
    
    

	/**
     *
     */
    protected void checkAuthentication() {
    	if (Boolean.TRUE.equals(secUserAuthenticationRequired) && !isAuthenticated()) {
			String errMsg = I18N.message("error.page.authentication.required");
			throw new AuthenticationCredentialsNotFoundException(errMsg);
		} 
    	if (!AppConfigFileHelper.isApplicationBO()) {
			if (Boolean.TRUE.equals(authenticationRequired) && !isAuthenticated()) {
				String errMsg = I18N.message("error.page.authentication.required");
				throw new AuthenticationCredentialsNotFoundException(errMsg);
			}
    	}
    	if (Boolean.TRUE.equals(authenticationSysAdminRequired) && !isAdmin()) {
        	String errMsg = I18N.message("error.page.authentication.sys.admin.required");
			throw new AuthenticationCredentialsNotFoundException(errMsg);
        }
    	if (Boolean.TRUE.equals(authenticationSuperAdminRequired) && !isSysAdmin()) {
        	String errMsg = I18N.message("error.page.authentication.super.admin.required");
			throw new AuthenticationCredentialsNotFoundException(errMsg);
        }
    	
    }

    /**
     *
     */
    private void checkAuthenticationBO() {
    	if (AppConfigFileHelper.isApplicationBO()) {
//    		if (Boolean.TRUE.equals(authenticationRequired) && !isAuthenticated()) {
//				String errMsg = I18N.message("error.page.authentication.access.not.granted.bo");
//				throw new AuthenticationCredentialsNotFoundException(errMsg);
//			}
//    		if (Boolean.TRUE.equals(authenticationSysAdminRequired) && !isSysAdmin()) {
//            	String errMsg = I18N.message("error.page.authentication.sys.admin.required");
//    			throw new AuthenticationCredentialsNotFoundException(errMsg);
//            }
//        	if (Boolean.TRUE.equals(authenticationSuperAdminRequired) && !isSysSuperAdminAuthenticated()) {
//            	String errMsg = I18N.message("error.page.authentication.super.admin.required");
//    			throw new AuthenticationCredentialsNotFoundException(errMsg);
//            }
//    		if (getSecUser() != null && isAuthenticated()) {
//    			if (isPermanentMember() && isGraUrl()) {
//        			this.setUrlRedirect("dashboardContent.do");
//        		}
//    		}
    	}
    }
    
    /**
     *
     * @return
     */
    private boolean hasAccessRightBO() {
    	if (getSecUser() != null && isAuthenticated() ) {
//	    	boolean hasNoAccessRight = isHasNoRightControlMenu();
//	    	if (hasNoAccessRight) {
//	    		getActionResult().setResultNoAccessRight();
//	    		logger.exit("execute - result: " + getActionResult().getResult());
//	            getRequest().setAttribute(ErrorHandler.ERROR_HANDLER_KEY, getActionResult().getErrorMessage());
//	            getRequest().getSession().setAttribute(ACTION_RESULT, getActionResult());
//	    		return false;
//	    	}
    	}
    	return true;
    }


    /**
     * Using to check BO users in case they are in wrong profile
     */
    private void forceLogout() {
    	String login ="None";
        if (getSecUser() != null) {
        	login = getSecUser().getLogin();
        }
        APP_SESSION_MNG.logoutCurrent();
    	
        logger.info("The user [" + login + "] has logged out sucessfully at: " +  DateUtils.todayH00M00S00());

        //SessionManager.clearLoginInfo(getRequest().getSession());
    }

    

    /**
     *
     * @throws Exception
     */
    protected abstract void processPage() throws Exception;

    /**
     *
     * @throws Exception
     */
    protected void processAction() throws Exception {
        if (StringUtils.isEmpty(action)) {
            return;
        }

       

        try {
            final Object result = MethodUtils.invokeExactMethod(this, action, null);
            logger.info("The returned result is: class [" + (result != null ? result.getClass().getCanonicalName() + "], value [" + result.toString() : "<null>") + "]");
        }
        catch (final NoSuchMethodException e) {
            final String errMsg = "Error in struts configuration file: Either the parameter action [" + action + "] is not a method of this class [" + this.getClass().getCanonicalName() + "], or this method is not accessible.";
            logger.error(errMsg, e);
            getActionResult().getErrorHandler().setCode(ErrorCode.ERR_INCORRECT_PARAMETERS);
            setActionResultSysError(errMsg);
            getActionResult().getErrorHandler().setTechnicalMessage(errMsg + ActionResult.BREAKLINE + e.getStackTrace());
            throw e;
        }
        catch (final InvocationTargetException e) {
            final String errMsg = "Error in struts configuration file: Either the parameter action [" + action + "] is not a method of this class [" + this.getClass().getCanonicalName() + "], or this method is not accessible.";
            logger.error(errMsg, e);
            getActionResult().getErrorHandler().setCode(ErrorCode.ERR_INCORRECT_PARAMETERS);
            setActionResultSysError(errMsg);
            getActionResult().getErrorHandler().setTechnicalMessage(errMsg + ActionResult.BREAKLINE + e.getStackTrace());
            throw e;
        }
    }

    /**
     * @throws Exception 
     * 
     */
    public abstract void viewAction() throws Exception ;
	

    /**
     *
     */
    protected void beforeProcessPage() {
    }

    /**
     *
     */
    protected void afterProcessPage() {
    }

    /**
     *
     */
    protected void setLastBreadcrumb() {

    }

    /**
	 * @return the param1
	 */
	public String getParam1() {
		return param1;
	}

	/**
	 * @param param1 the param1 to set
	 */
	public void setParam1(String param1) {
		this.param1 = param1;
	}

	/**
	 * @return the param2
	 */
	public String getParam2() {
		return param2;
	}

	/**
	 * @param param2 the param2 to set
	 */
	public void setParam2(String param2) {
		this.param2 = param2;
	}

	/**
	 * @return the param3
	 */
	public String getParam3() {
		return param3;
	}

	/**
	 * @param param3 the param3 to set
	 */
	public void setParam3(String param3) {
		this.param3 = param3;
	}

	/**
	 * @return the param4
	 */
	public String getParam4() {
		return param4;
	}

	/**
	 * @param param4 the param4 to set
	 */
	public void setParam4(String param4) {
		this.param4 = param4;
	}

	/**
     * @return the action
     */
    public String getAction() {
        return action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(final String action) {
        this.action = action;
    }


    /**
	 * @return the actionMapping
	 */
	public ActionMapping getActionMapping() {
		if (actionMapping == null) {
			try {
	    		actionMapping = (ActionMapping) getRequest().getAttribute("struts.actionMapping");
	    	} catch (Exception e) {
	    		logger.error("Error while getting ActionMapping", e);
	    	}
		}
		return actionMapping;
	}

	/**
	 * @param actionMapping the actionMapping to set
	 */
	public void setActionMapping(ActionMapping actionMapping) {
		this.actionMapping = actionMapping;
	}


    public String getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(final String selectedId) {
        this.selectedId = selectedId;
    }

    /**
     * @return the selectedId
     */
    public Long getSelectedIdAsLong() {
        try {
            return Long.valueOf(getSelectedId());
        }
        catch (final Exception e) {
            logger.debug("No selected id.");
        }

        return null;
    }

    /**
     * @return the firstSelectedId
     */
    public Long getFirstSelectedIdAsLong() {
        try {
            return Long.valueOf(getFirstSelectedId());
        }
        catch (final Exception e) {
            logger.debug("No selected id.");
        }

        return null;
    }

    /**
     * @return the firstSelectedId
     */
    public String getFirstSelectedId() {
        return firstSelectedId;
    }

    /**
     * @param firstSelectedId the firstSelectedId to set
     */
    public void setFirstSelectedId(final String firstSelectedId) {
        this.firstSelectedId = firstSelectedId;
    }

    public String getSecondSelectedId() {
        return secondSelectedId;
    }

    public Long getSecondSelectedIdAsLong() {
        try {
            return Long.valueOf(getSecondSelectedId());
        }
        catch (final Exception e) {
            logger.debug("No selected id.");
        }

        return null;
    }

    public void setSecondSelectedId(final String secondSelectedId) {
        this.secondSelectedId = secondSelectedId;
    }

    public String getFilterName() {
        if (filterName == null) {
            filterName = (String) ActionContext.getContext().getSession().get(SESS_KEY_FILTER_NAME);
        }
        return filterName;
    }

    public void setFilterName(final String filterName) {
        this.filterName = filterName;
        ActionContext.getContext().getSession().put(SESS_KEY_FILTER_NAME, filterName);
    }

    /**
	 * @return the loginRequired
	 */
	public Boolean getLoginRequired() {
		return loginRequired;
	}

	/**
	 * @param loginRequired the loginRequired to set
	 */
	public void setLoginRequired(Boolean loginRequired) {
		this.loginRequired = loginRequired;
	}

	public String getThirdSelectedId() {
        return thirdSelectedId;
    }

    public Long getThirdSelectedIdAsLong() {
        try {
            return Long.valueOf(getThirdSelectedId());
        }
        catch (final Exception e) {
            logger.debug("No selected id.");
        }

        return null;
    }

    public void setThirdSelectedId(final String thirdSelectedId) {
        this.thirdSelectedId = thirdSelectedId;
    }

  

    public Integer getFirstOrderIndex() {
        return firstOrderIndex;
    }

    public void setFirstOrderIndex(final Integer orderIndex) {
        this.firstOrderIndex = orderIndex;
    }

    public String getFirstOrderType() {
        return firstOrderType;
    }

    public void setFirstOrderType(final String orderType) {
        this.firstOrderType = orderType;
    }

    public int getFirstCurrentPageIndex() {
        return firstCurrentPageIndex;
    }

    public void setFirstCurrentPageIndex(final int currentPageIndex) {
        this.firstCurrentPageIndex = currentPageIndex;
    }

    public Integer getSecondOrderIndex() {
        return secondOrderIndex;
    }

    public void setSecondOrderIndex(final Integer secondOrderIndex) {
        this.secondOrderIndex = secondOrderIndex;
    }

    public String getSecondOrderType() {
        return secondOrderType;
    }

    public void setSecondOrderType(final String secondOrderType) {
        this.secondOrderType = secondOrderType;
    }

    public int getSecondCurrentPageIndex() {
        return secondCurrentPageIndex;
    }

    public void setSecondCurrentPageIndex(final int secondCurrentPageIndex) {
        this.secondCurrentPageIndex = secondCurrentPageIndex;
    }

    public Integer getThirdOrderIndex() {
        return thirdOrderIndex;
    }

    public void setThirdOrderIndex(final Integer thirdOrderIndex) {
        this.thirdOrderIndex = thirdOrderIndex;
    }

    public String getThirdOrderType() {
        return thirdOrderType;
    }

    public void setThirdOrderType(final String thirdOrderType) {
        this.thirdOrderType = thirdOrderType;
    }

    public int getThirdCurrentPageIndex() {
        return thirdCurrentPageIndex;
    }

    public void setThirdCurrentPageIndex(final int thirdCurrentPageIndex) {
        this.thirdCurrentPageIndex = thirdCurrentPageIndex;
    }

   

	public String getFirstMessageTitle() {
		return firstMessageTitle;
	}

	public void setFirstMessageTitle(String firstMessageTitle) {
		this.firstMessageTitle = firstMessageTitle;
	}

	public String getSecondMessageTitle() {
		return secondMessageTitle;
	}

	public void setSecondMessageTitle(String secondMessageTitle) {
		this.secondMessageTitle = secondMessageTitle;
	}

	public String getThirdMessageTitle() {
		return thirdMessageTitle;
	}

	public void setThirdMessageTitle(String thirdMessageTitle) {
		this.thirdMessageTitle = thirdMessageTitle;
	}

	public String getFirstMessageTitleLabel() {
		return firstMessageTitleLabel;
	}

	public void setFirstMessageTitleLabel(String firstMessageTitleLabel) {
		this.firstMessageTitleLabel = firstMessageTitleLabel;
	}

	public String getSecondMessageTitleLabel() {
		return secondMessageTitleLabel;
	}

	public void setSecondMessageTitleLabel(String secondMessageTitleLabel) {
		this.secondMessageTitleLabel = secondMessageTitleLabel;
	}

	public String getThirdMessageTitleLabel() {
		return thirdMessageTitleLabel;
	}

	public void setThirdMessageTitleLabel(String thirdMessageTitleLabel) {
		this.thirdMessageTitleLabel = thirdMessageTitleLabel;
	}

	/**
	 * @return the authenticationRequired
	 */
	public Boolean getAuthenticationRequired() {
		return authenticationRequired;
	}

	/**
	 * @param authenticationRequired
	 *            the authenticationRequired to set
	 */
	public void setAuthenticationRequired(Boolean authenticationRequired) {
		this.authenticationRequired = authenticationRequired;
	}

	/**
	 * @return the authenticationSuperAdminRequired
	 */
	public Boolean getAuthenticationSuperAdminRequired() {
		return authenticationSuperAdminRequired;
	}

	/**
	 * @param authenticationSuperAdminRequired the authenticationSuperAdminRequired to set
	 */
	public void setAuthenticationSuperAdminRequired(
			Boolean authenticationSuperAdminRequired) {
		this.authenticationSuperAdminRequired = authenticationSuperAdminRequired;
	}

	/**
	 * @return the authenticationSysAdminRequired
	 */
	public Boolean getAuthenticationSysAdminRequired() {
		return authenticationSysAdminRequired;
	}

	/**
	 * @param authenticationSysAdminRequired the authenticationSysAdminRequired to set
	 */
	public void setAuthenticationSysAdminRequired(
			Boolean authenticationSysAdminRequired) {
		this.authenticationSysAdminRequired = authenticationSysAdminRequired;
	}

	/**
	 * @return the secUserAuthenticationRequired
	 */
	public Boolean getSecUserAuthenticationRequired() {
		return secUserAuthenticationRequired;
	}

	/**
	 * @param secUserAuthenticationRequired the secUserAuthenticationRequired to set
	 */
	public void setSecUserAuthenticationRequired(
			Boolean secUserAuthenticationRequired) {
		this.secUserAuthenticationRequired = secUserAuthenticationRequired;
	}

	public Integer getDefaultOrderIndex() {
		return defaultOrderIndex;
	}

	public void setDefaultOrderIndex(Integer defaultOrderIndex) {
		this.defaultOrderIndex = defaultOrderIndex;
	}

	public String getDefaultOrderType() {
		return defaultOrderType;
	}

	public void setDefaultOrderType(String defaultOrderType) {
		this.defaultOrderType = defaultOrderType;
	}





	/* ***************************************************************************************
	 * START - SECURITY / ACCESS RIGHTS
	 * ***************************************************************************************/
	// -----------------------------------------------------------
	// Profiles - System super Admin
	// -----------------------------------------------------------
	/**
	 * @return the accessInfo
	 */
	public AppUserSession getUserInfo() {
		return APP_SESSION_MNG.getCurrent();
	}

	/**
	 * 
	 * @return
	 */
	public boolean isAdmin() {
		return getUserInfo().getIsAdmin();
	}

	/**
	 * @return the secondAction
	 */
	public String getSecondAction() {
		return secondAction;
	}

	/**
	 * @param secondAction the secondAction to set
	 */
	public void setSecondAction(String secondAction) {
		this.secondAction = secondAction;
	}

	
	/**
	 * @return the pageTitle
	 */
	public String getPageTitle() {
		return pageTitle;
	}

	/**
	 * @param pageTitle the pageTitle to set
	 */
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	/**
	 * @return the secondPageTitle
	 */
	public String getSecondPageTitle() {
		return secondPageTitle;
	}

	/**
	 * @param secondPageTitle the secondPageTitle to set
	 */
	public void setSecondPageTitle(String secondPageTitle) {
		this.secondPageTitle = secondPageTitle;
	}

	/**
	 * @return the thirdPageTitle
	 */
	public String getThirdPageTitle() {
		return thirdPageTitle;
	}

	/**
	 * @param thirdPageTitle the thirdPageTitle to set
	 */
	public void setThirdPageTitle(String thirdPageTitle) {
		this.thirdPageTitle = thirdPageTitle;
	}

	

	/* ***************************************************************************************
	 * END - SECURITY / ACCESS RIGHTS
	 * ***************************************************************************************/


	public String getHostPath(){
		return  "http://" + getRequest().getHeader("Host") + getRequest().getContextPath();
	}


	/**
	 * @return the today
	 */
	public Date getToday() {
		return today;
	}



	public String getYear() {
		return DateUtils.getYearStr(today);
	}

	public String getMonth() {
		return DateUtils.getMonthStr(today);
	}

	public String getDay() {
		return DateUtils.getDay(today) + "";
	}

	public String getStrToday() {
		return DateUtils.date2StringDDMMYYYY_SLASH(today);
	}

	




	/**
	 * @return the languageInterface
	 */
	public ELanguage getLanguageInterface() {
		ELanguage languageInterface = APP_SESSION_MNG.getCurrent().getLanguageInterface();
		if (languageInterface == null) {
			Cookie cooLang = getCookie(AppCookieKeys.COOKIE_LANGUAGE);
			if (cooLang != null) {
				languageInterface = ELanguage.getFromString(cooLang.getValue(), ELanguage.getFromLocale(AppSettingConfigHelper.getLocale()));
			} 
			if (languageInterface == null) {
				languageInterface = ELanguage.getFromLocale(SecApplicationContextHolder.getContext().getLocale());
			}
			APP_SESSION_MNG.getCurrent().setLanguageInterface(languageInterface);
		}
		return languageInterface;
	}

	/**
	 * @param languageInterface the languageInterface to set
	 */
	public void setLanguageInterface(ELanguage languageInterface) {
		addCookie(AppCookieKeys.COOKIE_LANGUAGE, languageInterface.getLocaleLanguage());
		APP_SESSION_MNG.getCurrent().setLanguageInterface(languageInterface);
	}

	/**
	 * @return the languageContent
	 */
	public ELanguage getLanguageContent() {
		if (languageContent == null) {
			languageContent = AppConfigFileHelper.getContentLanguageDefault();
			if (languageContent == null) {
				languageContent = getLanguageInterface();
			}
		}
		return languageContent;
	}

	/**
	 * @param languageContent the languageContent to set
	 */
	public void setLanguageContent(ELanguage languageContent) {
		this.languageContent = languageContent;
	}

	/**
	 * @return the tabLevel
	 */
	public Integer getTabLevel() {
		return tabLevel;
	}

	/**
	 * @param tabLevel the tabLevel to set
	 */
	public void setTabLevel(Integer tabLevel) {
		this.tabLevel = tabLevel;
	}
	
	@Override
	public String getValue(String code) {
		return getText(code);
	}
	
	@Override
	public String getText(String code) {		
		Map<String, String> I18N_MAP = I18N.getResources().get(getLanguageInterface().getLocale());
		return I18N_MAP.get(code);
	}
	
}
