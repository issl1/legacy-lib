package com.nokor.efinance.gui.ui.panel.collection.field.leader;

import com.nokor.efinance.core.common.IProfileCode;
import com.nokor.efinance.gui.ui.panel.collection.phone.filter.CollectionContractTablePanel;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author buntha.chea
 *
 */
public class ColFieldLeaderTabInboxPanel extends VerticalLayout {

	private static final long serialVersionUID = 4159287333510164015L;
	
	private ColFieldLeaderInboxFilterPanel leaderFilterPanel;
	private CollectionContractTablePanel leaderTablePanel;
	
	public ColFieldLeaderTabInboxPanel() {
		setSpacing(true);
		setMargin(true);
		
		leaderTablePanel = new CollectionContractTablePanel(null);
		leaderFilterPanel = new ColFieldLeaderInboxFilterPanel(leaderTablePanel, IProfileCode.COL_FIE_STAFF);
		
		leaderTablePanel.refresh(leaderFilterPanel.getRestrictions());
		
		addComponent(leaderFilterPanel);
		addComponent(leaderTablePanel);
		
	}
	/**
	 * Refresh
	 */
	public void refresh() {
		leaderTablePanel.refresh(leaderFilterPanel.getRestrictions());
	}
}
