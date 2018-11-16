package com.nokor.efinance.ra.ui.panel.blacklist;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

import com.nokor.efinance.core.shared.conf.AppConfig;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Upload.Receiver;

/**
 * 
 * @author uhout.cheng
 */
public class BlackListItemUploader implements Receiver {

	/** */
	private static final long serialVersionUID = 2329766689971861095L;
	
	private File file;
	private BlackListItemPopupWindow blackListItemPopupWindow;
	
	/**
	 * 
	 * @param blackListItemPopupWindow
	 */
	public BlackListItemUploader(BlackListItemPopupWindow blackListItemPopupWindow) {
		this.blackListItemPopupWindow = blackListItemPopupWindow;
	}
	
	/**
	 * @see com.vaadin.ui.Upload.Receiver#receiveUpload(java.lang.String, java.lang.String)
	 */
	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {
		FileOutputStream fos = null;
        try {
        	String documentDir = AppConfig.getInstance().getConfiguration().getString("document.path");
        	String dirPath = "BlackListItemCSV/" + (new Date().getTime());
        	String dirPathFileName = dirPath + "/" + filename;
        	blackListItemPopupWindow.getLblUploadDesc().setValue(filename);
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
