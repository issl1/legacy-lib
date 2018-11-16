package com.nokor.efinance.core.address.panel;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.ersys.core.hr.model.address.Address;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;

/**
 * 
 * @author prasnar
 *
 */
public class AddressComboBox extends ComboBox implements FinServicesHelper {
	/** */
	private static final long serialVersionUID = 2571585420723867600L;

	private static final String EMPTY = "[empty]";
	private String representEmpty;
	private LinkedHashMap<String, Address> valueMap = new LinkedHashMap<>();

	public AddressComboBox() {
		this.representEmpty = "";
        setFilteringMode(FilteringMode.CONTAINS);
	}
			
	/**
	 * @param caption
	 * @param entities
	 * @param emptyLabel
	 */
	public AddressComboBox(String caption, String emptyLabel) {
        super(caption);
        this.representEmpty = emptyLabel;
        setFilteringMode(FilteringMode.CONTAINS);
    }
	
	
	/**
	 * 
	 * @param addresses
	 */
	public void renderer (List<Address> addresses) {
		clear();
		if (addresses != null  && !addresses.isEmpty()) {
			if (representEmpty != null && !representEmpty.isEmpty()) {
				addItem(EMPTY);
				setItemCaption(EMPTY, representEmpty);
				setNullSelectionAllowed(false);
			}
			for (Address address : addresses) {
				addItem(address.getId().toString());
				setItemCaption(address.getId().toString(), ADDRESS_SRV.getDetailAddress(address));
				valueMap.put(address.getId().toString(), address);
			}
		}
	 }
	 
	 /**
	  * 
	  * @param addresses
	  */
	 public void setEntities (List<Address> addresses) {
	    renderer(addresses);
	 }
    
 
	    
	/**
     * @return
     */
	public Address getSelectedEntity() {
	    return valueMap.get(getValue());
	}
	
	/**
     * @return
     */
    public void setSelectedEntity(Address address) {
    	if (address != null) {
    		setValue(address.getId().toString());
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
			Address address = valueMap.get(iter.next());
			if (address.getId() != null && address.getId().equals(id)) {
				setValue(address.getId().toString());
				break;
			}
		}
    	
    }
    
    /**
     * Get values from map
     * @return
     */
    public LinkedHashMap<String, Address> getValueMap() {
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
