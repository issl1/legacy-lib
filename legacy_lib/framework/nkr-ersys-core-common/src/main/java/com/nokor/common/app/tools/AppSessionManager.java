package com.nokor.common.app.tools;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.nokor.common.app.menu.MenuHelper;
import com.nokor.common.app.tools.helper.AppServicesHelper;
import com.nokor.common.app.web.session.AppSessionKeys;
import com.nokor.common.app.web.session.AppUserSession;
import com.nokor.ersys.core.hr.model.organization.Employee;
import com.nokor.frmk.security.model.SecControl;
import com.nokor.frmk.security.model.SecControlProfilePrivilege;
import com.nokor.frmk.security.model.SecUser;

/**
 * 
 * @author prasnar
 *
 */
@Service("myAppSessionManager")
public class AppSessionManager extends UserSessionManager implements AppSessionKeys, AppServicesHelper {
	/** */
	private static final long serialVersionUID = 4821111508452056970L;

	/**
	 * 
	 */
	protected AppSessionManager() {
		super();
	}

	/**
	 * 
	 * @return
	 */
	public AppUserSession getCurrent() {
		AppUserSession session = (AppUserSession) SessionStrategy.getSession();
		if (session == null) {
			session = new AppUserSession();
			buildSessionInfo(session, getCurrentUser());
			SessionStrategy.setSession(session);
		}
		return session;
	}
	
	/**
	 * 
	 */
	public void init() {
		super.init();
 	}

	/**
	 * 
	 */
	public void logoutCurrent() {
		super.logoutCurrent();
	}

	/**
	 * 
	 * @param session
	 * @param secUser
	 * @return
	 */
	private static AppUserSession buildSessionInfo(AppUserSession session, SecUser secUser) {
		if (secUser == null || secUser.getId() == null) {
			return null;
		}
		
		Employee emp = EMPL_SRV.findFirstEmployeeByUser(secUser.getId());
		if (emp == null) {
			throw new IllegalStateException("The secUser [" + secUser.getId() + "] can not be found in Employee.");
		}

		session.setEmpId(emp.getId());
		session.setName(emp.getSecUser().getLogin());
		session.setCivility(emp.getCivility().getCode());
		session.setLastName(emp.getLastName());
		session.setFirstName(emp.getFirstName());
		session.setEmail(emp.getEmailPro());

		return session;
	}

	
	/**
	 * 
	 * @return
	 */
	public Employee getCurrentEmployee() {
		SecUser user = getCurrentUser();
		Employee emp = EMPL_SRV.findEmployeeByUser(user.getId());
		return emp;
	}

	
	/**
	 * 
	 * @param actionName
	 * @return
	 */
	public boolean isAllowedActionMenu(String actionName) {
		List<SecControlProfilePrivilege> privileges = getCurrent().getControlProfilePrivileges();
		return MenuHelper.isAllowedMenuAction(actionName, privileges);
	}
	
	/**
	 * 
	 * @param actionName
	 * @return
	 */
	public boolean isAllowedRefTableCode(String tableCode) {
		List<SecControlProfilePrivilege> privileges = getCurrent().getControlProfilePrivileges();
		SecControl secCtl = ENTITY_SRV.getByCode(SecControl.class, tableCode);
		return MenuHelper.hasPrivilegeOnControl(privileges, secCtl);
	}
	
	/**
	 * 
	 * @param lstSecUser
	 * @return
	 */
	public static List<AppUserSession> buildUserInfosxx(List<SecUser> lstSecUser) {
		List<AppUserSession> lstUserInfo = new ArrayList<AppUserSession>();
//		for (SecUser secUser : lstSecUser) {
//			lstUserInfo.add(buildSessionInfo(secUser));
//		}
		return lstUserInfo;
	}



}
