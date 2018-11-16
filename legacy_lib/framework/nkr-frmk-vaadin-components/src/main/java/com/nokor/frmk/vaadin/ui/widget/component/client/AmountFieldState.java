package com.nokor.frmk.vaadin.ui.widget.component.client;

import com.vaadin.shared.ui.textfield.AbstractTextFieldState;

/**
 * @author ky.nora
 */
public class AmountFieldState extends AbstractTextFieldState {
	
	private static final long serialVersionUID = -4354576322933643135L;

	public char decimalSeparator = '.';
	
	public char groupingSeparator = ',';
	
	public String mask = "#,##0.00";

}
