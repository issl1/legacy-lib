package com.nokor.frmk.vaadin.ui.widget.combo;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.OptionGroup;

/**
 * 
 * @author prasnar
 *
 */
public class SimpleOptionGroup extends OptionGroup {
	/** */
	private static final long serialVersionUID = -5802053168679228160L;
	
	private static final String DEFAULT_STYLE = "horizontal";
	
	protected List<?> values;
	protected List<String> descriptions;
	
	/**
	 * 
	 */
	protected SimpleOptionGroup() {
		super();
	}
	
	/**
	 * 
	 * @param values
	 */
	public SimpleOptionGroup(List<?> values) {
		this(values, null);
	}

	/**
	 * 
	 * @param values
	 * @param descriptions
	 */
	public SimpleOptionGroup(List<?> values, List<String> descriptions) {
		this.values = values;
		this.descriptions = descriptions;
		init();
	}


	/**
	 * 
	 * @param values
	 * @param descriptions
	 */
	protected void init() {
		if (this.descriptions == null) {
			this.descriptions = new ArrayList<String>();
			for (Object code : this.values) {
				this.descriptions.add(code.toString());
			}
		}
		build();
	}

	/**
	 * 
	 * @return
	 */
	protected void build() {
		for (int i = 0; i < values.size(); i++) {
			addItem(values.get(i));
			setItemCaption(values.get(i) , descriptions.get(i) );
		}
		setMultiSelect(true);
		setNullSelectionAllowed(false);
		setValue(values);
		addStyleName(DEFAULT_STYLE);
	}
}

