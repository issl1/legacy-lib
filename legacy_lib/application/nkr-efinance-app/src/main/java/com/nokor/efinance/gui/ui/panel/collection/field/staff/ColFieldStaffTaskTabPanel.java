package com.nokor.efinance.gui.ui.panel.collection.field.staff;

import com.nokor.efinance.gui.ui.panel.collection.phone.filter.CollectionContractTablePanel;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author buntha.chea
 *
 */
public class ColFieldStaffTaskTabPanel extends VerticalLayout {

	private static final long serialVersionUID = 6533329742680497433L;
	
	private ColFieldStaffFilterPanel filterPanel;
	private CollectionContractTablePanel tablePanel;
	
	public ColFieldStaffTaskTabPanel() {
		setSpacing(true);
		setMargin(true);
		
		tablePanel = new CollectionContractTablePanel(null);
		filterPanel = new ColFieldStaffFilterPanel(tablePanel);

		tablePanel.refresh(filterPanel.getRestrictions());
	
		addComponent(filterPanel);
		addComponent(tablePanel);
	}

}
