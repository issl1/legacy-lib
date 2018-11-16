package com.nokor.efinance.gui.ui.panel.payment.shop;

import org.seuksa.frmk.i18n.I18N;

import com.vaadin.ui.Window;

/**
 * 
 * @author uhout.cheng
 */
public class PaymentAtShopFileUploadWindow extends Window {
	
	/** */
	private static final long serialVersionUID = 4333845377181903550L;

	private PaymentAtShopFileUploadFormPanel fileUploadFormPanel;
	
	private PaymentAtShopTablePanel tablePanel;
	
	/**
	 * 
	 * @param tablePanel
	 */
	public PaymentAtShopFileUploadWindow(PaymentAtShopTablePanel tablePanel) {
		this.tablePanel = tablePanel;
		setModal(true);
		setWidth(60, Unit.PERCENTAGE);
		setCaption(I18N.message("files.upload"));
		
		fileUploadFormPanel = new PaymentAtShopFileUploadFormPanel(this);
		setContent(fileUploadFormPanel);
	}
	
	/**
	 * 
	 */
	public void refresh() {
		tablePanel.refresh();
		close();
	}

}
