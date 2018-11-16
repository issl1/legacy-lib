package com.nokor.frmk.security.context;


import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.mobile.device.Device;

import com.nokor.frmk.security.model.SecApplication;
import com.nokor.frmk.security.model.SecUser;

/**
 * The context of the SecApplication
 * 
 * @author prasnar
 */
public class SecApplicationContext implements SecApplicationContextAware {
	/** */
	private static final long serialVersionUID = -2677744061768057403L;

	private static final Logger logger = LoggerFactory.getLogger(SecApplicationContext.class);

	protected static final String ATTR_CONNECTION_DATE_TIME = "@connectionDatetime@";
	protected static final String ATTR_LOCALE = "@locale@";

	protected static final String ATTR_DEVICE = "@device@";
	protected static final String ATTR_USER_AGENT = "@useragent@";

    private Map<String, Object> attributes = null;
    private SecApplication secApplication;
    private ApplicationContext applicationContext;
    
    /**
     * 
     */
    public SecApplicationContext() {
    	attributes = new HashMap<String, Object>();
        setCreated(new Date());
    }
    
    
	@Override
	public void clear() {
		 attributes = new HashMap<String, Object>();
		 setCreated(null);
	}

	@Override
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	/**
	 * @return the locale
	 */
    @Override
	public Locale getLocale() {
    	if (getAttribute(ATTR_LOCALE) != null) {
    		return (Locale) getAttribute(ATTR_LOCALE);
    	} else {
    		return Locale.getDefault();
    	}
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(Locale locale) {
		attributes.put(ATTR_LOCALE, locale);
	}
	
	/**
	 * @return the device
	 */
    @Override
	public Device getDevice() {
    	if (getAttribute(ATTR_DEVICE) != null) {
    		return (Device) getAttribute(ATTR_DEVICE);
    	} else {
    		return null;
    	}
	}

	/**
	 * @param device the device to set
	 */
	public void setDevice(Device device) {
		attributes.put(ATTR_DEVICE, device);
	}
	
	/**
	 * @return the userAgent
	 */
	public String getUserAgent() {
    	if (getAttribute(ATTR_USER_AGENT) != null) {
    		return (String) getAttribute(ATTR_USER_AGENT);
    	} else {
    		return null;
    	}
	}

	/**
	 * @param userAgent the userAgent to set
	 */
	public void setUserAgent(String userAgent) {
		attributes.put(ATTR_USER_AGENT, userAgent);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	@Override
	public void putAttribute(String key, Object value) {
		attributes.put(key, value);
	}
	
	@Override
	public Object getAttribute(String key) {
		return attributes.get(key);
	}
	
	@Override
	public void removeAttribute(String key) {
		attributes.remove(key);
	}
    

	/**
	 * @return the created
	 */
	public Date getCreated() {
		return (Date) attributes.get(ATTR_CONNECTION_DATE_TIME);
	}
	
	/**
	 * 
	 * @param created
	 */
	public void setCreated(Date created) {
		attributes.put(ATTR_CONNECTION_DATE_TIME, created);
	}
	
	/**
	 * 
	 * @param beanName
	 * @return
	 */
	public Object getBean(String beanName) {
		return getApplicationContext().getBean(beanName);
	}
	
	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public <T> T getBean(Class<T> clazz) {
		return getApplicationContext().getBean(clazz);
	}


	@Override
	public SecApplication getSecApplication() {
		return secApplication;
	}

	@Override
	public void setSecApplication(SecApplication secApp) {
		this.secApplication = secApp;
	}

	@Override
	public boolean disconnect(SecUser secUser) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean disconnectAll() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void sendMessage(Object msg) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void ackMessage() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean clientDeviceIsMobile() {
		String userAgent = getUserAgent() != null ? getUserAgent().toLowerCase() : "";

		Device currentDevice = getDevice();
		if (currentDevice == null) {
			logger.debug("**SPRING**DEVICE IS NULL=>NORMAL* userAgent [" + userAgent + "]*************");
			return false;
		}
		if (currentDevice.isMobile()) {
			logger.debug("**SPRING**DEVICE IS MOBILE* userAgent [" + userAgent + "]*************");
			return true;
		} else if (currentDevice.isTablet()) {
			logger.debug("**SPRING**DEVICE IS TABLET* userAgent [" + userAgent + "]*************");
			return false;
		} else {
			logger.debug("**SPRING**DEVICE IS NORMAL* userAgent [" + userAgent + "]*************");
			return false;
		}
	}
	/**
	 * 
	 * @return
	 */
	public boolean clientDeviceIsTablet() {
		String userAgent = getUserAgent() != null ? getUserAgent().toLowerCase() : "";

		Device currentDevice = getDevice();
		if (currentDevice == null) {
			logger.debug("**SPRING**DEVICE IS NULL=>NORMAL* userAgent [" + userAgent + "]*************");
			return false;
		}
		if (currentDevice.isMobile()) {
			logger.debug("**SPRING**DEVICE IS MOBILE* userAgent [" + userAgent + "]*************");
			return true;
		} else if (currentDevice.isTablet()) {
			logger.debug("**SPRING**DEVICE IS TABLET* userAgent [" + userAgent + "]*************");
			return true;
		} else {
			logger.debug("**SPRING**DEVICE IS NORMAL* userAgent [" + userAgent + "]*************");
			return false;
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean clientDeviceIsNormal() {
		String userAgent = getUserAgent() != null ? getUserAgent().toLowerCase() : "";

		Device currentDevice = getDevice();
		if (currentDevice == null) {
			logger.debug("**SPRING**DEVICE IS NULL=>NORMAL* userAgent [" + userAgent + "]*************");
			return false;
		}
		if (currentDevice.isMobile()) {
			logger.debug("**SPRING**DEVICE IS MOBILE* userAgent [" + userAgent + "]*************");
			return false;
		} else if (currentDevice.isTablet()) {
			logger.debug("**SPRING**DEVICE IS TABLET* userAgent [" + userAgent + "]*************");
			return true;
		} else {
			logger.debug("**SPRING**DEVICE IS NORMAL* userAgent [" + userAgent + "]*************");
			return false;
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean clientDeviceIsMobileOrTablet() {
		String userAgent = getUserAgent() != null ? getUserAgent().toLowerCase() : "";

		Device currentDevice = getDevice();
		if (currentDevice == null) {
			logger.debug("**SPRING**DEVICE IS NULL=>NORMAL* userAgent [" + userAgent + "]*************");
			return false;
		}
		if (currentDevice.isMobile()) {
			logger.debug("**SPRING**DEVICE IS MOBILE* userAgent [" + userAgent + "]*************");
			return true;
		} else if (currentDevice.isTablet()) {
			logger.debug("**SPRING**DEVICE IS TABLET* userAgent [" + userAgent + "]*************");
			return true;
		} else {
			logger.debug("**SPRING**DEVICE IS NORMAL* userAgent [" + userAgent + "]*************");
			return false;
		}
	}

}
