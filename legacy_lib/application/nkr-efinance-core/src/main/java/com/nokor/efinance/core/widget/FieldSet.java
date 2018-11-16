package com.nokor.efinance.core.widget;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;

/**
 * @author bunlong.taing
 */
public class FieldSet extends CustomLayout {

	/** */
	private static final long serialVersionUID = 3814626048258497024L;
	
	private static final String TEMPLATE = "<fieldset style=\"padding-right: 2px; padding-left: 2px; padding-top: 0;\">"
			+ "<legend><b location=\"caption\"></b></legend>"
			+ "<div location=\"content\"></div>"
			+ "</fieldset>";
	
	/** */
	public FieldSet() {
		setTemplateContents(TEMPLATE);
	}
	
	/**
	 * Set Legend
	 * @param legend
	 */
	public void setLegend(String legend) {
		Label lbl = new Label(legend);
		addComponent(lbl, "caption");
	}
	
	/**
	 * Set content
	 * @param content
	 */
	public void setContent(Component content) {
		addComponent(content, "content");
	}

}
