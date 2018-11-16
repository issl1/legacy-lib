package com.nokor.efinance.gui.ui.panel.collection;

import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.contract.model.ContractUserInbox;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.gui.ui.panel.collection.phone.filter.CollectionContractTablePanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.leader.inbox.ColPhoneLeaderFlagFilterPanel;
import com.vaadin.ui.VerticalLayout;

/**
 * @author uhout.cheng
 */
public class LeaderColFlagContractsPanel extends VerticalLayout implements FinServicesHelper {
	
	/**
	 */
	private static final long serialVersionUID = -6703159047535485479L;

	private ColPhoneLeaderFlagFilterPanel flagFilterPanel;
	private CollectionContractTablePanel tablePanel;
	
	/**
	 */
	public LeaderColFlagContractsPanel() {
		setSpacing(true);
		setMargin(true);
		tablePanel = new CollectionContractTablePanel(true);
		flagFilterPanel = new ColPhoneLeaderFlagFilterPanel(tablePanel);
		
		addComponent(flagFilterPanel);
		addComponent(tablePanel);
	}	
	
	/**
	 * Refresh
	 */
	public void refresh() {
		BaseRestrictions<ContractUserInbox> restriction = flagFilterPanel.getRestrictions();
		tablePanel.refresh(restriction);
		tablePanel.setRestriction(restriction);
	}
}
