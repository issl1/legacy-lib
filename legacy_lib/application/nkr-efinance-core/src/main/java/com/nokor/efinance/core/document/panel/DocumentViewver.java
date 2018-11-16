package com.nokor.efinance.core.document.panel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Window;

public class DocumentViewver extends Window {

	private static final long serialVersionUID = 8093301531446556388L;

	/**
	 * @param tiltle
	 * @param filePathName
	 */
	public DocumentViewver(final String tiltle, final String filePathName) {
		super(tiltle);
		setModal(true);
        setResizable(true);
        setWidth("800px");
        setHeight("600px");
        center();
        BrowserFrame browserFrame = new BrowserFrame();
        StreamSource source = new StreamSource() {
        	private static final long serialVersionUID = 3558447485541502851L;
			@Override
			public InputStream getStream() {
                InputStream is = null;
				try {
					is = new FileInputStream(new File(filePathName));
				} catch (FileNotFoundException e) {
					is = null;
				}
                return is;
			}
		};
		browserFrame.setSource(new StreamResource(source, filePathName));
		browserFrame.setSizeFull();
        setContent(browserFrame);
	}
}
