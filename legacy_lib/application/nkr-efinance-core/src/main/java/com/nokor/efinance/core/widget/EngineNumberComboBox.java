package com.nokor.efinance.core.widget;
import java.util.LinkedHashMap;
import java.util.List;

import com.nokor.efinance.core.asset.model.Asset;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;
import java.util.Iterator;
/**
 * @author ly.youhort
 */
public class EngineNumberComboBox extends ComboBox {
	
	private static final long serialVersionUID = 228046564757710688L;
		
	protected LinkedHashMap<String, Asset> valueMap = new LinkedHashMap<String, Asset>();
	private String representEmpty;
	 
	public EngineNumberComboBox(List<Asset> assets) {
		this(null, assets);
	}
    /**
     * @param caption
     * @param dealers
     * @param emptyLabel
     */
    public EngineNumberComboBox(final String caption, List<Asset> asset, String emptyLabel) {
        super(caption);
        representEmpty = emptyLabel;
        renderers(asset);
        setFilteringMode(FilteringMode.CONTAINS);
    }
    
    /**
     * Render the ComboBox
     * @param dealers
     */
    private void renderers(List<Asset> assets) {
    	valueMap.clear();
    	removeAllItems();
    	if (assets != null  && !assets.isEmpty()) {
        	if (representEmpty != null && !representEmpty.isEmpty()) {
        		addItem("[empty]");
        		setItemCaption("[empty]", representEmpty);
        		setNullSelectionAllowed(false);
        	}
        	for (Asset asset : assets) {
        		if(asset.getEngineNumber() != null){
        			addItem(asset.getId().toString());
        			setItemCaption(asset.getId().toString(), asset.getEngineNumber());
        			valueMap.put(asset.getId().toString(), asset);
        		}
    			
        	}
        }
    }
    
    /**
     * Set the dealers to the ComboBox and remove any previous dealers.
     * @param dealers
     */
    public void setAssets (List<Asset> assets) {
    	renderers(assets);
    }
    
    /**
     * @param caption
     * @param dealers
     */
    public EngineNumberComboBox(final String caption, List<Asset> assets) {
    	this(caption, assets, null);
    }
    
	/**
     * @return
     */
    public Asset getSelectedEntity() {
        return valueMap.get(getValue());
    }

    /**
     * @return
     */
    public void setSelectedEntity(Asset asset) {
    	if (asset != null) {
    		setValue(asset.getId().toString());
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
    			Asset entity = valueMap.get(iter.next());
    			if (entity.getEngineNumber() != null && entity.getEngine().equals(code)) {
    				setValue(entity.getId().toString());
    				break;
    			}
    		}
    	}
    }
//    
    /**
     * Get values from map
     * @return
     */
    public LinkedHashMap<String, Asset> getValueMap() {
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



