package com.nokor.efinance.gui.ui.panel.dashboard;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;

/**
 * INBOX Staff Phone Panel 
 * @author uhout.cheng
 */
public class InboxStaffCmPanel extends AbstractControlPanel implements FinServicesHelper {
	
	/**
	 */
	private static final long serialVersionUID = 5413776616003795604L;

	private TabSheet tabDashbord;
	
	private InboxReceivedPanel receivedPanel;
	private InboxContractsPanel unprocessedInboxPanel;
	private InboxContractsPanel blockedInboxPanel;
	private InboxContractsPanel holdedInboxPanel;
	private InboxProcessedPanel processedInboxPanel;
	
	/**
	 * 
	 * @param dashboardPanel
	 */
	public InboxStaffCmPanel() {
		
		tabDashbord = new TabSheet();
		
		receivedPanel = new InboxReceivedPanel();
		unprocessedInboxPanel = new InboxContractsPanel(new EWkfStatus[] {ContractWkfStatus.PEN}, new EWkfStatus[] {ContractWkfStatus.PEN_TRAN});
		blockedInboxPanel = new InboxContractsPanel(new EWkfStatus[] {ContractWkfStatus.BLOCKED}, new EWkfStatus[] {ContractWkfStatus.BLOCKED_TRAN});
		holdedInboxPanel = new InboxContractsPanel(new EWkfStatus[] {ContractWkfStatus.HOLD_PAY}, null);
		processedInboxPanel = new InboxProcessedPanel();
		
		tabDashbord.addTab(receivedPanel, I18N.message("book.contracts"));
		tabDashbord.addTab(unprocessedInboxPanel, I18N.message("unprocessed"));
		tabDashbord.addTab(blockedInboxPanel, I18N.message("blocked"));
		tabDashbord.addTab(holdedInboxPanel, I18N.message("hold"));
		tabDashbord.addTab(processedInboxPanel, I18N.message("processed"));
		
		tabDashbord.setSelectedTab(receivedPanel);

		tabDashbord.addSelectedTabChangeListener(new SelectedTabChangeListener() {
			/** */
			private static final long serialVersionUID = 5893964693602218949L;
			
			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				if (tabDashbord.getSelectedTab() == unprocessedInboxPanel) {
					unprocessedInboxPanel.refresh();
				} else if (tabDashbord.getSelectedTab() == blockedInboxPanel) {
					blockedInboxPanel.refresh();
				} else if (tabDashbord.getSelectedTab() == holdedInboxPanel) {
					holdedInboxPanel.refresh();
				} else if (tabDashbord.getSelectedTab() == receivedPanel) {
					receivedPanel.assignValues();
				} 
			} 
		});
		
		receivedPanel.assignValues();
		
		addComponent(tabDashbord);
	}
	
	
}
