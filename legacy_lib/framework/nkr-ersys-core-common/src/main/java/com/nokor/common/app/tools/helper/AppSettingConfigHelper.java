package com.nokor.common.app.tools.helper;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import com.nokor.ersys.core.hr.model.organization.BaseOrganization;
import com.nokor.frmk.helper.FrmkSettingConfigHelper;



/**
 * 
 * @author prasnar
 * @version $Revision$
 *
 */
public class AppSettingConfigHelper extends FrmkSettingConfigHelper {
	private static BaseOrganization mainCompany;
	
    public final static String APP_MAIN_COM_CLASS = "app.main.com.class";
    public final static String APP_MAIN_COM_ID = "app.main.com.id";

    public final static String APP_MAIN_LOCALE = "app.main.locale";
    public final static String APP_BO_LOCALE = "app.bo.locale";
    public final static String APP_FO_LOCALE = "app.fo.locale";
    public final static String APP_RA_LOCALE = "app.ra.locale";
    public final static String APP_CMS_LOCALE = "app.cms.locale";
    
    public final static int APP_LIST_NB_RECORDS_PER_PAGE_DEFAULT_VALUE = 20;
    public final static String APP_MAIN_LIST_NB_RECORDS_PER_PAGE = "app.main.list.nb.records.per.page";
    public final static String APP_BO_LIST_NB_RECORDS_PER_PAGE = "app.bo.list.nb.records.per.page";
    public final static String APP_FO_LIST_NB_RECORDS_PER_PAGE = "app.fo.list.nb.records.per.page";
    public final static String APP_RA_LIST_NB_RECORDS_PER_PAGE = "app.ra.list.nb.records.per.page";
    public final static String APP_CMS_LIST_NB_RECORDS_PER_PAGE = "app.cms.list.nb.records.per.page";

    public final static String APP_VAADIN_CUSTOM_LAYOUTS_FOLDER = "app.vaadin.custom.layout.folder";
    
	/**
	 * 
	 * @return
	 */
	public static <T extends BaseOrganization> T getMainOrganization() {
		if (mainCompany == null) {
			mainCompany = ENTITY_SRV.getById(getAppMainOrganizationClass(), getAppMainCompanyId());
		}
		return (T) mainCompany;
	}
	
	/**
     * 
     * @return
     */
    public static <T extends BaseOrganization> Class<T> getAppMainOrganizationClass() {
   		String comClazzName = SETTING_SRV.getValue(APP_MAIN_COM_CLASS);
   		if (StringUtils.isEmpty(comClazzName)) {
   			throw new IllegalStateException( "Can not get class name from [" + APP_MAIN_COM_CLASS + "]");
   		}
   		try {
     		return (Class<T>) Class.forName(comClazzName);
    	} catch (Exception e) {
    		String errMsg = "Can not get class [" + comClazzName + "]";
    	    logger.error(errMsg, e);
    	    throw new IllegalStateException(errMsg, e);
    	}
  	
    }
	
    /**
     * 
     * @return
     */
    public static Long getAppMainCompanyId() {
   		return SETTING_SRV.getValueLong(APP_MAIN_COM_ID);
    }
    

    /**
     * 
     * @return
     */
	public static Locale getLocale() {
		if (AppConfigFileHelper.isApplicationBO()) {
			return getLocaleBO();
		}
		if (AppConfigFileHelper.isApplicationFO()) {
			return getLocaleFO();
		}
		if (AppConfigFileHelper.isApplicationRA()) {
			return getLocaleRA();
		}
		if (AppConfigFileHelper.isApplicationCMS()) {
			return getLocaleCMS();
		}
		return getLocale(APP_MAIN_LOCALE); 
	}
	
	/**
     * 
     * @return
     */
	public static Locale getLocaleFO() {
		return getLocale(APP_FO_LOCALE); 
	}
	/**
     * 
     * @return
     */
	public static Locale getLocaleBO() {
		return getLocale(APP_BO_LOCALE); 
	}
	
	/**
     * 
     * @return
     */
	public static Locale getLocaleRA() {
		return getLocale(APP_RA_LOCALE); 
	}
	
	/**
     * 
     * @return
     */
	public static Locale getLocaleCMS() {
		return getLocale(APP_CMS_LOCALE); 
	}
	
	/**
	 * 
	 * @return
	 */
	public static int getNbRecordsPerPageInList() {
		int nbRecordsPerPage;
        if (AppConfigFileHelper.isApplicationBO()) {
        	nbRecordsPerPage = getNbRecordsPerPageInListBO();
        } else if (AppConfigFileHelper.isApplicationFO()) {
        	nbRecordsPerPage = getNbRecordsPerPageInListFO();
        } else if (AppConfigFileHelper.isApplicationRA()) {
        	nbRecordsPerPage = getNbRecordsPerPageInListRA();
        } else if (AppConfigFileHelper.isApplicationCMS()) {
        	nbRecordsPerPage = getNbRecordsPerPageInListCMS();
        } else {
        	nbRecordsPerPage = getNbRecordsPerPageInListMAIN();
        	
        } 
        if (nbRecordsPerPage <= 0) {
            nbRecordsPerPage = getNbRecordsPerPageInListDefault();
        }
        
        return nbRecordsPerPage;
    }
	  
    /**
     * 
     * @return
     */
    public static int getNbRecordsPerPageInListDefault() {
    	return APP_LIST_NB_RECORDS_PER_PAGE_DEFAULT_VALUE;
    }
   
    /**
     * 
     * @return
     */
    public static int getNbRecordsPerPageInListMAIN() {
    	try {
    		return SETTING_SRV.getValueInt(APP_MAIN_LIST_NB_RECORDS_PER_PAGE);
    	} catch (Exception e) {
    		logger.error("Error while getting [getNbRecordsPerPageInListMAIN]");
    		logger.error("Return default value [" + APP_LIST_NB_RECORDS_PER_PAGE_DEFAULT_VALUE + "]");
    		return APP_LIST_NB_RECORDS_PER_PAGE_DEFAULT_VALUE;
    	}
    }
    /**
     * 
     * @return
     */
    public static int getNbRecordsPerPageInListBO() {
    	try {
    		return SETTING_SRV.getValueInt(APP_BO_LIST_NB_RECORDS_PER_PAGE);
    	} catch (Exception e) {
    		logger.error("Error while getting [getNbRecordsPerPageInListBO]");
    		logger.error("Return default value [" + APP_LIST_NB_RECORDS_PER_PAGE_DEFAULT_VALUE + "]");
    		return APP_LIST_NB_RECORDS_PER_PAGE_DEFAULT_VALUE;
    	}
    }
    
    /**
     * 
     * @return
     */
    public static int getNbRecordsPerPageInListFO() {
    	try {
    		return SETTING_SRV.getValueInt(APP_FO_LIST_NB_RECORDS_PER_PAGE);
    	} catch (Exception e) {
    		logger.error("Error while getting [getNbRecordsPerPageInListFO]");
    		logger.error("Return default value [" + APP_LIST_NB_RECORDS_PER_PAGE_DEFAULT_VALUE + "]");
    		return APP_LIST_NB_RECORDS_PER_PAGE_DEFAULT_VALUE;
    	}
    }
    
    /**
     * 
     * @return
     */
    public static int getNbRecordsPerPageInListRA() {
    	try {
    		return SETTING_SRV.getValueInt(APP_RA_LIST_NB_RECORDS_PER_PAGE);
    	} catch (Exception e) {
    		logger.error("Error while getting [getNbRecordsPerPageInListRA]");
    		logger.error("Return default value [" + APP_LIST_NB_RECORDS_PER_PAGE_DEFAULT_VALUE + "]");
    		return APP_LIST_NB_RECORDS_PER_PAGE_DEFAULT_VALUE;
    	}
    }
    
    /**
     * 
     * @return
     */
    public static int getNbRecordsPerPageInListCMS() {
    	try {
    		return SETTING_SRV.getValueInt(APP_CMS_LIST_NB_RECORDS_PER_PAGE);
    	} catch (Exception e) {
    		logger.error("Error while getting [getNbRecordsPerPageInListCMS]");
    		logger.error("Return default value [" + APP_LIST_NB_RECORDS_PER_PAGE_DEFAULT_VALUE + "]");
    		return APP_LIST_NB_RECORDS_PER_PAGE_DEFAULT_VALUE;
    	}
    }
    
    
    /**
	 * 
	 * @return
	 */
	public static String getVaadinCustomLayoutsFolder() {
		return SETTING_SRV.getValue(APP_VAADIN_CUSTOM_LAYOUTS_FOLDER);
	}
   
    
}
