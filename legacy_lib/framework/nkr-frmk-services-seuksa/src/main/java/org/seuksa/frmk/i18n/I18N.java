package org.seuksa.frmk.i18n;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.seuksa.frmk.tools.vo.ValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.frmk.helper.SeuksaServicesHelper;
import com.nokor.frmk.security.model.SecUser;

/**
 * 
 * @author prasnar
 * 
 */
public class I18N implements SeuksaServicesHelper {
    private static Logger logger = LoggerFactory.getLogger(I18N.class);

    public static String ERROR_UNEXPECTED_EXCEPTION = "error.unexpected.exception";
    public static String ERROR_FIELD_MANDATORY = "error.field.mandatory";
    public static String ERROR_FIELD_EMPTY_REQUIRED = "error.field.empty.required";
    public static String ERROR_PARAMETER_NOT_VALID = "error.parameter.not.valid";
    public static String ERROR_NOT_FOUND = "error.object.not.found";
    public static String ERROR_ALREADY_EXISTS = "error.object.already.exists";
    public static String ERROR_CAN_NOT_BE_DELETE_EXCEPTION = "error.object.can.not.deleted";

    private static I18N instance;
	private static Map<Locale, Map<String, String>> resources;
    private Locale applicationLocale;
    	
    /**
     * 
     */
    private I18N() {
    }
    

    /**
     * 
     * @return
     */
	public static void createInstance(Locale locale) {
		init();
	}
    

	
    /**
     * 
     * @return
     */
	private static void init() {
		if (instance == null) {
			synchronized (I18N.class) {
				if (instance == null) {
					instance = new I18N();
				}
			}
		}
		if (I18N.resources == null) {
			I18N.resources = new HashMap<>();
		}
	}
	
	/**
	 * 
	 */
	public static boolean isInitialized() {
		return instance != null;
	}
    
	/**
     * 
     * @param messages
     */
    public static void addBundle(Locale locale, Map<String, String> messages) {
    	if (instance == null) {
    		createInstance(Locale.getDefault());
    	}
    	I18N.resources.put(locale, messages);
    }

  
	/**
	 * 
	 * @return 
	 */
	public static Locale getLocale() {
		SecUser secUser = UserSessionManager.getCurrentUser();
		if (secUser != null) {
			return SESSION_MNG.getCurrent().getLocale();
		}
		return instance.applicationLocale;
	}



	/**
	 * @param applicationLocale the applicationLocale to set
	 */
	public static void setApplicationLocale(Locale applicationLocale) {
		instance.applicationLocale = applicationLocale;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public static boolean isEnglishLocale() {
		return Locale.ENGLISH.equals(getLocale());
	}

	   /**
     * 
     * @param messages
     */
    public static void initBundle(final ValuePair[] messages) {
    	initBundle(getLocale(), messages);
    }
    
    /**
     * 
     * @param messages
     */
    public static void initBundle(Locale locale, final ValuePair[] messages) {
        HashMap<String, String> bundle = new HashMap<String, String>();
        bundle.clear();
        for (final ValuePair val : messages) {
            bundle.put(val.getCode(), val.getValue());
        }
        addBundle(locale, bundle);
    }

	/**
	 * 
	 * @return
	 */
	protected Map<String, String> getCurrentLocaleMessages() {
		return resources.get(getLocale());
	}

    
    /**
     * @return
     */
    public static char groupingSepator() {
        final String gs = message("grouping_separator");
        return gs.charAt(gs.length() - 1);
    }

    /**
     * 
     * @return
     */
    public static char decimalSepator() {
        final String gs = message("decimal_separator");
        return gs.charAt(gs.length() - 1);
    }
    
    /**
     * 
     * @param code
     * @return
     */
    public static String message(final String code) {
    	return messageWithDefault(code, code);
    }
    
    /**
     * 
     * @param code
     * @return
     */
    public static String message(final String[] codes) {
    	for (String code : codes) {
			String msg = message(code);
			if (!code.equals(msg)) {
				return msg;
			}
			
		}
    	return codes[0];
    }
    
 
    /**
     * 
     * @param field
     * @return
     */
    public static String messageMandatoryField(final String field) {
        return message(ERROR_FIELD_MANDATORY, field);
    }
    
    /**
     * 
     * @param field
     * @return
     */
    public static String messageFieldEmptyRequired(final String field) {
        return message(ERROR_FIELD_EMPTY_REQUIRED, field);
    }

    /**
     * 
     * @param field
     * @return
     */
    public static String messageParameterNotValid(final String param) {
        return message(ERROR_PARAMETER_NOT_VALID, param);
    }

    /**
     * 
     * @param field
     * @return
     */
    public static String messageObjectAlreadyExists(final String param) {
        return message(ERROR_ALREADY_EXISTS, param);
    }
    /**
     * 
     * @param field
     * @return
     */
    public static String messageObjectNotFound(final String param) {
        return message(ERROR_NOT_FOUND, param);
    }
    
    /**
     * 
     * @param param
     * @return
     */
    public static String messageUnexpectedException(final String param) {
        return message(ERROR_UNEXPECTED_EXCEPTION, param);
    }
    
    /**
     * 
     * @param param
     * @return
     */
    public static String messageObjectCanNotDelete(final String param) {
        return message(ERROR_CAN_NOT_BE_DELETE_EXCEPTION, param);
    }
    
    /**
     * 
     * @param code
     * @param defaultValue
     * @return
     */
    public static String messageWithDefault(final String code, final String defaultValue) {
    	try {
	    	// search in the current Local
	        if (instance.getCurrentLocaleMessages().containsKey(code)) {
	            return instance.getCurrentLocaleMessages().get(code);
	        }
	        // if not found search in other Locals
	        else {
	        	Iterator<Locale> itLocales = resources.keySet().iterator();
	        	while (itLocales.hasNext()) {
	        		Locale locale = itLocales.next();
	        		if (!locale.getDisplayName().equals(getLocale().getDisplayName())) {
	        			if (resources.get(locale).containsKey(code)) {
	        				return resources.get(locale).get(code);
	        			}
	        		}
	        	}
	        }
    	} catch (Exception e) {
    		logger.warn("Error in message [" + code + "] - " + e.getMessage());
    	}
        return defaultValue;
    }

    /**
     * 
     * @param key
     * @param vals
     * @return
     */
    public static String message(final String code, final String... vals) {
        String message = message(code);
        try {
            for (int i = 0; i < vals.length; i++) {
                message = message.replaceAll("\\{" + i + "\\}", vals[i]);
            }
        }
        catch (Throwable e) {
        	logger.error(e.getMessage());
        }
        return message;
    }

    /**
     * 
     * @param code
     * @param defaultValue
     * @return
     */
    public static String value(final String code, final String defaultValue) {
    	return message(code, defaultValue);
    }
    
    /**
     * @param code
     * @return
     */
    public static String value(final String code) {
        return message(code);
    }
    /**
     * @param code
     * @return
     */
    public static String val(final String code) {
        return message(code);
    }
    /**
     * @param code
     * @return
     */
    public static String msg(final String code) {
        return message(code);
    }


	/**
	 * @return the resources
	 */
	public static Map<Locale, Map<String, String>> getResources() {
		return resources;
	}


	/**
	 * @param resources the resources to set
	 */
	public static void setResources(Map<Locale, Map<String, String>> resources) {
		I18N.resources = resources;
	}

}
