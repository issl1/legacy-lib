package com.nokor.ersys.core.web.action.login;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;

import com.nokor.common.app.tools.helper.AppConfigFileHelper;
import com.nokor.common.app.tools.helper.AppReferenceDataHelper;
import com.nokor.common.app.web.filter.LogSplitServletFilter;
import com.nokor.frmk.security.SecurityHelper;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.web.struts.action.BasePageAction;

/**
 * 
 * @author prasnar
 *
 */
public class LoginAction extends BasePageAction {
	/** */
	private static final long serialVersionUID = -3715856324045084962L;
	private LoginForm loginForm;
	private String redirectAction; 
	
	private static final String REFERER_HEADER = "Referer";
	private static final String LOGIN_ACTION = "login";
	
	/**
	 * 
	 */
	public LoginAction() {
		logger.info("LoginAction");
	}
	

	@Override
	protected void processPage() throws Exception {
	}
	


	@Override
	public void viewAction() throws Exception {
		displayLoginAction();
	}

	/**
	 * 
	 */
	public void displayLoginAction() {
		logger.debug("Loading the login form");
		loginForm = new LoginForm();
	}


	
	/**
	 * 
	 */
	public void loginExecuteAction() {
		processLogin(null);
	}

	/**
	 * 
	 */
	protected void processLogin(SecUser secUser) {
		
        Boolean isAllowLogin = Boolean.FALSE;
        String noAllowLoginMsg = null;

        try {
        	if (secUser == null) {
        		if (!loginForm.validateForm(getActionResult())) {
        			setLoginRequired(Boolean.TRUE);
        			return;
        		}
        		secUser = AUTHENTICAT_SRV.authenticate(loginForm.getUsername(), loginForm.getPassword());
        	}
	        
        	isAllowLogin = true;
        	LogSplitServletFilter.initLogUser();
	        
        } catch (BadCredentialsException e) {
            logger.error("BadCredentialsException for the user [" + loginForm.getUsername() + "]  at: " +  DateUtils.todayH00M00S00());
        } catch (Exception e) {
            logger.error("Exception for the user [" + loginForm.getUsername() + "]  at: " +  DateUtils.todayH00M00S00());
        }
        
        if (secUser == null || !isAllowLogin) {
            logger.error("Unsuccessful tentative of connection for the user [" + loginForm.getUsername() + "]  at: " +  DateUtils.todayH00M00S00());
        	if (StringUtils.isNotEmpty(noAllowLoginMsg)) {
            	getActionResult().setErrorMessage(noAllowLoginMsg);
        	} else {
            	String errMsg = I18N.message("error.login.connection.failed", loginForm.getUsername());
            	getRequest().getSession().setAttribute(SecurityHelper.SESS_KEY_AUTHENTICATION_ERROR, errMsg);
        		getActionResult().setErrorMessage(errMsg);
        	}
        	setLoginRequired(Boolean.TRUE);
        	SecurityContextHolder.clearContext();
        	
        	return;
        }
        
        logger.info("The user [" + secUser.getLogin() + "] has logged in sucessfully at: " +  DateUtils.todayH00M00S00());
        
        saveManualAppEvent(AppReferenceDataHelper.getSecEventActionLOGIN(), I18N.message("app.activity.msg.login", secUser.getLogin()), null);
 
    	redirectAfterLogin();
	}

	/**
	 * 
	 */
	public void loginWithAccessAction() {
		processLogin(getSecUser());
	}
	
	/**
	 * 
	 */
	public void loginCPanelAction() {
		// Must be first to be the system admin
		if (!isAdmin()) {
        	String errMsg = I18N.message("message.error.page.authentication.admin.required");
    		getActionResult().setErrorMessage(errMsg);
            logger.error(errMsg);
        	return;
        }
		
		// authenticate
		SecUser sysAdmUsr = SYS_AUTH_SRV.authenticate(getParam1(), getParam2());
		if (sysAdmUsr != null) {
        	String errMsg = I18N.message("error.login.connection.failed", "sysAdmin");
    		getActionResult().setErrorMessage(errMsg);
            logger.error(errMsg);
        	return;
		}
		
		redirectAfterLogin();
	}

	/**
	 * Default behavior: go to the last URL (the current page)
	 */
	protected void redirectAfterLogin() {
		if (StringUtils.isNotEmpty(redirectAction)) {
			String convertRedirectAction = redirectAction.replace(";", "&");
			redirectToActionAfterLogin(convertRedirectAction);
		} else {
	        String fromURL = getRequest().getHeader(REFERER_HEADER);
	        if (fromURL.contains(LOGIN_ACTION)) {
	        	redirectToActionAfterLogin(AppConfigFileHelper.getActionAfterRedirect());
	        } else {
	        	setActionResultRedirect(fromURL);
	        }
		}
	}
	
	/**
	 * 
	 */
	public void redirectToActionAfterLogin(String urlAction) {
		String fromURL = urlAction;
		setActionResultRedirect(fromURL);
	}
	

	/**
	 * @return the entityForm
	 */
	public LoginForm getEntityForm() {
		return loginForm;
	}

	/**
	 * @param entityForm the entityForm to set
	 */
	public void setEntityForm(LoginForm entityForm) {
		this.loginForm = entityForm;
	}

	
	/**
	 * @return the redirectAction
	 */
	public String getRedirectAction() {
		return redirectAction;
	}


	/**
	 * @param redirectAction the redirectAction to set
	 */
	public void setRedirectAction(String redirectAction) {
		this.redirectAction = redirectAction;
	}


	/**
	 * @return the loginForm
	 */
	public LoginForm getLoginForm() {
		return loginForm;
	}

	/**
	 * @param loginForm the loginForm to set
	 */
	public void setLoginForm(LoginForm loginForm) {
		this.loginForm = loginForm;
	}



}
