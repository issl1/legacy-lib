package com.nokor.efinance.gui.ui.panel.dashboard;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.vaadin.ui.VerticalLayout;

/**
 * @author buntha.chea
 */
public class InboxPanel extends VerticalLayout implements QuotationEntityField {
	
	private static final long serialVersionUID = -2983055467054135680L;
	
	// private InboxSummaryPanel inboxSummaryPanel;
	private InboxContractsPanel inboxContractsPanel;
	
	public InboxPanel() {
		setCaption(I18N.message("inbox"));
		setSizeFull();
		setHeight("100%");
		setMargin(true);
		setSpacing(true);
		
		// inboxSummaryPanel = new InboxSummaryPanel();
		inboxContractsPanel = new InboxContractsPanel(new EWkfStatus[]{}, null);
		// addComponent(inboxSummaryPanel);
		addComponent(inboxContractsPanel);
	}
	
	/**
	 * 
	 */
	public void refresh() {
		inboxContractsPanel.refresh();
	}
	
	/**
	 * @return
	 */
	public InboxContractsPanel getInboxContractsPanel() {
		return inboxContractsPanel;
	}
}
