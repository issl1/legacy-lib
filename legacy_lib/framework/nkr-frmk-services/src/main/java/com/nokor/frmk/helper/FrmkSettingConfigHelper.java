package com.nokor.frmk.helper;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * 
 * @author prasnar
 * @version $Revision$
 *
 */
public class FrmkSettingConfigHelper implements FrmkServicesHelper {
	protected static final Logger logger = LoggerFactory.getLogger(SeuksaSettingConfigHelper.class);
    
    public final static String APP_TMP_FOLDER = "app.tmp.folder";

    public final static String APP_REPORT_TEMPLATE_FOLDER = "app.report.template.folder";
    public final static String APP_REPORT_OUTPUT_FOLDER = "app.report.output.folder";

    public final static String APP_MAIL_SMTP = "app.mail.smtp";
    public final static String APP_MAIL_USER = "app.mail.user";
    public final static String APP_MAIL_PASSWORD = "app.mail.password";
    public final static String APP_MAIL_IS_ENABLED = "app.mail.is.enabled";

    


    /**
     * Constructor
     * Can not be instantiated
     */
	protected FrmkSettingConfigHelper() {
	}


    /**
     * 
     * @return
     */
    public static boolean isEmailEnabled() {
		return SETTING_SRV.getValueBoolean(APP_MAIL_IS_ENABLED);
    }
    
    /**
     * 
     * @return
     */
    public static String getMailSMTP() {
        return SETTING_SRV.getValue(APP_MAIL_SMTP);
    }
    
    /**
     * 
     * @return
     */
    public static String getMailUser() {
        return SETTING_SRV.getValue(APP_MAIL_USER);
    }
    
    /**
     * 
     * @return
     */
    public static String getMailPassword() {
        return SETTING_SRV.getValue(APP_MAIL_PASSWORD);
    }


    /**
     * 
     * @return
     */
	public static Locale getLocale(String appCode) {
		Locale locale = null;
		try {
	    	String localeLanguageTag = SETTING_SRV.getValue(appCode);
	    	locale = Locale.forLanguageTag(localeLanguageTag);
		} catch(Exception e) {
			logger.error("Error in getting Locale from the application settings configuration.");
			logger.error("The system default Locale is taken instead.");
			locale = new Locale(Locale.getDefault().getLanguage());
		}
		return locale;
		
	}
	
	


   /**
    * 
    * @return
    */
   public static String getTemporaryFolder() {
  		return SETTING_SRV.getValue(APP_TMP_FOLDER);
   }

 
   /**
    * 
    * @return
    */
	public static String getReportOutputFolder() {
		return SETTING_SRV.getValue(APP_REPORT_OUTPUT_FOLDER);
	}
   
   /**
    * 
    * @return
    */
	public static String getReportTemplateFolder() {
		return SETTING_SRV.getValue(APP_REPORT_TEMPLATE_FOLDER);
	}

}
