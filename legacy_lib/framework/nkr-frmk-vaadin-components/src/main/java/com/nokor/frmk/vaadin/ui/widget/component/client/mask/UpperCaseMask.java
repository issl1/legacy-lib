package com.nokor.frmk.vaadin.ui.widget.component.client.mask;

/**
 * @author ky.nora
 */
public class UpperCaseMask implements Mask {
	
	public boolean isValid(char character) {
		return Character.isLetter(getChar(character));
	}
	
	public char getChar(char character) {
		return Character.toUpperCase(character);
	}
}
