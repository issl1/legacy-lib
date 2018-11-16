package com.nokor.efinance.gui.ui.panel.collection.field.leader;

import com.nokor.efinance.core.common.IProfileCode;
import com.nokor.efinance.gui.ui.panel.collection.phone.filter.CollectionContractTablePanel;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author buntha.chea
 *
 */
public class ColFieldLeaderTabAssistPanel extends VerticalLayout {

	private static final long serialVersionUID = 4159287333510164015L;
	
	private ColFieldLeaderAssistFilterPanel assistFilterPanel;
	private CollectionContractTablePanel leaderTablePanel;
	
	public ColFieldLeaderTabAssistPanel() {
		setSpacing(true);
		setMargin(true);
		
		leaderTablePanel = new CollectionContractTablePanel(false);
		assistFilterPanel = new ColFieldLeaderAssistFilterPanel(leaderTablePanel, IProfileCode.COL_FIE_STAFF);
		
		addComponent(assistFilterPanel);
		addComponent(leaderTablePanel);
	}
	
	/**
	 * Refresh
	 */
	public void refresh() {
		leaderTablePanel.refresh(assistFilterPanel.getRestrictions());
	}

}
