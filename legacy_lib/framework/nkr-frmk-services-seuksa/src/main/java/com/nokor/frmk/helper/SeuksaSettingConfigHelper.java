package com.nokor.frmk.helper;

import org.seuksa.frmk.model.entity.RefDataId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * 
 * @author prasnar
 * @version $Revision$
 *
 */
public class SeuksaSettingConfigHelper implements SeuksaServicesHelper {
	protected static final Logger logger = LoggerFactory.getLogger(SeuksaSettingConfigHelper.class);
    
    /** ---------------------------------------------------------
     *  SECURITY
     ** ---------------------------------------------------------*/
    public final static String APP_SEC_CREDENTIAL_EXPIRED_AFTER_NB_DAYS = "app.security.credential.expired.after.nb.days";
    public final static int APP_SEC_CREDENTIAL_EXPIRED_AFTER_NB_DAYS_DEFAULT_VALUE = 0;
    public final static String APP_SEC_LOGIN_NB_MAX_ATTEMPTS = "app.security.login.nb.max.attempts";
    public final static int APP_SEC_LOGIN_NB_MAX_ATTEMPTS_DEFAULT_VALUE = 0;
    
    public final static String APP_REFDATA_DEFAULT_PREFIX = "app.refdata.default.";
    
   

    /**
     * Constructor
     * Can not be instantiated
     */
	protected SeuksaSettingConfigHelper() {
	}

   /**
    * 
    * @return
    */
   public static <T extends RefDataId> long getRefDataDefaultId(Class<T> refDataClazz) {
  		return SETTING_SRV.getValueLong(APP_REFDATA_DEFAULT_PREFIX + refDataClazz.getSimpleName(), 0);
   }


	/**
    * 
    * @return
    */
   public static int getCredentialExpiredAfterNbDays() {
   	try {
   		return SETTING_SRV.getValueInt(APP_SEC_CREDENTIAL_EXPIRED_AFTER_NB_DAYS);
   	} catch (Exception e) {
   		logger.error("Error while getting [APP_SEC_CREDENTIAL_EXPIRED_AFTER_NB_DAYS]");
   		logger.error("Return default value [" + APP_SEC_CREDENTIAL_EXPIRED_AFTER_NB_DAYS_DEFAULT_VALUE + "]");
   		return APP_SEC_CREDENTIAL_EXPIRED_AFTER_NB_DAYS_DEFAULT_VALUE;
   	}
   }
   
   /**
    * 
    * @return
    */
   public static int getLoginNbMaxAttemps() {
   	try {
   		return SETTING_SRV.getValueInt(APP_SEC_LOGIN_NB_MAX_ATTEMPTS);
   	} catch (Exception e) {
   		logger.error("Error while getting [APP_SEC_LOGIN_NB_MAX_ATTEMPS]");
   		logger.error("Return default value [" + APP_SEC_LOGIN_NB_MAX_ATTEMPTS_DEFAULT_VALUE + "]");
   		return APP_SEC_LOGIN_NB_MAX_ATTEMPTS_DEFAULT_VALUE;
   	}
   }
 
}
