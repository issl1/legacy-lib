package com.nokor.efinance.gui.ui.panel.contract.asset.registration.popup;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * New operation pop up panel
 * @author uhout.cheng
 */
public class NewOperationPopup extends Window {

	/** */
	private static final long serialVersionUID = -3436296401728334788L;
	
	
	/**
	 * 
	 */
	public NewOperationPopup() {
		setCaption(I18N.message("new.operation"));
		setModal(true);
		setResizable(false);
		init();
	}
	
	/**
	 * 
	 * @param asset
	 */
	public void assignValues(Asset asset) {
		if (asset != null) {
		}
	}
	
	/**
	 * 
	 */
	private void init() {
		VerticalLayout contentLayout = new VerticalLayout(); 
		contentLayout.addComponent(getVerLayout());
	    
		setContent(contentLayout);
	}
	
	/**
	 * 
	 * @return
	 */
	private VerticalLayout getVerLayout() {
		VerticalLayout verLayout = new VerticalLayout();
		verLayout.setMargin(true);
		FormLayout frmLayout = ComponentLayoutFactory.getFormLayoutCaptionAlignLeft();
		verLayout.addComponent(frmLayout);
		return verLayout;
	}
	
}
