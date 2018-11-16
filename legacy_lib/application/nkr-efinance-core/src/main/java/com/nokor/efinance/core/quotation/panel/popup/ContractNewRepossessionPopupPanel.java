package com.nokor.efinance.core.quotation.panel.popup;

import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.service.aftersales.LossService;
import com.nokor.efinance.core.contract.service.aftersales.LossSimulateRequest;
import com.nokor.efinance.core.contract.service.aftersales.LossSimulateResponse;
import com.nokor.efinance.core.contract.service.aftersales.LossValidateRequest;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.server.Page;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Contract New Repossession Pop up Panel
 * @author bunlong.taing
 */
public class ContractNewRepossessionPopupPanel extends Window implements ClickListener {

	/** */
	private static final long serialVersionUID = -7401089272241298258L;
	
	private NativeButton btnSubmit;
	private NativeButton btnCancel;
	
	private AutoDateField dfForeclosureDate;
	
	// private TextField txtForeclosureLocation;
	private TextField txtForeclosureStaffId;
	private TextField txtForeclosureStaffName;
	// private TextField txtForeclosureStaffPhone;

	private Contract contract;
	
	/**
	 * Contract New Repossession Pop up Panel
	 * @param caption
	 */
	public ContractNewRepossessionPopupPanel(String caption) {
		super(I18N.message(caption));
		setModal(true);
		setResizable(false);
		setWidth(420, Unit.PIXELS);
		setHeight(300, Unit.PIXELS);
		init();
	}
	
	/**
	 * Init components
	 */
	private void init() {
		btnSubmit = new NativeButton(I18N.message("submit"));
		btnCancel = new NativeButton(I18N.message("cancel"));
		btnSubmit.addClickListener(this);
		btnCancel.addClickListener(this);
		
		dfForeclosureDate = ComponentFactory.getAutoDateField("foreclosure.date", false);
		dfForeclosureDate.setValue(DateUtils.today());
		
		// txtForeclosureLocation = ComponentFactory.getTextField("foreclosure.location", false, 50, 200);
		txtForeclosureStaffId = ComponentFactory.getTextField("foreclosure.staff.id", false, 50, 200);
		txtForeclosureStaffName = ComponentFactory.getTextField("foreclosure.staff.name", false, 50, 200);
		// txtForeclosureStaffPhone = ComponentFactory.getTextField("foreclosure.staff.phone", false, 50, 200);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSubmit);
		navigationPanel.addButton(btnCancel);
		
		FormLayout formLayout = new FormLayout();
		formLayout.addComponent(dfForeclosureDate);
		// formLayout.addComponent(txtForeclosureLocation);
		formLayout.addComponent(txtForeclosureStaffId);
		formLayout.addComponent(txtForeclosureStaffName);
		// formLayout.addComponent(txtForeclosureStaffPhone);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		verticalLayout.addComponent(navigationPanel);
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
			SecUser secUser = UserSessionManager.getCurrentUser();
			txtForeclosureStaffId.setValue(secUser.getLogin());
			txtForeclosureStaffName.setValue(secUser.getDesc());
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
			if (dfForeclosureDate.getValue() != null) {
				LossService lossService = SpringUtils.getBean(LossService.class);
				LossSimulateRequest simulateRequest = new LossSimulateRequest();
				simulateRequest.setCotraId(contract.getId());
				simulateRequest.setEventDate(dfForeclosureDate.getValue());
				LossSimulateResponse simulateResponse = lossService.simulate(simulateRequest);
			
				LossValidateRequest validateRequest = new LossValidateRequest();
				validateRequest.setCotraId(contract.getId());
				validateRequest.setWkfStatus(ContractWkfStatus.REP);					
            	validateRequest.setEventDate(simulateResponse.getEventDate());
            	validateRequest.setTotalInterest(simulateResponse.getTotalInterest());
            	validateRequest.setTotalPrincipal(simulateResponse.getTotalPrincipal());
            	validateRequest.setTotalOther(simulateResponse.getTotalOther());
            	validateRequest.setInsuranceFee(simulateResponse.getInsuranceFee());
            	validateRequest.setServicingFee(simulateResponse.getServicingFee());
            	validateRequest.setCashflows(simulateResponse.getCashflows());
            	lossService.validate(validateRequest);
				
				Notification notification = new Notification("", Type.HUMANIZED_MESSAGE);
				notification.setDescription(I18N.message("success.change.status.contract"));
				notification.setDelayMsec(3000);
				notification.show(Page.getCurrent());	
				close();
			
			}
		} else if (event.getButton() == btnCancel) {
			close();
		}
	}

}
