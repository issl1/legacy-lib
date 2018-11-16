package com.nokor.efinance.batch;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.service.BaseEntityService;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.seuksa.frmk.tools.vo.ValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate5.SessionFactoryUtils;
import org.springframework.orm.hibernate5.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.nokor.common.app.tools.helper.AppConfigFileHelper;
import com.nokor.common.app.tools.helper.AppSettingConfigHelper;
import com.nokor.common.app.web.spring.StartupApplicationContextListener;
import com.nokor.frmk.config.AppConfigFile;
import com.nokor.frmk.config.service.RefDataService;
import com.nokor.frmk.config.service.SettingService;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.security.SecurityHelper;
import com.nokor.frmk.security.context.SecApplicationContext;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.security.model.SecApplication;
import com.nokor.frmk.security.service.SecurityService;

/**
 * @author ky.nora
 *
 */
public class BatchSession implements FrmkServicesHelper {
	protected static Logger logger = LoggerFactory.getLogger(BatchSession.class);

	private final String DEFAULT_APP_CONFIGURATION_FILE = "app-config.properties";
	private final String DEFAULT_I18N_FILE = "MessagesI18n";
	private final String DEFAULT_MAIN_APP_CONTEXT_FILE = "application-main-context.xml";

	private String appCfgFile = DEFAULT_APP_CONFIGURATION_FILE;
	private String i18nFile = DEFAULT_I18N_FILE;
	private String mainAppContextFile = DEFAULT_MAIN_APP_CONTEXT_FILE;

	private EntityService entitySrv;
	private SettingService settingSrv;
	private SecurityService securitySrv;
	private RefDataService refTableSrv;

	/**
	 * @throws Exception
	 */
	public void init() throws Exception {
		try {
			initSecApplicationContext();

			final ResourceBundle rb = ResourceBundle.getBundle("i18n.messages", Locale.ENGLISH);
	        final ValuePair[] messages = new ValuePair[rb.keySet().size()];
	        int i = 0;
	        for (final String key : rb.keySet()) {
	            final ValuePair message = new ValuePair();
	            message.setCode(key);
	            try {
					message.setValue(new String(rb.getString(key).getBytes("ISO-8859-1"), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					logger.error("UnsupportedEncodingException", e);
				}
	            messages[i++] = message;
	        }
	        I18N.initBundle(Locale.ENGLISH, messages);

			openSession();

			entitySrv = getBean("entityService");
			securitySrv = getBean("securityService");
			settingSrv = getBean("settingService");
			refTableSrv = getBean("referenceTableService");

		} catch (Exception e) {
			String errMsg = "*******Fatal error occured initializing application. Exiting.!!!*******";
			System.err.println(errMsg);
			logger.error(errMsg, e);
			SpringUtils.shutdownHsql();
			System.exit(-1);
		}
	}

	/**
	 * @see StartupApplicationContextListener
	 */
	private void initSecApplicationContext() {
		try {

			SpringUtils.initAppContext(mainAppContextFile);

			String appCode = null;

			if (StringUtils.isEmpty(appCfgFile)) {
				logger.info("The default config file is ["
						+ AppConfigFile.DEFAULT_APP_CONFIGURATION_FILE + "]");
				appCfgFile = AppConfigFile.DEFAULT_APP_CONFIGURATION_FILE;
			}

			AppConfigFile.initFile(appCfgFile);
			appCode = AppConfigFileHelper.getApplicationCode();
			logger.info("Config file [" + appCfgFile + "] ["
					+ AppConfigFileHelper.APP_CODE + "]: "
					+ (appCode != null ? appCode : "<N/A>"));

			if (StringUtils.isEmpty(appCode)) {
				throw new IllegalStateException("The application code ["
						+ AppConfigFileHelper.APP_CODE
						+ "] must be not defined in ["
						+ AppConfigFile.getConfigFile() + "]");
			}

			SecApplicationContext secAppContext = SecApplicationContextHolder
					.getContext();
			secAppContext.setApplicationContext(SpringUtils.getAppContext());

			SecApplication secApp = SecurityHelper.getSecApplication();
			logger.info("SecApplication: ["
					+ (secApp != null ? secApp : "<null>"));
			secAppContext.setSecApplication(secApp);

			Locale locale = AppSettingConfigHelper.getLocale();
			secAppContext.setLocale(locale); // read from init file/db

			logger.info("Context initialized.");

		} catch (final Exception e) {
			logger.error("****Context NOT initialized.*****", e);
			throw new IllegalStateException(e);
		}
	}

	/**
	 * 
	 * @throws Exception
	 */
	private void openSession() throws Exception {
		logger.info("OPEN BATCH SESSION - Simulate OpenSessionInViewFilter");
		SessionFactory sessionFactory = SpringUtils.getSessionFactory();
		Session session = sessionFactory.openSession();
		TransactionSynchronizationManager.bindResource(sessionFactory,
				new SessionHolder(session));
	}

	/**
     * 
     */
	public void shutDown() {
		logger.info("CLOSE BATCH SESSION");
		SessionFactory sessionFactory = SpringUtils.getSessionFactory();
		SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager
				.unbindResource(sessionFactory);
		SessionFactoryUtils.closeSession(sessionHolder.getSession());
	}

	/**
	 * @return the appContext
	 */
	public ApplicationContext getAppContext() {
		return SpringUtils.getAppContext();
	}

	/**
	 * 
	 * @param serviceClass
	 * @return
	 */
	public <T extends BaseEntityService> T getService(
			Class<T> serviceClass) {
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
	public <T> T getBean(String beanName) {
		return SpringUtils.getBean(beanName);
	}

	/**
	 * @return the appConfigFile
	 */
	public String getAppConfigFile() {
		return appCfgFile;
	}

	/**
	 * @param appConfigFile
	 *            the appConfigFile to set
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
	 * @param i18nFile
	 *            the i18nFile to set
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
	 * @param mainAppContextFile
	 *            the mainAppContextFile to set
	 */
	public void setMainAppContextFile(String mainAppContextFile) {
		this.mainAppContextFile = mainAppContextFile;
	}

	/**
	 * @return
	 */
	public EntityService getEntitySrv() {
		return entitySrv;
	}

	/**
	 * @param entitySrv
	 */
	public void setEntitySrv(EntityService entitySrv) {
		this.entitySrv = entitySrv;
	}

	/**
	 * @return
	 */
	public SettingService getSettingSrv() {
		return settingSrv;
	}

	/**
	 * @param settingSrv
	 */
	public void setSettingSrv(SettingService settingSrv) {
		this.settingSrv = settingSrv;
	}

	/**
	 * @return
	 */
	public SecurityService getSecuritySrv() {
		return securitySrv;
	}

	/**
	 * @param securitySrv
	 */
	public void setSecuritySrv(SecurityService securitySrv) {
		this.securitySrv = securitySrv;
	}

	/**
	 * @return
	 */
	public RefDataService getRefTableSrv() {
		return refTableSrv;
	}

	/**
	 * @param refTableSrv
	 */
	public void setRefTableSrv(RefDataService refTableSrv) {
		this.refTableSrv = refTableSrv;
	}

}
