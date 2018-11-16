package com.nokor.efinance.gui.ui.panel.payment.cashier.filter;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.collection.model.MCollection;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.efinance.core.payment.model.PaymentFileItem;
import com.nokor.efinance.core.payment.service.PaymentFileItemRestriction;
import com.nokor.efinance.core.workflow.PaymentFileWkfStatus;
import com.nokor.efinance.gui.ui.panel.payment.cashier.PaymentCashierDetailTablePanel;

/**
 * 
 * @author uhout.cheng
 */
public class PaymentCashierFilterPanel extends AbstractPaymentCashierFilterPanel implements MCollection {
	
	/** */
	private static final long serialVersionUID = -5262770161314116348L;

	private PaymentCashierDetailTablePanel tablePanel;
	
	/**
	 * 
	 * @param tablePanel
	 */
	public PaymentCashierFilterPanel(PaymentCashierDetailTablePanel tablePanel) {
		super(tablePanel);
		this.tablePanel = tablePanel;
	}

	/**
	 * @see com.nokor.efinance.gui.ui.panel.payment.cashier.filter.AbstractPaymentCashierFilterPanel#getRestrictions()
	 */
	@Override
	public PaymentFileItemRestriction getRestrictions() {
		PaymentFileItemRestriction restrictions = new PaymentFileItemRestriction();
		List<EPaymentMethod> paymentMethods = new ArrayList<EPaymentMethod>();
		if (this.tablePanel.isPendingCheque()) {
			restrictions.setWkfStatuses(new EWkfStatus[] { PaymentFileWkfStatus.MATCHED });
			paymentMethods.add(EPaymentMethod.CHEQUE);
		} else {
			restrictions.addCriterion(Restrictions.ne(PaymentFileItem.WKFSTATUS, PaymentFileWkfStatus.ALLOCATED));
			paymentMethods.add(EPaymentMethod.CASH);
			paymentMethods.add(EPaymentMethod.CHEQUE);
		}
		if (storeControlFilter != null) {
			restrictions.setPaymentDateFrom(storeControlFilter.getFrom());
			restrictions.setPaymentDateTo(storeControlFilter.getTo());
			if (!this.tablePanel.isPendingCheque()) {
				paymentMethods.clear();
				if (storeControlFilter.getPaymentMethod() != null) {
					paymentMethods.add(storeControlFilter.getPaymentMethod());
				} else {
					paymentMethods.add(EPaymentMethod.CASH);
					paymentMethods.add(EPaymentMethod.CHEQUE);
				}
			}
		} 
		restrictions.setPaymentMethods(paymentMethods);
		return restrictions;
	}
}
