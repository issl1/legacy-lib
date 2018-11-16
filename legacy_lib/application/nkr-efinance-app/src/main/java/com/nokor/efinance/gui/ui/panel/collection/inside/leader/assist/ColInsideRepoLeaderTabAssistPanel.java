package com.nokor.efinance.gui.ui.panel.collection.inside.leader.assist;

import com.nokor.efinance.core.common.IProfileCode;
import com.nokor.efinance.gui.ui.panel.collection.phone.filter.CollectionContractTablePanel;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author buntha.chea
 *
 */
public class ColInsideRepoLeaderTabAssistPanel extends VerticalLayout {

	private static final long serialVersionUID = 4159287333510164015L;
	
	private ColInsideRepoLeaderAssistFilterPanel flagFilterPanel;
	private CollectionContractTablePanel leaderTablePanel;
	
	public ColInsideRepoLeaderTabAssistPanel() {
		setSpacing(true);
		setMargin(true);
		
		leaderTablePanel = new CollectionContractTablePanel(false);
		flagFilterPanel = new ColInsideRepoLeaderAssistFilterPanel(leaderTablePanel, IProfileCode.COL_INS_STAFF);
		
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
