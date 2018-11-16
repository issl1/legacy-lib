package com.nokor.efinance.gui.ui.panel.collection;

import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.contract.model.ContractUserInbox;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.gui.ui.panel.collection.phone.filter.CollectionContractTablePanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.leader.inbox.ColPhoneLeaderAssistFilterPanel;
import com.vaadin.ui.VerticalLayout;

/**
 * @author uhout.cheng
 */
public class LeaderColAssistContractsPanel extends VerticalLayout implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = -725936932082769650L;
	
	private ColPhoneLeaderAssistFilterPanel assistFilterPanel;
	private CollectionContractTablePanel tablePanel;
	
	public LeaderColAssistContractsPanel() {
		setSpacing(true);
		setMargin(true);
		tablePanel = new CollectionContractTablePanel(false);
		assistFilterPanel = new ColPhoneLeaderAssistFilterPanel(tablePanel);
		
		addComponent(assistFilterPanel);
		addComponent(tablePanel);
	}
	
	/**
	 * Refresh
	 */
	public void refresh() {
		BaseRestrictions<ContractUserInbox> restriction = assistFilterPanel.getRestrictions();
		tablePanel.refresh(restriction);
		tablePanel.setRestriction(restriction);
	}
}
