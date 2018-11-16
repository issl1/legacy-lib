package com.nokor.efinance.gui.ui.panel.collection.phone.filter;

import com.vaadin.ui.VerticalLayout;
/**
 * 
 * @author buntha.chea
 *
 */
public class CollectionTaskTabPanel extends VerticalLayout {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3702749301987322399L;
	
	private ColFilterPanel filterPanel;
	private CollectionContractTablePanel tablePanel;

	public CollectionTaskTabPanel() {
		setSpacing(true);
		setMargin(true);
		
		tablePanel = new CollectionContractTablePanel(null);
		filterPanel = new ColFilterPanel(tablePanel);
		
		tablePanel.setRestriction(filterPanel.getRestrictions());
		tablePanel.refresh(filterPanel.getRestrictions());
		
		addComponent(filterPanel);
		addComponent(tablePanel);
	}

}
