package com.nokor.efinance.gui.ui.panel.payment.shop;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.document.panel.DocumentFileUploader;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.payment.model.PaymentFileItem;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.shared.conf.AppConfig;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author uhout.cheng
 */
public class PaymentAtShopFileUploadFormPanel extends AbstractFormPanel implements ClickListener, FinServicesHelper {
	
	/** */
	private static final long serialVersionUID = 4858186692295219905L;
	
	private Upload  btnFileUpload;
	private DocumentFileUploader fileUploader;
	private Label lblFileName;
	
	private NativeButton btnReset;
	
	private PaymentAtShopFileUploadWindow fileUploadWindow;
	
	/**
	 * 
	 * @param fileUploadWindow
	 */
	public PaymentAtShopFileUploadFormPanel(PaymentAtShopFileUploadWindow fileUploadWindow) {
		super.init();
		this.fileUploadWindow = fileUploadWindow;
		setMargin(false);
		
		btnReset = new NativeButton(I18N.message("reset"));
		btnReset.setIcon(FontAwesome.ERASER);
		btnReset.addClickListener(this);
		
		NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
		navigationPanel.addButton(btnReset);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		
		fileUploader = new DocumentFileUploader("PaymentAtShopFileUpload");
		btnFileUpload = new Upload();
		btnFileUpload.setButtonCaption(I18N.message("browse"));
		btnFileUpload.setImmediate(true);
		btnFileUpload.setReceiver(fileUploader);
		btnFileUpload.addSucceededListener(new SucceededListener() {
			
			/** */
			private static final long serialVersionUID = -7689612962761403702L;

			/**
			 * @see com.vaadin.ui.Upload.SucceededListener#uploadSucceeded(com.vaadin.ui.Upload.SucceededEvent)
			 */
			@Override
			public void uploadSucceeded(SucceededEvent event) {
				if (StringUtils.isNotEmpty(fileUploader.getFileNameDoc())) {
					lblFileName.setValue(fileUploader.getFileNameDoc());
				}
			}
		});
		lblFileName = ComponentFactory.getLabel();
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		horizontalLayout.setMargin(new MarginInfo(false, true, true, true));
		horizontalLayout.addComponent(btnFileUpload);
		horizontalLayout.addComponent(lblFileName);
		horizontalLayout.setComponentAlignment(lblFileName, Alignment.MIDDLE_LEFT);
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.addComponent(horizontalLayout);
		
		return contentLayout;
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnReset) {
			reset();
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#saveButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void saveButtonClick(ClickEvent event) {
		submit();
	}
	
	/**
	 * Submit
	 */
	private void submit() {
		if (validate()) {
			String path = AppConfig.getInstance().getConfiguration().getString("document.path");
			String fileName = fileUploader.getFileNameDoc();
			File file = new File(path + "/" + fileName);
			List<PaymentFileItem> blackListItems = getPaymentFileItems(file);
			try {
				ENTITY_SRV.saveOrUpdateBulk(blackListItems);
				reset();
				displaySuccess();
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			displayErrors();
		}
	}
	
	/**
	 * 
	 * @param fileName
	 * @return
	 */
    private boolean validateFileExtn(String fileName){
    	Pattern fileExtnPtrn = Pattern.compile("([^\\s]+(\\.(?i)(csv))$)");
        Matcher mtch = fileExtnPtrn.matcher(fileName);
        if(mtch.matches()){
            return true;
        }
        return false;
    }
	
    /**
	 * 
	 * @param file
	 * @return
	 */
	private List<PaymentFileItem> getPaymentFileItems(File file) {
		String cvsSplitBy = "\\|";
		String header = "Dealer ID|Contract NO|Date|Amount";
		int length = header.split(cvsSplitBy).length;
		List<PaymentFileItem> paymentFileItems = new ArrayList<PaymentFileItem>();
		if (file != null) {
			try {
				List<String> lines = FileUtils.readLines(file);
				for (int i = 0; i < lines.size(); i++) {
					if (i > 0) {
						String[] paymentFileItem = lines.get(i).split(cvsSplitBy, -1);
						if (length == paymentFileItem.length) {
							PaymentFileItem item = PaymentFileItem.createInstance();
							item.setDealerNo(paymentFileItem[0]);
							item.setCustomerRef1(paymentFileItem[1]);
							item.setPaymentDate(checkDate(paymentFileItem[2]));
							item.setAmount(StringUtils.isEmpty(paymentFileItem[3]) ? null : Double.parseDouble(paymentFileItem[3]));
							paymentFileItems.add(item);
						} 
					}
				}
			} catch (Exception e) {
				String errMsg = "Exception";
				logger.error(errMsg, e);
				Notification.show(errMsg, e.getMessage(), Notification.Type.ERROR_MESSAGE);
			}
		}
		return paymentFileItems;
	}
    
	/**
	 * 
	 * @param paymentDate
	 * @return
	 */
	private Date checkDate(String paymentDate) {
		return StringUtils.isEmpty(paymentDate) ? null : DateUtils.getDate(paymentDate, DateUtils.FORMAT_DDMMYYYY_SLASH);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	@Override
	protected boolean validate() {
		super.reset();
		if (StringUtils.isEmpty(fileUploader.getFileNameDoc())) {
			errors.add(I18N.message("field.required.1", I18N.message("file")));
		} else {
			if (!validateFileExtn(fileUploader.getFileNameDoc())) {
				errors.add(I18N.message("file.extension.incorrect"));
			}
		}
		return errors.isEmpty();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		lblFileName.setValue(StringUtils.EMPTY);
		fileUploader.setFileNameDoc(StringUtils.EMPTY);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#displaySuccess()
	 */
	@Override
	public void displaySuccess() {
		ComponentLayoutFactory.displaySuccessMsg("msg.info.file.upload.successfully");
		reset();
		fileUploadWindow.refresh();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
	 */
	@Override
	protected Entity getEntity() {
		return null;
	}

}
