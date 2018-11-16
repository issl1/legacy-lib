package com.nokor.frmk.vaadin.ui.widget.component.client;

import com.vaadin.shared.ui.textfield.AbstractTextFieldState;

/**
 * @author ky.nora
 */
public class MaskedTextFieldState extends AbstractTextFieldState {
	
	private static final long serialVersionUID = 2096175139491590748L;

	/**
	 * The mask
	 */
	public String mask;
	
	/**
	 * A Placeholder
	 */
	public char placeHolder = '_';
	
}
