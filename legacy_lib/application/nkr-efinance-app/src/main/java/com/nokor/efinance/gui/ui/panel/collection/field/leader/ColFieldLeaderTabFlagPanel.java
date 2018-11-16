package com.nokor.efinance.gui.ui.panel.collection.field.leader;

import com.nokor.efinance.core.common.IProfileCode;
import com.nokor.efinance.gui.ui.panel.collection.phone.filter.CollectionContractTablePanel;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author buntha.chea
 *
 */
public class ColFieldLeaderTabFlagPanel extends VerticalLayout {

	private static final long serialVersionUID = 4159287333510164015L;
	
	private ColFieldLeaderFlagFilterPanel flagFilterPanel;
	private CollectionContractTablePanel leaderTablePanel;
	
	public ColFieldLeaderTabFlagPanel() {
		setSpacing(true);
		setMargin(true);
		
		leaderTablePanel = new CollectionContractTablePanel(true);
		flagFilterPanel = new ColFieldLeaderFlagFilterPanel(leaderTablePanel, IProfileCode.COL_FIE_STAFF);
		
		addComponent(flagFilterPanel);
		addComponent(leaderTablePanel);
	}
	
	/**
	 * Refresh
	 */
	public void refresh() {
		leaderTablePanel.refresh(flagFilterPanel.getRestrictions());
	}
	
}
