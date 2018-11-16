package com.nokor.efinance.ra.ui.panel.referential.matrixPrice.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Iterator;

import javax.annotation.PostConstruct;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.service.EntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.financial.model.AssetMatrixPrice;
import com.nokor.efinance.core.financial.model.FinService;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.conf.AppConfig;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
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
@VaadinView(ServiceUploadMatrixPricePanel.NAME)
public class ServiceUploadMatrixPricePanel extends Panel implements View, FMEntityField {

	private static final long serialVersionUID = 4406052780869554555L;

	protected Logger logger = LoggerFactory.getLogger(getClass());

	public static final String NAME = "upload.matrix.prices";
	
	private EntityService entityService = (EntityService) SecApplicationContextHolder.getContext().getBean("entityService");

	@PostConstruct
	public void PostConstruct() {
		
		setCaption(I18N.message("upload.matrix.prices"));
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		verticalLayout.setSpacing(true);
		
		Upload uploadMatrixPrice = new Upload();
		uploadMatrixPrice.setButtonCaption(I18N.message("upload"));
		final FileUploader uploader = new FileUploader(); 
		uploadMatrixPrice.setReceiver(uploader);
		uploadMatrixPrice.addSucceededListener(uploader);
		verticalLayout.addComponent(uploadMatrixPrice);		
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
		
		private File file;
		
	    public OutputStream receiveUpload(String filename, String mimeType) {
	        FileOutputStream fos = null;
	        try {
	        	String tmpDir = AppConfig.getInstance().getConfiguration().getString("specific.tmpdir");
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
	        	InputStream stream;
				try {
					stream = new FileInputStream(file);
					XSSFWorkbook wb = new XSSFWorkbook(stream);
					XSSFSheet sheet = wb.getSheetAt(0);
					Iterator<Row> rowIterator = sheet.iterator();
					int i = 0;
					boolean isSuccessUpload = false;
				    while (rowIterator.hasNext()) {
				    	Row row = rowIterator.next();
				    	if (i > 0) {
				    		isSuccessUpload = false;
				    		FinService service = null;
				    		AssetModel assetModel = null;
				    		if (row.getCell(0) != null) {
				    			String assetModelCode = row.getCell(0).getStringCellValue();
				    			assetModel = entityService.getByCode(AssetModel.class, assetModelCode);
				    		}
				    		if (row.getCell(1) != null) {
				    			String serviceCode = row.getCell(1).getStringCellValue();
				    			service = entityService.getByCode(FinService.class, serviceCode);
				    		}
					        double price = row.getCell(2).getNumericCellValue();
					        Date date = row.getCell(3).getDateCellValue();
					        if (price != 0) {
								AssetMatrixPrice matrixPrice = new AssetMatrixPrice();
								matrixPrice.setAssetModel(assetModel);
								matrixPrice.setService(service);
								matrixPrice.setTiPriceUsd(price);
								matrixPrice.setTePriceUsd(price);
								matrixPrice.setVatPriceUsd(0d);
								matrixPrice.setDate(date);
								entityService.saveOrUpdate(matrixPrice);
								isSuccessUpload = true;
					        }
				    	}
				    	i++;
				    	if(isSuccessUpload){
				    		Notification notification = new Notification("", Type.HUMANIZED_MESSAGE);
							notification.setDescription(I18N.message("upload.successfully"));
							notification.setDelayMsec(3000);
							notification.show(Page.getCurrent());
				    	}
				    }
				} catch (FileNotFoundException e) {
					logger.error("FileNotFoundException", e);
				} catch (IOException e) {
					logger.error("IOException", e);
				}
	        }
	    }
	};
}
