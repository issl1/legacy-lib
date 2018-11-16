package com.nokor.efinance.gui.ui.panel.dashboard.task;

import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.gui.ui.panel.contract.ContractsPanel;
import com.vaadin.server.Page;


/**
 * 
 * @author uhout.cheng
 */
public class DetailContractTask implements FinServicesHelper {

	/**
	 * 
	 * @param entityId
	 */
	public void execute(Long entityId) {
		if (entityId != null) {
			Page.getCurrent().setUriFragment("!" + ContractsPanel.NAME + "/" + entityId);
		}
	}	
}
