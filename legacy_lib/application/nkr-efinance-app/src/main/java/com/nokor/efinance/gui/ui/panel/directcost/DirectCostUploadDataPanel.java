package com.nokor.efinance.gui.ui.panel.directcost;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.service.ContractInterfaceService;
import com.nokor.efinance.core.contract.service.ContractService;
import com.nokor.efinance.core.contract.service.cashflow.CashflowService;
import com.nokor.efinance.core.financial.model.FinService;
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
 * @author buntha.chea
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(DirectCostUploadDataPanel.NAME)
public class DirectCostUploadDataPanel extends Panel implements View {
	
	private static final long serialVersionUID = 6227740006388204118L;

	protected Logger logger = LoggerFactory.getLogger(getClass());

	public static final String NAME = "direct.cost.upload";
	
	public ContractInterfaceService contractInterfaceService = SpringUtils.getBean(ContractInterfaceService.class);
	public ContractService contractService = SpringUtils.getBean(ContractService.class);
	public CashflowService cashflowService = SpringUtils.getBean(CashflowService.class);
	
	@PostConstruct
	public void PostConstruct() {
		
		setCaption(I18N.message("direct.cost"));
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		verticalLayout.setSpacing(true);
		
		Upload upload = new Upload();
		upload.setButtonCaption(I18N.message("upload"));
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
	 * @author buntha.chea
	 */
	private class FileUploader implements Receiver, SucceededListener {
		private static final long serialVersionUID = -4738310396410864219L;
		private String strPartUpload = "";
		private File file;
		
	    public OutputStream receiveUpload(String filename, String mimeType) {
	        FileOutputStream fos = null;
	        try {
	        	String tmpDir = AppConfig.getInstance().getConfiguration().getString("specific.tmpdir");
	        	strPartUpload = tmpDir  + "/" + filename;
	            file = new File(tmpDir  + "/" + filename);
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
					readExcelFile(strPartUpload, true);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	};
	
	//Read Data From Excel File Uploaded
	public void readExcelFile(String templateFileName, boolean isMessage) {
		
	    try {
	    	File excel =  new File (templateFileName);
	        FileInputStream fis = new FileInputStream(excel);
	        XSSFWorkbook wb = new XSSFWorkbook(fis);
	        XSSFSheet ws = wb.getSheetAt(0);
	        int rowNum = ws.getLastRowNum() + 1;	        
	        List<DirectCost> directCosts = new ArrayList<>();
	        for (int i = 0; i <rowNum; i++) {
	        	XSSFRow row = ws.getRow(i);
	        	if (row != null) {
	        		String contractReference = String.valueOf(row.getCell(0)).trim();
	        		if (StringUtils.isNotBlank(contractReference)) {
	                	String directCostCode = String.valueOf(row.getCell(1)).trim();
	                	double directCostAmount = Double.parseDouble(String.valueOf(row.getCell(2)));
	                	double vatDirectCostAmount = Double.parseDouble(String.valueOf(row.getCell(3)));
	                	directCosts.add(new DirectCost(contractReference, directCostCode, new Amount(directCostAmount, vatDirectCostAmount, directCostAmount)));
	        		}
	        	}
	        }
	        fis.close();
	        addDirectCost(directCosts);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			Notification notification = new Notification("", Type.ERROR_MESSAGE);
			notification.setDescription(I18N.message("could.not.migration.direct.cost"));
			notification.setDelayMsec(3000);
			notification.show(Page.getCurrent());
		}
	}
	
	/**
	 * @param directCosts
	 */
	private void addDirectCost(List<DirectCost> directCosts) {
		boolean error = false;		
		for (DirectCost directCost : directCosts) {
			if (directCost.directCostAmount.getTiAmount() <= 0d) {
				error = true;
				Notification notification = new Notification("", Type.ERROR_MESSAGE);
				notification.setDescription("Amount Direct Cost of contract " + directCost.contractReference + " must be greater than ZERO");
				notification.show(Page.getCurrent());
				break;
			} else {
				Contract contract = contractService.getByReference(directCost.contractReference);
				if (contract == null) {
					error = true;
					Notification notification = new Notification("", Type.ERROR_MESSAGE);
					notification.setDescription("Contract " + directCost.contractReference + " is not fount in systeme, please amend the XLS file.");
					notification.show(Page.getCurrent());
					break;
				} else {
					FinService service = contractService.getByCode(FinService.class, directCost.directCostCode);
					if (service == null) {
						error = true;
						Notification notification = new Notification("", Type.ERROR_MESSAGE);
						notification.setDescription("Direct Cost " + directCost.directCostCode + " is not fount in systeme, please amend the XLS file.");
						notification.show(Page.getCurrent());
						break;
					} else {
						if (!cashflowService.getServiceCashflowsOfContract(contract.getId(), service.getId()).isEmpty()) {
							error = true;
							Notification notification = new Notification("", Type.ERROR_MESSAGE);
							notification.setDescription("Direct Cost " + directCost.directCostCode + " is already applied to contract " + directCost.contractReference + ", please remove from XLS file.");
							notification.show(Page.getCurrent());
							break;
						}
					}
				}
			}
		}
		
		if (!error) {
			for (DirectCost directCost : directCosts) {
				contractInterfaceService.addDirectCost(directCost.contractReference, directCost.directCostCode, directCost.directCostAmount);
			}
			Notification notification = new Notification("", Type.HUMANIZED_MESSAGE);
			notification.setDescription(I18N.message("add.direct.cost.successfully"));
			notification.setDelayMsec(3000);
			notification.show(Page.getCurrent());
		}
	}
		
	private class DirectCost {
		public String contractReference;
		public String directCostCode;
		public Amount directCostAmount;
		
		/**
		 * @param contractReference
		 * @param directCostCode
		 * @param directCostAmount
		 */
		public DirectCost(String contractReference, String directCostCode, Amount directCostAmount) {
			this.contractReference = contractReference; 
			this.directCostCode = directCostCode;
			this.directCostAmount = directCostAmount;
		}
	}
}
