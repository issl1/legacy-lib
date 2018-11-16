package com.nokor.efinance.gui.ui.panel.payment.blocked.filters;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.collection.model.MCollection;
import com.nokor.efinance.core.payment.service.PaymentFileItemRestriction;
import com.nokor.efinance.core.workflow.PaymentFileWkfStatus;
import com.nokor.efinance.gui.ui.panel.payment.blocked.BlockedPaymentDetailTablePanel;

/**
 * 
 * @author uhout.cheng
 */
public class BlockedPaymentFilterPanel extends AbstractBlockedPaymentFilterPanel implements MCollection {
	
	/** */
	private static final long serialVersionUID = -1802662755996217893L;
	
	/**
	 * 
	 * @param tablePanel
	 * @param wkfStatus
	 */
	public BlockedPaymentFilterPanel(BlockedPaymentDetailTablePanel tablePanel, EWkfStatus wkfStatus) {
		super(tablePanel, wkfStatus);
	}

	/**
	 * @see com.nokor.efinance.gui.ui.panel.payment.blocked.filters.AbstractBlockedPaymentFilterPanel#getRestrictions()
	 */
	@Override
	public PaymentFileItemRestriction getRestrictions() {
		PaymentFileItemRestriction restrictions = new PaymentFileItemRestriction();
		if (storeControlFilter != null) {
			restrictions.setWkfStatuses(new EWkfStatus[] { storeControlFilter.getWkfStatus() });
			restrictions.setPaymentChannels(storeControlFilter.getChannels());
			if (!PaymentFileWkfStatus.UNIDENTIFIED.equals(storeControlFilter.getWkfStatus())) {
				restrictions.setUploadDateFrom(storeControlFilter.getFrom());
				restrictions.setUploadDateTo(storeControlFilter.getTo());
				restrictions.setAmountFrom(storeControlFilter.getAmountFrom());
				restrictions.setAmountTo(storeControlFilter.getAmountTo());
				restrictions.setContractReference(storeControlFilter.getContractId());
			}
		}
		return restrictions;
	}
}
