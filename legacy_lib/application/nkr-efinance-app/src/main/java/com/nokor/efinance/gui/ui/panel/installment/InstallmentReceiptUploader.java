package com.nokor.efinance.gui.ui.panel.installment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.payment.model.InstallmentReceipt;
import com.nokor.efinance.core.shared.conf.AppConfig;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

/**
 * @author bunlong.taing
 */
public class InstallmentReceiptUploader implements Receiver, SucceededListener {

	/** */
	private static final long serialVersionUID = 7694700591876533901L;
	private EntityService entityService = SpringUtils.getBean(EntityService.class);
	private File file;
	private String fileNameReceipt;
	private InstallmentReceiptsPanel installmentReceiptsPanel;
	
	/**
	 * 
	 * @param receipt
	 */
	public InstallmentReceiptUploader (InstallmentReceiptsPanel installmentReceiptsPanel) {
		this.installmentReceiptsPanel = installmentReceiptsPanel;
	}

	/**
	 * @see com.vaadin.ui.Upload.SucceededListener#uploadSucceeded(com.vaadin.ui.Upload.SucceededEvent)
	 */
	@Override
	public void uploadSucceeded(SucceededEvent event) {
		Dealer dealer = installmentReceiptsPanel.getDealer();
		Date date = installmentReceiptsPanel.getDate(); 
		
		InstallmentReceipt receipt = new InstallmentReceipt();
		if (dealer == null) {
			Notification.show("", I18N.message("dealer.not.selected"), Type.ERROR_MESSAGE);
		} else {
			receipt.setDealer(dealer);
			receipt.setUserUpload((SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
			receipt.setPath(fileNameReceipt);
			receipt.setUploadDate(date == null ? DateUtils.today() : date);
			entityService.saveOrUpdate(receipt);
			MessageBox mb = new MessageBox(UI.getCurrent(), "400px", "160px", I18N.message("information"),
					MessageBox.Icon.INFO, I18N.message("save.successfully"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
			installmentReceiptsPanel.refresh();
		}
	}

	/**
	 * @see com.vaadin.ui.Upload.Receiver#receiveUpload(java.lang.String, java.lang.String)
	 */
	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {
		FileOutputStream fos = null;
        try {
        	Dealer dealer = installmentReceiptsPanel.getDealer();
        	String documentDir = AppConfig.getInstance().getConfiguration().getString("document.path");
        	String dirPath = "InstallmentReceipts/" + dealer.getId() + "/" + (new Date().getTime());
        	fileNameReceipt = dirPath + "/" + filename;
        	File tmpDirPath = new File(documentDir + "/" + dirPath);
        	if (!tmpDirPath.exists()) {
				tmpDirPath.mkdirs();
        	}	        	
            file = new File(documentDir + "/" + fileNameReceipt);
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            Notification.show("Could not open file<br/>", e.getMessage(), Type.ERROR_MESSAGE);
            return null;
        }
        return fos; 
	}
	
}
