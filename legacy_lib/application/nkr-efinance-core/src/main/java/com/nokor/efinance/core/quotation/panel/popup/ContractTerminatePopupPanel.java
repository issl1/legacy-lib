package com.nokor.efinance.core.quotation.panel.popup;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.service.aftersales.LossService;
import com.nokor.efinance.core.contract.service.aftersales.LossSimulateRequest;
import com.nokor.efinance.core.contract.service.aftersales.LossSimulateResponse;
import com.nokor.efinance.core.contract.service.aftersales.LossValidateRequest;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.Page;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Contract Terminate Pop up Panel
 * @author bunlong.taing
 */
public class ContractTerminatePopupPanel extends Window implements ClickListener {

	/** */
	private static final long serialVersionUID = -6647432839917810841L;
	
	private NativeButton btnSubmit;
	private NativeButton btnCancel;
	private PasswordField txtPassword;
	
	private Contract contract;
	private HorizontalLayout messagePanel;
	
	/**
	 * Contract Terminate Pop up Panel constructor
	 * @param caption
	 */
	public ContractTerminatePopupPanel(String caption) {
		super(I18N.message(caption));
		setModal(true);
		setResizable(false);
		setWidth(480, Unit.PIXELS);
		setHeight(150, Unit.PIXELS);
		init();
	}
	
	/**
	 * init components
	 */
	private void init() {
		messagePanel = new HorizontalLayout();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.setWidth(100, Unit.PERCENTAGE);
		messagePanel.addStyleName("message");
		
		btnSubmit = new NativeButton(I18N.message("submit"));
		btnCancel = new NativeButton(I18N.message("cancel"));
		btnSubmit.addClickListener(this);
		btnCancel.addClickListener(this);
		
		txtPassword = ComponentFactory.getPasswordField("password");
		txtPassword.setRequired(true);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSubmit);
		navigationPanel.addButton(btnCancel);
		
		FormLayout formLayout = new FormLayout();
		formLayout.addComponent(txtPassword);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		verticalLayout.addComponent(navigationPanel);
		verticalLayout.addComponent(messagePanel);
		verticalLayout.addComponent(formLayout);
		
		setContent(verticalLayout);
	}
	
	/**
	 * Assign values
	 * @param contract
	 */
	public void assignValue(Contract contract) {
		if (contract != null) {
			this.contract = contract;
		} else {
			MessageBox mb = new MessageBox(UI.getCurrent(), I18N.message("information"),
					MessageBox.Icon.INFO, I18N.message("contract.cannot.be.null"),
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.setWidth("300px");
			mb.setHeight("150px");
			mb.show();
			close();
		}
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSubmit) {
			if (validate()) {
				ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("confirm.terminate.contract"), new ConfirmDialog.Listener() {
					/** */
					private static final long serialVersionUID = -5351840836536105982L;
						public void onClose(ConfirmDialog dialog) {
							if (dialog.isConfirmed()) {
								submit();
								close();
				            }
						}
					});
					confirmDialog.setWidth("400px");
					confirmDialog.setHeight("150px");
			}
		} else if (event.getButton() == btnCancel) {
			close();
		}
	}
	
	/**
	 * Validate password
	 * @return
	 */
	private boolean validate() {
		boolean valid = false;
		messagePanel.setVisible(false);
		messagePanel.removeAllComponents();
		if (StringUtils.isEmpty(txtPassword.getValue())) {
			displayErrors(I18N.message("field.required.1", new String[] { I18N.message("password") }));
		} else {

//			if () {
//				displayErrors(I18N.message("access.denied"));
//			} else {
//				valid = true;
//			}
			valid = true;
		}
		return valid;
	}
	
	/**
	 * submit
	 */
	private void submit() {
		LossService lossService = SpringUtils.getBean(LossService.class);
		LossSimulateRequest simulateRequest = new LossSimulateRequest();
		simulateRequest.setCotraId(contract.getId());
		simulateRequest.setEventDate(DateUtils.today());
		LossSimulateResponse simulateResponse = lossService.simulate(simulateRequest);
		
		LossValidateRequest validateRequest = new LossValidateRequest();
		validateRequest.setCotraId(simulateResponse.getCotraId());
		validateRequest.setCashflows(simulateResponse.getCashflows());
		validateRequest.setTotalPrincipal(simulateResponse.getTotalPrincipal());
		validateRequest.setTotalInterest(simulateResponse.getTotalInterest());
		validateRequest.setTotalOther(simulateResponse.getTotalOther());
		validateRequest.setInsuranceFee(simulateResponse.getInsuranceFee());
		validateRequest.setServicingFee(simulateResponse.getServicingFee());
		validateRequest.setEventDate(simulateResponse.getEventDate());
		validateRequest.setWkfStatus(ContractWkfStatus.WRI);
		lossService.validate(validateRequest);
		
		Notification notification = new Notification("", Type.HUMANIZED_MESSAGE);
		notification.setDescription(I18N.message("msg.info.save.successfully"));
		notification.setDelayMsec(3000);
		notification.show(Page.getCurrent());
		close();
	}
	
	/**
	 * Display errors
	 */
	public void displayErrors(String message) {
		messagePanel.removeAllComponents();
		Label messageLabel = new Label(message);
		messageLabel.addStyleName("error");
		messagePanel.addComponent(messageLabel);
		messagePanel.setVisible(true);
	}

}
