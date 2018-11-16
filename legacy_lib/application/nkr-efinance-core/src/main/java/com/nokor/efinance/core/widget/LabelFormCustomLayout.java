package com.nokor.efinance.core.widget;

import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;

/**
 * 
 * @author uhout.cheng
 */
public class LabelFormCustomLayout extends CustomLayout {

	/** */
	private static final long serialVersionUID = -6738678795256491375L;

	private static final String TEMPLATE= "<table cellspacing=\"1\" cellpadding=\"1\" border=\"0\" >"
			+ "<tr valign=\"top\">"
			+ "<td align=\"left\"><div location=\"lblCaption\" /></td>"
			+ "<td>:</td>"
			+ "<td align=\"left\"><div location=\"lblValue\" /></td>"
			+ "</tr>"
			+ "</table>";
	
	private Label lblCaption;
	private Label lblValue;
	
	/**
	 * 
	 * @param caption
	 * @param value
	 */
	private void setComponentLayout(String caption, String value) {
		setTemplateContents(TEMPLATE);
		lblCaption = ComponentFactory.getLabel(caption);
		lblValue = ComponentFactory.getHtmlLabel("<b>" + value + "</b>");
		addComponent(lblCaption, "lblCaption");
		addComponent(lblValue, "lblValue");
	}
	
	/**
	 * 
	 * @param caption
	 * @param value
	 */
	public LabelFormCustomLayout(String caption, String value) {
		setComponentLayout(caption, value);
	}
	
	/**
	 * 
	 * @param caption
	 * @param value
	 * @param widthCaption
	 * @param widthValue
	 */
	public LabelFormCustomLayout(String caption, String value, float widthCaption, float widthValue) {
		setComponentLayout(caption, value);
		lblCaption.setWidth(widthCaption, Unit.PIXELS);
		lblValue.setWidth(widthValue, Unit.PIXELS);
	}

}
