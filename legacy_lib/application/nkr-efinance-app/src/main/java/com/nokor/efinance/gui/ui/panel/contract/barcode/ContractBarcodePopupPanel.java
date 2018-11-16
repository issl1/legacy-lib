package com.nokor.efinance.gui.ui.panel.contract.barcode;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.tools.barcode.ContractBarcode;
import com.vaadin.server.FileResource;
import com.vaadin.ui.Image;
import com.vaadin.ui.Window;

/**
 * Window show contract barcode
 * @author bunlong.taing
 */
public class ContractBarcodePopupPanel extends Window {
	/** */
	private static final long serialVersionUID = 4172515066909890268L;
	
	/**
	 */
	public ContractBarcodePopupPanel() {
		setCaption(I18N.message("contract"));
		setModal(true);
		setResizable(false);
	}
	
	/**
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		setContent(null);
		if (contract != null) {
			FileResource resource = new FileResource(ContractBarcode.create(contract));
			Image img = new Image(null, resource);
			setContent(img);
		}
	}

}
