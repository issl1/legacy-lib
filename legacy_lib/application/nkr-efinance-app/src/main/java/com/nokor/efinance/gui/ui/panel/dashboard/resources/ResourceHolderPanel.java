package com.nokor.efinance.gui.ui.panel.dashboard.resources;

import org.seuksa.frmk.i18n.I18N;

import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author uhout.cheng
 */
public class ResourceHolderPanel extends VerticalLayout {
	
	/** */
	private static final long serialVersionUID = 4572864980674417733L;
	
	private ResourceTablePanel tablePanel;
	
	/**
	 * 
	 */
	public ResourceHolderPanel() {
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		setCaption(I18N.message("resources"));
		createForm();
	}
	
	/**
	 * 
	 */
	private void createForm() {
		tablePanel = new ResourceTablePanel();
		addComponent(tablePanel);
	}
}
