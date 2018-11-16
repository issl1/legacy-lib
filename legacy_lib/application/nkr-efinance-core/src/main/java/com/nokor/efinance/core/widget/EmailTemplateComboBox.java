package com.nokor.efinance.core.widget;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.nokor.efinance.core.common.reference.model.EmailTemplate;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;

/**
 * Email Template ComboBox
 * @author uhout.cheng
 */
public class EmailTemplateComboBox extends ComboBox {
	
	/** */
	private static final long serialVersionUID = 3104424829329289555L;
	
	protected LinkedHashMap<String, EmailTemplate> valueMap = new LinkedHashMap<String, EmailTemplate>();
	private String representEmpty;
	 
	/**
	 * 
	 * @param emailTemplates
	 */
	public EmailTemplateComboBox(List<EmailTemplate> emailTemplates) {
		this(null, emailTemplates);
	}
	
    /**
     * 
     * @param caption
     * @param emailTemplates
     * @param emptyLabel
     */
    public EmailTemplateComboBox(final String caption, List<EmailTemplate> emailTemplates, String emptyLabel) {
        super(caption);
        representEmpty = emptyLabel;
        renderers(emailTemplates);
        setFilteringMode(FilteringMode.CONTAINS);
    }
    
    /**
     * Render the ComboBox
     * @param emailTemplates
     */
    private void renderers(List<EmailTemplate> emailTemplates) {
    	valueMap.clear();
    	removeAllItems();
    	if (emailTemplates != null  && !emailTemplates.isEmpty()) {
        	if (representEmpty != null && !representEmpty.isEmpty()) {
        		addItem("[empty]");
        		setItemCaption("[empty]", representEmpty);
        		setNullSelectionAllowed(false);
        	}
        	for (EmailTemplate emailTemplate : emailTemplates) {
        		if(StringUtils.isNoneEmpty(emailTemplate.getDescLocale())){
        			addItem(emailTemplate.getId().toString());
        			setItemCaption(emailTemplate.getId().toString(), emailTemplate.getDescLocale());
        			valueMap.put(emailTemplate.getId().toString(), emailTemplate);
        		}
    			
        	}
        }
    }
    
    /**
     * Set the emailTemplates to the ComboBox and remove any previous emailTemplates.
     * @param emailTemplates
     */
    public void setEmailTemplate(List<EmailTemplate> emailTemplates) {
    	renderers(emailTemplates);
    }
    
    /**
     * 
     * @param caption
     * @param emailTemplates
     */
    public EmailTemplateComboBox(final String caption, List<EmailTemplate> emailTemplates) {
    	this(caption, emailTemplates, null);
    }
    
	/**
     * @return
     */
    public EmailTemplate getSelectedEntity() {
        return valueMap.get(getValue());
    }

    /**
     * 
     * @param emailTemplate
     */
    public void setSelectedEntity(EmailTemplate emailTemplate) {
    	if (emailTemplate != null) {
    		setValue(emailTemplate.getId().toString());
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
    			EmailTemplate entity = valueMap.get(iter.next());
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
    public LinkedHashMap<String, EmailTemplate> getValueMap() {
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