package com.nokor.frmk.vaadin.ui.widget.component.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.nokor.frmk.vaadin.ui.widget.component.NumberField;
import com.vaadin.client.ui.textfield.TextFieldConnector;
import com.vaadin.shared.ui.Connect;

/**
 * @author ky.nora
 */
@Connect(NumberField.class)
public class NumberFieldConnector extends TextFieldConnector {

	private static final long serialVersionUID = -9091659482017429365L;

	@Override
	protected Widget createWidget() {
		return GWT.create(NumberFieldWidget.class);
	}
	
	@Override
	public NumberFieldWidget getWidget() {
		return (NumberFieldWidget) super.getWidget();
	}
	
}

