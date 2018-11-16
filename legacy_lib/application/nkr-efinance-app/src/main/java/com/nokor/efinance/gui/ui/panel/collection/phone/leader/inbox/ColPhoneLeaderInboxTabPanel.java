package com.nokor.efinance.gui.ui.panel.collection.phone.leader.inbox;

import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.common.IProfileCode;
import com.nokor.efinance.core.contract.model.ContractUserInbox;
import com.nokor.efinance.gui.ui.panel.collection.phone.filter.CollectionContractTablePanel;
import com.vaadin.ui.VerticalLayout;
/**
 * 
 * @author buntha.chea
 *
 */
public class ColPhoneLeaderInboxTabPanel extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6111763552444241426L;

	private ColPhoneLeaderInboxFilterPanel inboxFilterPanel;
	private CollectionContractTablePanel inboxTablePanel;
	
	public ColPhoneLeaderInboxTabPanel() {
		setSpacing(true);
		setMargin(true);
		
		inboxTablePanel = new CollectionContractTablePanel(null);
		inboxFilterPanel = new ColPhoneLeaderInboxFilterPanel(inboxTablePanel, IProfileCode.COL_PHO_STAFF);
		
		refresh();
		
		addComponent(inboxFilterPanel);
		addComponent(inboxTablePanel);
	}
	
	/**
	 * Refresh
	 */
	public void refresh() {
		BaseRestrictions<ContractUserInbox> restriction = inboxFilterPanel.getRestrictions();
		inboxTablePanel.refresh(restriction);
		inboxTablePanel.setRestriction(restriction);
	}

}
