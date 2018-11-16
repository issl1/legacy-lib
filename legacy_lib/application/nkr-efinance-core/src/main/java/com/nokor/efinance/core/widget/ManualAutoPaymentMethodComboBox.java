package com.nokor.efinance.core.widget;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.spring.SpringUtils;

import com.nokor.efinance.core.payment.model.ECategoryPaymentMethod;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.vaadin.ui.ComboBox;

/**
 * @author ly.youhort
 */
public class ManualAutoPaymentMethodComboBox extends ComboBox {
	
	private static final long serialVersionUID = 228046564757710688L;
	
	protected LinkedHashMap<String, EPaymentMethod> valueMap = new LinkedHashMap<String, EPaymentMethod>();
	private EntityService entityService;
	
	public ManualAutoPaymentMethodComboBox() {
		this(null);
    }
	
    /**
     * @param caption
     * @param dealers
     * @param emptyLabel
     */
    public ManualAutoPaymentMethodComboBox(final String caption) {
        super(caption);
    }
    
    /**
     * Renderer list value
     */
    public void renderer() {
    	valueMap.clear();
    	removeAllItems();
    	entityService = SpringUtils.getBean(EntityService.class);
    	
		List<EPaymentMethod> entities = getValueByCategories();
    	for (EPaymentMethod refEntity : entities) {
    		addItem(String.valueOf(refEntity.getId()));
    		setItemCaption(String.valueOf(refEntity.getId()), refEntity.getDescEn());
    		valueMap.put(String.valueOf(refEntity.getId()), refEntity);                
    	}
    	setValue(null);
    }
    
	/**
     * @return
     */
    public EPaymentMethod getSelectedEntity() {
        return valueMap.get(getValue());
    }

    /**
     * @return
     */
    public void setSelectedEntity(EPaymentMethod paymentMethod) {
    	if (paymentMethod != null) {
    		setValue(String.valueOf(paymentMethod.getId()));
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
    			EPaymentMethod entity = valueMap.get(iter.next());
    			if (entity.getCode() != null && entity.getCode().equals(code)) {
    				setValue(String.valueOf(entity.getId()));
    				break;
    			}
    		}
    	}
    }
    
    /**
     * Get values from map
     * @return
     */
    public LinkedHashMap<String, EPaymentMethod> getValueMap() {
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
    
    private List<EPaymentMethod> getValueByCategories() {
    	List<ECategoryPaymentMethod> categoryPaymentMethods = new ArrayList<>();
    	categoryPaymentMethods.add(ECategoryPaymentMethod.MAN);
    	categoryPaymentMethods.add(ECategoryPaymentMethod.AUT);
    	BaseRestrictions<EPaymentMethod> restrictions = new BaseRestrictions<>(EPaymentMethod.class);
    	restrictions.addCriterion(Restrictions.in("categoryPaymentMethod", categoryPaymentMethods));
    	List<EPaymentMethod> paymentMethods = entityService.list(restrictions);
    	return paymentMethods;
    }
}
