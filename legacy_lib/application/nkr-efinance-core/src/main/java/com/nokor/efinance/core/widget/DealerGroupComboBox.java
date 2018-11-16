package com.nokor.efinance.core.widget;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import com.nokor.efinance.core.dealer.model.DealerGroup;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;

/**
 * 
 * @author uhout.cheng
 */
public class DealerGroupComboBox extends ComboBox {
	
	/** */
	private static final long serialVersionUID = -2629648814028480308L;
	
	protected LinkedHashMap<String, DealerGroup> valueMap = new LinkedHashMap<String, DealerGroup>();
	private String representEmpty;
	 
	/**
	 * 
	 * @param dealerGroups
	 */
	public DealerGroupComboBox(List<DealerGroup> dealerGroups) {
		this(null, dealerGroups);
    }
	
    /**
     * @param caption
     * @param dealerGroups
     * @param emptyLabel
     */
    public DealerGroupComboBox(final String caption, List<DealerGroup> dealerGroups, String emptyLabel) {
        super(caption);
        representEmpty = emptyLabel;
        renderer(dealerGroups);
        setFilteringMode(FilteringMode.CONTAINS);
    }
    
    /**
     * Render the ComboBox
     * @param dealerGroups
     */
    private void renderer (List<DealerGroup> dealerGroups) {
    	valueMap.clear();
    	removeAllItems();
    	if (dealerGroups != null  && !dealerGroups.isEmpty()) {
        	if (representEmpty != null && !representEmpty.isEmpty()) {
        		addItem("[empty]");
        		setItemCaption("[empty]", representEmpty);
        		setNullSelectionAllowed(false);
        	}
        	for (DealerGroup dealerGroup : dealerGroups) {
        		addItem(dealerGroup.getId().toString());
        		setItemCaption(dealerGroup.getId().toString(), dealerGroup.getDescLocale());
        		valueMap.put(dealerGroup.getId().toString(), dealerGroup);                
        	}
        }
    }
    
    /**
     * Set the dealers to the ComboBox and remove any previous dealers.
     * @param dealerGroups
     */
    public void setDealers (List<DealerGroup> dealerGroups) {
    	renderer(dealerGroups);
    }
    
    /**
     * @param caption
     * @param dealerGroups
     */
    public DealerGroupComboBox(final String caption, List<DealerGroup> dealerGroups) {
    	this(caption, dealerGroups, null);
    }
    
	/**
     * @return
     */
    public DealerGroup getSelectedEntity() {
        return valueMap.get(getValue());
    }

    /**
     * @return
     */
    public void setSelectedEntity(DealerGroup dealerGroup) {
    	if (dealerGroup != null) {
    		setValue(dealerGroup.getId().toString());
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
    			DealerGroup entity = valueMap.get(iter.next());
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
    public LinkedHashMap<String, DealerGroup> getValueMap() {
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
