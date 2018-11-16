package com.nokor.efinance.gui.ui.panel.collection;

import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.gui.ui.panel.collection.filter.LeaderColContractFilterPanel;
import com.nokor.efinance.gui.ui.panel.collection.staffphone.ColContractDetailPanel;

/**
 * @author uhout.cheng
 */
public class LeaderColContractsPanel extends ColContractsHolderPanel implements FinServicesHelper {
	
	/** */
	private static final long serialVersionUID = 9187261039959045395L;
	
	private LeaderColContractFilterPanel filterPanel;
	private ColContractTablePanel contractTabPanel;
	private ColContractDetailPanel detailPanel;
	
	/**
	 */
	public LeaderColContractsPanel() {
		setMargin(true);
		detailPanel = new ColContractDetailPanel();
		contractTabPanel = new ColContractTablePanel(detailPanel);		
		filterPanel = new LeaderColContractFilterPanel(contractTabPanel);
		detailPanel.setVisibleLayout(false);
		init(filterPanel, contractTabPanel, detailPanel);
		refresh();
	}	
}
