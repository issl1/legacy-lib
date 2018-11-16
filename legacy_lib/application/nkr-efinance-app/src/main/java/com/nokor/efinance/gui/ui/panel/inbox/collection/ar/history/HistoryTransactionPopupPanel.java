package com.nokor.efinance.gui.ui.panel.inbox.collection.ar.history;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.collection.model.EPaymentChannel;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author buntha.chea
 *
 */
public class HistoryTransactionPopupPanel extends Window implements ClickListener, FinServicesHelper {

	/**
	 * 
	 */
	private static final long serialVersionUID = -139102874799949067L;
	
	private Label lblPaymentValue;
	private Label lblAmountValue;
	
	private Button btnReverse;
	private Button btnPrint;
	
	private Payment payment;
	
	private Label lblChannelValue;
	private Label lblContractIdValue;
	private Label lblRecordDateValue;
	private Label lblReceiptIdValue;
	private Label lblPayeeValue;
	private Label lblAmountVatValue;
	
	public HistoryTransactionPopupPanel() {
		setModal(true);
		setCaption(I18N.message("transaction"));
		
		Label lblPayment = getLabel("payment");
		Label lblAmount = getLabel("amount");
		
		Label lblChannel = getLabel("channel");
		Label lblContractId = getLabel("contract.id");
		Label lblRecordDate = getLabel("record.date");
		Label lblReceiptId = getLabel("receipt.id");
		Label lblPayee = getLabel("payee");
		Label lblAmountVat = getLabel("amount.vat");
		
		btnReverse = ComponentLayoutFactory.getButtonStyle("reverse", null, 80, "btn btn-success button-small");
		btnReverse.addClickListener(this);
		
		btnPrint = ComponentLayoutFactory.getButtonStyle("print", FontAwesome.PRINT, 80, "btn btn-success button-small");
		btnPrint.addClickListener(this);
		
		lblPaymentValue = getLabelValue();
		lblAmountValue = getLabelValue();
		
		lblChannelValue = getLabelValue();
		lblContractIdValue = getLabelValue();
		lblRecordDateValue = getLabelValue();
		lblReceiptIdValue = getLabelValue();
		lblPayeeValue = getLabelValue();
		lblAmountVatValue = getLabelValue();
		
		GridLayout transactionGridLayout = ComponentLayoutFactory.getGridLayout(8,  3);
		transactionGridLayout.setSpacing(true);
		
		int iCol = 0;
		transactionGridLayout.addComponent(lblPayment, iCol++, 0);
		transactionGridLayout.addComponent(lblPaymentValue, iCol++, 0);
		transactionGridLayout.addComponent(lblChannel, iCol++, 0);
		transactionGridLayout.addComponent(lblChannelValue, iCol++, 0);
		transactionGridLayout.addComponent(lblRecordDate, iCol++, 0);
		transactionGridLayout.addComponent(lblRecordDateValue, iCol++, 0);
		
		iCol = 0;
		transactionGridLayout.addComponent(lblReceiptId, iCol++, 1);
		transactionGridLayout.addComponent(lblReceiptIdValue, iCol++, 1);
		transactionGridLayout.addComponent(lblContractId, iCol++, 1);
		transactionGridLayout.addComponent(lblContractIdValue, iCol++, 1);
		transactionGridLayout.addComponent(lblPayee, iCol++, 1);
		transactionGridLayout.addComponent(lblPayeeValue, iCol++, 1);
		
		iCol = 0;
		transactionGridLayout.addComponent(lblAmount, iCol++, 2);
		transactionGridLayout.addComponent(lblAmountValue, iCol++, 2);
		transactionGridLayout.addComponent(lblAmountVat, iCol++, 2);
		transactionGridLayout.addComponent(lblAmountVatValue, iCol++, 2);
		
		Panel detailPanel = ComponentLayoutFactory.getPanel(transactionGridLayout, true, false);
		
		HorizontalLayout buttonaLayout = ComponentLayoutFactory.getHorizontalLayout(true, true);
		buttonaLayout.addComponent(btnReverse);
		buttonaLayout.addComponent(btnPrint);
		
		
		VerticalLayout mainLayout = ComponentLayoutFactory.getVerticalLayout(true, true);
		mainLayout.addComponent(detailPanel);
		mainLayout.addComponent(buttonaLayout);
		mainLayout.setComponentAlignment(buttonaLayout, Alignment.BOTTOM_RIGHT);
		
		setContent(mainLayout);
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
	 * 
	 * @return
	 */
	private Label getLabelValue() {
		Label label = new Label("", ContentMode.HTML);
		label.setWidthUndefined();
		return label;
	}
	
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	private String getDescription(String value) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<b>");
		stringBuffer.append(value == null ? "" : value);
		stringBuffer.append("</b>");
		return stringBuffer.toString();
	}
	
	/**
	 * Assign Value
	 * @param payment
	 */
	public void assignValue(Payment payment) {
		this.payment = payment;
		
		String channel = StringUtils.EMPTY;
		List<Cashflow> cashflows = payment.getCashflows();
		if (cashflows != null & !cashflows.isEmpty()) {
			EPaymentChannel paymentChannel = cashflows.get(0).getPaymentChannel();
			if (paymentChannel != null) {
				channel = paymentChannel.getDescLocale();
			}
		}
		
		lblPaymentValue.setValue(getDescription(DateUtils.getDateLabel(payment.getPaymentDate())));
		lblAmountValue.setValue(getDescription(AmountUtils.format(payment.getTiPaidAmount())));
		lblChannelValue.setValue(getDescription(channel));
		lblContractIdValue.setValue(getDescription(payment.getContract() != null ? payment.getContract().getReference() : ""));
		lblRecordDateValue.setValue(getDescription(DateUtils.getDateLabel(payment.getCreateDate())));
		lblReceiptIdValue.setValue(getDescription(payment.getReference()));
		lblPayeeValue.setValue(getDescription(getDescription(payment.getPayee() != null ? payment.getPayee().getName() : "")));
		lblAmountVatValue.setValue(getDescription(AmountUtils.format(payment.getVatPaidAmount())));
	}
	

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnReverse) {
			ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("are.you.sure.cancel.payment"),
			        new ConfirmDialog.Listener() {
						private static final long serialVersionUID = 2380193173874927880L;
						public void onClose(ConfirmDialog dialog) {
			                if (dialog.isConfirmed()) {
			                	try {
			                		PAYMENT_SRV.cancelPayment(payment);
				                	close();
				                	ComponentLayoutFactory.displaySuccessfullyMsg();
								} catch (Exception e) {
									e.printStackTrace();
									ComponentLayoutFactory.displayErrorMsg(e.getMessage());
								}
			                }
			            }
				});
				confirmDialog.setWidth("400px");
				confirmDialog.setHeight("150px");
		} else if (event.getButton() == btnPrint) {
			close();
		}
	}

}
