package com.nokor.frmk.vaadin.ui.widget.component.client.mask;

/**
 * @author ky.nora
 */
public class HexMask implements Mask {
	public boolean isValid(char character) {
		return ((character == '0' || character == '1' || character == '2' || character == '3' || character == '4'
				|| character == '5' || character == '6' || character == '7' || character == '8' || character == '9'
				|| character == 'a' || character == 'A' || character == 'b' || character == 'B' || character == 'c'
				|| character == 'C' || character == 'd' || character == 'D' || character == 'e' || character == 'E'
				|| character == 'f' || character == 'F'));
	}

	public char getChar(char character) {
		if (Character.isDigit(character)) {
			return character;
		}
		return Character.toUpperCase(character);
	}
}