package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.redumption;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.contract.model.Contract;
import com.vaadin.ui.Window;

/**
 * 
 * @author buntha.chea
 *
 */
public class RedemptionPopupPanel extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8850882983705491911L;
	
	private RedemptionPanel redemptionPanel;
	
	public RedemptionPopupPanel(Contract contract) {
		setModal(true);
		setCaption(I18N.message("redemption"));
		setHeight("870px");
		setWidth("786px");
		
		redemptionPanel = new RedemptionPanel();
		redemptionPanel.assignValue(contract);
		setContent(redemptionPanel);
	}
}
