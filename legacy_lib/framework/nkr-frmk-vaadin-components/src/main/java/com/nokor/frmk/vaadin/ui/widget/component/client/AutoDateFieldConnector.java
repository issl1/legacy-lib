package com.nokor.frmk.vaadin.ui.widget.component.client;

import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.UIDL;
import com.vaadin.client.ui.datefield.PopupDateFieldConnector;
import com.vaadin.shared.ui.Connect;
import com.vaadin.shared.ui.datefield.PopupDateFieldState;

/**
 * @author ky.nora
 */
@Connect(AutoDateField.class)
public class AutoDateFieldConnector extends PopupDateFieldConnector {
	
	private static final long serialVersionUID = 6333559619108522684L;
	
    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        super.updateFromUIDL(uidl, client);
        getWidget().mask = uidl.getStringAttribute("mask");
    }
    
    @Override
    public AutoDateFieldWidget getWidget() {
        return (AutoDateFieldWidget) super.getWidget();
    }
    
    @Override
    public PopupDateFieldState getState() {
        return (PopupDateFieldState) super.getState();
    }
    
}

