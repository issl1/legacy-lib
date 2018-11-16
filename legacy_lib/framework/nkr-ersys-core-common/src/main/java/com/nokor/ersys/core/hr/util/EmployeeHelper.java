package com.nokor.ersys.core.hr.util;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.slf4j.LoggerFactory;

import com.nokor.common.app.tools.helper.AppServicesHelper;
import com.nokor.ersys.core.hr.model.organization.Employee;
import com.nokor.frmk.helper.FrmkAppConfigFileHelper;
import com.nokor.frmk.security.SecurityHelper;
import com.nokor.frmk.security.model.SecProfile;
import com.nokor.frmk.security.model.SecUser;

/**
 * 
 * @author prasnar
 *
 */
public class EmployeeHelper implements AppServicesHelper {

	private static boolean hasInitMasterPwdInCrypto = false;

	/**
	 * 
	 * @param secUsrId
	 * @return
	 */
	public static String getEmployeeNameBySecUser(Long secUsrId) {
		Employee employee = EMPL_SRV.findEmployeeByUser(secUsrId);

		if (employee != null) {
			return employee.getFullName();
		}

		return null;
	}

	/**
	 * Get Employee by email
	 * 
	 * @param email
	 * @return
	 */
	public static String getEmployeeNameByEmail(String email) {

		Employee employee = EMPL_SRV.findFirstEmployeeByEmail(email);

		if (employee != null) {
			return employee.getFullName();
		}

		return null;
	}

	/**
	 *
	 * @param email
	 * @return
	 */
	public static Employee getEmployeeByEmail(String email) {
		Employee employee = EMPL_SRV.findFirstEmployeeByEmail(email);
		return employee;
	}

	/**
	 * @return the hasInitMasterPwdInCrypto
	 */
	public static boolean hasInitMasterPwdInCrypto() {
		return hasInitMasterPwdInCrypto;
	}

	/**
	 * @param hasInitMasterPwdInCrypto
	 *            the hasInitMasterPwdInCrypto to set
	 */
	public static void setHasInitMasterPwdInCrypto(boolean hasInitMasterPwdInCrypto) {
		EmployeeHelper.hasInitMasterPwdInCrypto = hasInitMasterPwdInCrypto;
	}

	/**
	 * Only when the new website launched only
	 * 
	 * @param employee
	 * @return
	 */
	public static boolean sendEmailWelcomeMemberWithAccessInfo(Employee employee) {
		// normally no need it is already done in StartupListener for FO/BO
		// if (!EmployeeHelper.hasInitMasterPwdInCrypto()) {
		// CryptoHelper.init(AppConfigUtil.getMasterKeyTmp().toCharArray());
		// }
		// String passwordRaw = CryptoHelper.decrypt(employee.getEmpTmp1()) +
		// CryptoHelper.decrypt(employee.getEmpTmp2());
		//
		// if (passwordRaw == null || StringUtils.isEmpty(passwordRaw)) {
		// LoggerFactory.getLogger(EmployeeHelper.class).warn("The passwordRaw is empty");
		// return false;
		// }
		//
		// try {
		// SecUser secUser = employee.getSecUser();
		// if (secUser == null) {
		// return false;
		// }
		// UserInfo to = SessionManager.buildUserInfo(employee.getSecUser());
		//
		// if (!StringUtils.isEmpty(to.getEmail())) {
		//
		// SecUser admin = SecurityAccessHelper.getSnefccaAdmin();
		// UserInfo from = SessionManager.buildUserInfo(admin);
		// from.setEmail(AppConfigUtil.getEmailNotification());
		//
		// Map<String, Object> templateVariables = new HashMap<String,
		// Object>();
		// ConfigEmail configEmail = null;
		//
		// // Send Email and Change password
		// configEmail =
		// ConfigEmailUtil.getFirstWelcomeMemberAccessInfoEmailing();
		//
		// final String htmlSubject = configEmail.getEmailSubject();
		// final String htmlBody =
		// mailSender.renderHtmlBody(configEmail.getEmailText());
		//
		// templateVariables.put("cfgId", configEmail.getId());
		// templateVariables.put("to", employee.getCivilityAndFullName());
		// templateVariables.put("login", employee.getSecUser().getLogin());
		// templateVariables.put("password", passwordRaw);
		//
		// try {
		// mailSender.sendEmail(to, from, htmlSubject, htmlBody,
		// templateVariables);
		// } catch (Exception e) {
		// LoggerFactory.getLogger(EmployeeHelper.class).errorStackTrace("Error while sending the email",
		// e);
		// return false;
		// }
		// }
		// } catch (Exception e) {
		// LoggerFactory.getLogger(EmployeeHelper.class).errorStackTrace("Error while preparing the email",
		// e);
		// return false;
		// }

		return true;
	}

	/**
	 *
	 * @param employee
	 * @param passwordRaw
	 * @param isChangePassword
	 */
	public static void sendEmailUserAccessInfo(Employee employee, String passwordRaw, Boolean isChangePassword, boolean isBO) {
		// SecUser secUser = employee.getSecUser();
		//
		// if (secUser == null || StringUtils.isEmpty(passwordRaw)) {
		// return;
		// }
		//
		// UserInfo to = SessionManager.buildUserInfo(employee , false);
		//
		// if (!StringUtils.isEmpty(to.getEmail())) {
		//
		// SecUser admin = SecurityAccessHelper.getSnefccaAdmin();
		// UserInfo from = SessionManager.buildUserInfo(admin);
		// from.setEmail(AppConfigUtil.getEmailNotification());
		//
		// Map<String, Object> templateVariables = new HashMap<String,
		// Object>();
		// ConfigEmail configEmail = null;
		//
		// // Send Email and Change password
		// if (isChangePassword) {
		// if (isBO) {
		// configEmail = ConfigEmailUtil.getSnefccaChangeMemberAccessInfo();
		// }
		// else {
		// configEmail = ConfigEmailUtil.getUserChangePasswordInFO();
		// templateVariables.put("user_name", employee.getFullName());
		// }
		//
		// // Map<String, Object> templateVariables = new HashMap<String,
		// Object>();
		// // templateVariables.put("to", employee.getLastNameFirstName());
		// // templateVariables.put("password", passwordRaw);
		// // templateVariables.put("user_name", employee.getFullName());
		// // EMAIL_SENDER_SRV.sendEmail(to, from,
		// MailUtils.MAIL_TEMPLATE_CHANGE_PASSWORD_SUBJECT,
		// MailUtils.MAIL_TEMPLATE_CHANGE_PASSWORD_BODY, templateVariables);
		//
		// // Send Email when create new user
		// }
		// else {
		// configEmail = ConfigEmailUtil.getSentLoginAccessInfo();
		//
		// // Map<String, Object> templateVariables = new HashMap<String,
		// Object>();
		// // templateVariables.put("to", employee.getLastNameFirstName());
		// // templateVariables.put("login", employee.getSecUser().getLogin());
		// // templateVariables.put("password", passwordRaw);
		// // EMAIL_SENDER_SRV.sendEmail(to, from,
		// MailUtils.MAIL_TEMPLATE_CREATE_USER_SUBJECT,
		// MailUtils.MAIL_TEMPLATE_CREATE_USER_BODY, templateVariables);
		// }
		//
		// final String htmlSubject = configEmail.getEmailSubject();
		// final String htmlBody =
		// mailSender.renderHtmlBody(configEmail.getEmailText());
		//
		// templateVariables.put("cfgId", configEmail.getId());
		// templateVariables.put("to", employee.getCivilityAndFullName());
		// templateVariables.put("login", employee.getSecUser().getLogin());
		// templateVariables.put("password", passwordRaw);
		// mailSender.sendEmail(to, from, htmlSubject, htmlBody,
		// templateVariables);

		// }
	}

	/**
	 *
	 * @param employee
	 */
	public static String removeUserAccess(Employee employee) {
		if (employee.getSecUser() != null) {
			LoggerFactory.getLogger(EmployeeHelper.class).info("Employee [" + employee.getId() + "] - Empty email - Remove secUser");

			try {
				// Archive the user
				EMPL_SRV.removeUserAccess(employee);
			} catch (Exception e) {
				String msgErr = "Error while in EMPLOYEE_SRV.removeUserAccess";
				LoggerFactory.getLogger(EmployeeHelper.class).error(msgErr, e);
				return msgErr;
			}
		}
		return null;
	}

	/**
	 *
	 * @param employee
	 */
	public static String buildAndSendUserAccess(Employee employee, SecProfile defaultProfile, String loginParam, String passwordRaw, Boolean isSendPasswordByEmail) {
		if (isSendPasswordByEmail == null) {
			isSendPasswordByEmail = false;
		}

		String login = loginParam;

		// Should never appear, since an email regexp already protects this
		// error
		if (!SecurityHelper.isValidLogin(login)) {
			return I18N.message("message.error.login.not.valid");
		}

		String passwordRawReal = StringUtils.trim(passwordRaw);
		if (StringUtils.isEmpty(passwordRawReal)) {
			LoggerFactory.getLogger(EmployeeHelper.class).info("Employee [" + employee.getId() + ", " + login);
			isSendPasswordByEmail = false;
			// return null;
		}

		// Need to generate the pwd for the following cases:
		// - either the user is not created yet
		// - either the user is created, but the login is not a valid login
		// (email)
		// - either it was requested
		// (passwordRawReal.equals(PasswordUtils.DEFAULT_PWD))
		if (FrmkAppConfigFileHelper.getTokenForPwdAutoGeneration().equals(passwordRawReal)) {
			passwordRawReal = SecurityHelper.getGenerateRandomSecurePassword();
			employee.setPwdInTmp(passwordRawReal);
		}
		Boolean isChangePassword = Boolean.FALSE;

		// Employee could have the same user login in the same company
		// Boolean validUserAccess =
		// EMPLOYEE_SRV.validateEmployeeUserAccess(login,
		// employee.getCompany().getId());
		//
		// if (!validUserAccess) {
		// String msg = I18N.message("message.error.user.login.already.exists",
		// login);
		// LoggerFactory.getLogger(EmployeeHelper.class).info("Employee [" +
		// employee.getId() + "] - " + msg);
		// return msg;
		// }

		SecUser secUserFound = SECURITY_SRV.loadUserByUsername(login);

		// Create user access for employee
		String descLogin = login.equals(SecurityHelper.DEFAULT_ADMIN_LOGIN) ? "Administrator" : employee.getFullName();
		if (employee.getSecUser() == null) {
			if (secUserFound != null) {
				String msg = I18N.message("message.error.user.login.already.exists", secUserFound.getLogin());
				LoggerFactory.getLogger(EmployeeHelper.class).info("Employee [" + employee.getId() + "] - " + msg);
				return msg;
			} else {
				// TODO to add a profile: mandatory
				// ESecProfile profile = null;
				SecUser secUser = EMPL_SRV.createUser(employee, login, descLogin, passwordRawReal, defaultProfile);
				if (secUser == null) {
					String msg = I18N.message("message.error.user.login.creation", employee.getFullName());
					LoggerFactory.getLogger(EmployeeHelper.class).error("Employee [" + employee.getId() + "] - " + msg);
					return msg;
				}
				employee.setSecUser(secUser);
			}

			employee.getSecUser().setDefaultProfile(defaultProfile);
			employee.getSecUser().getProfiles().clear();
			employee.getSecUser().getProfiles().add(defaultProfile);

			ENTITY_SRV.merge(employee);
		}
		// Update login or change password
		else {

			isChangePassword = StringUtils.isNotEmpty(passwordRawReal);

			if (secUserFound != null) {
				if (isChangePassword) {
					SECURITY_SRV.changePassword(secUserFound, passwordRawReal);
				}
				employee.setSecUser(secUserFound);
			} else {
				if (isChangePassword) {
					SECURITY_SRV.changePassword(employee.getSecUser(), passwordRawReal);
				}
			}

			employee.getSecUser().setLogin(login);
			employee.getSecUser().setDesc(descLogin);
			employee.getSecUser().setDefaultProfile(defaultProfile);

			employee.getSecUser().getProfiles().clear();
			employee.getSecUser().getProfiles().add(defaultProfile);

			EMPL_SRV.update(employee);
		}

		if (isSendPasswordByEmail && !StringUtils.isEmpty(login) && employee.getSecUser() != null) {
			EmployeeHelper.sendEmailUserAccessInfo(employee, passwordRawReal, isChangePassword, true);
		}

		return null;
	}

}
