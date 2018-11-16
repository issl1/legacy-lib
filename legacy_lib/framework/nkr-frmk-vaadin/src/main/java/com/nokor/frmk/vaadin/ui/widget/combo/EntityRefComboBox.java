package com.nokor.frmk.vaadin.ui.widget.combo;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;

import org.hibernate.criterion.Order;
import org.seuksa.frmk.dao.criteria.FilterMode;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.RefDataId;

import com.nokor.frmk.helper.FrmkServicesHelper;
import com.vaadin.ui.ComboBox;

/**
 * @author ly.youhort
 */
public class EntityRefComboBox<T extends RefDataId> extends ComboBox implements FrmkServicesHelper {
	
	private static final long serialVersionUID = 228046564757710688L;
	
	/** Base criteria */
	protected BaseRestrictions<T> restrictions;
	
	private boolean onlyActive;
	private Function<T, String> captionRenderer;
	
	protected LinkedHashMap<String, T> valueMap = new LinkedHashMap<String, T>();
	 
	/**
	 * 
	 */
	public EntityRefComboBox() {
		this((String) null);
    }

    /**
     * @param values
     */
    public EntityRefComboBox(final String caption) {
        this(caption, false);
    }
    
    /**
     * 
     * @param values
     */
    public EntityRefComboBox(List<T> values) {
		this(null, values);
    }
	
    /**
     * @param values
     */
    public EntityRefComboBox(final String caption, List<T> values) {
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
    public EntityRefComboBox(final String caption, boolean onlyActive) {
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
    		setValue(value.getId().toString());
    	} else {
    		setValue(null);
    	}
    }
    
    /**
     * @param code
     */
    public void setValueAsCode(String code) {
    	if (valueMap != null) {
    		for (Iterator<String> iter = valueMap.keySet().iterator(); iter.hasNext(); ) {
    			T entity = valueMap.get(iter.next());
    			if (entity.getCode() != null && entity.getCode().equals(code)) {
    				setValue(entity.getId().toString());
    				break;
    			}
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
    		if (captionRenderer == null) {
    			captionRenderer = new DescCaptionRenderer();
    		}
    		setItemCaption(refEntity.getId().toString(), captionRenderer.apply(refEntity));
    		valueMap.put(refEntity.getId().toString(), refEntity);                
    	}
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
	 * @return the captionRenderer
	 */
	public Function<T, String> getCaptionRenderer() {
		return captionRenderer;
	}

	/**
	 * @param captionRenderer the captionRenderer to set
	 */
	public void setCaptionRenderer(Function<T, String> captionRenderer) {
		this.captionRenderer = captionRenderer;
	}
	
	/**
	 * Render caption from Entity's desc
	 * @author bunlong.taing
	 *
	 */
	private class DescCaptionRenderer implements Function<T, String> {

		/**
		 * @see java.util.function.Function#apply(java.lang.Object)
		 */
		@Override
		public String apply(T t) {
			return I18N.isEnglishLocale() ? t.getDescEn() : t.getDesc();
		}
		
	}
	
}
