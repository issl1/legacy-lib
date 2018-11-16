package com.nokor.efinance.gui.ui.panel.payment.integration.file;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.document.panel.DocumentFileUploader;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.payment.model.EPaymentFileFormat;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.shared.conf.AppConfig;
import com.nokor.efinance.gui.ui.panel.payment.integration.file.list.FileIntegrationUploadWindow;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
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
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author uhout.cheng
 */
public class FileIntegrationHolderPanel extends AbstractControlPanel implements ClickListener, FinServicesHelper {
	
	/** */
	private static final long serialVersionUID = 3843270168100777201L;
	
	private ERefDataComboBox<EPaymentFileFormat> cbxPaymentFileFormat;
	private Upload  btnFileUpload;
	private DocumentFileUploader fileUploader;
	private Label lblFileName;
	
	private NativeButton btnSubmit;
	private NativeButton btnReset;
	
	private VerticalLayout messagePanel;
	
	private FileIntegrationUploadWindow fileUploadWindow;
	
	/**
	 * 
	 * @param fileUploadWindow
	 */
	public FileIntegrationHolderPanel(FileIntegrationUploadWindow fileUploadWindow) {
		this.fileUploadWindow = fileUploadWindow;
		setMargin(false);
		setSpacing(true);
		
		addComponent(createForm());
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	private Component createForm() {
		messagePanel = new VerticalLayout();
		messagePanel.setMargin(new MarginInfo(false, true, false, true));
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		cbxPaymentFileFormat = new ERefDataComboBox<>(EPaymentFileFormat.values());
		cbxPaymentFileFormat.setWidth(150, Unit.PIXELS);
		
		fileUploader = new DocumentFileUploader("PaymentFileIntegration");
		btnFileUpload = new Upload();
		btnFileUpload.setButtonCaption(I18N.message("browse"));
		btnFileUpload.setImmediate(true);
		btnFileUpload.setReceiver(fileUploader);
		btnFileUpload.addSucceededListener(new SucceededListener() {
			/** */
			private static final long serialVersionUID = 3380081745111938292L;
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
		
		btnSubmit = new NativeButton(I18N.message("submit"));
		btnSubmit.setIcon(FontAwesome.SAVE);
		btnSubmit.addClickListener(this);
		btnReset = new NativeButton(I18N.message("reset"));
		btnReset.setIcon(FontAwesome.REFRESH);
		btnReset.addClickListener(this);
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSubmit);
		navigationPanel.addButton(btnReset);
		
		Label lblFileFormat = ComponentLayoutFactory.getLabelCaptionRequired("file.format");
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		horizontalLayout.setMargin(new MarginInfo(false, true, true, true));
		horizontalLayout.addComponent(lblFileFormat);
		horizontalLayout.addComponent(cbxPaymentFileFormat);
		horizontalLayout.addComponent(btnFileUpload);
		horizontalLayout.addComponent(lblFileName);
		horizontalLayout.setComponentAlignment(lblFileName, Alignment.MIDDLE_LEFT);
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(messagePanel);
		contentLayout.addComponent(horizontalLayout);
		
		return contentLayout;
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSubmit) {
			submit();
		} else if (event.getButton() == btnReset) {
			reset();
		}
	}
	
	/**
	 * Submit
	 */
	private void submit() {
		removeErrorsPanel();
		String path = AppConfig.getInstance().getConfiguration().getString("document.path");
		String filePath = path + "/" + fileUploader.getFileNameDoc();
		if (validate(filePath)) {
			FILE_INTEGRATION_SRV.integrateFilePayment(filePath, cbxPaymentFileFormat.getSelectedEntity());
			displaySuccess();
		} else {
			displayErrorsPanel();
		}
	}
	
	/**
	 * 
	 * @param filePath
	 * @return
	 */
	private boolean validate(String filePath) {
		checkMandatorySelectField(cbxPaymentFileFormat, "file.format");
		if (StringUtils.isEmpty(fileUploader.getFileNameDoc())) {
			errors.add(I18N.message("field.required.1", I18N.message("file")));
		}
		if (cbxPaymentFileFormat.getSelectedEntity() != null && StringUtils.isNotEmpty(fileUploader.getFileNameDoc())) {
			
			String error = FILE_INTEGRATION_SRV.validateFileFormat(filePath, cbxPaymentFileFormat.getSelectedEntity());
			if (StringUtils.isNotEmpty(error)) {
				errors.add(error);
			}
		}
		return errors.isEmpty();
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	protected void reset() {
		super.reset();
		lblFileName.setValue(StringUtils.EMPTY);
		fileUploader.setFileNameDoc(StringUtils.EMPTY);
		cbxPaymentFileFormat.setSelectedEntity(null);
		removeErrorsPanel();
	}
	
	/**
	 * Remove Errors Panel
	 */
	private void removeErrorsPanel() {
		messagePanel.removeAllComponents();
		messagePanel.setVisible(false);
		errors.clear();
	}
	
	/**
	 * Display Errors Panel
	 */
	private void displayErrorsPanel() {
		messagePanel.removeAllComponents();
		for (String error : errors) {
			Label messageLabel = new Label(error);
			messageLabel.addStyleName("error");
			messagePanel.addComponent(messageLabel);
		}
		messagePanel.setVisible(true);
	}
	
	/**
	 * Display Success
	 */
	protected void displaySuccess() {
		ComponentLayoutFactory.displaySuccessMsg("msg.info.file.integration.successfully");
		reset();
		fileUploadWindow.refresh();
	}

}
