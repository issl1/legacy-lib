package com.nokor.efinance.gui.ui.panel.inbox.collection.ar.batch;

import com.nokor.efinance.core.workflow.PaymentFileWkfStatus;
import com.nokor.efinance.gui.ui.panel.payment.blocked.BlockedPaymentDetailsPanel;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author buntha.chea
 *
 */
public class BatchTabPanel extends VerticalLayout implements SelectedTabChangeListener {
	
	/** */
	private static final long serialVersionUID = 944982895374471906L;
	
	private TabSheet tabSheet;
	private BatchFileTabPanel fileTabPanel;
	
	private BlockedPaymentDetailsPanel unIdentifiedDetailsPanel;
	private BlockedPaymentDetailsPanel unMatchedDetailsPanel;
	private BlockedPaymentDetailsPanel suspendedDetailsPanel;
	private BlockedPaymentDetailsPanel overDetailsPanel;
	private BlockedPaymentDetailsPanel matchedDetailsPanel;
	
	public BatchTabPanel() {
		setMargin(true);
		
		fileTabPanel = new BatchFileTabPanel();
		
		unIdentifiedDetailsPanel = new BlockedPaymentDetailsPanel(PaymentFileWkfStatus.UNIDENTIFIED);
		unMatchedDetailsPanel = new BlockedPaymentDetailsPanel(PaymentFileWkfStatus.UNMATCHED);
		suspendedDetailsPanel = new BlockedPaymentDetailsPanel(PaymentFileWkfStatus.SUSPENDED);
		overDetailsPanel = new BlockedPaymentDetailsPanel(PaymentFileWkfStatus.OVER);
		matchedDetailsPanel = new BlockedPaymentDetailsPanel(PaymentFileWkfStatus.MATCHED);
		
		tabSheet = new TabSheet();
		tabSheet.addSelectedTabChangeListener(this);
		tabSheet.addTab(fileTabPanel, I18N.message("file"));
		tabSheet.addTab(unIdentifiedDetailsPanel, I18N.message("unidentified"));
		tabSheet.addTab(unMatchedDetailsPanel, I18N.message("unmatched"));
		tabSheet.addTab(suspendedDetailsPanel, I18N.message("suspended"));
		tabSheet.addTab(overDetailsPanel, I18N.message("over"));
		tabSheet.addTab(matchedDetailsPanel, I18N.message("matched"));
		
		addComponent(tabSheet);
	}
	
	/**
	 * @see com.vaadin.ui.TabSheet.SelectedTabChangeListener#selectedTabChange(com.vaadin.ui.TabSheet.SelectedTabChangeEvent)
	 */
	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		if (tabSheet.getSelectedTab().equals(fileTabPanel)) {
			fileTabPanel.reset();
		} else if (tabSheet.getSelectedTab().equals(unIdentifiedDetailsPanel)) {
			unIdentifiedDetailsPanel.refresh();
		} else if (tabSheet.getSelectedTab().equals(unMatchedDetailsPanel)) {
			unMatchedDetailsPanel.refresh();
		} else if (tabSheet.getSelectedTab().equals(suspendedDetailsPanel)) {
			suspendedDetailsPanel.refresh();
		} else if (tabSheet.getSelectedTab().equals(overDetailsPanel)) {
			overDetailsPanel.refresh();
		} else if (tabSheet.getSelectedTab().equals(matchedDetailsPanel)) {
			matchedDetailsPanel.refresh();
		}
	}

}
