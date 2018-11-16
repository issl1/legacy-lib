package com.nokor.efinance.gui.ui.panel.inbox.collection.ar.cashier;

import org.apache.commons.lang3.StringUtils;

import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.widget.FieldSet;
import com.nokor.efinance.gui.ui.panel.inbox.collection.ar.RecordPaymentPopupPanel;
import com.nokor.efinance.gui.ui.panel.inbox.collection.ar.TransactionTablePanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
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
import com.vaadin.ui.themes.Reindeer;

/**
 * 
 * @author buntha.chea
 *
 */
public class ColAuctionBidderDetailPanel extends AbstractControlPanel implements ClickListener {

	private static final long serialVersionUID = -2583404124240405265L;
	
	private Label lblBidderIdValue;
	private Label lblBidderTypeValue;
	
	private Label lblTitleValue;
	private Label lblFullnameValue;
	private Label lblDOBValue;
	private Label lblIDNumberValue;
	
	private Label lblPhoneNoValue;
	private Label lblEmailValue;
	private Label lblAddressValue;
	
	private Label lblTotalDueValue;
	private Label lblSaleValue;
	private Label lblPenaltiesValue;
	
	private Label lblTranfersValue;
	private Label lblChequesValue;
	private Label lblTranfersAmountValue;
	private Label lblChequesAmountValue;
	
	private Label lblTotalValue;
	
	private Button btnPrintReceipt;
	private Button btnPrintTemporaryReceipt;
	private Button btnMakePayment;
	
	private TabSheet tabSheet;
	private TransactionTablePanel transactionTablePanel;
	/**
	 * 
	 */
	public ColAuctionBidderDetailPanel() {
		setMargin(true);
		init();
		
		HorizontalLayout horizontalLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		horizontalLayout.setMargin(new MarginInfo(true, true, true, false));
		horizontalLayout.addComponent(createOustandingPanel());
		horizontalLayout.addComponent(createPendingPanel());
		
		HorizontalLayout buttonLayout = ComponentLayoutFactory.getHorizontalLayout(true, true);
		buttonLayout.addComponent(btnPrintReceipt);
		buttonLayout.addComponent(btnPrintTemporaryReceipt);
		buttonLayout.addComponent(btnMakePayment);
		
		Label lblTotal = getLabel("total");
		
		HorizontalLayout totalLayout = ComponentLayoutFactory.getHorizontalLayout(true, true);
		totalLayout.addComponent(lblTotal);
		totalLayout.addComponent(lblTotalValue);
		
		addComponent(createBidderDetailPanel());
		addComponent(horizontalLayout);
		addComponent(tabSheet);
		addComponent(totalLayout);
		addComponent(buttonLayout);
		setComponentAlignment(buttonLayout, Alignment.BOTTOM_RIGHT);
	}
	
	/**
	 * init
	 */
	private void init() {
		lblBidderIdValue = getLabelValue();
		lblBidderTypeValue = getLabelValue();
		
		lblTitleValue = getLabelValue();
		lblFullnameValue = getLabelValue();
		lblDOBValue = getLabelValue();
		lblIDNumberValue = getLabelValue();
		
		lblPhoneNoValue = getLabelValue();
		lblEmailValue = getLabelValue();
		lblAddressValue = getLabelValue();
		
		lblTotalDueValue = getLabelValue();
		lblSaleValue = getLabelValue();
		lblPenaltiesValue = getLabelValue();
		
		lblTranfersValue = getLabelValue();
		lblTranfersAmountValue = getLabelValue();
		lblChequesValue = getLabelValue();
		lblChequesAmountValue = getLabelValue();
		
		lblTotalValue = getLabelValue();
		
		btnPrintReceipt = ComponentLayoutFactory.getButtonStyle("print.receipt", FontAwesome.PRINT, 110, "btn btn-success");
		btnPrintReceipt.addClickListener(this);
		
		btnPrintTemporaryReceipt = ComponentLayoutFactory.getButtonStyle("print.temporary.receipt", FontAwesome.PRINT, 165, "btn btn-success");
		btnPrintTemporaryReceipt.addClickListener(this);
		
		btnMakePayment = ComponentLayoutFactory.getButtonStyle("make.a.payment", null, 110, "btn btn-success");
		btnMakePayment.addClickListener(this);
		
		transactionTablePanel = new TransactionTablePanel();
		
		tabSheet = new TabSheet();
		tabSheet.addTab(transactionTablePanel, I18N.message("transactions"));
		tabSheet.addTab(new VerticalLayout(), I18N.message("pending.cheques"));
		tabSheet.addTab(new VerticalLayout(), I18N.message("pending.tranfers"));
	}
	
	/**
	 * createBidderDetailPanel
	 * @return
	 */
	private Panel createBidderDetailPanel() {
		
		GridLayout bidderDetailGridLayout = ComponentLayoutFactory.getGridLayout(8, 3);
		bidderDetailGridLayout.setSpacing(true);
		bidderDetailGridLayout.setMargin(true);
		
		Label lblBidderId = getLabel("id");
		Label lblBidderType = getLabel("type");
		
		Label lblTitle = getLabel("title");
		Label lblFullname = getLabel("fullname");
		Label lblDOB = getLabel("dob");
		Label lblIdNumber = getLabel("id.number");
		
		Label lblPhoneNo = getLabel("phone.no");
		Label lblEmail = getLabel("email");
		Label lblAddress = getLabel("address");
		
		int iCol = 0;
		int iRow = 0;
		bidderDetailGridLayout.addComponent(lblBidderId, iCol++, iRow);
		bidderDetailGridLayout.addComponent(lblBidderIdValue, iCol++, iRow);
		bidderDetailGridLayout.addComponent(lblBidderType, iCol++, iRow);
		bidderDetailGridLayout.addComponent(lblBidderTypeValue, iCol++, iRow);
		
		iCol = 0;
		iRow++;
		bidderDetailGridLayout.addComponent(lblTitle, iCol++, iRow);
		bidderDetailGridLayout.addComponent(lblTitleValue, iCol++, iRow);
		bidderDetailGridLayout.addComponent(lblFullname, iCol++, iRow);
		bidderDetailGridLayout.addComponent(lblFullnameValue, iCol++, iRow);
		bidderDetailGridLayout.addComponent(lblDOB, iCol++, iRow);
		bidderDetailGridLayout.addComponent(lblDOBValue, iCol++, iRow);
		bidderDetailGridLayout.addComponent(lblIdNumber, iCol++, iRow);
		bidderDetailGridLayout.addComponent(lblIDNumberValue, iCol++, iRow);
		
		iCol = 0;
		iRow++;
		bidderDetailGridLayout.addComponent(lblPhoneNo, iCol++, iRow);
		bidderDetailGridLayout.addComponent(lblPhoneNoValue, iCol++, iRow);
		bidderDetailGridLayout.addComponent(lblEmail, iCol++, iRow);
		bidderDetailGridLayout.addComponent(lblEmailValue, iCol++, iRow);
		bidderDetailGridLayout.addComponent(lblAddress, iCol++, iRow);
		bidderDetailGridLayout.addComponent(lblAddressValue, iCol++, iRow);
		
		FieldSet fieldSet = new FieldSet();
		fieldSet.setLegend(I18N.message("detail"));
		fieldSet.setContent(bidderDetailGridLayout);
		
		Panel bidderDetailPanel = new Panel();
		bidderDetailPanel.setStyleName(Reindeer.PANEL_LIGHT);
		bidderDetailPanel.setContent(fieldSet);
		return bidderDetailPanel;
	}
	
	/**
	 * Outstanding Panel
	 * @return
	 */
	private FieldSet createOustandingPanel() {
		
		Label lblTotalDue = getLabel("total.due");
		Label lblSale = getLabel("sale");
		Label lblPenalties = getLabel("penalties");
		
		GridLayout outstandingGridLayout = ComponentLayoutFactory.getGridLayout(2, 3);
		outstandingGridLayout.setSpacing(true);
		outstandingGridLayout.setMargin(true);
		
		outstandingGridLayout.addComponent(lblTotalDue, 0, 0);
		outstandingGridLayout.addComponent(lblTotalDueValue, 1, 0);
	
		outstandingGridLayout.addComponent(lblSale, 0, 1);
		outstandingGridLayout.addComponent(lblSaleValue, 1, 1);
		
		outstandingGridLayout.addComponent(lblPenalties, 0, 2);
		outstandingGridLayout.addComponent(lblPenaltiesValue, 1, 2);
		
		FieldSet fieldSet = new FieldSet();
		fieldSet.setLegend(I18N.message("outstanding"));
		fieldSet.setContent(outstandingGridLayout);
		fieldSet.setSizeUndefined();
		
		return fieldSet;
	}
	
	/**
	 * 
	 * @return
	 */
	private FieldSet createPendingPanel() {
		Label lblTranfers = getLabel("tranfer");
		Label lblCheques = getLabel("cheques");
		Label lblTanfersAmount = getLabel("amount");
		Label lblChequesAmont = getLabel("amount");
		
		GridLayout pendingGridLayout = ComponentLayoutFactory.getGridLayout(4, 2);
		pendingGridLayout.setMargin(true);
		pendingGridLayout.setSpacing(true);
		pendingGridLayout.setHeight(79, Unit.PIXELS);
		
		pendingGridLayout.addComponent(lblTranfers, 0, 0);
		pendingGridLayout.addComponent(lblTranfersValue, 1, 0);
		pendingGridLayout.addComponent(lblCheques, 2, 0);
		pendingGridLayout.addComponent(lblChequesValue, 3, 0);
		
		pendingGridLayout.addComponent(lblTanfersAmount, 0, 1);
		pendingGridLayout.addComponent(lblTranfersAmountValue, 1, 1);
		pendingGridLayout.addComponent(lblChequesAmont, 2, 1);
		pendingGridLayout.addComponent(lblChequesAmountValue, 3, 1);
		
		FieldSet fieldSet = new FieldSet();
		fieldSet.setLegend(I18N.message("pending"));
		fieldSet.setContent(pendingGridLayout);
		fieldSet.setSizeUndefined();
	
		return fieldSet;
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
	 * @param caption
	 * @return
	 */
	private Label getLabel(String caption) {
		return ComponentFactory.getLabel(I18N.message(caption) + StringUtils.SPACE + ":");
	}
	
//	/**
//	 * 
//	 * @param value
//	 * @return
//	 */
//	private String getDescription(String value) {
//		StringBuffer stringBuffer = new StringBuffer();
//		stringBuffer.append("<b>");
//		stringBuffer.append(getDefaultString(value));
//		stringBuffer.append("</b>");
//		return stringBuffer.toString();
//	}
//	
	/**
	 * 
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnPrintReceipt) {
			
		} else if (event.getButton() == btnPrintTemporaryReceipt) {
			
		} else if (event.getButton() == btnMakePayment) {
			RecordPaymentPopupPanel window = new RecordPaymentPopupPanel(I18N.message("record.payment"));
			UI.getCurrent().addWindow(window);
		}
		
	}
}
