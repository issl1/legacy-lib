package com.nokor.frmk.vaadin.ui.widget.combo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.hibernate.criterion.Order;
import org.seuksa.frmk.dao.criteria.FilterMode;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.RefDataId;

import com.nokor.frmk.helper.FrmkServicesHelper;
import com.vaadin.ui.OptionGroup;

/**
 * 
 * @author phirun.kong
 *
 * @param <T>
 */
public class EntityRefOptionGroup<T extends RefDataId> extends OptionGroup implements FrmkServicesHelper {

	/**	 */
	private static final long serialVersionUID = 4508970184461393017L;
	
	/** Base criteria */
	protected BaseRestrictions<T> restrictions;
	protected LinkedHashMap<String, T> valueMap = new LinkedHashMap<String, T>();
	private boolean onlyActive;
	
	/**
	 * 
	 */
	public EntityRefOptionGroup() {
		this((String) null);
    }

    /**
     * @param values
     */
    public EntityRefOptionGroup(final String caption) {
        this(caption, false);
    }
    
    /**
     * 
     * @param values
     */
    public EntityRefOptionGroup(List<T> values) {
		this(null, values);
    }
	
    /**
     * @param values
     */
    public EntityRefOptionGroup(final String caption, List<T> values) {
    	super(caption);
        if (values != null  && !values.isEmpty()) {
        	for (T refEntity : values) {
        		addItem(refEntity.getId().toString());
        		if (I18N.isEnglishLocale()) {
        			setItemCaption(refEntity.getId().toString(), refEntity.getDescEn());
        		} else {
        			setItemCaption(refEntity.getId().toString(), refEntity.getDesc());
        		}
        		valueMap.put(refEntity.getId().toString(), refEntity);                
        	}
        }
    }
    
    /**
     * @param values
     */
    public EntityRefOptionGroup(final String caption, boolean onlyActive) {
        super(caption);
        this.onlyActive = onlyActive;
    }
    
    /**
	 * @return the restrictions
	 */
	public BaseRestrictions<T> getRestrictions() {
		return restrictions;
	}

	/**
	 * @param restrictions the restrictions to set
	 */
	public void setRestrictions(BaseRestrictions<T> restrictions) {
		this.restrictions = restrictions;
		if (onlyActive) {
			this.restrictions.addCriterion("statusRecord", FilterMode.EQUALS, EStatusRecord.ACTIV);
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
     * Renderer list value
     */
    public void renderer() {
    	valueMap.clear();
    	removeAllItems();
    	if (restrictions.getOrders() == null || restrictions.getOrders().isEmpty()) {
    		restrictions.addOrder(Order.asc("descEn"));
    	}
		List<T> entities = ENTITY_SRV.list(restrictions);
    	for (T refEntity : entities) {
    		addItem(refEntity.getId().toString());
    		if (I18N.isEnglishLocale()) {
    			setItemCaption(refEntity.getId().toString(), refEntity.getDescEn());
    		} else {
    			setItemCaption(refEntity.getId().toString(), refEntity.getDesc());
    		}
    		valueMap.put(refEntity.getId().toString(), refEntity);                
    	}
    	setMultiSelect(true);
		setNullSelectionAllowed(false);
		setValue(null);
    }
    
    /**
     */
    public void clear() {
    	valueMap.clear();
    	removeAllItems();
    	setValue(null);
    }
    
	/**
     * @return
     */
    public List<T> getSelectedEntity() {
    	Object obj = getValue();
    	if (obj != null) {
    		List<T> values = new ArrayList<T>();
    		Set<?> selValues = (Set<?>) obj;
	    	for (Object key : selValues) {
				values.add(valueMap.get(key));
			}
	    	return values;
    	}
    	return null;
    }
    
    /**
     * @return
     */
    public void setSelectedEntity(List<T> values) {
    	if (values != null) {
    		Set<Object> val = new HashSet<Object>();
    		for (T value : values) {
    			val.add(value.getId().toString());
			}
    		setValue(val);
    	} else {
    		setValue(null);
    	}
    }

}

