package com.nokor.efinance.gui.ui.panel.collection.field.supervisor.assist;

import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.contract.model.ContractUserInbox;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.gui.ui.panel.collection.phone.filter.CollectionContractTablePanel;
import com.vaadin.ui.VerticalLayout;

/**
 * @author uhout.cheng
 */
public class SupervisorColAssistContractsPanel extends VerticalLayout implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = 7764902867177180997L;

	private ColFieldSupervisorAssistFilterPanel assistFilterPanel;
	private CollectionContractTablePanel tablePanel;
	
	/**
	 * 
	 * @param profileCode
	 */
	public SupervisorColAssistContractsPanel(String profileCode) {
		setSpacing(true);
		setMargin(true);
		tablePanel = new CollectionContractTablePanel(false);
		assistFilterPanel = new ColFieldSupervisorAssistFilterPanel(tablePanel, profileCode);
		
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
