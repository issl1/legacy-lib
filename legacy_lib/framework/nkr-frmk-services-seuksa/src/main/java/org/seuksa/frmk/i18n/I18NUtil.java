package org.seuksa.frmk.i18n;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.seuksa.frmk.tools.vo.ValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author prasnar
 *
 */
public class I18NUtil {
    private static Logger logger = LoggerFactory.getLogger(I18NUtil.class);

    /**
     * 
     */
    public static void printAll() {
    	Map<Locale, Map<String, String>> resources = I18N.getResources();
    	Iterator<Locale> itLoc = resources.keySet().iterator();
    	while (itLoc.hasNext()) {
    		Locale loc = itLoc.next();
    		logger.info("*****Locale: [" +  loc.toLanguageTag() + "]");
    		Map<String, String> props = resources.get(loc);
    		Iterator<String> keys = props.keySet().iterator();
    		while (keys.hasNext()) {
    			String key = keys.next();
        		logger.info("  -[" +  key + "] " + "[" + props.get(key) + "]");
    		}
    	}
    }
    
    /**
     * 
     * @param propertyFile
     * @param encodingSource
     * @param encodingTarget
     */
    public static void init(String propertyFile, String encodingSource, String encodingTarget) {
    	init(propertyFile, Locale.getDefault(), encodingSource, encodingTarget);
    }

    /**
     * 
     * @param propertyFiles
     * @param localeLanguages
     * @param encodingSource
     * @param encodingTarget
     */
    public static void init(List<String> propertyFiles, List<String> localeLanguages, String encodingSource, String encodingTarget) {
    	for (String lang : localeLanguages) {
    		Locale locale = new Locale(lang);
    		init(propertyFiles, localeLanguages, locale, encodingSource, encodingTarget);
    	}
    }
    
    /**
     * 
     * @param propertyFiles
     * @param localeLanguages
     * @return
     */
    private static List<String> getDefaultPropertyFiles(List<String> propertyFiles, List<String> localeLanguages) {
    	List<String> defaultPropertyFiles = new ArrayList<>();
    	for (String propertyFile : propertyFiles) {
    		boolean isDefault = true;
    		for (String lang : localeLanguages) {
        		if (propertyFile.contains("_" + lang)) {
        			isDefault = false;
        		}
        	}
    		if (isDefault) {
    			defaultPropertyFiles.add(propertyFile);
    		}
		}
    	return defaultPropertyFiles;
    }
    
    /**
     * 
     * @param propertyFiles
     * @param lang
     * @return
     */
    private static List<String> getLocalPropertyFiles(List<String> propertyFiles, String lang) {
    	List<String> localPropertyFiles = new ArrayList<>();
    	for (String propertyFile : propertyFiles) {
    		if (propertyFile.contains("_" + lang)) {
    			localPropertyFiles.add(propertyFile);
        	}
		}
    	return localPropertyFiles;
    }
    
    /**
     * 
     * @param propertyFiles
     * @param locale
     * @param encodingSource
     * @param encodingTarget
     */
    public static void init(List<String> propertyFiles, List<String> localeLanguages, Locale locale, String encodingSource, String encodingTarget) {
    	HashMap<String, String> msgAll = new HashMap<>();
    	// Load default property file
    	List<String> defaultPropertyFiles = getDefaultPropertyFiles(propertyFiles, localeLanguages);
    	for (String propertyFile : defaultPropertyFiles) {
			HashMap<String, String> messages = buildMessagesBundle(propertyFile, locale, encodingSource, encodingTarget);
        	if (messages != null) {
        		msgAll.putAll(messages);
        	}
    		logger.info("*****I18N File [" + propertyFile 
    				+ "] - Locale [" +  locale.toLanguageTag() 
    				+ "] [" +  locale.toLanguageTag() + "] - Nb messages total: " + (messages != null ? messages.size() : 0));
    	}
    	List<String> localPropertyFiles = getLocalPropertyFiles(propertyFiles, locale.getLanguage());
    	for (String propertyFile : localPropertyFiles) {
			HashMap<String, String> messages = buildMessagesBundle(propertyFile, locale, encodingSource, encodingTarget);
        	if (messages != null) {
        		msgAll.putAll(messages);
        	}
    		logger.info("*****I18N File [" + propertyFile 
    				+ "] - Locale [" +  locale.toLanguageTag() 
    				+ "] [" +  locale.toLanguageTag() + "] - Nb messages total: " + (messages != null ? messages.size() : 0));
    	}
    	I18N.addBundle(locale, msgAll);
    }

    /**
     * 
     * @param propertyFile
     * @param locale
     * @param encodingSource
     * @param encodingTarget
     */
    public static void init(String propertyFile, Locale locale, String encodingSource, String encodingTarget) {
    	HashMap<String, String> messages =buildMessagesBundle(propertyFile, locale, encodingSource, encodingTarget);
    	I18N.addBundle(locale, messages);
    }
    	
    /**
     * 
     * @param propertyFile
     * @param locale
     * @param encodingSource
     * @param encodingTarget
     */
    public static HashMap<String, String> buildMessagesBundle(String propertyFile, Locale locale, String encodingSource, String encodingTarget) {
    	HashMap<String, String> messages = new HashMap<>();
    	ResourceBundle rb = getResourceBundle(propertyFile, locale, encodingSource, encodingTarget);
        if (rb == null) {
        	return messages;
        }
        for (String key : rb.keySet()) {
        	if (!messages.containsKey(key)) {
	            ValuePair msg = new ValuePair();
	            msg.setCode(key);
	            try {
	                msg.setValue(new String(rb.getString(key).getBytes(encodingSource), encodingTarget));
	            }
	            catch (Exception e) {
	                logger.error(e.getMessage());
	                msg.setValue(key);
	            }
	            messages.put(msg.getCode(), msg.getValue());
        	}
	     }
        return messages;
    }
    	
    /**
     * 
     * @param propertyFile
     * @param locale
     * @param encodingSource
     * @param encodingTarget
     * @return
     */
    private static ResourceBundle getResourceBundle(String propertyFile, Locale locale, String encodingSource, String encodingTarget) {
        logger.info("Init I18n: Resource file [" + propertyFile + "]");
        if (locale == null) {
            logger.info("Locale is null, then the Locale.getDefault() [" + Locale.getDefault().toLanguageTag() + "]");
        	locale = Locale.getDefault();
        }
        LoggerFactory.getLogger(I18N.class).info(". Locale [" + locale.toLanguageTag() + "]");
        LoggerFactory.getLogger(I18N.class).info(". Encoding Source [" + encodingSource + "]");
        LoggerFactory.getLogger(I18N.class).info(". Encoding Target [" + encodingTarget + "]");

        ResourceBundle rb = null;
        try {
            logger.info("Init I18n: Trying with [" + locale.toLanguageTag() + "]");
        	rb = ResourceBundle.getBundle(propertyFile, locale);
        } catch (MissingResourceException e1) {
        	logger.error("- 1st try: " + e1.getMessage());
            logger.info("Init I18n: Trying with default locale [" + Locale.getDefault().toLanguageTag() + "]");
            try {
            	rb = ResourceBundle.getBundle(propertyFile);
            } catch (MissingResourceException e2) {
            	logger.error("- 2nd try: " + e2.getMessage());
            	logger.error(e2.getMessage());
            }
        }
        
        return rb;
    }
}
