package com.nokor.efinance.core.widget;
import java.util.LinkedHashMap;
import java.util.List;

import com.gl.finwiz.share.domain.AP.BankDTO;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;


/**
 * 
 * @author uhout.cheng
 */
public class BankComboBox extends ComboBox {
	
	/** */
	private static final long serialVersionUID = -8194074879469667477L;
	
	protected LinkedHashMap<String, BankDTO> valueMap = new LinkedHashMap<String, BankDTO>();
	private String representEmpty;
	 
	/**
	 * 
	 * @param caption
	 */
	public BankComboBox() {
		setFilteringMode(FilteringMode.CONTAINS);
	}
	
	/**
	 * 
	 * @param banks
	 */
	public BankComboBox(List<BankDTO> banks) {
		this(null, banks);
	}
	
    /**
     * @param caption
     * @param dealers
     * @param emptyLabel
     */
    public BankComboBox(final String caption, List<BankDTO> banks, String emptyLabel) {
        super(caption);
        representEmpty = emptyLabel;
        renderers(banks);
        setFilteringMode(FilteringMode.CONTAINS);
    }
    
    /**
     * Render the ComboBox
     * @param banks
     */
    private void renderers(List<BankDTO> banks) {
    	valueMap.clear();
    	removeAllItems();
    	if (banks != null  && !banks.isEmpty()) {
        	if (representEmpty != null && !representEmpty.isEmpty()) {
        		addItem("[empty]");
        		setItemCaption("[empty]", representEmpty);
        		setNullSelectionAllowed(false);
        	}
        	for (BankDTO bank : banks) {
        		if(bank.getName() != null){
        			addItem(bank.getId().toString());
        			setItemCaption(bank.getId().toString(), bank.getName());
        			valueMap.put(bank.getId().toString(), bank);
        		}
    			
        	}
        }
    }
    
    /**
     * 
     * @param banks
     */
    public void setBankBranches(List<BankDTO> banks) {
    	renderers(banks);
    }
    
    /**
     * @param caption
     * @param dealers
     */
    public BankComboBox(final String caption, List<BankDTO> banks) {
    	this(caption, banks, null);
    }
    
	/**
     * @return
     */
    public BankDTO getSelectedEntity() {
        return valueMap.get(getValue());
    }

    /**
     * 
     * @param bank
     */
    public void setSelectedEntity(BankDTO bank) {
    	if (bank != null) {
    		setValue(bank.getId().toString());
    	} else {
    		if (representEmpty != null && !representEmpty.isEmpty()) {
    			setValue("[empty]");
    		} else {
    			setValue(null);
    		}
    	}
    }
        
    /**
     * Get values from map
     * @return
     */
    public LinkedHashMap<String, BankDTO> getValueMap() {
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