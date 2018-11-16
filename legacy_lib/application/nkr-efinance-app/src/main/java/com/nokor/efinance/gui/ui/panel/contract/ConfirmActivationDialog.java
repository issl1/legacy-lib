package com.nokor.efinance.gui.ui.panel.contract;

import org.seuksa.frmk.i18n.I18N;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * @author ly.youhort
 */
public class ConfirmActivationDialog extends Window {
	
    /** */
	private static final long serialVersionUID = 7837382885463743029L;
	

	private CheckBox cbDisburse;
	
	private VerticalLayout contentLayout;
	
	/**
	 * 
	 * @param listener
	 */
	public ConfirmActivationDialog(final ClickListener listener) {
		
		setCaption(I18N.message("information"));
		setModal(true);
		setResizable(false);
		setWidth(300, Unit.PIXELS);
		
		contentLayout = new VerticalLayout();
		contentLayout.setMargin(true);
		contentLayout.setSpacing(true);

        Button btnActivate = new NativeButton(I18N.message("activate"));
        btnActivate.addClickListener(listener);
        btnActivate.setIcon(FontAwesome.CHECK);
        		
		Button btnCancel = new NativeButton(I18N.message("cancel"), new Button.ClickListener() {
			private static final long serialVersionUID = 3975121141565713259L;
			public void buttonClick(ClickEvent event) {
            	close();
            }
        });
		btnCancel.setIcon(FontAwesome.TIMES);
		
		
		cbDisburse = new CheckBox(I18N.message("disburse"));
		Label lblDescreption = new Label(I18N.message("confirmation"));
	
		HorizontalLayout buttonsLayout = new HorizontalLayout();
		buttonsLayout.addComponent(btnActivate);
		buttonsLayout.addComponent(btnCancel);
	
		VerticalLayout verLayout = new VerticalLayout();
		verLayout.addComponent(buttonsLayout);
		verLayout.setComponentAlignment(buttonsLayout, Alignment.BOTTOM_CENTER);
		
		contentLayout.addComponent(lblDescreption);
		contentLayout.addComponent(cbDisburse);
		contentLayout.addComponent(verLayout);
	
        setContent(contentLayout);
        
        UI.getCurrent().addWindow(this);
	}
	
	/**
	 * @return
	 */
	public boolean isDisburce() {
		return cbDisburse.getValue();
	}
	
	/**
	 * @param listener
	 */
	public static ConfirmActivationDialog show(final ClickListener listener) {
		return new ConfirmActivationDialog(listener);
	}
}
