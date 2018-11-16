package com.nokor.frmk.vaadin.ui.widget.component.client.mask;

/**
 * @author ky.nora
 */
public class SignMask extends AbstractMask {
	
	public boolean isValid(char character) {
		return character == '-' || character == '+';
	}
}