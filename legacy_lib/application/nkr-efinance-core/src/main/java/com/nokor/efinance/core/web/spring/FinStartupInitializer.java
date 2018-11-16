package com.nokor.efinance.core.web.spring;

import javax.servlet.ServletContextEvent;

import com.nokor.common.app.web.spring.StartupApplicationContextListener;
import com.nokor.efinance.core.helper.FinSettingConfigHelper;
import com.nokor.efinance.core.shared.quotation.SequenceManager;

/**
 * 
 * @author prasnar
 *
 */
public class FinStartupInitializer extends StartupApplicationContextListener {
	private static final String APP_NAME = "EFinance System";
    /**
     * @see org.springframework.web.context.ContextLoaderListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextInitialized(final ServletContextEvent event) {
        logger.info(APP_NAME + " - Context - Initializing...");

        super.contextInitialized(event);
        	
        try {
        	logger.info("---------------------- Startup : >> Initialized data reference ------------------------");
    		
    		logger.info("---------------------- Startup : << Initialized data reference ------------------------");
    		
    		logger.info("---------------------- Startup : >> Initialized sequence number ------------------------");
    		
    		SequenceManager.getInstance().setQuotationReferenceNumber(FinSettingConfigHelper.getQuotationReferenceNumber());
    		
    		logger.info("---------------------- Sequence number = " + SequenceManager.getInstance().getQuotationReferenceNumber());
    		
    		logger.info("---------------------- Startup : << Initialized sequence number ------------------------");

        }
        catch (Exception e) {
        	String errMsg = APP_NAME + " - Error while context initializing.";
            logger.error(errMsg, e);
        }
        	
        logger.info(APP_NAME + " - Context successfully initialized.");
    }

    /**
     * @see org.springframework.web.context.ContextLoaderListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextDestroyed(final ServletContextEvent event) {
        super.contextDestroyed(event);
        try {

        }
        catch (Exception e) {
        	String errMsg = APP_NAME + " - Error while context destroying.";
            logger.error(errMsg, e);
        }
		
    }
}
