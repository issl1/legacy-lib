package com.nokor.frmk.testing;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Converter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.model.entity.EntityA;
import org.seuksa.frmk.model.entity.EntityRefA;
import org.seuksa.frmk.model.entity.MEntityA;
import org.seuksa.frmk.model.entity.MEntityRefA;
import org.seuksa.frmk.model.entity.RefDataId;
import org.seuksa.frmk.service.BaseEntityService;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate5.SessionFactoryUtils;
import org.springframework.orm.hibernate5.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

import com.nokor.common.app.tools.SessionStrategy;
import com.nokor.common.app.tools.UserSession;
import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.common.app.tools.helper.AppServicesHelper;
import com.nokor.common.app.web.spring.StartupApplicationContextListener;
import com.nokor.frmk.security.model.SecUser;

import junit.framework.TestCase;

/**
 * @author prasnar
 * 
 */
public class BaseTestCase extends TestCase implements AppServicesHelper {
    protected static Logger logger = LoggerFactory.getLogger(BaseTestCase.class);
    
	private static final String CR = "\r\n";
	private static final List<String> EXCLUDED_ENTITYA_FIELDS = Arrays.asList(MEntityA.ID, MEntityA.CREATEUSER, MEntityA.CREATEDATE, MEntityA.UPDATEUSER, MEntityA.UPDATEDATE, MEntityA.STATUSRECORD, MEntityA.CRUDACTION);
	private static final List<String> EXCLUDED_ENTITYREFA_FIELDS = Arrays.asList(MEntityRefA.CODE, MEntityRefA.DESC, MEntityRefA.DESCEN, MEntityRefA.DESCFR, MEntityRefA.SORTINDEX);
	private static final List<String> EXCLUDED_OTHER_FIELDS = Arrays.asList("serialVersionUID");
	private static final String DEFAULT_APP_CONFIGURATION_FILE = "app-config.properties";
    private static final String DEFAULT_I18N_FILE = "MessagesI18n";
    private static final String DEFAULT_MAIN_APP_CONTEXT_FILE = "application-main-context.xml";

    private String appCfgFile = DEFAULT_APP_CONFIGURATION_FILE;
    private String i18nFile = DEFAULT_I18N_FILE;
    private String mainAppContextFile = DEFAULT_MAIN_APP_CONTEXT_FILE;
    
    protected boolean requiredSpringContext = true;
    protected boolean requiredSecApplicationContext = true;
    protected boolean requiredAuhentication = false;
    protected String login = "admin";
    protected String password = "admin";
    
    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        try {
        	SessionStrategy.setSessionTEST();
        	
        	if (isRequiredSpringContext()) {
        		StartupApplicationContextListener startupContext = new StartupApplicationContextListener();
        		startupContext.contextInitialized(mainAppContextFile);

	    		openSession();
	        		
    			setAuthentication();

    			if (isRequiredAuhentication()) {
	    			authenticate(login, password);
	    		}
	    		
	    		if (UserSession.isAuthenticated()) {
	    			logger.info("Authenticated: " + UserSessionManager.getCurrentUser()	.getUsername());
	    		} else {
	    			SecUser anonymous = getAnonynmousSecUser();
	    			logger.info("Authenticated: Anonymous [" + anonymous.getLogin() + "]");
	    		}
	    		

        	}
    		
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
     * 
     */
    protected void authenticate() {
    	authenticate(login, password);
    }
    
    /**
     * 
     */
    protected void logout() {
    	AUTHENTICAT_SRV.logout();
    	if (UserSession.isAuthenticated()) {
    		throw new IllegalStateException("Logout failed [" + login + "]");
    	}
    }
    
    /**
     * 
     * @param login
     * @param password
     */
    protected void authenticate(String login, String password) {
    	Assert.isTrue(!StringUtils.isEmpty(login), "Login can not be null");
    	Assert.isTrue(!StringUtils.isEmpty(password), "Password can not be null");

    	AUTHENTICAT_SRV.authenticate(login, password);
    	if (!UserSession.isAuthenticated()) {
    		throw new IllegalStateException("Authentication failed [" + login + "]");
    	}
    }
   
    /**
     * 
     * @param sysAdmlogin
     * @param sysAdmFullPassword
     */
    protected void authenticateSysAdmin(String sysAdmlogin, String sysAdmFullPassword) {
    	Assert.isTrue(!StringUtils.isEmpty(login), "Login can not be null");
    	Assert.isTrue(!StringUtils.isEmpty(password), "Password can not be null");
    	
    	SYS_AUTH_SRV.authenticate(sysAdmlogin, sysAdmFullPassword);
    	if (!UserSession.isAuthenticated()) {
    		throw new IllegalStateException("Authentication failed [" + login + "]");
    	}
    }
    /**
     * 
     * @return
     */
    protected boolean isRequiredAuhentication() {
    	return requiredAuhentication;
    }
    
    /**
     * 
     */
    protected void setAuthentication() {
    }
    
    
    /**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @param requiredAuhentication the requiredAuhentication to set
	 */
	public void setRequiredAuhentication(boolean requiredAuhentication) {
		this.requiredAuhentication = requiredAuhentication;
	}

	
    
    /**
	 * @return the requiredSecApplicationContext
	 */
	public boolean isRequiredSecApplicationContext() {
		return requiredSecApplicationContext;
	}

	/**
	 * @param requiredSecApplicationContext the requiredSecApplicationContext to set
	 */
	public void setRequiredSecApplicationContext(boolean requiredSecApplicationContext) {
		this.requiredSecApplicationContext = requiredSecApplicationContext;
	}

	/**
     * 
     * @return
     */
    protected boolean isInitWorkflow() {
    	return true;
    }
  
    
    
    /**
	 * @return the requiredSpringContext
	 */
	public boolean isRequiredSpringContext() {
		return requiredSpringContext;
	}

	/**
	 * @param requiredSpringContext the requiredSpringContext to set
	 */
	public void setRequiredSpringContext(boolean requiredSpringContext) {
		this.requiredSpringContext = requiredSpringContext;
	}

 
    
    /**
     * 
     * @throws Exception
     */
    protected void openSession() throws Exception {
    	logger.info("OPEN SESSION - Simulate OpenSessionInViewFilter");
        SessionFactory sessionFactory = SpringUtils.getSessionFactory();
        Session session = sessionFactory.openSession();
        TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
    }

    
    /**
     * 
     */
    protected void closeSession() {
    	logger.info("CLOSE SESSION - Simulate OpenSessionInViewFilter");
        SessionFactory sessionFactory = SpringUtils.getSessionFactory();
        SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.unbindResource(sessionFactory);
        SessionFactoryUtils.closeSession(sessionHolder.getSession());
    }
    
    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
    	closeSession();
    	super.tearDown();
    }

    /**
     * 
     */
    public BaseTestCase() {
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
    
    /**
     * 
     * @param beanName
     * @return
     */
    protected static <T> T getBean(String beanName) {
    	return SpringUtils.getBean(beanName);
    }

	/**
	 * @return the appConfigFile
	 */
	public String getAppConfigFile() {
		return appCfgFile;
	}

	/**
	 * @param appConfigFile the appConfigFile to set
	 */
	public void setAppConfigFile(String appConfigFile) {
		this.appCfgFile = appConfigFile;
	}

	/**
	 * @return the i18nFile
	 */
	public String getI18nFile() {
		return i18nFile;
	}

	/**
	 * @param i18nFile the i18nFile to set
	 */
	public void setI18nFile(String i18nFile) {
		this.i18nFile = i18nFile;
	}

	/**
	 * @return the mainAppContextFile
	 */
	public String getMainAppContextFile() {
		return mainAppContextFile;
	}

	/**
	 * @param mainAppContextFile the mainAppContextFile to set
	 */
	public void setMainAppContextFile(String mainAppContextFile) {
		this.mainAppContextFile = mainAppContextFile;
	}

    /**
     * 
     * @param clazz
     */
    public void generateMetaClass(Class<?> clazz) {
    	generateMetaClass(clazz, null);
    }

    /**
     * 
     * @param clazz
     * @param outputDir
     */
    public void generateMetaClass(Class<?> clazz, String outputDir) {

		try {
    		List<Field> fields = Arrays.asList(clazz.getDeclaredFields() );
    		String strFields = "";
    		for (Field f : fields) {
    			if (isSupportedTypes(clazz, f)) {
    			strFields += "	public final static String " + f.getName().toUpperCase() + " = \"" + f.getName() + "\";" + CR ;
//        		logger.debug("> " + f.getName() 
//        				+ " - " + f.getType().getName() );
//        				+ " - isPrimitive" + f.getType().isPrimitive()
//        				+ " - isSynthetic" + f.getType().isSynthetic());
    			}
    		}
    		
    		String className = "M" + clazz.getSimpleName();
    		String strClass =  "/**" + CR
							+ " * Auto-generated on " + DateUtils.today() + CR
							+ " */" + CR
    						+ "package " + clazz.getPackage().getName() + ";" + CR  + CR
    						+ "/**" + CR
							+ " * Meta data of " + clazz.getCanonicalName() + CR
							+ " * @author " + CR
    						+ " */" + CR
    						+ "public interface " + className +  " {" + CR + CR
    						+ strFields + CR
    						+ "}"  + CR;
        	logger.info(strClass);
        	 
        	if (StringUtils.isNotEmpty(outputDir)) {
        		File file = new File(outputDir + "/" + className + ".java");
        		FileUtils.write(file, strClass);
            	logger.info("Output file: [" + file.getPath() + "]");
        	}
        	
		} catch (Exception e) {
			logger.debug(e.getMessage(), e);
		}
    }
	
	/**
	 * 
	 * @return
	 */
	public SecUser getAnonynmousSecUser() {
		return SECURITY_SRV.getAnonynmousUser();
	}
	

    /**
     * 
     * @param type
     * @return
     */
    private boolean isSupportedTypes(Class<?> clazz, Field field) {
    	boolean isSupported = (field.getType().getName().startsWith("java.util.")
    			|| field.getType().getName().startsWith("java.lang.")
    			|| field.getType().getName().startsWith("java.math.")
    			|| field.getType().isPrimitive()
    			|| field.getType().isEnum()
    			|| field.getType().isAnnotationPresent(Converter.class)
    			|| Entity.class.isAssignableFrom(field.getType())
    			|| RefDataId.class.isAssignableFrom(field.getType())
    			)
    			&& !Iterable.class.isAssignableFrom(field.getType())
    			&& !Modifier.isStatic(field.getModifiers())
    			&& !Modifier.isFinal(field.getModifiers())
    			&& !Modifier.isInterface(field.getModifiers())
    			&& !EXCLUDED_OTHER_FIELDS.contains(field.getName());
    	
    	if (isSupported && EntityA.class.isAssignableFrom(clazz)) {
			isSupported = !EXCLUDED_ENTITYA_FIELDS.contains(field.getName());
		}
    	if (isSupported && EntityRefA.class.isAssignableFrom(clazz)) {
			isSupported = !EXCLUDED_ENTITYREFA_FIELDS.contains(field.getName());
		}
    	
    	return isSupported;
    }
  


}
