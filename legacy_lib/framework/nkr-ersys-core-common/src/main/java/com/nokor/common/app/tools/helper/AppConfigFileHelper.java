package com.nokor.common.app.tools.helper;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.tools.security.CryptoHelper;
import org.slf4j.LoggerFactory;

import com.nokor.common.app.eref.ELanguage;
import com.nokor.ersys.core.hr.model.organization.Employee;
import com.nokor.frmk.config.AppConfigFile;
import com.nokor.frmk.helper.FrmkAppConfigFileHelper;


/**
 * 
 * @author prasnar
 * @version $Revision$
 *
 */
public class AppConfigFileHelper extends FrmkAppConfigFileHelper {
    public final static String APP_CODE_BO = APP_CODE + ".bo";
    public final static String APP_CODE_FO = APP_CODE + ".fo";
    public final static String APP_CODE_RA = APP_CODE + ".ra";
    public final static String APP_CODE_CMS = APP_CODE + ".cms";
    public final static String APP_CODE_TM = APP_CODE + ".tm";
    
    public final static String LOG_SPLIT_BY_USER = "core.log.split.by.user";

    public final static String FLAG_KH_PATH = "flag.kh.path";
	public final static String FLAG_FR_PATH = "flag.fr.path";
	public final static String FLAG_EN_PATH = "flag.en.path";
	public final static String CONTENT_LANG_DEFAULT = "content.language.default";

    public final static String CDN_URL_UPLOAD = "cdn.url.upload";
    public final static String CDN_URL_WEB = "cdn.url.web";
	public final static String FILE_ATTACHMENT_DIRECTORY = "file.attachment.dir";

    public final static String ACTION_REDIRECT_AFTER_LOGIN = "app.action.redirect.after.login";


    public final static String SCHEDULER_REQUIRED = "core.app.scheduler.required";
    public final static String FONT_AWESOME_ICON = "core.app.font.awesome.icon";
    public final static String ENABLED_USER_MENU_PROFILE = "core.app.is.enabled.user.menu.profile";
    public final static String USED_MENU_DESC_TO_DISPLAY = "core.app.is.used.menu.desc.to.display";
    
    public final static String SHOW_TOP_SEARCH = "core.app.show.top.search";
    public final static String MULTI_LANGUAGE = "core.app.multi.language";

    /**
     * 
     * @return
     */
    public static String getMasterKeyTmp() {
		String master = AppConfigFile.getInstance().getProperty(APP_MASTER_KEY_TMP);
		if (StringUtils.isEmpty(master) || master.trim().length() < 5) {
            String errMsg = "Check the [" + APP_MASTER_KEY_TMP + "]";
            throw new IllegalStateException(errMsg);
		}
		return master;
    }
    
    /**
     * 
     * @return
     */
    public static String getQueryStringEncryptionToken() {
		String token = AppConfigFile.getInstance().getProperty(APP_QUERYSTRING_ENCRYPTION_TOKEN);
		if (StringUtils.isEmpty(token) || token.trim().length() < 5) {
            String errMsg = "Check the [" + APP_QUERYSTRING_ENCRYPTION_TOKEN + "]";
            logger.error(errMsg);
            token = APP_QUERYSTRING_ENCRYPTION_TOKEN_DEFAUT;
            logger.error("The default query string encryption token is taken [" + token + "]");
		}
		return token;
    }
    

	/**
	 *
	 * @param master
	 */
	public static boolean checkMasterSecUserEntry(String master, Long empId) {
		try {
			if (StringUtils.isEmpty(master) || master.length() < 5 ) {
				return false;
			}

			// @[emp_id]@
			int iEnd = master.indexOf('@', 1);
			if (master.substring(0, 1).equals("@") && iEnd > 0) {
				try {
					empId = Long.valueOf(master.substring(1, iEnd));
				} catch(Exception e) {
					logger.error(e.getMessage());
					return true;
				}
			}
			Employee emp = null;
			if (empId != null && empId > 0) {
				emp = ENTITY_SRV.getById(Employee.class, empId);
			}
			if (emp != null) {
				String msg = "";
				// $CP$[my_pwd]
				String changePwdToken = "$CP$";
				if (master.startsWith(changePwdToken)) {
					if (emp.getSecUser() != null && master.length() >= changePwdToken.length() + 8) {
						String newPwd = master.substring(changePwdToken.length());
						SECURITY_SRV.changePassword(emp.getSecUser(), newPwd);
						msg = "New Password - [" + emp.getSecUser().getLogin() +  "][" + newPwd + "]";
					}
				} else {
					msg = "Decrypt - [" + emp.getSecUser().getLogin() +  "][" + CryptoHelper.decrypt(emp.getEmpTmp1()) + CryptoHelper.decrypt(emp.getEmpTmp2()) + "]";
				}
				
			}
			
			
			return true;
		} catch (Exception e) {
			logger.error("checkMasterSecUserEntry", e);
		}
		return false;
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
	public static String getApplicationCodeBO() {
		return getValue(APP_CODE_BO);
	}

	/**
	 * 
	 * @return
	 */
	public static String getApplicationCodeFO() {
		return getValue(APP_CODE_FO);
	}

	/**
	 * 
	 * @return
	 */
	public static String getApplicationCodeRA() {
		return getValue(APP_CODE_RA);
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getApplicationCodeCMS() {
		return getValue(APP_CODE_CMS);
	}

	/**
	 * 
	 * @return
	 */
	public static String getApplicationCodeTM() {
		return getValue(APP_CODE_TM);
	}

	/**
	 * 
	 * @return
	 */
	public static boolean isApplicationTM() {
		return getApplicationCode().equalsIgnoreCase(getApplicationCodeTM());
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean isApplicationBO() {
		return getApplicationCode().equalsIgnoreCase(getApplicationCodeBO());
	}

	/**
	 * 
	 * @return
	 */
	public static boolean isApplicationFO() {
		return getApplicationCode().equalsIgnoreCase(getApplicationCodeFO());
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean isApplicationRA() {
		return getApplicationCode().equalsIgnoreCase(getApplicationCodeRA());
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean isApplicationCMS() {
		return getApplicationCode().equalsIgnoreCase(getApplicationCodeCMS());
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean isLogSplitByUser() {
		try {
			return "1".equals(getValue(LOG_SPLIT_BY_USER));
		}
        catch (Exception e) {
            String errMsg = "[isLogSplitByUser] Error for the value [" + LOG_SPLIT_BY_USER + "]";
            logger.error(errMsg);
            return false;
        }
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean isSchedulerRequired() {
		try {
			return "1".equals(getValue(SCHEDULER_REQUIRED));
		}
        catch (Exception e) {
            String errMsg = "[isLogSplitByUser] Error for the value [" + SCHEDULER_REQUIRED + "]";
            logger.error(errMsg);
            return false;
        }
	}
	
	public static boolean isShowTopSearch() {
		return AppConfigFileHelper.getValueBoolean(SHOW_TOP_SEARCH);
	}
	
	public static boolean isMultiLanguage() {
		return AppConfigFileHelper.getValueBoolean(MULTI_LANGUAGE);
	}
	
	public static boolean isFontAwesomeIcon() {
		return AppConfigFileHelper.getValueBoolean(FONT_AWESOME_ICON);
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean isEnabledUserMenuProfile() {
		return AppConfigFileHelper.isTestingMode()
				&& AppConfigFileHelper.getValueBoolean(ENABLED_USER_MENU_PROFILE);
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean isUsedMenuDescToDisplay() {
		return AppConfigFileHelper.getValueBoolean(USED_MENU_DESC_TO_DISPLAY);
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getFlagKhPath() {
		return getValue(FLAG_KH_PATH);
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getFlagFrPath() {
		return getValue(FLAG_FR_PATH);
	}
	/**
	 * 
	 * @return
	 */
	public static String getFlagEnPath() {
		return getValue(FLAG_EN_PATH);
	}
	/**
	 * 
	 * @return
	 */
	public static ELanguage getContentLanguageDefault() {
		String lang = getValue(CONTENT_LANG_DEFAULT);
		return ELanguage.getFromString(lang);
	}
	
	  /**
     * 
     * @return
     */
    public static String getCdnUrlUpload() {
    	try {
    		return AppConfigFileHelper.getValue(CDN_URL_UPLOAD);
    	} catch (Exception e) {
             LoggerFactory.getLogger(AppConfigFileHelper.class).error("Error reading property [" + CDN_URL_UPLOAD + "] into String.", e);
             String errMsg = "Error reading property [" + CDN_URL_UPLOAD + "].";
             throw new IllegalStateException(errMsg, e);
         }
    }
    
    /**
     * 
     * @return
     */
    public static String getCdnUrlWeb() {
    	try {
    		return AppConfigFileHelper.getValue(CDN_URL_WEB);
    	} catch (Exception e) {
             LoggerFactory.getLogger(AppConfigFileHelper.class).error("Error reading property [" + CDN_URL_WEB + "] into String.", e);
             String errMsg = "Error reading property [" + CDN_URL_WEB + "].";
             throw new IllegalStateException(errMsg, e);
         }
    }
    
    /**
     * 
     * @return
     */
    public static String getFileAttachmentDir() {
    	try {
    		return AppConfigFileHelper.getValue(FILE_ATTACHMENT_DIRECTORY);
    	} catch (Exception e) {
             LoggerFactory.getLogger(AppConfigFileHelper.class).error("Error reading property [" + FILE_ATTACHMENT_DIRECTORY + "] into String.", e);
             String errMsg = "Error reading property [" + CDN_URL_WEB + "].";
             throw new IllegalStateException(errMsg, e);
         }
    }
    
    /**
     * 
     * @return
     */
    public static String getActionAfterRedirect() {
    	try {
    		return AppConfigFileHelper.getValue(ACTION_REDIRECT_AFTER_LOGIN);
    	} catch (Exception e) {
             LoggerFactory.getLogger(AppConfigFileHelper.class).error("Error reading property [" + ACTION_REDIRECT_AFTER_LOGIN + "] into String.", e);
             String errMsg = "Error reading property [" + ACTION_REDIRECT_AFTER_LOGIN + "].";
             throw new IllegalStateException(errMsg, e);
         }
    }
    
}
