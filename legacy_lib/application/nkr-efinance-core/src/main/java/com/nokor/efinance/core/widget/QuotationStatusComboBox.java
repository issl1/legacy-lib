package com.nokor.efinance.core.widget;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.service.EntityService;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.quotation.model.ProfileQuotationStatus;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.security.model.SecProfile;
import com.vaadin.ui.ComboBox;

/**
 * @author ly.youhort
 */
public class QuotationStatusComboBox extends ComboBox {
	
	private static final long serialVersionUID = 214252229353055827L;
	
	private EntityService entityService; 
	/** Base criteria */
	private BaseRestrictions<ProfileQuotationStatus> restrictions;
	
	protected LinkedHashMap<String, EWkfStatus> valueMap = new LinkedHashMap<>();
	 
	
    /**
     * @param dealers
     */
    public QuotationStatusComboBox(final String caption, SecProfile profile) {
        super(caption); 
        clear();
        entityService = (EntityService) SecApplicationContextHolder.getContext().getBean("entityService");
        restrictions = new BaseRestrictions<ProfileQuotationStatus>(ProfileQuotationStatus.class);
        List<Criterion> criterions = new ArrayList<Criterion>();
        criterions.add(Restrictions.eq("profile.id", profile.getId()));
        restrictions.setCriterions(criterions);
        
        List<ProfileQuotationStatus> profileQuotationStatusList = entityService.list(restrictions);        
        if (profileQuotationStatusList != null  && !profileQuotationStatusList.isEmpty()) {
        	LinkedHashMap<String, EWkfStatus> quotationStatusMap = new LinkedHashMap<>();
        	for (ProfileQuotationStatus profileQuotationStatus : profileQuotationStatusList) {
        		if (!quotationStatusMap.containsKey(profileQuotationStatus.getWkfStatus().toString())) {
	        		quotationStatusMap.put(profileQuotationStatus.getWkfStatus().toString(), profileQuotationStatus.getWkfStatus());
        		}
        	}
        	for (EWkfStatus quotationStatus : QuotationWkfStatus.values()) {
        		if (quotationStatusMap.containsKey(quotationStatus.toString())) {
        			addItem(quotationStatus.toString());
	        		setItemCaption(quotationStatus.toString(), quotationStatus.getDesc());
	        		valueMap.put(quotationStatus.toString(), quotationStatus);
        		}
            }
        }
    }
    
	/**
     * @return
     */
    public EWkfStatus getSelectedEntity() {
        return valueMap.get(getValue());
    }

    /**
     * @return
     */
    public void setSelectedEntity(EWkfStatus quotationStatus) {
    	if (quotationStatus != null) {
    		setValue(quotationStatus.toString());
    	} else {
    		setValue(null);
    	}
    }
    
    
    /**
     * Get values from map
     * @return
     */
    public LinkedHashMap<String, EWkfStatus> getValueMap() {
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
