package com.nokor.efinance.gui.ui.panel.collection.field.supervisor.flag;

import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.contract.model.ContractUserInbox;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.gui.ui.panel.collection.phone.filter.CollectionContractTablePanel;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author uhout.cheng
 */
public class SupervisorColFlagContractsPanel extends VerticalLayout implements FinServicesHelper {
	
	/** */
	private static final long serialVersionUID = 6502531322712429597L;
	
	private ColFieldSupervisorFlagFilterPanel flagFilterPanel;
	private CollectionContractTablePanel tablePanel;
	
	/**
	 * 
	 */
	public SupervisorColFlagContractsPanel() {
		setSpacing(true);
		setMargin(true);
		tablePanel = new CollectionContractTablePanel(null);
		flagFilterPanel = new ColFieldSupervisorFlagFilterPanel(tablePanel);
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
