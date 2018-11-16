package com.nokor.efinance.gui.ui.panel.collection.phone;

import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.gui.ui.panel.collection.ColContractTablePanel;
import com.nokor.efinance.gui.ui.panel.collection.ColContractsHolderPanel;
import com.nokor.efinance.gui.ui.panel.collection.filter.DefaultColContractFilterPanel;
import com.nokor.efinance.gui.ui.panel.collection.staffphone.ColContractDetailPanel;

/**
 * INBOX Staff Phone Panel 
 * @author uhout.cheng
 */
public class InboxStaffPhonePanel extends ColContractsHolderPanel implements FinServicesHelper {
	
	/** */
	private static final long serialVersionUID = 9187261039959045395L;
	
	private DefaultColContractFilterPanel filterPanel;
	private ColContractTablePanel contractTabPanel;
	private ColContractDetailPanel detailPanel;
	
	/**
	 */
	public InboxStaffPhonePanel() {
		setMargin(true);
		detailPanel = new ColContractDetailPanel();
		contractTabPanel = new ColContractTablePanel(detailPanel);		
		filterPanel = new DefaultColContractFilterPanel(contractTabPanel);
		detailPanel.setVisibleLayout(false);		
		init(filterPanel, contractTabPanel, detailPanel);
		refresh();
	}
}
