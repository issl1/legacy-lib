package com.nokor.frmk.vaadin.ui.widget.component.client.mask;

/**
 * @author ky.nora
 */
public class LowerCaseMask implements Mask {
	
	public boolean isValid(char character) {
		return Character.isLetter(getChar(character));
	}
	
	public char getChar(char character) {
		return Character.toLowerCase(character);
	}
}