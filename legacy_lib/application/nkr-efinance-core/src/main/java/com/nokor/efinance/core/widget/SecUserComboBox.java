package com.nokor.efinance.core.widget;

import java.util.LinkedHashMap;
import java.util.List;

import com.nokor.frmk.security.model.SecUser;
import com.vaadin.ui.ComboBox;

/**
 * @author ly.youhort
 */
public class SecUserComboBox extends ComboBox {

	private static final long serialVersionUID = 4127214033490718305L;
	
	protected LinkedHashMap<String, SecUser> valueMap = new LinkedHashMap<String, SecUser>();
	 
	public SecUserComboBox(List<SecUser> users) {
		this(null, users);
    }
	
    /**
     * @param dealers
     */
    public SecUserComboBox(final String caption, List<SecUser> users) {
        super(caption);
        setValues(users);
    }
    
    /**
     * @param users
     */
    public void setValues(List<SecUser> users) {
    	clear();
    	if (users != null  && !users.isEmpty()) {
        	for (SecUser user : users) {
        		addItem(user.getId().toString());
        		setItemCaption(user.getId().toString(), user.getDesc());
        		valueMap.put(user.getId().toString(), user);                
        	}
        }
    }
    
	/**
     * @return
     */
    public SecUser getSelectedEntity() {
        return valueMap.get(getValue());
    }

    /**
     * @return
     */
    public void setSelectedEntity(SecUser user) {
    	if (user != null) {
    		setValue(user.getId().toString());
    	} else {
    		setValue(null);
    	}
    }
   
    
    /**
     * Get values from map
     * @return
     */
    public LinkedHashMap<String, SecUser> getValueMap() {
        return valueMap;
    }
    
    /**
     * @param key
     * @return
     */
    public boolean containsKey(final String key) {
    	return key == null ? false : valueMap.containsKey(key);
    }
    
    /**
     */
    public void clear() {
    	valueMap.clear();
    	removeAllItems();
    	setValue(null);
    }
    
    /**
     * @param secUser
     */
    public void addValue(SecUser user) {
    	if (user != null && !valueMap.containsKey(user.getId().toString())) {
    		addItem(user.getId().toString());
    		setItemCaption(user.getId().toString(), user.getDesc());
    		valueMap.put(user.getId().toString(), user);
    	}
    }
}
