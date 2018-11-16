package com.nokor.efinance.gui.ui.panel.payment.blocked;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.payment.model.MPayment;
import com.nokor.efinance.gui.ui.panel.payment.blocked.filters.BlockedPaymentFilterPanel;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author uhout.cheng
 */
public class BlockedPaymentDetailsPanel extends VerticalLayout implements MPayment {

	/** */
	private static final long serialVersionUID = -9031155014596338762L;
	
	private BlockedPaymentFilterPanel filterPanel;
	private BlockedPaymentDetailTablePanel tablePanel;
	private BlockedPaymentDetailInfosPanel infosPanel;
	
	/**
	 * 
	 * @param wkfStatus
	 */
	public BlockedPaymentDetailsPanel(EWkfStatus wkfStatus) {
		infosPanel = new BlockedPaymentDetailInfosPanel(wkfStatus);
		tablePanel = new BlockedPaymentDetailTablePanel(infosPanel, wkfStatus);
		filterPanel = new BlockedPaymentFilterPanel(tablePanel, wkfStatus);
		
		setSpacing(true);
		setMargin(true);
		addComponent(filterPanel);
		addComponent(tablePanel);
		
		/*if (!PaymentFileWkfStatus.UNIDENTIFIED.equals(wkfStatus) && !PaymentFileWkfStatus.ERROR.equals(wkfStatus)) {
			addComponent(infosPanel);
		}	*/
	}
	
	/**
	 * 
	 */
	public void refresh() {
		tablePanel.refreshAfterSaveOrAllocation();
	}
		
}
