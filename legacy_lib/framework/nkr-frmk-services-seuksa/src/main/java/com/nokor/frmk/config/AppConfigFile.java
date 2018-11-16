package com.nokor.frmk.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.tools.MyStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.frmk.helper.SeuksaAppConfigFileHelper;

/**
 * 
 * @author prasnar
 *
 */
public class AppConfigFile extends Properties {
	/** */
	private static final long serialVersionUID = 4817939764467381363L;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final String DEFAULT_APP_GLOBAL_CONFIGURATION_FILE = "app-global-config.properties";
    public static final String DEFAULT_APP_CONFIGURATION_FILE = "app-config.properties";

    public static final String FILE_EXTENSION = ".properties";

    private static String configFile = DEFAULT_APP_CONFIGURATION_FILE;

    
    /** Singleton */
    private static AppConfigFile instance;

    /**
     * Constructor
     * Can not be instantiated
     */
	private AppConfigFile() {
	}

	/**
     * 
     * @param configFile
     */
    public static void initFile(String configFile) {
    	AppConfigFile.configFile = configFile;
    	getInstance();
     }
    
	
	/**
	 * 
	 * @param cfgFile
	 * @return
	 */
	public static AppConfigFile getInstance() {
		if (instance == null) {
			synchronized (AppConfigFile.class) {
				if (instance == null) {
					instance = new AppConfigFile();
					instance.loadConfigFiles();
				}
			}
		}
		return instance;
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public String getValue(String code) {
		return getProperty(code);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public boolean getValueBoolean(String code) {
		try {
			String value = getValue(code);
			if (StringUtils.isEmpty(value)) {
				return false;
			}
			value = value.trim();
			
            return Arrays.asList("true", "1").contains(value);
        } catch (Exception e) {
            String errMsg = "[getValueBoolean] Error for the value [" + code + "] - Check its code/value in the [" + configFile + "] file";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg, e);
        }
	}

	/**
	 * 
	 * @param code
	 * @return
	 */
	public long getValueLong(String code) {
		try {
			String value = getValue(code);
            return  StringUtils.isNotEmpty(value) ? Long.valueOf(value) : 0;
        }
        catch (Exception e) {
            String errMsg = "[ValueLong] Error for the value [" + code + "] - Check its code/value in the [" + configFile + "] file";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg, e);
        }
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public int getValueInt(String code) {
		try {
			String value = getValue(code);
            return  StringUtils.isNotEmpty(value) ? Integer.valueOf(value) : 0;
        }
        catch (Exception e) {
            String errMsg = "[ValueInteger] Error for the value [" + code + "] - Check its code/value in the [" + configFile + "] file";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg, e);
        }
	}
	/**
	 * 
	 * @param code
	 * @return
	 */
	public List<String> getValues(String code) {
		try {
			String propStr = getValue(code);
	        if (StringUtils.isEmpty(propStr)) { 
	            logger.warn("[" + code + "] is not defined in [" + configFile + "] file.");
	            return new ArrayList<String>();
	        }
	
	        String[] values = propStr.replace(" ", "").split(MyStringUtils.LIST_SEPARATOR_COMMA);
			return Arrays.asList(values);
		} catch (Exception e) {
            String errMsg = "[getValues] Error for the value [" + code + "] - Check its code/value in the [" + configFile + "] file";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg, e);
        }
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public List<Integer> getValuesInt(String code) {
		try {
			String propStr = getValue(code);
		    if (StringUtils.isEmpty(propStr)) {
	            logger.warn("[" + code + "] is not defined in [" + configFile + "] file.");
		        return new ArrayList<Integer>();
		    }
		
		    return MyStringUtils.getValuesInt(propStr);
		} catch (Exception e) {
            String errMsg = "[getValuesInt] Error for the value [" + code + "] - Check its code/value in the [" + configFile + "] file";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg, e);
        }
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public List<Long> getValuesLong(String code) {
		try {
			String propStr = getValue(code);
	        if (StringUtils.isEmpty(propStr)) {
	            logger.warn("[" + code + "] is not defined in [" + configFile + "] file.");
	            return new ArrayList<Long>();
	        }
			return MyStringUtils.getValuesLong(propStr);
		} catch (Exception e) {
            String errMsg = "[getValuesLong] Error for the value [" + code + "] - Check its code/value in the [" + configFile + "] file";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg, e);
        }
	}
	
	/**
	 * @return the configFile
	 */
	public static String getConfigFile() {
		return configFile;
	}

	/**
	 * @param cfgFile the configFile to set
	 */
	public  static void setConfigFile(String cfgFile) {
		configFile = cfgFile;
	}

	/**
	 * 
	 */
    private void loadConfigFiles() {
    	
    	// Search GLOBAL CONFIG FILES - in Core
    	boolean foundAppCfg = loadConfigFile(DEFAULT_APP_GLOBAL_CONFIGURATION_FILE, false);
    	logger.info("Config file[" + DEFAULT_APP_GLOBAL_CONFIGURATION_FILE + "] Found: " + foundAppCfg);
    	
    	List<String> otherAppGlobalCfgFiles = SeuksaAppConfigFileHelper.getOtherAppGlobalConfigFiles();
    	loadConfigFiles(otherAppGlobalCfgFiles);
    	
    	
    	// Search APP CONFIG FILES - in FO, BO, RA, CMS.. 
    	foundAppCfg = loadConfigFile(DEFAULT_APP_CONFIGURATION_FILE, !foundAppCfg);
    	logger.info("Config file[" + DEFAULT_APP_CONFIGURATION_FILE + "] Found: " + foundAppCfg);
    	
    	List<String> otherAppCfgFiles = SeuksaAppConfigFileHelper.getOtherAppConfigFiles();
    	loadConfigFiles(otherAppCfgFiles);
    	
    	
    }
    
    /**
     * 
     * @param appCfgFiles
     */
    private void loadConfigFiles(List<String> appCfgFiles) {
    	if (appCfgFiles != null) {
    		boolean foundAppCfg = false;
    		for (String appCfgFile : appCfgFiles) {
    			if (!appCfgFile.endsWith(FILE_EXTENSION)) {
    				appCfgFile = appCfgFile + FILE_EXTENSION;
    			}
    			if (!appCfgFile.equalsIgnoreCase(DEFAULT_APP_GLOBAL_CONFIGURATION_FILE)
    					&& !appCfgFile.equalsIgnoreCase(DEFAULT_APP_CONFIGURATION_FILE)) {
    				foundAppCfg = loadConfigFile(appCfgFile, true);
    		    	logger.info("Config file[" + appCfgFile + "] Found: " + foundAppCfg);
    			}
    				
    		}
    	}
    	
    }
	/**
     * Load the configuration properties file
     * @return Properties
     */
    private boolean loadConfigFile(String cfgFile, boolean notFoundThrowError) {
    	logger.info("Loading the configuration file [" + cfgFile + "]");
    	
    	logger.debug("Searching in the application classpath.." + cfgFile);
        InputStream in = ClassLoader.getSystemResourceAsStream(cfgFile);
        if (in == null) {
        	logger.debug("Searching in the system classpath.." + cfgFile);
            in = ClassLoader.getSystemResourceAsStream("/" + cfgFile);
        }
        if (in == null) {
        	logger.debug("Searching in the system classpath.." + cfgFile);
            in = ClassLoader.getSystemClassLoader().getResourceAsStream("/" + cfgFile);
        }
        if (in == null) {
        	logger.debug("Searching in the system classpath.." + cfgFile);
            in = AppConfigFile.class.getClassLoader().getResourceAsStream(cfgFile); 
        }
        
        if (in == null) {
            String errMsg = "Can't find " + cfgFile + " in the classpath, check your Configuration";
            logger.error(errMsg);
            if (notFoundThrowError) {
            	throw new IllegalStateException("Can't find [" + cfgFile + "]");
            }
            return false;
        }
        try {
        	instance.load(in);
		} catch (Exception e) {
			 String errMsg = "Error while loading [" + cfgFile + "]";
			logger.error(errMsg);
			throw new IllegalStateException("Can't load [" + cfgFile + "]");
		}
        try {
			in.close();
		} catch (IOException e) {
		    logger.warn(e.getMessage());
            // do nothing
		    
		}
		Enumeration eKeys = instance.keys();
		while(eKeys.hasMoreElements()) {
			String key = (String) eKeys.nextElement();
			String value = (String) instance.get(key);

			logger.debug("Configuration: [" +  key + "] = [" + value + "]");
		}
		return true;
    }
    
 }
