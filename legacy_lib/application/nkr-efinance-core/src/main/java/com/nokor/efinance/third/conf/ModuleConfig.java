package com.nokor.efinance.third.conf;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ModuleConfig {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	/** Config name */
    private static final String CONFIG_NAME = "/efinance-module-config.xml";
    
    private static ModuleConfig config = null;
    
    private Configuration configuration = null;
    
    private ModuleConfig() {
    	try {
			configuration = new XMLConfiguration(ModuleConfig.class.getResource(CONFIG_NAME));			
		} catch (ConfigurationException e) {
			logger.error("Error while loading configuation file [efinance-module-config.xml]", e);
			throw new IllegalArgumentException("Error while loading configuation file [efinance-module-config.xml]", e);
		}
    }
    
    /**
     * Get instance of NAZConfig
     * @return
     */
    public static ModuleConfig getInstance() {
    	if (config == null) {
    		config = new ModuleConfig();
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
