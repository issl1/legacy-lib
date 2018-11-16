package com.nokor.efinance.gui.ui.panel.contract.document;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.gui.ui.panel.contract.barcode.ContractBarcodePopupPanel;
import com.nokor.efinance.gui.ui.panel.contract.document.popup.PrintDocumentPopupWindow;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;


/**
 * Document panel last version in CM
 * @author uhout.cheng
 */
public class DocumentPanel extends AbstractTabPanel implements ClickListener {

	/** */
	private static final long serialVersionUID = 7234044050542642085L;
	
	private DocumentsTablePanel documentTablePanel;
	private Button btnOrderToPrint;
	private Button btnPrintBarcode;
	private DocumentHistoriesTablePanel documentHistoriesPanel;
	
	private ContractBarcodePopupPanel barcodePopup;
	private Contract contract;
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		super.setMargin(false);
		barcodePopup = new ContractBarcodePopupPanel();
		documentTablePanel = new DocumentsTablePanel();
		btnOrderToPrint = ComponentLayoutFactory.getButtonStyle("order.print", FontAwesome.PRINT, 160, "btn btn-success button-small");
		btnOrderToPrint.addClickListener(this);
		btnPrintBarcode = ComponentLayoutFactory.getButtonStyle("print", FontAwesome.PRINT, 100, "btn btn-success button-small");
		btnPrintBarcode.addClickListener(this);
		documentHistoriesPanel = new DocumentHistoriesTablePanel();
		
		HorizontalLayout printLayout = new HorizontalLayout();
		printLayout.setSpacing(true);
		printLayout.addComponent(btnOrderToPrint);
		printLayout.addComponent(btnPrintBarcode);
		
		VerticalLayout contentLayout = ComponentLayoutFactory.getVerticalLayout(false, true);
		contentLayout.addComponent(documentTablePanel);
		contentLayout.addComponent(printLayout);
		contentLayout.addComponent(documentHistoriesPanel);
		return contentLayout;
	}
	
	/**
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		reset();
		if (contract != null) {
			this.contract = contract;
		}
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnOrderToPrint)) {
			UI.getCurrent().addWindow(new PrintDocumentPopupWindow());
		} else if (event.getButton().equals(btnPrintBarcode)) {
			barcodePopup.assignValues(contract);
			UI.getCurrent().addWindow(barcodePopup);
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	protected void reset() {
		super.reset();
		contract = null;
		barcodePopup.assignValues(null);
	}
	
}
