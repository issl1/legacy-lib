package com.nokor.frmk.helper;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.frmk.config.AppConfigFile;


/**
 * 
 * @author prasnar
 * @version $Revision$
 *
 */
public class FrmkAppConfigFileHelper extends SeuksaAppConfigFileHelper{
	protected static final Logger logger = LoggerFactory.getLogger(SeuksaAppConfigFileHelper.class);

    public final static String APP_I18N_LANGUAGE_LIST = "core.app.i18n.language.list";
    public final static String APP_I18N_BUNDLE_NAME = "core.app.i18n.bundle.name";
    public final static String APP_I18N_BUNDLE_NAME_LIST = "core.app.i18n.bundle.name.list";
    public final static String APP_I18N_ENCODING_SOURCE = "core.app.i18n.encoding.source";
    public final static String APP_I18N_ENCODING_TARGET = "core.app.i18n.encoding.target";
    
    public final static String APP_ENVERS_IS_EVENT_INSERT = "core.app.hibernate.envers.is.event.insert";
    public final static String APP_ENVERS_IS_EVENT_UPDATE = "core.app.hibernate.envers.is.event.update";
    public final static String APP_ENVERS_IS_EVENT_DELETE = "core.app.hibernate.envers.is.event.delete";
    public final static String APP_ENVERS_IS_EVENT_COLLECTION_RECREATE = "core.app.hibernate.envers.is.event.collection.recreate";
    public final static String APP_ENVERS_IS_EVENT_COLLECTION_REMOVE = "core.app.hibernate.envers.is.event.collection.remove";
    public final static String APP_ENVERS_IS_EVENT_COLLECTION_UPDATE = "core.app.hibernate.envers.is.event.collection.update";


    public final static String APP_RECOVER_PWD_MODE = "core.app.recover.pwd.mode";
    public static final String INIT_PWD_TOKEN="@NULL@";

    public final static String APP_TESTING_MODE = "core.app.testing.mode";
    public final static String APP_FOR_DEVELOPMENT_ONLY_MODE = "core.app.for.development.only.mode";
    public final static String APP_EMAIL_BACKUP_ADDR = "core.app.email.backup.address";
    public final static String APP_EMAIL_BACKUP_MODE = "core.app.email.backup.mode";
    public final static String APP_EMAIL_TESTING_SUBJECT_PREFIX = "core.app.email.testing.subject.prefix";
    public final static String APP_QUERYSTRING_ENCRYPTION_TOKEN_DEFAUT = "1FFAAZZ0";
    public final static String APP_QUERYSTRING_ENCRYPTION_TOKEN = "core.app.security.querystring.encryption.token";

    public final static String APP_MASTER_KEY_TMP = "core.app.master.key.tmp";
    public final static String APP_SYS_SPADM = "core.app.security.sys.spadm";

    public final static String APP_TMP_FOLDER = "core.app.tmp.folder";
    


    /**
     * Constructor
     * Can not be instantiated
     */
	protected FrmkAppConfigFileHelper() {
	}

    /**
     * 
     * @return
     */
    public static boolean isTestingMode() {
		return getValueBoolean(APP_TESTING_MODE);
    }
    
    /**
     * 
     * @return
     */
    public static boolean isRecoverPwdMode() {
		return getValueBoolean(APP_RECOVER_PWD_MODE);
    }
    
    /**
     * 
     * @return
     */
    public static boolean isForDevelopmentOnlyMode() {
		return getValueBoolean(APP_FOR_DEVELOPMENT_ONLY_MODE);
    }
    
    /**
     * 
     * @return
     */
    public static boolean isEmailBackupMode() {
		return getValueBoolean(APP_EMAIL_BACKUP_MODE);
    }
    
    /**
     * 
     * @return
     */
    public static String getEmailBackupAddress() {
		return AppConfigFile.getInstance().getProperty(APP_EMAIL_BACKUP_ADDR);
    }
    
    /**
     * 
     * @return
     */
    public static String getEmailTestingSubjectPrefix() {
		return AppConfigFile.getInstance().getProperty(APP_EMAIL_TESTING_SUBJECT_PREFIX);
    }
    
	
	/**
	 * 
	 * @return
	 */
	public static String getI18nBundleName() {
		return AppConfigFile.getInstance().getProperty(APP_I18N_BUNDLE_NAME);
    }
	
	/**
	 * 
	 * @return
	 */
	public static List<String> getI18nBundlesName() {
		return AppConfigFile.getInstance().getValues(APP_I18N_BUNDLE_NAME_LIST);
    }
	
	/**
	 * 
	 * @return
	 */
	public static List<String> getI18nLanguages() {
		return AppConfigFile.getInstance().getValues(APP_I18N_LANGUAGE_LIST);
    }
	
	/**
	 * 
	 * @return
	 */
	public static String getI18nEncodingSource() {
		return AppConfigFile.getInstance().getProperty(APP_I18N_ENCODING_SOURCE);
    }
	/**
	 * 
	 * @return
	 */
	public static String getI18nEncodingTarget() {
		return AppConfigFile.getInstance().getProperty(APP_I18N_ENCODING_TARGET);
    }


    /**
     * 
     * @return
     */
    public static String getSysSpAdmMessage() {
		String prop = AppConfigFile.getInstance().getValue(APP_SYS_SPADM);
		if (StringUtils.isEmpty(prop) || prop.trim().length() < 5) {
            String errMsg = "Check the [" + APP_SYS_SPADM + "]";
            throw new IllegalStateException(errMsg);
		}
		return prop;
    }
    
    
    /**
     * 
     * @return
     */
    public static String getTemporaryFolder() {
    	try {
    		return AppConfigFile.getInstance().getProperty(APP_TMP_FOLDER);
    	} catch (Exception e) {
             LoggerFactory.getLogger(SeuksaAppConfigFileHelper.class).error("Error reading property [" + APP_TMP_FOLDER + "] into String.", e);
             String errMsg = "Error reading property [" + APP_TMP_FOLDER + "].";
             throw new IllegalStateException(errMsg, e);
         }
    }
    
 
    /**
     * 
     * @return
     */
    public static boolean isEnversEventInsert() {
		return getValueBoolean(APP_ENVERS_IS_EVENT_INSERT);
    }

    /**
     * 
     * @return
     */
    public static boolean isEnversEventUpdate() {
		return getValueBoolean(APP_ENVERS_IS_EVENT_UPDATE);
    }
    
    /**
     * 
     * @return
     */
    public static boolean isEnversEventDelete() {
		return getValueBoolean(APP_ENVERS_IS_EVENT_DELETE);
    }
    
    /**
     * 
     * @return
     */
    public static boolean isEnversEventCollectionRecreate() {
		return getValueBoolean(APP_ENVERS_IS_EVENT_COLLECTION_RECREATE);
    }
    
    /**
     * 
     * @return
     */
    public static boolean isEnversEventCollectionUpdate() {
		return getValueBoolean(APP_ENVERS_IS_EVENT_COLLECTION_UPDATE);
    }
    
    /**
     * 
     * @return
     */
    public static boolean isEnversEventCollectionRemove() {
		return getValueBoolean(APP_ENVERS_IS_EVENT_COLLECTION_REMOVE);
    }
}
