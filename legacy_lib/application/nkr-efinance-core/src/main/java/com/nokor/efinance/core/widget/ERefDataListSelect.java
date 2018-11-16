package com.nokor.efinance.core.widget;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.RefDataId;
import org.seuksa.frmk.model.eref.BaseERefData;

import com.vaadin.ui.ListSelect;

/**
 * Multi-selection list
 * 
 * @author pengleng.huot
 *
 * @param <T>
 */
public class ERefDataListSelect<T extends RefDataId> extends ListSelect {

	/** */
	private static final long serialVersionUID = -4353707858929914753L;

	protected LinkedHashMap<String, T> valueMap;
	
	/**
	 * @param values
	 */
	public ERefDataListSelect(final List<T> values) {
		this(null, values);
	}
	
	
	/**
	 * @param clazz
	 */
	@SuppressWarnings("unchecked")
	public ERefDataListSelect(Class<? extends BaseERefData> clazz) {
		this(null, (List<T>) BaseERefData.getValues(clazz));
	}
	
	/**
	 * 
	 * @param caption
	 * @param clazz
	 */
	@SuppressWarnings("unchecked")
	public ERefDataListSelect(final String caption, Class<? extends BaseERefData> clazz) {
		this(caption, (List<T>) BaseERefData.getValues(clazz));
	}
	
	/**
	 * @param caption Caption
	 * @param values Combo values
	 */
	public ERefDataListSelect(final String caption, final List<T> values) {
		super(caption);
		this.valueMap = new LinkedHashMap<String, T>();
		setNullSelectionAllowed(true);
		setMultiSelect(true);
		setEntities(values);
	}
	
	/**
	 * 
	 * @param values
	 */
	public void setEntities(List<T> values) {
		this.clear();
		if (values != null  && !values.isEmpty()) {
        	for (T sysRefData : values) {
        		addItem(sysRefData.getCode());
        		if (I18N.isEnglishLocale()) {
        			setItemCaption(sysRefData.getCode(), ((BaseERefData) sysRefData).getDescEn());
                } else {
                	setItemCaption(sysRefData.getCode(), ((BaseERefData) sysRefData).getDesc());
                }
        		valueMap.put(sysRefData.getCode(), sysRefData);              
        	}
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
	
	/** */
	public void clear() {
		this.valueMap.clear();
		removeAllItems();
		setValue(null);
	}
	
	/**
	 * Get all the selected Entities
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<T> getSelectedEntities() {
		List<T> selectedValue = new ArrayList<T>();
		Object[] keys = ((Set) getValue()).toArray();
		for (Object key : keys) {
			selectedValue.add(valueMap.get((String) key));
		}
		return selectedValue;
	}
	
	/**
	 * @param values
	 */
	public void setSelectedEntities(List<T> values) {
		if (values != null) {
			List<String> selectedEntities = new ArrayList<String>();
			for(T refEntity : values) {
				selectedEntities.add(refEntity.getCode());
			}
			setValue(selectedEntities);
		} else {
			setValue(null);
		}
	}

}
