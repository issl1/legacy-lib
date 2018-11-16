package com.nokor.efinance.core.widget;
import java.util.LinkedHashMap;
import java.util.List;

import com.gl.finwiz.share.domain.AP.BankBranchDTO;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;


/**
 * 
 * @author uhout.cheng
 */
public class BankBranchComboBox extends ComboBox {
	
	/** */
	private static final long serialVersionUID = -1259672080597272178L;
	
	protected LinkedHashMap<String, BankBranchDTO> valueMap = new LinkedHashMap<String, BankBranchDTO>();
	private String representEmpty;
	 
	/**
	 * 
	 * @param caption
	 */
	public BankBranchComboBox() {
		setFilteringMode(FilteringMode.CONTAINS);
	}
	
	/**
	 * 
	 * @param bankBranches
	 */
	public BankBranchComboBox(List<BankBranchDTO> bankBranches) {
		this(null, bankBranches);
	}
	
    /**
     * @param caption
     * @param dealers
     * @param emptyLabel
     */
    public BankBranchComboBox(final String caption, List<BankBranchDTO> bankBranches, String emptyLabel) {
        super(caption);
        representEmpty = emptyLabel;
        renderers(bankBranches);
        setFilteringMode(FilteringMode.CONTAINS);
    }
    
    /**
     * Render the ComboBox
     * @param bankBranches
     */
    private void renderers(List<BankBranchDTO> bankBranches) {
    	valueMap.clear();
    	removeAllItems();
    	if (bankBranches != null  && !bankBranches.isEmpty()) {
        	if (representEmpty != null && !representEmpty.isEmpty()) {
        		addItem("[empty]");
        		setItemCaption("[empty]", representEmpty);
        		setNullSelectionAllowed(false);
        	}
        	for (BankBranchDTO bankBranch : bankBranches) {
        		if(bankBranch.getName() != null){
        			addItem(bankBranch.getId().toString());
        			setItemCaption(bankBranch.getId().toString(), bankBranch.getName());
        			valueMap.put(bankBranch.getId().toString(), bankBranch);
        		}
    			
        	}
        }
    }
    
    /**
     * 
     * @param bankBranches
     */
    public void setBankBranches(List<BankBranchDTO> bankBranches) {
    	renderers(bankBranches);
    }
    
    /**
     * @param caption
     * @param dealers
     */
    public BankBranchComboBox(final String caption, List<BankBranchDTO> bankBranches) {
    	this(caption, bankBranches, null);
    }
    
	/**
     * @return
     */
    public BankBranchDTO getSelectedEntity() {
        return valueMap.get(getValue());
    }

    /**
     * 
     * @param bankBranch
     */
    public void setSelectedEntity(BankBranchDTO bankBranch) {
    	if (bankBranch != null) {
    		setValue(bankBranch.getId().toString());
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
    public LinkedHashMap<String, BankBranchDTO> getValueMap() {
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