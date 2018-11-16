package com.nokor.efinance.core.document.panel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

import org.apache.commons.io.output.NullOutputStream;
import org.apache.commons.lang.StringUtils;

import com.nokor.efinance.core.shared.conf.AppConfig;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

/**
 * @author ly.youhort
 */
public class DocumentUploader implements Receiver, SucceededListener {
	
	private static final long serialVersionUID = -4738310396410864219L;
	
	private File file;
	private String tmpFileName;
	private CheckBox cbDocument;
	private Button btnPath;
	private Upload uploadComponent;
	
	public DocumentUploader(CheckBox cbDocument, Button btnPath, Upload uploadComponent) {
		this.cbDocument = cbDocument;
		this.btnPath = btnPath;
		this.uploadComponent = uploadComponent;
	}
	
    public OutputStream receiveUpload(String filename, String mimeType) {
        FileOutputStream fos = null;
        if (StringUtils.isEmpty(filename)) {
        	uploadComponent.interruptUpload();
        	return new NullOutputStream();
        }
        try {
        	String tmpDir = AppConfig.getInstance().getConfiguration().getString("specific.tmpdir");
        	String tmpPath = "tmp_" + (new Date().getTime());
        	tmpFileName = tmpPath + "/" + filename;
        	File tmpDirPath = new File(tmpDir + "/" + tmpPath);
        	if (!tmpDirPath.exists()) {
				tmpDirPath.mkdirs();
        	}	        	
            file = new File(tmpDir + "/" + tmpFileName);
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            Notification.show("Could not open file<br/>", e.getMessage(), Type.ERROR_MESSAGE);
            return null;
        }
        return fos; // Return the output stream to write to
    }
    public void uploadSucceeded(SucceededEvent event) {
    	this.cbDocument.setValue(true);
    	btnPath.setData(tmpFileName);
    	btnPath.setVisible(true);
    }
}
