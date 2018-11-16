package com.nokor.frmk.security.context;


import java.io.Serializable;
import java.util.Locale;

import org.springframework.context.ApplicationContext;
import org.springframework.mobile.device.Device;

import com.nokor.frmk.security.model.SecApplication;
import com.nokor.frmk.security.model.SecUser;

/**
 * What should able to do a SecApplicationContext
 * 
 * @author prasnar
 *
 */
public interface SecApplicationContextAware extends Serializable {
	void clear();

	SecApplication getSecApplication();

	void setSecApplication(SecApplication secApp);

	ApplicationContext getApplicationContext();
	
	void setApplicationContext(ApplicationContext applicationContext);
	
	Locale getLocale();

	void removeAttribute(String key);

	void putAttribute(String key, Object value);

	Object getAttribute(String key);
	
	boolean disconnect(SecUser secUser);
	
	boolean disconnectAll();
	
	void sendMessage(Object msg);
	
	void ackMessage();

	Device getDevice();
}