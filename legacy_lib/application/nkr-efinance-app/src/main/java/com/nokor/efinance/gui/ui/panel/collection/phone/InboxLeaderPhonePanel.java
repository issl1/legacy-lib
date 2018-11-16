package com.nokor.efinance.gui.ui.panel.collection.phone;

import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.gui.ui.panel.collection.LeaderColAssistContractsPanel;
import com.nokor.efinance.gui.ui.panel.collection.LeaderColFlagContractsPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.leader.inbox.ColPhoneLeaderInboxTabPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;

/**
 * INBOX leader Phone Panel 
 * @author uhout.cheng
 */
public class InboxLeaderPhonePanel extends AbstractControlPanel implements FinServicesHelper, SelectedTabChangeListener {
	
	/** */
	private static final long serialVersionUID = 9187261039959045395L;
	
	private TabSheet tabLeader;
	
	private ColPhoneLeaderInboxTabPanel colPhoneLeaderInboxTabPanel;
	private LeaderColFlagContractsPanel flagContractsPanel; 
	private LeaderColAssistContractsPanel assistContractsPanel;
	
	/** 
	 * @param dashboardPanel
	 */
	public InboxLeaderPhonePanel() {
		
		tabLeader = new TabSheet();
		tabLeader.addSelectedTabChangeListener(this);
	
		colPhoneLeaderInboxTabPanel = new ColPhoneLeaderInboxTabPanel();
		flagContractsPanel = new LeaderColFlagContractsPanel();
		assistContractsPanel = new LeaderColAssistContractsPanel();
		
		tabLeader.addTab(colPhoneLeaderInboxTabPanel, I18N.message("inbox"));
		tabLeader.addTab(flagContractsPanel, I18N.message("flag.request"));
		tabLeader.addTab(assistContractsPanel, I18N.message("assist.request"));
		
		tabLeader.setSelectedTab(colPhoneLeaderInboxTabPanel);
		
		addComponent(tabLeader);
	}

	/**
	 * @see com.vaadin.ui.TabSheet.SelectedTabChangeListener#selectedTabChange(com.vaadin.ui.TabSheet.SelectedTabChangeEvent)
	 */
	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		if (tabLeader.getSelectedTab() == flagContractsPanel) {
			flagContractsPanel.refresh();
		} else if (tabLeader.getSelectedTab() == assistContractsPanel) {
			assistContractsPanel.refresh();
		} else if (tabLeader.getSelectedTab() == colPhoneLeaderInboxTabPanel) {
			colPhoneLeaderInboxTabPanel.refresh();
		}
	}
	
}
