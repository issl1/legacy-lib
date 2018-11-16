package com.nokor.frmk.helper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.tools.MyStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.frmk.config.AppConfigFile;
import com.nokor.frmk.security.SecurityHelper;
import com.nokor.frmk.security.model.SecApplication;


/**
 * 
 * @author prasnar
 * @version $Revision$
 *
 */
public class SeuksaAppConfigFileHelper implements SeuksaServicesHelper  {
	protected static final Logger logger = LoggerFactory.getLogger(SeuksaAppConfigFileHelper.class);

    public final static String APP_CONFIG_FILES = "core.app.config.files";
    public final static String APP_CODE = "core.app.code";

    public final static String OTHER_APP_CONFIG_FILES = "core.app.other.app-config.files";
    public final static String OTHER_APP_GLOBAL_CONFIG_FILES = "core.app.other.app-global-config.files";
    
    private static Boolean isSecurityLdapMode;
    private static Boolean isSecurityLdapExceptAdmin;
    public final static String APP_SECURITY_LDAP_EXCEPT_ADMIN = "core.app.security.ldap.except.admin";
    public final static String APP_SECURITY_LDAP_MODE = "core.app.security.ldap.mode";
    public final static String APP_SECURITY_LDAP_SERVICE_CLASS = "core.app.security.ldap.service.class";
    public final static String APP_SECURITY_LDAP_SERVICE_NAME = "core.app.security.ldap.service.name";
    public final static String APP_THIRD_CONNECTION_MODE_MOCKING = "core.app.connection.third.mode.mocking";

    public final static String APP_SECURITY_CHECK_TABLE_APPLICATION_USER = "core.app.security.check.table[sec_application_user]";
    public final static String APP_SECURITY_CHECK_TABLE_USER_PROFILE = "core.app.security.check.table[sec_user_profile]";
    public final static String APP_SECURITY_CHECK_TABLE_APPLICATION_PROFILE = "core.app.security.check.table[sec_application_profile]";
    public final static String APP_SECURITY_CHECK_TABLE_APPLICATION_USER_PROFILE = "core.app.security.check.table[sec_application_user_profile]";
    public final static String LOGIN_PREFIX_DEFAULT = "user-";
    public final static String APP_SECURITY_LOGIN_PREFIX = "core.app.security.login.prefix";
    public final static String APP_SECURITY_TOKEN_FOR_PWD_AUTO_GENERATION = "core.app.security.token.for.pwd.auto.generation";

    public final static String APP_REF_DATA_DESC_IS_MANDATORY = "core.app.refdata.desc.is.mandatory";
    public final static String APP_REF_DATA_DESC_EN_IS_MANDATORY = "core.app.refdata.desc.en.is.mandatory";

    public final static String APP_SYS_TASKS_IS_REQUIRED = "core.app.sys.is.required";
    public final static String APP_SYS_TASKS_BEFORE_SPRING_CONTEXT_SQL_FOLDER = "core.app.sys.tasks.before.spring.context.sql.folder";
    public final static String APP_SYS_TASKS_AFTER_SPRING_CONTEXT_SQL_FOLDER = "core.app.sys.tasks.after.spring.context.sql.folder";
    public final static String APP_SYS_TASKS_AFTER_STARTUP_SQL_FOLDER = "core.app.sys.tasks.after.startup.sql.folder";

    private final static String TRUE = "true";
    private final static String TRUE_1 = "1";
    private final static List<String> TRUE_LST = Arrays.asList(TRUE, TRUE_1);


    /**
     * Constructor
     * Can not be instantiated
     */
	protected SeuksaAppConfigFileHelper() {
	}

	/**
	 * 
	 * @param code
	 * @return
	 */
	public static String getValue(String code) {
		return AppConfigFile.getInstance().getProperty(code);
	}
	
	/**
	 * 
	 * @param code
	 * @param defaultVal
	 * @return
	 */
	public static String getValue(String code, String defaultVal) {
		String val = AppConfigFile.getInstance().getProperty(code);
		return StringUtils.isNotBlank(val) ? val : defaultVal;
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static boolean getValueBoolean(String code) {
		try {
			String value = getValue(code);
			if (StringUtils.isEmpty(value)) {
				return false;
			}
			value = value.trim();
			
            return isTrue(value);
        }
        catch (Exception e) {
            String errMsg = "[getValueBoolean] Error for the value [" + code + "] - Check its existence in the Advanced Options or in the ts_setting table";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg, e);
        }
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isTrue(String value) {
		 return TRUE_LST.contains(value);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static long getValueLong(String code) {
		try {
			String value = getValue(code);
            return  StringUtils.isNotEmpty(value) ? Long.valueOf(value) : 0;
        }
        catch (Exception e) {
            String errMsg = "[ValueLong] Error for the value [" + code + "] - Check its existence in the Advanced Options or in the ts_setting table";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg, e);
        }
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static int getValueInt(String code) {
		try {
			String value = getValue(code);
            return  StringUtils.isNotEmpty(value) ? Integer.valueOf(value) : 0;
        }
        catch (Exception e) {
            String errMsg = "[ValueInteger] Error for the value [" + code + "] - Check its existence in the Advanced Options or in the ts_setting table";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg, e);
        }
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static BigDecimal getValueBigDecimal(String code) {
		try {
            return BigDecimal.valueOf(getValueDouble(code));
        }
        catch (Exception e) {
            String errMsg = "[ValueBigDecimal] Error for the value [" + code + "] - Check its existence in the Advanced Options or in the ts_setting table";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg, e);
        }
	}

	/**
	 * 
	 * @param code
	 * @return
	 */
	public static Double getValueDouble(String code) {
		try {
			String value = getValue(code);
            return StringUtils.isNotEmpty(value) ? Double.valueOf(value) : 0L;
        }
        catch (Exception e) {
            String errMsg = "[ValueDouble] Error for the value [" + code + "] - Check its existence in the Advanced Options or in the ts_setting table";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg, e);
        }
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static List<Long> getValuesID(String code) {
		return getValuesLong(code);
	}

	/**
	 * 
	 * @param code
	 * @return
	 */
	public static List<Long> getValuesLong(String code) {
		String propStr = getValue(code);
        if (StringUtils.isEmpty(propStr)) {
            logger.warn("[" + code + "] is not defined in the Application Advanced Configuration.");
            return new ArrayList<Long>();
        }
		return MyStringUtils.getValuesLong(propStr);
	}
	


	/**
	 * 
	 * @param code
	 * @return
	 */
	public static List<Integer> getValuesInt(String code) {
		String propStr = getValue(code);
        if (StringUtils.isEmpty(propStr)) {
            logger.warn("[" + code + "] is not defined in the Application Advanced Configuration.");
            return new ArrayList<Integer>();
        }

        return MyStringUtils.getValuesInt(propStr);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static List<String> getValues(String code) {
		String propStr = getValue(code);
        if (StringUtils.isEmpty(propStr)) {
            logger.warn("[" + code + "] is not defined in the Application Advanced Configuration.");
            return new ArrayList<String>();
        }

        String[] values = propStr.replace(" ", "").split(MyStringUtils.LIST_SEPARATOR_COMMA);
		return Arrays.asList(values);
	}


	
	/**
	 * 
	 * @return
	 */
	public static String getApplicationCode() {
		return AppConfigFile.getInstance().getProperty(APP_CODE);
    }
	

	
	/**
	 * 
	 * @return
	 */
	public static SecApplication getSecApplication() {
		String appCode = getApplicationCode();
		if (StringUtils.isEmpty(appCode)) {
	    	throw new IllegalStateException("The application code [" + SeuksaAppConfigFileHelper.APP_CODE + "] should be defined in [" + AppConfigFile.getConfigFile() + "]");
		}
	    SecApplication secApp = ENTITY_SRV.getByCode(SecApplication.class, appCode);
	    if (secApp == null) {
	    	throw new IllegalStateException("The SecApplication Code set in [" + AppConfigFile.getConfigFile() + "] is invalid [" + appCode + "].");
	    }
	    return secApp;
	}
   
	/**
	 * 
	 * @return
	 */
	public static List<String> getOtherAppGlobalConfigFiles() {
		return getValues(OTHER_APP_GLOBAL_CONFIG_FILES);
    }
	
	/**
	 * 
	 * @return
	 */
	public static List<String> getOtherAppConfigFiles() {
		return getValues(OTHER_APP_CONFIG_FILES);
    }

    /**
     * 
     * @return
     */
    public static boolean isSecurityLdapMode() {
		if (isSecurityLdapMode == null) {
			isSecurityLdapMode = getValueBoolean(APP_SECURITY_LDAP_MODE);
		}
		return isSecurityLdapMode;
    }

    /**
	 * @param isSecurityLdapMode the isSecurityLdapMode to set
	 */
	public static void setSecurityLdapMode(Boolean isSecurityLdapMode) {
		SeuksaAppConfigFileHelper.isSecurityLdapMode = isSecurityLdapMode;
	}
	
	/**
	 * 
	 * @param login
	 * @return
	 */
	public static boolean isSecurityLdapExceptAdmin(String login) {
		return isSecurityLdapMode() 
				&& ((SecurityHelper.isSysAdminLogin(login) && !SeuksaAppConfigFileHelper.isSecurityLdapExceptAdmin())
						|| !SecurityHelper.isSysAdminLogin(login));
	}
	/**
     * 
     * @return
     */
    public static boolean isSecurityLdapExceptAdmin() {
		if (isSecurityLdapExceptAdmin == null) {
			isSecurityLdapExceptAdmin = getValueBoolean(APP_SECURITY_LDAP_EXCEPT_ADMIN);
		}
		return isSecurityLdapExceptAdmin;
    }

    /**
	 * @param isSecurityLdapExceptAdmin the isSecurityLdapExceptAdmin to set
	 */
	public static void setSecurityLdapExceptAdmin(Boolean isSecurityLdapExceptAdmin) {
		SeuksaAppConfigFileHelper.isSecurityLdapExceptAdmin = isSecurityLdapExceptAdmin;
	}

	/**
     * 
     * @return
     */
    public static String getSecurityLdapClass() {
		return AppConfigFile.getInstance().getProperty(APP_SECURITY_LDAP_SERVICE_CLASS);
    }
    
    /**
     * 
     * @return
     */
    public static String getSecurityLdapName() {
		return AppConfigFile.getInstance().getProperty(APP_SECURITY_LDAP_SERVICE_NAME);
    }
    
    /**
     * 
     * @return
     */
    public static boolean isThirdMockingMode() {
		return getValueBoolean(APP_THIRD_CONNECTION_MODE_MOCKING);
    }
    
	/**
	 * 
	 * @return
	 */
	public static boolean checkSecurityTableApplicationUser() {
		return getValueBoolean(APP_SECURITY_CHECK_TABLE_APPLICATION_USER);
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean checkSecurityTableUserProfile() {
		return getValueBoolean(APP_SECURITY_CHECK_TABLE_USER_PROFILE);
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean checkSecurityTableApplicationProfile() {
		return getValueBoolean(APP_SECURITY_CHECK_TABLE_APPLICATION_PROFILE);
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean checkSecurityTableApplicationUserProfile() {
		return getValueBoolean(APP_SECURITY_CHECK_TABLE_APPLICATION_USER_PROFILE);
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getLoginPrefix() {
		String pref = getValue(APP_SECURITY_LOGIN_PREFIX);
		return StringUtils.isNotEmpty(pref) ? pref : LOGIN_PREFIX_DEFAULT;
	}
	

	/**
	 * 
	 * @return
	 */
	public static String getTokenForPwdAutoGeneration() {
		return getValue(APP_SECURITY_TOKEN_FOR_PWD_AUTO_GENERATION);
	}
	
	/**
     * 
     * @return
     */
    public static boolean isRefDataDescMandatory() {
		return getValueBoolean(APP_REF_DATA_DESC_IS_MANDATORY);
    }   
    
    /**
     * 
     * @return
     */
    public static boolean isRefDataDescEnMandatory() {
		return getValueBoolean(APP_REF_DATA_DESC_EN_IS_MANDATORY);
    }


    /**
     * 
     * @return
     */
    public static boolean isSysTasksRequired() {
		return getValueBoolean(APP_SYS_TASKS_IS_REQUIRED);
    }
    
    /**
     * 
     * @return
     */
    public static String getSysTaskBeforeSpringContextSqlFolder() {
    	try {
    		return AppConfigFile.getInstance().getProperty(APP_SYS_TASKS_BEFORE_SPRING_CONTEXT_SQL_FOLDER);
    	} catch (Exception e) {
             LoggerFactory.getLogger(SeuksaAppConfigFileHelper.class).error("Error reading property [" + APP_SYS_TASKS_BEFORE_SPRING_CONTEXT_SQL_FOLDER + "] into String.", e);
             String errMsg = "Error reading property [" + APP_SYS_TASKS_BEFORE_SPRING_CONTEXT_SQL_FOLDER + "].";
             throw new IllegalStateException(errMsg, e);
         }
    }
    
   /**
     * 
     * @return
     */
    public static String getSysTaskAfterSpringContextSqlFolder() {
    	try {
    		return getSysTaskBeforeSpringContextSqlFolder() + AppConfigFile.getInstance().getProperty(APP_SYS_TASKS_AFTER_SPRING_CONTEXT_SQL_FOLDER);
    	} catch (Exception e) {
             LoggerFactory.getLogger(SeuksaAppConfigFileHelper.class).error("Error reading property [" + APP_SYS_TASKS_AFTER_SPRING_CONTEXT_SQL_FOLDER + "] into String.", e);
             String errMsg = "Error reading property [" + APP_SYS_TASKS_AFTER_SPRING_CONTEXT_SQL_FOLDER + "].";
             throw new IllegalStateException(errMsg, e);
         }
    }

    /**
     * 
     * @return
     */
    public static String getSysTaskAfterStartupSqlFolder() {
    	try {
    		return getSysTaskBeforeSpringContextSqlFolder() + AppConfigFile.getInstance().getProperty(APP_SYS_TASKS_AFTER_STARTUP_SQL_FOLDER);
    	} catch (Exception e) {
             LoggerFactory.getLogger(SeuksaAppConfigFileHelper.class).error("Error reading property [" + APP_SYS_TASKS_AFTER_STARTUP_SQL_FOLDER + "] into String.", e);
             String errMsg = "Error reading property [" + APP_SYS_TASKS_AFTER_STARTUP_SQL_FOLDER + "].";
             throw new IllegalStateException(errMsg, e);
         }
    }

}
