/**
 * 
 */
package com.nokor.common.app.tools;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author prasnar
 *
 */
public abstract class SessionInfo implements BaseSessionKeys, Serializable {
	/** */
	private static final long serialVersionUID = 8283452237124835635L;

	protected Logger logger = LoggerFactory.getLogger(getClass());

    protected Map<String, Object> attributes = null;

    /**
     * 
     */
	public void start() {
       	attributes = new HashMap<String, Object>();
        setCreated(new Date());
	}
	
	/**
	 * 
	 */
	public void end() {
		attributes.clear();
       	attributes = new HashMap<String, Object>();
	}
	
	/**
	 * @return the attributes
	 */
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	/**
	 * @param attributes the attributes to set
	 */
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getAttribute(String key) {
		return attributes.get(key);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setAttribute(String key, Object value) {
		attributes.put(key, value);
	}
    
	
	/**
	 * @return the created
	 */
	public Date getCreated() {
		return (Date) attributes.get(KEY_CONNECTION_DATE_TIME);
	}
	
	/**
	 * 
	 * @param created
	 */
	public void setCreated(Date created) {
		attributes.put(KEY_CONNECTION_DATE_TIME, created);
	}
	


}
