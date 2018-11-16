package com.nokor.frmk.vaadin.ui.widget.combo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.RefDataId;
import org.seuksa.frmk.model.eref.BaseERefData;

import com.vaadin.ui.ComboBox;

/**
 * @author ly.youhort
 *
 */
public class ERefDataComboBox<T extends RefDataId> extends ComboBox {

	private static final long serialVersionUID = 8795061348473343268L;

	protected LinkedHashMap<String, T> valueMap = new LinkedHashMap<String, T>();
	
	private Locale locale = I18N.getLocale();
	
	/**
	 * @param values 
	 */
	public ERefDataComboBox(final List<T> values) {
		this(null, I18N.getLocale(), values);
	}
	
	/**
	 * @param values
	 */
	public ERefDataComboBox(final Locale locale, final List<T> values) {
		this(null, locale, values);
	}
	
	
	/**
	 * 
	 * @param clazz
	 */
	public ERefDataComboBox(Class<T> clazz) {
		this(null, I18N.getLocale(), (List<T>) BaseERefData.getValues(clazz));
	}
	
	/**
	 * 
	 * @param caption
	 * @param clazz
	 */
	public ERefDataComboBox(final String caption, Class<T> clazz) {
		this(caption, I18N.getLocale(), (List<T>) BaseERefData.getValues(clazz));
	}
	
	/**
	 * 
	 * @param caption
	 * @param clazz
	 */
	public ERefDataComboBox(final String caption, Locale locale, Class<T> clazz) {
		this(caption, locale, (List<T>) BaseERefData.getValues(clazz));
	}
	
	/**
	 * @param caption Caption
	 * @param values Combo values
	 */
	public ERefDataComboBox(final String caption, final List<T> values) {
		this(caption, I18N.getLocale(), values);
	}
	
	/**
	 * @param caption Caption
	 * @param values Combo values
	 */
	public ERefDataComboBox(final String caption, Locale locale, final List<T> values) {
		super(caption);
		assignValueMap(values);
		this.locale= locale;
	}
	
	public void assignValueMap(List<T> values) {
		valueMap.clear();
    	removeAllItems();
		
		if (values != null  && !values.isEmpty()) {
        	for (T sysRefData : values) {
        		addItem(sysRefData.getCode());
        		if (Locale.ENGLISH.equals(locale)) {
        			setItemCaption(sysRefData.getCode(), ((BaseERefData) sysRefData).getDescEn());
                } else {
                	setItemCaption(sysRefData.getCode(), ((BaseERefData) sysRefData).getDesc());
                }
        		valueMap.put(sysRefData.getCode(), sysRefData);                
        	}
        }
		setValue(null);
		markAsDirty();
	}
	
	/**
     * @return
     */
    public T getSelectedEntity() {
        return valueMap.get(getValue());
    }
    
    /**
     * @return
     */
    public void setSelectedEntity(T value) {
    	if (value != null) {
    		setValue(value.getCode());
    	} else {
    		setValue(null);
    	}
    }
       
    /**
     * Get values from map
     * @return
     */
    public LinkedHashMap<String, T> getValueMap() {
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
}
