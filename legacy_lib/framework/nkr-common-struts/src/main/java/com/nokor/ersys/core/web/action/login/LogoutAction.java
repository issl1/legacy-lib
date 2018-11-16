package com.nokor.ersys.core.web.action.login;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.common.app.tools.helper.AppReferenceDataHelper;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.web.struts.action.BasePageAction;

/**
 * 
 * @author prasnar
 *
 */
public class LogoutAction extends BasePageAction {
	/** */
	private static final long serialVersionUID = -5873832987862970111L;

	/**
	 * 
	 */
	public LogoutAction() {
		logger.info("LogoutAction:" + (getSecUser() != null ? getSecUser().getLogin() : "null"));
	}
	
	@Override
	protected void processPage() throws Exception {
	}

	@Override
	public void viewAction() throws Exception {
	}
	
	/**
	 * 
	 */
	public void logoutExecuteAction() {
		SecUser secUser = getSecUser();
        if (secUser == null) {
        	return;
        }
        logger.info("The user [" + secUser.getLogin() + "] has logged out sucessfully at: " +  DateUtils.todayH00M00S00());
    	saveManualAppEvent(AppReferenceDataHelper.getSecEventActionLOGOUT(), I18N.message("app.activity.msg.logout", secUser.getLogin()), null);
        
    	// SessionManager.clearBookBasket(getRequest().getSession());
    	APP_SESSION_MNG.logoutCurrent();
        
	}

}
