package com.nokor.efinance.core.widget;

import java.util.LinkedHashMap;
import java.util.List;

import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.ComboBox;

/**
 * ComboBox for EntityA
 * @author bunlong.taing
 */
public abstract class EntityAComboBox<T extends EntityA> extends ComboBox {

	/** */
	private static final long serialVersionUID = -3708823235903086371L;
	
	protected LinkedHashMap<String, T> valueMap = new LinkedHashMap<String, T>();
	
	/**
	 * @param entityAs
	 */
	public EntityAComboBox(List<T> entityAs) {
		this(null, entityAs);
    }
	
	/**
	 * @param caption
	 * @param entityAs
	 */
    public EntityAComboBox(final String caption, List<T> entityAs) {
        super(I18N.message(caption));
        setValues(entityAs);
    }
    
    /**
     * Add Value to the ComboBox
     * @param entityAs
     */
    public void setValues(List<T> entityAs) {
    	clear();
    	if (entityAs != null  && !entityAs.isEmpty()) {
        	for (T entityA : entityAs) {
        		addValue(entityA);           
        	}
        }
    }
    
    /**
     * Add a value to the ComboBox
     * @param entityA
     */
    public void addValue(T entityA) {
    	if (entityA != null && !valueMap.containsKey(entityA.getId().toString())) {
    		addItem(entityA.getId().toString());
    		setItemCaption(entityA.getId().toString(), getEntityACaption(entityA));
    		valueMap.put(entityA.getId().toString(), entityA);
    	}
    }
    
    /**
     * Get the selected entity
     * @return
     */
    public T getSelectedEntity() {
        return valueMap.get(getValue());
    }

    /**
     * Set entity to be selected
     * @param entityA
     */
    public void setSelectedEntity(T entityA) {
    	if (entityA != null) {
    		setValue(entityA.getId().toString());
    	} else {
    		setValue(null);
    	}
    }
    
    /**
     * Get all the values in the ComboBox
     * @return
     */
    public LinkedHashMap<String, T> getValueMap() {
        return valueMap;
    }
    
    /**
     * Check whether the ComboBox contain the key
     * @param key
     * @return
     */
    public boolean containsKey(final String key) {
    	return key == null ? false : valueMap.containsKey(key);
    }
    
    /**
     * Clear or remove all values in the ComboBox
     */
    public void clear() {
    	valueMap.clear();
    	removeAllItems();
    	setValue(null);
    }
    
    /**
     * Get the entity caption for each item in the ComboBox
     * @param entityA
     * @return
     */
    protected abstract String getEntityACaption(T entityA);

}
