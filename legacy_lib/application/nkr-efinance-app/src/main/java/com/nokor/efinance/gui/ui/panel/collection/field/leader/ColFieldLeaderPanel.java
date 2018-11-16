package com.nokor.efinance.gui.ui.panel.collection.field.leader;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
/**
 * 
 * @author buntha.chea
 *
 */
public class ColFieldLeaderPanel extends VerticalLayout implements SelectedTabChangeListener {

	private static final long serialVersionUID = 1845015375864192670L;
	
	private TabSheet fieldLeaderTabSheet;
	
	private ColFieldLeaderTabInboxPanel inboxTabPanel;
	private ColFieldLeaderTabFlagPanel flagTabPanel;
	private ColFieldLeaderTabAssistPanel assistTabPanel;
	
	/**
	 * 
	 */
	public ColFieldLeaderPanel() {
		
		fieldLeaderTabSheet = new TabSheet();
		fieldLeaderTabSheet.addSelectedTabChangeListener(this);
		
		inboxTabPanel = new ColFieldLeaderTabInboxPanel();
		flagTabPanel = new ColFieldLeaderTabFlagPanel();
		assistTabPanel = new ColFieldLeaderTabAssistPanel();
		
		fieldLeaderTabSheet.addTab(inboxTabPanel, I18N.message("inbox"));
		fieldLeaderTabSheet.addTab(flagTabPanel, I18N.message("flag.request"));
		fieldLeaderTabSheet.addTab(assistTabPanel, I18N.message("assist.request"));
		
		addComponent(fieldLeaderTabSheet);
	}

	/**
	 * @see com.vaadin.ui.TabSheet.SelectedTabChangeListener#selectedTabChange(com.vaadin.ui.TabSheet.SelectedTabChangeEvent)
	 */
	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		if (fieldLeaderTabSheet.getSelectedTab() == inboxTabPanel) {
			inboxTabPanel.refresh();
		} else if (fieldLeaderTabSheet.getSelectedTab() == flagTabPanel) {
			flagTabPanel.refresh();
		} else if (fieldLeaderTabSheet.getSelectedTab() == assistTabPanel) {
			assistTabPanel.refresh();
		}
	}

}
