package com.nokor.efinance.core.shared.conf;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AppConfig {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	/** Config name */
    private static final String CONFIG_NAME = "/efinance-config.xml";
    
    private static AppConfig config = null;
    
    private Configuration configuration = null;
    
    private AppConfig() {
    	try {
			configuration = new XMLConfiguration(AppConfig.class.getResource(CONFIG_NAME));			
		} catch (ConfigurationException e) {
			logger.error("Error while loading configuation file [efinance-config.xml]", e);
			throw new IllegalArgumentException("Error while loading configuation file [efinance-config.xml]", e);
		}
    }
    
    /**
     * Get instance of NAZConfig
     * @return
     */
    public static AppConfig getInstance() {
    	if (config == null) {
    		config = new AppConfig();
    	}
    	return config;
    }
    
    /**
     * Return the current configuration 
     * @return
     */
    public Configuration getConfiguration() {
    	return configuration;
    }
}
