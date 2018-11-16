package com.nokor.frmk.vaadin.ui.widget.component.client.mask;

/**
 * @author ky.nora
 */
public interface Mask {
	boolean isValid(char character);
	char getChar(char character);
}
