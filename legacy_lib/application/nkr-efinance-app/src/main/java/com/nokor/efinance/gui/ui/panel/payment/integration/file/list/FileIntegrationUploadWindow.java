package com.nokor.efinance.gui.ui.panel.payment.integration.file.list;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.gui.ui.panel.payment.integration.file.FileIntegrationHolderPanel;
import com.vaadin.ui.Window;

/**
 * 
 * @author uhout.cheng
 */
public class FileIntegrationUploadWindow extends Window {
	
	/** */
	private static final long serialVersionUID = 8227351838569301948L;
	
	private FileIntegrationHolderPanel integrationHolderPanel;
	
	private PaymentFileTablePanel tablePanel;
	
	/**
	 * 
	 * @param tablePanel
	 */
	public FileIntegrationUploadWindow(PaymentFileTablePanel tablePanel) {
		this.tablePanel = tablePanel;
		setModal(true);
		setWidth(70, Unit.PERCENTAGE);
		setCaption(I18N.message("files.integration"));
		
		integrationHolderPanel = new FileIntegrationHolderPanel(this);
		setContent(integrationHolderPanel);
	}
	
	/**
	 * 
	 */
	public void refresh() {
		tablePanel.refresh();
		close();
	}

}
