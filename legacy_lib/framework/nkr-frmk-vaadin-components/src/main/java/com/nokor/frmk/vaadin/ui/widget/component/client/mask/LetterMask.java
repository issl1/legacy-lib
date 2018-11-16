package com.nokor.frmk.vaadin.ui.widget.component.client.mask;

/**
 * @author ky.nora
 */
public class LetterMask extends AbstractMask {

	public boolean isValid(char character) {
		return Character.isLetter(character);
	}

}
