package com.nokor.efinance.ra.ui.panel.tools;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.quotation.MigrationQuotationService;
import com.nokor.efinance.core.shared.conf.AppConfig;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;

/**
 * @author sok.vina
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(UpdateMigrationContractPanel.NAME)
public class UpdateMigrationContractPanel extends Panel implements View {
	
	private static final long serialVersionUID = 6227740006388204118L;

	protected Logger logger = LoggerFactory.getLogger(getClass());

	public static final String NAME = "update.migration.contract";
	
	@Autowired
	private MigrationQuotationService migrationQuotationService;
	@PostConstruct
	public void PostConstruct() {
		
		setCaption(I18N.message("Update migration contract"));
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		verticalLayout.setSpacing(true);
		
		Upload upload = new Upload();
		upload.setButtonCaption(I18N.message("Update migration contract"));
		final FileUploader uploader = new FileUploader(); 
		upload.setReceiver(uploader);
		upload.addSucceededListener(uploader);
		verticalLayout.addComponent(upload);
		setContent(verticalLayout);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	/**
	 * @author sok.vina
	 */
	private class FileUploader implements Receiver, SucceededListener {
		private static final long serialVersionUID = -4738310396410864219L;
		private String strPartUpload = "";
		private File file;
		
	    public OutputStream receiveUpload(String filename, String mimeType) {
	        FileOutputStream fos = null;
	        try {
	        	String tmpDir = AppConfig.getInstance().getConfiguration().getString("specific.tmpdir");
	        	strPartUpload = tmpDir + "/" + filename;
	            file = new File(tmpDir + "/" + filename);
	            fos = new FileOutputStream(file);
	        } catch (FileNotFoundException e) {
	            Notification.show("Could not open file<br/>", e.getMessage(), Type.ERROR_MESSAGE);
	            return null;
	        }
	        return fos;
	    }

		public void uploadSucceeded(SucceededEvent event) {
			if (file != null) {
				try {
					migrationQuotationService.updateMigrationContract(strPartUpload, true);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	};

}
