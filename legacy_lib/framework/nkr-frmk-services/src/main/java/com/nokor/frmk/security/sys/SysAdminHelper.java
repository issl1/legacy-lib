package com.nokor.frmk.security.sys;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.security.CryptoHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.util.Assert;
import org.springframework.util.Base64Utils;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.security.model.SecProfile;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.security.spring.encoding.SecSaltGenerator;



/**
 * 
 * @author prasnar
 *
 */
public class SysAdminHelper implements FrmkServicesHelper {
	private static Logger logger = LoggerFactory.getLogger(SysAdminHelper.class.getName());
	private static final String FIELD_SEP = "@#@";
	private static final String FIELD_SEP_MASTER_KEY = "(#@#)";

	private static int idx = 0;
	private static final int IDX_RANDOM_NUMBER1 = idx++;
	private static final int IDX_SYS_ADM_LOGIN = idx++;
	private static final int IDX_RANDOM_NUMBER2 = idx++;
	private static final int IDX_SYS_ADM_PWD = idx++;
	private static final int IDX_ENCRYPT_DATE = idx++;

	
	/**
	 * Create a new sysAdmin user (login/password)
	 * 
	 * @param sysAdmlogin
	 * @param sysAdmPassword
	 * @return
	 */
	public static String createSysAdmin(String sysAdmlogin, String sysAdmPassword) {
		try {
			checkCurrentConnectedUser();
			
			String masterPwdForEncryption = buildMasterPwdForEncryption(sysAdmlogin, sysAdmPassword);
			String clearMessage = serializeMessage(sysAdmlogin, sysAdmPassword);
			
			String encryptedMessage = CryptoHelper.encryptWithMasterPwd(masterPwdForEncryption, clearMessage);

			logger.debug("Clear message [" + clearMessage + "]");
			logger.debug("Encrypted message [" + encryptedMessage + "]");
			
			String decryptedMessage = CryptoHelper.decryptWithMasterPwd(masterPwdForEncryption, encryptedMessage);
			logger.debug("Decrypted message [" + decryptedMessage + "]");
			
			Assert.isTrue(clearMessage.equals(decryptedMessage), "The message decryption has failed.");
			
			logger.info("SysAdmlogin [" + sysAdmlogin + "] LicenseKey [" + encryptedMessage + "]");
			
			return encryptedMessage;

		} catch (Exception e) {
			String errMsg = "License creation failed.";
			logger.error(errMsg, e);
			throw new IllegalStateException(errMsg, e);
		}
	}
	
	
	/**
	 * 
	 * @param sysAdmLogin
	 * @param sysAdmPassword
	 * @param encryptedMessage
	 * @return
	 */
	public static boolean isValidMessage(String sysAdmLogin, String sysAdmPassword, String encryptedMessage) {
		Assert.notNull(sysAdmPassword, "sysAdmPassword can not be null");
		
		SysAdminHelper.checkCurrentConnectedUser();

		try {
		
			// Serialize MasterPwd from SecSession
			String masterPwdForEncryption = buildMasterPwdForEncryption(sysAdmLogin, sysAdmPassword);
	
			// Get clear message from SecSession 
			String clearMsgFromSession = CryptoHelper.decryptWithMasterPwd(masterPwdForEncryption, encryptedMessage);
			
			if (StringUtils.isEmpty(clearMsgFromSession)) {
				logger.warn("User [" + sysAdmLogin + "] - Decryption failed.");
				return false;
			}
	
			String[] clearFields = clearMsgFromSession.split(FIELD_SEP);
			
			// Simple verification of the fields
			boolean isValid = StringUtils.isNoneEmpty(clearFields[IDX_RANDOM_NUMBER1])
					&& clearFields[IDX_SYS_ADM_LOGIN].equals(sysAdmLogin)
					&& StringUtils.isNoneEmpty(clearFields[IDX_RANDOM_NUMBER2])
					&& clearFields[IDX_SYS_ADM_PWD].equals(sysAdmPassword)
					&& DateUtils.isDate(clearFields[IDX_ENCRYPT_DATE]);
	
			if (!isValid) {
				logger.info("User [" + sysAdmLogin + "] - The message is not valid.");
			}
			
		
			return isValid;
			
		} catch (Exception e) {
			String errMsg = "Error on validating message";
			logger.error(errMsg, e);
			throw new IllegalStateException(errMsg, e);
		}
	}
	

	/**
	 * 
	 * @param adminUser
	 * @return
	 */
	public static SecUser checkAgainstCurrentConnectedUser(SecUser adminUser) {
		SecUser currentConnectedUser = checkCurrentConnectedUser();
		
		if (!adminUser.isInAdminProfile()) {
			throw new InsufficientAuthenticationException("The adminUser [" + adminUser.getLogin() + "] is not part of [" + SecProfile.ADMIN.getCode() + "] profile.");
		}
		if (!adminUser.getId().equals(currentConnectedUser.getId()) || !adminUser.isInAdminProfile()) {
			throw new InsufficientAuthenticationException("The adminUser [" + adminUser.getLogin() + "] is not the current connected user.");
		}
		
		return currentConnectedUser;
	}

	/**
	 * 
	 * @return
	 */
	public static SecUser checkCurrentConnectedUser() {
		SecUser currentConnectedUser = UserSessionManager.getCurrentUser();
		if (currentConnectedUser == null) {
			throw new InsufficientAuthenticationException("No user is connected.");
		}
		if (!currentConnectedUser.isInAdminProfile()) {
			throw new InsufficientAuthenticationException("Only [" + SecProfile.ADMIN.getCode() + "] profile is autorized for this operation.");
		}
		
		return currentConnectedUser;
	}
	
	/**
	 * Serialize the MasterPwd
	 * 
	 * @param sysAdmlogin
	 * @param sysAdmPassword
	 * @return
	 */
	public static String buildMasterPwdForEncryption(String sysAdmlogin, String sysAdmPassword) {
		Assert.notNull(sysAdmPassword, "sysAdmPassword can not be null");

		String line = sysAdmlogin + FIELD_SEP_MASTER_KEY + sysAdmPassword;
		
		String masterPwdForEncryption = Base64Utils.encodeToString(line.getBytes());
		return masterPwdForEncryption;
	}
	
	/**
	 * 
	 * @param sysAdmlogin
	 * @param sysAdmPassword
	 * @return
	 */
	public static String serializeMessage(String sysAdmlogin, String sysAdmPassword) {
		Assert.notNull(sysAdmPassword, "sysAdmPassword can not be null");

		String masterPwdForEncryption = SecSaltGenerator.generateSecureRandom()
										+ FIELD_SEP + sysAdmlogin 
										+ FIELD_SEP + SecSaltGenerator.generateSecureRandom()
										+ FIELD_SEP + sysAdmPassword 
										+ FIELD_SEP + DateUtils.todayFull();
		
		return masterPwdForEncryption;
	}


	
}
