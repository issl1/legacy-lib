package com.nokor.frmk.vaadin.ui.widget.component.client.mask;

/**
 * @author ky.nora
 */
public class YearMask extends AbstractConcateMask {
	
	public boolean isValid(char character, String concate) {
		if (!Character.isDigit(character)) {
			return false;
		}
		
		try {
			// no rule apply yet, just try check again
			Integer.parseInt(concate);
		} catch (Exception e) {
			return false;
		}	
		return true;
	}
}
