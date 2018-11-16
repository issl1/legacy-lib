package com.nokor.frmk.security.sys;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.security.CryptoHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.Base64Utils;

import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.security.sys.model.SecSession;



/**
 * 
 * @author prasnar
 *
 */
public class LicenseHelper implements FrmkServicesHelper {
	private static Logger logger = LoggerFactory.getLogger(LicenseHelper.class.getName());
	private static final String FIELD_SEP = "@#@";

	/**
	 * 
	 * @param user
	 * @return
	 */
	public static boolean checkLicense(SecUser user) {
		try {
			SecSession sess = SYS_SESS_SRV.loadLicense(user);
			if (sess == null) {
				logger.info("User [" + user.getLogin() + "] - No licence found.");
				return false;
			}

			boolean isValid = isValidSessionMessage(sess);
			if (!isValid) {
				logger.info("User [" + user.getLogin() + "] - Licence found but not valid.");
				return false;
			}
			updateSessionByLastInfo(sess);

			logger.info("User [" + user.getLogin() + "] - Licence OK.");
			return true;
		} catch (Exception e) {
			String errMsg = "License not valid.";
			logger.error(errMsg, e);
			throw new IllegalStateException(errMsg, e);
		}
	}

	/**
	 * 
	 * @param user
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static String createLicense(SecUser user, Date startDate, Date endDate) {
		try {
			boolean hasAlreadyLicense = SYS_SESS_SRV.hasLicense(user);
			if (hasAlreadyLicense) {
				logger.debug("The user [" + user.getLogin() + "] has already a license.");
				return null;
			}
			
			SecSession sess = SecSession.createInstance();
			sess.setUser(user);
			sess.setStartTS(startDate);
			sess.setEndTS(endDate);
			sess.setLastHeartbeatTS(startDate);
			sess.setNbDaysAlreadyUsed(0L);
			
			String userLicenseKey = generateLicenseKey();
			String masterPwdForEncryption = serializeMasterPwdForEncryption(sess, userLicenseKey);
			String encryptedMessage = CryptoHelper.encryptWithMasterPwd(masterPwdForEncryption, masterPwdForEncryption);

			logger.debug("Clear message [" + masterPwdForEncryption + "]");
			logger.debug("Encrypted message [" + encryptedMessage + "]");
			
			String decryptedMessage = CryptoHelper.decryptWithMasterPwd(masterPwdForEncryption, encryptedMessage);
			logger.debug("Decrypted message [" + decryptedMessage + "]");
			
			Assert.isTrue(masterPwdForEncryption.equals(decryptedMessage), "The message decryption has failed.");
			
			logger.debug("User [" + user.getLogin() + "] LicenseKey [" + userLicenseKey + "]");

			sess.setMessage(encryptedMessage);
			ENTITY_SRV.create(sess);
			
			return userLicenseKey;

		} catch (Exception e) {
			String errMsg = "License creation failed.";
			logger.error(errMsg, e);
			throw new IllegalStateException(errMsg, e);
		}
	}

	/**
	 * 
	 * @param userName
	 */
	public static void resetLicense(String userName) {
		try {
			Assert.notNull(userName, "UserName can not be null");
			SecUser user = SECURITY_SRV.loadUserByUsername(userName);
			if (user == null) {
				throw new IllegalStateException("The [" + userName + "] can not be found");
			}
			
			SYS_SESS_SRV.deleteForUser(user);
		} catch (Exception e) {
			String errMsg = "Error in reseting the license for [" + userName + "]";
			logger.error(errMsg, e);
			throw new IllegalStateException(errMsg, e);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private static String generateLicenseKey() {
		long current = Calendar.getInstance().getTimeInMillis();
		String licenseKey = Base64Utils.encodeToString((current + "").getBytes());
		return licenseKey;
	}

	/**
	 * 
	 * @return
	 * @throws IllegalStateException
	 */
	private static String getMacAddress() throws IllegalStateException {
		try {
			InetAddress ip = InetAddress.getLocalHost();
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			byte[] mac = network.getHardwareAddress();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i],
						(i < mac.length - 1) ? "-" : ""));
			}
			return sb.toString();
		} catch (Exception e) {
			throw new IllegalStateException("Error in getting the Mac Address", e);
		}
	}
	
	
	/**
	 * 
	 * @return
	 * @throws IllegalStateException
	 */
	private static String getHostname() throws IllegalStateException {
		try {
			InetAddress ip = InetAddress.getLocalHost();
			return ip.getHostName();
		} catch (Exception e) {
			throw new IllegalStateException("Error in getting the Hostname",
					e);
		}
	}
	

	/**
	 * Serialize the MasterPwd
	 * 
	 * @param sess
	 * @param userLicenseKey
	 * @return
	 */
	private static String serializeMasterPwdForEncryption(SecSession sess, String userLicenseKey) {
		if (sess == null || StringUtils.isEmpty(userLicenseKey)) {
			logger.info("User [" + sess.getUser().getLogin() + "] - No licence found.");
			return null;
		}
		String masterPwdForEncryption = getHostname()
				//+ FIELD_SEP + getMacAddress()
				+ FIELD_SEP + sess.getUser().getId()
				+ FIELD_SEP + userLicenseKey
				+ FIELD_SEP + new SimpleDateFormat(DateUtils.FORMAT_YYYYMMDD_HHMMSS).format(sess.getStartTS())
				+ FIELD_SEP + new SimpleDateFormat(DateUtils.FORMAT_YYYYMMDD_HHMMSS).format(sess.getLastHeartbeatTS()) 
				+ FIELD_SEP + sess.getNbDaysAlreadyUsed();
		
		return masterPwdForEncryption;
	}
	
	
	/**
	 * Check SecSession validity/security
	 * . Serialize the MasterPwd
	 * . Decrypt session via the MasterPwd
	 * . Get the message
	 * . Check the message
	 * 
	 * @param sess
	 * @return
	 */
	private static boolean isValidSessionMessage(SecSession sess) {
		Assert.notNull(sess, "SecSession can not be null");

		String userLicenseKey = SYS_SESS_SRV.getUserLicenceKey(sess.getUser());
		
		if (StringUtils.isEmpty(userLicenseKey)) {
			logger.info("User [" + sess.getUser().getLogin() + "] - No licence key found.");
			return false;
		}


		// Serialize MasterPwd from SecSession
		String masterPwdForEncryption = serializeMasterPwdForEncryption(sess, userLicenseKey);

		// Get clear message from SecSession 
		String clearMsgFromSession = CryptoHelper.decryptWithMasterPwd(masterPwdForEncryption, sess.getMessage());
		
		
		if (StringUtils.isEmpty(clearMsgFromSession)) {
			logger.info("User [" + sess.getUser().getLogin() + "] - No message found in SecSession.");
			return false;
		}

		String[] clearFields = clearMsgFromSession.split(FIELD_SEP);
		
		// Simple verification of the fields
		int i = 0;
		boolean isValid = clearFields[i++].equals(getHostname())
				//&& clearFields[i++].equals(getMacAddress())
				&& clearFields[i++].equals(String.valueOf(sess.getUser().getId()))
				&& clearFields[i++].equals(userLicenseKey)
				&& clearFields[i++].equals(new SimpleDateFormat(DateUtils.FORMAT_YYYYMMDD_HHMMSS).format(sess.getStartTS()))
				&& clearFields[i++].equals(new SimpleDateFormat(DateUtils.FORMAT_YYYYMMDD_HHMMSS).format(sess.getLastHeartbeatTS()))
				&& clearFields[i++].equals(String.valueOf(sess.getNbDaysAlreadyUsed()));

		if (!isValid) {
			logger.info("User [" + sess.getUser().getLogin() + "] - The message is not valid.");
		}
		
		
		// Date verification
		
		Date today = DateUtils.today();
		if (today.before(sess.getStartTS())) {
			logger.info("User [" + sess.getUser().getLogin() + "] - Licence not valid yet.");
			return false;
		}
		if (today.after(sess.getEndTS())) {
			logger.info("User [" + sess.getUser().getLogin() + "] - Licence expired.");
			return false;
		}

		// PREVENT FROM HACKING BY CHANGING THE SYSTEM DATE
		if (today.before(sess.getLastHeartbeatTS())) {
			logger.info("User [" + sess.getUser().getLogin() + "] - Licence not valid (LastHeartbeat verification) - The system date seems not correct.");
			return false;
		}

		// PREVENT FROM HACKING BY CHANGING THE SYSTEM DATE
		long diff = DateUtils.dateDiff(sess.getStartTS(), sess.getLastHeartbeatTS());
		long nbDays = DateUtils.milliSec2Days(diff);
		if (nbDays < sess.getNbDaysAlreadyUsed()) {
			logger.info("User [" + sess.getUser().getLogin() + "] - Licence not valid (NbDaysAlreadyUsed verification) - The system date seems not correct.");
			return false;
		}
		
		return true;
	}
	
	/**
	 * 
	 * @param sess
	 */
	private static void updateSessionByLastInfo(SecSession sess) {
		// Update the LastHeartbeat and NbDaysAlreadyUsed
		sess.setLastHeartbeatTS(DateUtils.today());
		long diff = DateUtils.dateDiff(sess.getStartTS(), sess.getLastHeartbeatTS());
		long nbDays = DateUtils.milliSec2Days(diff);
		sess.setNbDaysAlreadyUsed(nbDays);
		
		// Update the message
		String userLicenseKey = SYS_SESS_SRV.getUserLicenceKey(sess.getUser());
		String masterPwdForEncryption = serializeMasterPwdForEncryption(sess, userLicenseKey);
		sess.setMessage(CryptoHelper.encryptWithMasterPwd(masterPwdForEncryption, masterPwdForEncryption));
		
		ENTITY_SRV.update(sess);
	}
	
	
}
