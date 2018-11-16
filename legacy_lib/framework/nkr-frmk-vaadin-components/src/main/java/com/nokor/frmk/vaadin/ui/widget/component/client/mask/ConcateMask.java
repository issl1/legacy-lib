package com.nokor.frmk.vaadin.ui.widget.component.client.mask;

/**
 * @author ky.nora
 */
public interface ConcateMask {
	boolean isValid(char character, String concate);
	char getChar(char character);
}
