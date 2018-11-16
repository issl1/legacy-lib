package com.nokor.efinance.core.document.panel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.nokor.efinance.core.shared.conf.AppConfig;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

/**
 * @author ly.youhort
 *
 */
public class DisplayDocumentPanel extends Window {

	private static final long serialVersionUID = 7212903972039140830L;

	private String filePathName;
	
	public DisplayDocumentPanel(String filePathName) {
		setModal(true);
        setResizable(true);
        setWidth("800");
        setHeight("600");
        center();        
        this.filePathName = filePathName;
	}
	
	/**
	 * Display document
	 */
	public void display() {
		BrowserFrame browserFrame = new BrowserFrame();
        StreamSource source = new StreamSource() {
        	private static final long serialVersionUID = 3558447485541502851L;
			@Override
			public InputStream getStream() {
				String tmpDir = "";
            	if (filePathName.indexOf("tmp_") != -1) {
            		tmpDir = AppConfig.getInstance().getConfiguration().getString("specific.tmpdir"); 
            	} else {
            		tmpDir = AppConfig.getInstance().getConfiguration().getString("document.path");
            	}
                InputStream is = null;
				try {
					is = new FileInputStream(new File(tmpDir + "/" + filePathName));
				} catch (FileNotFoundException e) {
					is = null;
				}
                return is;
			}
		};
		browserFrame.setSource(null);
		browserFrame.setSource(new StreamResource(source, filePathName));
		browserFrame.setSizeFull();
		browserFrame.setImmediate(true);
        setContent(browserFrame);
        UI.getCurrent().addWindow(this);
	}
	
}
