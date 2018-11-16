package com.nokor.efinance.core.widget;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.nokor.efinance.core.common.reference.model.ESmsTemplate;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;

/**
 * SMS Template ComboBox
 * @author uhout.cheng
 */
public class SMSTemplateComboBox extends ComboBox {
	
	/** */
	private static final long serialVersionUID = -1076722238373039592L;
	
	protected LinkedHashMap<String, ESmsTemplate> valueMap = new LinkedHashMap<String, ESmsTemplate>();
	private String representEmpty;
	 
	/**
	 * 
	 * @param smsTemplates
	 */
	public SMSTemplateComboBox(List<ESmsTemplate> smsTemplates) {
		this(null, smsTemplates);
	}
	
    /**
     * 
     * @param caption
     * @param smsTemplates
     * @param emptyLabel
     */
    public SMSTemplateComboBox(final String caption, List<ESmsTemplate> smsTemplates, String emptyLabel) {
        super(caption);
        representEmpty = emptyLabel;
        renderers(smsTemplates);
        setFilteringMode(FilteringMode.CONTAINS);
    }
    
    /**
     * Render the ComboBox
     * @param dealers
     */
    private void renderers(List<ESmsTemplate> smsTemplates) {
    	valueMap.clear();
    	removeAllItems();
    	if (smsTemplates != null  && !smsTemplates.isEmpty()) {
        	if (representEmpty != null && !representEmpty.isEmpty()) {
        		addItem("[empty]");
        		setItemCaption("[empty]", representEmpty);
        		setNullSelectionAllowed(false);
        	}
        	for (ESmsTemplate smsTemplate : smsTemplates) {
        		if(StringUtils.isNoneEmpty(smsTemplate.getDescLocale())){
        			addItem(smsTemplate.getId().toString());
        			setItemCaption(smsTemplate.getId().toString(), smsTemplate.getDescLocale());
        			valueMap.put(smsTemplate.getId().toString(), smsTemplate);
        		}
    			
        	}
        }
    }
    
    /**
     * Set the smsTemplates to the ComboBox and remove any previous smsTemplates.
     * @param smsTemplates
     */
    public void setSmsTemplate(List<ESmsTemplate> smsTemplates) {
    	renderers(smsTemplates);
    }
    
    /**
     * 
     * @param caption
     * @param smsTemplates
     */
    public SMSTemplateComboBox(final String caption, List<ESmsTemplate> smsTemplates) {
    	this(caption, smsTemplates, null);
    }
    
	/**
     * @return
     */
    public ESmsTemplate getSelectedEntity() {
        return valueMap.get(getValue());
    }

    /**
     * 
     * @param smsTemplate
     */
    public void setSelectedEntity(ESmsTemplate smsTemplate) {
    	if (smsTemplate != null) {
    		setValue(smsTemplate.getId().toString());
    	} else {
    		if (representEmpty != null && !representEmpty.isEmpty()) {
    			setValue("[empty]");
    		} else {
    			setValue(null);
    		}
    	}
    }
    
    /**
     * @param code
     */
    public void setValueAsCode(String code) {
    	if (valueMap != null) {
    		for (Iterator<String> iter = valueMap.keySet().iterator(); iter.hasNext(); ) {
    			ESmsTemplate entity = valueMap.get(iter.next());
    			if (StringUtils.isNoneEmpty(entity.getDescLocale()) && entity.getCode().equals(code)) {
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
    public LinkedHashMap<String, ESmsTemplate> getValueMap() {
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