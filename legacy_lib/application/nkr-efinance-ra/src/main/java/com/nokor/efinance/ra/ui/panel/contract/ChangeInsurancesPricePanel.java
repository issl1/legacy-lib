package com.nokor.efinance.ra.ui.panel.contract;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.MyMathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.service.ContractService;
import com.nokor.efinance.core.shared.conf.AppConfig;
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
 * @author ly.youhort
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(ChangeInsurancesPricePanel.NAME)
public class ChangeInsurancesPricePanel extends Panel implements View {
	
	private static final long serialVersionUID = -2149707107396177780L;
	
	protected Logger logger = LoggerFactory.getLogger(getClass());

	public static final String NAME = "change.insurances.price";
	
	@Autowired
	private ContractService contractService;

	@PostConstruct
	public void PostConstruct() {
		
		setCaption(I18N.message("Change Insurances Price"));
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		verticalLayout.setSpacing(true);
		
		Upload upload = new Upload();
		upload.setButtonCaption(I18N.message("Upload Excel"));
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
	 * @author ly.youhort
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
					Map<Long, Double> priceMaps = new HashMap<>(); 
					stream = new FileInputStream(file);
					HSSFWorkbook wb = new HSSFWorkbook(stream);
					HSSFSheet sheet = wb.getSheetAt(0);
					Iterator<Row> rowIterator = sheet.iterator();
					int i = 0;
				    while (rowIterator.hasNext()) {
				    	Row row = rowIterator.next();
				    	
				    	String debug = "";
				    	
				    	if (i > 1) {
					        String reference = row.getCell(0).getStringCellValue();
					        Double price = row.getCell(1).getNumericCellValue();
					        
					        if (price == null || price <= 0) {
					        	throw new IllegalArgumentException("Price not correct");
					        }
					        
					        Contract contract = contractService.getByReference(reference);
					        
					        if (contract == null) {
					        	throw new IllegalArgumentException("Contract not found");
					        }
					        
					        priceMaps.put(contract.getId(), price);
					        
					        logger.error(debug);
				    	}
				    	i++;
				    }
				    
				    int j = 0;
				    String message = "";
				    String messageUpdate = "";
				    String messageNotUpdate = "";
				    for (Iterator<Long> iter = priceMaps.keySet().iterator(); iter.hasNext(); ) {
				    	BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<>(Cashflow.class);
				    	Long id = iter.next();
				    	
				    	Contract contract = contractService.getById(Contract.class, id);
				    	
						restrictions.addCriterion(Restrictions.eq("contract.id", id));
						restrictions.addCriterion(Restrictions.eq("cashflowType", ECashflowType.FEE));
						restrictions.addCriterion(Restrictions.eq("paid", false));
						restrictions.addCriterion(Restrictions.eq("cancel", false));
						restrictions.addCriterion(Restrictions.eq("service.id", new Long(2)));
						restrictions.addCriterion(Restrictions.gt("numInstallment", 11));
						restrictions.addCriterion(Restrictions.gt("tiInstallmentUsd", 30d));
						
						List<Cashflow> cashflowsFee = contractService.list(restrictions);		        
											        
						if (cashflowsFee != null && !cashflowsFee.isEmpty()) {
							for (Cashflow cashflowFee : cashflowsFee) {
								cashflowFee.setTiInstallmentAmount(MyMathUtils.roundAmountTo(priceMaps.get(id)));
								cashflowFee.setTeInstallmentAmount(cashflowFee.getTiInstallmentAmount());
								contractService.saveOrUpdate(cashflowFee);
							}
							messageUpdate += contract.getReference() + ";" + MyMathUtils.roundAmountTo(priceMaps.get(id)) + "\n";
						} else {
							messageNotUpdate += contract.getReference() + ";" + MyMathUtils.roundAmountTo(priceMaps.get(id)) + "\n";
						}
						message += contract.getReference() + ";" + MyMathUtils.roundAmountTo(priceMaps.get(id)) + "\n";
						j++;
				    }
 				    
				    logger.error("Nb Update Insurance Prices = " + j);
				    logger.error(message); 
				    logger.error("=============================================");
				    logger.error("Contract Updated");
				    logger.error(messageUpdate);
				    logger.error("=============================================");
				    logger.error("Nb Contract not Updated");
				    logger.error(messageNotUpdate);
				    
				    Notification notification = new Notification("", Type.HUMANIZED_MESSAGE);
					notification.setDescription(I18N.message("Change OK"));
					notification.setDelayMsec(3000);
					notification.show(Page.getCurrent());
				    
				} catch (FileNotFoundException e) {
					logger.error("FileNotFoundException", e);
				} catch (IOException e) {
					logger.error("IOException", e);
				}
	        }
	    }
	};
}
