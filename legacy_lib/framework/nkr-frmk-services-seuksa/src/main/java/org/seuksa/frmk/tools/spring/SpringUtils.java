package org.seuksa.frmk.tools.spring;

import javax.servlet.ServletContext;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.seuksa.frmk.service.BaseEntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author prasnar
 * @version $revision$
 */
public class SpringUtils {
    private static Logger logger = LoggerFactory.getLogger(SpringUtils.class);

    private static String MAIN_APP_CONTEXT_FILE = "application-main-context.xml";

    private static String mainAppContextFile = MAIN_APP_CONTEXT_FILE;

    private static SessionFactory sessionFactory = null;
    private static ApplicationContext appContext = null;

    /**
     * 
     * @return
     */
    public static boolean isInitialized() {
        return sessionFactory != null;
    }
    
	/**
	 * 
	 * @return
	 */
	public static Boolean isAuthenticated() {
		return SecurityContextHolder.getContext().getAuthentication() != null
				&& SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null;
	}
	

    /**
	 * @return the mainAppContextFile
	 */
	public static String getMainAppContextFile() {
		return mainAppContextFile;
	}



	/**
	 * @param mainAppContextFile the mainAppContextFile to set
	 */
	public static void setMainAppContextFile(String appContextFile) {
		mainAppContextFile = appContextFile;
	}



	/**
     * 
     */
    public static ApplicationContext initAppContext(String appContextFile) {
        try {
        	mainAppContextFile = appContextFile;
            // ClassPathResource
            String[] contextPaths = new String[] { mainAppContextFile };
            appContext = new ClassPathXmlApplicationContext(contextPaths);
            
            sessionFactory = appContext.getBean(SessionFactory.class);
        } catch (Throwable ex) {
            logger.error("***********Initial SessionFactory creation failed.*******");
            logger.error(ex.getMessage(), ex);
            throw new ExceptionInInitializerError(ex);
        }
        return appContext;
    }
    
    /**
     * @return the appContext
     */
    public static ApplicationContext initAppContext(ServletContext servletContext) {
        logger.info("***********Initializing Spring Context *******");
        appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        sessionFactory = appContext.getBean(SessionFactory.class);
        logger.info("***********Spring Context Initialized*******");
        return appContext;
    }

    /**
     * 
     */
    public static void shutdownHsql() {
        try {
            if (sessionFactory == null) {
                return;
            }
            logger.info("Shutting down HSQLDB...");
            Session session = SpringUtils.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.flush();
            session.getTransaction().commit();
            sessionFactory.close();
            sessionFactory = null;
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    /**
     * 
     */
    public static void closeSession() {
        try {
            if (sessionFactory.getCurrentSession() != null) {
                sessionFactory.getCurrentSession().disconnect();
                sessionFactory.getCurrentSession().close();
            }
        } catch (Exception e) {
            logger.warn("Error while closing session", e);
        }
    }

    /**
     * 
     * @return
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * @return the appContext
     */
    public static ApplicationContext getAppContext() {
        return appContext;
    }
    
    /**
     * 
     * @param appContext
     * @return
     */
    public static void setAppContext(ApplicationContext applicationContext) {
        appContext = applicationContext;
    }
    
    /**
     * 
     * @param serviceClass
     * @return
     */
    public static <T extends BaseEntityService> T getService(Class<T> serviceClass) {
    	return getAppContext().getBean(serviceClass);
    }
    
    /**
     * 
     * @param beanClass
     * @return
     */
    public static <T> T getBean(Class<T> beanClass) {
    	return getAppContext().getBean(beanClass);
    }
    
    /**
     * 
     * @param beanName
     * @return
     */
    public static <T> T getBean(String beanName) {
    	return (T) getAppContext().getBean(beanName);
    }
    
   
}
