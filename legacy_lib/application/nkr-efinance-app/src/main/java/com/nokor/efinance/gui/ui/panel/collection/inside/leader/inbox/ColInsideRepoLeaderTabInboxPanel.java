package com.nokor.efinance.gui.ui.panel.collection.inside.leader.inbox;

import com.nokor.efinance.core.common.IProfileCode;
import com.nokor.efinance.gui.ui.panel.collection.inside.leader.ColInsideRepoLeaderFilterPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.filter.CollectionContractTablePanel;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author buntha.chea
 *
 */
public class ColInsideRepoLeaderTabInboxPanel extends VerticalLayout {

	private static final long serialVersionUID = 4159287333510164015L;
	
	private ColInsideRepoLeaderFilterPanel leaderFilterPanel;
	private CollectionContractTablePanel leaderTablePanel;
	
	public ColInsideRepoLeaderTabInboxPanel() {
		setSpacing(true);
		setMargin(true);
		
		leaderTablePanel = new CollectionContractTablePanel(null);
		leaderFilterPanel = new ColInsideRepoLeaderFilterPanel(leaderTablePanel, IProfileCode.COL_INS_STAFF);
		
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
