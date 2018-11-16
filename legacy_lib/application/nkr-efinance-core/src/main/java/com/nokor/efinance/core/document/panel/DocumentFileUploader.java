package com.nokor.efinance.core.document.panel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

import org.apache.commons.io.output.NullOutputStream;
import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.shared.conf.AppConfig;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

/**
 * Document Template Uploader
 * @author bunlong.taing
 */
public class DocumentFileUploader implements Receiver, SucceededListener {

	/** */
	private static final long serialVersionUID = 7258525987573817909L;
	private File file;
	private String fileNameDoc;
	private String dirName;
	
	/**
	 * @param dirName Directory name
	 */
	public DocumentFileUploader (String dirName) {
		this.dirName = dirName;
	}

	/**
	 * @see com.vaadin.ui.Upload.SucceededListener#uploadSucceeded(com.vaadin.ui.Upload.SucceededEvent)
	 */
	@Override
	public void uploadSucceeded(SucceededEvent event) {
		if (StringUtils.isNotEmpty(getFileNameDoc())) { 
			Notification notification = new Notification("");
			notification.setDescription(I18N.message("upload.successfully"));
			notification.show(UI.getCurrent().getPage());
		}
	}

	/**
	 * @see com.vaadin.ui.Upload.Receiver#receiveUpload(java.lang.String, java.lang.String)
	 */
	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {
		FileOutputStream fos = null;
		if (StringUtils.isEmpty(filename)) {
			setFileNameDoc(null);
        	return new NullOutputStream();
        }
        try {
        	String documentDir = AppConfig.getInstance().getConfiguration().getString("document.path");
        	String dirPath = dirName + "/" + (new Date().getTime());
        	fileNameDoc = dirPath + "/" + filename;
        	File tmpDirPath = new File(documentDir + "/" + dirPath);
        	if (!tmpDirPath.exists()) {
				tmpDirPath.mkdirs();
        	}	        	
            file = new File(documentDir + "/" + fileNameDoc);
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            Notification.show("Could not open file<br/>", e.getMessage(), Type.ERROR_MESSAGE);
            return null;
        }
        return fos; 
	}
	
	/**
	 * Document Template file name uploaded
	 * @return Document Template's name
	 */
	public String getFileNameDoc () {
		return this.fileNameDoc;
	}

	/**
	 * @param fileNameDoc the fileNameDoc to set
	 */
	public void setFileNameDoc(String fileNameDoc) {
		this.fileNameDoc = fileNameDoc;
	}

}
