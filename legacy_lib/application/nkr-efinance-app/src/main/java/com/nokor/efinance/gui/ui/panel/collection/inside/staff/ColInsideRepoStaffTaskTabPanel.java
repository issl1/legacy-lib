package com.nokor.efinance.gui.ui.panel.collection.inside.staff;

import com.nokor.efinance.gui.ui.panel.collection.phone.filter.CollectionContractTablePanel;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author buntha.chea
 *
 */
public class ColInsideRepoStaffTaskTabPanel extends VerticalLayout {

	private static final long serialVersionUID = 6533329742680497433L;
	
	private ColInsideRepoStaffFilterPanel filterPanel;
	private CollectionContractTablePanel tablePanel;
	
	public ColInsideRepoStaffTaskTabPanel() {
		setSpacing(true);
		setMargin(true);
		
		tablePanel = new CollectionContractTablePanel(null);
		filterPanel = new ColInsideRepoStaffFilterPanel(tablePanel);

		tablePanel.refresh(filterPanel.getRestrictions());
	
		addComponent(filterPanel);
		addComponent(tablePanel);
	}

}
