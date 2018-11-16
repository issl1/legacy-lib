package com.nokor.efinance.core.widget;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;

/**
 * @author ly.youhort
 */
public class DealerComboBox extends ComboBox {
	
	private static final long serialVersionUID = 228046564757710688L;
		
	protected LinkedHashMap<String, Dealer> valueMap = new LinkedHashMap<String, Dealer>();
	private String representEmpty;
	 
	public DealerComboBox(List<Dealer> dealers) {
		this(null, dealers);
    }
	
    /**
     * @param caption
     * @param dealers
     * @param emptyLabel
     */
    public DealerComboBox(final String caption, List<Dealer> dealers, String emptyLabel) {
        super(caption);
        representEmpty = emptyLabel;
        renderer(dealers);
        setFilteringMode(FilteringMode.CONTAINS);
    }
    
    /**
     * Render the ComboBox
     * @param dealers
     */
    private void renderer (List<Dealer> dealers) {
    	valueMap.clear();
    	removeAllItems();
    	if (dealers != null  && !dealers.isEmpty()) {
        	if (representEmpty != null && !representEmpty.isEmpty()) {
        		addItem("[empty]");
        		setItemCaption("[empty]", representEmpty);
        		setNullSelectionAllowed(false);
        	}
        	for (Dealer dealer : dealers) {
        		addItem(dealer.getId().toString());
        		setItemCaption(dealer.getId().toString(), I18N.isEnglishLocale() ? dealer.getNameEn() : dealer.getName());
        		valueMap.put(dealer.getId().toString(), dealer);                
        	}
        }
    }
    
    /**
     * Set the dealers to the ComboBox and remove any previous dealers.
     * @param dealers
     */
    public void setDealers (List<Dealer> dealers) {
    	renderer(dealers);
    }
    
    /**
     * @param caption
     * @param dealers
     */
    public DealerComboBox(final String caption, List<Dealer> dealers) {
    	this(caption, dealers, null);
    }
    
	/**
     * @return
     */
    public Dealer getSelectedEntity() {
        return valueMap.get(getValue());
    }

    /**
     * @return
     */
    public void setSelectedEntity(Dealer dealer) {
    	if (dealer != null) {
    		setValue(dealer.getId().toString());
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
    			Dealer entity = valueMap.get(iter.next());
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
    public LinkedHashMap<String,Dealer> getValueMap() {
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
