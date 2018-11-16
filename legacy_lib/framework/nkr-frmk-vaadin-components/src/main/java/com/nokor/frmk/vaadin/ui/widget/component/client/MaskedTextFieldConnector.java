package com.nokor.frmk.vaadin.ui.widget.component.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.nokor.frmk.vaadin.ui.widget.component.MaskedTextField;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.textfield.TextFieldConnector;
import com.vaadin.shared.ui.Connect;

/**
 * @author ky.nora
 */
@Connect(MaskedTextField.class)
public class MaskedTextFieldConnector extends TextFieldConnector {
	
	private static final long serialVersionUID = -3198401856877620501L;

	@Override
	protected Widget createWidget() {
		return GWT.create(MaskedTextFieldWidget.class);
	}
	
	@Override
	public MaskedTextFieldWidget getWidget() {
		return (MaskedTextFieldWidget) super.getWidget();
	}
	
	@Override
	public MaskedTextFieldState getState() {
		return (MaskedTextFieldState) super.getState();
	}
	
	@Override
	public void onStateChanged(StateChangeEvent stateChangeEvent) {
		getWidget().setMask(getState().mask);
		getWidget().setPlaceHolder(getState().placeHolder);
		super.onStateChanged(stateChangeEvent);
	}

}
