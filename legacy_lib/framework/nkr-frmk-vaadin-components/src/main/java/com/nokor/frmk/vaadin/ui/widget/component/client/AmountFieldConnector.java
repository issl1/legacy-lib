package com.nokor.frmk.vaadin.ui.widget.component.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.nokor.frmk.vaadin.ui.widget.component.AmountField;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.textfield.TextFieldConnector;
import com.vaadin.shared.ui.Connect;

/**
 * @author ky.nora
 */
@Connect(AmountField.class)
public class AmountFieldConnector extends TextFieldConnector {

	private static final long serialVersionUID = 5006927386741732267L;

	@Override
	protected Widget createWidget() {
		return GWT.create(AmountFieldWidget.class);
	}
	
	@Override
	public AmountFieldWidget getWidget() {
		return (AmountFieldWidget) super.getWidget();
	}

	@Override
	public AmountFieldState getState() {
		return (AmountFieldState) super.getState();
	}

	@Override
	public void onStateChanged(StateChangeEvent stateChangeEvent) {
		getWidget().setGroupingSeparator(getState().groupingSeparator);
		getWidget().setDecimalSeparator(getState().decimalSeparator);
		getWidget().setMask(getState().mask);
		super.onStateChanged(stateChangeEvent);
	}
	
}
