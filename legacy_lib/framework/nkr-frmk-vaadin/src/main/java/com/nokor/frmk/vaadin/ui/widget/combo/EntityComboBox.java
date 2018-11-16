package com.nokor.frmk.vaadin.ui.widget.combo;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.frmk.helper.SeuksaServicesHelper;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;

/**
 * 
 * @author prasnar
 *
 */
public class EntityComboBox<T extends EntityA> extends ComboBox implements SeuksaServicesHelper {
	/** */
	private static final long serialVersionUID = 2571585420723867600L;

	private static final String EMPTY = "[empty]";
	private Class<T> entityClass;
	private String representEmpty;
	private String displayField;
	private LinkedHashMap<String, T> valueMap = new LinkedHashMap<String, T>();

	/**
	 * 
	 * @param entityClass
	 * @param displayField
	 */
	public EntityComboBox(Class<T> entityClass, String displayField) {
		this(entityClass, null, displayField, null);
    }
	
	/**
	 * @param caption
	 * @param entities
	 * @param emptyLabel
	 */
	public EntityComboBox(Class<T> entityClass, String caption, String displayField, String emptyLabel) {
        super(caption);
        this.entityClass = entityClass;
        this.displayField = displayField;
        this.representEmpty = emptyLabel;
        setFilteringMode(FilteringMode.CONTAINS);
    }
	
	/**
	 * 
	 */
	public void renderer() {
		renderer(new BaseRestrictions<T>(entityClass));
	}
	
	/**
	 * Render data
	 */
	public void renderer(BaseRestrictions<T> restrictions) {
		List<T> entities = ENTITY_SRV.list(restrictions);
    	renderer(entities);
    	setValue(null);
    }
	
	/**
	 * 
	 * @param entities
	 */
	 public void renderer (List<T> entities) {
	    	valueMap.clear();
	    	removeAllItems();
	    	if (entities != null  && !entities.isEmpty()) {
	    		if (representEmpty != null && !representEmpty.isEmpty()) {
	        		addItem(EMPTY);
	        		setItemCaption(EMPTY, representEmpty);
	        		setNullSelectionAllowed(false);
	        	}
	        	for (T entity : entities) {
	        		addItem(entity.getId().toString());
	        		try {
	        			PropertyUtilsBean bean = new PropertyUtilsBean();
	        			String value = (String) bean.getNestedProperty(entity, displayField);
						setItemCaption(entity.getId().toString(), value);
	        		} catch (Exception e) {
	        			throw new IllegalStateException("Error on Field [" + displayField + "]", e);
					}
	        		valueMap.put(entity.getId().toString(), entity);
	        	}
	        }
	  }
	 
	 /**
	  * 
	  * @param entities
	  */
	 public void setEntities (List<T> entities) {
	    renderer(entities);
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
    public void setSelectedEntity(T bank) {
    	if (bank != null) {
    		setValue(bank.getId().toString());
    	} else {
    		if (representEmpty != null && !representEmpty.isEmpty()) {
    			setValue(EMPTY);
    		} else {
    			setValue(null);
    		}
    	}
    }
    
    /**
     * @param id
     */
    public void setValueAsCode(Long id) {
    	if (valueMap.size() == 0) {
    		return;
    	}
    	
		for (Iterator<String> iter = valueMap.keySet().iterator(); iter.hasNext(); ) {
			T entity = valueMap.get(iter.next());
			if (entity.getId() != null && entity.getId().equals(id)) {
				setValue(entity.getId().toString());
				break;
			}
		}
    	
    }
    
    /**
     * Get values from map
     * @return
     */
    public LinkedHashMap<String,T> getValueMap() {
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
     * 
     */
    public void clear() {
    	valueMap.clear();
    	removeAllItems();
    	setValue(null);
    }
}
