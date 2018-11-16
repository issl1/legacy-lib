package com.nokor.frmk.testing;

import junit.framework.TestCase;

import org.seuksa.frmk.service.BaseEntityService;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * @author prasnar
 * @version $Revision$
 */
public class AbstractTestCase extends TestCase {

    private static String MAIN_APP_CONTEXT_FILE = "application-main-context.xml";

    protected static Logger logger = LoggerFactory.getLogger(AbstractTestCase.class);
    
    private String mainAppConfigFile;

    /**
     * 
     * @param mainAppConfigFile
     */
    public AbstractTestCase() {
    	super(MAIN_APP_CONTEXT_FILE);
    }
    
    /**
     * 
     * @param mainAppConfigFile
     */
    public AbstractTestCase(String mainAppConfigFile) {
    	initContext(mainAppConfigFile);
    }
    
    /**
     * 
     * @param mainAppConfigFile
     */
    public void initContext(String mainAppConfigFile) {
    	try {
    		this.mainAppConfigFile = mainAppConfigFile;
            SpringUtils.initAppContext(mainAppConfigFile);
            
        } catch (Exception e) {
            String errMsg = "*******Fatal error occured initializing application. Exiting.!!!*******";
            System.err.println(errMsg);
            logger.error(errMsg);
            logger.error(e.getMessage());
            SpringUtils.shutdownHsql();
            System.exit(-1);
        }
        
    }
    

    

    /**
	 * @return the mainAppConfigFile
	 */
	public String getMainAppConfigFile() {
		return mainAppConfigFile;
	}



	/**
     * @return the appContext
     */
    public static ApplicationContext getAppContext() {
        return SpringUtils.getAppContext();
    }
    
    /**
     * 
     * @param serviceClass
     * @return
     */
    protected static <T extends BaseEntityService> T getService(Class<T> serviceClass) {
    	return SpringUtils.getService(serviceClass);
    }

    /**
     * 
     * @param beanClass
     * @return
     */
    protected static <T> T getBean(Class<T> beanClass) {
    	return SpringUtils.getBean(beanClass);
    }

   
  

}
