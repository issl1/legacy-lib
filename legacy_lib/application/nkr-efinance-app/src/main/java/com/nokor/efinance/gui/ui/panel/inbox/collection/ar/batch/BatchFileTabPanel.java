package com.nokor.efinance.gui.ui.panel.inbox.collection.ar.batch;

import org.seuksa.frmk.tools.spring.SpringUtils;

import com.nokor.efinance.gui.ui.panel.payment.integration.file.list.PaymentFileHolderPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;

/**
 * 
 * @author buntha.chea
 *
 */
public class BatchFileTabPanel extends AbstractControlPanel {

	/** */
	private static final long serialVersionUID = 5803240638617772219L;
	
	private PaymentFileHolderPanel paymentFileHolderPanel; 
	
	/**
	 * 
	 */
	public BatchFileTabPanel() {
		paymentFileHolderPanel = SpringUtils.getBean(PaymentFileHolderPanel.class);
		
		setMargin(true);
		addComponent(paymentFileHolderPanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	protected void reset() {
		paymentFileHolderPanel.setSelectedDefaultTab();
	}
	
}
