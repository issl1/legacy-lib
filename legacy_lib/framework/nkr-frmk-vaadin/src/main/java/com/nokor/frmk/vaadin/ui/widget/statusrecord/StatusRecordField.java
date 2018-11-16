package com.nokor.frmk.vaadin.ui.widget.statusrecord;

import org.seuksa.frmk.i18n.I18N;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;

public class StatusRecordField extends HorizontalLayout {

	/** */
	private static final long serialVersionUID = 2845065613642274095L;

	private CheckBox cbActive;
	private CheckBox cbInactive;
	
	public StatusRecordField() {
		super();
		setSpacing(true);
		
		cbActive = new CheckBox(I18N.message("active"));
        cbActive.setValue(true);
        
        cbInactive = new CheckBox(I18N.message("inactive"));
        cbInactive.setValue(false);
        
        addComponent(cbActive);
        addComponent(cbInactive);
	}
	
	public void clearValues() {
		cbActive.setValue(true);
		cbInactive.setValue(false);
	}
	
	public void setActiveValue(Boolean value) {
		cbActive.setValue(value);
	}
	
	public Boolean getActiveValue() {
		return cbActive.getValue();
	}
	
	public void getInactiveValue(Boolean value) {
		cbInactive.setValue(value);
	}
	
	public Boolean getInactiveValue() {
		return cbInactive.getValue();
	}
	
	public boolean isActiveAllValues() {
		return cbActive.getValue() && cbInactive.getValue();
	}
	
	public boolean isInactiveAllValues() {
		return !cbActive.getValue() && !cbInactive.getValue();
	}
}
