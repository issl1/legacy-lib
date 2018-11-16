package com.nokor.efinance.gui.ui.panel.collection.inside.leader;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.gui.ui.panel.collection.inside.leader.assist.ColInsideRepoLeaderTabAssistPanel;
import com.nokor.efinance.gui.ui.panel.collection.inside.leader.flag.ColInsideRepoLeaderTabFlagPanel;
import com.nokor.efinance.gui.ui.panel.collection.inside.leader.inbox.ColInsideRepoLeaderTabInboxPanel;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.VerticalLayout;
/**
 * 
 * @author buntha.chea
 *
 */
public class ColInsideRepoLeaderPanel extends VerticalLayout implements SelectedTabChangeListener {

	private static final long serialVersionUID = 1845015375864192670L;
	
	private TabSheet leaderTabSheet;
	
	private ColInsideRepoLeaderTabInboxPanel inboxTabPanel;
	private ColInsideRepoLeaderTabFlagPanel flagTabPanel;
	private ColInsideRepoLeaderTabAssistPanel assistTabPanel;
	
	/**
	 * 
	 */
	public ColInsideRepoLeaderPanel() {
		
		leaderTabSheet = new TabSheet();
		leaderTabSheet.addSelectedTabChangeListener(this);
		
		inboxTabPanel = new ColInsideRepoLeaderTabInboxPanel();
		flagTabPanel = new ColInsideRepoLeaderTabFlagPanel();
		assistTabPanel = new ColInsideRepoLeaderTabAssistPanel();
		
		leaderTabSheet.addTab(inboxTabPanel, I18N.message("inbox"));
		leaderTabSheet.addTab(flagTabPanel, I18N.message("flag.request"));
		leaderTabSheet.addTab(assistTabPanel, I18N.message("assist.request"));
		
		addComponent(leaderTabSheet);
	}
	
	/**
	 * @see com.vaadin.ui.TabSheet.SelectedTabChangeListener#selectedTabChange(com.vaadin.ui.TabSheet.SelectedTabChangeEvent)
	 */
	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		if (leaderTabSheet.getSelectedTab() == inboxTabPanel) {
			inboxTabPanel.refresh();
		} else if (leaderTabSheet.getSelectedTab() == flagTabPanel) {
			flagTabPanel.refresh();
		} else if (leaderTabSheet.getSelectedTab() == assistTabPanel) {
			assistTabPanel.refresh();
		}
	}

}
