package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.payment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.LockSplit;
import com.nokor.efinance.core.contract.model.LockSplitRestriction;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.workflow.LockSplitWkfStatus;
import com.nokor.frmk.vaadin.ui.layout.LayoutHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class ColPhoneLockSplitSummaryPanel extends AbstractControlPanel implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = -9206909275935732405L;
	
	private static final String TEMPLATE = "payment/summary/locksplitPaymentLayout";
	
	private Label lblNextExpectedPaymentValue;
	private Label lblNextLockSplitValidFromDateValue;
	private Label lblNextDueDateValue;
	private Label lblNumberOfPaymentRecievedValue;
	private Label lblTotalAmountRecievedValue;
	private Label lblNumberOfBlockedPaymentValue;
	private Label lblAmountBlockedValue;
	private Label lblNumberOfPendingChequesValue;
	private Label lblAmountPendingChequeValue;
	private Label lblNumberOfPendingTransferValue;
	private Label lblAmountPendingTransferValue;
	
	/**
	 * 
	 */
	public ColPhoneLockSplitSummaryPanel() {
		init();
	}
	
	/**
	 * init
	 */
	private void init() {
		lblNextExpectedPaymentValue = getLabelValue();
		lblNextLockSplitValidFromDateValue = getLabelValue();
		lblNextDueDateValue = getLabelValue();
		lblNumberOfPaymentRecievedValue = getLabelValue();
		lblTotalAmountRecievedValue = getLabelValue();
		lblNumberOfBlockedPaymentValue = getLabelValue();
		lblAmountBlockedValue = getLabelValue();
		lblNumberOfPendingChequesValue = getLabelValue();
		lblAmountPendingChequeValue = getLabelValue();
		lblNumberOfPendingTransferValue = getLabelValue();
		lblAmountPendingTransferValue = getLabelValue();
				
		Label lblNextExpectedPaymentTitle = getLabel("next.expected.payment");
		Label lblNextLockSplitValidFromDateTitle = getLabel("next.locksplit.valid.from.date");
		Label lblNextDueDateTitle = getLabel("next.due.date");
		
		GridLayout topGridLayout = new GridLayout(5, 2);
		topGridLayout.setMargin(true);
		topGridLayout.setSpacing(true);
		
		topGridLayout.addComponent(lblNextExpectedPaymentTitle, 0, 0);
		topGridLayout.addComponent(lblNextExpectedPaymentValue, 1, 0);
		topGridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), 2, 0);
		topGridLayout.addComponent(lblNextLockSplitValidFromDateTitle, 3, 0);
		topGridLayout.addComponent(lblNextLockSplitValidFromDateValue, 4, 0);
		topGridLayout.addComponent(lblNextDueDateTitle, 0, 1);
		topGridLayout.addComponent(lblNextDueDateValue, 1, 1);
		
		Panel panel = getPanelCaptionColor("", getCustomLayout(), true);
		
		VerticalLayout mainVerticalLayout = new VerticalLayout();
		mainVerticalLayout.setMargin(true);
		mainVerticalLayout.setSpacing(true);
		
		mainVerticalLayout.addComponent(topGridLayout);
		mainVerticalLayout.addComponent(panel);
		
		addComponent(mainVerticalLayout);
	}
	
	/**
	 * 
	 * @param caption
	 * @param component
	 * @param isBorderPanel
	 * @return
	 */
	public Panel getPanelCaptionColor(String caption, Component component, boolean isBorderPanel) {
		Panel panel = new Panel(component);
		panel.setCaptionAsHtml(true);
		panel.setCaption("<h2 style=\"margin: 0; color: #398439\" >" + I18N.message(caption) + "</h2>");
		if (!isBorderPanel) {
			panel.setStyleName(Reindeer.PANEL_LIGHT);
		}
		return panel;
	}
	
	/**
	 * 
	 * @return customLayout
	 */
	private CustomLayout getCustomLayout() {
		CustomLayout customLayout = getCustomLayout(TEMPLATE);
		
		customLayout.addComponent(ComponentFactory.getLabel("number.of.payment.recieved"), "lblNumberOfPaymentRecieved");
		customLayout.addComponent(lblNumberOfPaymentRecievedValue, "lblNumberOfPaymentRecievedValue");
		customLayout.addComponent(ComponentFactory.getLabel("total.amount.recieved"), "lblTotalAmountRecieved");
		customLayout.addComponent(lblTotalAmountRecievedValue, "lblTotalAmountRecievedValue");
		customLayout.addComponent(ComponentFactory.getLabel("number.of.blocked.payment"), "lblNumberOfBlockedPayment");
		customLayout.addComponent(lblNumberOfBlockedPaymentValue, "lblNumberOfBlockedPaymentValue");
		customLayout.addComponent(ComponentFactory.getLabel("amount.blocked"), "lblAmountBlocked");
		customLayout.addComponent(lblAmountBlockedValue, "lblAmountBlockedValue");
		customLayout.addComponent(ComponentFactory.getLabel("number.of.pending.cheques"), "lblNumberOfPendingCheques");
		customLayout.addComponent(lblNumberOfPendingChequesValue, "lblNumberOfPendingChequesValue");
		customLayout.addComponent(ComponentFactory.getLabel("amount.pending.cheque"), "lblAmountPendingCheque");
		customLayout.addComponent(lblAmountPendingChequeValue, "lblAmountPendingChequeValue");
		customLayout.addComponent(ComponentFactory.getLabel("number.of.pending.transfer"), "lblNumberOfPendingTransfer");
		customLayout.addComponent(lblNumberOfPendingTransferValue, "lblNumberOfPendingTransferValue");
		customLayout.addComponent(ComponentFactory.getLabel("amount.pending.transfer"), "lblAmountPendingTransfer");
		customLayout.addComponent(lblAmountPendingTransferValue, "lblAmountPendingTransferValue");
		
		return customLayout;
	}
	
	/**
	 * 
	 * @param contract
	 */
	protected void assignValues(Contract contract) {
		this.reset();
		if (contract != null) {
			LockSplitRestriction lckSplitRestrictions = new LockSplitRestriction();
			lckSplitRestrictions.setContractID(contract.getReference());
			List<EWkfStatus> pendingStatus = new ArrayList<>();
			pendingStatus.add(LockSplitWkfStatus.LNEW);
			pendingStatus.add(LockSplitWkfStatus.LPEN);
			pendingStatus.add(LockSplitWkfStatus.LPAR);
			lckSplitRestrictions.setWkfStatusList(pendingStatus);
			lckSplitRestrictions.addCriterion(Restrictions.lt(LockSplit.PAYMENTDATE, DateUtils.getDateAtEndOfDay(DateUtils.today())));
			LockSplit lockSplit = null;
			List<LockSplit> lockSplits = PAYMENT_SRV.list(lckSplitRestrictions);
			if (lockSplits != null && !lockSplits.isEmpty()) {
				lockSplit = lockSplits.get(0);
			}
			if (lockSplit != null) {
				lblNextExpectedPaymentValue.setValue(getDescription(MyNumberUtils.formatDoubleToString(
						MyNumberUtils.getDouble(lockSplit.getTotalAmount()), "###,##0.00")));
				lblNextLockSplitValidFromDateValue.setValue(getDescription(getDateFormat(lockSplit.getFrom())));
			}
			Collection collection = contract.getCollection();
			if (collection != null) {
				lblNextDueDateValue.setValue(getDescription(getDateFormat(collection.getNextDueDate())));
			}
		}
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	private String getDateFormat(Date date) {
		String dateFormat = DateUtils.getDateLabel(date, DateUtils.FORMAT_DDMMYYYY_SLASH); 
		return dateFormat != null ? dateFormat : StringUtils.EMPTY;
	}
	
	/**
	 * 
	 * @param template
	 * @return customlayout
	 */
	private CustomLayout getCustomLayout(String template) {
		CustomLayout customLayout = LayoutHelper.createCustomLayout(template);
		if (customLayout == null) {
			Notification.show("Could not locate template " + LayoutHelper.getTemplateFullPath(template), Type.ERROR_MESSAGE);
		}
		return customLayout;
	}
	
	/**
	 * 
	 * @return label
	 */
	private Label getLabelValue() {
		Label label = new Label("", ContentMode.HTML);
		label.setWidthUndefined();
		return label;
	}
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	private Label getLabel(String caption) {
		return ComponentFactory.getLabel(I18N.message(caption) + StringUtils.SPACE + ":");
	}
	
	/**
	 * reset
	 */
	protected void reset() {
		lblNextExpectedPaymentValue.setValue(StringUtils.EMPTY);
		lblNextLockSplitValidFromDateValue.setValue(StringUtils.EMPTY);
		lblNextDueDateValue.setValue(StringUtils.EMPTY);
		lblNumberOfPaymentRecievedValue.setValue(StringUtils.EMPTY);
		lblTotalAmountRecievedValue.setValue(StringUtils.EMPTY);
		lblNumberOfBlockedPaymentValue.setValue(StringUtils.EMPTY);
		lblAmountBlockedValue.setValue(StringUtils.EMPTY);
		lblNumberOfPendingChequesValue.setValue(StringUtils.EMPTY);
		lblAmountPendingChequeValue.setValue(StringUtils.EMPTY);
		lblNumberOfPendingTransferValue.setValue(StringUtils.EMPTY);
		lblAmountPendingTransferValue.setValue(StringUtils.EMPTY);
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	private String getDescription(String value) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<b>");
		stringBuffer.append(getDefaultString(value));
		stringBuffer.append("</b>");
		return stringBuffer.toString();
	}

}
