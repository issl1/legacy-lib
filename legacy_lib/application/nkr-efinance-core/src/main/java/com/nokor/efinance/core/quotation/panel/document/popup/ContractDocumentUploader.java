package com.nokor.efinance.core.quotation.panel.document.popup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

import com.nokor.efinance.core.contract.panel.ContractDocumentPanel;
import com.nokor.efinance.core.shared.conf.AppConfig;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Upload.Receiver;

/**
 * 
 * @author buntha.chea
 *
 */
public class ContractDocumentUploader implements Receiver {

	/**  */
	private static final long serialVersionUID = 1830998788107740134L;
	
	private File file;
	private ContractDocumentPanel contractDocumentPanel;
		
	public ContractDocumentUploader(ContractDocumentPanel contractDocumentPanel) {
		this.contractDocumentPanel = contractDocumentPanel;
	}

	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {
		FileOutputStream fos = null;
        try {
        	String documentDir = AppConfig.getInstance().getConfiguration().getString("document.path");
        	String dirPath = "ContractDocuments/" + (new Date().getTime());
        	String dirPathFileName = dirPath + "/" + filename;
        	contractDocumentPanel.getLblUploadDesc().setValue(filename);
        	File tmpDirPath = new File(documentDir + "/" + dirPath);
        	if (!tmpDirPath.exists()) {
				tmpDirPath.mkdirs();
        	}	        	
            file = new File(documentDir + "/" + dirPathFileName);
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            Notification.show("Could not open file<br/>", e.getMessage(), Type.ERROR_MESSAGE);
            return null;
        }
        return fos; 
	}
	
	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

}
